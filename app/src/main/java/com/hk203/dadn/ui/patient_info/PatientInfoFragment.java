package com.hk203.dadn.ui.patient_info;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.hk203.dadn.MQTTService;
import com.hk203.dadn.R;
import com.hk203.dadn.databinding.FragmentPatientInfoBinding;
import com.hk203.dadn.databinding.FragmentPatientListBinding;
import com.hk203.dadn.ui.patientlist.Patient;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        setUpTemperatureChart();
        setUpMqttService();
        setUpClickTreatmentHistoryHandler();
        setUpClickAddTreatmentHandler();
    }

    private void setUpTemperatureChart() {
        lc_temp = binding.lcTemp;
        lc_temp.setDrawBorders(false);
        lc_temp.setDescription(null);
        lc_temp.setDrawGridBackground(false);
        lc_temp.getLegend().setEnabled(false);
        lc_temp.getAxisLeft().setEnabled(false);
        lc_temp.getAxisRight().setEnabled(false);
        lc_temp.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        lc_temp.getXAxis().setTextSize(13);
        lc_temp.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if ((int)value>=xAxisValues.size()){
                    return "";
                }
                else{
                    return xAxisValues.get((int)value);
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
        dataSet.setValueTextSize(13);
        dataSet.setValueTextColor(Color.BLUE);
        LineData lineData = new LineData(dataSet);
        lineData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return decimalFormat.format(value);
            }
        });
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
            updateChart(Float.valueOf(message.toString()));
            /*JSONObject jsonObject = new JSONObject(message.toString());
            String data = jsonObject.getString("data");
            float temp = Float.valueOf(data.split("-")[0]);*/
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {

        }
    };

    private void setUpClickTreatmentHistoryHandler(){
        binding.tvTrm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_treatment_history);
                String[] trm = {"trm 1","trm 2", "trm 3"};
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        getContext(),
                        android.R.layout.simple_list_item_1,
                        trm
                );
                ((ListView)dialog.findViewById(R.id.lv_treatment_history)).setAdapter(adapter);
                dialog.setCancelable(true);
                dialog.getWindow().setLayout(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT
                );
                dialog.show();
                dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

            }
        });
    }

    private void setUpClickAddTreatmentHandler(){
        binding.imAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_add_treatment);
                dialog.setCancelable(true);
                setUpConfirmAddTreatmentHandler(dialog);
                setUpCancelAddTreatmentHandler(dialog);
                dialog.getWindow().setLayout(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT
                );
                dialog.show();
            }
        });
    }

    private void setUpConfirmAddTreatmentHandler(Dialog dialog){
        dialog.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ADD",((EditText)dialog.findViewById(R.id.et_new_treatment)).getText().toString());
                dialog.cancel();
            }
        });
    }

    private void setUpCancelAddTreatmentHandler(Dialog dialog){
        dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }
}