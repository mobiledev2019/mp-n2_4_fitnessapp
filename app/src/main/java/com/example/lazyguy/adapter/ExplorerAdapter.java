package com.example.lazyguy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lazyguy.R;
import com.example.lazyguy.model.Explorer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ExplorerAdapter extends ArrayAdapter<Explorer> {
    private ArrayList<Explorer> arrayExplorer;
    private Context contextExplorerAdapter;
    private LayoutInflater inflater;

    public ExplorerAdapter(Context context, int resource,  ArrayList<Explorer> objects) {
        super(context, resource, objects);
        this.arrayExplorer = objects;
        this.contextExplorerAdapter = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder {
        public TextView tvExName;
        public ImageView imgEx;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_listview, parent, false);

            viewHolder.tvExName = convertView.findViewById(R.id.tvExName);
            viewHolder.imgEx = convertView.findViewById(R.id.imgExplorer);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Explorer explorer = arrayExplorer.get(position);
        if (explorer != null) {
            viewHolder.tvExName.setText(explorer.getName());
            Picasso.get().load(explorer.getDetail()).into(viewHolder.imgEx);
        }
        return convertView;
    }
}
