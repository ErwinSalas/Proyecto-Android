package com.example.pavilion.proyectomoviles.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Bryan on 6/4/2017.
 */

public class BDHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME= "MiBD.db";
    public static final String TABLE_USERS_NAME = "users";
    public static final String COLUMN_USERS_NAME = "name";
    public static final String COLUMN_USERS_LASTNAME = "lastName";
    public static final String COLUMN_USERS_EMAIL = "email";
    public static final String COLUMN_USERS_PASSWORD = "password";

    /*public static final String COLUMN_USERS_PROPERTY_ID = "propertyId";

    public static final String TABLE_PROPERTY_NAME = "property";
    public static final String COLUMN_PROPERTY_NAME = "name";
    public static final String COLUMN_PROPERTY_LOCATION = "location";
    public static final String COLUMN_PROPERTY_ID = "propertyId";

    public static final String TABLE_LOT_NAME = "lot";
    public static final String COLUMN_LOT_ID = "id";
    public static final String COLUMN_LOT_HORIZONTAL_SIZE = "horirzontal";
    public static final String COLUMN_LOT_VERTICAL_SIZE = "vertical";
    public static final String COLUMN_LOT_TYPE_OF_CROP = "vertical";*/

    public BDHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_USERS_NAME + "(" + COLUMN_USERS_EMAIL +
                " text primary key," + COLUMN_USERS_NAME +
                " text, " + COLUMN_USERS_LASTNAME +
                " text, " + COLUMN_USERS_PASSWORD + " text)");

        /*db.execSQL("create table " + TABLE_PROPERTY_NAME + "(" + COLUMN_PROPERTY_ID +
                " text primary key," + COLUMN_PROPERTY_NAME +
                " text, " + COLUMN_PROPERTY_LOCATION + " text)");

        db.execSQL("create table " + TABLE_LOT_NAME + "(" + COLUMN_LOT_ID +
                " text primary key," + COLUMN_LOT_HORIZONTAL_SIZE +
                " text, " + COLUMN_LOT_VERTICAL_SIZE + " text)" +
                " text, " + COLUMN_LOT_TYPE_OF_CROP + " text)");*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_USERS_NAME);
       /* db.execSQL("drop table if exists " + TABLE_PROPERTY_NAME);
        db.execSQL("drop table if exists " + TABLE_LOT_NAME);*/
        onCreate(db);

    }


    public void onUpgraded() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("drop table if exists " + TABLE_USERS_NAME);
       /* db.execSQL("drop table if exists " + TABLE_PROPERTY_NAME);
        db.execSQL("drop table if exists " + TABLE_LOT_NAME);*/
        onCreate(db);

    }

    public boolean insertUser(String email, String name, String lastName, String password){


        SQLiteDatabase bd = this.getWritableDatabase();
        ContentValues register = new ContentValues();
        register.put(COLUMN_USERS_EMAIL, email);
        register.put(COLUMN_USERS_NAME, name);
        register.put(COLUMN_USERS_LASTNAME, lastName);
        register.put(COLUMN_USERS_PASSWORD, password);
        bd.insert(TABLE_USERS_NAME, null, register);
        bd.close();
        return true;
    }

    /*public boolean insertProperty(String id, String name, String location){
        SQLiteDatabase bd = this.getWritableDatabase();
        ContentValues register = new ContentValues();
        register.put(COLUMN_PROPERTY_ID, id);
        register.put(COLUMN_PROPERTY_NAME, name);
        register.put(COLUMN_PROPERTY_LOCATION, location);
        bd.insert(TABLE_PROPERTY_NAME, null, register);
        bd.close();
        return true;
    }

    public boolean insertLot(String id, String horizontal, String vertical, String crop){
        SQLiteDatabase bd = this.getWritableDatabase();
        ContentValues register = new ContentValues();
        register.put(COLUMN_LOT_ID, id);
        register.put(COLUMN_LOT_HORIZONTAL_SIZE, horizontal);
        register.put(COLUMN_LOT_VERTICAL_SIZE, vertical);
        register.put(COLUMN_LOT_TYPE_OF_CROP, crop);
        bd.insert(TABLE_LOT_NAME, null, register);
        bd.close();
        return true;
    }*/


    public Integer deleteUser(String email){
        SQLiteDatabase bd = this.getWritableDatabase();
        return bd.delete(TABLE_USERS_NAME, COLUMN_USERS_EMAIL+"='" + email+"'", null);
    }

    /*public Integer deleteProperty(String propertyId){
        SQLiteDatabase bd = this.getWritableDatabase();
        return bd.delete(TABLE_PROPERTY_NAME, COLUMN_PROPERTY_ID+"='" + propertyId+"'", null);
    }

    public Integer deleteLot(String LotId){
        SQLiteDatabase bd = this.getWritableDatabase();
        return bd.delete(TABLE_LOT_NAME, COLUMN_LOT_ID+"='" + LotId+"'", null);
    }*/


    public Cursor getUserData(String email, String password){
        SQLiteDatabase bd = this.getReadableDatabase();
        Cursor fila = bd.rawQuery(
                "select * from "+TABLE_USERS_NAME+" where "+COLUMN_USERS_EMAIL+"='" + email+"' and "
                        +COLUMN_USERS_PASSWORD+"='" + password+"'", null);
        return fila;
    }

    /*public Cursor getPropertyData(String id){
        SQLiteDatabase bd = this.getReadableDatabase();
        Cursor fila = bd.rawQuery(
                "select * from "+TABLE_PROPERTY_NAME+" where "+COLUMN_PROPERTY_ID+"='" + id+"'", null);
        return fila;
    }

    public Cursor getLotData(String id){
        SQLiteDatabase bd = this.getReadableDatabase();
        Cursor fila = bd.rawQuery(
                "select * from "+TABLE_LOT_NAME+" where "+COLUMN_LOT_ID+"='" + id+"'", null);
        return fila;
    }*/
}
