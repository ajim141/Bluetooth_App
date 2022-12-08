package com.example.tmlapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    Button login_button, create_new_user, loginasadmin, BT_setting, buttonOn, buttonOff, showbutton, searchbutton;
    EditText loginid, loginpassword;
    // DBHelper for database
    DBHelper db;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_PRIVILEGED
    };
    private static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_PRIVILEGED
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();

        // buttons
        login_button = findViewById(R.id.loginButton);
        create_new_user = findViewById(R.id.createNewUser);
        loginasadmin = findViewById(R.id.loginAdminButton);
        BT_setting = findViewById(R.id.BtsettingButton);

        // edit text
        loginid = findViewById(R.id.login_logid);
        loginpassword = findViewById(R.id.login_password);


        // db helper
        db = new DBHelper(this);

        // getting the values from database
        Cursor res = db.getuserlogindata();
        if (res.getCount() <= 0) {
            create_new_user.setText("create admin account");
        }

        // creating new user - done
        // saving user data somewhere on external or internal storage
        // sending the data over BT
        create_new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateNewUser.class);
                startActivity(intent);

                // getting the values from database
                Cursor res = db.getuserlogindata();
                if (res.getCount() <= 0) {
                    create_new_user.setText("create admin account");
                } else {
                    create_new_user.setText("create user account");
                }
            }
        });

        BT_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Bluetooth_activity.class);
                startActivity(intent);
            }
        });


        // admin login button
         loginasadmin.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 // getting the user input
                 String loginidTxt = loginid.getText().toString();
                 String loginpasswordTxt = loginpassword.getText().toString();

                 // getting the values from database
                 Cursor res = db.getuserlogindata();
                 if(res.getCount() <= 0){
                     Toast.makeText(MainActivity.this,"create admin account",Toast.LENGTH_SHORT).show();
                     return;
                 }
                 StringBuffer buffer = new StringBuffer();
                 res.moveToFirst();

                 buffer.append("name : "+ res.getString(0)+"\n");
                 buffer.append("password : "+ res.getString(3)+"\n");

                 if (loginidTxt.equals(res.getString(0))) {
                     if (loginpasswordTxt.equals(res.getString(3))) {
                         Intent intent = new Intent(MainActivity.this, Admin_LogIn_Page.class);
                         startActivity(intent); }
                 }else{
                     Toast.makeText(MainActivity.this,"Either userid or password is wrong",Toast.LENGTH_SHORT).show();
                 } }
         });

        // login particular user comparing login id and password
        // comparing login id and password from BT or local storage
        // fetching the user data from BT or local storage
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getting the user input
                String loginidTxt = loginid.getText().toString();
                String loginpasswordTxt = loginpassword.getText().toString();

                // getting the values from database
                Cursor res = db.getalluserdata();

                if(res.getCount() == 0){
                    Toast.makeText(MainActivity.this,"database is empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(res.getCount() == 1){
                    Toast.makeText(MainActivity.this,"create user account",Toast.LENGTH_SHORT).show();
                    return;
                }

                StringBuffer buffer = new StringBuffer();
                while(res.moveToNext()){
                    buffer.append("name : "+ res.getString(0)+"\n");
                    buffer.append("dob : "+ res.getString(2)+"\n");
                    buffer.append("password : "+ res.getString(3)+"\n");

                    Log.e("string", "onClick: "+  res.getString(0));
                    Log.e("string", "onClick: "+  res.getString(3));

                    if (loginidTxt.equals(res.getString(0))) {
                        if (loginpasswordTxt.equals(res.getString(3))) {
                            Intent intent = new Intent(MainActivity.this, User_Log_In.class);
                            intent.putExtra("name",res.getString(0));
                            intent.putExtra("dob",res.getString(2));
                            startActivity(intent);
                        }
                    }else{
                        Toast.makeText(MainActivity.this,"Either userid or password is wrong",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    private void checkPermissions(){
        int permission1 = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN);
        if (permission1 != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    1
            );
        } else if (permission2 != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_LOCATION,
                    1
            );
        }
    }
}


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode==RESULT_OK){
//            Toast.makeText(MainActivity.this,"Bluetooth is ON",Toast.LENGTH_SHORT).show();
//        }else if(resultCode == RESULT_CANCELED){
//            Toast.makeText(MainActivity.this,"Bluetooth is cancelled",Toast.LENGTH_SHORT).show();
//        }
//    }
//}