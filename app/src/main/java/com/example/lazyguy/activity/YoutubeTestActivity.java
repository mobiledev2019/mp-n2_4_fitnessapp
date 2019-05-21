package com.example.lazyguy.activity;

import android.app.Notification;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.AlarmClock;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lazyguy.R;
import com.example.lazyguy.fragment.YoutubeFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class YoutubeTestActivity extends AppCompatActivity {

    private TextView tvHelloWorld;
    private TextToSpeech mTTS;
    private static final Uri ALARM_URI = Uri.parse("android-app://com.myclockapp/set_alarm_page");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_test);
        getSupportActionBar().setTitle("Test");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        anhXa();

        JsonParse j = new JsonParse();
        j.execute();

        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.ENGLISH);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(YoutubeTestActivity.this, "Language not surpported", Toast.LENGTH_SHORT).show();
                    } else {
                    }
                } else {
                    Toast.makeText(YoutubeTestActivity.this, "Initialization failed", Toast.LENGTH_SHORT).show();
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

    public void anhXa() {
        tvHelloWorld = findViewById(R.id.tvHelloWorld);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class JsonParse extends AsyncTask<Object, String, String> {
        public String data;
        InputStream is;
        BufferedReader bufferedReader;
        StringBuilder stringBuilder;

        @Override
        protected String doInBackground(Object... objects) {
            try {
                URL myUrl = new URL("http://api.openweathermap.org/data/2.5/weather?q=London,uk&APPID=1364d078d44487625a44d854756268cb");
                //URL myUrl = new URL("https://secure-gorge-93555.herokuapp.com/api");
                HttpURLConnection httpURLConnection = (HttpURLConnection) myUrl.openConnection();
                httpURLConnection.connect();
                is = httpURLConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(is));

                String line = "";
                stringBuilder = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                data = stringBuilder.toString();

                try {
                    JSONObject parrentObject = new JSONObject(data);
                    if (parrentObject == null) Toast.makeText(YoutubeTestActivity.this, "No data", Toast.LENGTH_SHORT).show();
                    String base = (String) parrentObject.get("base");
                    tvHelloWorld.setText(base);
                    JSONArray weatherArray = (JSONArray) parrentObject.get("weather");
                    for (int i = 0; i < weatherArray.length(); i++) {
                        JSONObject w = (JSONObject) weatherArray.get(i);
                        int id = (int) w.get("id");
                        tvHelloWorld.append(id + "");
                    }
//                    Double temp = (Double) parrentObject.get("celsiusTemp");
//                    Double humi = (Double) parrentObject.get("humidityTemp");
//                    tvHelloWorld.setText("Nhiệt độ: " + temp + " Độ ẩm" + humi);
                } catch (Exception j) {
                    j.printStackTrace();
                    Log.d("errortemp","Lỗi: " + j.toString());
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException i) {
                i.printStackTrace();
            }
            return data;
        }

        public String getData() {
            return data;
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }


}
