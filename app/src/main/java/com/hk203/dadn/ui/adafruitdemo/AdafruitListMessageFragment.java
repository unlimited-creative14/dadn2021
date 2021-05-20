package com.hk203.dadn.ui.adafruitdemo;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialDialogs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hk203.dadn.MQTTService;
import com.hk203.dadn.R;
import com.hk203.dadn.databinding.FragmentAdafruitListMessageBinding;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdafruitListMessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdafruitListMessageFragment extends Fragment {
    Bundle bundle;
    FragmentAdafruitListMessageBinding binding;
    MQTTService svc;
    public AdafruitListMessageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment AdafruitListMessageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdafruitListMessageFragment newInstance() {
        AdafruitListMessageFragment fragment = new AdafruitListMessageFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bundle = getArguments();
        // Inflate the layout for this fragment
        binding = FragmentAdafruitListMessageBinding.inflate(getLayoutInflater());
        svc = new MQTTService(
                getContext(),
                bundle.getString("adaUsername"),
                bundle.getString("adaIOKey"),
                bundle.getString("adaTopic"),
                MQTTService.default_callback
        );
        createList(binding.getRoot());
        binding.fabSendMqttmessage.setOnClickListener(this::fabSendMessage);
        return binding.getRoot();
    }
    boolean connected = false;
    void fabSendMessage(View v)
    {
        if(!connected)
        {
            Toast.makeText(getContext(), "Not connected!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            SendMQTTMessageDialogFragment df = SendMQTTMessageDialogFragment.newInstance(svc);
            df.show(getParentFragmentManager(), "dft");
        }
    }

    void createList(View view)
    {
        svc.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                connected = true;
            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                TextView tv = (TextView) getLayoutInflater().inflate(R.layout.single_text_view, null);
                tv.setText(message.toString());

                binding.messageContainer.addView(tv);
                binding.scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
}