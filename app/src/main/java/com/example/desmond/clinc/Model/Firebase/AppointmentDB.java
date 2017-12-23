package com.example.desmond.clinc.Model.Firebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.example.desmond.clinc.AppointmentsState.CompleteState;
import com.example.desmond.clinc.AppointmentsState.FinishState;
import com.example.desmond.clinc.AppointmentsState.NonState;
import com.example.desmond.clinc.AppointmentsState.PendingState;
import com.example.desmond.clinc.Controller.Actions.AppointmentParent;
import com.example.desmond.clinc.Model.Appointment;
import com.example.desmond.clinc.Model.AppointmentDate;
import com.example.desmond.clinc.Model.CallBacks.AppointmentCallback;
import com.example.desmond.clinc.Model.CallBacks.AppointmentsCallback;
import com.example.desmond.clinc.Model.CallBacks.CheckCallback;
import com.example.desmond.clinc.Model.CallBacks.MadeAppointmentCallback;
import com.example.desmond.clinc.Model.CallBacks.StateCallback;
import com.example.desmond.clinc.Model.CallBacks.UserAppointmentCallback;
import com.example.desmond.clinc.Model.ModelFacade;
import com.example.desmond.clinc.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Desmond on 12/11/2017.
 */

public class AppointmentDB {


    FirebaseDatabase database;
    DatabaseReference ref;
    UserDB db;
    public final static String USER_APPOINTMENT = "user_appointment";
    Context context;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;


