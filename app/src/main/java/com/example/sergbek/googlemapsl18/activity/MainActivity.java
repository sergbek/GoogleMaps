package com.example.sergbek.googlemapsl18.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.sergbek.googlemapsl18.R;
import com.example.sergbek.googlemapsl18.fragment.MyLocationFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
    private GoogleMap mMap;
    private Button mBtnMyLocation;
    private static Context sContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sContext=getBaseContext();

        mBtnMyLocation= (Button) findViewById(R.id.btn_myLocation_AM);
        mBtnMyLocation.setOnClickListener(this);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_AM);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        mMap.setTrafficEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onClick(View v) {
        MyLocationFragment myLocationFragment=new MyLocationFragment();
        myLocationFragment.show(getFragmentManager(),"myLocation");
    }

    public static Context getContext() {
        return sContext;
    }
}
