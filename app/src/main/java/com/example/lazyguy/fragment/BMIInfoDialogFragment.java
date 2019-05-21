package com.example.lazyguy.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lazyguy.R;

import java.text.DecimalFormat;

public class BMIInfoDialogFragment extends AppCompatDialogFragment {
    private TextView tvBMIinfo;
    private ImageView imgBMIchart;
    private Drawable img;
    private String info = "", title = "BMI information";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_bmi_info_dialog, null);
        builder.setView(view)
                .setTitle(title)
                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        tvBMIinfo = view.findViewById(R.id.tvBMIinfo);
        imgBMIchart = view.findViewById(R.id.imgBmiChart);
        tvBMIinfo.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        tvBMIinfo.setText(info);
        if (img != null) imgBMIchart.setImageDrawable(img);
        else imgBMIchart.setVisibility(View.GONE);
        return builder.create();
    }

    public void setIndex (String text, Drawable drawable) {
        info = text;
        img = drawable;
    }

    public void setIndex (String text, String t,Drawable drawable) {
        info = text;
        img = drawable;
        title = t;
    }
}
