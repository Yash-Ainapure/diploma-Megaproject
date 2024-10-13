package com.example.mega;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Objects;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    public static final int LOCATION_REQUEST = 500;
    private GoogleMap mMap;
    Double lat, log;
    ArrayList<LatLng> mMarkerPoints;
    private DatabaseReference myRef;

    Double lat_points = null, long_points = null;
    LatLng latLng;
    private LatLng mOrigin;
    private LatLng mDestination;
    private Polyline mPolyline;
    String ar[];
    protected LocationManager locationManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        } catch (NullPointerException e) {
            Toast.makeText(getApplicationContext(), "Null Pointer Exception occurred", Toast.LENGTH_SHORT).show();
        }
         getLiveLocation();
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mMarkerPoints = new ArrayList<>();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), user_page.class);
        startActivity(i);
        finish();
    }

    synchronized void getLiveLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, this);
    }


    //    synchronized void database() {
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//                        locations l = snapshot1.getValue(locations.class);
//                        if (l != null) {
//                            lat_points += l.getLatitude();
//                        }
//                        if (l != null) {
//                            long_points += l.getLongitude();
//                        }
//                        Toast.makeText(MapsActivity.this, lat_points + "|" + long_points, Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(MapsActivity.this, "database error", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        mMap.setMyLocationEnabled(true);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            public void onMapLongClick(@NonNull LatLng latLng) {
                if (mMarkerPoints.size() > 1) {
                    mMarkerPoints.clear();
                    mMap.clear();
                }
                mMarkerPoints.add(latLng);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                if (mMarkerPoints.size() == 1) {
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                } else {
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                }
                mMap.addMarker(markerOptions);

                if (mMarkerPoints.size() == 2) {
                    //String url=getRequestUrl(mMarkerPoints.get(0),mMarkerPoints.get(1));
                    mOrigin = mMarkerPoints.get(0);
                    mDestination = mMarkerPoints.get(1);
                    Toast.makeText(MapsActivity.this, "drawing...", Toast.LENGTH_SHORT).show();
                    drawRoute(mMap);
                }
            }
        });
    }
    private void drawRoute(GoogleMap mMap) {
        Toast.makeText(this, "drawing route", Toast.LENGTH_SHORT).show();
        String url = getDirectionsUrl(mOrigin, mDestination);
        DownloadTask downloadTask = new DownloadTask(mMap);
        downloadTask.execute(url);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        lat = location.getLatitude();
        log = location.getLongitude();
        latLng = new LatLng(lat, log);
        if (mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }
//        Toast.makeText(this, "Location changed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Toast.makeText(this, "Status changed", Toast.LENGTH_SHORT).show();
//        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        Toast.makeText(this, "Provider enabled", Toast.LENGTH_SHORT).show();
//        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Toast.makeText(this, "Provider disabled"+provider, Toast.LENGTH_SHORT).show();
        if (provider.equals(LocationManager.GPS_PROVIDER)) {
            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }




    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        Log.d("origin", String.valueOf(origin));
        Log.d("dest getting location route", String.valueOf(dest));
        Toast.makeText(this, "getting routeee", Toast.LENGTH_SHORT).show();
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
// Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
// Key
        String key = "key=" + "AIzaSyDPZreUD3bkxr76bGBEuXqphLaCJINVE4s";
        String sensor="sensor=false";
        String mode="mode=driving";
// Building the parameters to the web service
       String parameters = str_origin + "&" + str_dest + "&" + key;
        String output = "json";
// Building the url to the web service
        String url =
                "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;

    }
}