package com.example.desmond.clinc.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Desmond on 11/26/2017.
 */

public class Diagnosis implements Serializable {


    private User patient;
    private User doctor;
    private String dateCreated;
    private String diseasesName;
    private String diagnosisBlank;
    private String diagnosisPrice;
    private ArrayList<String> medicines;

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    public User getDoctor() {
        return doctor;
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDiagnosisBlank() {
        return diagnosisBlank;
    }

    public void setDiagnosisBlank(String diagnosisBlank) {
        this.diagnosisBlank = diagnosisBlank;
    }

    public String getDiagnosisPrice() {
        return diagnosisPrice;
    }

    public void setDiagnosisPrice(String diagnosisPrice) {
        this.diagnosisPrice = diagnosisPrice;
    }

    public ArrayList<String> getMedicines() {
        return medicines;
    }

    public void setMedicines(ArrayList<String> medicines) {
        this.medicines = medicines;
    }

    public String getDiseasesName() {
        return diseasesName;
    }

    public void setDiseasesName(String diseasesName) {
        this.diseasesName = diseasesName;
    }
}
