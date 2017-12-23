package com.example.desmond.clinc.View;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.desmond.clinc.Controller.DiagnosisController;
import com.example.desmond.clinc.Model.Diagnosis;
import com.example.desmond.clinc.Model.ModelFacade;
import com.example.desmond.clinc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateDiagnosis extends Fragment {


    TextView username;
    TextView userEmail;
    EditText diagnosisPrice;
    EditText diseasesName;
    EditText diagnosisBlank;
    Button nextBtn;
    Button cancelBtn;
    DiagnosisController controller;

    public CreateDiagnosis() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_diagnosis, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        username = view.findViewById(R.id.user_name);
        userEmail = view.findViewById(R.id.user_email);
        diagnosisPrice = view.findViewById(R.id.diagnosis_price);
        diseasesName = view.findViewById(R.id.disease_name);
        diagnosisBlank = view.findViewById(R.id.diagnosis_blank);
        nextBtn = view.findViewById(R.id.next_btn);
        cancelBtn = view.findViewById(R.id.cancel_btn);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        Diagnosis diagnosis = ModelFacade.createDiagnosis();
        controller = new DiagnosisController(this, diagnosis);

        controller.showPatientsDialog();

        return view;
    }


    public TextView getUsername() {
        return username;
    }

    public TextView getUserEmail() {
        return userEmail;
    }

    public EditText getDiagnosisPrice() {
        return diagnosisPrice;
    }

    public EditText getDiseasesName() {
        return diseasesName;
    }

    public EditText getDiagnosisBlank() {
        return diagnosisBlank;
    }

    public Button getNextBtn() {
        return nextBtn;
    }

    public Button getCancelBtn() {
        return cancelBtn;
    }

    public void addNextBtnListener(View.OnClickListener listener) {
        getNextBtn().setOnClickListener(listener);

    }

    public void addCancelBtnListener(View.OnClickListener listener) {
        getCancelBtn().setOnClickListener(listener);

    }
}
