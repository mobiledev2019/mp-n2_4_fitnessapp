package com.example.lazyguy.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.lazyguy.R;
import com.example.lazyguy.fragment.BMIInfoDialogFragment;
import com.example.lazyguy.fragment.SettingsFragment;
import com.example.lazyguy.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends AppCompatActivity {
    public static final String KEY_PREF_MASS_SWITCH = "mass_switch";
    public static final String KEY_PREF_LENGTH_SWITCH = "length_switch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getLayoutInflater().inflate(R.layout.toolbar, (ViewGroup)findViewById(android.R.id.content));
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(SettingsActivity.this, "back", Toast.LENGTH_SHORT).show();
//                //startActivity(new Intent(SettingsActivity.this, MainActivity.class));
//                //onBackPressed();
//            }
//        });

        LinearLayout root = (LinearLayout)findViewById(android.R.id.content).getParent().getParent().getParent();
        android.support.v7.widget.Toolbar bar = (android.support.v7.widget.Toolbar) LayoutInflater.from(this).inflate(R.layout.toolbar, root, false);
        root.addView(bar, 0);
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
            }
        });
    }
}
