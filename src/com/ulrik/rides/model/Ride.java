package com.ulrik.rides.model;

/**
 * Created with IntelliJ IDEA.
 * User: Leonardo Neves
 * Date: 27/03/2015
 * Time: 15:09
 */
public class Ride {

    private int idRide;
    private String date;
    private boolean isPaid = false;

    private boolean sentWs = false;

    public Ride() {
    }

    public Ride(int idRide, String date, boolean isPaid, boolean sentWs) {
        this.idRide = idRide;
        this.date = date;
        this.isPaid = isPaid;
        this.sentWs = sentWs;
    }

    public int getId() {
        return idRide;
    }

    public void setId(int idRide) {
        this.idRide = idRide;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isIsPaid() {
        return isPaid;
    }

    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public boolean isSentWs() {
        return sentWs;
    }

    public void setSentWs(boolean sentWs) {
        this.sentWs = sentWs;
    }
}
