package com.example.desmond.clinc.View;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.desmond.clinc.Controller.AppointmentsController;
import com.example.desmond.clinc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MakeAppointment extends Fragment {


    RecyclerView makeRecyclerView;
    AppointmentsController controller;
    public MakeAppointment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_make_appointment, container, false);
        makeRecyclerView = view.findViewById(R.id.available_appointments);
        controller = new AppointmentsController(this);
        controller.showAllAvailableAppointments();


        return view;


    }

    public RecyclerView getMakeRecyclerView() {
        return makeRecyclerView;
    }

    public void setMakeRecyclerView(RecyclerView makeRecyclerView) {
        this.makeRecyclerView = makeRecyclerView;
    }



}
