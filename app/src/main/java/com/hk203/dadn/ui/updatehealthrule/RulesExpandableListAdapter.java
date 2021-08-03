package com.hk203.dadn.ui.updatehealthrule;
import android.widget.ArrayAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hk203.dadn.MainActivity;
import com.hk203.dadn.R;
import com.hk203.dadn.databinding.ViewHealthRuleBinding;
import com.hk203.dadn.repositories.IoTHealthCareRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RulesExpandableListAdapter extends BaseExpandableListAdapter {
    Activity context;
    List<HealthRule> rules;
    private final IoTHealthCareRepository repo = IoTHealthCareRepository.getInstance();

    public RulesExpandableListAdapter(Activity context, List<HealthRule> rules) {
        this.context = context;
        this.rules = rules;
    }

    @Override
    public int getGroupCount() {
        return rules.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return rules.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return ((HealthRule)getGroup(groupPosition)).warning_level;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = getGroup(groupPosition).toString();
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.view_expandable_item, null);
        }
        TextView listTitleTextView = convertView
                .findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        HealthRule item = (HealthRule) getGroup(groupPosition);

        if (convertView == null) {

            convertView = ViewHealthRuleBinding.inflate(context.getLayoutInflater()).getRoot();
        }

        setLedColor(item, convertView);
        setDataText(item, convertView);

        return convertView;
    }

    void setDataText(HealthRule item, View view)
    {
        ViewHealthRuleBinding binding = ViewHealthRuleBinding.bind(view);

        binding.warningLevelTv.setText(item.toString());
        binding.etTempFrom.setText(Float.toString(item.temp_from));
        binding.etTempTo.setText(Float.toString(item.temp_to));
        binding.etDuration.setText(Float.toString(item.duration));

        binding.btnUpdateRule.setOnClickListener( v -> {
            item.temp_from = Float.parseFloat(binding.etTempFrom.getText().toString());
            item.temp_to = Float.parseFloat(binding.etTempTo.getText().toString());
            item.duration = Float.parseFloat(binding.etDuration.getText().toString());

            Gson gson = new Gson();
            RequestBody body = RequestBody.create(gson.toJson(item), MediaType.parse("application/json"));
            repo.updateHealthRule(((MainActivity) context).getAuthToken(), body, new Callback<Map<String, String>>() {
                @Override
                public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                    Toast.makeText(context, response.body().getOrDefault("message", "Unknown error"), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Map<String, String>> call, Throwable t) {

                }
            });
        });
    }

    void setLedColor(HealthRule item, View view)
    {
        int color;
        switch (item.warning_level){
            case HealthRule.RED_LED:
                color = Color.RED;
                break;
            case HealthRule.GREEN_LED:
                color = Color.GREEN;
                break;
            case HealthRule.YELLOW_LED:
                color = Color.YELLOW;
                break;
            default:
                color = Color.DKGRAY;
        }

        ImageView led = view.findViewById(R.id.led_preview);
        led.setBackgroundColor(color);
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
