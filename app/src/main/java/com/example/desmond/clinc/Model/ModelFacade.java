package com.example.desmond.clinc.Model;

/**
 * Created by Desmond on 11/26/2017.
 */

public class ModelFacade {


    public static User createUser(String type) throws IllegalAccessException, ClassNotFoundException, InstantiationException {

        return UserFactory.createUser(type);
    }

    public static Diagnosis createDiagnosis() {
        return new Diagnosis();
    }

    public static Appointment createAppointment() {
        return new Appointment();
    }

    public static AppointmentDate createAppointmentDate() {
        return new AppointmentDate();
    }

    public static Address createAddress() {
        return new Address();
    }

    public static Authentication createAuthntication() {
        return new Authentication();
    }


}
