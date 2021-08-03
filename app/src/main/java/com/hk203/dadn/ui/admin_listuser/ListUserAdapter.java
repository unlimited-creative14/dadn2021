package com.hk203.dadn.ui.admin_listuser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.hk203.dadn.R;

import androidx.annotation.NonNull;

import com.hk203.dadn.models.AdminUserProfile;

import java.util.List;

public class ListUserAdapter extends ArrayAdapter<AdminUserProfile> {
    private int res;
    public ListUserAdapter(@NonNull Context context, int resource, @NonNull AdminUserProfile[] objects) {
        super(context, resource, objects);
        res = resource;
    }

    public ListUserAdapter(@NonNull Context context, int resource, @NonNull List<AdminUserProfile> objects) {
        super(context, resource, objects);
        res = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(res, parent, false);

            vh = new ViewHolder();
            vh.doctorNameBtn = convertView.findViewById(R.id.btn_doctor_name);
            convertView.setTag(vh);
        }
        else
            vh = (ViewHolder) convertView.getTag();
        AdminUserProfile p = getItem(position);
        if (p.role == 1)
        {
            vh.doctorNameBtn.setText("ID:"+Integer.toString(p.id) + " " + p.first_name + " " + p.last_name + "(QTV)");
        }
        else
            vh.doctorNameBtn.setText("ID:"+Integer.toString(p.id) + " " + p.first_name + " " + p.last_name);

        return convertView;
    }

    class ViewHolder{
        Button doctorNameBtn;
    }
}
