package com.example.mahdi.dia_diary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class InteractWithDoctor_Activity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private DoctorAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interact_with_doctor);

        final ArrayList<Doctor> doctorArrayList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();

        db.collection("Doctors").orderBy("name").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentSnapshot dc : queryDocumentSnapshots) {


                    Doctor doctor = dc.toObject(Doctor.class);


                    doctorArrayList.add(doctor);


                }
                if (doctorArrayList.size() > 0) {
                    mRecyclerView = findViewById(R.id.recyclerView);
                    mRecyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(InteractWithDoctor_Activity.this);
                    mAdapter = new DoctorAdapter(doctorArrayList);

                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);

                    mAdapter.setOnItemClickListener(new DoctorAdapter.OnitemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Doctor temp = doctorArrayList.get(position);
                            Intent intent = new Intent(InteractWithDoctor_Activity.this,DoctorDetails_Activity.class);
                            intent.putExtra("Name",temp.name);
                            intent.putExtra("ID",temp.doctor_id);
                            intent.putExtra("Email",temp.email);
                            intent.putExtra("Hospital",temp.hospital);
                            startActivity(intent);

                        }
                    });

                } else {
                    Toast.makeText(InteractWithDoctor_Activity.this,"Oops! Something went wrong. Check your internet connection.",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
