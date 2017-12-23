package com.example.desmond.clinc.Controller.Actions;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Desmond on 12/12/2017.
 */

public class OpenTimePickerFragment extends android.app.DialogFragment{


    TimePickerDialog.OnTimeSetListener timeSetListener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        TimePickerDialog pickerDialog =
                new TimePickerDialog(getActivity(), getTimeSetListener(), hour, 00, DateFormat.is24HourFormat(getActivity()));
        return pickerDialog;
    }


    public TimePickerDialog.OnTimeSetListener getTimeSetListener() {
        return timeSetListener;
    }

    public void setTimeSetListener(TimePickerDialog.OnTimeSetListener timeSetListener) {
        this.timeSetListener = timeSetListener;
    }
}
