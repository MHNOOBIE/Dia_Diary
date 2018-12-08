package com.example.mahdi.dia_diary;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DoctorDetails_Activity extends AppCompatActivity implements View.OnClickListener {

    private Button request_bt;
    private TextView name_tv, email_tv, hospital_tv;
    private String doctor_id, pat_id;
    int supervised = 0;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();

        String name = intent.getStringExtra("Name");
        doctor_id = intent.getStringExtra("ID");
        pat_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String email = intent.getStringExtra("Email");
        String hospital = intent.getStringExtra("Hospital");


        request_bt = findViewById(R.id.request_bt);
        name_tv = findViewById(R.id.name_tv);
        email_tv = findViewById(R.id.email_tv);
        hospital_tv = findViewById(R.id.hospital_tv);

        name_tv.setText(name);
        email_tv.setText(email);
        hospital_tv.setText(hospital);

        request_bt.setText("Checking State...");

        db.collection("Doctors").document(doctor_id).collection("Supervising").document(pat_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(!task.getResult().exists()){
                    db.collection("Requests").document(doctor_id).collection("Pending").document(pat_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.getResult().exists()){
                                request_bt.setText("Waiting for doctor's response...");
                            }

                            else {
                                request_bt.setText("Request Assistance");
                                request_bt.setOnClickListener(DoctorDetails_Activity.this);
                            }
                        }
                    });
                }else supervised=1;
            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(supervised==1)
                request_bt.setText("Already under supervision");
            }
        });





    }

    @Override
    public void onClick(View view) {
        if (view.getId() == request_bt.getId()) {
            request_bt.setText("Sending Request");
            request_bt.setOnClickListener(null);
            Map<String, Object> data = new HashMap<>();

            data.put("pat_id", pat_id);
            db.collection("Requests").document(doctor_id).collection("Pending").document(pat_id).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(DoctorDetails_Activity.this, "Request Succesfully Sent.Please wait for response :) .", Toast.LENGTH_LONG).show();
                    request_bt.setText("Waiting for doctor's response...");
                    request_bt.setOnClickListener(null);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(DoctorDetails_Activity.this, "Ooops! Something went wrong.Try again later.", Toast.LENGTH_LONG).show();
                    request_bt.setText("Request Assistance");
                    request_bt.setOnClickListener(DoctorDetails_Activity.this);

                }
            });
        }
    }
}


