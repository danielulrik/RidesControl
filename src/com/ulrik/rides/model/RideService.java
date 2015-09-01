package com.ulrik.rides.model;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel Ulrik
 * Date: 02/04/2015
 * Time: 09:23
 */
public class RideService {

    private Integer id;
    private String date;
    private boolean isPaid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }
}
