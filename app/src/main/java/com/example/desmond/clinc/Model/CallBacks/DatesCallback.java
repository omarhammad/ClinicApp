package com.example.desmond.clinc.Model.CallBacks;

import com.example.desmond.clinc.Model.AppointmentDate;

import java.util.List;

/**
 * Created by Desmond on 12/12/2017.
 */

public interface DatesCallback {


    void success(List<AppointmentDate> dates);

    void fail();


}
