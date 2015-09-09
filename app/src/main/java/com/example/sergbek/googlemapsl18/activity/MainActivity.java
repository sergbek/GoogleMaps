package com.example.sergbek.googlemapsl18.activity;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sergbek.googlemapsl18.database.DataBase;
import com.example.sergbek.googlemapsl18.model.MarkerEntity;
import com.example.sergbek.googlemapsl18.view.MyInfoWindowAdapter;
import com.example.sergbek.googlemapsl18.listeners.MyLocationListener;
import com.example.sergbek.googlemapsl18.listeners.OnMarkerCompletionListeners;
import com.example.sergbek.googlemapsl18.R;
import com.example.sergbek.googlemapsl18.utils.Utils;
import com.example.sergbek.googlemapsl18.fragment.MarkerFragment;
import com.example.sergbek.googlemapsl18.fragment.MyLocationFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        View.OnClickListener, GoogleMap.OnMapLongClickListener
        ,OnMarkerCompletionListeners {
    
    private GoogleMap mMap;
    private Button mBtnMyLocation;
    private LocationManager mLocationManager;
    private String mProvider;
    private Location mLocation;
    private MyLocationListener mLocationListener;
    private DataBase mDataBase;
    private LatLng mCurrentMarkerLatLng;
    private Map<Marker,Uri> mMarkersPhotoMap;

    private static Context sContext;

    private static final long MINIMUM_DISTANCE_FOR_UPDATES = 10;
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000;

    private double mLat;
    private double mLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sContext=getBaseContext();
        defineComponents();
        initObjects();
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        selectBestProvider();
        mLocation = mLocationManager.getLastKnownLocation(mProvider);
        initMap();
        setListeners();
        setMyLocation(mLocation);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        mMap.setTrafficEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(48.37, 31.16), 5));
        mMap.setOnMapLongClickListener(this);
        addOnMapOldMarkers();
        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter(this, mMarkersPhotoMap));
    }

    @Override
    public void onClick(View v) {
        MyLocationFragment myLocationFragment=new MyLocationFragment();
        Bundle bundle=new Bundle();
        bundle.putDouble("lat", mLat);
        bundle.putDouble("lng", mLng);
        myLocationFragment.setArguments(bundle);
        myLocationFragment.show(getFragmentManager(), "myLocation");
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        this.mCurrentMarkerLatLng=latLng;
        MarkerFragment markerFragment=new MarkerFragment();
        markerFragment.show(getFragmentManager(), "markerFragment");
    }

    @Override
    public void onDone(Uri uriString, String tittle) {
        Marker marker=mMap.addMarker(new MarkerOptions()
                .position(mCurrentMarkerLatLng)
                .title(tittle)
                .snippet(createSnippet(mCurrentMarkerLatLng)));

        MarkerEntity markerEntity=new MarkerEntity();
        markerEntity.setLatitude(String.valueOf(mCurrentMarkerLatLng.latitude));
        markerEntity.setLongitude(String.valueOf(mCurrentMarkerLatLng.longitude));
        markerEntity.setTitle(tittle);

        if (uriString!=null){
            marker.setIcon(Utils.convertUriToBitmapDescriptor(uriString));
            markerEntity.setPhoto(String.valueOf(uriString));
            mMarkersPhotoMap.put(marker, uriString);
        } else {
            Uri uri=Uri.parse("android.resource://com.example.sergbek.googlemapsl18/" + R.drawable.ic_question);
            marker.setIcon(Utils.convertUriToBitmapDescriptor(uri));
            markerEntity.setPhoto(String.valueOf(uri));
            mMarkersPhotoMap.put(marker, uri);
        }
        mDataBase.addMarker(markerEntity);
    }

    public static Context getContext() {
        return sContext;
    }

    public void selectBestProvider() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        mProvider = mLocationManager.getBestProvider(criteria, true);
    }

    public void setMyLocation(Location location) {
        if (location != null) {
            mLat =location.getLatitude();
            mLng =location.getLongitude();
        }
    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_AM);
        mapFragment.getMapAsync(this);
    }

    private void defineComponents() {
        mBtnMyLocation= (Button) findViewById(R.id.btn_myLocation_AM);
    }

    private String createSnippet(LatLng latLng) {
        return "Location: " + String.format("%1$s || %2$s",
                String.valueOf(latLng.latitude).substring(0, 5),
                String.valueOf(latLng.longitude).substring(0, 5));
    }

    private void initObjects(){
        mDataBase=new DataBase(this);
        mMarkersPhotoMap=new HashMap<>();
        mLocationListener = new MyLocationListener(this);
    }

    private void setListeners(){
        mBtnMyLocation.setOnClickListener(this);
    }

    private void addOnMapOldMarkers() {
        List<MarkerEntity> markerList=mDataBase.getAllMarker();

        for (int i = 0; i < markerList.size(); i++) {
            double latitude     = Double.parseDouble(markerList.get(i).getLatitude());
            double longitude    = Double.parseDouble(markerList.get(i).getLongitude());
            String tittle=markerList.get(i).getTitle();
            LatLng latLng=new LatLng(latitude,longitude);
            Uri uriPhoto=Uri.parse(markerList.get(i).getPhoto());

            Marker marker= mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(tittle)
                    .snippet(createSnippet(latLng))
                    .icon(Utils.convertUriToBitmapDescriptor(uriPhoto)));

            mMarkersPhotoMap.put(marker,uriPhoto);
        }
    }
}
