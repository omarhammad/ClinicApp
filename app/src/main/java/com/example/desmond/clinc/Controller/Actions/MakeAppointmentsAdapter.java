package com.example.desmond.clinc.Controller.Actions;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.example.desmond.clinc.Model.Appointment;
import com.example.desmond.clinc.Model.CallBacks.AppointmentsCallback;
import com.example.desmond.clinc.Model.CallBacks.CheckCallback;
import com.example.desmond.clinc.Model.Firebase.AppointmentDB;
import com.example.desmond.clinc.Model.Firebase.DBFacade;
import com.example.desmond.clinc.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Desmond on 12/14/2017.
 */

public class MakeAppointmentsAdapter extends ExpandableRecyclerAdapter<AppointmentParent, Appointment, MakeAppointmentsAdapter.DoctorsViewHolder, MakeAppointmentsAdapter.AppointmentViewHolder> {

    Context context;
    LayoutInflater inflater;
    DBFacade db;


    public MakeAppointmentsAdapter(Context context, ArrayList<AppointmentParent> doctors) {
        super(doctors);
        this.context = context;
        inflater = LayoutInflater.from(context);
        db = DBFacade.getINSTANCE(context);
    }

    @NonNull
    @Override
    public DoctorsViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View view = inflater.inflate(R.layout.available_doctors_layout, parentViewGroup, false);
        return new DoctorsViewHolder(view);
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View view = inflater.inflate(R.layout.available_time_layout, childViewGroup, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(@NonNull DoctorsViewHolder parentViewHolder, int parentPosition, @NonNull AppointmentParent doctorName) {

        parentViewHolder.doctorName.setText(doctorName.getDoctorName());

    }


    @Override
    public void onBindChildViewHolder(@NonNull AppointmentViewHolder childViewHolder, final int parentPosition, final int childPosition, @NonNull final Appointment appointment) {

        childViewHolder.appointmentDate.setText(appointment.getAppointmentDate().getAppointmentDate());
        childViewHolder.appointmentTime.setText(appointment.getAppointmentDate().getAppointmentTime());
        childViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.makeAppointment(appointment, new CheckCallback() {
                    @Override
                    public void success() {
                        Toast.makeText(context, "Appointment Made...", Toast.LENGTH_LONG).show();
                        db.getAllAvailableAppointments(new AppointmentsCallback() {

                            @Override
                            public void success(ArrayList<AppointmentParent> appointments) {
                                setParentList(appointments, false);
                            }

                            @Override
                            public void fail() {

                            }
                        });
                    }

                    @Override
                    public void fail() {
                        Toast.makeText(context, "Appointment making Failed...", Toast.LENGTH_LONG).show();
                    }
                });


            }
        });

    }

    public class DoctorsViewHolder extends ParentViewHolder {


        /**
         * Default constructor.
         *
         * @param itemView The {@link View} being hosted in this ViewHolder
         */


        TextView doctorName;

        public DoctorsViewHolder(@NonNull View itemView) {
            super(itemView);

            doctorName = itemView.findViewById(R.id.doctor_name);
        }
    }

    public class AppointmentViewHolder extends ChildViewHolder {

        /**
         * Default constructor.
         *
         * @param itemView The {@link View} being hosted in this ViewHolder
         */

        TextView appointmentDate;
        TextView appointmentTime;
        View itemView;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            appointmentDate = itemView.findViewById(R.id.appointment_date);
            appointmentTime = itemView.findViewById(R.id.appointment_time);


        }
    }

    @Override
    public void setParentList(@NonNull List<AppointmentParent> parentList, boolean preserveExpansionState) {
        super.setParentList(parentList, preserveExpansionState);
    }
}
