package com.example.desmond.clinc.AppointmentsState;

import android.graphics.Color;
import android.widget.TextView;

/**
 * Created by Desmond on 12/12/2017.
 */

public class FinishState implements State {
    @Override
    public void previewStyle(TextView view) {

        view.setText("Finish");
        view.setBackgroundColor(Color.parseColor("#FF3424"));

    }

    @Override
    public String toString() {
        return "Finish";
    }
}
