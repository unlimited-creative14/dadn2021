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
import com.google.gson.GsonBuilder;
import com.hk203.dadn.MQTTService;
import com.hk203.dadn.R;
import com.hk203.dadn.databinding.FragmentAdafruitListMessageBinding;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
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

    @Override
    public void onStop() {
        super.onStop();

        try {
            svc.mqttAndroidClient.disconnect();
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        svc.mqttAndroidClient.unregisterResources();
    }

    void createList(View view)
    {
        svc.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                connected = true;
                Toast.makeText(getContext(), "Connected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void connectionLost(Throwable cause) {
                connected = false;
                Toast.makeText(
                        getContext(),
                        "Disconnected! " + ((cause == null) ? "null" : cause.getMessage()) ,
                        Toast.LENGTH_SHORT
                ).show();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String test = message.toString();
                boolean isJson = true;
                JSONObject obj = null;
                try {
                    obj = new JSONObject(test);
                } catch (JSONException ex) {
                    // edited, to include @Arthur's comment
                    // e.g. in case JSONArray is valid as well...
                    try {
                        new JSONArray(test);
                    } catch (JSONException ex1) {
                        isJson = false;
                    }
                }

                if (isJson)
                {
                    LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.multext_title_text_view, null);
                    ((TextView)layout.findViewById(R.id.title_text_view)).setText("JSON");
                    Iterator<String> keys = obj.keys();
                    while (keys.hasNext())
                    {
                        String key = keys.next();
                        if (true) {
                            TextView tv = (TextView) getLayoutInflater().inflate(R.layout.single_text_view, null);
                            tv.setText(key + ":" + obj.get(key).toString());
                            tv.setPadding(60, 0,0,0);
                            layout.addView(tv);
                        }
                    }
                    binding.messageContainer.addView(layout);
                }
                else
                {
                    TextView tv = (TextView) getLayoutInflater().inflate(R.layout.single_text_view, null);
                    tv.setText(message.toString());

                    binding.messageContainer.addView(tv);
                }
                binding.scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
}