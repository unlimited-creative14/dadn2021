package com.hk203.dadn.ui.updatehealthrule;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.hk203.dadn.R;
import com.hk203.dadn.databinding.FragmentUpdateHealthRuleBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateHealthRuleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateHealthRuleFragment extends Fragment {
    UpdateHealthRuleViewModel updateHealthRuleViewModel;
    private FragmentUpdateHealthRuleBinding binding;


    public UpdateHealthRuleFragment() {
        // Required empty public constructor

    }

    public static UpdateHealthRuleFragment newInstance() {
        UpdateHealthRuleFragment fragment = new UpdateHealthRuleFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        updateHealthRuleViewModel =
                new ViewModelProvider(this).get(UpdateHealthRuleViewModel.class);

        binding = FragmentUpdateHealthRuleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // Inflate the layout for this fragment
        createItem(root);
        return root;
    }

    void createItem(View view)
    {
        ExpandableListView expandableListView;
        ExpandableListAdapter adapter = new RulesExpandableListAdapter(getActivity(), updateHealthRuleViewModel.getRules().getValue());

        expandableListView = (ExpandableListView) view.findViewById(R.id.expandable_list_rule);
        expandableListView.setAdapter(adapter);
    }

}