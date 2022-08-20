package com.ronnelrazo.fatteningfeedingapp.localDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper  {

    private Context context;
    private static final String DATABASE_NAME = "Fattening_app";
    private static final int DATABASE_VERSION = 1;

    //tables
    public PC_FAT_MAS_USER mas_user = new PC_FAT_MAS_USER();
    public PC_FAT_MAS_AUTHORIZE mas_authorize = new PC_FAT_MAS_AUTHORIZE();

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context; // add this line to initialize your context.

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String user = "CREATE TABLE " + mas_user.PC_FAT_MAS_USER +" (" +
                  mas_user.AD_USER + " TEXT PRIMARY KEY, " +
                  mas_user.PASSWORD + " TEXT, " +
                  mas_user.ACTIVE_FLAG + " TEXT)";
        db.execSQL(user);


        String authorize = "CREATE TABLE " + mas_authorize.PC_FAT_MAS_AUTHORIZED +" (" +
                mas_authorize.AD_USER + " TEXT PRIMARY KEY, " +
                mas_authorize.ORG_CODE + " TEXT, " +
                mas_authorize.FARM_CODE + " TEXT)";
        db.execSQL(authorize);


    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + mas_user.PC_FAT_MAS_USER );
        onCreate(db);


        db.execSQL("DROP TABLE IF EXISTS " + mas_authorize.PC_FAT_MAS_AUTHORIZED );
        onCreate(db);

    }



}
