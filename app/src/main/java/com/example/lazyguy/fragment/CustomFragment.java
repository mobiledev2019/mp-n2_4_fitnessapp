package com.example.lazyguy.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lazyguy.R;
import com.example.lazyguy.activity.ExcerciseActivity;
import com.example.lazyguy.activity.MainActivity;
import com.example.lazyguy.activity.MakeCustomExActivity;
import com.example.lazyguy.activity.YoutubeTestActivity;
import com.example.lazyguy.adapter.CustomAdapter;
import com.example.lazyguy.adapter.ProgramAdapter;
import com.example.lazyguy.model.Program;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomFragment extends Fragment{

    private ListView lvCustom;
    private ArrayList<Program> arrayListCustom;
    private ArrayList<Boolean> arrayListCheck;
    private CustomAdapter customAdapter;
    private DatabaseReference mData;
    String nameList = "", nameArray[];;
    Boolean show = false;
    FloatingActionButton fbtDelete;

    @SuppressLint("RestrictedApi")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_custom, container, false);
        addView(rootView);
        addController();

        mData = FirebaseDatabase.getInstance().getReference();
        arrayListCustom = new ArrayList<>();
        arrayListCheck = new ArrayList<>();
        customAdapter = new CustomAdapter(getContext(), R.layout.item_listview_custom, arrayListCustom, arrayListCheck, show);
        lvCustom.setAdapter(customAdapter);
        arrayListCustom.add(new Program("Add new Custom Program"));
        arrayListCheck.add(false);
        customAdapter.notifyDataSetChanged();
        fbtDelete.setVisibility(View.GONE);

        mData.child("Custom").child("Name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nameList = dataSnapshot.getValue().toString();
                nameArray = nameList.split("/");
                for (String s : nameArray) {
                    arrayListCustom.add(new Program(s));
                    arrayListCheck.add(false);
                }
                customAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        return rootView;
    }

    private void addController() {
        lvCustom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String program = arrayListCustom.get(position).getName();
                Toast.makeText(getContext(), program, Toast.LENGTH_SHORT).show();
                if (position == 0) {
                    startActivity(new Intent(getContext(), MakeCustomExActivity.class));
                } else {
                    Intent intent = new Intent(getContext(), ExcerciseActivity.class);
                    intent.putExtra("explorer", "Custom");
                    intent.putExtra("program", program);
                    startActivity(intent);
                }
            }
        });
        lvCustom.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String program = arrayListCustom.get(position).getName();
                Toast.makeText(getContext(), program, Toast.LENGTH_SHORT).show();
                customAdapter.show = true;
                customAdapter.notifyDataSetChanged();
                fbtDelete.setVisibility(View.VISIBLE);
                return false;
            }
        });
        fbtDelete.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                //delete custom program
                ArrayList<String> strings = new ArrayList<>();
                for (String ss : nameArray) {
                    strings.add(ss);
                }
                for (int i = 0; i < arrayListCheck.size(); i++){
                    if (arrayListCheck.get(i)) {
                        String text = arrayListCustom.get(i).getName();
                        strings.remove(text);
                        mData.child("Custom").child(text).removeValue();
                        mData.child("Custom").child("End" + text).removeValue();
                    }
                }
                String updateString = "";
                for (String sss : strings) {
                    updateString += sss + "/";
                }
                mData.child("Custom").child("Name").setValue(updateString);

                //Toast.makeText(getContext(), "update: " + updateString, Toast.LENGTH_SHORT).show();
                customAdapter.show = false;
                customAdapter.notifyDataSetChanged();
                fbtDelete.setVisibility(View.GONE);
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });
    }

    public void addView(View view) {
        lvCustom = view.findViewById(R.id.lvCustom);
        fbtDelete = view.findViewById(R.id.fbtDelete);
    }

}