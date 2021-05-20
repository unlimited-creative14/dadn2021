package com.hk203.dadn.ui.patient_info;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.hk203.dadn.R;
import com.hk203.dadn.databinding.FragmentPatientInfoBinding;
import com.hk203.dadn.databinding.FragmentPatientListBinding;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPatientInfoBinding.inflate(getLayoutInflater());

        lc_temp = binding.lcTemp;
        List<Entry> entries = new ArrayList<>();
        LineDataSet dataSet = new LineDataSet(entries,"oC");
        LineData lineData = new LineData(dataSet);
        lc_temp.setData(lineData);


        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<50;i++){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addEntry();
                        }
                    });
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void addEntry(){
        LineData data = lc_temp.getLineData();
        Random rd = new Random();
        data.addEntry(new Entry(data.getEntryCount(), rd.nextInt(10)),0);
        lc_temp.notifyDataSetChanged();
        lc_temp.setVisibleXRange(0,6);
        lc_temp.moveViewToX(data.getEntryCount()-6);
    }
}