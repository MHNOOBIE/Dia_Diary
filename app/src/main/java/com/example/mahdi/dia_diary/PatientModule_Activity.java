package com.example.mahdi.dia_diary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class PatientModule_Activity extends AppCompatActivity implements View.OnClickListener {

    private Button uploadReport_bt;
    private Button diaTracker_bt;
    private Button showReports_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientmodule);

        Button logEntry_bt = findViewById(R.id.logEntry_bt);
        uploadReport_bt = findViewById(R.id.uploadReport_bt);
        diaTracker_bt = findViewById(R.id.diaTracker_bt);
        showReports_bt = findViewById(R.id.showReports_bt);

        showReports_bt.setOnClickListener(this);

        logEntry_bt.setOnClickListener(this);
        uploadReport_bt.setOnClickListener(this);
        diaTracker_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.logEntry_bt) {
            Intent intent = new Intent(PatientModule_Activity.this, LogEntry_Activity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.uploadReport_bt) {
            Intent intent = new Intent(PatientModule_Activity.this, UploadReport_Activity.class);
            startActivity(intent);

        } else if (v.getId() == R.id.diaTracker_bt) {
            Intent intent = new Intent(PatientModule_Activity.this, DiaTracker_Activity.class);
            startActivity(intent);

        } else if (v.getId() == R.id.showReports_bt) {
            Intent intent = new Intent(PatientModule_Activity.this, ShowReports_Activity.class);
            startActivity(intent);

        }
    }
}
