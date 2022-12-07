package com.example.tmlapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class admin_trips extends AppCompatActivity {

    Button trip1, trip2, trip3, trip4, trip5, overall, back;
    EditText title;
    Intent intent;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_trips);

        intent = getIntent();
        String username = intent.getStringExtra("username");

        db = new DBHelper(this);

        trip1 =findViewById(R.id.admin_trips_trip1);
        trip2 =findViewById(R.id.admin_trips_trip2);
        trip3 =findViewById(R.id.admin_trips_trip3);
        trip4 =findViewById(R.id.admin_trips_trip4);
        trip5 =findViewById(R.id.admin_trips_trip5);
        overall =findViewById(R.id.admin_trips_overall);
        back = findViewById(R.id.admin_trips_back);

        title =findViewById(R.id.admin_trip_title);

        title.setText("Admin Login For User "+username);

        Cursor res = db.getuserlastfivetripdata(username);
        if(res.getCount()<=0){
            Toast.makeText(admin_trips.this,"number of entries "+res.getCount(),Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Toast.makeText(admin_trips.this,"number of entries "+res.getCount(),Toast.LENGTH_SHORT).show();
        }

        trip1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(admin_trips.this, Admin_Trips_Data.class);
                intent.putExtra("tripnumber","1");
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        trip2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(admin_trips.this, Admin_Trips_Data.class);
                intent.putExtra("tripnumber","2");
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        trip3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(admin_trips.this, Admin_Trips_Data.class);
                intent.putExtra("tripnumber","3");
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        trip4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(admin_trips.this, Admin_Trips_Data.class);
                intent.putExtra("tripnumber","4");
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        trip5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(admin_trips.this, Admin_Trips_Data.class);
                intent.putExtra("tripnumber","5");
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        overall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(admin_trips.this, admin_overall_driver_rating.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             finish();
            }
        });
    }
}