package com.example.desmond.clinc.View;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.desmond.clinc.Controller.AppointmentsController;
import com.example.desmond.clinc.Model.AppointmentDate;
import com.example.desmond.clinc.Model.ModelFacade;
import com.example.desmond.clinc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateAppointment extends Fragment {


    Button pickTime;
    Button pickDate;
    Button done;
    TextView pickedDate;
    TextView pickedTime;
    AppointmentsController controller;



    public CreateAppointment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_appointment, container, false);


        pickDate = view.findViewById(R.id.pick_date_btn);
        pickTime = view.findViewById(R.id.pick_time_btn);
        done = view.findViewById(R.id.create_appointment);
        pickedDate = view.findViewById(R.id.create_date);
        pickedTime = view.findViewById(R.id.create_time);


        AppointmentDate date = ModelFacade.createAppointmentDate();
         controller = new AppointmentsController(this,date);


        return view;

    }


    public Button getPickTime() {
        return pickTime;
    }

    public void setPickTime(Button pickTime) {
        this.pickTime = pickTime;
    }

    public Button getPickDate() {
        return pickDate;
    }

    public void setPickDate(Button pickDate) {
        this.pickDate = pickDate;
    }

    public Button getDone() {
        return done;
    }

    public void setDone(Button done) {
        this.done = done;
    }

    public TextView getPickedDate() {
        return pickedDate;
    }

    public void setPickedDate(TextView pickedDate) {
        this.pickedDate = pickedDate;
    }

    public TextView getPickedTime() {
        return pickedTime;
    }

    public void setPickedTime(TextView pickedTime) {
        this.pickedTime = pickedTime;
    }

    public void pickDateBtnListener(View.OnClickListener listener){

        getPickDate().setOnClickListener(listener);

    }

    public void pickTimeBtnListener(View.OnClickListener listener){

        getPickTime().setOnClickListener(listener);

    }

    public void doneBtnListener(View.OnClickListener listener){

        getDone().setOnClickListener(listener);

    }









}
