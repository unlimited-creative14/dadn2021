package com.hk203.dadn.ui.adafruitdemo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.hk203.dadn.MQTTService;
import com.hk203.dadn.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SendMQTTMessageDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SendMQTTMessageDialogFragment extends DialogFragment {

    public SendMQTTMessageDialogFragment() {
        // Required empty public constructor
    }

    MQTTService svc;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment SendMQTTMessageDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SendMQTTMessageDialogFragment newInstance(MQTTService svc) {
        SendMQTTMessageDialogFragment fragment = new SendMQTTMessageDialogFragment();
        fragment.svc = svc;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = getLayoutInflater().inflate(R.layout.fragment_send_m_q_t_t_message_dialog, null);
        builder.setView(v)
                .setMessage("Enter message")
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText etMsg = (EditText)v.findViewById(R.id.et_message);
                        try {
                            if (etMsg.getText().length() > 0)
                                svc.sendData(etMsg.getText().toString());
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();

    }
}