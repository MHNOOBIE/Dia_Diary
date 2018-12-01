package com.example.mahdi.dia_diary;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.annotation.Nullable;

public class GlucoseTrackerActivity extends AppCompatActivity {

    private TextView time_tv,type_tv,glucose_tv;
    private TableLayout tableLayout;
    private TableRow tableRow;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glucose_tracker);

        tableLayout = findViewById(R.id.tableLayout);
        tableLayout.setColumnStretchable(0,true);
        tableLayout.setColumnStretchable(1,true);
        tableLayout.setColumnStretchable(2,true);

        db = FirebaseFirestore.getInstance();

        db.collection("GlucoseEntry").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Glucose").orderBy("time").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentSnapshot dc: queryDocumentSnapshots){
                    //LogEntry log = dc.toObject(LogEntry.class);
                   // date_text.setText(log.getTime().toString());
                   // gluc_text.setText(log.getGluc_value());
                    Glucose glucose = dc.toObject(Glucose.class);
                    tableRow = new TableRow(GlucoseTrackerActivity.this);

                    time_tv = new TextView(GlucoseTrackerActivity.this);
                    type_tv = new TextView(GlucoseTrackerActivity.this);
                    glucose_tv = new TextView(GlucoseTrackerActivity.this);


                    String date_n = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).format(glucose.time);

                    time_tv.setText(date_n);
                    time_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                    time_tv.setTextColor(Color.parseColor("#111111"));

                    type_tv.setText(glucose.type);
                    type_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                    type_tv.setTextColor(Color.parseColor("#111111"));


                    glucose_tv.setText(glucose.gluc_value);
                    glucose_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                    glucose_tv.setTextColor(Color.parseColor("#111111"));

                    tableRow.addView(time_tv);
                    tableRow.addView(type_tv);
                    tableRow.addView(glucose_tv);

                    tableLayout.addView(tableRow);

                }
            }
        });
    }
}
