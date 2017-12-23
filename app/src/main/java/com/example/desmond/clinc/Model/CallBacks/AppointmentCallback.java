package com.example.desmond.clinc.Model.CallBacks;

import com.example.desmond.clinc.Model.Appointment;

/**
 * Created by Desmond on 12/22/2017.
 */

public interface AppointmentCallback {

    void success(Appointment appointments);

    void fail();

}
