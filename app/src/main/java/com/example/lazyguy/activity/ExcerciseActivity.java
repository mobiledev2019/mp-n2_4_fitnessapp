package com.example.lazyguy.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lazyguy.R;
import com.example.lazyguy.adapter.ExcerciseAdapter;
import com.example.lazyguy.adapter.ProgramAdapter;
import com.example.lazyguy.model.Datasheet;
import com.example.lazyguy.model.Excercise;
import com.example.lazyguy.model.Program;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ExcerciseActivity extends AppCompatActivity {

    String explorer, program;
    private ListView lvExcercise;
    private ExcerciseAdapter excerciseAdapter;
    private DatabaseReference mData;
    private ArrayList<Excercise> excercisesChoose;
    private ArrayList<String> exKey;
    private Toolbar tbEx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excercise);
        anhXa();
        //getSupportActionBar().setTitle("Excercise");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Intent intent = this.getIntent();
        explorer = intent.getStringExtra("explorer");
        program = intent.getStringExtra("program");
        tbEx.setTitle(program);
        tbEx.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        excercisesChoose = new ArrayList<>();
        exKey = new ArrayList<>();
        excerciseAdapter = new ExcerciseAdapter(this, R.layout.item_listview_programpart, excercisesChoose);
        lvExcercise.setAdapter(excerciseAdapter);

        mData = FirebaseDatabase.getInstance().getReference();

        if (explorer.equals("Custom")){
            mData.child("Custom").child(program).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    final Datasheet dd = dataSnapshot.getValue(Datasheet.class);
                    mData.child("Excercise").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            if (dd.getExid().equals(dataSnapshot.getKey())) {
                                excercisesChoose.add(dataSnapshot.getValue(Excercise.class));
                                excerciseAdapter.notifyDataSetChanged();
                                exKey.add(dataSnapshot.getKey());
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
        } else {
            mData.child("Datasheet").child(explorer).child(program).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    final Datasheet d = dataSnapshot.getValue(Datasheet.class);
                    mData.child("Excercise").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            if (d.getExid().equals(dataSnapshot.getKey())) {
                                excercisesChoose.add(dataSnapshot.getValue(Excercise.class));
                                excerciseAdapter.notifyDataSetChanged();
                                exKey.add(dataSnapshot.getKey());
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
        lvExcercise.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1 = new Intent(ExcerciseActivity.this, PracticeActivity.class);
                String exid = exKey.get(position);
                intent1.putExtra("explorer", explorer);
                intent1.putExtra("program", program);
                intent1.putExtra("exid", exid);
                startActivity(intent1);
            }
        });
    }

    /*
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    */

    public void anhXa() {
        lvExcercise = findViewById(R.id.lvExcercise);
        tbEx = findViewById(R.id.tbEx);
    }
}
