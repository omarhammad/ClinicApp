package com.example.desmond.clinc.Controller.Actions;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.desmond.clinc.Model.Diagnosis;
import com.example.desmond.clinc.Model.Firebase.AuthenticationDB;
import com.example.desmond.clinc.R;
import com.example.desmond.clinc.View.AddMedicines;
import com.example.desmond.clinc.View.ShowDiagnosis;

import java.util.ArrayList;

/**
 * Created by Desmond on 12/21/2017.
 */

public class DiagnosisAdapter extends RecyclerView.Adapter<DiagnosisAdapter.DiagnosisViewHolder> {


    Context context;
    ArrayList<Diagnosis> diagnoses;
    FragmentActivity activity;


    public DiagnosisAdapter(Context context,FragmentActivity activity, ArrayList<Diagnosis> diagnoses) {
        this.context = context;
        this.activity = activity;
        this.diagnoses = diagnoses;
    }

    @Override
    public DiagnosisViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.diagnosis_list_item, parent, false);
        return new DiagnosisViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DiagnosisViewHolder holder, int position) {

        final Diagnosis diagnosis = diagnoses.get(position);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.getIntent().putExtra("ShowDiagnosis",diagnosis);
                FragmentUtils.replaceFragment(activity, R.id.container, true, new ShowDiagnosis());

            }
        });


        holder.diseaseName.setText(diagnosis.getDiseasesName());
        String username;
        if (AuthenticationDB.USER_TYPE.equals("patient")) {
            username = "Doctor : "+diagnosis.getDoctor().getFirstName() + " " + diagnosis.getDoctor().getLastName();
            holder.userName.setText(username);
        }else{
            username = "Patient : "+diagnosis.getPatient().getFirstName() + " " + diagnosis.getPatient().getLastName();
            holder.userName.setText(username);
        }
        String date = diagnosis.getDateCreated();
        holder.date.setText(date.split("@")[0]);
        holder.time.setText(date.split("@")[1]);

    }

    @Override
    public int getItemCount() {
        return diagnoses.size();
    }

    public class DiagnosisViewHolder extends RecyclerView.ViewHolder{


        TextView diseaseName;
        TextView userName;
        TextView date;
        TextView time;
        View view;

        public DiagnosisViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            diseaseName = itemView.findViewById(R.id.disease_name);
            userName = itemView.findViewById(R.id.user_name);
            date = itemView.findViewById(R.id.diagnosis_date);
            time = itemView.findViewById(R.id.diagnosis_time);


        }
    }


    public ArrayList<Diagnosis> getDiagnoses() {
        return diagnoses;
    }
}
