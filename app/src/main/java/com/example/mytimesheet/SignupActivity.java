package com.example.mytimesheet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editEmail;
    EditText editPassword;
    Button signUp;
    FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        FirebaseApp.initializeApp(SignupActivity.this);
        mAuth = FirebaseAuth.getInstance();
        editPassword = (EditText) findViewById(R.id.regPassword);
        editEmail = (EditText) findViewById(R.id.regEmail);
        signUp = (Button) findViewById(R.id.signupButton);
        signUp.setOnClickListener(this);
        findViewById(R.id.signInOption).setOnClickListener(this);
    }


    private void registerUser(){

        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if ( email.isEmpty() ){
            editEmail.setError("Email is required");
            editEmail.requestFocus();
            return;
        }

        if ( !Patterns.EMAIL_ADDRESS.matcher(email).matches() ){
            editEmail.setError("Please enter a valid email");
            editEmail.requestFocus();
            return;
        }

        if ( password.isEmpty() ){
            editEmail.setError("Password is required");
            editPassword.requestFocus();
            return;
        }

        if ( password.length() < 6 ){
            editPassword.setError("Password must be at least 6 character long");
            editPassword.requestFocus();
            return;
        }




        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if ( task.isSuccessful() ){
                    Toast.makeText(getApplicationContext(),"User registerd successfully",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(getApplicationContext(),"That email address is already used, please sign in",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void hideKeyboard(){
        View v = getCurrentFocus();
        if ( v != null ){
            ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.signupButton:
                hideKeyboard();
                registerUser();
                startActivity(new Intent(SignupActivity.this,MainActivity.class));
                break;

            case R.id.signInOption:
                startActivity(new Intent(SignupActivity.this,MainActivity.class));
                break;
        }
    }
}
