package com.example.mahdi.dia_diary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class GlucoseTrackerActivity extends AppCompatActivity {

    private TextView date_text;
    private TextView gluc_text;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glucose_tracker);
        date_text = findViewById(R.id.date_text);
        gluc_text = findViewById(R.id.gluc_text);
        db = FirebaseFirestore.getInstance();

        db.collection("LogEntry").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Glucose").orderBy("time").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentSnapshot dc: queryDocumentSnapshots){
                    LogEntry log = dc.toObject(LogEntry.class);
                    date_text.setText(log.getTime().toString());
                    gluc_text.setText(log.getGluc_value());
                }
            }
        });
    }
}
