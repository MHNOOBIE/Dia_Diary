package com.example.mahdi.dia_diary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class DoctorRoot_Activity extends AppCompatActivity implements View.OnClickListener {
    private TextView greeting_tv;
    private Button patientlist_bt, supervisionrequests_bt, logout_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_root_);

        greeting_tv = findViewById(R.id.greetings_tv);
        supervisionrequests_bt = findViewById(R.id.supervisionrequests_bt);
        patientlist_bt = findViewById(R.id.patientlist_bt);
        logout_bt = findViewById(R.id.logout_bt);


        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            greeting_tv.setText("Good Morning Doctor!");
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            greeting_tv.setText("Good Afternoon Doctor!");
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            greeting_tv.setText("Good Evening Doctor!");
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            greeting_tv.setText("Good Night Doctor!");
        }

        supervisionrequests_bt.setOnClickListener(this);
        patientlist_bt.setOnClickListener(this);
        logout_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == supervisionrequests_bt.getId()) {
            Intent intent = new Intent(DoctorRoot_Activity.this, SuperVisionRequests_Activity.class);
            startActivity(intent);
        } else if (view.getId() == patientlist_bt.getId()) {
            Intent intent = new Intent(DoctorRoot_Activity.this, PatientList_Activity.class);
            startActivity(intent);
        } else if (view.getId() == logout_bt.getId()) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(DoctorRoot_Activity.this, Login_Activity.class);
            startActivity(intent);
            finish();

        }
    }
}
