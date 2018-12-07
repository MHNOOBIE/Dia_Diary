package com.example.mahdi.dia_diary;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {


    private ArrayList<Users> patientList;
    private OnitemClickListener mListener;

    public interface OnitemClickListener {
        void onItemClick(int position);

    }

    public void setOnItemClickListener(OnitemClickListener listener){
        mListener = listener;
    }
    public static class PatientViewHolder extends RecyclerView.ViewHolder{

        public TextView name,email,country;

        public PatientViewHolder(View itemView, final OnitemClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.patientname_tv);
            email = itemView.findViewById(R.id.patientemail_tv);
            country = itemView.findViewById(R.id.country_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }

    public PatientAdapter(ArrayList<Users> patientList){
        this.patientList = patientList;
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_item,parent,false);
        return new PatientViewHolder(v,mListener);


    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        Users user = patientList.get(position);
        holder.name.setText(user.getName());
        holder.country.setText(user.getCountry());
        holder.email.setText(user.getEmail());
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }
}
