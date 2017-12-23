package com.example.desmond.clinc.View;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.desmond.clinc.Controller.AppointmentsController;
import com.example.desmond.clinc.R;

public class ShowAppointments  extends Fragment {

    FloatingActionButton floatingActionButton;
    RecyclerView appointmentsList;
    AppointmentsController controller;



    public ShowAppointments() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_show_appointments, container, false);


        floatingActionButton = view.findViewById(R.id.add_appointments);
        appointmentsList = view.findViewById(R.id.appointments_list);
        controller = new AppointmentsController(this);
        controller.showAppointments();

        return view;
    }


    public FloatingActionButton getFloatingActionButton() {
        return floatingActionButton;
    }

    public void setFloatingActionButton(FloatingActionButton floatingActionButton) {
        this.floatingActionButton = floatingActionButton;
    }

    public RecyclerView getAppointmentsList() {
        return appointmentsList;
    }

    public void setAppointmentsList(RecyclerView appointmentsList) {
        this.appointmentsList = appointmentsList;
    }

    public void addAddBtnListener(FloatingActionButton.OnClickListener listener) {
        floatingActionButton.setOnClickListener(listener);
    }
}
