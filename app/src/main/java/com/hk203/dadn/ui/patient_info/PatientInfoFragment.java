package com.hk203.dadn.ui.patient_info;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hk203.dadn.MQTTService;
import com.hk203.dadn.R;
import com.hk203.dadn.databinding.FragmentPatientInfoBinding;
import com.hk203.dadn.databinding.FragmentPatientListBinding;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class PatientInfoFragment extends Fragment {
    private FragmentPatientInfoBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    MQTTService svc;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPatientInfoBinding.inflate(getLayoutInflater());
        MqttCallbackExtended cb = new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                //ToDO:
                try {
                    svc.sendData("Hello, i am Long");
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        };

        svc = new MQTTService(
                getActivity(),
                "hoangkim",
                "aio_wxbW0127KrpCclPXXdw3811V8hNG",
                "hoangkim/feeds/test1",
                cb
        );

        return binding.getRoot();
    }
}