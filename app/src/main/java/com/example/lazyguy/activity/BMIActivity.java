package com.example.lazyguy.activity;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lazyguy.R;
import com.example.lazyguy.fragment.BMIDialogFragment;
import com.example.lazyguy.fragment.BMIInfoDialogFragment;
import com.example.lazyguy.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class BMIActivity extends AppCompatActivity {
    private Spinner spHeight, spWeight;
    private int heightUnit, weightUnit;
    private Button btCalculate;
    private EditText edtCalAge, edtCalWeight, edtCalHeight;
    private RadioButton rbCalMen, rbCalWomen;
    private User myUser;
    private String nationality;
    private Toolbar tbBMI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);
        //getSupportActionBar().setTitle("BMI Caculation");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        anhXa();
        tbBMI.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tbBMI.inflateMenu(R.menu.bmimenu);
        tbBMI.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_bmi_profile:
                        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        DatabaseReference mData = FirebaseDatabase.getInstance().getReference();

                        if (mAuth.getCurrentUser() == null)
                            Toast.makeText(BMIActivity.this, "You haven't login yet", Toast.LENGTH_SHORT).show();
                        else {
                            mData.child("User").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    String googleId = mAuth.getCurrentUser().getUid();
                                    User u = dataSnapshot.getValue(User.class);
                                    if (u.getGoogleId().equals(googleId)) {
                                        myUser = u;
                                    }
                                    if (myUser != null) {
                                        edtCalWeight.setText(myUser.getWeight() + "");
                                        edtCalHeight.setText(myUser.getHeight() + "");
                                        if (myUser.isSex()) rbCalMen.setChecked(true);
                                        else rbCalWomen.setChecked(true);
                                        spWeight.setSelection(0);
                                        spHeight.setSelection(0);
                                        Toast.makeText(BMIActivity.this, "Your profile has been set to caculate", Toast.LENGTH_SHORT).show();
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
                        break;
                    case R.id.action_bmi_info:
                        BMIInfoDialogFragment dialog = new BMIInfoDialogFragment();
                        Drawable d = getDrawable(R.drawable.chart);
                        dialog.setIndex(getString(R.string.bmi_info), d);
                        dialog.show(getSupportFragmentManager(), "BmiInfoDialog");
                        break;
                }
                return false;
            }
        });
        setUpSpinner();
        myUser = new User();
        SharedPreferences sharedPref = android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this);
        Boolean switchPref = sharedPref.getBoolean(SettingsActivity.KEY_PREF_MASS_SWITCH, false);
        if (!switchPref) {
            spWeight.setSelection(0);
            weightUnit = 0;
        }
        else {
            spWeight.setSelection(1);
            weightUnit = 1;
        }
        Boolean switchPref2 = sharedPref.getBoolean(SettingsActivity.KEY_PREF_LENGTH_SWITCH, false);
        if (!switchPref2) {
            spHeight.setSelection(0);
            heightUnit = 0;
        }
        else {
            spHeight.setSelection(1);
            heightUnit = 1;
        }
        nationality = sharedPref.getString("nationality", "Asia");

        spWeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                weightUnit = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spHeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                heightUnit = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtCalAge.getText().toString().equals("")) {
                    Toast.makeText(BMIActivity.this, "Age field is empty", Toast.LENGTH_SHORT).show();
                }
                else if (edtCalWeight.getText().toString().equals("")) {
                    Toast.makeText(BMIActivity.this, "Weight field is empty", Toast.LENGTH_SHORT).show();
                }
                else if (edtCalHeight.getText().toString().equals("")) {
                    Toast.makeText(BMIActivity.this, "Height field is empty", Toast.LENGTH_SHORT).show();
                }
                else if (!rbCalMen.isChecked() && !rbCalWomen.isChecked()) {
                    Toast.makeText(BMIActivity.this, "You must choose gender button", Toast.LENGTH_SHORT).show();
                }
                else {
                    double bmi = caculateBMI();
                    if (bmi != 0) {
                        String cmt = commentBMI(bmi);
                        BMIDialogFragment dialog = new BMIDialogFragment();
                        dialog.setIndex(bmi, cmt);
                        dialog.show(getSupportFragmentManager(), "BmiDialog");
                    }
                }
            }
        });
    }

    public Double caculateBMI () {
        Double age = Double.parseDouble(edtCalAge.getText().toString());
        Double weight = Double.parseDouble(edtCalWeight.getText().toString());
        if (weightUnit == 1) weight *= 0.45359237;
        Double height = Double.parseDouble(edtCalHeight.getText().toString());
        if (heightUnit == 1) height *= 2.54;

        Double bmi = 0.0;
        if (weight == 0 || age == 0 || height == 0)
            Toast.makeText(this, "Field has value equal 0", Toast.LENGTH_SHORT).show();
        else {
            bmi = 10000 * weight / (height * height);
            DecimalFormat df = new DecimalFormat("#.00");
            //Toast.makeText(this, "Bmi " + df.format(bmi), Toast.LENGTH_SHORT).show();
        }
        return bmi;
    }

    public String commentBMI (double bmi) {
        String cmt = "In " + nationality +  ", ";
        Double age = Double.parseDouble(edtCalAge.getText().toString());
        double[] fatman = {0.0, 19.5, 19.5, 18.5, 18.0, 18.0, 18.5, 19.0, 20.0, 21.0, 22.0, 23.0, 24.0, 25.0, 26.0, 27.0, 27.5, 28.0, 29.0, 29.5, 30.5};
        double[] normalman = {0.0, 18.0, 18.0, 17.5, 17.0, 17.0, 17.0, 17.5, 18.0, 18.5, 19.5, 20.0, 21.0, 22.0, 22.5, 23.5, 24.0, 25.0, 26.0, 26.5, 27.0};
        double[] thinman = {0.0, 15.0, 15.0, 14.5, 14.0, 14.0, 14.0, 14.0, 14.0, 14.0, 14.0, 14.5, 15.0, 15.5, 16.0, 16.5, 17.0, 18.0, 18.0, 19.5, 20.0};
        double[] fatwoman = {0.0, 19.0, 19.0, 18.0, 18.0, 18.0, 19.0, 19.5, 20.5, 22.0, 23.0, 24.0, 25.0, 26.0, 27.0, 28.0, 29.0, 29.5, 30.5, 31.0, 31.5};
        double[] normalwoman = {0.0, 18.0, 18.0, 17.0, 17.0, 17.0, 17.0, 17.5, 18.0, 19.0, 20.0, 21.0, 22.0, 22.5, 23.5, 24.0, 23.5, 25.0, 25.5, 26.0, 26.5};
        double[] thinwoman = {0.0, 14.5, 14.0, 14.0, 14.0, 13.5, 13.5, 13.5, 13.5, 14.0, 14.0, 14.5, 15.0, 15.0, 16.0, 16.5, 17.0, 17.0, 17.5, 17.5, 18.0};

        if (age > 20) {
            if (bmi <= 18.5) cmt += "You are underweight";
            else if (bmi <= 25) cmt += "You are healthy range";
            else if (bmi <= 30) cmt += "You are overweight";
            else  cmt += "You are obese";
        }
        if (age <= 20) {
            int agee = Integer.parseInt(edtCalAge.getText().toString());
            if (rbCalMen.isChecked()) {
                if (bmi > fatman[agee]) cmt += "You are obese";
                else if (bmi > normalman[agee]) cmt += "You are overweight";
                else if (bmi > thinman[agee]) cmt += "You are healthy range";
                else cmt += "You are underweight";
            }
            if (rbCalWomen.isChecked()) {
                if (bmi > fatwoman[agee]) cmt += "You are obese";
                else if (bmi > normalwoman[agee]) cmt += "You are overweight";
                else if (bmi > thinwoman[agee]) cmt += "You are healthy range";
                else cmt += "You are underweight";
            }
        }
        return cmt;
    }

    public void anhXa () {
        spHeight = findViewById(R.id.spHeight);
        spWeight = findViewById(R.id.spWeight);
        btCalculate = findViewById(R.id.btCalculate);
        edtCalAge = findViewById(R.id.edtCalAge);
        edtCalHeight = findViewById(R.id.edtCalHeight);
        edtCalWeight = findViewById(R.id.edtCalWeight);
        rbCalMen = findViewById(R.id.rbCalMen);
        rbCalWomen = findViewById(R.id.rbCalWomen);
        tbBMI = findViewById(R.id.tbBMI);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_bmi_profile:
                final FirebaseAuth mAuth = FirebaseAuth.getInstance();
                DatabaseReference mData = FirebaseDatabase.getInstance().getReference();

                if (mAuth.getCurrentUser() == null)
                    Toast.makeText(this, "You haven't login yet", Toast.LENGTH_SHORT).show();
                else {
                    mData.child("User").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            String googleId = mAuth.getCurrentUser().getUid();
                            User u = dataSnapshot.getValue(User.class);
                            if (u.getGoogleId().equals(googleId)) {
                                myUser = u;
                            }
                            if (myUser != null) {
                                edtCalWeight.setText(myUser.getWeight() + "");
                                edtCalHeight.setText(myUser.getHeight() + "");
                                if (myUser.isSex()) rbCalMen.setChecked(true);
                                else rbCalWomen.setChecked(true);
                                spWeight.setSelection(0);
                                spHeight.setSelection(0);
                                Toast.makeText(BMIActivity.this, "Your profile has been set to caculate", Toast.LENGTH_SHORT).show();
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
                break;
            case R.id.action_bmi_info:
                BMIInfoDialogFragment dialog = new BMIInfoDialogFragment();
                Drawable d = getDrawable(R.drawable.chart);
                dialog.setIndex(getString(R.string.bmi_info), d);
                dialog.show(getSupportFragmentManager(), "BmiInfoDialog");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bmimenu, menu);
        return true;
    }

    public void setUpSpinner() {
        ArrayList<String> height = new ArrayList<>();
        height.add("Cm");
        height.add("Inch");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, height);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spHeight.setAdapter(dataAdapter);
        ArrayList<String> weight = new ArrayList<>();
        weight.add("Kg");
        weight.add("Ibs");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, weight);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWeight.setAdapter(dataAdapter2);
    }
}
