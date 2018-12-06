package com.example.mahdi.dia_diary;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {


    private ArrayList<Doctor> doctorList;
    private OnitemClickListener mListener;

    public interface OnitemClickListener {
        void onItemClick(int position);

    }

    public void setOnItemClickListener(OnitemClickListener listener){
        mListener = listener;
    }
    public static class DoctorViewHolder extends RecyclerView.ViewHolder{

        public TextView name,email,hospital;

        public DoctorViewHolder(View itemView, final OnitemClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.doctorname_tv);
            email = itemView.findViewById(R.id.doctoremail_tv);
            hospital = itemView.findViewById(R.id.hospital_tv);

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

    public DoctorAdapter(ArrayList<Doctor> doctorList){
        this.doctorList = doctorList;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_item,parent,false);
        return new DoctorViewHolder(v,mListener);


    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor doctor = doctorList.get(position);
        holder.name.setText(doctor.getName());
        holder.hospital.setText(doctor.getHospital());
        holder.email.setText(doctor.getEmail());
    }

    @Override
    public int getItemCount() {
      return doctorList.size();
    }
}
