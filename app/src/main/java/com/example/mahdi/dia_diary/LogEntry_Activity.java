package com.example.mahdi.dia_diary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LogEntry_Activity extends AppCompatActivity implements View.OnClickListener {
    private Button glucoseInput_bt, insulinInput_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_entry);
        glucoseInput_bt = findViewById(R.id.glucoseTracker_bt);
        insulinInput_bt = findViewById(R.id.insulinTracker_bt);
        glucoseInput_bt.setOnClickListener(this);
        insulinInput_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == glucoseInput_bt.getId()) {
            Intent intent = new Intent(LogEntry_Activity.this, GlucoseInput_Acitivty.class);
            startActivity(intent);
        }
        if (v.getId() == insulinInput_bt.getId()) {
            Intent intent = new Intent(LogEntry_Activity.this, InsulinInput_Activity.class);
            startActivity(intent);
        }
    }

}
