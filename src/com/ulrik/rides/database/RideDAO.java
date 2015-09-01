package com.ulrik.rides.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.ulrik.rides.model.Payment;
import com.ulrik.rides.model.Ride;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Leonardo Neves
 * Date: 31/03/2015
 * Time: 15:13
 */
public class RideDAO {

    private static RideDAO instance;
    private SQLiteDatabase db;

    private RideDAO() {
    }

    public static RideDAO getInstance() {
        if (instance == null) {
            instance = new RideDAO();
        }
        return instance;
    }

    public void setContexDatabase(Context context) {
        DatabaseHandler auxDb = new DatabaseHandler(context);
        instance.db = auxDb.getWritableDatabase();
    }

    public void insertRide(Ride ride) {
        ContentValues values = new ContentValues();
        values.put("date", ride.getDate());
        values.put("isPaid", ride.isIsPaid());
        values.put("sentWs", ride.isSentWs());
        db.insert("ride", null, values);
    }

    public void insertPayment(Payment payment) {
        ContentValues values = new ContentValues();
        values.put("idRide1", payment.getIdRide1());
        values.put("idRide2", payment.getIdRide2());
        values.put("idRide3", payment.getIdRide3());
        values.put("sentWs", payment.isSentWs());
        db.insert("payment", null, values);
    }

    public void updateRide(Ride ride) {
        ContentValues values = new ContentValues();
        values.put("isPaid", ride.isIsPaid());
        values.put("sentWs", ride.isSentWs());
        db.update("ride", values, "_id = ?", new String[]{String.valueOf("" + ride.getId())});
    }

    public void updatePayment(Payment payment) {
        ContentValues values = new ContentValues();
        values.put("idRide1", payment.getIdRide1());
        values.put("idRide2", payment.getIdRide2());
        values.put("idRide3", payment.getIdRide3());
        values.put("sentWs", payment.isSentWs());
        db.update("payment", values, "_id = ?", new String[]{String.valueOf("" + payment.getIdPayment())});
    }

    public ArrayList<Ride> findAllRides() {
        ArrayList<Ride> rides = new ArrayList<Ride>();
        String[] columms = new String[]{"_id", "date", "isPaid", "sentWs"};
        Cursor cursor = db.query("ride", columms, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Ride ride = new Ride();
                ride.setId(cursor.getInt(0));
                ride.setDate(cursor.getString(1));
                ride.setIsPaid(cursor.getInt(2) == 1);
                ride.setSentWs(cursor.getInt(3) == 1);
                rides.add(ride);
            } while (cursor.moveToNext());
        }
        return rides;
    }

    public List<Payment> findAllPayments() {
        List<Payment> payments = new LinkedList<Payment>();
        String[] columms = new String[]{"_id", "idRide1", "idRide2", "idRide3", "sentWs"};
        Cursor cursor = db.query("payment", columms, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Payment payment = new Payment();
                payment.setIdPayment(cursor.getInt(0));
                payment.setIdRide1(cursor.getInt(1));
                payment.setIdRide2(cursor.getInt(2));
                payment.setIdRide3(cursor.getInt(3));
                payment.setSentWs(cursor.getInt(4) == 1);
                payments.add(payment);
            } while (cursor.moveToNext());
        }
        return payments;
    }

    public void deleteRide(Ride ride) {
        String selection = "_id " + " = ?";
        String[] selectionArgs = {String.valueOf(ride.getId())};
        db.delete("ride", selection, selectionArgs);
    }


    public void clearRides() {
        db.execSQL("drop table ride;");
        db.execSQL("create table ride(_id integer primary key autoincrement, date text not null, isPaid INTEGER, sentWs INTEGER);");
    }

    public void clearPayments() {
        db.execSQL("drop table payment;");
        db.execSQL("create table payment(_id integer primary key autoincrement, idRide1 INTEGER, idRide2 INTEGER, idRide3 INTEGER, sentWs INTEGER);");
    }

    public void clearAll() {
        clearRides();
        clearPayments();
    }
}
