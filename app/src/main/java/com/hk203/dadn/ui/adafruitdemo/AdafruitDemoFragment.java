package com.hk203.dadn.ui.adafruitdemo;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hk203.dadn.MQTTService;
import com.hk203.dadn.R;
import com.hk203.dadn.databinding.FragmentAdafruitDemoBinding;

public class AdafruitDemoFragment extends Fragment {

    FragmentAdafruitDemoBinding binding;
    public static AdafruitDemoFragment newInstance() {
        return new AdafruitDemoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAdafruitDemoBinding.inflate(getLayoutInflater());
        binding.btnStartSubfeed.setOnClickListener(this::onBtnStartClicked);
        binding.btnFillDefault.setOnClickListener(this::fillDefault);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    void fillDefault(View v)
    {
//        binding.etServerUrl.setText(MQTTService.serverUri);
//        binding.etAdaUsername.setText(MQTTService.default_username);
//        binding.etAdaIoKey.setText(MQTTService.default_io_key);
//        binding.etAdaTopic.setText(MQTTService.default_subscriptionTopic);

        binding.etServerUrl.setText(MQTTService.serverUri);
        binding.etAdaUsername.setText("pipe1404");
        binding.etAdaIoKey.setText("aio_vSSW67Hkla1acSRfrqbIXTGWHpy1");
        binding.etAdaTopic.setText("pipe1404/feeds/led-zz");
    }
    void onBtnStartClicked(View v)
    {
        Bundle bundle = new Bundle();
        bundle.putString("serverUrl", binding.etServerUrl.getText().toString());
        bundle.putString("adaUsername", binding.etAdaUsername.getText().toString());
        bundle.putString("adaIOKey", binding.etAdaIoKey.getText().toString());
        bundle.putString("adaTopic", binding.etAdaTopic.getText().toString());

        NavController controller = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
        controller.navigate(R.id.action_nav_adafruit_demo_to_nav_adafruit_list_message, bundle);
    }

}