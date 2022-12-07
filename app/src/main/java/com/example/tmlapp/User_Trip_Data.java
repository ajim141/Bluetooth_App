package com.example.tmlapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Array;

public class User_Trip_Data extends AppCompatActivity {

    Button back, debug_save, debug_view;
    EditText distance, afe, time, fuel_consumed;

    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_trip_data);

        // buttons
        back = findViewById(R.id.user_trip_data_back);
        debug_save = findViewById(R.id.user_trip_data_debug_save);
        debug_view = findViewById(R.id.user_trip_data_debug_view);

        // edit text
        distance = findViewById(R.id.user_trip_data_distance);
        afe = findViewById(R.id.user_trip_data_afe);
        time = findViewById(R.id.user_trip_data_time);
        fuel_consumed = findViewById(R.id.user_trip_data_fuel_consumed);

        // db helper for sql operation
        db = new DBHelper(this);

        // getting intent data from previous activity
        Intent intent = getIntent();
        String strnam = intent.getStringExtra("name");

        // getting the data from trip table
        Cursor res = db.getusertripdata(strnam);
        if(res.getCount()<=0){
            Toast.makeText(User_Trip_Data.this,"data not present",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(User_Trip_Data.this,"data present",Toast.LENGTH_SHORT).show();

            // cursor moving to last to show last trips data to driver
            res.moveToLast();

            // filling the data into edit text blocks
            distance.setText(res.getString(2));
            afe.setText(res.getString(3));
            time.setText(res.getString(4));
            fuel_consumed.setText(res.getString(5));
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        debug_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer distanceTxt = Integer.parseInt(distance.getText().toString());
                Integer afeTxt = Integer.parseInt(afe.getText().toString());
                Integer timeTxt = Integer.parseInt(time.getText().toString());
                Integer fuel_value = Integer.parseInt(fuel_consumed.getText().toString());


                    Boolean check = db.inserttripdata(strnam,distanceTxt,afeTxt,timeTxt,fuel_value);
                    if(check == true){
                        Toast.makeText(User_Trip_Data.this,"data inserted successfully",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Log.e("database error", "onClick: failed");
                        Toast.makeText(User_Trip_Data.this,"failed to insert the data",Toast.LENGTH_SHORT).show();
                    }
            }

        });


        debug_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = db.gettripdata();

                if(res.getCount() == 0){
                    Toast.makeText(User_Trip_Data.this,"database is empty",Toast.LENGTH_SHORT).show();
                    return;
                }

                StringBuffer buffer = new StringBuffer();
                while(res.moveToNext()){
                    buffer.append("name : "+ res.getString(0)+"\n");
                    buffer.append("distance : "+ res.getString(1)+"\n");
                    buffer.append("afe : "+ res.getString(2)+"\n");
                    buffer.append("time : "+ res.getString(3)+"\n");
                    buffer.append("fuel : "+ res.getString(4)+"\n");
                    buffer.append("\n");
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(User_Trip_Data.this);
                builder.setCancelable(true);
                builder.setTitle("user data");
                builder.setMessage(buffer.toString());
                builder.show();
            }

        });

    }
}