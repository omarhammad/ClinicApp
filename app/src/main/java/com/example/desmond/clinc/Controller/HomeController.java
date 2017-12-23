package com.example.desmond.clinc.Controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.desmond.clinc.Controller.Actions.FragmentUtils;
import com.example.desmond.clinc.Controller.Actions.SlidingImage;
import com.example.desmond.clinc.Model.Appointment;
import com.example.desmond.clinc.Model.CallBacks.AppointmentCallback;
import com.example.desmond.clinc.Model.CallBacks.DiagnosisCallback;
import com.example.desmond.clinc.Model.CallBacks.ImageCallback;
import com.example.desmond.clinc.Model.Diagnosis;
import com.example.desmond.clinc.Model.Firebase.AppointmentDB;
import com.example.desmond.clinc.Model.Firebase.AuthenticationDB;
import com.example.desmond.clinc.Model.Firebase.DBFacade;
import com.example.desmond.clinc.Model.Firebase.DiagnosisDB;
import com.example.desmond.clinc.Model.Firebase.UserDB;
import com.example.desmond.clinc.R;
import com.example.desmond.clinc.View.AddMedicines;
import com.example.desmond.clinc.View.CreateDiagnosis;
import com.example.desmond.clinc.View.HomeFragment;
import com.example.desmond.clinc.View.HomeScreen;
import com.example.desmond.clinc.View.LoginScreen;
import com.example.desmond.clinc.View.ProfileFragment;
import com.example.desmond.clinc.View.ShowAppointments;
import com.example.desmond.clinc.View.ShowListDiagnosis;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Desmond on 12/7/2017.
 */

public class HomeController {


    HomeScreen homeView;
    HomeFragment homeFragment;
    DBFacade db;
    Context context;


    public HomeController(HomeScreen homeView) {

        this.homeView = homeView;
        db = DBFacade.getINSTANCE(context);
        this.context = homeView.getApplicationContext();
        this.homeView.addNavigationViewListener(setMenuEvents());


    }

    public HomeController(HomeFragment homeFragment) {
        this.homeFragment = homeFragment;
        this.context = homeFragment.getContext();
        db = DBFacade.getINSTANCE(context);
    }

    public void showImageSlider() {


        homeFragment.getHealthImages().setAdapter(new SlidingImage(context, Arrays.asList(R.drawable.health_0, R.drawable.health_1, R.drawable.health_2
                , R.drawable.health_3, R.drawable.health_4, R.drawable.health_5)));
    }

    public void showLastDiagnosis() {
        db.getLastDiagnosis(new DiagnosisCallback() {
            @Override
            public void sucess(String userName) {

            }

            @Override
            public void sucess(Diagnosis diagnosis) {
                setLastDiagnosisData(diagnosis);
            }

            @Override
            public void fail() {

            }
        });
    }

    public void showUpcomingAppointment() {
        db.getUpComingAppointment(new AppointmentCallback() {
            @Override
            public void success(Appointment appointment) {
                setUpComingAppointment(appointment);
            }

            @Override
            public void fail() {

            }
        });
    }


    private void setLastDiagnosisData(Diagnosis diagnosisData) {
        View view = homeFragment.getLastDiagnosis();

        if (diagnosisData == null) {
            return;
        } else {
            view.setVisibility(View.VISIBLE);
        }


        TextView date = view.findViewById(R.id.diagnosis_date);
        TextView time = view.findViewById(R.id.diagnosis_time);
        TextView username = view.findViewById(R.id.user_name);
        TextView disease = view.findViewById(R.id.disease_name);


        date.setText(diagnosisData.getDateCreated().split("@")[0]);
        time.setText(diagnosisData.getDateCreated().split("@")[1]);
        String name;
        if (AuthenticationDB.USER_TYPE.equals("patient")) {
            name = diagnosisData.getDoctor().getFirstName() + " " + diagnosisData.getDoctor().getLastName();
            username.setText(name);
        } else {
            name = diagnosisData.getPatient().getFirstName() + " " + diagnosisData.getPatient().getLastName();
            username.setText(name);
        }

        disease.setText(diagnosisData.getDiseasesName());
    }

    private void setUpComingAppointment(Appointment appointment) {
        View view = homeFragment.getUpComingAppointment();


        TextView time = view.findViewById(R.id.show_time);
        TextView date = view.findViewById(R.id.show_date);
        TextView username = view.findViewById(R.id.show_user_name);
        TextView state = view.findViewById(R.id.show_state);

        if (appointment == null) {
            return;
        } else {
            view.setVisibility(View.VISIBLE);
        }

        time.setText(appointment.getAppointmentDate().getAppointmentTime());
        date.setText(appointment.getAppointmentDate().getAppointmentDate());
        String name = appointment.getUser().getFirstName() + " " + appointment.getUser().getLastName();
        username.setText(name);
        state.setText("UpComing");

    }


    public NavigationView.OnNavigationItemSelectedListener setMenuEvents() {

        return new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        FragmentUtils.replaceFragment(homeView, R.id.container, true, new HomeFragment());
                        break;
                    case R.id.appointments_fragment:
                        FragmentUtils.replaceFragment(homeView, R.id.container, true, new ShowAppointments());
                        break;
                    case R.id.diagnosis_fragment:
                        FragmentUtils.replaceFragment(homeView, R.id.container, true, new ShowListDiagnosis());
                        break;
                    case R.id.profile_fragment:
                        FragmentUtils.replaceFragment(homeView, R.id.container, true, new ProfileFragment());
                        break;
                    case R.id.logout:
                        db.signOut(context);
                        getLoginPage();
                        break;


                }

                homeView.getDrawerLayout().closeDrawer(GravityCompat.START);

                return true;
            }

        };
    }

    public void startWithHomeFragment() {
        FragmentUtils.addFragment(homeView, R.id.container, true, new HomeFragment());
        homeView.getNavigationView().setCheckedItem(R.id.navigation_home);
    }

    public void setDrawerHeaderData() throws IOException {

        View view = homeView.getNavigationView().getHeaderView(0);
        final ImageView userPhoto = view.findViewById(R.id.nav_profile);
        TextView username = view.findViewById(R.id.nav_username);
        db.getUserImage(new ImageCallback() {
            @Override
            public void success(byte[] image) {
                userPhoto.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
            }

            @Override
            public void fail() {

            }
        });
        username.setText(AuthenticationDB.USER_NAME);
    }

    public void getLoginPage() {
        Intent intent = new Intent(context, LoginScreen.class);
        context.startActivity(intent);
        homeView.finish();

    }

    public void saveUserData() {
        db.saveUserData(context);
    }

}
