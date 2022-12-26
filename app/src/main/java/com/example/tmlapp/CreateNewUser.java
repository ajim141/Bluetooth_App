package com.example.tmlapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CreateNewUser extends AppCompatActivity {

    ImageView profileImage;
    EditText name,phone, dob, password,confirmpassword;
    Button back, save, view;
    DBHelper db;

    final static int PICK_IMAGE = 1;
    Uri imageUri;
    OutputStream outputStream;
    static Bitmap bitmap = null;

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
        profileImage = findViewById(R.id.createNewUserImage);

        // database helper
        db = new DBHelper(this);

        clickListeners();

    }

    private void clickListeners() {

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                gallery.putExtra("crop","true");
                gallery.putExtra("noFaceDetection","true");

                startActivityForResult(Intent.createChooser(gallery,"Select Picture"),PICK_IMAGE);
            }
        });


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
                saveToGallery();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE && resultCode == RESULT_OK){
            imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
//            bitmap.compress("jpg",100,outputStream);
            profileImage.setImageBitmap(bitmap);
            Log.d("string", bitmap.getHeight() +" "+bitmap.getWidth());

        }
    }

    private void saveToGallery(){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) profileImage.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        FileOutputStream outputStream = null;
        File file = Environment.getExternalStorageDirectory();
        File dir = new File(file.getAbsolutePath() + "/MyPics");
        dir.mkdirs();

        String filename = String.format("%d.png",System.currentTimeMillis());
        File outFile = new File(dir,filename);
        try {
            outFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            outputStream = new FileOutputStream(outFile);
        }catch (Exception e){
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        try{
            outputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            outputStream.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}