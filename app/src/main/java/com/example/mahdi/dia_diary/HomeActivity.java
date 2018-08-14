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
    private Button gt_bt;
    private Button log_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        logout_bt = findViewById(R.id.logout_bt);
        gt_bt = findViewById(R.id.gt_bt);
        log_bt = findViewById(R.id.log_bt);

        logout_bt.setOnClickListener(this);
        gt_bt.setOnClickListener(this);
        log_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.logout_bt){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(HomeActivity.this,"Successfully logged out .",Toast.LENGTH_LONG).show();

            Intent intent = new Intent(HomeActivity.this,Login_Activity.class);
            startActivity(intent);
            finish();
        }
        if(v.getId()==R.id.gt_bt){
            Intent intent = new Intent(HomeActivity.this,GlucoseTrackerActivity.class);
            startActivity(intent);
        }
        if(v.getId()==R.id.log_bt){
            Intent intent = new Intent(HomeActivity.this,LogEntry_Activity.class);
            startActivity(intent);

        }
    }
}
