package com.example.mahdi.dia_diary;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Register_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private String country;
    private EditText username;
    private EditText password;
    private EditText cpassword;
    private EditText email_id;
    private Button register_bt;
    private FirebaseAuth mAuth;
    private ProgressBar pbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        cpassword = findViewById(R.id.cpassword);
        email_id = findViewById(R.id.email_id);
        register_bt = findViewById(R.id.register_bt);

        register_bt.setOnClickListener(this);
        Spinner spinner = findViewById(R.id.c_spinner);
        pbar = findViewById(R.id.pbar);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.countries_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        country = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.register_bt) {
            final String Email = email_id.getText().toString();
            String Password = password.getText().toString();
            String CPassword = cpassword.getText().toString();
            final String Username = username.getText().toString();


            if (Username.isEmpty()) {
                Toast.makeText(this, "Enter your Username .", Toast.LENGTH_SHORT).show();
                return;
            }

            if (Password.isEmpty()) {
                Toast.makeText(this, "Enter your Password .", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!CPassword.equals(Password)) {
                Toast.makeText(this, "Passwords do not match .", Toast.LENGTH_SHORT).show();
                return;
            }

            if (Email.isEmpty()) {
                Toast.makeText(this, "Enter your Email .", Toast.LENGTH_SHORT).show();
                return;
            }

            if (country.equals("Select Your Country")) {
                Toast.makeText(this, "Select your country .", Toast.LENGTH_SHORT).show();
                return;
            }

            register_bt.setVisibility(View.GONE);
            pbar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information

                                Users user = new Users(Username, country,Email);
                                FirebaseFirestore db = FirebaseFirestore.getInstance();

                                db.collection("Users").document(mAuth.getCurrentUser().getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Register_Activity.this, "Registration Successful .", Toast.LENGTH_LONG).show();
                                        //FirebaseUser user = mAuth.getCurrentUser();
                                        //updateUI(user);

                                        Intent intent = new Intent(Register_Activity.this, Login_Activity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        mAuth.getCurrentUser().delete();
                                        Toast.makeText(Register_Activity.this, "Registration Failed. Could not connect to database. Try again later ." + e.getMessage(), Toast.LENGTH_LONG).show();

                                    }
                                });


                            } else {
                                if (task.getException() != null)
                                    Toast.makeText(Register_Activity.this, "Registration Failed ." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                pbar.setVisibility(View.GONE);
                                register_bt.setVisibility(View.VISIBLE);


                            }

                            // ...
                        }
                    });


        }
    }
}
