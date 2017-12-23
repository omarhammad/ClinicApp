package com.example.desmond.clinc.Model;

/**
 * Created by Desmond on 12/12/2017.
 */

public class AppointmentDate {

    private String appointmentDate;
    private String appointmentTime;

    public AppointmentDate() {
    }

    public AppointmentDate(String appointmentDate, String appointmentTime) {
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    @Override
    public String toString() {
        return getAppointmentDate() + "@" + getAppointmentTime();
    }
}
