package com.hk203.dadn.ui.admin_devicemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hk203.dadn.databinding.DeviceItemBinding;
import com.hk203.dadn.models.MqttDevice;
import com.hk203.dadn.ui.admin_listuser.ListBindedPatientAdapter;

import java.util.List;

public class DeviceListAdapter extends ArrayAdapter<MqttDevice> {
    int res;
    public DeviceListAdapter(@NonNull Context context, int resource, @NonNull MqttDevice[] objects) {
        super(context, resource, objects);
        res = resource;
    }

    public DeviceListAdapter(@NonNull Context context, int resource, @NonNull List<MqttDevice> objects) {
        super(context, resource, objects);
        res = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder vh;
        if(convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(res, parent, false);
            DeviceItemBinding binding =  DeviceItemBinding.bind(convertView);
            vh = new ViewHolder();

            vh.dev_id = binding.tvDeviceId;
            vh.dev_feed_in = binding.tvDevFeedIn;
            vh.dev_feed_out = binding.tvDevFeedOut;

            convertView.setTag(vh);
        }
        else
            vh = (ViewHolder) convertView.getTag();

        vh.dev_id.setText("Thiết bị " + Integer.toString(getItem(position).dev_id));
        vh.dev_feed_in.setText(getItem(position).feed_in);
        vh.dev_feed_out.setText(getItem(position).feed_out);
        return  convertView;
    }

    class ViewHolder
    {
        TextView dev_id;
        TextView dev_feed_in, dev_feed_out;
    }
}
