package com.hk203.dadn.ui.patient_info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.DialogFragment;
import com.hk203.dadn.R;

public class AddTreatmentDialog extends DialogFragment {
    interface OnAddNewTreatmentListener{
        void onAddNewTreatment(String msg);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_new_treatment, container);
        view.findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OnAddNewTreatmentListener)getParentFragment()).onAddNewTreatment("123");
            }
        });
        return view;
    }
}
