package com.hk203.dadn.ui.patientlist;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.hk203.dadn.R;
import java.util.List;

public class PatientStatusListAdapter extends ArrayAdapter<String> implements SpinnerAdapter {
    private Context context;
    private int resource;

    public PatientStatusListAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource,parent,false);
        TextView tv = (TextView)convertView.findViewById(R.id.tv_status_adapter);
        tv.setText(getItem(position));
        if (getItem(position).equals("Recovery")) {
            tv.setTextColor(ContextCompat.getColor(getContext(),R.color.green));
        }
        else if (getItem(position).equals("Incubation")){
            tv.setTextColor(ContextCompat.getColor(getContext(),R.color.yellow));
        }
        else if (getItem(position).equals("Febrile")) {
            tv.setTextColor(ContextCompat.getColor(getContext(),R.color.orange));
        }
        else if (getItem(position).equals("Emergency")) {
            tv.setTextColor(ContextCompat.getColor(getContext(),R.color.red));
        }
        return convertView;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource,parent,false);
        TextView tv = (TextView)convertView.findViewById(R.id.tv_status_adapter);
        tv.setText(getItem(position));
        if (getItem(position).equals("Recovery")) {
            tv.setTextColor(ContextCompat.getColor(getContext(),R.color.green));
        }
        else if (getItem(position).equals("Incubation")){
            tv.setTextColor(ContextCompat.getColor(getContext(),R.color.yellow));
        }
        else if (getItem(position).equals("Febrile")) {
            tv.setTextColor(ContextCompat.getColor(getContext(),R.color.orange));
        }
        else if (getItem(position).equals("Emergency")) {
            tv.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
        }
        tv.setPadding(15,0,15,15);
        return convertView;
    }
}
