package com.example.desmond.clinc.Model.Firebase;

import android.content.Context;
import android.net.Uri;

import com.example.desmond.clinc.Model.Appointment;
import com.example.desmond.clinc.Model.AppointmentDate;
import com.example.desmond.clinc.Model.Authentication;
import com.example.desmond.clinc.Model.CallBacks.AppointmentCallback;
import com.example.desmond.clinc.Model.CallBacks.AppointmentsCallback;
import com.example.desmond.clinc.Model.CallBacks.CheckCallback;
import com.example.desmond.clinc.Model.CallBacks.DiagnosisCallback;
import com.example.desmond.clinc.Model.CallBacks.DiagnosisListCallback;
import com.example.desmond.clinc.Model.CallBacks.ImageCallback;
import com.example.desmond.clinc.Model.CallBacks.MadeAppointmentCallback;
import com.example.desmond.clinc.Model.CallBacks.UserAppointmentCallback;
import com.example.desmond.clinc.Model.CallBacks.UserCallback;
import com.example.desmond.clinc.Model.Diagnosis;
import com.example.desmond.clinc.Model.Register;
import com.example.desmond.clinc.Model.User;

import java.io.IOException;
import java.util.List;

/**
 * Created by Desmond on 12/22/2017.
 */

public class DBFacade {


    private static DBFacade INSTANCE;

    private AuthenticationDB authDB;
    private UserDB userDB;
    private AppointmentDB appointmentDB;
    private DiagnosisDB diagnosisDB;

    private DBFacade(Context context) {

        authDB = new AuthenticationDB();
        userDB = new UserDB();
        appointmentDB = new AppointmentDB(context);
        diagnosisDB = new DiagnosisDB(context);
    }


    public static DBFacade getINSTANCE(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DBFacade(context);
        }
        return INSTANCE;
    }

    //Authentication DB...
    public void register(Register register, CheckCallback callback) {
        authDB.register(register, callback);
    }

    public void signIn(Context context, Authentication auth, CheckCallback callback) {
        authDB.signIn(context, auth, callback);
    }

    public void signOut(Context context) {

        authDB.signOut(context);

    }

    public boolean checkUserSession(Context context) {

       return authDB.checkUserSession(context);
    }

    // User DB.....

    public void getDoctors(List<String> doctorsId, UserAppointmentCallback callback) {
        userDB.getDoctors(doctorsId, callback);
    }

    public void getPatients(List<String> patientsId, UserAppointmentCallback callback) {
        userDB.getDoctors(patientsId, callback);
    }

    public void getPatients(UserCallback callback) {

        userDB.getPatients(callback);
    }

    public void editPatientData(User user, CheckCallback callback) {
        userDB.editPatientData(user, callback);
    }

    public void editDoctorData(User user, CheckCallback callback) {
        userDB.editDoctorData(user, callback);
    }

    public void setUserImage(Uri file, ImageCallback callback) {
        userDB.setUserImage(file, callback);
    }

    public void getUserImage(ImageCallback callback) throws IOException {
        userDB.getUserImage(callback);
    }

    public void saveUserData(Context context) {

        userDB.saveUserData(context);
    }

    // Appointment DB...

    public void insertAppointment(AppointmentDate date, CheckCallback callback) {
        appointmentDB.insertAppointment(date, callback);
    }

    public void makeAppointment(Appointment appointment, CheckCallback callback) {
        appointmentDB.makeAppointment(appointment, callback);
    }

    public void deleteAppointment(Appointment appointment) {
        appointmentDB.deleteAppointment(appointment);
    }

    public void getAllDoctorAppointments(MadeAppointmentCallback callback) {
        appointmentDB.getAllDoctorAppointments(callback);
    }

    public void getAllPatientAppointments(MadeAppointmentCallback callback) {
        appointmentDB.getAllPatientAppointments(callback);
    }

    public void getAllAvailableAppointments(AppointmentsCallback callback) {
        appointmentDB.getAllAvailableAppointments(callback);
    }

    public void getUpComingAppointment(AppointmentCallback callback) {
        appointmentDB.getUpComingAppointment(callback);
    }

    // Diagnosis DB...

    public void setDiagnosis(Diagnosis diagnosis) {

        diagnosisDB.setDiagnosis(diagnosis);
    }

    public void deleteDiagnosis(Diagnosis diagnosis) {

        diagnosisDB.deleteDiagnosis(diagnosis);
    }

    public void getAllPatientDiagnosis(DiagnosisListCallback callback) {
        diagnosisDB.getAllPatientDiagnosis(callback);
    }

    public void getAllDoctorDiagnosis(DiagnosisListCallback callback) {
        diagnosisDB.getAllPatientDiagnosis(callback);
    }

    public void getLastDiagnosis(DiagnosisCallback callback) {

        diagnosisDB.getLastDiagnosis(callback);
    }


}
