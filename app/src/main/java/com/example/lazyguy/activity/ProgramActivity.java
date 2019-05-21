package com.example.lazyguy.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lazyguy.R;
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

public class ProgramActivity extends AppCompatActivity {

    private ListView lvProgram;
    private ArrayList<Program> arrayListProgram;
    private ProgramAdapter programAdapter;
    private DatabaseReference mData;
    private String explorer;
    private ArrayList<Datasheet> datasheets;
    private ImageView imgNoPart;
    private Toolbar tbProgram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program);
        AnhXa();
        Intent intent = this.getIntent();
        explorer = intent.getStringExtra("explorer");
        tbProgram.setTitle(explorer);
        tbProgram.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (explorer.equals("Beginner Program")) explorer = "Beginner";
        if (explorer.equals("Intermediate Program")) explorer = "Intermediate";
        if (explorer.equals("Advanced Program")) explorer = "Advanced";

        //getSupportActionBar().setTitle(explorer);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        arrayListProgram = new ArrayList<>();
        programAdapter = new ProgramAdapter(this, R.layout.item_listview_programpart, arrayListProgram);
        lvProgram.setAdapter(programAdapter);

        mData = FirebaseDatabase.getInstance().getReference();

        mData.child("Datasheet").child(explorer).child("NumberPart").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String numberPart = dataSnapshot.getValue().toString();
                int np = Integer.parseInt(numberPart);
                for (int i = 1; i <= np; i++) {
                    arrayListProgram.add(new Program("Part " + i));
                }
                if (arrayListProgram.size() > 0) {
                    imgNoPart.setVisibility(View.GONE);
                } else {
                    imgNoPart.setVisibility(View.VISIBLE);
                }
                programAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        lvProgram.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String program = arrayListProgram.get(position).getName();
                Intent intent = new Intent(ProgramActivity.this, ExcerciseActivity.class);
                intent.putExtra("explorer", explorer);
                intent.putExtra("program", program);
                startActivity(intent);
            }
        });

        lvProgram.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String program = arrayListProgram.get(position).getName();

                return false;
            }
        });

    }

    public void AnhXa() {
        lvProgram = findViewById(R.id.lvProgram);
        imgNoPart = findViewById(R.id.imgNoPart);
        tbProgram = findViewById(R.id.tbProgram);
    }

}
