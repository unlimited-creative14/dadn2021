package com.hk203.dadn.ui.notifications;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hk203.dadn.R;
import com.hk203.dadn.databinding.FragmentNotificationsBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link notificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class notificationsFragment extends Fragment {
    FragmentNotificationsBinding binding;
    public notificationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment notificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static notificationsFragment newInstance(String param1, String param2) {
        notificationsFragment fragment = new notificationsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNotificationsBinding.inflate(getLayoutInflater());
        ListView notiflv = binding.getRoot().findViewById(R.id.notificationsListView);

        return binding.getRoot();
    }
}