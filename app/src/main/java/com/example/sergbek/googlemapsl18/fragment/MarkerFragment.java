package com.example.sergbek.googlemapsl18.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.sergbek.googlemapsl18.R;


public class MarkerFragment extends DialogFragment {

    private Button mBrowse;
    private Button mOk;
    private EditText mEditTittle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_marker,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        defineComponents();

    }

    private void defineComponents() {
        mBrowse     =(Button)getView().findViewById(R.id.browse_FM);
        mOk         =(Button)getView().findViewById(R.id.ok_FM);
        mEditTittle =(EditText)getView().findViewById(R.id.editTittle_FM);
    }
}
