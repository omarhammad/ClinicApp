package com.example.desmond.clinc.Controller.Actions;

import com.bignerdranch.expandablerecyclerview.model.Parent;
import com.example.desmond.clinc.Model.Appointment;

import java.util.List;

/**
 * Created by Desmond on 12/14/2017.
 */

public class AppointmentParent implements Parent<Appointment> {

    String doctorName;
    List<Appointment> appointments;


    public AppointmentParent(String doctorName, List<Appointment> appointments) {
        this.doctorName = doctorName;
        this.appointments = appointments;
    }

    @Override
    public List<Appointment> getChildList() {
        return appointments;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public String getDoctorName() {
        return doctorName;
    }

}
