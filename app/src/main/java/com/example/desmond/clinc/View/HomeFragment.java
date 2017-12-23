package com.example.desmond.clinc.View;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.desmond.clinc.Controller.HomeController;
import com.example.desmond.clinc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    View upComingAppointment;
    View lastDiagnosis;
    ViewPager healthImages;
    HomeController controller;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this




        View view = inflater.inflate(R.layout.fragment_home, container, false);

        upComingAppointment = view.findViewById(R.id.upcoming_appointment);
        upComingAppointment.setVisibility(View.GONE);

        lastDiagnosis = view.findViewById(R.id.last_diagnosis);
        lastDiagnosis.setVisibility(View.GONE);

        healthImages = view.findViewById(R.id.health_advices);

        controller = new HomeController(this);
        controller.showImageSlider();
        controller.showLastDiagnosis();
        controller.showUpcomingAppointment();

        return view;
    }


    public View getUpComingAppointment() {
        return upComingAppointment;
    }

    public View getLastDiagnosis() {
        return lastDiagnosis;
    }

    public ViewPager getHealthImages() {
        return healthImages;
    }

}
