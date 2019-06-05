package com.volunteam.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.volunteam.R;
import com.volunteam.components.User;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    public FirebaseAuth mAuth;
    public FirebaseDatabase mDatabase;
    public DatabaseReference userDatabase;

    public EditText etFirstName, etLastName, etEmail, etPassword, etRepeatPassword;
    public Button btnSignUp, btnHaveAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initFirebase();

        etFirstName = (EditText)findViewById(R.id.etFirstName);
        etLastName = (EditText)findViewById(R.id.etLastName);
        etEmail = (EditText)findViewById(R.id.etEmailRegister);
        etPassword = (EditText)findViewById(R.id.etPasswordRegister);
        etRepeatPassword = (EditText)findViewById(R.id.etRepeatPasswordRegister);
        btnSignUp = (Button)findViewById(R.id.register);
        btnHaveAccount = (Button)findViewById(R.id.alreadyAcc);

        btnSignUp.setOnClickListener(this);
        btnHaveAccount.setOnClickListener(this);
    }


    public void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        userDatabase = mDatabase.getReference();
    }

    public void signUpWithEmailAndPassword(String email, String password) {
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter password",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(etFirstName.getText().toString())){
            Toast.makeText(this, "Please enter your First Name",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(etLastName.getText().toString())){
            Toast.makeText(this, "Please enter your Second Name",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(etRepeatPassword.getText().toString())){
            Toast.makeText(this, "Please repeat your password",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!etPassword.getText().toString().equals(etRepeatPassword.getText().toString())){
            Toast.makeText(this, "Passwords don't match",Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            storeUserInDatabase();
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void storeUserInDatabase() {
        final User user = new User(etFirstName.getText().toString(), etLastName.getText().toString(), etEmail.getText().toString(), null, null);

        userDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(user).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.register) {
            signUpWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString());
        } else if(i == R.id.alreadyAcc){
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        }
    }
}
