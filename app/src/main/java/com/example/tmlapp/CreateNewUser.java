package com.example.tmlapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateNewUser extends AppCompatActivity {

    EditText name,phone, dob, password,confirmpassword;
    Button back, save, view;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_user);

        // text
        name = findViewById(R.id.createNewUser_name);
        phone = findViewById(R.id.createNewUser_phone);
        dob = findViewById(R.id.createNewUser_DOB);
        password = findViewById(R.id.createNewUser_Password);
        confirmpassword = findViewById(R.id.createNewUser_confirmPassword);

        // buttons
        back = findViewById(R.id.createNewUser_back);
        save = findViewById(R.id.createNewUser_save);
        view = findViewById(R.id.createNewUser_view);
        db = new DBHelper(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameTxt = name.getText().toString();
                String phoneTxt = phone.getText().toString();
                String dobTxt = dob.getText().toString();
                String passTxt = password.getText().toString();
                String confirmpassTxt = confirmpassword.getText().toString();

                if(passTxt.compareTo(confirmpassTxt) == 0){

                    Boolean check = db.insertuserdata(nameTxt,phoneTxt,dobTxt,confirmpassTxt);
                    if(check == true){
                        Toast.makeText(CreateNewUser.this,"data inserted successfully",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Log.e("database error", "onClick: failed");
                        Toast.makeText(CreateNewUser.this,"failed to insert the data",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                        Toast.makeText(CreateNewUser.this,"password not matching",Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = db.getalluserdata();

                if(res.getCount() == 0){
                    Toast.makeText(CreateNewUser.this,"database is empty",Toast.LENGTH_SHORT).show();
                    return;
                }

                    StringBuffer buffer = new StringBuffer();
                    while(res.moveToNext()){
                        buffer.append("name : "+ res.getString(0)+"\n");
                        buffer.append("phone : "+ res.getString(1)+"\n");
                        buffer.append("dob : "+ res.getString(2)+"\n");
                        buffer.append("password : "+ res.getString(3)+"\n");
                        buffer.append("\n");

                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateNewUser.this);
                    builder.setCancelable(true);
                    builder.setTitle("user data");
                    builder.setMessage(buffer.toString());
                    builder.show();
                }

        });
    }
}