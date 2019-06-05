package com.volunteam.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.volunteam.R;
import com.volunteam.components.FirebaseHandler;
import com.volunteam.components.User;

import java.util.ArrayList;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public EditText etEmail, etPassword;
    public Button btnSignIn, btnCreateAccount;

    public FirebaseAuth mAuth;
    public FirebaseUser mUser;
    public FirebaseDatabase mDatabase;
    public DatabaseReference userReference;


    FirebaseHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initFirebase();

        etEmail = (EditText)findViewById(R.id.etEmailLogin);
        etPassword = (EditText)findViewById(R.id.etPasswordLogin);
        btnSignIn = (Button)findViewById(R.id.logIn);
        btnCreateAccount = (Button)findViewById(R.id.createAccount);

        btnSignIn.setOnClickListener(this);
        btnCreateAccount.setOnClickListener(this);
    }


    public void logInWithEmailAndPassword(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public void initFirebase() {
        if(FirebaseHandler.getFirebaseHandler() == null) {
            mAuth = FirebaseAuth.getInstance();
            mUser = mAuth.getCurrentUser();
            mDatabase = FirebaseDatabase.getInstance();
            userReference = mDatabase.getReference().child("users");
            mHandler = new FirebaseHandler(mAuth, mUser, mDatabase, mDatabase.getReference());
            FirebaseHandler.setFirebaseHandler(mHandler);
        }
        else{
            mHandler = FirebaseHandler.getFirebaseHandler();
            mAuth = mHandler.getAuth();
            mUser = mHandler.getUser();
            mDatabase = mHandler.getDatabase();
            userReference = mHandler.getReference();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i == R.id.logIn) {
            logInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString());
        } else if (i == R.id.createAccount) {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        }
    }
}