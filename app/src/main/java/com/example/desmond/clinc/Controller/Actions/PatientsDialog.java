package com.example.desmond.clinc.Controller.Actions;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.desmond.clinc.Model.Patient;
import com.example.desmond.clinc.Model.User;
import com.example.desmond.clinc.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

/**
 * Created by Desmond on 12/20/2017.
 */

@SuppressLint("ValidFragment")
public class PatientsDialog extends DialogFragment {


    AlertDialog dialog;

    public PatientsDialog( AlertDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        return getDialog();

    }

    @Override
    public AlertDialog getDialog() {
        return dialog;
    }

    public void setDialog(AlertDialog dialog) {
        this.dialog = dialog;
    }
}
