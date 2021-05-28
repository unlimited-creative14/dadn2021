package com.hk203.dadn.ui.patient_info;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.hk203.dadn.MQTTService;
import com.hk203.dadn.R;
import com.hk203.dadn.databinding.FragmentPatientInfoBinding;
import com.hk203.dadn.databinding.FragmentPatientListBinding;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PatientInfoFragment extends Fragment {
    private FragmentPatientInfoBinding binding;
    private LineChart lc_temp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    MQTTService svc;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPatientInfoBinding.inflate(getLayoutInflater());

        lc_temp = binding.lcTemp;
        List<Entry> entries = new ArrayList<>();
        LineDataSet dataSet = new LineDataSet(entries,"oC");
        LineData lineData = new LineData(dataSet);
        lc_temp.setData(lineData);

        MQTTService mqttService = new MQTTService(getContext());
        mqttService.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                Log.w("Mqtt","connect complete");
            }

            @Override
            public void connectionLost(Throwable cause) {
                Log.w("Mqtt","connect lost");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.w("Mqtt",message.toString());
                updateChart(Float.valueOf(message.toString()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void addEntry(float newTemp){
        LineData data = lc_temp.getLineData();
        data.addEntry(new Entry(data.getEntryCount(), newTemp),0);
        lc_temp.notifyDataSetChanged();
        lc_temp.setVisibleXRange(0,6);
        lc_temp.moveViewToX(data.getEntryCount()-6);
    }

    private void updateChart(float newTemp){
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
}