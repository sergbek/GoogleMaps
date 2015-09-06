package com.example.sergbek.googlemapsl18.fragment;

import android.app.DialogFragment;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.example.sergbek.googlemapsl18.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MyLocationFragment extends DialogFragment {

    private View mRootView;
    private TextView mLat;
    private TextView mLng;
    private TextView mAddress;

    double lat = 48.6024;
    double lng = 22.2384;


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

        mLat.setText("Широта: " + String.valueOf(lat));
        mLng.setText("Долгота: " + String.valueOf(lng));

        Geocoder geocoder=new Geocoder(getActivity(), Locale.getDefault());

        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);

            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder(
                        "Адрес:\n");
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress
                            .append(returnedAddress.getAddressLine(i)).append(
                            "\n");
                }
                mAddress.setText(strReturnedAddress.toString());
            } else {
                mAddress.setText("Нет адресов!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            mAddress.setText("Не могу получить адрес!");
        }

        return mRootView;
    }

    private void defineComponents() {
        mLat = (TextView) mRootView.findViewById(R.id.latitude);
        mLng = (TextView) mRootView.findViewById(R.id.longitude);
        mAddress = (TextView) mRootView.findViewById(R.id.address);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        removeTitleDialog();
    }

    private void removeTitleDialog(){
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
}
