package com.example.sergbek.googlemapsl18.view;


import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sergbek.googlemapsl18.R;
import com.example.sergbek.googlemapsl18.activity.MainActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.Map;

public class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{

    private final View mRootView;

    private ImageView mImageView;
    private TextView mTvTittle;
    private TextView mTvSnippet;

    private Map<Marker,Uri> mMarkersPhotoMap;

    public MyInfoWindowAdapter(MainActivity activity,Map<Marker,Uri> markersPhotoMap) {
        mRootView = activity.getLayoutInflater().inflate(
                R.layout.custom_info_window, null);
        this.mMarkersPhotoMap=markersPhotoMap;
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

        Uri photo=mMarkersPhotoMap.get(marker);
        mImageView.setImageURI(photo);

        return mRootView;
    }

    private void defineComponents() {
        mImageView          =   (ImageView)mRootView.findViewById(R.id.iv_CIF);
        mTvTittle           =   (TextView) mRootView.findViewById(R.id.tv_title_CIF);
        mTvSnippet          =   (TextView) mRootView.findViewById(R.id.tv_snippet_CIF);
    }


}
