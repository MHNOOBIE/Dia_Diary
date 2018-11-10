package com.example.mahdi.dia_diary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

public class GlucoseModuleActivity extends AppCompatActivity implements View.OnClickListener {
    private Button in_bt,track_bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glucose_module);
        in_bt = findViewById(R.id.in_bt);
        track_bt = findViewById(R.id.track_bt);
        in_bt.setOnClickListener(this);
        track_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==in_bt.getId()){
                Intent intent = new Intent(GlucoseModuleActivity.this,Input_Acitivty.class);
                startActivity(intent);
        }
        if(v.getId()==track_bt.getId()){

        }
    }

}
