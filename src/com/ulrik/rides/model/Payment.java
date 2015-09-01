package com.ulrik.rides.model;

/**
 * Created with IntelliJ IDEA.
 * User: Leonardo Neves
 * Date: 27/03/2015
 * Time: 17:28
 */
public class Payment {

    private int idPayment;
    private int idRide1;
    private int idRide2;
    private int idRide3;
    private boolean sentWs = false;

    public Payment() {
    }

    public Payment(int idRide1, int idRide2, int idRide3, boolean sentWs) {
        this.idRide1 = idRide1;
        this.idRide2 = idRide2;
        this.idRide3 = idRide3;
        this.sentWs = sentWs;
    }

    public int getIdPayment() {
        return idPayment;
    }

    public void setIdPayment(int idPayment) {
        this.idPayment = idPayment;
    }

    public int getIdRide1() {
        return idRide1;
    }

    public void setIdRide1(int idRide1) {
        this.idRide1 = idRide1;
    }

    public int getIdRide2() {
        return idRide2;
    }

    public void setIdRide2(int idRide2) {
        this.idRide2 = idRide2;
    }

    public int getIdRide3() {
        return idRide3;
    }

    public void setIdRide3(int idRide3) {
        this.idRide3 = idRide3;
    }

    public boolean isSentWs() {
        return sentWs;
    }

    public void setSentWs(boolean sentWs) {
        this.sentWs = sentWs;
    }
}
