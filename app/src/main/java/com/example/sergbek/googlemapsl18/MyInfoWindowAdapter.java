package com.example.sergbek.googlemapsl18;


import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sergbek.googlemapsl18.activity.MainActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{

    private final View mRootView;

    private ImageView mImageView;
    private TextView mTvTittle;
    private TextView mTvSnippet;

    private DataBase mDataBase;

    public MyInfoWindowAdapter(MainActivity activity) {
        mRootView = activity.getLayoutInflater().inflate(
                R.layout.custom_info_window, null);
        mDataBase=new DataBase(activity);
    }


    @Override
    public View getInfoWindow(Marker marker) {

        return null;
    }


    @Override
    public View getInfoContents(final Marker marker) {
        defineComponents();

        mTvTittle.setText(marker.getTitle());
        mTvSnippet.setText(marker.getSnippet());
        MarkerEntity markerEntity=mDataBase.getMarkerPhoto(1);

        Log.d("www",markerEntity.toString());
        mImageView.setImageURI(Uri.parse(markerEntity.getPhoto()));

        return mRootView;
    }

    private void defineComponents() {
        mImageView          =   (ImageView)mRootView.findViewById(R.id.iv_CIF);
        mTvTittle           =   (TextView) mRootView.findViewById(R.id.tv_title_CIF);
        mTvSnippet          =   (TextView) mRootView.findViewById(R.id.tv_snippet_CIF);
    }


}
