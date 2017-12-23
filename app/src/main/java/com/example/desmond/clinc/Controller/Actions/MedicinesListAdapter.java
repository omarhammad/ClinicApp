package com.example.desmond.clinc.Controller.Actions;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.desmond.clinc.R;

import java.util.ArrayList;

/**
 * Created by Desmond on 12/20/2017.
 */

public class MedicinesListAdapter extends RecyclerView.Adapter<MedicinesListAdapter.MedicinesViewHolder> {


    Context context;
    ArrayList<String> mediciens;

    public MedicinesListAdapter(Context context, ArrayList<String> mediciens) {
        this.context = context;
        this.mediciens = mediciens;
    }

    @Override
    public MedicinesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.medicenes_item_layout, parent, false);
        return new MedicinesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MedicinesViewHolder holder, int position) {
        holder.mediceneName.setText(mediciens.get(position));

    }

    @Override
    public int getItemCount() {
        return mediciens.size();
    }

    public class MedicinesViewHolder extends RecyclerView.ViewHolder{


        TextView mediceneName;
        public MedicinesViewHolder(View itemView) {
            super(itemView);

            mediceneName = itemView.findViewById(R.id.medicine_name);

        }
    }


    public ArrayList<String> getMedicines() {
        return mediciens;
    }
}
