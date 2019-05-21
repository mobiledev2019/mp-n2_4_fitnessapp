package com.example.lazyguy.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lazyguy.R;

import java.text.DecimalFormat;

public class BMIDialogFragment extends AppCompatDialogFragment {
    private TextView tvBMIresult, tvBMIcmt;
    public double bmi = 0.0;
    public String cmt = "";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_bmi_dialog, null);
        builder.setView(view)
                .setTitle("Caculate Result")
                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        tvBMIresult = view.findViewById(R.id.tvBMIresult);
        tvBMIcmt = view.findViewById(R.id.tvBMIcmt);
        DecimalFormat df = new DecimalFormat("#.00");
        tvBMIresult.setText(df.format(bmi) + "");
        tvBMIcmt.setText(cmt);
        if (cmt.equals("In Asia, You are underweight") || cmt.equals("In Africa, You are underweight")) tvBMIresult.setTextColor(Color.BLUE);
        else if (cmt.equals("In Asia, You are healthy range") || cmt.equals("In Africa, You are healthy range")) tvBMIresult.setTextColor(Color.GREEN);
        else if (cmt.equals("In Asia, You are overweight") || cmt.equals("In Africa, You are overweight")) tvBMIresult.setTextColor(Color.YELLOW);
        else tvBMIresult.setTextColor(Color.RED);
        return builder.create();
    }

    public void setIndex (double b, String c) {
        cmt = c;
        bmi = b;
    }
}
