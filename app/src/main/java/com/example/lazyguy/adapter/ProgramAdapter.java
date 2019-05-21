package com.example.lazyguy.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lazyguy.R;
import com.example.lazyguy.model.Datasheet;
import com.example.lazyguy.model.Explorer;
import com.example.lazyguy.model.Program;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProgramAdapter extends ArrayAdapter<Program> {
    private ArrayList<Program> arrayProgram;
    private Context contextProgramAdapter;
    private LayoutInflater inflater;

    public ProgramAdapter(Context context, int resource,  ArrayList<Program> objects) {
        super(context, resource, objects);
        this.arrayProgram = objects;
        this.contextProgramAdapter = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder2 {
        public TextView tvExName;
        public LinearLayout llProgram;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder2 viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder2();
            convertView = inflater.inflate(R.layout.item_listview_programpart, parent, false);

            viewHolder.tvExName = convertView.findViewById(R.id.tvPPName);
            viewHolder.llProgram = convertView.findViewById(R.id.llListProgramPart);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder2) convertView.getTag();
        }

        final Program program = arrayProgram.get(position);
        if (program != null) {
            viewHolder.tvExName.setText(program.getName());
            if (position%2 == 0) {
                viewHolder.llProgram.setBackgroundColor(convertView.getResources().getColor(R.color.colorPrimary));
                viewHolder.tvExName.setTextColor(convertView.getResources().getColor(R.color.white));
            }
        }
        return convertView;
    }
}
