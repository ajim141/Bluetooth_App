package com.example.tmlapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Admin_Trips_Data extends AppCompatActivity {

    Button back, overall;
    EditText distance, afe, time, fuel_consumed, trip_number, username;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_trips_data);

        // db helper
        db = new DBHelper(this);

        Intent intent = getIntent();

        String usernameFromIntent = intent.getStringExtra("username");
        String tripnumberFromIntent = intent.getStringExtra("tripnumber");

        // buttons
        back =findViewById(R.id.admin_trip_data_back);
        overall =findViewById(R.id.admin_trip_data_overall);

        // texts
        distance =findViewById(R.id.admin_trip_distance);
        afe =findViewById(R.id.admin_trip_afe);
        time =findViewById(R.id.admin_trip_time);
        fuel_consumed =findViewById(R.id.admin_trip_fuel_consumed);
        trip_number =findViewById(R.id.admin_trip_trip_number);
        username =findViewById(R.id.admin_trip_user);

        // setting the user and trip number
        trip_number.setText(tripnumberFromIntent);
        username.setText(usernameFromIntent);

        // getting data from trips table
        Cursor res = db.getuserlastfivetripdata(usernameFromIntent);

        if(res.getCount()<=0){
            Toast.makeText(Admin_Trips_Data.this,"data not present",Toast.LENGTH_SHORT).show();
        }else{

            if(res.getCount() >= Integer.parseInt(tripnumberFromIntent)) {
                // cursor moving to last to show last trips data to driver
                res.moveToPosition(Integer.parseInt(tripnumberFromIntent) - 1);
                // filling the data into edit text blocks
                distance.setText(res.getString(2));
                afe.setText(res.getString(3));
                time.setText(res.getString(4));
                fuel_consumed.setText(res.getString(5));

            }else{
                Toast.makeText(Admin_Trips_Data.this,"driver has done only "+res.getCount()+" trips",Toast.LENGTH_SHORT).show();
            }
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        overall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}