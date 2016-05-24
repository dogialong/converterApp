package com.example.longdg.converterapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Longdg on 24/05/2016.
 */
public class ConversionDBHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DB_NAME = " ConversionDB";
    private static final String TABLE_NAME ="conversions";

    private static final String CREATE_CONVERSIONS_TABLE =
            "create table " + TABLE_NAME + " (" + "_id integer not null primary key, " + "units text not null, " + "factor real not null)";

    public ConversionDBHelper(Context context) {
        super(context,DB_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CONVERSIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        this.onCreate(db);
    }
}
