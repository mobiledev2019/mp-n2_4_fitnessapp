package com.example.lazyguy.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lazyguy.CircleImage;
import com.example.lazyguy.R;
import com.example.lazyguy.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private CircleImage imgProfile;

    private TextView tvUserName, tvEmail;

    private DatabaseReference mData;
    private EditText edtHeight, edtWeight;
    private User myUser;
    private RadioGroup radioGroup;
    private RadioButton rbMan, rbWoman;
    Button btnUpdateProfile, btnSaveProfile, btnResetProfile;
    String key;
    Toolbar tbProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        AnhXa();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    imgProfile.setImageResource(R.drawable.naruto);
                    tvUserName.setText("User name");
                    tvEmail.setText("abc@gmail.com");
                } else {
                    String userName = firebaseAuth.getCurrentUser().getDisplayName();
                    String email = firebaseAuth.getCurrentUser().getEmail();
                    String imageUrl = firebaseAuth.getCurrentUser().getPhotoUrl().toString();
                    Picasso.get().load(imageUrl).into(imgProfile);
                    tvUserName.setText(userName);
                    tvEmail.setText(email);
                }
            }
        };
        mData = FirebaseDatabase.getInstance().getReference();
        myUser = new User();
        mData.child("User").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String googleId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                User u = dataSnapshot.getValue(User.class);
                if (u.getGoogleId().equals(googleId)){
                    key = dataSnapshot.getKey();
                    myUser = u;
                }
                if (myUser != null) loadData();
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
        //getSupportActionBar().setTitle("User Account");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Reset();

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Enable();
            }
        });

        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this, "Save change", Toast.LENGTH_SHORT).show();
                saveData();
                if (key != null) mData.child("User").child(key).setValue(myUser);
                Reset();
            }
        });

        btnResetProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this, "Unsave change", Toast.LENGTH_SHORT).show();
                loadData();
                Reset();
            }
        });

        tbProfile.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void Reset() {
        edtHeight.setTag(edtHeight.getKeyListener());
        edtHeight.setKeyListener(null);
        edtWeight.setTag(edtWeight.getKeyListener());
        edtWeight.setKeyListener(null);
        rbMan.setEnabled(false);
        rbWoman.setEnabled(false);
        btnSaveProfile.setVisibility(View.INVISIBLE);
        btnResetProfile.setVisibility(View.INVISIBLE);
    }

    public void Enable () {
        edtHeight.setKeyListener((KeyListener) edtHeight.getTag());
        edtWeight.setKeyListener((KeyListener) edtWeight.getTag());
        rbMan.setEnabled(true);
        rbWoman.setEnabled(true);
        btnSaveProfile.setVisibility(View.VISIBLE);
        btnResetProfile.setVisibility(View.VISIBLE);
    }

    public void loadData () {
        edtHeight.setText(myUser.getHeight()+"");
        edtWeight.setText(myUser.getWeight()+"");
        if (myUser.isSex()){
            rbMan.setChecked(true);
        } else {
            rbWoman.setChecked(true);
        }
    }

    public void saveData () {
        myUser.setHeight(Float.parseFloat(edtHeight.getText().toString()));
        myUser.setWeight(Float.parseFloat(edtWeight.getText().toString()));
        if (rbMan.isChecked()) myUser.setSex(true);
        if (rbWoman.isChecked()) myUser.setSex(false);
    }

    public void AnhXa() {
        imgProfile = findViewById(R.id.imgAvatarProfile);
        tvUserName = findViewById(R.id.tvUserNameProfile);
        tvEmail = findViewById(R.id.tvMailProfile);
        edtHeight = findViewById(R.id.edtHeight);
        edtWeight = findViewById(R.id.edtWeight);
        radioGroup = findViewById(R.id.rgSex);
        rbMan = findViewById(R.id.rbMan);
        rbWoman = findViewById(R.id.rbWoman);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        btnResetProfile = findViewById(R.id.btnResetProfile);
        tbProfile = findViewById(R.id.tbProfile);
    }
}