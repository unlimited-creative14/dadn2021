package com.hk203.dadn.ui.admin_listuser;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.hk203.dadn.MainActivity;
import com.hk203.dadn.R;
import com.hk203.dadn.models.AdminPatient;
import com.hk203.dadn.models.Patient;
import com.hk203.dadn.repositories.IoTHealthCareRepository;
import com.hk203.dadn.ui.admin_listpatient.PatientDetailFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListBindedPatientAdapter extends ArrayAdapter<AdminPatient> {

    int res;
    private IoTHealthCareRepository repo = IoTHealthCareRepository.getInstance();

    public ListBindedPatientAdapter(@NonNull Context context, int resource, @NonNull AdminPatient[] objects) {
        super(context, resource, objects);
        res = resource;
    }

    public ListBindedPatientAdapter(@NonNull Context context, int resource, @NonNull List<AdminPatient> objects) {
        super(context, resource, objects);
        res = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder vh;
        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(res, parent, false);

            vh = new ViewHolder();
            vh.btnPatientName = convertView.findViewById(R.id.btn_patient_name);
            vh.btnUnbindPatient = convertView.findViewById(R.id.btn_unbind_patient);

            convertView.setTag(vh);
        }
        else
            vh = (ViewHolder) convertView.getTag();

        AdminPatient p = getItem(position);
        vh.btnPatientName.setText(p.first_name + " " + p.last_name);

        vh.btnPatientName.setOnClickListener(v -> {
            final AppCompatActivity activity = (AppCompatActivity)getContext();
            NavController controller = Navigation.findNavController(
                    activity,
                    R.id.nav_host_fragment_content_main
            );
            Bundle bd = new Bundle();
            bd.putSerializable("patient", getItem(position));

            controller.navigate(R.id.nav_admin_patient_detail, bd);
        });

        vh.btnUnbindPatient.setOnClickListener(v->{
            new AlertDialog.Builder(getContext()).setTitle("Xoá bệnh nhân mã só " + getItem(position).pat_id)
                    .setMessage("Xoá ! ra khỏi danh sách?".replace("!", getItem(position).toString()))
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            repo.deletePatient(
                                    ((MainActivity)getContext()).getAuthToken(),
                                    getItem(position).pat_id,
                                    new Callback<Map<String, String>>() {
                                        @Override
                                        public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                                            if (response.code() == 200) {
                                                Toast.makeText(getContext(), "Deleted!", Toast.LENGTH_SHORT).show();
                                                remove(getItem(position));
                                            }
                                            else
                                                Toast.makeText(getContext(), "Delete failed!", Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onFailure(Call<Map<String, String>> call, Throwable t) {

                                        }
                                    }
                            );
                        }
                    }).setNegativeButton("Huỷ", (dialog, pos) -> dialog.cancel()).create().show();
        });

        return convertView;
    }

    class ViewHolder
    {
        Button btnPatientName;
        ImageButton btnUnbindPatient;
    }
}
