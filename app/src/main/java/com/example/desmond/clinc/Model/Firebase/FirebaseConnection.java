package com.example.desmond.clinc.Model.Firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Desmond on 11/28/2017.
 */

public class FirebaseConnection {

    private static FirebaseConnection INSTANCE;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseStorage storage;


    private FirebaseConnection() {

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
    }


    public static FirebaseConnection getINSTANCE() {

        if (INSTANCE == null) {
            INSTANCE = new FirebaseConnection();
        }
        return INSTANCE;

    }

    public FirebaseAuth getAuth() {
        return mAuth;
    }

    public FirebaseDatabase getDB() {
        return database;
    }

    public FirebaseStorage getStorage() {
        return storage ;
    }



}
