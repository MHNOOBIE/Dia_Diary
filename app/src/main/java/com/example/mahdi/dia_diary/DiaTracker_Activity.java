package com.example.mahdi.dia_diary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DiaTracker_Activity extends AppCompatActivity implements View.OnClickListener {

    private Button glucoseTracker_bt, insulinTracker_bt, mixedTracker_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dia_tracker);

        glucoseTracker_bt = findViewById(R.id.glucoseTracker_bt);
        insulinTracker_bt = findViewById(R.id.insulinTracker_bt);
        mixedTracker_bt = findViewById(R.id.mixedTracker_bt);

        glucoseTracker_bt.setOnClickListener(DiaTracker_Activity.this);
        insulinTracker_bt.setOnClickListener(DiaTracker_Activity.this);
        mixedTracker_bt.setOnClickListener(DiaTracker_Activity.this);

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == glucoseTracker_bt.getId()) {
            Intent intent = new Intent(DiaTracker_Activity.this, GlucoseTrackerActivity.class);
            startActivity(intent);
        } else if (v.getId() == insulinTracker_bt.getId()) {
            Intent intent = new Intent(DiaTracker_Activity.this, InsulinTracker_Activity.class);
            startActivity(intent);
        } else if (v.getId() == mixedTracker_bt.getId()) {
            Intent intent = new Intent(DiaTracker_Activity.this, MixedTracker_Activity.class);
            startActivity(intent);
        }
    }
}
