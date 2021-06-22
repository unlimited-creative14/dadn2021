package com.hk203.dadn.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.hk203.dadn.R;
import com.hk203.dadn.databinding.FragmentNotificationListBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class NotificationListFragment extends Fragment {
    private FragmentNotificationListBinding binding;
    private List<Notification> notificationList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationListBinding.inflate(getLayoutInflater());

        //set up list view notification
        getNotificationListFromServer();
        NotificationListAdapter adapter = new NotificationListAdapter(
                getContext(),
                R.layout.adapter_notification_list,
                notificationList
        );
        binding.lvNotifications.setAdapter(adapter);

        return binding.getRoot();
    }

    private void getNotificationListFromServer(){
        notificationList.add(new Notification(new Date(),"notification 1","1"));
        notificationList.add(new Notification(new Date(),"notification 2","2"));
    }
}
