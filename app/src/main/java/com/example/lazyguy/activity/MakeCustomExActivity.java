package com.example.lazyguy.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lazyguy.R;
import com.example.lazyguy.adapter.ExcerciseAdapter;
import com.example.lazyguy.adapter.ExcerciseChooseAdapter;
import com.example.lazyguy.fragment.CustomNameDialogFragment;
import com.example.lazyguy.model.Datasheet;
import com.example.lazyguy.model.Excercise;
import com.example.lazyguy.model.ExcerciseChoose;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MakeCustomExActivity extends AppCompatActivity implements CustomNameDialogFragment.DialogListener {
    private Toolbar tbChoose;
    private ListView lvExcercise;
    private ExcerciseChooseAdapter excerciseAdapter;
    private DatabaseReference mData;
    private Button btnSave;
    private ArrayList<ExcerciseChoose> excerciseChooses, chooses;
    private String customName = "";
    private String key, endkey, currentname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_custom_ex);
        addView();
        addController();
        mData = FirebaseDatabase.getInstance().getReference();
        excerciseChooses = new ArrayList<>();
        excerciseAdapter = new ExcerciseChooseAdapter(this, R.layout.item_listview_excercise, excerciseChooses);
        lvExcercise.setAdapter(excerciseAdapter);

        mData.child("Excercise").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ExcerciseChoose ee = new ExcerciseChoose(dataSnapshot.getValue(Excercise.class), false, 0);
                excerciseChooses.add(ee);
                excerciseAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void addController() {
        tbChoose.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void addView() {
        tbChoose = findViewById(R.id.tbChoose);
        lvExcercise = findViewById(R.id.lvCustom);
        btnSave = findViewById(R.id.btSaveCustom);
    }

    public void checkAll(View view) {
        for (int i = 0; i < excerciseChooses.size(); i++) {
            ExcerciseChoose e = excerciseChooses.get(i);
            e.setCheck(true);
            excerciseChooses.set(i, e);
        }
        excerciseAdapter.notifyDataSetChanged();
    }

    public void unCheckAll(View view) {
        for (int i = 0; i < excerciseChooses.size(); i++) {
            ExcerciseChoose e = excerciseChooses.get(i);
            e.setCheck(false);
            excerciseChooses.set(i, e);
        }
        excerciseAdapter.notifyDataSetChanged();
    }

    public void save(View view) {
        if (excerciseChooses == null) return;
        chooses = new ArrayList<>();
        for (ExcerciseChoose ec : excerciseChooses) {
            if (ec.getCheck()) chooses.add(ec);
        }
        if (chooses.size() == 0){
            Toast.makeText(this, "No excercise have been selected", Toast.LENGTH_SHORT).show();
        } else {
            openDialog();
        }
    }

    private void openDialog() {
        CustomNameDialogFragment dialog = new CustomNameDialogFragment();
        dialog.show(getSupportFragmentManager(), "CustomNameDialog");
    }

    @Override
    public void applyTexts(String name) {
        customName = name;
        for (final ExcerciseChoose e : chooses) {
            mData.child("Excercise").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (dataSnapshot.getValue(Excercise.class).getName().equals(e.getExcercise().getName())){
                        key = dataSnapshot.getKey();
                        Datasheet d = new Datasheet(dataSnapshot.getKey(), e.getRepetition(), 3, 1);
                        mData.child("Custom").child(customName).push().setValue(d);
                    }
                }
                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }
                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }
                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        mData.child("Excercise").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getValue(Excercise.class).getName().equals(chooses.get(0).getExcercise().getName())){
                    endkey = dataSnapshot.getKey();
                    mData.child("Custom").child("End" + customName).setValue(endkey);
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mData.child("Custom").child("Name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentname = dataSnapshot.getValue().toString();
                if (!currentname.contains(customName)){
                    currentname += (customName + "/");
                    mData.child("Custom").child("Name").setValue(currentname);
                }
                return;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        startActivity(new Intent(MakeCustomExActivity.this, MainActivity.class));
    }

    public void searchExID(final Excercise e) {

    }
}
