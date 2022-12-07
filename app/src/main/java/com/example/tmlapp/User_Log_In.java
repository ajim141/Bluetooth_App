package com.example.tmlapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class User_Log_In extends AppCompatActivity {

    Button trips, back;
    EditText welcomename, name, dob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_log_in);

        // edit text
        welcomename = findViewById(R.id.user_welcome_name);
        name = findViewById(R.id.user_name);
        dob = findViewById(R.id.user_dob);

        // buttons
        trips = findViewById(R.id.user_log_in_trips);
        back = findViewById(R.id.user_log_in_back);

        // getting intent data(user name) from main activity
        Intent intent = getIntent();
        String strname = intent.getStringExtra("name");
        String strdob = intent.getStringExtra("dob");

        // filling text in edit text area
        welcomename.setText(strname);
        name.setText(strname);
        dob.setText(strdob);

        trips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User_Log_In.this, User_Trip_Data.class);
                intent.putExtra("name",strname);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             finish();            }
        });
    }
}