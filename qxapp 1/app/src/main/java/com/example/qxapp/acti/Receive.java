package com.example.qxapp.acti;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.qxapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.model.DirectionsResult;

import java.util.List;

public class Receive extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private TextView receive_userName, receive_content, receive_time;

    private ImageView back;
    private GoogleMap mMap;
    private UiSettings mUiSettings;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean permissionDenied = false;

    private double longitude;
    private double latitude;
    private GoogleApiClient googleApiClient;

    private double startLatitude = 0;
    private double startLongitude = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //Initializing googleApiClient
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION,},
                    11);
        }
        initData();
        initView();

        //back
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        String userName = intent.getStringExtra("r_userName");
        String content = intent.getStringExtra("r_content");
        String time = intent.getStringExtra("r_time");
        if (!TextUtils.isEmpty(content)) {
            String[] location = content.split(",");
            if (location != null && location.length == 2) {
                startLatitude = Double.parseDouble(location[0]);
                startLongitude = Double.parseDouble(location[1]);
            }
        }
    }

    private void initView() {
        back = findViewById(R.id.receive_back);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(this);
            mMap.setOnMyLocationClickListener(this);
        } else {
            enableMyLocation();
        }
        
        mUiSettings = mMap.getUiSettings();
        mUiSettings.setMyLocationButtonEnabled(true);
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setMapToolbarEnabled(true);
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setScrollGesturesEnabled(true);
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
                mMap.setOnMyLocationButtonClickListener(this);
                mMap.setOnMyLocationClickListener(this);
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }
        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true;
        }
    }

    private void getCurrentLocation() {
//        mMap.clear();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            //Getting longitude and latitude
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            //moving the map to location
            moveMap();
        }
    }

    private void moveMap() {
        /**
         * Creating the latlng object to store lat, long coordinates
         * adding marker to map
         * move the camera with animation
         */
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        LatLng latLng = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("My Location"));

        if (startLatitude != 0 && startLongitude != 0) {

            //Destination Marker
            LatLng endLatLng = new LatLng(startLatitude, startLongitude);
            mMap.addMarker(new MarkerOptions()
                    .position(endLatLng)
                    .icon(BitmapDescriptorFactory.defaultMarker())
                    .title("Destination"));
            // range
            LatLngBounds bounds = new LatLngBounds.Builder()
                    .include(latLng)
                    .include(endLatLng)
                    .build();
            // Move the camera now.
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
            // get route
            GeoApiContext geoApi = new GeoApiContext.Builder()
                    .apiKey(getString(R.string.google_map_api_key))
                    .build();
            try {
                DirectionsResult result = DirectionsApi.getDirections(geoApi, latitude + "," + longitude, startLatitude + "," + startLongitude).await();
                if (result != null) {
                    List<LatLng> decodedPath = PolyUtil.decode(result.routes[0].overviewPolyline.getEncodedPath());
                    mMap.addPolyline(new PolylineOptions().color(R.color.red)
                            .width(16)
                            .clickable(true).addAll(decodedPath));
                    if (!decodedPath.isEmpty()) {
//                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(decodedPath.get(0), 4));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Directions get error", Toast.LENGTH_LONG).show();
            }
        }
        // Position the map's camera near Alice Springs in the center of Australia,
        // and set the zoom factor so most of Canada shows on the screen.
        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 4));
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Log.d("Receive", location.toString());
        LatLng latLng = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("My Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 4));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }
}
