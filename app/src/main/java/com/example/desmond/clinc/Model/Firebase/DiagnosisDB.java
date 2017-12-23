package com.example.desmond.clinc.Model.Firebase;

import android.content.Context;

import com.example.desmond.clinc.AppointmentsState.PendingState;
import com.example.desmond.clinc.Model.Appointment;
import com.example.desmond.clinc.Model.Authentication;
import com.example.desmond.clinc.Model.CallBacks.DiagnosisCallback;
import com.example.desmond.clinc.Model.CallBacks.DiagnosisListCallback;
import com.example.desmond.clinc.Model.Diagnosis;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Desmond on 11/28/2017.
 */

public class DiagnosisDB {

    Context context;
    FirebaseDatabase database;
    DatabaseReference ref;

    public DiagnosisDB(Context context) {
        this.context = context;
        database = FirebaseConnection.getINSTANCE().getDB();
        ref = database.getReference("diagnosisManagement");

    }


    public void setDiagnosis(Diagnosis diagnosis) {

        String patient_id = diagnosis.getPatient().getEmail().split("@")[0] + "@" + diagnosis.getDiseasesName();
        String doctor_id = AuthenticationDB.USER_ID;
        ref.child("diagnosis").child(doctor_id).child(patient_id.trim()).setValue(diagnosis);

    }

    public void getAllPatientDiagnosis(final DiagnosisListCallback callback) {

        ref.child("diagnosis").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Diagnosis> diagnosis = new ArrayList<>();
                String userEmail = AuthenticationDB.USER_EMAIL.split("@")[0];

                for (DataSnapshot doctors : dataSnapshot.getChildren()) {
                    for (DataSnapshot patients : doctors.getChildren()) {
                        if (patients.getKey().contains(userEmail)) {
                            diagnosis.add(patients.getValue(Diagnosis.class));
                        }
                    }
                }

                callback.sucess(diagnosis);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.fail();
            }
        });


    }

    public void getAllDoctorDiagnosis(final DiagnosisListCallback callback) {

        ref.child("diagnosis").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Diagnosis> diagnoses = new ArrayList<>();

                for (DataSnapshot doctors : dataSnapshot.getChildren()) {

                    if (doctors.getKey().equals(AuthenticationDB.USER_ID)) {

                        for (DataSnapshot patients : doctors.getChildren()) {
                            diagnoses.add(patients.getValue(Diagnosis.class));
                        }
                        break;
                    }
                }
                callback.sucess(diagnoses);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.fail();
            }
        });


    }

    public void getLastDiagnosis(final DiagnosisCallback callback) {

        if (AuthenticationDB.USER_TYPE.equals("doctor")) {
            getAllDoctorDiagnosis(new DiagnosisListCallback() {
                @Override
                public void sucess(ArrayList<Diagnosis> diagnoses) {
                    callback.sucess(checkLastDiagnosis(diagnoses));
                }

                @Override
                public void fail() {
                    callback.fail();
                }
            });
        } else if (AuthenticationDB.USER_TYPE.equals("patient")){
            getAllPatientDiagnosis(new DiagnosisListCallback() {
                @Override
                public void sucess(ArrayList<Diagnosis> diagnoses) {
                    callback.sucess(checkLastDiagnosis(diagnoses));
                }

                @Override
                public void fail() {
                    callback.fail();
                }
            });
        }

    }

    public void deleteDiagnosis(Diagnosis diagnosis) {

        String patient_id = diagnosis.getPatient().getEmail().split("@")[0] + "@" + diagnosis.getDiseasesName();
        String doctor_id = AuthenticationDB.USER_ID;
        ref.child("diagnosis").child(doctor_id).child(patient_id).getRef().removeValue();

    }

    private Diagnosis checkLastDiagnosis(ArrayList<Diagnosis> diagnoses) {

        if (diagnoses.size() == 1) {
          return  diagnoses.get(0);
        }

        Diagnosis lastDiagnosis=null;
        for (int i = 0; i < diagnoses.size() - 1; i++) {
            for (int j = i+1; j < diagnoses.size(); j++) {
                lastDiagnosis =  DiagnosisCompare(diagnoses.get(i), diagnoses.get(j));
            }
        }

        return lastDiagnosis;
    }

    private Diagnosis DiagnosisCompare(Diagnosis diagnosis1, Diagnosis diagnosis2) {

        DateFormat dateFormat = new SimpleDateFormat("E, dd-MM-yyyy hh:mm a");
        Date date1 = new Date();
        Date date2 = new Date();


        String appDate1 = diagnosis1.getDateCreated().split("@")[0]+" "+diagnosis1.getDateCreated().split("@")[1];
        String appDate2 = diagnosis2.getDateCreated().split("@")[0]+" "+diagnosis2.getDateCreated().split("@")[1];

        try {
            date1 = dateFormat.parse(appDate1);
            date2 = dateFormat.parse(appDate2);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date1.before(date2)) {
            return diagnosis2;
        } else {
            return diagnosis1;
        }
    }



}
