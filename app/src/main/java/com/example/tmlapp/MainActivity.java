package com.example.tmlapp;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

    Button login_button, create_new_user, loginasadmin, buttonOn, buttonOff, showbutton, searchbutton;
    EditText loginid, loginpassword;
    ListView btlist;
    BluetoothAdapter BTadapter;
    // DBHelper for database
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // buttons
        login_button = findViewById(R.id.loginButton);
        create_new_user = findViewById(R.id.createNewUser);
        loginasadmin = findViewById(R.id.loginAdminButton);
        buttonOn = findViewById(R.id.btonbutton);
        buttonOff = findViewById(R.id.btoffbutton);
        showbutton = findViewById(R.id.showbutton);
        searchbutton = findViewById(R.id.searchbutton);

        //listview
        btlist =  findViewById(R.id.btlist);

        // edit text
        loginid = findViewById(R.id.login_logid);
        loginpassword = findViewById(R.id.login_password);

        //check if BT is supported
        BTadapter = BluetoothAdapter.getDefaultAdapter();

        // if BT is not supported show toast
        if (BTadapter == null) {
            //bluetooth not supported in device
            Toast.makeText(MainActivity.this, "bluetooth not supported", Toast.LENGTH_SHORT).show();
        }

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

        // button to turn on bluetooth
        buttonOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // enable if not already enabled
                if (!BTadapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    Log.e("string", "onClick: before ");

                    int requestCodeForEnabling = 0;

                    // start activity for pop up window of turning on bluetooth
                    startActivityForResult(enableBtIntent, requestCodeForEnabling);
                    Log.e("string", "onClick: after ");
                }
            }
        });

        // button to turn off bluetooth
        buttonOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BTadapter.isEnabled()){
                    BTadapter.disable();
                }
            }
        });

        // show already paired devices
        showbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<BluetoothDevice> btdevice = BTadapter.getBondedDevices();
                String[] string = new String[btdevice.size()];
                int index = 0;

                if(btdevice.size()>0){
                    for(BluetoothDevice device : btdevice){
                        string[index] = device.getName();
                        index++;
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,string);
                    btlist.setAdapter(arrayAdapter);
                }
            }
        });

        // search for nearby devices
        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // to discover the new devices
                Log.e("string", "before ");

                BTadapter.startDiscovery();

                Log.e("string", "after");

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

    @Override
    protected void onResume() {
        super.onResume();

//        BTadapter.startDiscovery();
        ArrayList<String> stringArrayList = new ArrayList<String>();
        ArrayAdapter<String> arrayAdaptersearch =  new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,stringArrayList);

        BroadcastReceiver myreceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if(BluetoothDevice.ACTION_FOUND.equals(action)){
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                    Log.i("bluetooth device",device.getName());
//                    stringArrayList.add(device.getName());
                    arrayAdaptersearch.notifyDataSetChanged();
                }
                if(stringArrayList.size() != 0){
                    ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,stringArrayList);
                    btlist.setAdapter(itemAdapter);
                }
            }
        };

        // intent to register the device
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(myreceiver,intentFilter);

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
}