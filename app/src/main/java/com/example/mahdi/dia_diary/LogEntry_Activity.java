package com.example.mahdi.dia_diary;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class LogEntry_Activity extends AppCompatActivity implements View.OnClickListener{


    private EditText gluc_et;
    private Button submit_bt;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        gluc_et = findViewById(R.id.gluc_et);
        submit_bt = findViewById(R.id.submit_bt);
        progressBar = findViewById(R.id.progressBar);




        submit_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.submit_bt){
            submit_bt.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
           String gluc_value = gluc_et.getText().toString();
           if(Float.parseFloat(gluc_value)>60){
               Toast.makeText(this, "Value must be between 0 - 60 .", Toast.LENGTH_SHORT).show();
               submit_bt.setVisibility(View.VISIBLE);
               progressBar.setVisibility(View.INVISIBLE);               return;
           }
           LogEntry log = new LogEntry(gluc_value, Calendar.getInstance().getTime());
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("LogEntry").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Glucose").document().set(log).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(LogEntry_Activity.this,"Log Succesfully recorded.",Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(LogEntry_Activity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    submit_bt.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);

                    Toast.makeText(LogEntry_Activity.this,"Failed to access database. Try again."+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
