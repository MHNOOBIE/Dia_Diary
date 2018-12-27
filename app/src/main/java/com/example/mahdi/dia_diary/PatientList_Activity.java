package com.example.mahdi.dia_diary;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class PatientList_Activity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private PatientAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView pt_tv;
    FirebaseFirestore db;
    FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list_);

        final ArrayList<Users> patientArrayList = new ArrayList<>();
        final ArrayList<String> patientIDList = new ArrayList<>();

        mauth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        pt_tv = findViewById(R.id.pt_tv);
        String doctor_id = mauth.getUid();

        final ArrayList<String> patient_id = new ArrayList<>();
        pt_tv.setText("Loading Patient List...");

        db.collection("Doctors").document(doctor_id).collection("Supervising").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentSnapshot dc : queryDocumentSnapshots) {


                    String pat_id = dc.getString("pat_id");
                    patient_id.add(pat_id);



                }
                if (patient_id.size() > 0) {

                    for (final String pat_id : patient_id) {

                        db.collection("Users").document(pat_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Users user = documentSnapshot.toObject(Users.class);
                                patientArrayList.add(user);
                                patientIDList.add(pat_id);
                            }
                        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (patientArrayList.size() > 0) {
                                    mRecyclerView = findViewById(R.id.recyclerView);
                                    mRecyclerView.setHasFixedSize(true);
                                    mLayoutManager = new LinearLayoutManager(PatientList_Activity.this);
                                    mAdapter = new PatientAdapter(patientArrayList);

                                    mRecyclerView.setLayoutManager(mLayoutManager);
                                    mRecyclerView.setAdapter(mAdapter);

                                    pt_tv.setText("Tap on a Patient");

                                   mAdapter.setOnItemClickListener(new PatientAdapter.OnitemClickListener() {
                                        @Override
                                        public void onItemClick(int position) {
                                            Users temp = patientArrayList.get(position);
                                            Intent intent = new Intent(PatientList_Activity.this,SupervisedPatient_Activity.class);
                                            intent.putExtra("Name",temp.name);
                                            intent.putExtra("ID",patientIDList.get(position));
                                            intent.putExtra("Email",temp.email);
                                            intent.putExtra("Country",temp.country);

                                            startActivity(intent);

                                        }
                                    });
                                } else {
                                    Toast.makeText(PatientList_Activity.this,"Oops! Something went wrong. Check your internet connection.",Toast.LENGTH_LONG).show();
                                }
                            }
                        });


                    }
                } else {
                    pt_tv.setText("No patients under supervision.");
                    Toast.makeText(PatientList_Activity.this, "No patients under supervision.", Toast.LENGTH_LONG).show();
                }
            }


        });

    }
}
