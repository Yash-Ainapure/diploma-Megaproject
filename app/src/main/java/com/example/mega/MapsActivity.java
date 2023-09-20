package com.example.mega;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Objects;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback/*, LocationListener*/ {
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
        // getLiveLocation();
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

//    synchronized void getLiveLocation() {
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
//    }

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
    }
}

//        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
//            @Override
//            public void onMapLongClick(@NonNull LatLng latLng) {
//                if (mMarkerPoints.size() > 1) {
//                    mMarkerPoints.clear();
//                    mMap.clear();
//                }
//                mMarkerPoints.add(latLng);
//                MarkerOptions markerOptions = new MarkerOptions();
//                markerOptions.position(latLng);
//                if (mMarkerPoints.size() == 1) {
//                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//                } else {
//                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//
//                }
//                mMap.addMarker(markerOptions);
//
//                if (mMarkerPoints.size() == 2) {
//                    //String url=getRequestUrl(mMarkerPoints.get(0),mMarkerPoints.get(1));
//                    mOrigin = mMarkerPoints.get(0);
//                    mDestination = mMarkerPoints.get(1);
//                    drawRoute();
//                }
//            }
//        });
//    private void drawRoute() {
//        String url = getDirectionsUrl(mOrigin, mDestination);
//        DownloadTask downloadTask = new DownloadTask();
//// Start downloading json data from Google Directions API
//        downloadTask.execute(url);
//    }

//    private String getDirectionsUrl(LatLng origin, LatLng dest) {
//        // Origin of route
//        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
//// Destination of route
//        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
//// Key
//        String key = "key=" + "AIzaSyAIQLac3sD2ldZaIdyJZ10bNN9-hvNpN30";
//        String sensor="sensor=false";
//        String mode="mode=driving";
//// Building the parameters to the web service
//       String parameters = str_origin + "&" + str_dest + "&" + key;
//        String output = "json";
//// Building the url to the web service
//        String url =
//                "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
//        return url;
//
//    }
//
//    /**
//     * A method to download json data from url
//     */
//    private String downloadUrl(String strUrl) throws IOException {
//        String data = "";
//        InputStream iStream = null;
//        HttpURLConnection urlConnection = null;
//        try {
//            URL url = new URL(strUrl);
//// Creating an http connection to communicate with url
//            urlConnection = (HttpURLConnection) url.openConnection();
//// Connecting to url
//            urlConnection.connect();
//// Reading data from url
//            iStream = urlConnection.getInputStream();
//            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
//            StringBuffer sb = new StringBuffer();
//            String line = "";
//            while ((line = br.readLine()) != null) {
//                sb.append(line);
//            }
//            data = sb.toString();
//            br.close();
//        } catch (Exception e) {
//            Log.d("Exception on download", e.toString());
//        } finally {
//            iStream.close();
//            urlConnection.disconnect();
//        }
//        return data;
//    }
//
//    @Override
//    public void onLocationChanged(@NonNull Location location) {
//        lat=location.getLatitude();
//        log=location.getLongitude();
//        location.distanceTo(mMap.getMyLocation());
//    }
//
//    private class DownloadTask extends AsyncTask<String, Void, String> {
//        // Downloading data in non-ui thread
//        @Override
//        protected String doInBackground(String... url) {
//// For storing data from web service
//            String data = "";
//            try {
//// Fetching the data from web service
//                data = downloadUrl(url[0]);
//                Log.d("DownloadTask", "DownloadTask : " + data);
//            } catch (Exception e) {
//                Log.d("Background Task", e.toString());
//                Log.e("doinbackground error",e.toString());
//            }
//            return data;
//        }
//        // Executes in UI thread, after the execution of
//// doInBackground()
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            ParserTask parserTask = new ParserTask();
//// Invokes the thread for parsing the JSON data
//            parserTask.execute(result);
//        }
//    }
//    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
//        // Parsing the data in non-ui thread
//        @Override
//        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
//            JSONObject jObject;
//            List<List<HashMap<String, String>>> routes = null;
//            try {
//                jObject = new JSONObject(jsonData[0]);
//                DirectionsJSONParser parser = new DirectionsJSONParser();
//// Starts parsing data
//                routes = parser.parse(jObject);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return routes;
//        }
//        // Executes in UI thread, after the parsing process
//        @Override
//        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
//            ArrayList<LatLng> points = null;
//            PolylineOptions lineOptions = null;
//// Traversing through all the routes
//            for (int i = 0; i < result.size(); i++) {
//                points = new ArrayList<LatLng>();
//                lineOptions = new PolylineOptions();
//// Fetching i-th route
//                List<HashMap<String, String>> path = result.get(i);
//// Fetching all the points in i-th route
//                for (int j = 0; j < path.size(); j++) {
//                    HashMap<String, String> point = path.get(j);
//                    double lat = Double.parseDouble(point.get("lat"));
//                    double lng = Double.parseDouble(point.get("lng"));
//                    LatLng position = new LatLng(lat, lng);
//                    points.add(position);
//                }
//// Adding all the points in the route to LineOptions
//                lineOptions.addAll(points);
//                lineOptions.width(8);
//                lineOptions.color(Color.RED);
//            }
//// Drawing polyline in the Google Map for the i-th route
//            if (lineOptions != null) {
//                if (mPolyline != null) {
//                    mPolyline.remove();
//                }
//                mPolyline = mMap.addPolyline(lineOptions);
//            } else
//                Toast.makeText(getApplicationContext(), "No route is found", Toast.LENGTH_LONG).show();
//        }
//    }
//}