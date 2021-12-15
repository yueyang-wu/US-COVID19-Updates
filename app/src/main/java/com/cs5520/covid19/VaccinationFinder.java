package com.cs5520.covid19;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VaccinationFinder extends AppCompatActivity implements OnMapReadyCallback {
    private static int PROVIDER_NUMBER = 5;
    private GoogleMap mMap;
    LocationManager locationManager;
    Location location;
    Button btn_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // set back_to_home button to go to home page
        btn_back = (Button) findViewById(R.id.back_to_home);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VaccinationFinder.this, HomePage.class));
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     *
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        double latitude = 0, longitude = 0;

        // check and ask for user permission to share their current location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        // get current location
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }


        // Add a marker in current location
        LatLng currLocation = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions()
                .position(currLocation)
                .title("Your current location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currLocation));


        // get nearby vaccination provider
        int providerDisplayed = 0, i = 0;
        while (providerDisplayed < PROVIDER_NUMBER && i < StatisticJsonData.providerLocation.length()) {
            try {
                JSONObject jasonObject = StatisticJsonData.providerLocation.getJSONObject(i);
                double lat = Double.parseDouble(jasonObject.getString("latitude"));
                double lon = Double.parseDouble(jasonObject.getString("longitude"));
                if (lat >= latitude - 0.5 && lat <= latitude + 0.5 && lon >= longitude - 0.5 && lon <= longitude + 0.5) {
                    String info = jasonObject.getString("loc_name");
                    LatLng providerLocation = new LatLng(lat, lon);
                    mMap.addMarker(new MarkerOptions().position(providerLocation).title(info));
                    providerDisplayed++;
                }
                i++;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
