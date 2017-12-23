package com.example.desmond.clinc.Controller.Actions;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import java.util.Calendar;

/**
 * Created by Desmond on 12/12/2017.
 */

public class OpenDatePickerFragment extends DialogFragment  {

    DatePickerDialog.OnDateSetListener dateSetListener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog pickerDialog =  new DatePickerDialog(getActivity(), getDateSetListener(), yy, mm, dd);
        pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        return pickerDialog;
    }

    public DatePickerDialog.OnDateSetListener getDateSetListener() {
        return dateSetListener;
    }

    public void setDateSetListener(DatePickerDialog.OnDateSetListener dateSetListener) {
        this.dateSetListener = dateSetListener;
    }
}
