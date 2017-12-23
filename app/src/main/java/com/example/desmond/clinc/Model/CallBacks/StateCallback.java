package com.example.desmond.clinc.Model.CallBacks;

import com.example.desmond.clinc.Model.Appointment;

/**
 * Created by Desmond on 12/12/2017.
 */

public interface StateCallback {


    void pending(Appointment appointment);

    void complete(Appointment appointment);

    void non(Appointment appointment);

    void finish(Appointment appointment);
}