    public AppointmentDB(Context context) {
        database = FirebaseConnection.getINSTANCE().getDB();
        ref = database.getReference("appointmentsManagement");
        db = new UserDB();
        this.context = context;
        sharedPreferences = context.getSharedPreferences(USER_APPOINTMENT, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }


    public void insertAppointment(AppointmentDate appointmentDate, final CheckCallback checkCallback) {
        // Doctor Create Appointment.....

        SharedPreferences preferences = context.getSharedPreferences(UserDB.USER_DATA, Context.MODE_PRIVATE);
        String name = preferences.getString("firstName", "") + " " + preferences.getString("lastName", "");
        String appointmentId = appointmentDate.getAppointmentDate() + "@" + appointmentDate.getAppointmentTime();
        ref.child("appointments").child(AuthenticationDB.USER_ID + "@" + name).child(appointmentId).setValue(appointmentDate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    checkCallback.success();

                } else {
                    checkCallback.fail();
                }
            }
        });
    }

    public void makeAppointment(Appointment appointment, final CheckCallback checkCallback) {
        // take the doctorId from the list then make a connection between them.....
        // the patient cant make appointments with the same doctor at the same time...


        String makeId = appointment.getAppointmentDate().getAppointmentDate() +
                "@" + appointment.getAppointmentDate().getAppointmentTime();

        String doctorId = appointment.getUser().getUserId();
        String relation = doctorId + "+" + AuthenticationDB.USER_ID;

        ref.child("madeAppointments").child(makeId).setValue(relation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    checkCallback.success();
                } else {
                    checkCallback.fail();
                }
            }
        });


    }

    public void getAllMadeAppointment() {

        ref.child("madeAppointments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> appointments = new HashSet<>();

                for (DataSnapshot date : dataSnapshot.getChildren()) {
                    String app = date.getKey().split("@")[0] + " " + date.getKey().split("@")[1];
                    System.out.println(app);

                    appointments.add(app);
                }
                storeMadeAppointment(appointments);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void deleteAppointment(Appointment appointment) {
        // deleteAppointment base on patientId AND doctorId.....
        String id = appointment.getAppointmentDate().getAppointmentDate() + "@" + appointment.getAppointmentDate().getAppointmentTime();
        ref.child("madeAppointments").child(id).removeValue();
    }

    public void getAllDoctorAppointments(final MadeAppointmentCallback callback) {



        ref.child("madeAppointments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final ArrayList<Appointment> appointments = new ArrayList<>();
                ArrayList<String> patientsId = new ArrayList<>();
                Appointment appointment;
                for (DataSnapshot doctorApp : dataSnapshot.getChildren()) {


                    String userId = doctorApp.getValue(String.class);
                    String patientId = userId.split("\\+")[1];
                    String doctorId = userId.split("\\+")[0];
                    String doctorDate = doctorApp.getKey();


                    if (doctorId.equalsIgnoreCase(AuthenticationDB.USER_ID)) {

                        appointment = new Appointment();
                        AppointmentDate date = new AppointmentDate();
                        date.setAppointmentDate(doctorDate.split("@")[0]);
                        date.setAppointmentTime(doctorDate.split("@")[1]);
                        appointment.setAppointmentDate(date);
                        checkMadeAppointmentsState(appointment);
                        appointments.add(appointment);
                        patientsId.add(patientId);
                    }

                }


                db.getPatients(patientsId, new UserAppointmentCallback() {
                    @Override
                    public void getDoctors(ArrayList<User> users) {


                    }

                    @Override
                    public void getPatient(Set<User> users) {
                        int index = 0;
                        for (User usr : users) {
                            appointments.get(index).setUser(usr);
                            index++;
                        }
                        callback.success(appointments);
                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void getAllPatientAppointments(final MadeAppointmentCallback callback) {

        ref.child("madeAppointments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final ArrayList<Appointment> appointments = new ArrayList<>();
                ArrayList<String> doctorsId = new ArrayList<>();
                Appointment appointment;
                for (DataSnapshot patientApp : dataSnapshot.getChildren()) {

                    String userId = patientApp.getValue(String.class);
                    String patientId = userId.split("\\+")[1];
                    String doctorId = userId.split("\\+")[0];
                    String patientDate = patientApp.getKey();


                    if (patientId.equalsIgnoreCase(AuthenticationDB.USER_ID)) {

                        appointment = new Appointment();
                        AppointmentDate date = new AppointmentDate();
                        date.setAppointmentDate(patientDate.split("@")[0]);
                        date.setAppointmentTime(patientDate.split("@")[1]);
                        appointment.setAppointmentDate(date);
                        checkMadeAppointmentsState(appointment);
                        appointments.add(appointment);
                        doctorsId.add(doctorId);
                    }

                }


                db.getDoctors(doctorsId, new UserAppointmentCallback() {
                    @Override
                    public void getDoctors(ArrayList<User> users) {
                        int index = 0;
                        for (User usr : users) {
                            appointments.get(index).setUser(usr);
                            index++;
                        }
                        callback.success(appointments);

                    }

                    @Override
                    public void getPatient(Set<User> users) {

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getAllAvailableAppointments(final AppointmentsCallback appointmentsCallback) {
        // get the appointment that is have a state NonState......
        getAllMadeAppointment();

        ref.child("appointments").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashSet<String> madeAppointments = getStoredMadeAppointments();
                ArrayList<AppointmentParent> appointmentsList = new ArrayList<>();


                for (DataSnapshot doctors : dataSnapshot.getChildren()) {

                    final ArrayList<Appointment> appointments = new ArrayList<>();
                    for (DataSnapshot app : doctors.getChildren()) {

                        final Appointment appointment = ModelFacade.createAppointment();
                        appointment.setAppointmentDate(app.getValue(AppointmentDate.class));
                        try {
                            appointment.setUser(ModelFacade.createUser("Doctor"));
                        } catch (IllegalAccessException | ClassNotFoundException | InstantiationException e) {
                            e.printStackTrace();
                        }

                        appointment.getUser().setFirstName(doctors.getKey().split("@")[1].split(" ")[0]);
                        appointment.getUser().setFirstName(doctors.getKey().split("@")[1].split(" ")[1]);
                        appointment.getUser().setUserId(doctors.getKey().split("@")[0]);

                        checkAppointmentsState(madeAppointments, appointment);


                        System.out.println(appointment);
                        if (appointment.getState() instanceof NonState) {
                            appointments.add(appointment);
                        }

                    }


                    if (appointments.size() > 0) {
                        AppointmentParent parent = new AppointmentParent(doctors.getKey().split("@")[1], appointments);
                        appointmentsList.add(parent);
                    }

                }

                appointmentsCallback.success(appointmentsList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                appointmentsCallback.fail();
            }
        });


    }

    public void getUpComingAppointment(final AppointmentCallback callback) {
        // get the last appointment for the user

        if (AuthenticationDB.USER_TYPE.equals("doctor")) {
            getAllDoctorAppointments(new MadeAppointmentCallback() {
                @Override
                public void success(ArrayList<Appointment> appointments) {

                    callback.success(checkUpComingAppointment(appointments));

                }

                @Override
                public void fail() {

                }
            });
        } else if (AuthenticationDB.USER_TYPE.equals("patient")) {
            getAllPatientAppointments(new MadeAppointmentCallback() {
                @Override
                public void success(ArrayList<Appointment> appointments) {

                    callback.success(checkUpComingAppointment(appointments));

                }

                @Override
                public void fail() {

                }
            });
        }


    }


    /// Utilises Methods....
    private void checkMadeAppointmentsState(Appointment appointment) {

        String appDate = appointment.getAppointmentDate().getAppointmentDate()
                + " " + appointment.getAppointmentDate().getAppointmentTime();

        StateCallback stateCallback = setState();
        if (isComplete(appDate)) {
            stateCallback.complete(appointment);
        } else {
            stateCallback.pending(appointment);
        }

    }

    private void checkAppointmentsState(Set<String> madeAppointment, Appointment appointment) {

        StateCallback stateCallback = setState();
        String appDate = appointment.getAppointmentDate().getAppointmentDate() + " " + appointment.getAppointmentDate().getAppointmentTime();
        if (isMade(madeAppointment, appDate)) {
            if (isComplete(appDate)) {
                stateCallback.complete(appointment);
            } else {
                stateCallback.pending(appointment);
            }
        } else {
            if (isFinished(appDate)) {
                stateCallback.finish(appointment);
            } else {
                stateCallback.non(appointment);
            }
        }


    }

    private boolean isComplete(String appointment) {

        DateFormat dateFormat = new SimpleDateFormat("E, dd-MM-yyyy hh:mm a");
        Date currentDate = new Date();
        Date userDate = new Date();

        String dateValue2 = dateFormat.format(currentDate);


        try {
            userDate = dateFormat.parse(appointment);
            currentDate = dateFormat.parse(dateValue2);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return userDate.before(currentDate);

    }

    private boolean isFinished(String appointment) {

        DateFormat dateFormat = new SimpleDateFormat("E, dd-MM-yyyy hh:mm a");
        Date currentDate = new Date();
        Date userDate = new Date();

        String dateValue2 = dateFormat.format(currentDate);


        try {
            userDate = dateFormat.parse(appointment);
            currentDate = dateFormat.parse(dateValue2);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return userDate.before(currentDate);

    }

    private boolean isMade(Set<String> madeAppointment, String appointment) {

        for (String made : madeAppointment) {
            if (made.equals(appointment)) {
                return true;
            }
        }


        return false;
    }

    private StateCallback setState() {

        return new StateCallback() {
            @Override
            public void pending(Appointment appointment) {
                appointment.setState(new PendingState());
            }

            @Override
            public void complete(Appointment appointment) {
                appointment.setState(new CompleteState());
            }

            @Override
            public void non(Appointment appointment) {
                appointment.setState(new NonState());
            }

            @Override
            public void finish(Appointment appointment) {
                appointment.setState(new FinishState());
            }
        };


    }

    private void storeMadeAppointment(Set<String> appointments) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(UserDB.USER_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        editor.putStringSet("MadeAppointments", appointments);
        editor.commit();
    }

    private Appointment checkUpComingAppointment(ArrayList<Appointment> appointments) {

        ArrayList<Appointment> pendingAppointment = new ArrayList<>();

        for (Appointment appointment : appointments) {
            if (appointment.getState() instanceof PendingState) {
                pendingAppointment.add(appointment);
            }
        }

        if (pendingAppointment.size() == 1) {
            return pendingAppointment.get(0);
        }

        Appointment upComing=null;
        for (int i = 0; i < pendingAppointment.size() - 1; i++) {
            for (int j = i+1; j < pendingAppointment.size(); j++) {
                upComing =  appointmentCompare(pendingAppointment.get(i), pendingAppointment.get(j));
            }
        }

        return upComing;
    }

    private Appointment appointmentCompare(Appointment appointment1, Appointment appointment2) {

        DateFormat dateFormat = new SimpleDateFormat("E, dd-MM-yyyy hh:mm a");
        Date date1 = new Date();
        Date date2 = new Date();


        String appDate1=appointment1.getAppointmentDate().getAppointmentDate()+" "+appointment1.getAppointmentDate().getAppointmentTime();
        String appDate2=appointment2.getAppointmentDate().getAppointmentDate()+" "+appointment2.getAppointmentDate().getAppointmentTime();

        try {
            date1 = dateFormat.parse(appDate1);
            date2 = dateFormat.parse(appDate2);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date1.before(date2)) {
            return appointment1;
        } else {
            return appointment2;
        }
    }

    private HashSet<String> getStoredMadeAppointments() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(UserDB.USER_DATA, Context.MODE_PRIVATE);
        return (HashSet<String>) sharedPreferences.getStringSet("MadeAppointments", null);
    }


}