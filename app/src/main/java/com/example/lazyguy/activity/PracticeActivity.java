package com.example.lazyguy.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.lazyguy.R;
import com.example.lazyguy.fragment.BMIInfoDialogFragment;
import com.example.lazyguy.fragment.YoutubeFragment;
import com.example.lazyguy.model.Datasheet;
import com.example.lazyguy.model.Excercise;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class PracticeActivity extends AppCompatActivity {

    private String exid, nextid, explorer, program;
    private DatabaseReference mData;
    private Excercise currentExcercise;
    private Datasheet currentDatasheet;
    public static final String API_KEY = "AIzaSyDiknFb_PAk9uXW7xCeK72mkxqvRcr8H3M";
    private String videoId;
    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running, soundOn = true, next = false, wait = true;
    YoutubeFragment fragment;
    private TextView tvExNumber;
    private TextToSpeech mTTS;
    private AudioManager audioManager;
    private int volume;
    private Button btPracticePause;
    private String endPart = "";
    private Toolbar tbPractice;
    SharedPreferences pre;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        anhXa();
        pre = getSharedPreferences("SavingPractice", MODE_PRIVATE);
        edit = pre.edit();
        onClick();
        tbPractice.inflateMenu(R.menu.practicemenu);
        mData = FirebaseDatabase.getInstance().getReference();
        audioManager = (AudioManager) getSystemService(getApplicationContext().AUDIO_SERVICE);
        volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        Intent intent = this.getIntent();
        exid = intent.getStringExtra("exid");
        explorer = intent.getStringExtra("explorer");
        program = intent.getStringExtra("program");
        initYoutubeFragment();
        getCurrent();

        chronometer.setFormat("%s / 00:10");
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
        chronometer.start();
        running = true;
        tvExNumber.setText("Waiting");
        wait = true;
        //save data
        edit.putString("explorer", explorer);
        edit.putString("program", program);
        edit.putString("exid", exid);
        edit.commit();

        if (explorer.equals("Custom")){
            mData.child("Custom").child("End" + program).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    endPart = dataSnapshot.getValue().toString();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } else {
            mData.child("Datasheet").child(explorer).child("End" + program).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    endPart = dataSnapshot.getValue().toString();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.ENGLISH);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(PracticeActivity.this, "Language not surpported", Toast.LENGTH_SHORT).show();
                    } else {
                        speak("Waiting 10 seconds. Your first movement is " + currentExcercise.getName());
                    }
                } else {
                    Toast.makeText(PracticeActivity.this, "Initialization failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void onClick() {
        tbPractice.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tbPractice.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_practice_sound) {
                    if (soundOn) {
                        item.setIcon(R.drawable.ic_volume_off_black_24dp);
                        soundOn = false;
                        volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                    } else {
                        item.setIcon(R.drawable.ic_volume_up_black_24dp);
                        soundOn = true;
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
                    }
                    return true;
                }
                if (id == R.id.action_practice_info) {
                    BMIInfoDialogFragment dialog = new BMIInfoDialogFragment();
                    dialog.setIndex(currentExcercise.getDetail(), "Excercise guide", null);
                    dialog.show(getSupportFragmentManager(), "BmiInfoDialog");
                    return true;
                }
                return false;
            }
        });
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if ((SystemClock.elapsedRealtime() - chronometer.getBase()) > 11000 && wait) {
                    speak(currentExcercise.getName() + currentExcercise.getDetail());
                    int time = currentDatasheet.getRepetition() * 5;
                    chronometer.setFormat("%s / 0:" + time);
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    pauseOffset = 0;
                    tvExNumber.setText("Repetition: " + currentDatasheet.getRepetition() + "   Set: " + currentDatasheet.getSet());
                    wait = false;
                }
                if (currentDatasheet != null) {
                    if ((SystemClock.elapsedRealtime() - chronometer.getBase()) > (currentDatasheet.getRepetition() * 5000 + 1000) && !wait) {
                        if (nextid.equals(endPart)){
                            Toast.makeText(PracticeActivity.this, "You completed " + program + " in " + explorer, Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                        speak("Waiting 10 seconds to your next movement");
                        chronometer.setFormat("%s / 00:10");
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        pauseOffset = 0;
                        exid = nextid;
                        getCurrent();
                        initYoutubeFragment();
                        tvExNumber.setText("Waiting");
                        wait = true;
                        //save data
                        edit.putString("explorer", explorer);
                        edit.putString("program", program);
                        edit.putString("exid", exid);
                        edit.commit();
                    }
                }
            }
        });
    }

    private void speak(String text) {
        float pitch = 1.0f, speed = 0.75f;
        mTTS.setPitch(pitch);
        mTTS.setSpeechRate(speed);
        mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void getCurrent () {
        if (explorer.equals("Custom")) {
            mData.child("Custom").child(program).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Datasheet dd = dataSnapshot.getValue(Datasheet.class);
                    if (next == true) {
                        nextid = dd.getExid();
                        next = false;
                    }
                    if (exid.equals(dd.getExid())) {
                        currentDatasheet = dd;
                        next = true;
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
        else {
            mData.child("Datasheet").child(explorer).child(program).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Datasheet d = dataSnapshot.getValue(Datasheet.class);
                    if (next == true) {
                        nextid = d.getExid();
                        next = false;
                    }
                    if (exid.equals(d.getExid())) {
                        currentDatasheet = d;
                        next = true;
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
                if (exid.equals(dataSnapshot.getKey())){
                    currentExcercise = dataSnapshot.getValue(Excercise.class);
                    tbPractice.setTitle(currentExcercise.getName());
                    videoId = currentExcercise.getUrl();
                    fragment.setVideoId(videoId);
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

    public void initYoutubeFragment () {
        fragment = new YoutubeFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.relaPractice, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void startChronometer(View v) {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
        }
    }

    public void pauseChronometer(View v) {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }
    }

    public void resetChronometer(View v) {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }

    public void skip(View view) {
        if (!running) {
            btPracticePause.setText("Pause");
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
        }
        if (wait && currentDatasheet != null) {
            speak(currentExcercise.getName() + currentExcercise.getDetail());
            getCurrent();
            int time = currentDatasheet.getRepetition() * 5;
            chronometer.setFormat("%s / 0:" + time);
            chronometer.setBase(SystemClock.elapsedRealtime());
            pauseOffset = 0;
            tvExNumber.setText("Repetition: " + currentDatasheet.getRepetition() + "   Set: " + currentDatasheet.getSet());
            wait = false;
            //save data
            edit.putString("explorer", explorer);
            edit.putString("program", program);
            edit.putString("exid", exid);
            edit.commit();
            return;
        }
        if (!wait && currentDatasheet != null) {
            if (nextid.equals(endPart)){
                Toast.makeText(PracticeActivity.this, "You completed " + program + " in " + explorer, Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            speak("Waiting 10 seconds to your next movement");
            chronometer.setFormat("%s / 00:10");
            chronometer.setBase(SystemClock.elapsedRealtime());
            pauseOffset = 0;
            exid = nextid;
            getCurrent();
            initYoutubeFragment();
            tvExNumber.setText("Waiting");
            wait = true;
            return;
        }
    }

    public void pause(View view) {
        if (running) {
            btPracticePause.setText("Start");
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }
        else {
            btPracticePause.setText("Pause");
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
        }
    }

    public void anhXa() {
        chronometer = findViewById(R.id.chronometer);
        tvExNumber = findViewById(R.id.tvExNumber);
        btPracticePause = findViewById(R.id.btPracticePause);
        tbPractice = findViewById(R.id.tbPractice);
    }

    @Override
    protected void onDestroy() {
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();
    }

}
