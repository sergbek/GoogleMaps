package com.example.sergbek.googlemapsl18.asynctask;


import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sergbek.googlemapsl18.activity.MainActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LoaderLocationAsyncTask extends AsyncTask<Double,Void,String> {

    private TextView mAddress;
    private ProgressBar mProgressBar;

    public LoaderLocationAsyncTask(TextView address,ProgressBar progressBar) {
        this.mAddress = address;
        this.mProgressBar=progressBar;
    }


    @Override
    protected String doInBackground(Double... params) {
        Geocoder geocoder = new Geocoder(MainActivity.getContext(), Locale.getDefault());

        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(params[0], params[1], 1);

            if (!addresses.isEmpty()) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder(
                        "Address:\n \n");
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress
                            .append(returnedAddress.getAddressLine(i)).append(
                            "\n");
                }
                strReturnedAddress.append(returnedAddress.getCountryName()+"\n");

                return strReturnedAddress.toString();
            }
            else
                return new String("No address!");

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return new String("Error!");
    }

    @Override
    protected void onPostExecute(String s) {
        mAddress.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mAddress.setText(s);

    }
}
