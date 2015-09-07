package com.example.sergbek.googlemapsl18.activity;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.sergbek.googlemapsl18.MyLocationListener;
import com.example.sergbek.googlemapsl18.R;
import com.example.sergbek.googlemapsl18.fragment.MyLocationFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
    
    private GoogleMap mMap;
    private Button mBtnMyLocation;
    private LocationManager mLocationManager;
    private String mProvider;
    private Location mLocation;
    private MyLocationListener mLocationListener;

    private static Context sContext;

    private static final long MINIMUM_DISTANCE_FOR_UPDATES = 10;
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 2000;

    private  double lat ;
    private  double lng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sContext=getBaseContext();

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        defineComponents();
        selectBestProvider();
        mLocation = mLocationManager.getLastKnownLocation(mProvider);
        mLocationListener = new MyLocationListener(this);
        showCurrentLocation(mLocation);
        initMap();
        mBtnMyLocation.setOnClickListener(this);
        Log.d("tag",mProvider);
    }

    private void selectBestProvider() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        mProvider = mLocationManager.getBestProvider(criteria, true);
    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_AM);
        mapFragment.getMapAsync(this);
    }

    private void defineComponents() {
        mBtnMyLocation= (Button) findViewById(R.id.btn_myLocation_AM);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        mMap.setTrafficEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onClick(View v) {
        MyLocationFragment myLocationFragment=new MyLocationFragment();
        Bundle bundle=new Bundle();
        bundle.putDouble("lat",lat);
        bundle.putDouble("lng",lng);
        myLocationFragment.setArguments(bundle);
        myLocationFragment.show(getFragmentManager(),"myLocation");
    }

    public static Context getContext() {
        return sContext;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLocationManager.requestLocationUpdates(mProvider,
                MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_FOR_UPDATES,
                mLocationListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationManager.removeUpdates(mLocationListener);
    }

    public void showCurrentLocation(Location location) {
        if (location != null) {
            lat=location.getLatitude();
            lng=location.getLongitude();
        }
    }
}
