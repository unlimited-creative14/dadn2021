package com.hk203.dadn.ui.patient_info;

import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.gson.Gson;
import com.hk203.dadn.MQTTService;
import com.hk203.dadn.MainActivity;
import com.hk203.dadn.R;
import com.hk203.dadn.databinding.FragmentPatientInfoBinding;
import com.hk203.dadn.models.Patient;
import com.hk203.dadn.viewmodels.PatientDetailViewModel;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class PatientInfoFragment extends Fragment {
    private static final DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
    private FragmentPatientInfoBinding binding;
    private LineChart lc_temp;
    private final ArrayList<String> xAxisValues = new ArrayList<>();
    private MQTTService mqttService;
    private int patientId;
    private PatientDetailViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        patientId = getArguments().getInt("patientId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPatientInfoBinding.inflate(getLayoutInflater());

        viewModel = new ViewModelProvider(this).get(PatientDetailViewModel.class);

        binding.tvTrm.setOnClickListener(v -> {
            NavController controller = Navigation.findNavController(
                    getActivity(),
                    R.id.nav_host_fragment_content_main
            );
            Bundle bundle = new Bundle();
            bundle.putInt("patientId", patientId);
            controller.navigate(R.id.action_nav_patient_info_to_treatmentHistoryFragment, bundle);
        });

        viewModel.getPatientDetail().observe(getViewLifecycleOwner(), patientDetail -> {
            binding.tvFullName.setText(patientDetail.getName());
            binding.etDevId.setText(String.valueOf(patientDetail.dev_id));
            binding.tvPhone.setText(patientDetail.phone);
            binding.tvStatus.setText(patientDetail.getStatus());
            binding.tvStatus.setTextColor(patientDetail.getStatusColor());
            binding.tvPendingTreatment.setText(patientDetail.getPendingTreatment());
        });

        viewModel.getPutPatientInfoResponse().observe(getViewLifecycleOwner(), putPatientInfoResponse ->
                Toast.makeText(getContext(), putPatientInfoResponse.message, Toast.LENGTH_SHORT).show());

        viewModel.getErrorResponse().observe(getViewLifecycleOwner(), errorResponse -> {
            Toast.makeText(getContext(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
        });

        viewModel.loadPatientDetail(
                ((MainActivity) getActivity()).getAuthToken(),
                patientId
        );

        binding.etDevId.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    Log.d("CC",binding.etDevId.getText().toString());
                    viewModel.putPatientInfo(
                            ((MainActivity)getActivity()).getAuthToken(),
                            patientId,
                            Integer.parseInt(binding.etDevId.getText().toString())
                    );
                }
                return true;
            }
        });

        setUpTemperatureChart();
        setUpMqttService();

        return binding.getRoot();
    }

    private void setUpTemperatureChart() {
        lc_temp = binding.lcTemp;
        lc_temp.setDrawBorders(false);
        lc_temp.setDescription(null);
        lc_temp.setDrawGridBackground(false);
        lc_temp.getLegend().setEnabled(false);
        /*lc_temp.getAxisLeft().setEnabled(false);*/
        lc_temp.getAxisLeft().setTextSize(13);
        lc_temp.getAxisLeft().setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        lc_temp.getAxisRight().setEnabled(false);
        lc_temp.getXAxis().setDrawGridLines(true);
        lc_temp.getXAxis().setGridColor(ContextCompat.getColor(getContext(), R.color.orange));
        lc_temp.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        lc_temp.getXAxis().setTextSize(13);
        lc_temp.getXAxis().setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        lc_temp.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index < xAxisValues.size() && index % 2 == 0) {
                    return xAxisValues.get(index);
                } else {
                    return "";
                }
            }
        });
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 37));
        entries.add(new Entry(1, 37));
        entries.add(new Entry(2, 37));
        entries.add(new Entry(3, 37));
        LineDataSet dataSet = new LineDataSet(entries, null);
        dataSet.setLineWidth(3);
        dataSet.setColor(ContextCompat.getColor(getContext(), R.color.orange));
        dataSet.setDrawCircles(true);
        dataSet.setCircleColor(Color.GREEN);
        dataSet.setCircleRadius(4);
        dataSet.setValueTextSize(0);
        LineData lineData = new LineData(dataSet);
        lc_temp.setData(lineData);
    }

    private void addEntry(float newTemp) {
        LineData data = lc_temp.getLineData();
        data.addEntry(new Entry(data.getEntryCount(), newTemp), 0);
        lc_temp.notifyDataSetChanged();
        lc_temp.setVisibleXRange(0, 10);
        lc_temp.moveViewToX(data.getEntryCount() - 10);
    }

    private void updateChart(float newTemp) {
        new Thread(() -> getActivity().runOnUiThread(() -> addEntry(newTemp))).start();
    }

    private void setUpMqttService() {
        mqttService = new MQTTService(getContext(),
                "kimnguyenlong",
                "aio_mpYq21jymEgrHUlt5MLMNtZaCLnQ",
                "kimnguyenlong/feeds/temp", mqttCallback
        );
    }

    private final MqttCallbackExtended mqttCallback = new MqttCallbackExtended() {
        @Override
        public void connectComplete(boolean reconnect, String serverURI) {
            Log.w("Mqtt", "connect complete");
        }

        @Override
        public void connectionLost(Throwable cause) {
            Log.w("Mqtt", "connect lost");
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            Log.w("Mqtt", message.toString());
            xAxisValues.add(dateFormat.format(Calendar.getInstance().getTime()));
            Gson gson = new Gson();
            TempData data = gson.fromJson(message.toString(), TempData.class);
            updateChart(Float.parseFloat(data.getData().split("-")[0]));
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {

        }
    };
}