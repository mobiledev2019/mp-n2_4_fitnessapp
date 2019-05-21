package com.example.lazyguy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.lazyguy.R;
import com.example.lazyguy.activity.BMIActivity;

public class CountFragment extends Fragment {

    private ImageView imgBmi;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_count, container, false);
        anhXa(rootView);

        imgBmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BMIActivity.class));
            }
        });
        return rootView;
    }

    public void anhXa(View view) {
        imgBmi = view.findViewById(R.id.imgBmi);
    }

}
