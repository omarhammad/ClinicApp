package com.example.desmond.clinc.Controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.example.desmond.clinc.Controller.Actions.FragmentUtils;
import com.example.desmond.clinc.Model.Address;
import com.example.desmond.clinc.Model.CallBacks.CheckCallback;
import com.example.desmond.clinc.Model.CallBacks.ImageCallback;
import com.example.desmond.clinc.Model.Firebase.AppointmentDB;
import com.example.desmond.clinc.Model.Firebase.AuthenticationDB;
import com.example.desmond.clinc.Model.Firebase.DBFacade;
import com.example.desmond.clinc.Model.Firebase.UserDB;
import com.example.desmond.clinc.Model.ModelFacade;
import com.example.desmond.clinc.Model.User;
import com.example.desmond.clinc.R;
import com.example.desmond.clinc.View.EditProfileFragment;
import com.example.desmond.clinc.View.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Desmond on 12/7/2017.
 */

public class ProfileController {


    private ProfileFragment profileView;
    private EditProfileFragment editProfileFragment;
    private DBFacade db;
    private Context context;
    SharedPreferences userData;
    AppointmentDB appointmentDB;

    public ProfileController(ProfileFragment profileView) {
        this.profileView = profileView;
        profileView.addProfileImageListener(getImageFromGallery());
        profileView.addEditProfileBtnListener(editProfileBtnListener());
        db = DBFacade.getINSTANCE(context);
        this.context = profileView.getContext();
        userData = context.getSharedPreferences(UserDB.USER_DATA, MODE_PRIVATE);

    }

    public ProfileController(EditProfileFragment editProfileFragment) {
        this.editProfileFragment = editProfileFragment;
        editProfileFragment.addCancelEditProfileBtnListener(cancelBtnListener());
        editProfileFragment.addSaveEditProfileBtnListener(saveEditedData());
        this.context = editProfileFragment.getContext();
        db = DBFacade.getINSTANCE(context);
        userData = context.getSharedPreferences(UserDB.USER_DATA, MODE_PRIVATE);
        appointmentDB = new AppointmentDB(context);


    }

    public void setProfileData() {


        String name = userData.getString("firstName", "") + " " + userData.getString("lastName", "");
        String address = userData.getString("country", "") + " " + userData.getString("city", "");
        String phoneNum = userData.getString("phoneNum", "");
        String email = userData.getString("email", "");



        showImageView();
        profileView.getName().setText(name);
        profileView.getAddress().setText(address);
        profileView.getPhoneNum().setText(phoneNum);
        profileView.getEmail().setText(email);
    }

    public void setEditProfileData() {

        editProfileFragment.getFname().setText(userData.getString("firstName",""));
        editProfileFragment.getLname().setText(userData.getString("lastName",""));
        editProfileFragment.getCountry().setText(userData.getString("country",""));
        editProfileFragment.getCity().setText(userData.getString("city",""));
        editProfileFragment.getPhoneNum().setText(userData.getString("phoneNum",""));
    }

    public void uploadImage(Uri path) {
        db.setUserImage(path, new ImageCallback() {
            @Override
            public void success(byte[] image) {
                profileView.getProfileImage().setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
            }

            @Override
            public void fail() {
                profileView.getProfileImage().setImageResource(R.drawable.avatar_profile);
            }
        });
    }

    public View.OnClickListener getImageFromGallery() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCaptureImageDialog();
            }
        };

    }

    public View.OnClickListener editProfileBtnListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditProfilePage();
            }
        };
    }

    public View.OnClickListener cancelBtnListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfilePage();
            }
        };
    }

    public View.OnClickListener saveEditedData() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    editProfileData();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void showImageView() {

        try {

            db.getUserImage(new ImageCallback() {
                @Override
                public void success(byte[] image) {
                    profileView.getProfileImage().setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
                }

                @Override
                public void fail() {
                    profileView.getProfileImage().setImageResource(R.drawable.avatar_profile);
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void showCaptureImageDialog() {


        PickImageDialog.build(new PickSetup())
                .setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult r) {
                        uploadImage(r.getUri());
                    }
                }).show(profileView.getFragmentManager());

    }

    private void openEditProfilePage() {
        FragmentUtils.replaceFragment(profileView.getActivity(), R.id.container, true, new EditProfileFragment());
    }

    private void editProfileData() throws IllegalAccessException, InstantiationException, ClassNotFoundException {


        String type = AuthenticationDB.USER_TYPE;
        if (type.equalsIgnoreCase("patient")) {


            User uesr = ModelFacade.createUser("Patient");

            if (checkProfileData(uesr)) {
                db.editPatientData(uesr, new CheckCallback() {
                    @Override
                    public void success() {
                        openProfilePage();
                    }

                    @Override
                    public void fail() {
                        Toast.makeText(context, "Editing Failed....", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(context, "Form Data Incomplete", Toast.LENGTH_SHORT).show();
            }


        } else {

            User user = ModelFacade.createUser("Doctor");

            if (checkProfileData(user)) {
                db.editDoctorData(user, new CheckCallback() {
                    @Override
                    public void success() {
                        openProfilePage();
                    }

                    @Override
                    public void fail() {
                        Toast.makeText(context, "Editing Failed....", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(context, "Form Data Incomplete", Toast.LENGTH_SHORT).show();
            }

        }

        db.saveUserData(context);

    }

    private void openProfilePage() {
        FragmentUtils.replaceFragment(editProfileFragment.getActivity(), R.id.container, true, new ProfileFragment());
    }

    private boolean checkProfileData(User user) {

        String fname = editProfileFragment.getFname().getText().toString();
        String lname = editProfileFragment.getLname().getText().toString();
        String country = editProfileFragment.getCountry().getText().toString();
        String city = editProfileFragment.getCity().getText().toString();
        String phoneNum = editProfileFragment.getPhoneNum().getText().toString();

        if (fname.trim().equals("") || lname.trim().equals("") || country.trim().equals("")
                || city.trim().equals("") || phoneNum.trim().equals("")) {

            return false;
        } else {
            user.setFirstName(fname);
            user.setLastName(lname);
            Address address = ModelFacade.createAddress();
            address.setCountry(country);
            address.setCity(city);
            user.setAddress(address);
            user.setPhoneNum(phoneNum);
            user.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());

            return true;

        }
    }

}

