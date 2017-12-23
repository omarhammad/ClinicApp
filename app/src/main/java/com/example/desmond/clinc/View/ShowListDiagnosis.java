package com.example.desmond.clinc.View;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.desmond.clinc.Controller.DiagnosisController;
import com.example.desmond.clinc.R;

public class ShowListDiagnosis extends Fragment {

    public ShowListDiagnosis() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    FloatingActionButton createBtn;
    DiagnosisController controller;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_show_list_diagnosis, container, false);

        recyclerView = view.findViewById(R.id.diagnosis_list);
        createBtn = view.findViewById(R.id.add_diagnosis);
        controller = new DiagnosisController(this);
        controller.showDiagnosisList();
        return view;
    }


    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public FloatingActionButton getcreateBtn() {
        return createBtn;
    }

    public void addCreateBtnListener(View.OnClickListener listener) {
        getcreateBtn().setOnClickListener(listener);
    }
}
