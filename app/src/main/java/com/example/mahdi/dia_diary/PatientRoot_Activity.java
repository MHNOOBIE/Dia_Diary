package com.example.mahdi.dia_diary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class PatientRoot_Activity extends AppCompatActivity implements View.OnClickListener {
    private Button pm_bt, doctor_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_root);

        pm_bt = findViewById(R.id.patientlist_bt);
        doctor_bt = findViewById(R.id.supervisionrequests_bt);
        Button logout_bt = findViewById(R.id.logout_bt);

        TextView greetings_tv = findViewById(R.id.greetings_tv);


        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            greetings_tv.setText("Good Morning");
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            greetings_tv.setText("Good Afternoon");
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            greetings_tv.setText("Good Evening");
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            greetings_tv.setText("Good Night");
        }

        pm_bt.setOnClickListener(this);
        doctor_bt.setOnClickListener(this);
        logout_bt.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if (v.getId() == pm_bt.getId()) {
            Intent intent = new Intent(PatientRoot_Activity.this, PatientModule_Activity.class);
            startActivity(intent);
        } else if (v.getId() == doctor_bt.getId()) {
            Intent intent = new Intent(PatientRoot_Activity.this, InteractWithDoctor_Activity.class);
            startActivity(intent);

        }

        if (v.getId() == R.id.logout_bt) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(PatientRoot_Activity.this, "Successfully logged out .", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(PatientRoot_Activity.this, Login_Activity.class);
            startActivity(intent);
            finish();
        }


    }
}
