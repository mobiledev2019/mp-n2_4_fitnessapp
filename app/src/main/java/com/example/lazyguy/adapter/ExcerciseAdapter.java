package com.example.lazyguy.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.lazyguy.R;
import com.example.lazyguy.model.Datasheet;
import com.example.lazyguy.model.Excercise;

import java.util.ArrayList;

public class ExcerciseAdapter extends ArrayAdapter<Excercise> {

    private ArrayList<Excercise> arrayExcercise;
    private Context contextExcerciseAdapter;
    private LayoutInflater inflater;

    public ExcerciseAdapter(Context context, int resource,  ArrayList<Excercise> objects) {
        super(context, resource, objects);
        this.arrayExcercise = objects;
        this.contextExcerciseAdapter = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder3 {
        public TextView tvExName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ExcerciseAdapter.ViewHolder3 viewHolder;
        if (convertView == null) {
            viewHolder = new ExcerciseAdapter.ViewHolder3();
            convertView = inflater.inflate(R.layout.item_listview_programpart, parent, false);

            viewHolder.tvExName = convertView.findViewById(R.id.tvPPName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ExcerciseAdapter.ViewHolder3) convertView.getTag();
        }

        Excercise excercise = arrayExcercise.get(position);
        if (excercise != null) {
            viewHolder.tvExName.setText(excercise.getName());
            //viewHolder.tvExName.setTextColor(Color.BLACK);
        }
        return convertView;
    }

}
