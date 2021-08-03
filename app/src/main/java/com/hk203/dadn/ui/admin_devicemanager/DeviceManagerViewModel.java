package com.hk203.dadn.ui.admin_devicemanager;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hk203.dadn.models.MqttDevice;
import com.hk203.dadn.repositories.IoTHealthCareRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceManagerViewModel extends ViewModel {
    private final IoTHealthCareRepository repo = IoTHealthCareRepository.getInstance();
    private MutableLiveData<List<MqttDevice>> devices;

    public MutableLiveData<List<MqttDevice>> getDevices()
    {
        if (devices == null)
            devices = new MutableLiveData<>();
        return devices;
    }

    public void loadDevices(String token, Runnable cb)
    {
        repo.listAllDevice(token, new Callback<List<MqttDevice>>() {
            @Override
            public void onResponse(Call<List<MqttDevice>> call, Response<List<MqttDevice>> response) {
                devices.setValue(response.body());
                cb.run();
            }

            @Override
            public void onFailure(Call<List<MqttDevice>> call, Throwable t) {

            }
        });
    }
}