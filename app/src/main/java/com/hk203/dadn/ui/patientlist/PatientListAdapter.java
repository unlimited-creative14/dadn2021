package com.hk203.dadn.ui.patientlist;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.hk203.dadn.R;
import com.hk203.dadn.models.Patient;
import java.util.List;

public class PatientListAdapter extends ArrayAdapter<Patient> {
    private final Context context;
    private final int resource;

    public PatientListAdapter(Context context, int resource, List<Patient> objects) {
        super(context,resource,objects);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView==null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.tv_id = convertView.findViewById(R.id.tv_id);
            viewHolder.tv_name = convertView.findViewById(R.id.tv_name);
            viewHolder.tv_dev_id = convertView.findViewById(R.id.tv_dev_id);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }

        viewHolder.tv_id.setText(String.valueOf(getItem(position).pat_id));
        viewHolder.tv_name.setText(getItem(position).getName());
        String devId = getItem(position).dev_id == 0 ? "No data!" : String.valueOf(getItem(position).dev_id);
        viewHolder.tv_dev_id.setText(devId);

        return convertView;
    }

    private static class ViewHolder{
        TextView tv_id;
        TextView tv_name;
        TextView tv_dev_id;
    }
}