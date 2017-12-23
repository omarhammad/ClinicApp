package com.example.desmond.clinc.Model;

import com.example.desmond.clinc.AppointmentsState.NonState;
import com.example.desmond.clinc.AppointmentsState.State;

/**
 * Created by Desmond on 11/26/2017.
 */

public  class Appointment {


    private AppointmentDate appointmentDate;
    private User user;
    private State state;

    public Appointment() {

        state = new NonState();
    }

    public AppointmentDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(AppointmentDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return user.getFirstName() +" : "+appointmentDate.getAppointmentDate()+" "+appointmentDate.getAppointmentTime() + " state :"+getState().toString();
    }
}
