package com.example.sergbek.googlemapsl18.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sergbek.googlemapsl18.listeners.OnMarkerCompletionListeners;
import com.example.sergbek.googlemapsl18.R;
import com.example.sergbek.googlemapsl18.activity.MainActivity;


public class MarkerFragment extends DialogFragment implements View.OnClickListener {

    private Button mBrowse;
    private Button mOk;
    private EditText mEditTittle;
    private ImageView mImageView;
    private OnMarkerCompletionListeners mCompletionListeners;

    private Uri mNewPhoto;

    private static final  int CODE_GET_FROM_GALLERY = 0;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mCompletionListeners = (OnMarkerCompletionListeners)activity;
    }

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
        removeTitleDialog();
        defineComponents();
        mImageView.setImageResource(R.drawable.ic_question);

        mBrowse.setOnClickListener(this);
        mOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==mBrowse)
            uploadPhotoClick();
        else if (v==mOk)
            updateMarker();
    }

    private void updateMarker() {
        if (!mEditTittle.getText().toString().isEmpty()){
            mCompletionListeners.onDone(mNewPhoto, mEditTittle.getText().toString());
            dismiss();
        }
        else
            Toast.makeText(MainActivity.getContext(),"Fill in note text",Toast.LENGTH_SHORT).show();
    }

    private void defineComponents() {
        mBrowse     =(Button)getView().findViewById(R.id.browse_FM);
        mOk         =(Button)getView().findViewById(R.id.ok_FM);
        mEditTittle =(EditText)getView().findViewById(R.id.editTittle_FM);
        mImageView  =(ImageView)getView().findViewById(R.id.iv_FM);
    }

    public void uploadPhotoClick() {
        startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI),
                                                                                        CODE_GET_FROM_GALLERY);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODE_GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            mNewPhoto = data.getData();
            if (mNewPhoto != null) {
                mImageView.setImageURI(mNewPhoto);
            }
        }
    }

    private void removeTitleDialog(){
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
}
