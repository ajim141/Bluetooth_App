package com.example.tmlapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context)  {
        super(context, "TML_App_database.sqlite", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table TML_Trips(ID integer primary key AUTOINCREMENT not null, name TEXT, distance integer, afe integer, time integer, fuel integer)");
        db.execSQL("create table TML_database(name TEXT primary key, contact integer, dob integer,password integer)");
        Log.e("database", "onCreate: database created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists TML_database");
        db.execSQL("drop Table if exists TML_Trips");
        onCreate(db);
    }

    public Boolean insertuserdata(String name, String contact, String dob, String password){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("contact", contact);
        contentValues.put("dob", dob);
        contentValues.put("password", password);

        long result = DB.insert("TML_database",null,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Boolean inserttripdata(String name, Integer distance, Integer afe, Integer time, Integer fuel){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("distance", distance);
        contentValues.put("afe", afe);
        contentValues.put("time", time);
        contentValues.put("fuel", fuel);

        long result = DB.insert("TML_Trips",null,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Boolean updateuserdata(String name, String contact, String dob, String password) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("contact", contact);
        contentValues.put("dob", dob);
        contentValues.put("password", password);

        Cursor cursor = DB.rawQuery("Select * from TML_database where name=?", new String[]{name});

        if (cursor.getCount() > 0) {

            long result = DB.update("TML_database", contentValues, "name=?", new String[]{name});
            if (result == -1)
                return false;
            else
                return true;
        } else {
            return false;
        }
    }

    public Boolean deleteuserdata(String name) {
        SQLiteDatabase DB = this.getWritableDatabase();

        Cursor cursor = DB.rawQuery("Select * from TML_database where name=?", new String[]{name});

        if (cursor.getCount() > 0) {

            long result = DB.delete("TML_database", "name=?", new String[]{name});
            if (result == -1)
                return false;
            else
                return true;
        } else {
            return false;
        }
    }

    public Cursor getalluserdata() {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from TML_database", null);
        return cursor;
    }

    public Cursor getuserlogindata() {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from TML_database Limit 1", null);
        return cursor;
    }

    public Cursor gettripdata() {
        SQLiteDatabase DB = this.getWritableDatabase();

        Cursor cursor = DB.rawQuery("Select * from TML_trips", null);

        return cursor;
    }

    public Cursor getusertripdata(String name) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM TML_trips  WHERE name = '" + name + "'", null);
//      Cursor cursor = DB.rawQuery("Select * from TML_trips", null);
        return cursor;
    }

    public Cursor getuserlastfivetripdata(String name) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM TML_trips WHERE name = '" + name + "' Order by ID DESC LIMIT 5", null);
//        SELECT * FROM Employee ORDER BY ID DESC LIMIT 5
        //      Cursor cursor = DB.rawQuery("Select * from TML_trips", null);
        return cursor;
    }


}
