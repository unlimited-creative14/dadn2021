package com.hk203.dadn.ui.patientlist;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.hk203.dadn.R;

import java.util.List;

public class PatientListAdapter extends ArrayAdapter<Patient> {
    private Context context;
    private int resource;

    public PatientListAdapter(Context context, int resource, List<Patient> objects) {
        super(context,resource,objects);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource,parent,false);
        ((TextView)convertView.findViewById(R.id.tv_number)).setText(String.valueOf(position+1));
        ((TextView)convertView.findViewById(R.id.tv_name)).setText(getItem(position).getName());
        TextView tvStatus = (TextView)convertView.findViewById(R.id.tv_status);
        tvStatus.setText(getItem(position).getStatus());
        if (getItem(position).getStatus().equals("Recovery")) {
            tvStatus.setTextColor(ContextCompat.getColor(getContext(),R.color.green));
        }
        else if (getItem(position).getStatus().equals("Incubation")){
            tvStatus.setTextColor(ContextCompat.getColor(getContext(),R.color.yellow));
        }
        else if (getItem(position).getStatus().equals("Febrile")) {
            tvStatus.setTextColor(ContextCompat.getColor(getContext(),R.color.orange));
        }
        else if (getItem(position).getStatus().equals("Emergency")) {
            tvStatus.setTextColor(ContextCompat.getColor(getContext(),R.color.red));
        }
        return convertView;
    }
}