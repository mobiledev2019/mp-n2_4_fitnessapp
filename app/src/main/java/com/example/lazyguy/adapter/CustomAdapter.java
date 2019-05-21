package com.example.lazyguy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.lazyguy.R;
import com.example.lazyguy.model.Excercise;
import com.example.lazyguy.model.Program;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Program> {
    private ArrayList<Program> arrayExcercise;
    private ArrayList<Boolean> arrayCheck;
    public Boolean show;
    private Context contextExcerciseAdapter;
    private LayoutInflater inflater;

    public CustomAdapter(Context context, int resource, ArrayList<Program> objects, ArrayList<Boolean> checks, Boolean shows) {
        super(context, resource, objects);
        this.arrayExcercise = objects;
        this.arrayCheck = checks;
        this.show = shows;
        this.contextExcerciseAdapter = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder6 {
        public TextView tvExName;
        public CheckBox cbExCheck;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final CustomAdapter.ViewHolder6 viewHolder;
        if (convertView == null) {
            viewHolder = new CustomAdapter.ViewHolder6();
            convertView = inflater.inflate(R.layout.item_listview_custom, parent, false);
            viewHolder.tvExName = convertView.findViewById(R.id.tvItemCustom);
            viewHolder.cbExCheck = convertView.findViewById(R.id.cbItemCustom);
            if (position == 0) viewHolder.cbExCheck.setVisibility(View.GONE);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CustomAdapter.ViewHolder6) convertView.getTag();
        }

        if (!show || position == 0) viewHolder.cbExCheck.setVisibility(View.GONE);
        else viewHolder.cbExCheck.setVisibility(View.VISIBLE);

        final Program excercise = arrayExcercise.get(position);
        if (excercise != null) {
            viewHolder.tvExName.setText(excercise.getName());
            viewHolder.cbExCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    arrayCheck.set(position, isChecked);
                }
            });
            viewHolder.cbExCheck.setChecked(arrayCheck.get(position));
        }
        return convertView;
    }
}
