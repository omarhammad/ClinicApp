package com.example.desmond.clinc.Model.Firebase;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.desmond.clinc.Model.CallBacks.CheckCallback;
import com.example.desmond.clinc.Model.CallBacks.UserAppointmentCallback;
import com.example.desmond.clinc.Model.CallBacks.ImageCallback;
import com.example.desmond.clinc.Model.CallBacks.TypeCallback;
import com.example.desmond.clinc.Model.CallBacks.UserCallback;
import com.example.desmond.clinc.Model.Doctor;
import com.example.desmond.clinc.Model.Patient;
import com.example.desmond.clinc.Model.User;
import com.example.desmond.clinc.View.HomeScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Desmond on 11/28/2017.
 */

public class UserDB {


    private FirebaseDatabase database;
    private DatabaseReference reference;
    private StorageReference storageReference;
    public final static String USER_DATA = "USER_DATA";
    SharedPreferences.Editor editor;

    private final long ONE_MEGABYTE = 1024 * 1024;


    public UserDB() {
        database = FirebaseConnection.getINSTANCE().getDB();
        reference = database.getReference("users");
        storageReference = FirebaseConnection.getINSTANCE().getStorage().getReference();
    }

    public void getDoctor(final UserCallback userCallback) {

        reference.child("doctor").child(AuthenticationDB.USER_ID + "@DOCTOR").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Doctor doctor = dataSnapshot.getValue(Doctor.class);

                userCallback.currentUser(doctor);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void getPatient(final UserCallback userCallback) {
        reference.child("patient").child(AuthenticationDB.USER_ID + "@PATIENT").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(Patient.class);
                userCallback.currentUser(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void getDoctors(final List<String> doctorIds, final UserAppointmentCallback callback) {


        reference.child("doctor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<User> users = new ArrayList<>();

                for (String doctorId : doctorIds) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        if (data.getKey().split("@")[0].equalsIgnoreCase(doctorId)) {
                            User user = data.getValue(User.class);
                            System.out.println("User Appointment : "+user.getFirstName() + " " + user.getLastName());
                            users.add(user);
                            break;
                        }

                    }

                }

                callback.getDoctors(users);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void getPatients(final List<String> patientsId, final UserAppointmentCallback callback) {

        reference.child("patient").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<User> users = new HashSet<>();

                for (String doctorId : patientsId) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        if (data.getKey().split("@")[0].equalsIgnoreCase(doctorId)) {
                            User user = data.getValue(User.class);
                            System.out.println("User Appointment : "+user.getFirstName() + " " + user.getLastName());
                            users.add(user);
                            break;
                        }

                    }

                }

                callback.getPatient(users);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void getPatients(final UserCallback callback) {
        reference.child("patient").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<User> users = new ArrayList<>();

                for (DataSnapshot patients : dataSnapshot.getChildren()) {
                    User user = patients.getValue(Patient.class);
                    users.add(user);
                }
                callback.getUseres(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void editPatientData(final User user, final CheckCallback checkCallback) {

        reference.child("patient").child(AuthenticationDB.USER_ID + "@PATIENT").setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {

            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    checkCallback.success();

                } else {
                    checkCallback.fail();
                }

            }
        });


    }

    public void editDoctorData(final User user, final CheckCallback checkCallback) {

        reference.child("doctor").child(AuthenticationDB.USER_ID + "@DOCTOR").setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {

            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    checkCallback.success();

                } else {
                    checkCallback.fail();
                }

            }
        });

    }

    public void setUserImage(Uri file, final ImageCallback imageCallback) {
        StorageReference userImageRef = storageReference.child("UserImages/" + AuthenticationDB.USER_ID);

        UploadTask uploadTask = userImageRef.putFile(file);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                imageCallback.fail();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                try {

                    getUserImage(new ImageCallback() {
                        @Override
                        public void success(byte[] image) {
                            imageCallback.success(image);
                        }

                        @Override
                        public void fail() {
                            imageCallback.fail();
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void getUserImage(final ImageCallback imageCallback) throws IOException {
        StorageReference userImageRef = storageReference.child("UserImages/" + AuthenticationDB.USER_ID);
        userImageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                imageCallback.success(bytes);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                imageCallback.fail();
            }
        });

    }

    public void saveUserData(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_DATA, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        getUserType(new TypeCallback() {
            @Override
            public void userType(String type) {

                if (type.equalsIgnoreCase("patient")) {
                    getPatient(new UserCallback() {
                        @Override
                        public void currentUser(User user) {
                            editor.putString("firstName", user.getFirstName());
                            editor.putString("lastName", user.getLastName());
                            editor.putString("email", user.getEmail());
                            editor.putString("phoneNum", user.getPhoneNum());
                            editor.putString("userId", AuthenticationDB.USER_ID);
                            editor.putString("country", user.getAddress().getCountry());
                            editor.putString("city", user.getAddress().getCity());
                            editor.putString("type", "patient");
                            editor.commit();
                        }

                        @Override
                        public void getUseres(ArrayList<User> users) {

                        }
                    });

                } else {

                    getDoctor(new UserCallback() {
                        @Override
                        public void currentUser(User user) {
                            Doctor doctor = ((Doctor) user);
                            editor.putString("firstName", doctor.getFirstName());
                            editor.putString("lastName", doctor.getLastName());
                            editor.putString("email", doctor.getEmail());
                            editor.putString("phoneNum", doctor.getPhoneNum());
                            editor.putString("userId", AuthenticationDB.USER_ID);
                            editor.putString("country", doctor.getAddress().getCountry());
                            editor.putString("city", doctor.getAddress().getCity());
                            editor.putString("specialist", (doctor.getSpecialist()));
                            editor.putString("type", "doctor");
                            editor.commit();
                        }

                        @Override
                        public void getUseres(ArrayList<User> users) {

                        }
                    });

                }


            }
        });

        AuthenticationDB.USER_TYPE = sharedPreferences.getString("type", "");
        AuthenticationDB.USER_EMAIL = sharedPreferences.getString("email", "");
        AuthenticationDB.USER_NAME = sharedPreferences.getString("firstName", "")+" "+sharedPreferences.getString("lastName", "");
        System.out.println("User type is : " + AuthenticationDB.USER_TYPE);
    }

    private void getUserType(final TypeCallback typeCallback) {




        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while (iterator.hasNext()) {

                    Iterator<DataSnapshot> iter2 = iterator.next().getChildren().iterator();
                    while (iter2.hasNext()) {
                        String user = iter2.next().getKey();

                        if (user.contains(AuthenticationDB.USER_ID)) {
                            typeCallback.userType(user.split("@")[1].toLowerCase());
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }




}


