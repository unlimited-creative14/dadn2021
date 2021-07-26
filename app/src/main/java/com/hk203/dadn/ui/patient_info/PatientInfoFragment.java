package com.hk203.dadn.ui.patient_info;

import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.gson.Gson;
import com.hk203.dadn.MQTTService;
import com.hk203.dadn.R;
import com.hk203.dadn.databinding.FragmentPatientInfoBinding;
import com.hk203.dadn.ui.patientlist.Patient;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PatientInfoFragment extends Fragment {
    private static final DecimalFormat decimalFormat = new DecimalFormat(".00");
    private static final DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
    private FragmentPatientInfoBinding binding;
    private LineChart lc_temp;
    private ArrayList<String> xAxisValues = new ArrayList<>();
    private MQTTService mqttService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Msg", ((Patient)getArguments().getSerializable("patient")).getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPatientInfoBinding.inflate(getLayoutInflater());
        binding.tvTrm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(
                        getActivity(),
                        R.id.nav_host_fragment_content_main
                );
                controller.navigate(R.id.action_nav_patient_info_to_treatmentHistoryFragment);
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        setUpTemperatureChart();
        setUpMqttService();
    }

    private void setUpTemperatureChart() {
        lc_temp = binding.lcTemp;
        lc_temp.setDrawBorders(false);
        lc_temp.setDescription(null);
        lc_temp.setDrawGridBackground(false);
        lc_temp.getLegend().setEnabled(false);
        /*lc_temp.getAxisLeft().setEnabled(false);*/
        lc_temp.getAxisLeft().setTextSize(13);
        lc_temp.getAxisLeft().setTextColor(Color.BLACK);
        lc_temp.getAxisRight().setEnabled(false);
        lc_temp.getXAxis().setDrawGridLines(true);
        lc_temp.getXAxis().setGridColor(ContextCompat.getColor(getContext(),R.color.orange));
        lc_temp.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        lc_temp.getXAxis().setTextSize(13);
        lc_temp.getXAxis().setTextColor(Color.BLACK);
        lc_temp.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int)value;
                if (index<xAxisValues.size() && index%2==0){
                    return xAxisValues.get(index);
                }
                else{
                    return "";
                }
            }
        });
        List<Entry> entries = new ArrayList<>();
        LineDataSet dataSet = new LineDataSet(entries, null);
        dataSet.setLineWidth(3);
        dataSet.setColor(ContextCompat.getColor(getContext(),R.color.orange));
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addEntry(newTemp);
                    }
                });
            }
        }).start();
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
            TempData data = gson.fromJson(message.toString(),TempData.class);
            updateChart(Float.parseFloat(data.getData().split("-")[0]));
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {

        }
    };
}