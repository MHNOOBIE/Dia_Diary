package com.example.mahdi.dia_diary;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DoctorLogin_Activity extends AppCompatActivity implements View.OnClickListener {

    private Button login_bt;
    private EditText email_id;
    private EditText password;
    private ProgressBar pbar;
    private TextView loginPatient_tv;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_login_);

        login_bt = findViewById(R.id.login_bt);
        email_id = findViewById(R.id.email_id);
        password = findViewById(R.id.password);
        pbar = findViewById(R.id.pbar);
        loginPatient_tv = findViewById(R.id.loginPatient_tv);

        mAuth = FirebaseAuth.getInstance();

        login_bt.setOnClickListener(this);
        loginPatient_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == login_bt.getId()) {

            String Email = email_id.getText().toString();
            String Password = password.getText().toString();

            if (Email.isEmpty()) {
                Toast.makeText(this, "Enter your email .", Toast.LENGTH_SHORT).show();
                return;
            }

            if (Password.isEmpty()) {
                Toast.makeText(this, "Enter your password .", Toast.LENGTH_SHORT).show();
                return;
            }

            login_bt.setVisibility(View.GONE);
            pbar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(DoctorLogin_Activity.this, "Login Successful", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(DoctorLogin_Activity.this, DoctorRoot_Activity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                // If sign in fails, display a message to the user.
                                if (task.getException() != null)
                                    Toast.makeText(DoctorLogin_Activity.this, "Authentication Failed ." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                pbar.setVisibility(View.GONE);
                                login_bt.setVisibility(View.VISIBLE);


                            }


                            // ...
                        }
                    });

        } else if (view.getId() == loginPatient_tv.getId()) {
            Intent intent = new Intent(DoctorLogin_Activity.this,Login_Activity.class);
            startActivity(intent);
            finish();
        }
    }
}
