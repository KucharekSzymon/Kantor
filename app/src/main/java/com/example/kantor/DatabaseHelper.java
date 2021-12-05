package com.example.kantor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String db_name = "Kantor.db";
    public static final String table_name = "waluty";


    public DatabaseHelper(Context context) {
        super(context, db_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    boolean addWaluta(String name,String kupno, String sprzedarz){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues ct = new ContentValues();
        ct.put("Nazwa",name);
        ct.put("kupno",kupno);
        ct.put("sprzedaz",sprzedarz);
        long resault = db.insert(table_name,null,ct);
        if (resault == -1)
            return false;
        else
            return true;
    }
    boolean ifAdmin(String login, String pwd){
        SQLiteDatabase db =  this.getWritableDatabase();
        String[] columns = {"Username"};

        String selection = "Username=? and Password=?";
        String[] selectionArgs = {login, pwd};

        Cursor cursor = db.query("Users", columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();

        cursor.close();
        close();
        if (count > 0)
            return true;
        else
            return false;
    }
    Float exchange(String walA, String walB){
        if(walA.equals(walB))
            return Float.parseFloat("-99");

        if(walA == "PLN"){
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor a = db.rawQuery("select kupno from waluty where Nazwa='" + walB + "'", null);
            if (a.moveToFirst()) {
                return Float.parseFloat(a.getString(0));
            } else
                return Float.parseFloat("0");
        }

        else{
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor a = db.rawQuery("select sprzedaz from waluty where Nazwa='" + walA + "'", null);
            if (a.moveToFirst()) {
                return Float.parseFloat(a.getString(0));
            } else
                return Float.parseFloat("0");
        }

    }

    public void zmien(String temp,String nazwa,String kupno, String sprzedaz){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Nazwa",nazwa);
        cv.put("kupno",kupno);
        cv.put("sprzedaz",sprzedaz);
        String x [] = {temp};
        db.update("waluty",cv,"Nazwa=?",x);
    }

    public void usun(String nazwa){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("waluty","Nazwa='"+nazwa+"'",null);
    }
    public Cursor getWaluty(){
        SQLiteDatabase db =  this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from waluty",null);
        return res;
    }
    public List<String> getAllLabels(){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM waluty";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        labels.add("PLN");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }
}
