package com.example.desmond.clinc.View;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.desmond.clinc.Controller.ProfileController;
import com.example.desmond.clinc.Model.ModelFacade;
import com.example.desmond.clinc.Model.User;
import com.example.desmond.clinc.R;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    private TextView name;
    private TextView address;
    private TextView phoneNum;
    private TextView email;
    private ImageButton editBtn;
    private ImageView profileImage;
    private ProfileController controller;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        name = view.findViewById(R.id.profile_name);
        address = view.findViewById(R.id.profile_address);
        email = view.findViewById(R.id.profile_email);
        phoneNum = view.findViewById(R.id.profile_phonenum);
        profileImage = view.findViewById(R.id.profile_image);
        editBtn = view.findViewById(R.id.edit_profile);


        controller = new ProfileController(this);
        controller.setProfileData();


        return view;
    }

    public TextView getName() {
        return name;
    }

    public TextView getAddress() {
        return address;
    }

    public TextView getPhoneNum() {
        return phoneNum;
    }

    public TextView getEmail() {
        return email;
    }

    public ImageView getProfileImage() {
        return profileImage;
    }

    public void addEditProfileBtnListener(View.OnClickListener listener) {
        editBtn.setOnClickListener(listener);
    }

    public void addProfileImageListener(View.OnClickListener listener) {
        profileImage.setOnClickListener(listener);
    }





}
