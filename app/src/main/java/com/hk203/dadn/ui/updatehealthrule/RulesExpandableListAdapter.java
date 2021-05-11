package com.hk203.dadn.ui.updatehealthrule;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.hk203.dadn.R;

import java.util.ArrayList;

public class RulesExpandableListAdapter extends BaseExpandableListAdapter {
    Activity context;
    ArrayList<HealthRule> rules;

    public RulesExpandableListAdapter(Activity context, ArrayList<HealthRule> rules) {
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
        return ((HealthRule)getGroup(groupPosition)).getId();
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
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        HealthRule item = (HealthRule) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.view_health_rule, null);
        }

        setLedColor(item, convertView);
        setAllSpinner(item, convertView);
        setDataText(item, convertView);

        return convertView;
    }

    void setDataText(HealthRule item, View view)
    {
        EditText et_tempFrom, et_tempTo;
        EditText et_timeFrom, et_timeTo;

        et_tempFrom = view.findViewById(R.id.et_tempFrom);
        et_tempTo = view.findViewById(R.id.et_tempTo);
        et_timeFrom = view.findViewById(R.id.et_timeFrom);
        et_timeTo = view.findViewById(R.id.et_timeTo);


        et_tempFrom.setText(String.valueOf(item.getTempFrom()));
        et_tempTo.setText(String.valueOf(item.getTempTo()));
        et_timeFrom.setText(String.valueOf(item.getTimeFrom()));
        et_timeTo.setText(String.valueOf(item.getTimeTo()));
    }
    void setAllSpinner(HealthRule item, View view)
    {
        Spinner spinnerAction = (Spinner)view.findViewById(R.id.ruleActionsSpinner);
        ArrayAdapter<String> warningLevelAdapter = new ArrayAdapter<>(context, R.layout.view_spinner_item, HealthRule.getAllWarningLevel());
        warningLevelAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerAction.setAdapter(warningLevelAdapter);

        spinnerAction.setSelection(item.getWarningLevel());

        Spinner spinnerDegUnit = (Spinner)view.findViewById(R.id.ruleDegUnit);
        ArrayList<String> degUnitValues = new ArrayList<String>();
        degUnitValues.add("°C");
        degUnitValues.add("°F");
        ArrayAdapter<String> degUnitAdapter = new ArrayAdapter<>(context, R.layout.view_spinner_item, degUnitValues);
        degUnitAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerDegUnit.setAdapter(degUnitAdapter);

        Spinner spinnerTimeUnit = (Spinner)view.findViewById(R.id.ruleTimeUnit);
        ArrayList<String> timeUnitValues = new ArrayList<String>();
        timeUnitValues.add("phut");
        timeUnitValues.add("gio");
        ArrayAdapter<String> timeUnitAdapter = new ArrayAdapter<>(context, R.layout.view_spinner_item, timeUnitValues);
        timeUnitAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerTimeUnit.setAdapter(timeUnitAdapter);
    }
    void setLedColor(HealthRule item, View view)
    {
        int color;
        switch (item.getWarningLevel()){
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
