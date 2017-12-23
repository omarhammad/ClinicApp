package com.example.desmond.clinc.AppointmentsState;

import android.graphics.Color;
import android.widget.TextView;

/**
 * Created by Desmond on 12/12/2017.
 */

public class NonState implements State {
    @Override
    public void previewStyle(TextView view) {

        view.setText("NonYet");
        view.setBackgroundColor(Color.parseColor("#FFBB00"));


    }

    @Override
    public String toString() {
        return "Non State";
    }
}
