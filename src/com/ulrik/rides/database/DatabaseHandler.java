package com.ulrik.rides.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created with IntelliJ IDEA.
 * User: Leonardo Neves
 * Date: 31/03/2015
 * Time: 15:12
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "db_versa_communication";
    private static final int DB_VERSION = 1;

    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table ride(_id integer primary key autoincrement, date text not null, isPaid INTEGER, sentWs INTEGER);");
        db.execSQL("create table payment(_id integer primary key autoincrement, idRide1 INTEGER, idRide2 INTEGER, idRide3 INTEGER, sentWs INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table ride;");
        db.execSQL("drop table payment;");
        onCreate(db);
    }
}
