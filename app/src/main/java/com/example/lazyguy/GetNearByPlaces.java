package com.example.lazyguy;

import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.api.services.people.v1.model.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;

public class GetNearByPlaces extends AsyncTask <Object, String, String> {
    GoogleMap map;
    String url;
    InputStream is;
    BufferedReader bufferedReader;
    StringBuilder stringBuilder;
    String data;
    String message = "";

    @Override
    protected String doInBackground(Object... objects) {
        map = (GoogleMap) objects[0];
        url = (String) objects[1];
        try {
            URL myUrl = new URL(url);
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

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        }

        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            JSONObject parrentObject = new JSONObject(s);
            JSONArray resultsArray = parrentObject.getJSONArray("results");
            if (resultsArray.length() == 0) {
                message = "No gym to be found";
            }
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject jsonObject = resultsArray.getJSONObject(i);
                JSONObject locationObj = jsonObject.getJSONObject("geometry").getJSONObject("location");
                String latitude = locationObj.getString("lat");
                String longtitude = locationObj.getString("lng");

                JSONObject nameObject = resultsArray.getJSONObject(i);
                String name = nameObject.getString("name");
                String vicinity = nameObject.getString("vicinity");

                LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longtitude));
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(name);
                markerOptions.snippet(vicinity);
                markerOptions.position(latLng);

                map.addMarker(markerOptions);
            }
        } catch (Exception j) {
            j.printStackTrace();
        }
    }

    public String getMessage() {
        return message;
    }
}
