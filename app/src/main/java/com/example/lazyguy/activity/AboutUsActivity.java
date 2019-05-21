package com.example.lazyguy.activity;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lazyguy.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AboutUsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private TextView tvUs;
    private GoogleMap map;
    private Toolbar tbAbout;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        //getSupportActionBar().setTitle("All about us");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        anhXa();
        tbAbout.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tvUs.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.ourMap);
        mapFragment.getMapAsync(this);

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
        tvUs = findViewById(R.id.tvus);
        tbAbout = findViewById(R.id.tbAbout);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng ptit = new LatLng(20.9813316, 105.7852926);
        map.addMarker(new MarkerOptions()
                .position(ptit)
                .title("Lazy Inc Headquater"))
                .setSnippet("Your health is our happiness");
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(ptit, 15));
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
}
