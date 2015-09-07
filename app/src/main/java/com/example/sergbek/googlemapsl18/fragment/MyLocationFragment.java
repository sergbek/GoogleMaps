package com.example.sergbek.googlemapsl18.fragment;

import android.app.DialogFragment;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sergbek.googlemapsl18.R;
import com.example.sergbek.googlemapsl18.activity.MainActivity;
import com.example.sergbek.googlemapsl18.asynctask.LoaderLocationAsyncTask;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MyLocationFragment extends DialogFragment implements View.OnClickListener {

    private View mRootView;

    private TextView mLat;
    private TextView mLng;
    private TextView mAddress;
    private Button mOk;
    private ProgressBar mProgressBar;

    private double lat;
    private double lng;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView =
                inflater.inflate(R.layout.fragment_my_location, container);

        defineComponents();
        lat=getArguments().getDouble("lat");
        lng=getArguments().getDouble("lng");

        Log.d("www", lat + "______" + lng);

        mLat.setText("Широта: " + String.valueOf(lat));
        mLng.setText("Долгота: " + String.valueOf(lng));


        LoaderLocationAsyncTask loaderLocationAsyncTask=new LoaderLocationAsyncTask(mAddress,mProgressBar);
        loaderLocationAsyncTask.execute(lat,lng);

        mOk.setOnClickListener(this);

        return mRootView;
    }

    private void defineComponents() {
        mLat = (TextView) mRootView.findViewById(R.id.latitude_FML);
        mLng = (TextView) mRootView.findViewById(R.id.longitude_FML);
        mAddress = (TextView) mRootView.findViewById(R.id.address_FML);
        mOk = (Button) mRootView.findViewById(R.id.btnOk_FML);
        mProgressBar = (ProgressBar) mRootView.findViewById(R.id.progressBar_FML);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getDialog().setTitle("My Location");
        setCancelable(false);
    }



    @Override
    public void onClick(View v) {
        dismiss();
    }
}
