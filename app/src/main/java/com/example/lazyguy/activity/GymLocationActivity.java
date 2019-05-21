package com.example.lazyguy.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lazyguy.GetNearByPlaces;
import com.example.lazyguy.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class GymLocationActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener
        , GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap map;
    private SearchView svLocation;
    private FloatingActionButton fbtnMapLayer, fbtnGym, fbtnMyLocation;
    private TextView tvRadius;
    private SeekBar sbRadius;
    private int mapLayer = 2;
    GoogleApiClient client;
    LocationRequest request;
    LatLng latLngCurrent, latLngSearching;
    float radiusGym = 1;
    Toolbar tbGym;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_location);
        //getSupportActionBar().setTitle("Location for Gym");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        anhXa();
        tbGym.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        final MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.myMap);

        fbtnMapLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mapLayer) {
                    case 1:
                        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        Toast.makeText(GymLocationActivity.this, "Normal Map", Toast.LENGTH_SHORT).show();
                        mapLayer = 2;
                        break;
                    case 2:
                        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        Toast.makeText(GymLocationActivity.this, "Satellite Map", Toast.LENGTH_SHORT).show();
                        mapLayer = 3;
                        break;
                    case 3:
                        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        Toast.makeText(GymLocationActivity.this, "Terrain Map", Toast.LENGTH_SHORT).show();
                        mapLayer = 1;
                        break;
                }
            }
        });
        fbtnGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                stringBuilder.append("location=" + latLngSearching.latitude + "," + latLngSearching.longitude);
                stringBuilder.append("&radius="+radiusGym*1000);
                stringBuilder.append("&types="+"gym");
                stringBuilder.append("&key=" + getString(R.string.map_api_key));
                String url = stringBuilder.toString();
                Object[] dataTransfer = new Object[2];
                dataTransfer[0] = map;
                dataTransfer[1] = url;

                GetNearByPlaces getNearByPlaces = new GetNearByPlaces();
                getNearByPlaces.execute(dataTransfer);
                if (!getNearByPlaces.getMessage().equals("")){
                    Toast.makeText(GymLocationActivity.this, getNearByPlaces.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        fbtnMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //latLngCurrent = new LatLng(21.0869263,105.815237);
                map.clear();
                latLngSearching = latLngCurrent;
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLngCurrent, 14);
                map.animateCamera(update);
                MarkerOptions options = new MarkerOptions();
                options.position(latLngCurrent);
                options.title("Current Location");
                map.addMarker(options);
            }
        });
        sbRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radiusGym = (1000 + (float) progress * 40) / 1000;
                tvRadius.setText(radiusGym + " km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        svLocation.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String location = svLocation.getQuery().toString();
                List<Address> addressList = null;

                if (location != null || !location.equals("")) {
                    map.clear();
                    Geocoder geocoder = new Geocoder(GymLocationActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        map.addMarker(new MarkerOptions().position(latLng).title(location));
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                        latLngSearching = latLng;

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(GymLocationActivity.this, "Location not found", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

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
        fbtnMapLayer = findViewById(R.id.fbtnMapLayer);
        fbtnGym = findViewById(R.id.fbtnGym);
        fbtnMyLocation = findViewById(R.id.fbtnMyLocation);
        svLocation = findViewById(R.id.svLocation);
        tvRadius = findViewById(R.id.tvRadius);
        sbRadius = findViewById(R.id.sbRadius);
        tbGym = findViewById(R.id.tbGym);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        client = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        client.connect();

        latLngCurrent = new LatLng(21.0287797,105.850176);
        latLngSearching = new LatLng(21.0287797,105.850176);
        map.clear();
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLngCurrent, 14);
        map.animateCamera(update);
        MarkerOptions options = new MarkerOptions();
        options.position(latLngCurrent);
        options.title("Ha Noi, Viet Nam");
        map.addMarker(options);

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(GymLocationActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(GymLocationActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(GymLocationActivity.this,
                    Manifest.permission.READ_CONTACTS)) {
            } else {
                ActivityCompat.requestPermissions(GymLocationActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        0);
            }
            return;
        }
        map.setMyLocationEnabled(true);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null) {
            Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
        } else {
            //if (latLngCurrent == null) {
                latLngCurrent = new LatLng(location.getLatitude(), location.getLongitude());
//                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLngCurrent, 15);
//                map.animateCamera(update);
//                MarkerOptions options = new MarkerOptions();
//                options.position(latLngCurrent);
//                options.title("Current Location");
//                map.addMarker(options);
            }
        //}
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        request = new LocationRequest().create();
        request.setInterval(1000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(client, request, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
