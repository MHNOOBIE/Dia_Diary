package com.example.mahdi.dia_diary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button logout_bt;
    private Button logEntry_bt;
    private Button uploadReport_bt;
    private Button diaTracker_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        logout_bt = findViewById(R.id.logout_bt);
        logEntry_bt = findViewById(R.id.logEntry_bt);
        uploadReport_bt = findViewById(R.id.uploadReport_bt);
        diaTracker_bt = findViewById(R.id.diaTracker_bt);

        logout_bt.setOnClickListener(this);
        logEntry_bt.setOnClickListener(this);
        uploadReport_bt.setOnClickListener(this);
        diaTracker_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.logout_bt) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(HomeActivity.this, "Successfully logged out .", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(HomeActivity.this, Login_Activity.class);
            startActivity(intent);
            finish();
        }
        if (v.getId() == R.id.logEntry_bt) {
            Intent intent = new Intent(HomeActivity.this, LogEntry_Activity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.uploadReport_bt) {
            Intent intent = new Intent(HomeActivity.this, LogEntry_Activity.class);
            startActivity(intent);

        }
        if(v.getId() == R.id.diaTracker_bt) {
            Intent intent = new Intent(HomeActivity.this, DiaTracker_Activity.class);
            startActivity(intent);

        }
    }
}
