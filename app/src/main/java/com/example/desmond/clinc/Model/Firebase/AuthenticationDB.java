package com.example.desmond.clinc.Model.Firebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.example.desmond.clinc.Model.Authentication;
import com.example.desmond.clinc.Model.CallBacks.CheckCallback;
import com.example.desmond.clinc.Model.CallBacks.TypeCallback;
import com.example.desmond.clinc.Model.ModelFacade;
import com.example.desmond.clinc.Model.Register;
import com.example.desmond.clinc.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

import static android.content.Context.MODE_PRIVATE;
import static com.example.desmond.clinc.Model.Firebase.UserDB.USER_DATA;

/**
 * Created by Desmond on 11/28/2017.
 */

public class AuthenticationDB {


    private FirebaseAuth auth;
    private FirebaseDatabase db;
    public static String USER_ID = "NO-ID";
    public static String USER_TYPE  = "NO-TYPE";
    public static String USER_EMAIL = "NO-EMAIL";
    public static String USER_NAME= "NO-NAME";
    private FirebaseUser user;
    UserDB userDB;
    private DatabaseReference reference;

    public AuthenticationDB() {

        auth = FirebaseConnection.getINSTANCE().getAuth();
        db = FirebaseConnection.getINSTANCE().getDB();
        reference = db.getReference("users");
        userDB = new UserDB();
    }

    public void register(final Register register, final CheckCallback checkCallback) {

        auth.createUserWithEmailAndPassword(register.getEmail(), register.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            user = auth.getCurrentUser();

                            User userInfo = null;
                            try {
                                userInfo = ModelFacade.createUser("Patient");
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            }


                            userInfo.setFirstName(register.getFirstName());
                            userInfo.setLastName(register.getLastName());
                            userInfo.setAddress(register.getAddress());
                            userInfo.setEmail(register.getEmail());
                            userInfo.setPhoneNum(register.getPhoneNum());

                            reference.child("patient").child(user.getUid() + "@PATIENT").setValue(userInfo);
                            checkCallback.success();

                        } else {
                            checkCallback.fail();
                        }

                    }
                });

    }

    public void signIn(final Context context, Authentication authentication, final CheckCallback checkCallback) {


        auth.signInWithEmailAndPassword(authentication.getUsername(), authentication.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            setUserId();
                            userDB.saveUserData(context);
                            checkCallback.success();
                        } else {
                            checkCallback.fail();
                        }
                    }
                });


    }

    public void signOut(Context context) {
        auth.signOut();
        context.getSharedPreferences(USER_DATA, MODE_PRIVATE).edit().clear().commit();
    }

    private void setUserId() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        USER_ID = user.getUid();
    }

    public boolean checkUserSession(Context context) {

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            USER_ID = user.getUid();
            return true;
        } else {
            return false;
        }
    }


}
