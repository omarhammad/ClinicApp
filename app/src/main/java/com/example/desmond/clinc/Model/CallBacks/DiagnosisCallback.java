package com.example.desmond.clinc.Model.CallBacks;

import com.example.desmond.clinc.Model.Diagnosis;

/**
 * Created by Desmond on 12/18/2017.
 */

public interface DiagnosisCallback {


    void sucess(String userName);

    void sucess(Diagnosis diagnosis);

    void fail();
}
