package com.example.desmond.clinc.Controller.Actions;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.desmond.clinc.Model.Appointment;
import com.example.desmond.clinc.R;

import java.util.List;

/**
 * Created by Desmond on 12/17/2017.
 */

public class ShowAppointmentsAdapter extends RecyclerView.Adapter<ShowAppointmentsAdapter.AppointmentViewHolder> {


    Context context;
    List<Appointment> appointments;

    public ShowAppointmentsAdapter(Context context, List<Appointment> appointments) {
        this.context = context;
        this.appointments = appointments;
    }

    @Override

    public AppointmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.appointment_layout, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AppointmentViewHolder holder, int position) {

        Appointment appointment = appointments.get(position);
        holder.date.setText(appointment.getAppointmentDate().getAppointmentDate());
        holder.time.setText(appointment.getAppointmentDate().getAppointmentTime());
       String username = appointment.getUser().getFirstName() + " " + appointment.getUser().getLastName();
        holder.userName.setText(username);
        appointment.getState().previewStyle(holder.state);

    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder {


        TextView date;
        TextView time;
        TextView userName;
        TextView state;
        public AppointmentViewHolder(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.show_date);
            time = itemView.findViewById(R.id.show_time);
            userName = itemView.findViewById(R.id.show_user_name);
            state = itemView.findViewById(R.id.show_state);
        }
    }


    public List<Appointment> getAppointments() {
        return appointments;
    }
}
