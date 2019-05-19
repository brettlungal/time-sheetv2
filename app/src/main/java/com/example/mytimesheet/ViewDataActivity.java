package com.example.mytimesheet;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewDataActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth mAuth;
    ListView listViewWork;
    DatabaseReference dbase;
    List<LoggedWork> workedList;
    String databasePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);

        listViewWork = (ListView)findViewById(R.id.listOfWork);
        findViewById(R.id.returnToProfile).setOnClickListener(this);
        workedList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        databasePath = user.getEmail().substring(0,user.getEmail().indexOf("@"));
        dbase = FirebaseDatabase.getInstance().getReference(databasePath);
        listViewWork.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                LoggedWork log = workedList.get(position);
                String date = log.getDate();
                double hours = log.getHours();
                String workId = log.getId();
                showUpdateDialog(workId,date,hours);
                return true;
            }
        });

    }

    private void showUpdateDialog(final String workId, final String date, double hours){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_dialog,null);
        dialogBuilder.setView(dialogView);

        final EditText editTextHours = (EditText)dialogView.findViewById(R.id.editHours);
        final Button buttonUpdate = (Button)dialogView.findViewById(R.id.updateButton);
        final Button buttonDelete = (Button)dialogView.findViewById(R.id.deleteButton);

        dialogBuilder.setTitle("Updating "+date+" work");

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newHours = editTextHours.getText().toString().trim();
                if (TextUtils.isEmpty(newHours)){
                    editTextHours.setError("Hours Required");
                }else {
                    double parsedHours = Double.parseDouble(newHours);
                    updateData(workId,date,parsedHours);
                    alertDialog.dismiss();
                }

            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeData(workId);
                alertDialog.dismiss();
            }
        });




    }

    protected void updateData(String id,String date, double newHours){
        DatabaseReference updateDate = FirebaseDatabase.getInstance().getReference(databasePath).child(id); //got work to be updated
        LoggedWork log = new LoggedWork(id,date,newHours);
        updateDate.setValue(log);
        Toast.makeText(this,"Hours successfully updated",Toast.LENGTH_LONG).show();
    }

    protected void removeData(final String tag){
        DatabaseReference removeDate = FirebaseDatabase.getInstance().getReference(databasePath).child(tag);
        removeDate.removeValue();
        Toast.makeText(this,"Hours successfully deleted",Toast.LENGTH_LONG).show();
    }



    @Override
    protected void onStart() {
        super.onStart();
        dbase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                workedList.clear();

                for( DataSnapshot workSnapshot : dataSnapshot.getChildren() ){
                    LoggedWork log = workSnapshot.getValue(LoggedWork.class);
                    workedList.add(log);
                }

                WorkList adapter = new WorkList(ViewDataActivity.this,workedList);
                listViewWork.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.returnToProfile:
                startActivity(new Intent(ViewDataActivity.this , ProfileActivity.class));

                break;


        }
    }
}
