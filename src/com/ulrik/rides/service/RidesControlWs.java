package com.ulrik.rides.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ulrik.rides.database.RideDAO;
import com.ulrik.rides.model.Payment;
import com.ulrik.rides.model.Ride;
import com.ulrik.rides.model.RideService;
import com.ulrik.rides.view.MyActivity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel Ulrik
 * Date: 10/04/2015
 * Time: 10:04
 */
public class RidesControlWs {

    private static final String URL_BASE = "http://192.168.0.35:8080/VersaCommTeste/ws/";

    private static final TypeToken TYPE_TOKEN_RIDE = new TypeToken<List<RideService>>() {
    };
    private static final TypeToken TYPE_TOKEN_PAYMENT = new TypeToken<List<Payment>>() {
    };

    private Context context;
    private ProgressDialog progressDialog;

    public RidesControlWs(Context context, ProgressDialog progressDialog) {
        this.context = context;
        this.progressDialog = progressDialog;
    }

    private void receiveFromServer(String resourcePath, final String errorMessage) {
        Entity entity = null;
        if (resourcePath.contains("ride")) {
            RideDAO.getInstance().clearRides();
            entity = Entity.RIDE;
        } else if (resourcePath.contains("payment")) {
            RideDAO.getInstance().clearPayments();
            entity = Entity.PAYMENT;
        }

        final Entity finalEntity = entity;
        HttpClientRest.doGet(URL_BASE + resourcePath, progressDialog, new HttpClientRest.OnFinish() {
            @Override
            public void onGetResponse(InputStream response) {
                ParserJson parserJson = new ParserJson();

                if (finalEntity.equals(Entity.RIDE)) {
                    List<RideService> rideServices = (List<RideService>) parserJson.descerializar(TYPE_TOKEN_RIDE.getType(), response);
                    for (RideService rideService : rideServices) {
                        Ride ride = new Ride(rideService.getId(), rideService.getDate(), rideService.isPaid(), true);
                        RideDAO.getInstance().insertRide(ride);
                    }
                } else if (finalEntity.equals(Entity.PAYMENT)) {
                    List<Payment> payments = (List<Payment>) parserJson.descerializar(TYPE_TOKEN_PAYMENT.getType(), response);
                    if (payments.size() > 0) {
                        for (Payment payment : payments) {
                            RideDAO.getInstance().insertPayment(payment);
                        }
                        updateRides();
                        ((MyActivity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((MyActivity) context).notifyAdapters();
                            }
                        });
                    }
                }

                ((MyActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MyActivity) context).notifyAdapters();
                    }
                });
            }

            @Override
            public void onGetError(Exception erro) {
                ((MyActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(context, errorMessage, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                });
                erro.getMessage();
            }
        });


    }

    private void sendToWs(String resourcePath, final String errorMessage) {
        Gson gson = new Gson();
        Entity entity = null;
        if (resourcePath.contains("ride")) {
            entity = Entity.RIDE;
        } else if (resourcePath.contains("payment")) {
            entity = Entity.PAYMENT;
        }

        String json = "";
        if (entity.equals(Entity.RIDE)) {
            ArrayList<RideService> listRides = new ArrayList<RideService>();
            for (Ride ride : RideDAO.getInstance().findAllRides()) {
                RideService rideService = new RideService();
                rideService.setId(ride.getId());
                rideService.setDate(ride.getDate());
                rideService.setPaid(ride.isIsPaid());
                listRides.add(rideService);
            }
            json = gson.toJson(listRides);
        } else {
            json = gson.toJson(RideDAO.getInstance().findAllPayments());
        }

        final Entity finalEntity = entity;
        HttpClientRest.doPut(URL_BASE + resourcePath, progressDialog, new HttpClientRest.OnFinish() {
            @Override
            public void onGetResponse(InputStream response) {
                if (finalEntity.equals(Entity.RIDE)) {
                    for (Ride ride : RideDAO.getInstance().findAllRides()) {
                        ride.setSentWs(true);
                        RideDAO.getInstance().updateRide(ride);
                    }
                } else if (finalEntity.equals(Entity.PAYMENT)) {
                    for (Payment payment : RideDAO.getInstance().findAllPayments()) {
                        payment.setSentWs(true);
                        RideDAO.getInstance().updatePayment(payment);
                    }
                }

                ((MyActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MyActivity) context).notifyAdapters();
                    }
                });
            }

            @Override
            public void onGetError(Exception erro) {
                ((MyActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(context, errorMessage, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                });
                erro.printStackTrace();
            }
        }, json);

    }

    public void receiveRidesFromServer() {
        receiveFromServer("ride/getall", "An error occurred, the rides weren't received from the server.");
    }

    public void receivePaymentsFromServer() {
        receiveFromServer("payment/getall", "An error occurred, the payments weren't received from the server.");
    }

    public void sendPaymentsToWs() {
        sendToWs("payment/addall", "An error occurred, the payments weren't sent to the server");
    }

    public void sendRidesToWs() {
        sendToWs("ride/addall", "An error occurred, the rides weren't sent to the server");
    }

//    public void sendRidesToWs() {
//        Gson gson = new Gson();
//        ArrayList<RideService> listRides = new ArrayList<>();
//        for (Ride ride : RideDAO.getInstance().findAllRides()) {
//            RideService rideService = new RideService();
//            rideService.setId(ride.getId());
//            rideService.setDate(ride.getDate());
//            rideService.setPaid(ride.isIsPaid());
//            listRides.add(rideService);
//        }
//
//        String json = gson.toJson(listRides);
//        HttpClientRest.doPut(URL_BASE + "ride/addall", progressDialog, new HttpClientRest.OnFinish() {
//            @Override
//            public void onGetResponse(InputStream response) {
//                for (Ride ride : RideDAO.getInstance().findAllRides()) {
//                    ride.setSentWs(true);
//                    RideDAO.getInstance().updateRide(ride);
//                }
//
//                ((MyActivity) context).runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        ((MyActivity) context).notifyAdapters();
//                    }
//                });
//            }
//
//            @Override
//            public void onGetError(Exception erro) {
//                ((MyActivity) context).runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast toast = Toast.makeText(context, "An error occurred, the rides weren't sent to the server.", Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
//                    }
//                });
//                erro.printStackTrace();
//            }
//        }, json);
//    }

    private void updatePayments() {

    }

    private void updateRides() {
        List<Ride> rides = RideDAO.getInstance().findAllRides();
        List<Payment> payments = RideDAO.getInstance().findAllPayments();

        for (Payment payment : payments) {
            for (Ride ride : rides) {
                if (payment.getIdRide1() == ride.getId() || payment.getIdRide2() == ride.getId() || payment.getIdRide3() == ride.getId()) {
                    ride.setIsPaid(true);
                    RideDAO.getInstance().updateRide(ride);
                }
            }
        }
    }

    enum Entity {
        RIDE, PAYMENT
    }

}
