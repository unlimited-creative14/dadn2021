package com.hk203.dadn.ui.patient_info;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.hk203.dadn.R;
import com.hk203.dadn.models.Treatment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class TreatmentHistoryAdapter extends ArrayAdapter<Treatment> {
    private Context context;
    private int resource;
    private static final DateFormat timeFormat = new SimpleDateFormat("dd MMM, h:mm a", Locale.US);
    private static final SimpleDateFormat originFormat = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            Locale.US
    );

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
                getItem(position).getTreatmentDes()
        );
        try {
            ((TextView)convertView.findViewById(R.id.tv_datetime)).setText(
                    timeFormat.format(originFormat.parse(getItem(position).last_modified))
            );
        } catch (ParseException e) {
            Log.d("Exc",e.toString());
        }
        return convertView;
    }
}
