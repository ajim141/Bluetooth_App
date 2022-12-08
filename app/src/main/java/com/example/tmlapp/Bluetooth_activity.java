package com.example.tmlapp;

import androidx.annotation.Nullable;
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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class Bluetooth_activity extends AppCompatActivity {



    Button buttonOn, buttonOff, showbutton, searchbutton;
    ListView btlist, available_list;
    BluetoothAdapter BTadapter;
    ArrayAdapter<String> arrayAdapter, arraysearchadapter;
//    BroadcastReceiver btbroadcastlisner;


    public static final int BT_REQ_CODE = 1;


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
        setContentView(R.layout.activity_bluetooth);

        buttonOn = findViewById(R.id.btonbutton);
        buttonOff = findViewById(R.id.btoffbutton);
        showbutton = findViewById(R.id.showbutton);
        searchbutton = findViewById(R.id.searchbutton);

        //listview
        btlist = findViewById(R.id.btlist);
        available_list = findViewById(R.id.btlistsearch);

        //check if BT is supported
        BTadapter = BluetoothAdapter.getDefaultAdapter();

        // if BT is not supported show toast
        if (BTadapter == null) {
            //bluetooth not supported in device
            Toast.makeText(Bluetooth_activity.this, "bluetooth not supported", Toast.LENGTH_SHORT).show();
        }

        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(btbroadcastlisner,intentFilter);

        IntentFilter intentFilter1 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(btbroadcastlisner,intentFilter1);

        buttonOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BTadapter.isEnabled()) {
                  BTadapter.enable();
//                    Intent enable_bt = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                    startActivityForResult(enable_bt, BT_REQ_CODE);
                }

                if(BTadapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
                    Intent discoverable = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    discoverable.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                    startActivity(discoverable);
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
                    arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,string);
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
//                arrayAdapter.clear();
                checkPermissions();
               Boolean val = BTadapter.startDiscovery();
                Toast.makeText(Bluetooth_activity.this, "val" + val, Toast.LENGTH_SHORT).show();

                Log.e("string", "after");

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Toast.makeText(Bluetooth_activity.this, "bluetooth ON", Toast.LENGTH_SHORT).show();
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(Bluetooth_activity.this, "bluetooth cancelled", Toast.LENGTH_SHORT).show();
        }
    }
    BroadcastReceiver btbroadcastlisner = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Toast.makeText(Bluetooth_activity.this, "action" + action, Toast.LENGTH_SHORT).show();

            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device.getBondState() != BluetoothDevice.BOND_BONDED){
                    arraysearchadapter.add(device.getName() +"\n" + device.getAddress());
                    Toast.makeText(Bluetooth_activity.this, "dev " + device.getName(), Toast.LENGTH_SHORT).show();

                    btlist.setAdapter(arraysearchadapter);
                    }
            }else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                if(arraysearchadapter.getCount() == 0){
                    Toast.makeText(Bluetooth_activity.this, "No device found", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Bluetooth_activity.this, "click device to connect", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

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