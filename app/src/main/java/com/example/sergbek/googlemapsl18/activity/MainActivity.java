package com.example.sergbek.googlemapsl18.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sergbek.googlemapsl18.DataBase;
import com.example.sergbek.googlemapsl18.MarkerEntity;
import com.example.sergbek.googlemapsl18.MyInfoWindowAdapter;
import com.example.sergbek.googlemapsl18.MyLocationListener;
import com.example.sergbek.googlemapsl18.OnMarkerCompletionListeners;
import com.example.sergbek.googlemapsl18.R;
import com.example.sergbek.googlemapsl18.fragment.MarkerFragment;
import com.example.sergbek.googlemapsl18.fragment.MyLocationFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        View.OnClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener
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
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 10000;

    private  double lat;
    private  double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sContext=getBaseContext();
        defineComponents();

        mDataBase=new DataBase(this);
        mMarkersPhotoMap=new HashMap<>();

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        selectBestProvider();
        mLocation = mLocationManager.getLastKnownLocation(mProvider);

        initMap();
        mBtnMyLocation.setOnClickListener(this);
        mLocationListener = new MyLocationListener(this);

        setMyLocation(mLocation);
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
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(48.37, 31.16), 5));
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);

        List<MarkerEntity> markerList=mDataBase.getAllMarker();

        for (int i = 0; i < markerList.size(); i++) {
            double latitude     = Double.parseDouble(markerList.get(i).getLatitude());
            double longitude    = Double.parseDouble(markerList.get(i).getLongitude());
            LatLng latLng=new LatLng(latitude,longitude);
           Marker marker= mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(markerList.get(i).getTitle())
                            .snippet(createSnippet(latLng))
                            .icon(convertUriImageToBitmapDescriptor(Uri.parse(markerList.get(i).getPhoto()))));

            mMarkersPhotoMap.put(marker,Uri.parse(markerList.get(i).getPhoto()));
        }

        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter(this,mMarkersPhotoMap));

    }

    @Override
    public void onClick(View v) {
        MyLocationFragment myLocationFragment=new MyLocationFragment();
        Bundle bundle=new Bundle();
        bundle.putDouble("lat", lat);
        bundle.putDouble("lng", lng);
        myLocationFragment.setArguments(bundle);
        myLocationFragment.show(getFragmentManager(), "myLocation");
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


    public void setMyLocation(Location location) {
        if (location != null) {
            lat=location.getLatitude();
            lng=location.getLongitude();
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }


    @Override
    public void onMapLongClick(LatLng latLng) {
        this.mCurrentMarkerLatLng=latLng;
        MarkerFragment markerFragment=new MarkerFragment();
        markerFragment.show(getFragmentManager(),"markerFragment");
    }



    @Override
    public void onDone(Uri uriString, String tittle) {
        Marker marker=mMap.addMarker(new MarkerOptions()
                            .position(mCurrentMarkerLatLng)
                            .title(tittle)
                            .snippet(createSnippet(mCurrentMarkerLatLng))
                            .icon(convertUriImageToBitmapDescriptor(uriString)));

        mDataBase.addMarker(new MarkerEntity(String.valueOf(mCurrentMarkerLatLng.latitude),
                String.valueOf(mCurrentMarkerLatLng.longitude),
                tittle, String.valueOf(uriString)));

        mMarkersPhotoMap.put(marker,uriString);

    }

    private BitmapDescriptor convertUriImageToBitmapDescriptor(Uri imgUri) {
        BitmapDescriptor icon = null;
        if (imgUri != null) {
            try {
                Bitmap bitmap;
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imgUri);
                bitmap = ThumbnailUtils.extractThumbnail(bitmap, 100, 100);
                icon = BitmapDescriptorFactory.fromBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return icon;
    }

    private String createSnippet(LatLng latLng) {
        return "Location: " + String.format("%1$s || %2$s",
                String.valueOf(latLng.latitude).substring(0, 5),
                String.valueOf(latLng.longitude).substring(0, 5));
    }
}
