package com.example.lazyguy.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.lazyguy.R;
import com.example.lazyguy.activity.ExcerciseActivity;
import com.example.lazyguy.activity.PracticeActivity;
import com.example.lazyguy.activity.ProgramActivity;
import com.example.lazyguy.model.Excercise;

public class ProcessDialogFragment extends AppCompatDialogFragment {
    TextView tvConfirm;
    String explorer = "", program = "", exid = "", explorer2 = "";
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_process_dialog, null);
        tvConfirm = view.findViewById(R.id.tvConfirm);
        tvConfirm.setText("Do you want to continue your process at " + program + " in " + explorer + " ?");
        builder.setView(view)
                .setTitle("Your process")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getContext(), ProgramActivity.class);
                        intent.putExtra("explorer", explorer2);
                        startActivity(intent);
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getContext(), ExcerciseActivity.class);
                        intent.putExtra("explorer", explorer);
                        intent.putExtra("program", program);
                        //intent.putExtra("exid", exid);
                        startActivity(intent);
                    }
                });
        return builder.create();
    }

    public void setIndex (String e, String p, String ex, String e2) {
        explorer = e;
        program = p;
        exid = ex;
        explorer2 = e2;
    }
}
