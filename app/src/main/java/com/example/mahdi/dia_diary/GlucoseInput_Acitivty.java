package com.example.mahdi.dia_diary;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.Calendar;
import java.util.Date;

public class GlucoseInput_Acitivty extends AppCompatActivity implements View.OnClickListener {
    private Button submit_bt, time_bt, date_bt;
    private ProgressBar progressBar;
    private EditText glucose_et;
    private String type;
    private int hour, min, y, m, d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glucose_input);
        submit_bt = findViewById(R.id.submit_bt);
        glucose_et = findViewById(R.id.insulin_et);
        time_bt = findViewById(R.id.time_bt);
        date_bt = findViewById(R.id.date_bt);
        progressBar = findViewById(R.id.progressBar);

        MaterialSpinner spinner = findViewById(R.id.type_spinner);
        spinner.setItems("Random", "Fasting", "After Meal");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                type = item;
            }
        });
        submit_bt.setOnClickListener(this);
        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);
        y = calendar.get(Calendar.YEAR);
        m = calendar.get(Calendar.MONTH);
        d = calendar.get(Calendar.DAY_OF_MONTH);

        date_bt.setText((m + 1) + "/" + d + "/" + y);
        date_bt.setOnClickListener(this);

        time_bt.setText(String.format("%02d:%02d", hour, min));
        time_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == submit_bt.getId()) {
            String gluc_value = glucose_et.getText().toString();

            if (TextUtils.isEmpty(type)) {
                type = "Random";
            }

            if (TextUtils.isEmpty(gluc_value)) {
                Toast.makeText(this, "Enter glucose value .", Toast.LENGTH_SHORT).show();
                return;
            } else if (Float.parseFloat(gluc_value) > 60) {
                Toast.makeText(this, "Value must be between 0 - 60 .", Toast.LENGTH_SHORT).show();
                return;
            }
            Date date = new Date(y-1900, m, d, hour, min);
            Glucose glucose = new Glucose(gluc_value, date, type);
            submit_bt.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("GlucoseEntry").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Glucose").add(glucose).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(GlucoseInput_Acitivty.this, "Log Succesfully recorded.", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(GlucoseInput_Acitivty.this, PatientModule_Activity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    submit_bt.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);

                    Toast.makeText(GlucoseInput_Acitivty.this, "Failed to access database. Try again." + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }
        if (v.getId() == time_bt.getId()) {
            TimePickerDialog timePickerDialog;
            timePickerDialog = new TimePickerDialog(GlucoseInput_Acitivty.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    time_bt.setText(String.format("%02d:%02d", hourOfDay, minute));
                    hour = hourOfDay;
                    min = minute;
                }
            }, hour, min, true);
            timePickerDialog.show();
        }

        if (v.getId() == date_bt.getId()) {
            DatePickerDialog datePickerDialog;
            datePickerDialog = new DatePickerDialog(GlucoseInput_Acitivty.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    date_bt.setText((month + 1) + "/" + dayOfMonth + "/" + year);
                    m = month;
                    d = dayOfMonth;
                    y = year;
                }
            }, y, m, d);
            datePickerDialog.show();
        }
    }

}
