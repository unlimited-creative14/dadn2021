package com.hk203.dadn.ui.patient_info;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
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
import com.github.mikephil.charting.data.DataSet;
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
import com.hk203.dadn.models.PatientDetail;
import com.hk203.dadn.viewmodels.PatientDetailViewModel;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class PatientInfoFragment extends Fragment {
    private static final DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.US);
    private FragmentPatientInfoBinding binding;
    private final ArrayList<String> xAxisValues = new ArrayList<>();
    private int patientId;
    private String authToken;
    private int devId;
    private int newDevId;
    private PatientDetailViewModel viewModel;
    private MQTTService mqttService;
    private boolean isUnsubscribed;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        patientId = getArguments().getInt("patientId");
        authToken = ((MainActivity) getActivity()).getAuthToken();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPatientInfoBinding.inflate(getLayoutInflater());

        ((MainActivity) getActivity()).setToolbarTitle("Patient Info");

        viewModel = new ViewModelProvider(this).get(PatientDetailViewModel.class);

        viewModel.getPatientDetail().observe(getViewLifecycleOwner(), patientDetail -> {
            binding.tvFullName.setText(patientDetail.getName());
            binding.tvPhone.setText(patientDetail.phone);
            binding.tvStatus.setText(patientDetail.getStatus());
            binding.tvStatus.setTextColor(ContextCompat.getColor(getContext(), patientDetail.getStatusColor()));
            binding.tvPendingTreatment.setText(patientDetail.getPendingTreatment());
            devId = patientDetail.dev_id;
            binding.etDevId.setText(getDevIdText());
            if (patientDetail.dev_id != 0) {
                setUpTemperatureChart(patientDetail.tempHistory);
                viewModel.loadDeviceFeedInfo(
                        authToken,
                        devId
                );
            }
        });

        viewModel.getPutPatientInfoResponse().observe(getViewLifecycleOwner(), response -> {
            devId = newDevId;
            binding.etDevId.setText(getDevIdText());
            if (devId>0) {
                viewModel.loadDeviceFeedInfo(
                        authToken,
                        devId
                );
            }
            Toast.makeText(getContext(), response.message, Toast.LENGTH_SHORT).show();
        });

        viewModel.getFeedInfo().observe(getViewLifecycleOwner(), feedInfo -> setUpMqttService(
                feedInfo.username,
                feedInfo.iokey,
                feedInfo.feed_in
        ));

        viewModel.getErrorResponse().observe(getViewLifecycleOwner(), errorResponse -> {
            Toast.makeText(getContext(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
        });

        binding.etDevId.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.etDevId.getText().length() == 0) {
                    newDevId = 0;
                    viewModel.putPatientInfo(
                            ((MainActivity) getActivity()).getAuthToken(),
                            patientId,
                            0
                    );
                } else {
                    newDevId = Integer.parseInt(binding.etDevId.getText().toString());
                    if (newDevId > 0) {
                        viewModel.putPatientInfo(
                                ((MainActivity) getActivity()).getAuthToken(),
                                patientId,
                                newDevId
                        );
                    } else {
                        Toast.makeText(getContext(), "Device Id must greater than 0", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            return true;
        });

        binding.refreshDevId.setOnClickListener(v -> binding.etDevId.setText(getDevIdText()));

        binding.tvTrm.setOnClickListener(v -> {
            NavController controller = Navigation.findNavController(
                    getActivity(),
                    R.id.nav_host_fragment_content_main
            );
            Bundle bundle = new Bundle();
            bundle.putInt("patientId", patientId);
            controller.navigate(R.id.action_nav_patient_info_to_treatmentHistoryFragment, bundle);
        });

        viewModel.loadPatientDetail(authToken, patientId);

        return binding.getRoot();
    }

    private String getDevIdText() {
        return devId == 0 ? "no data!" : String.valueOf(devId);
    }

    private void setUpTemperatureChart(@Nullable List<PatientDetail.Temp> tempHistory) {
        xAxisValues.clear();

        binding.lcTemp.setDrawBorders(false);
        binding.lcTemp.setDescription(null);
        binding.lcTemp.getLegend().setEnabled(false);

        binding.lcTemp.getAxisLeft().setTextSize(13);
        binding.lcTemp.getAxisLeft().setTextColor(ContextCompat.getColor(getContext(), R.color.black));

        binding.lcTemp.getAxisRight().setEnabled(false);

        binding.lcTemp.getXAxis().setDrawGridLines(true);
        binding.lcTemp.getXAxis().setGridColor(ContextCompat.getColor(getContext(), R.color.orange));
        binding.lcTemp.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        binding.lcTemp.getXAxis().setTextSize(13);
        binding.lcTemp.getXAxis().setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        binding.lcTemp.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index < xAxisValues.size() && index >= 0) {
                    return xAxisValues.get(index);
                } else {
                    return "";
                }
            }
        });

        List<Entry> entries = new ArrayList<>();
        if (tempHistory != null && tempHistory.size() > 0) {
            SimpleDateFormat originFormat = new SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                    Locale.US
            );
            for (int i = tempHistory.size() - 1; i >= 0; i--) {
                PatientDetail.Temp temp = tempHistory.get(i);
                try {
                    xAxisValues.add(timeFormat.format(originFormat.parse(temp.recv_time)));
                } catch (ParseException e) {
                    Log.d("Exc", e.toString());
                }
                entries.add(new Entry(tempHistory.size() - i - 1, temp.temp_value));
            }
        }

        LineDataSet dataSet = new LineDataSet(entries, null);
        dataSet.setLineWidth(3);
        dataSet.setColor(ContextCompat.getColor(getContext(), R.color.orange));
        dataSet.setDrawCircles(true);
        dataSet.setCircleColor(Color.GREEN);
        dataSet.setCircleRadius(4);
        dataSet.setValueTextSize(0);

        LineData lineData = new LineData(dataSet);
        binding.lcTemp.setData(lineData);
        binding.lcTemp.setVisibleXRange(0, 10);
        binding.lcTemp.moveViewToX(0);
        binding.lcTemp.invalidate();
    }

    private void addEntry(float newTemp) {
        LineData data = binding.lcTemp.getLineData();
        data.addEntry(new Entry(data.getEntryCount(), newTemp), 0);
        binding.lcTemp.notifyDataSetChanged();
        binding.lcTemp.setVisibleXRange(0, 10);
        binding.lcTemp.moveViewToX(data.getEntryCount() - 10);
    }

    private void updateChart(float newTemp) {
        try {
            new Thread(() -> getActivity().runOnUiThread(() -> addEntry(newTemp))).start();
        }catch (Exception e){
            Log.d("Exc",e.toString());
        }
    }

    private void setUpMqttService(String userName, String ioKey, String topic) {
        if (mqttService != null){
            if (userName.equals(mqttService.getUsername())
                    && ioKey.equals(mqttService.getIoKey())
                    && topic.equals(mqttService.getSubscriptionTopic())){
                if (isUnsubscribed){
                    mqttService.subscribeToTopic();
                    return;
                }
            }else{
                mqttService.unSubscribe();
                mqttService = null;
                isUnsubscribed = true;
            }
        }
        mqttService = new MQTTService(getContext(),
                userName,
                ioKey,
                topic,
                new MqttCallbackExtended() {
                    @Override
                    public void connectComplete(boolean reconnect, String serverURI) {
                        Log.w("Mqtt", "connect complete");
                    }

                    @Override
                    public void connectionLost(Throwable cause) {
                        Log.w("Mqtt", "connect lost");
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) {
                        try {
                            Log.w("Mqtt", message.toString());
                            xAxisValues.add(timeFormat.format(Calendar.getInstance().getTime()));
                            Gson gson = new Gson();
                            TempData data = gson.fromJson(message.toString(), TempData.class);
                            float newTemp = Float.parseFloat(data.getData().split("-")[0]);
                            updateChart(newTemp);
                            updateInfo(newTemp);
                        } catch (Exception e) {
                            Log.d("Exc", e.toString());
                        }
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {

                    }
                }
        );
        isUnsubscribed = false;
    }

    private void updateInfo(float newTemp){
        if (newTemp <= 27) {
            binding.tvStatus.setText("Recovery");
            binding.tvStatus.setTextColor(ContextCompat.getColor(getContext(),R.color.green));
            binding.tvPendingTreatment.setText("Close monitoring");
        } else if (newTemp >= 27 && newTemp <= 27.5) {
            binding.tvStatus.setText("Incubation");
            binding.tvStatus.setTextColor(ContextCompat.getColor(getContext(),R.color.yellow));
            binding.tvPendingTreatment.setText("Chest X-ray");
        } else if (newTemp >= 27.5 && newTemp <= 28) {
            binding.tvStatus.setText("Febrile");
            binding.tvStatus.setTextColor(ContextCompat.getColor(getContext(),R.color.orange));
            binding.tvPendingTreatment.setText("Monitoring for warning signs");
        } else {
            binding.tvStatus.setText("Emergency");
            binding.tvStatus.setTextColor(ContextCompat.getColor(getContext(),R.color.red));
            binding.tvPendingTreatment.setText("Measure Hematocrit");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mqttService != null) {
            mqttService.unSubscribe();
            isUnsubscribed = true;
        }
    }
}