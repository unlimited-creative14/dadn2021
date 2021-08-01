package com.hk203.dadn.ui.admin_listuser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.hk203.dadn.R;
import com.hk203.dadn.models.AdminPatient;
import com.hk203.dadn.models.Patient;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ListBindedPatientAdapter extends ArrayAdapter<AdminPatient> {

    int res;

    public ListBindedPatientAdapter(@NonNull Context context, int resource, @NonNull AdminPatient[] objects) {
        super(context, resource, objects);
        res = resource;
    }

    public ListBindedPatientAdapter(@NonNull Context context, int resource, @NonNull List<AdminPatient> objects) {
        super(context, resource, objects);
        res = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder vh;
        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(res, parent, false);

            vh = new ViewHolder();
            vh.btnPatientName = convertView.findViewById(R.id.btn_patient_name);
            vh.btnUnbindPatient = convertView.findViewById(R.id.btn_unbind_patient);

            convertView.setTag(vh);
        }
        else
            vh = (ViewHolder) convertView.getTag();

        Patient p = getItem(position);
        vh.btnPatientName.setText(p.first_name + " " + p.last_name);

        vh.btnPatientName.setOnClickListener(v -> {
            final AppCompatActivity activity = (AppCompatActivity)getContext();
            FragmentManager fm = activity.getSupportFragmentManager();

        });
        return convertView;
    }

    class ViewHolder
    {
        Button btnPatientName;
        ImageButton btnUnbindPatient;
    }
}
