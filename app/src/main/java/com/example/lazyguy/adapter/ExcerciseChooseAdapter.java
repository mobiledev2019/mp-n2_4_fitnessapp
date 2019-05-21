package com.example.lazyguy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import com.example.lazyguy.R;
import com.example.lazyguy.model.ExcerciseChoose;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPickerListener;

import java.util.ArrayList;

public class ExcerciseChooseAdapter extends ArrayAdapter<ExcerciseChoose> {

    private ArrayList<ExcerciseChoose> arrayExcerciseChooses;
    private Context contextExcerciseAdapter;
    private LayoutInflater inflater;

    public ExcerciseChooseAdapter(Context context, int resource, ArrayList<ExcerciseChoose> objects) {
        super(context, resource, objects);
        this.arrayExcerciseChooses = objects;
        this.contextExcerciseAdapter = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder5 {
        public TextView tvExName;
        public CheckBox cbExCheck;
        public ScrollableNumberPicker npRepetition;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ExcerciseChooseAdapter.ViewHolder5 viewHolder;
        if (convertView == null) {
            viewHolder = new ExcerciseChooseAdapter.ViewHolder5();
            convertView = inflater.inflate(R.layout.item_listview_excercise, parent, false);
            viewHolder.tvExName = convertView.findViewById(R.id.tvItemExName);
            viewHolder.cbExCheck = convertView.findViewById(R.id.cbItemExCheck);
            viewHolder.npRepetition = convertView.findViewById(R.id.npRepetition);
            viewHolder.npRepetition.setMinValue(1);
            viewHolder.npRepetition.setMaxValue(10);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ExcerciseChooseAdapter.ViewHolder5) convertView.getTag();
        }

        final ExcerciseChoose excerciseChoose = arrayExcerciseChooses.get(position);
        if (excerciseChoose != null) {
            viewHolder.tvExName.setText(excerciseChoose.getExcercise().getName());
            viewHolder.cbExCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    excerciseChoose.setCheck(isChecked);
                    if (!isChecked) viewHolder.npRepetition.setVisibility(View.GONE);
                    else {
                        viewHolder.npRepetition.setVisibility(View.VISIBLE);
                        viewHolder.npRepetition.setListener(new ScrollableNumberPickerListener() {
                            @Override
                            public void onNumberPicked(int value) {
                                excerciseChoose.setRepetition(value);
                            }
                        });
                    }
                }
            });

            viewHolder.cbExCheck.setChecked(excerciseChoose.getCheck());
            if (!excerciseChoose.getCheck()) viewHolder.npRepetition.setVisibility(View.GONE);
            else {
                viewHolder.npRepetition.setVisibility(View.VISIBLE);
                viewHolder.npRepetition.setValue(excerciseChoose.getRepetition());
            }
        }
        return convertView;
    }
}
