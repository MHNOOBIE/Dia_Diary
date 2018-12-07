package com.example.mahdi.dia_diary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SupervisedPatient_Activity extends AppCompatActivity implements View.OnClickListener {

    private Button glucoseTracker_bt, insulinTracker_bt,mixedTracker_bt;
    private TextView name_tv, email_tv, country_tv;
    private String doctor_id, pat_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervised_patient_);

        name_tv = findViewById(R.id.name_tv);
        email_tv = findViewById(R.id.email_tv);
        country_tv = findViewById(R.id.country_tv);

        glucoseTracker_bt = findViewById(R.id.glucoseTracker_bt);
        insulinTracker_bt = findViewById(R.id.insulinTracker_bt);
        mixedTracker_bt = findViewById(R.id.mixedTracker_bt);


        Intent intent = getIntent();
        doctor_id = FirebaseAuth.getInstance().getUid();

        pat_id = intent.getStringExtra("ID");

        name_tv.setText(intent.getStringExtra("Name"));
        email_tv.setText(intent.getStringExtra("Email"));
        country_tv.setText(intent.getStringExtra("Country"));

        glucoseTracker_bt.setOnClickListener(this);
        insulinTracker_bt.setOnClickListener(this);
        mixedTracker_bt.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if(view.getId() == glucoseTracker_bt.getId()){
            Intent intent = new Intent(SupervisedPatient_Activity.this,SupervisedGlucose_Activity.class);
            intent.putExtra("ID",pat_id);
            startActivity(intent);
        }
        else if(view.getId() == insulinTracker_bt.getId()){
            Intent intent = new Intent(SupervisedPatient_Activity.this,SupervisedInsulin_Activity.class);
            intent.putExtra("ID",pat_id);
            startActivity(intent);
        }

        else if(view.getId() == mixedTracker_bt.getId()){
            Intent intent = new Intent(SupervisedPatient_Activity.this,SupervisedMixed_Activity.class);
            intent.putExtra("ID",pat_id);
            startActivity(intent);
        }
    }
}
