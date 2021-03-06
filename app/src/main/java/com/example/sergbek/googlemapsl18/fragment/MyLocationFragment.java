package com.example.sergbek.googlemapsl18.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sergbek.googlemapsl18.R;
import com.example.sergbek.googlemapsl18.asynctask.LoaderLocationAsyncTask;




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
