package com.example.lazyguy.fragment;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lazyguy.activity.GymLocationActivity;
import com.example.lazyguy.activity.ProfileActivity;
import com.example.lazyguy.activity.ProgramActivity;
import com.example.lazyguy.R;
import com.example.lazyguy.activity.RemindActivity;
import com.example.lazyguy.adapter.ExplorerAdapter;
import com.example.lazyguy.model.Explorer;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class ExploreFragment extends Fragment {

    private ListView lvExplorer;
    private ArrayList<Explorer> arrayListExplorer;
    private ArrayList<String> strings;
    private ExplorerAdapter explorerAdapter;
    private DatabaseReference mData;
    private FloatingActionButton fbtMic;
    TextToSpeech mTTS;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_explore, container, false);
        AnhXa(rootView);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        Boolean mic = sharedPref.getBoolean("assistant_switch", true);
        if (mic) fbtMic.setVisibility(View.VISIBLE);
        else fbtMic.setVisibility(View.INVISIBLE);

        arrayListExplorer = new ArrayList<>();
        strings = new ArrayList<>();
        //arrayListExplorer = explorerDAO.getExplorers();
        explorerAdapter = new ExplorerAdapter(getActivity(), R.layout.item_listview, arrayListExplorer);
        lvExplorer.setAdapter(explorerAdapter);

        mData = FirebaseDatabase.getInstance().getReference();
        mData.child("Explorer").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String key = dataSnapshot.getKey();
                Explorer e = dataSnapshot.getValue(Explorer.class);
                arrayListExplorer.add(e);
                strings.add(key);
                explorerAdapter.notifyDataSetChanged();
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

        mTTS = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(getContext(), "Language not surpported", Toast.LENGTH_SHORT).show();
                    } else {
                    }
                } else {
                    Toast.makeText(getContext(), "Initialization failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        onclick();

        return rootView;
    }

    private void speak(String text) {
        float pitch = 1.0f, speed = 0.75f;
        mTTS.setPitch(pitch);
        mTTS.setSpeechRate(speed);
        if (Build.VERSION.SDK_INT >= 21) {
            mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    private void onclick() {
        //bắt sự kiện click vào listview
        lvExplorer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences pre = getActivity().getSharedPreferences("SavingPractice", MODE_PRIVATE);
                String explorer = pre.getString("explorer", "");
                String program = pre.getString("program", "");
                String exid = pre.getString("exid", "");
                ProcessDialogFragment dialog = new ProcessDialogFragment();
                String ex = arrayListExplorer.get(position).getName();
                dialog.setIndex(explorer, program, exid, ex);
                dialog.show(getFragmentManager(), "ProcessDialog");
            }
        });

        fbtMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak("What do you want boss ?");
                record();
            }
        });
    }

    private void record() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi, speak something");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_SPEECH_INPUT:
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    takeorder(result.get(0));
                    Toast.makeText(getContext(), result.get(0), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void takeorder(String text) {
        boolean recognize = false;
        text = text.toLowerCase();
        if (text.indexOf("what's") != -1) {
            if (text.indexOf("your name") != -1) {
                //speak("My name is Friday. Thanks for asking.");
                speak("Tôi là trợ lý ảo của bạn");
                recognize = true;
            }
        }
        if (text.indexOf("time") != -1) {
            Date now = new Date();
            String time = DateUtils.formatDateTime(getContext(), now.getTime(), DateUtils.FORMAT_SHOW_TIME);
            speak("The time now is " + time);
            recognize = true;
        }
        if (text.indexOf("open") != -1) {
            if (text.indexOf("google") != -1) {
                speak("Google has just been opened.");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/"));
                startActivity(intent);
                recognize = true;
            }
        }
        if (text.indexOf("remind") != -1) {
            speak("Open remind");
            recognize = true;
            Intent intent = new Intent(getContext(), RemindActivity.class);
            startActivity(intent);
        }
        if (text.indexOf("location") != -1) {
            speak("Open gym location");
            recognize = true;
            Intent intent = new Intent(getContext(), GymLocationActivity.class);
            startActivity(intent);
        }
        if (text.indexOf("profile") != -1) {
            speak("Open profile");
            recognize = true;
            Intent intent = new Intent(getContext(), ProfileActivity.class);
            startActivity(intent);
        }
        if (!recognize) {
            speak("Order can't be recognized.");
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, text);
            startActivity(intent);
        }

    }

    public void AnhXa (View view) {
        lvExplorer = view.findViewById(R.id.lvExplorer);
        fbtMic = view.findViewById(R.id.fbtMic);
    }
}
