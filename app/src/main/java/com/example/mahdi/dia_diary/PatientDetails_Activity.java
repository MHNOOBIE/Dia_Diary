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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PatientDetails_Activity extends AppCompatActivity implements View.OnClickListener {

    private Button requestreject_bt, requestaccept_bt;
    private TextView name_tv, email_tv, country_tv;
    private String doctor_id, pat_id;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientrequest_details_);

        requestaccept_bt = findViewById(R.id.requestaccept_bt);
        requestreject_bt = findViewById(R.id.requestreject_bt);

        name_tv = findViewById(R.id.name_tv);
        email_tv = findViewById(R.id.email_tv);
        country_tv = findViewById(R.id.country_tv);

        Intent intent = getIntent();
        doctor_id = FirebaseAuth.getInstance().getUid();

        pat_id = intent.getStringExtra("ID");

        name_tv.setText(intent.getStringExtra("Name"));
        email_tv.setText(intent.getStringExtra("Email"));
        country_tv.setText(intent.getStringExtra("Country"));

        db = FirebaseFirestore.getInstance();

        requestreject_bt.setOnClickListener(this);
        requestaccept_bt.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == requestaccept_bt.getId()) {
            requestreject_bt.setEnabled(false);
            requestaccept_bt.setText("Processing...");
            requestaccept_bt.setOnClickListener(null);
            Map<String, Object> data = new HashMap<>();
            data.put("pat_id", pat_id);
            db.collection("Doctors").document(doctor_id).collection("Supervising").document(pat_id).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(PatientDetails_Activity.this, "Accepted Succesfully", Toast.LENGTH_LONG).show();

                }
            }).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    db.collection("Requests").document(doctor_id).collection("Pending").document(pat_id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent = new Intent(PatientDetails_Activity.this, DoctorRoot_Activity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    ;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PatientDetails_Activity.this, "Ooop!Something went wrong!", Toast.LENGTH_LONG).show();
                    requestaccept_bt.setText("Accept Request");
                    requestaccept_bt.setOnClickListener(PatientDetails_Activity.this);
                    requestreject_bt.setEnabled(true);
                }
            });

        } else if (view.getId() == requestreject_bt.getId()) {
            requestaccept_bt.setEnabled(false);
            requestreject_bt.setText("Processing");
            requestreject_bt.setOnClickListener(null);
            db.collection("Requests").document(doctor_id).collection("Pending").document(pat_id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    Toast.makeText(PatientDetails_Activity.this, "Request Rejected!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(PatientDetails_Activity.this, DoctorRoot_Activity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PatientDetails_Activity.this, "Ooop!Something went wrong!", Toast.LENGTH_LONG).show();
                    requestreject_bt.setText("Reject Request");
                    requestreject_bt.setOnClickListener(PatientDetails_Activity.this);
                    requestaccept_bt.setEnabled(true);
                }
            });
        }

    }
}