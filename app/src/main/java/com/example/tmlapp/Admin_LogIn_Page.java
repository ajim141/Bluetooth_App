package com.example.tmlapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Arrays;

public class Admin_LogIn_Page extends AppCompatActivity {

    Button user1, user2, user3, deleteuser, adduser;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_log_in_page);

        user1 = findViewById(R.id.admin_log_in_page_user1);
        user2 = findViewById(R.id.admin_log_in_page_user2);
        user3 = findViewById(R.id.admin_log_in_page_user3);
        deleteuser = findViewById(R.id.admin_log_in_page_delete_user);
        adduser = findViewById(R.id.admin_log_in_page_back);

        // dbhelper
        db = new DBHelper(this);

        // string array for storing username and initialize them with NA
        String[] usernames = new String[3];
        Arrays.fill(usernames,"NA");

        int userno=0;
        int Numberofdata=0;
        Cursor res = db.getalluserdata();
        res.moveToNext();

        while(res.moveToNext()){
            if(res.getString(0)!=null) {
                usernames[userno] = res.getString(0);
            }
            userno++;
        }

        user1.setText("user1 -"+ usernames[0]);
        user2.setText("user2 -"+ usernames[1]);
        user3.setText("user3 -"+ usernames[2]);

        user1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_LogIn_Page.this, admin_trips.class);
                intent.putExtra("username",usernames[0]);
                startActivity(intent);
            }
        });

        user2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_LogIn_Page.this, admin_trips.class);
                intent.putExtra("username",usernames[1]);
                startActivity(intent);
            }
        });

        user3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_LogIn_Page.this, admin_trips.class);
                intent.putExtra("username",usernames[2]);
                startActivity(intent);
            }
        });

        deleteuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // delete user method
            }
        });

        adduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add user method

            }
        });

    }
}