package com.example.desmond.clinc.AppointmentsState;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.example.desmond.clinc.Model.Appointment;

/**
 * Created by Desmond on 12/12/2017.
 */

public class PendingState implements State {
    @Override
    public void previewStyle(TextView view) {

        view.setText("Pending");
        view.setBackgroundColor(Color.parseColor("#3284FF"));

    }

    @Override
    public String toString() {
        return "Pending";
    }
}
