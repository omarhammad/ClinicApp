package com.example.desmond.clinc.Model.CallBacks;

import com.example.desmond.clinc.Model.User;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Desmond on 12/20/2017.
 */

public interface UserAppointmentCallback {
    void getDoctors(ArrayList<User> users);
    void getPatient(Set<User> users);


}
