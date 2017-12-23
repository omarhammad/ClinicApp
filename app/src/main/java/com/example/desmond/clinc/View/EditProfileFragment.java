package com.example.desmond.clinc.View;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.desmond.clinc.Controller.ProfileController;
import com.example.desmond.clinc.Model.ModelFacade;
import com.example.desmond.clinc.Model.User;
import com.example.desmond.clinc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment {

    ImageButton cancelBtn;
    ImageButton saveBtn;
    EditText fname;
    EditText lname;
    EditText country;
    EditText city;
    EditText phoneNum;
    private ProfileController controller;


    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        cancelBtn = view.findViewById(R.id.cancel_btn);
        saveBtn = view.findViewById(R.id.save_btn);
        fname = view.findViewById(R.id.edit_fname);
        lname = view.findViewById(R.id.edit_lname);
        country = view.findViewById(R.id.edit_country);
        city = view.findViewById(R.id.edit_city);
        phoneNum = view.findViewById(R.id.edit_phonenum);


        controller = new ProfileController(this);
        controller.setEditProfileData();



        return view;
    }


    public EditText getFname() {
        return fname;
    }

    public EditText getLname() {
        return lname;
    }


    public EditText getCountry() {
        return country;
    }

    public EditText getCity() {
        return city;
    }

    public EditText getPhoneNum() {
        return phoneNum;
    }

    public void addCancelEditProfileBtnListener(View.OnClickListener listener) {
        cancelBtn.setOnClickListener(listener);
    }

    public void addSaveEditProfileBtnListener(View.OnClickListener listener) {
        saveBtn.setOnClickListener(listener);
    }







}
