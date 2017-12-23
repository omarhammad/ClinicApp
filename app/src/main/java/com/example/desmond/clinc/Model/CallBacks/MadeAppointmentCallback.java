package com.example.desmond.clinc.Model.CallBacks;

import com.example.desmond.clinc.Model.Appointment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Desmond on 12/17/2017.
 */

public interface MadeAppointmentCallback {

    void success(ArrayList<Appointment> appointments);

    void fail();
}
