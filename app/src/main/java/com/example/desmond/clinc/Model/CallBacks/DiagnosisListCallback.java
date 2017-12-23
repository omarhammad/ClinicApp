package com.example.desmond.clinc.Model.CallBacks;

import com.example.desmond.clinc.Model.Diagnosis;

import java.util.ArrayList;

/**
 * Created by Desmond on 12/21/2017.
 */

public interface DiagnosisListCallback {


    void sucess(ArrayList<Diagnosis> diagnoses);

    void fail();
}
