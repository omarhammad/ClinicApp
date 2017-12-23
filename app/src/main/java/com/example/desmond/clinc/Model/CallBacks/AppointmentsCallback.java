package com.example.desmond.clinc.Model.CallBacks;

import com.example.desmond.clinc.Controller.Actions.AppointmentParent;

import java.util.ArrayList;

/**
 * Created by Desmond on 12/12/2017.
 */

public interface AppointmentsCallback {

    void success(ArrayList<AppointmentParent> appointments);

    void fail();



}
