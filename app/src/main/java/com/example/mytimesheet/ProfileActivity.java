package com.example.mytimesheet;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    TextView hello,date , theLog;
    EditText hours;
    Button logHours;
    DatabaseReference theDatabase;
    DatePickerDialog.OnDateSetListener dateSelector;


    int selectedDay,selectedMonth,selectedYear = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.logout).setOnClickListener(this);
        findViewById(R.id.logButton).setOnClickListener(this);
        hello = (TextView) findViewById(R.id.welcome);
        theLog = (TextView)findViewById(R.id.log);

        SpannableString content = new SpannableString("Log hours");
        content.setSpan(new UnderlineSpan(), 0 , content.length() , 0);
        theLog.setText(content);

        FirebaseUser user = mAuth.getCurrentUser();
        String databasePath = user.getEmail().substring(0,user.getEmail().indexOf("@"));
        theDatabase = FirebaseDatabase.getInstance().getReference(databasePath);



        hello.setText("Welcome, "+databasePath+"!");


        findViewById(R.id.selectDate).setOnClickListener(this);
        date = (TextView)findViewById(R.id.selectDate);
        date.setOnClickListener(this);
        hours = (EditText)findViewById(R.id.hoursWorked);
        logHours = (Button)findViewById(R.id.logButton);

        findViewById(R.id.viewHours).setOnClickListener(this);

        dateSelector = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                selectedDay = dayOfMonth;
                selectedMonth = month+1;
                selectedYear = year;
                date.setText((month+1)+"/"+dayOfMonth+"/"+year);
            }
        };

    }

    private void hideKeyboard(){
        View v = getCurrentFocus();
        if ( v != null ){
            ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public String intTOMonth(int month){
        String returnMonth = "";

        switch(month){
            case 1:
                returnMonth = "Jan";
                break;
            case 2:
                returnMonth = "Feb";
                break;
            case 3:
                returnMonth = "Mar";
                break;
            case 4:
                returnMonth = "Apr";
                break;
            case 5:
                returnMonth = "May";
                break;
            case 6:
                returnMonth = "Jun";
                break;
            case 7:
                returnMonth = "Jul";
                break;
            case 8:
                returnMonth = "Aug";
                break;
            case 9:
                returnMonth = "Sep";
                break;
            case 10:
                returnMonth = "Oct";
                break;
            case 11:
                returnMonth = "Nov";
                break;
            case 12:
                returnMonth = "Dec";
                break;
            case -1:
                break;
        }

        return returnMonth;
    }

    private void addHours(){
        String stringHours = hours.getText().toString().trim();
        String augmentedMonth = intTOMonth(selectedMonth);
        String currDate = augmentedMonth+"/"+selectedDay+"/"+selectedYear;
        double finalHours = Double.parseDouble(stringHours);
        if ( !TextUtils.isEmpty(stringHours) &&  ( (selectedDay!=-1) && (selectedMonth!=-1) && (selectedYear!=-1) )){
            //add it
            String id = theDatabase.push().getKey(); //here if i want to add data under the same child, get the key of an exisiting push
            LoggedWork log = new LoggedWork(id,currDate,finalHours);
            theDatabase.child(id).setValue(log);
            Toast.makeText(getApplicationContext(),"Hours saved",Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(getApplicationContext(),"Please enter valid data to save",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout:
                mAuth.signOut();
                finish();
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                break;
            case R.id.logButton:
                hideKeyboard();
                addHours();
                hours.setText("");
                date.setText("Tap To Select Date");
                break;

            case R.id.selectDate:
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(ProfileActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,dateSelector,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                break;
            case R.id.viewHours:
                startActivity(new Intent(ProfileActivity.this, ViewDataActivity.class));

                break;
        }
    }
}
