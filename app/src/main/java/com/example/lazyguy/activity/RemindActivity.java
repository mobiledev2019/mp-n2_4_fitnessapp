package com.example.lazyguy.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.lazyguy.AlertReceiver;
import com.example.lazyguy.R;
import com.example.lazyguy.fragment.TimePickerFragment;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RemindActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    private TextView tvBreakfast, tvLunch, tvDinner, tvDrinkQuestion, tvEx;
    private Switch swBreakfast, swLunch, swDinner, swDrink, swEx;
    private String tag = "";
    private Intent myIntent1, myIntent2, myIntent3;
    private RadioGroup rgDrink;
    private RadioButton rbDrink1, rbDrink2, rbDrink3, rbDrink4;
    private int timeToDrink = 0;
    private ArrayList<Integer> arrayChannel;
    SharedPreferences pre;
    SharedPreferences.Editor edit;
    Toolbar tbRemind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind);
        //getSupportActionBar().setTitle("Remind for health");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        anhXa();
        tbRemind.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        pre = getSharedPreferences("DrinkorNot", MODE_PRIVATE);
        edit = pre.edit();
        arrayChannel = new ArrayList<>();
        setBeginTime();
        checkSwitch();
        checkTextView();
        checkRadioButton();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        int t = 0;
        if (tag.equals("breakfast")){
            Toast.makeText(this, "Set breakfast time", Toast.LENGTH_SHORT).show();
            t = 1;
        }
        if (tag.equals("lunch")){
            Toast.makeText(this, "Set lunch time", Toast.LENGTH_SHORT).show();
            t = 2;
        }
        if (tag.equals("dinner")){
            Toast.makeText(this, "Set dinner time", Toast.LENGTH_SHORT).show();
            t = 3;
        }
        if (tag.equals("excercise")){
            Toast.makeText(this, "Set excercise time", Toast.LENGTH_SHORT).show();
            t = 10;
        }
        updateTimeText(c, t);
        startAlarm(c, t);
    }

    public void checkSwitch () {
        swEx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    edit.putBoolean("Excercise", false);
                    edit.commit();
                    cancelAlarm(10);
                    tvEx.setVisibility(View.INVISIBLE);
                } else {
                    edit.putBoolean("Excercise", true);
                    edit.commit();
                    tvEx.setVisibility(View.VISIBLE);
                }
            }
        });
        swBreakfast.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    edit.putBoolean("Breakfast", false);
                    edit.commit();
                    cancelAlarm(1);
                    tvBreakfast.setVisibility(View.INVISIBLE);
                } else {
                    edit.putBoolean("Breakfast", true);
                    edit.commit();
                    tvBreakfast.setVisibility(View.VISIBLE);
                }
            }
        });
        swLunch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    edit.putBoolean("Lunch", false);
                    edit.commit();
                    cancelAlarm(2);
                    tvLunch.setVisibility(View.INVISIBLE);
                } else {
                    edit.putBoolean("Lunch", true);
                    edit.commit();
                    tvLunch.setVisibility(View.VISIBLE);
                }
            }
        });
        swDinner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    edit.putBoolean("Dinner", false);
                    edit.commit();
                    cancelAlarm(3);
                    tvDinner.setVisibility(View.INVISIBLE);
                } else {
                    edit.putBoolean("Dinner", true);
                    edit.commit();
                    tvDinner.setVisibility(View.VISIBLE);
                }
            }
        });
        swDrink.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    edit.putBoolean("DrinkorNot", false);
                    edit.commit();
                    cancelArrayAlarm(arrayChannel);
                    arrayChannel.clear();
                    rbDrink1.setVisibility(View.GONE);
                    rbDrink2.setVisibility(View.GONE);
                    rbDrink3.setVisibility(View.GONE);
                    rbDrink4.setVisibility(View.GONE);
                    tvDrinkQuestion.setVisibility(View.GONE);
                } else {
                    edit.putBoolean("DrinkorNot", true);
                    edit.commit();
                    rbDrink1.setVisibility(View.VISIBLE);
                    rbDrink2.setVisibility(View.VISIBLE);
                    rbDrink3.setVisibility(View.VISIBLE);
                    rbDrink4.setVisibility(View.VISIBLE);
                    tvDrinkQuestion.setVisibility(View.VISIBLE);
                    rbDrink1.setChecked(false);
                    rbDrink2.setChecked(false);
                    rbDrink3.setChecked(false);
                    rbDrink4.setChecked(false);
                }
            }
        });
    }

    public void checkTextView () {
        tvEx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swEx.isChecked()){
                    DialogFragment timePicker = new TimePickerFragment();
                    tag = "excercise";
                    timePicker.show(getSupportFragmentManager(), "excercise");
                }
            }
        });

        tvBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swBreakfast.isChecked()){
                    DialogFragment timePicker = new TimePickerFragment();
                    tag = "breakfast";
                    timePicker.show(getSupportFragmentManager(), "breakfast");
                }

            }
        });

        tvLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swLunch.isChecked()){
                    DialogFragment timePicker = new TimePickerFragment();
                    tag = "lunch";
                    timePicker.show(getSupportFragmentManager(), "lunch");
                }

            }
        });
        tvDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swDinner.isChecked()){
                    DialogFragment timePicker = new TimePickerFragment();
                    tag = "dinner";
                    timePicker.show(getSupportFragmentManager(), "dinner");
                }

            }
        });
    }

    public void checkRadioButton () {
        rbDrink1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.putString("DrinkHours", "1h");
                edit.commit();
                cancelArrayAlarm(arrayChannel);
                arrayChannel.clear();
                timeToDrink = 1;
                Toast.makeText(RemindActivity.this, "Best period to drink", Toast.LENGTH_SHORT).show();
                Calendar c = Calendar.getInstance();
                int times = 24/timeToDrink;
                Date d = c.getTime();
                for (int i = 0; i < times; i++) {
                    c.set(Calendar.HOUR_OF_DAY, d.getHours() + timeToDrink * i);
                    c.set(Calendar.MINUTE, d.getMinutes());
                    c.set(Calendar.SECOND, d.getSeconds() + 5);
                    startAlarm(c, 5 + i);
                    arrayChannel.add(i+5);
                }

            }
        });
        rbDrink2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.putString("DrinkHours", "2h");
                edit.commit();
                cancelArrayAlarm(arrayChannel);
                arrayChannel.clear();
                timeToDrink = 2;
                Toast.makeText(RemindActivity.this, "Normal period to drink", Toast.LENGTH_SHORT).show();
                Calendar c = Calendar.getInstance();
                int times = 24/timeToDrink;
                Date d = c.getTime();
                for (int i = 0; i < times; i++) {
                    c.set(Calendar.HOUR_OF_DAY, d.getHours() + timeToDrink * i);
                    c.set(Calendar.MINUTE, d.getMinutes());
                    c.set(Calendar.SECOND, d.getSeconds() + 5);
                    startAlarm(c, 5 + i);
                    arrayChannel.add(i+5);
                }

            }
        });
        rbDrink3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.putString("DrinkHours", "3h");
                edit.commit();
                cancelArrayAlarm(arrayChannel);
                arrayChannel.clear();
                timeToDrink = 3;
                Toast.makeText(RemindActivity.this, "Let's drink enough water", Toast.LENGTH_SHORT).show();
                Calendar c = Calendar.getInstance();
                int times = 24/timeToDrink;
                Date d = c.getTime();
                for (int i = 0; i < times; i++) {
                    c.set(Calendar.HOUR_OF_DAY, d.getHours() + timeToDrink * i);
                    c.set(Calendar.MINUTE, d.getMinutes());
                    c.set(Calendar.SECOND, d.getSeconds() + 5);
                    startAlarm(c, 5 + i);
                    arrayChannel.add(i+5);
                }

            }
        });
        rbDrink4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.putString("DrinkHours", "4h");
                edit.commit();
                cancelArrayAlarm(arrayChannel);
                arrayChannel.clear();
                timeToDrink = 4;
                Toast.makeText(RemindActivity.this, "You must be so busy", Toast.LENGTH_SHORT).show();
                Calendar c = Calendar.getInstance();
                int times = 24/timeToDrink;
                Date d = c.getTime();
                for (int i = 0; i < times; i++) {
                    c.set(Calendar.HOUR_OF_DAY, d.getHours() + timeToDrink * i);
                    c.set(Calendar.MINUTE, d.getMinutes());
                    c.set(Calendar.SECOND, d.getSeconds() + 5);
                    startAlarm(c, 5 + i);
                    arrayChannel.add(i+5);
                }

            }
        });
    }

    public void setBeginTime() {
        Boolean drinkornot = pre.getBoolean("DrinkorNot", false);
        if (drinkornot) swDrink.setChecked(true);
        else swDrink.setChecked(false);

        Boolean excercise = pre.getBoolean("Excercise", false);
        if (excercise) {
            swEx.setChecked(true);
            tvEx.setText(pre.getString("ExcerciseTime", "12:00"));
        }
        else swEx.setChecked(false);

        Boolean breakfast = pre.getBoolean("Breakfast", false);
        if (breakfast) {
            swBreakfast.setChecked(true);
            tvBreakfast.setText(pre.getString("BreakfastTime", "12:00"));
        }
        else swBreakfast.setChecked(false);
        Boolean lunch = pre.getBoolean("Lunch", false);
        if (lunch) {
            swLunch.setChecked(true);
            tvLunch.setText(pre.getString("LunchTime", "12:00"));
        }
        else swLunch.setChecked(false);
        Boolean dinner = pre.getBoolean("Dinner", false);
        if (dinner){
            swDinner.setChecked(true);
            tvDinner.setText(pre.getString("DinnerTime", "12:00"));
        }
        else swDinner.setChecked(false);

        if (!swBreakfast.isChecked()) tvBreakfast.setVisibility(View.INVISIBLE);
        if (!swLunch.isChecked()) tvLunch.setVisibility(View.INVISIBLE);
        if (!swDinner.isChecked()) tvDinner.setVisibility(View.INVISIBLE);
        if (!swDrink.isChecked()) {
            rbDrink1.setVisibility(View.GONE);
            rbDrink2.setVisibility(View.GONE);
            rbDrink3.setVisibility(View.GONE);
            rbDrink4.setVisibility(View.GONE);
            tvDrinkQuestion.setVisibility(View.GONE);
        } else {
            rbDrink1.setChecked(false);
            rbDrink2.setChecked(false);
            rbDrink3.setChecked(false);
            rbDrink4.setChecked(false);
            rbDrink1.setVisibility(View.VISIBLE);
            rbDrink2.setVisibility(View.VISIBLE);
            rbDrink3.setVisibility(View.VISIBLE);
            rbDrink4.setVisibility(View.VISIBLE);
            tvDrinkQuestion.setVisibility(View.VISIBLE);


            String drinkHours = pre.getString("DrinkHours", "1h");
            if (drinkHours.equals("1h")) rbDrink1.setChecked(true);
            if (drinkHours.equals("2h")) rbDrink2.setChecked(true);
            if (drinkHours.equals("3h")) rbDrink3.setChecked(true);
            if (drinkHours.equals("4h")) rbDrink4.setChecked(true);
        }
    }

    private void updateTimeText(Calendar c, int type) {
        String timeText = "";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        timeText += " (24h)";
        switch (type) {
            case 1:
                tvBreakfast.setText(timeText);
                edit.putString("BreakfastTime", timeText);
                edit.commit();
                break;
            case 2:
                edit.putString("LunchTime", timeText);
                edit.commit();
                tvLunch.setText(timeText);
                break;
            case 3:
                edit.putString("DinnerTime", timeText);
                edit.commit();
                tvDinner.setText(timeText);
                break;
            case 10:
                edit.putString("ExcerciseTime", timeText);
                edit.commit();
                tvEx.setText(timeText);
                break;
        }
    }

    private void startAlarm(Calendar c, int type) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("type", type);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, type, intent, 0);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm(int type) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, type, intent, 0);
        alarmManager.cancel(pendingIntent);
    }

    public void cancelArrayAlarm (ArrayList<Integer> arrayList) {
        for (int i : arrayList) {
            cancelAlarm(i);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void anhXa() {
        tvBreakfast = findViewById(R.id.tvBreakfast);
        tvLunch = findViewById(R.id.tvLunch);
        tvDinner = findViewById(R.id.tvDinnner);
        swBreakfast = findViewById(R.id.swBreakfast);
        swLunch = findViewById(R.id.swLunch);
        swDinner = findViewById(R.id.swDinner);
        swDrink = findViewById(R.id.swDrink);
        rgDrink = findViewById(R.id.rgDrink);
        rbDrink1 = findViewById(R.id.rbDrink1);
        rbDrink2 = findViewById(R.id.rbDrink2);
        rbDrink3 = findViewById(R.id.rbDrink3);
        rbDrink4 = findViewById(R.id.rbDrink4);
        tvDrinkQuestion = findViewById(R.id.tvDrinkQuestion);
        tbRemind = findViewById(R.id.tbRemind);
        tvEx = findViewById(R.id.tvExcercise);
        swEx = findViewById(R.id.swExcercise);
    }
}