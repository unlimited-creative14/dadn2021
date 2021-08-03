package com.hk203.dadn;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.lang.reflect.Array;

public class MQTTService {
    public static final String serverUri = "tcp://io.adafruit.com:1883";
    private final String subscriptionTopic;
    private final MqttAndroidClient mqttAndroidClient;

    public MQTTService(Context context, String username, String io_key, String topic, MqttCallbackExtended callback)
    {
        mqttAndroidClient = new MqttAndroidClient(context, serverUri, MqttClient.generateClientId());
        subscriptionTopic = topic;
        mqttAndroidClient.setCallback(callback);
        connect(username, io_key);
    }

    private void connect(String username, String io_key) {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(false);
        //mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(io_key.toCharArray());

        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener()
            {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    subscribeToTopic();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception)
                {
                    Log.w("Mqtt", "Failed to connect to:" +
                            serverUri + exception.toString());
                }
            });
        } catch (MqttException ex) {
            ex.printStackTrace();
        }
    }

    public void subscribeToTopic() {
        try {
            mqttAndroidClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener()
            {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.w("Mqtt", "Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,Throwable exception)
                {
                    Log.w("Mqtt", "Subscribed failed!");
                }
            });
        } catch (MqttException ex) {
            System.err.println("Exceptions to subscribing");
            ex.printStackTrace();
        }
    }

    public void unSubscribe(){
        try{
            Log.d("Mqtt","Unsubscribed");
            mqttAndroidClient.unsubscribe(subscriptionTopic);
        } catch (MqttException e) {
            Log.d("Mqtt","Unsubscribe failed!");
            e.printStackTrace();
        }
    }

    public void sendData(CharSequence data) throws MqttException {
        MqttMessage msg = new MqttMessage();
        msg.setPayload(data.toString().getBytes());
        try {
            mqttAndroidClient.publish(subscriptionTopic, msg);
        }
        catch (Exception e)
        {
            throw e;
        }

    }
}