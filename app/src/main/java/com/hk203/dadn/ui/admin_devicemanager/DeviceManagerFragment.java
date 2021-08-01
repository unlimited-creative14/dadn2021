package com.hk203.dadn.ui.admin_devicemanager;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hk203.dadn.MainActivity;
import com.hk203.dadn.R;
import com.hk203.dadn.databinding.DeviceManagerFragmentBinding;
import com.hk203.dadn.databinding.DialogAddDeviceBinding;
import com.hk203.dadn.repositories.IoTHealthCareRepository;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceManagerFragment extends Fragment {

    private DeviceManagerViewModel mViewModel;
    private DeviceManagerFragmentBinding binding;
    private IoTHealthCareRepository repo = IoTHealthCareRepository.getInstance();
    public static DeviceManagerFragment newInstance() {
        return new DeviceManagerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DeviceManagerFragmentBinding.inflate(inflater);
        mViewModel = new ViewModelProvider(this).get(DeviceManagerViewModel.class);

        mViewModel.getDevices().observe(getViewLifecycleOwner(), devices -> {
            DeviceListAdapter adapter = new DeviceListAdapter(getContext(), R.layout.device_item, devices);
            binding.deviceList.setAdapter(adapter);
        });

        mViewModel.loadDevices(
                ((MainActivity)getActivity()).getAuthToken(),
                () -> binding.tvLoading.setVisibility(View.GONE)
        );

        binding.fabAddDevice.setOnClickListener(v -> {
            AlertDialog.Builder builder
                    = new AlertDialog
                    .Builder(getContext());
            DialogAddDeviceBinding binding = DialogAddDeviceBinding.inflate(inflater);
            builder.setView(binding.getRoot());
            builder.setTitle("Thêm thiết bị mqtt");
            builder.setCancelable(true);

            builder.setPositiveButton("Thêm", (dialog, which) -> {
                Map<String, String> mp = new HashMap<>();
                mp.put("feed_in", binding.etFeedIn.getText().toString());
                mp.put("feed_out", binding.etFeedOut.getText().toString());

                Gson gson = new Gson();
                RequestBody body = RequestBody.create(gson.toJson(mp), MediaType.parse("application/json"));

                repo.createDevice(
                        ((MainActivity) getActivity()).getAuthToken(),
                        body,
                        new Callback<Map<String, String>>() {
                            @Override
                            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                                mViewModel.loadDevices(
                                        ((MainActivity)getActivity()).getAuthToken(),
                                        () -> dialog.dismiss()
                                );
                                Toast.makeText(getContext(), response.body().getOrDefault("message", "Unknown error"), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Map<String, String>> call, Throwable t) {

                            }
                        }
                );
            });
            builder.create().show();
        });
        return binding.getRoot();
    }


}