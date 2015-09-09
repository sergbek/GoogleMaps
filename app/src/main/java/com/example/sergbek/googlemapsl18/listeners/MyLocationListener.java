package com.example.sergbek.googlemapsl18.listeners;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.example.sergbek.googlemapsl18.activity.MainActivity;


public class MyLocationListener implements LocationListener {
    private MainActivity mMainActivity;

    public MyLocationListener(MainActivity mainActivity) {
        this.mMainActivity = mainActivity;
    }

    @Override
    public void onLocationChanged(Location location) {
       mMainActivity.setMyLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}
