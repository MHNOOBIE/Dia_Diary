package com.example.mahdi.dia_diary;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class Login_Activity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private Button login_bt;
    private Button register_bt;
    private EditText email_id;
    private EditText password;
    private ProgressBar pbar;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
        if (currentUser!=null){
            Toast.makeText(this,"User Logged in",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Login_Activity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }
      /*  else {
            Toast.makeText(this,"User Not Logged in",Toast.LENGTH_LONG).show();
        } */
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_bt = findViewById(R.id.login_bt);
        register_bt = findViewById(R.id.register_bt);
        email_id = findViewById(R.id.email_id);
        password = findViewById(R.id.password);
        pbar = findViewById(R.id.pbar);

        login_bt.setOnClickListener(this);
        register_bt.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.register_bt)
        {
            Intent intent = new Intent(Login_Activity.this,Register_Activity.class);
            startActivity(intent);
        }

        if(v.getId() == R.id.login_bt)
        {
            String Email = email_id.getText().toString();
            String Password = password.getText().toString();
            if(Email.isEmpty()){
                Toast.makeText(this,"Enter your email .",Toast.LENGTH_SHORT).show();
                return;
            }

            if(Password.isEmpty()){
                Toast.makeText(this,"Enter your password .",Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(Login_Activity.this,"Login Successful",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Login_Activity.this,HomeActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                // If sign in fails, display a message to the user.
                                if(task.getException()!=null)
                                Toast.makeText(Login_Activity.this, "Authentication Failed ." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                pbar.setVisibility(View.GONE);
                                login_bt.setVisibility(View.VISIBLE);



                                }



                            // ...
                        }
                    });

           // Intent intent = new Intent(Login_Activity.this,Register_Activity.class);
           // startActivity(intent);
        }
    }
}
