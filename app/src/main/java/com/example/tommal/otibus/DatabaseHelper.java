package com.example.tommal.otibus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tommal.otibus.JSOUP.Configuration;
import com.example.tommal.otibus.JSOUP.SubitoSingleObject;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "otibus.db";

    public static final String TABLE_NAME = "items_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "LINK";
    public static final String COL_3 = "TIMESTAMPINSERIMENTO";
    public static final String COL_4 = "PREZZO";
    public static final String COL_5 = "TITOLO";

    public static final String TABLE_NAMEN_NEW = "itemsNew_table";
    public static final String COL_N1 = "ID";
    public static final String COL_N2 = "LINK";
    public static final String COL_N3 = "TIMESTAMPINSERIMENTO";
    public static final String COL_N4 = "PREZZO";
    public static final String COL_N5 = "TITOLO";

    public static final String TABLE_NAME_PERSONALIZZAZIONE = "personalizzazione_table";
    public static final String COL_P1 = "ID";
    public static final String COL_P2 = "COSA";
    public static final String COL_P3 = "PREZZO_MAX";




    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null , 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table  items_table (ID INTEGER PRIMARY KEY AUTOINCREMENT,LINK TEXT,TIMESTAMPINSERIMENTO TEXT, PREZZO TEXT,TITOLO TEXT)");
        db.execSQL("create table  itemsNew_table (ID INTEGER PRIMARY KEY AUTOINCREMENT,LINK TEXT,TIMESTAMPINSERIMENTO TEXT, PREZZO TEXT,TITOLO TEXT)");
        db.execSQL("create table  personalizzazione_table (ID INTEGER PRIMARY KEY AUTOINCREMENT,COSA TEXT, PREZZO_MAX TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAMEN_NEW);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME_PERSONALIZZAZIONE);
        onCreate(db);
    }

public void deleteFromTABLE_NAME(){
        SQLiteDatabase db = this.getWritableDatabase();
         db.delete(TABLE_NAME,"",new String[]{});

}

    public void deleteFromTABLE_NAME_NEW(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAMEN_NEW,"",new String[]{});
    }


    public boolean insertDataItems(SubitoSingleObject su) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,su.getUrl());
        contentValues.put(COL_3,su.getTimeStamp());
        contentValues.put(COL_4,su.getPrezzo());
        contentValues.put(COL_5,su.getTitle());
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }
    public boolean insertDataItemsNew(SubitoSingleObject su) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_N2,su.getUrl());
        contentValues.put(COL_N3,su.getTimeStamp());
        contentValues.put(COL_N4,su.getPrezzo());
        contentValues.put(COL_N5,su.getTitle());
        long result = db.insert(TABLE_NAMEN_NEW,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }
    public boolean insertDataConfigurations(Configuration co) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_P2,co.getCosa());
        contentValues.put(COL_P3,co.getPrezzoMax());
        long result = db.insert(TABLE_NAME_PERSONALIZZAZIONE,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }
    public Cursor getAllData(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("Select * from " + TABLE_NAME, null);
    }
    public Cursor getAllDataNew(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("Select * from " + TABLE_NAMEN_NEW, null);
    }

    public List<SubitoSingleObject> getSelectedItems(){
        List<SubitoSingleObject> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + TABLE_NAME, null);
        while (cursor.moveToNext()){
            list.add(new SubitoSingleObject(cursor.getString(4),cursor.getString(1),cursor.getFloat(3), cursor.getString(2)));
        }
       return list;
    }
    public List<SubitoSingleObject> getSelectedItemsNew(){
        List<SubitoSingleObject> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + TABLE_NAMEN_NEW, null);
        while (cursor.moveToNext()){
            list.add(new SubitoSingleObject(cursor.getString(4),cursor.getString(1),cursor.getFloat(3), cursor.getString(2)));
        }
        return list;
    }


    public List<Configuration> getSelectedConfigurations(){
        List<Configuration> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + TABLE_NAME_PERSONALIZZAZIONE, null);
        while (cursor.moveToNext()){
            list.add(new Configuration(cursor.getString(1),cursor.getString(2)));
        }
        return list;
    }

}