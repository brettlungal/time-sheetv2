package com.example.mytimesheet;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText userEmail;
    EditText userPass;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(MainActivity.this);
        mAuth = FirebaseAuth.getInstance();

        if ( mAuth.getCurrentUser() != null ){
            finish();
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
        }

        findViewById(R.id.signUpOption).setOnClickListener(this);
        findViewById(R.id.login).setOnClickListener(this);
        userEmail = (EditText)findViewById(R.id.loginEmail);
        userPass = (EditText)findViewById(R.id.loginPassword);


    }

    private void userLogin(){
        String email = userEmail.getText().toString().trim();
        String pass = userPass.getText().toString().trim();

        if ( email.isEmpty() ){
            userEmail.setError("Email is required");
            userEmail.requestFocus();
            return;
        }

        if ( !Patterns.EMAIL_ADDRESS.matcher(email).matches() ){
            userEmail.setError("Please enter a valid email");
            userEmail.requestFocus();
            return;
        }

        if ( pass.isEmpty() ){
            userPass.setError("Password is required");
            userPass.requestFocus();
            return;
        }

        if ( pass.length() < 6 ){
            userPass.setError("Password must be at least 6 character long");
            userPass.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if ( task.isSuccessful() ){
                    //launch new activity
                    finish();
                    startActivity(new Intent(MainActivity.this , ProfileActivity.class));
                }else{
                    Toast.makeText(getApplicationContext(),"Username or password incorrect. Try again",Toast.LENGTH_SHORT).show();
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
    public void onClick(View view){
        switch(view.getId()){
            case R.id.signUpOption:
                startActivity(new Intent(MainActivity.this,SignupActivity.class));
                break;

            case R.id.login:
                System.out.println("LOGIN TIME\n=====================");
                hideKeyboard();
                userLogin();
                break;
        }
    }
}
