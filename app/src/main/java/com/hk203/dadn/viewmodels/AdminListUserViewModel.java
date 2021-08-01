package com.hk203.dadn.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hk203.dadn.models.AdminUserProfile;
import com.hk203.dadn.models.ErrorResponse;
import com.hk203.dadn.models.UserLoginResponse;
import com.hk203.dadn.models.UserProfile;
import com.hk203.dadn.repositories.IoTHealthCareRepository;
import com.hk203.dadn.utils.ErrorUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminListUserViewModel extends ViewModel {
    private final IoTHealthCareRepository repo = IoTHealthCareRepository.getInstance();

    public MutableLiveData<List<AdminUserProfile>> getUserList() {
        if (userList == null)
            userList = new MutableLiveData<>(new ArrayList<>());
        return userList;
    }

    private MutableLiveData<List<AdminUserProfile>> userList;
    public void loadUserList(String token, Runnable failureCb, Runnable successCb)
    {
        repo.listUser(token, new Callback<List<AdminUserProfile>>() {
            @Override
            public void onResponse(Call<List<AdminUserProfile>> call, Response<List<AdminUserProfile>> response) {
                if(response.isSuccessful())
                {
                    userList.postValue(response.body());
                    successCb.run();
                }

                else
                    failureCb.run();
            }

            @Override
            public void onFailure(Call<List<AdminUserProfile>> call, Throwable t) {
                failureCb.run();
            }
        });
    }
}
