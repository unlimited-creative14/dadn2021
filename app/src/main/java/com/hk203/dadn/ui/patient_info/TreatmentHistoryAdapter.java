package com.hk203.dadn.ui.patient_info;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.hk203.dadn.R;
import com.hk203.dadn.models.Treatment;

import java.util.List;


public class TreatmentHistoryAdapter extends ArrayAdapter<Treatment> {
    private Context context;
    private int resource;

    public TreatmentHistoryAdapter(Context context, int resource, List<Treatment> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView,ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource,parent,false);
        ((TextView)convertView.findViewById(R.id.tv_treatment)).setText(
                String.valueOf(getItem(position).treatment_id)
        );
        ((TextView)convertView.findViewById(R.id.tv_datetime)).setText(
                "no data"
        );
        return convertView;
    }
}
