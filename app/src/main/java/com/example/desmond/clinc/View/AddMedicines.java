package com.example.desmond.clinc.View;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.desmond.clinc.Controller.DiagnosisController;
import com.example.desmond.clinc.Model.Diagnosis;
import com.example.desmond.clinc.R;

import java.util.ArrayList;

public class AddMedicines extends Fragment {


    RecyclerView medicinesList;
    ImageView pushBtn;
    EditText medicineName;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_medicenes, container, false);

        pushBtn = view.findViewById(R.id.push_btn);
        medicineName = view.findViewById(R.id.medicine_name);
        medicinesList = view.findViewById(R.id.medicenes_recyclerview);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Diagnosis diagnosis = (Diagnosis) getActivity().getIntent().getSerializableExtra("Diagnosis");

        DiagnosisController controller = new DiagnosisController(this, diagnosis);
        controller.showMedicinesList(new ArrayList<String>(),getMedicinesList());

        return view;
    }

    public RecyclerView getMedicinesList() {
        return medicinesList;
    }

    public ImageView getPushBtn() {
        return pushBtn;
    }

    public EditText getMedicineName() {
        return medicineName;
    }

    public void addpushBtnListener(View.OnClickListener listener) {
        pushBtn.setOnClickListener(listener);
    }

    public void addEditTextWatcher(TextWatcher watcher) {
        medicineName.addTextChangedListener(watcher);
    }
}
