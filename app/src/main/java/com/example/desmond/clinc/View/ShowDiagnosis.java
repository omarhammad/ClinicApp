package com.example.desmond.clinc.View;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.desmond.clinc.Controller.DiagnosisController;
import com.example.desmond.clinc.Model.Diagnosis;
import com.example.desmond.clinc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowDiagnosis extends Fragment {


    TextView diseaseName;
    TextView userName;
    TextView diagnosisBlank;
    TextView diagnosisPrice;
    RecyclerView medicinesList;
    DiagnosisController controller;

    public ShowDiagnosis() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_diagnosis, container, false);
        Diagnosis diagnosis = (Diagnosis) getActivity().getIntent().getSerializableExtra("ShowDiagnosis");

        diseaseName = view.findViewById(R.id.disease_name);
        userName = view.findViewById(R.id.user_name);
        diagnosisPrice = view.findViewById(R.id.diagnosis_price);
        diagnosisBlank = view.findViewById(R.id.diagnosis_blank);
        medicinesList = view.findViewById(R.id.medicenes_list);
        controller = new DiagnosisController(this, diagnosis);
        controller.showDiagnosisData();

        return view;

    }

    public TextView getDiseaseName() {
        return diseaseName;
    }

    public TextView getUserName() {
        return userName;
    }

    public TextView getDiagnosisBlank() {
        return diagnosisBlank;
    }

    public TextView getDiagnosisPrice() {
        return diagnosisPrice;
    }

    public RecyclerView getMedicinesList() {
        return medicinesList;
    }
}
