package com.ulrik.rides.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.ulrik.rides.R;
import com.ulrik.rides.database.RideDAO;
import com.ulrik.rides.model.Payment;
import com.ulrik.rides.model.Ride;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Leonardo Neves
 * Date: 07/04/2015
 * Time: 10:17
 */
public class PaymentExpandableListActivity extends Activity implements ExpandableListView.OnChildClickListener {

    private static final String ID_FIRST_KEY = "ID_FIRST_KEY";
    private static final String DATE_SECOND_KEY = "DATE_SECOND_KEY";
    private boolean inEditionMode = false;

    private Payment payment;
    private List<Ride> rides;

    private TextView textViewDataRide1, textViewDataRide2, textViewDataRide3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rides_payment_expandable);

        payment = new Payment();
        ((TextView) findViewById(R.id.new_payment_id)).setText((RideDAO.getInstance().findAllPayments().size() + 1) + getString(R.string.text_payment));

        textViewDataRide1 = (TextView) findViewById(R.id.text_view_data_ride_1);
        textViewDataRide2 = (TextView) findViewById(R.id.text_view_data_ride_2);
        textViewDataRide3 = (TextView) findViewById(R.id.text_view_data_ride_3);

        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expandable_payment);

        if (getIntent().getExtras() != null) {
            inEditionMode = true;
            ((TextView) findViewById(R.id.new_payment_id)).setText(getString(R.string.payment_number) + getIntent().getIntExtra("idPayment", 0));

            if (getIntent().getIntExtra("idRide1", 0) == 0) {
                textViewDataRide1.setText("");
            } else {
                textViewDataRide1.setText("id: " + getIntent().getIntExtra("idRide1", 0) + " - date: " + getIntent().getStringExtra("dateRide1"));
            }

            if (getIntent().getIntExtra("idRide2", 0) == 0) {
                textViewDataRide2.setText("");
            } else {
                textViewDataRide2.setText("id: " + getIntent().getIntExtra("idRide2", 0) + " - date: " + getIntent().getStringExtra("dateRide2"));
            }

            if (getIntent().getIntExtra("idRide", 0) == 0) {
                textViewDataRide3.setText("");
            } else {
                textViewDataRide3.setText("id: " + getIntent().getIntExtra("idRide3", 0) + " - date: " + getIntent().getStringExtra("dateRide3"));
            }

        }

        rides = RideDAO.getInstance().findAllRides();
        List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
        List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
        for (int i = 0; i < 3; i++) {
            Map<String, String> curGroupMap = new HashMap<String, String>();
            groupData.add(curGroupMap);
            curGroupMap.put(ID_FIRST_KEY, "Ride " + (i + 1));

            List<Map<String, String>> children = new ArrayList<Map<String, String>>();
            for (Ride ride : rides) {
                    Map<String, String> curChildMap = new HashMap<String, String>();
                    children.add(curChildMap);
                    curChildMap.put(ID_FIRST_KEY, "Id ride: " + ride.getId());
                    curChildMap.put(DATE_SECOND_KEY, "Data: " + ride.getDate());
            }
            childData.add(children);
        }


        ExpandableListAdapter mAdapter = new SimpleExpandableListAdapter(
                this,
                groupData,
                android.R.layout.simple_expandable_list_item_1,
                new String[]{ID_FIRST_KEY, DATE_SECOND_KEY},
                new int[]{android.R.id.text1, android.R.id.text2},
                childData,
                android.R.layout.simple_expandable_list_item_2,
                new String[]{ID_FIRST_KEY, DATE_SECOND_KEY},
                new int[]{android.R.id.text1, android.R.id.text2}
        );
        expandableListView.setAdapter(mAdapter);
        expandableListView.setOnChildClickListener(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rides_menu_add_payment, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_payment) {
            if (!inEditionMode) {
                RideDAO.getInstance().insertPayment(payment);
            } else {
                if (getIntent().getIntExtra("positionPayment", -1) >= 0) {
                    int idRide1 = payment.getIdRide1(), idRide2 = payment.getIdRide2(), idRide3 = payment.getIdRide3();
                    payment = RideDAO.getInstance().findAllPayments().get(getIntent().getIntExtra("positionPayment", -1));
                    payment.setIdRide1(idRide1 > 0 ? idRide1 : payment.getIdRide1());
                    payment.setIdRide2(idRide2 > 0 ? idRide2 : payment.getIdRide2());
                    payment.setIdRide3(idRide3 > 0 ? idRide3 : payment.getIdRide3());
                }
                RideDAO.getInstance().updatePayment(payment);
            }
            for (Ride ride : RideDAO.getInstance().findAllRides()) {
                if (ride.getId() == payment.getIdRide1() || ride.getId() == payment.getIdRide2() || ride.getId() == payment.getIdRide3()) {
                    ride.setIsPaid(true);
                    RideDAO.getInstance().updateRide(ride);
                }
            }
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        int selectedRideId = rides.get(childPosition).getId();
        if (groupPosition == 0) {
            addRideToPayment(1, selectedRideId);
        }
        if (groupPosition == 1) {
            addRideToPayment(2, selectedRideId);
        }
        if (groupPosition == 2) {
            addRideToPayment(3, selectedRideId);
        }
        return true;
    }

    private void addRideToPayment(int whichPaymentRideId, int idAddedRide) {
        Ride ride = rides.get(idAddedRide - 1);
        boolean wasPaid = false;
        if (whichPaymentRideId == 1) {
            if (payment.getIdRide2() != idAddedRide && payment.getIdRide3() != idAddedRide) {
                wasPaid = false;
                for (Payment p : RideDAO.getInstance().findAllPayments()) {
                    if (p.getIdRide2() == idAddedRide || p.getIdRide3() == idAddedRide) {
                        wasPaid = true;
                        break;
                    }
                }
                if (!wasPaid) {
                    textViewDataRide1.setText("id: " + ride.getId() + " - date: " + ride.getDate());
                    payment.setIdRide1(idAddedRide);
                }
            }
        } else if (whichPaymentRideId == 2) {
            if (payment.getIdRide1() != idAddedRide && payment.getIdRide3() != idAddedRide) {
                wasPaid = false;
                for (Payment p : RideDAO.getInstance().findAllPayments()) {
                    if (p.getIdRide1() == idAddedRide || p.getIdRide3() == idAddedRide) {
                        wasPaid = true;
                        break;
                    }
                }
                if (!wasPaid) {
                    textViewDataRide2.setText("id: " + ride.getId() + " - date: " + ride.getDate());
                    payment.setIdRide2(idAddedRide);
                }
            }
        } else if (whichPaymentRideId == 3) {
            if (payment.getIdRide1() != idAddedRide && payment.getIdRide2() != idAddedRide) {
                wasPaid = false;
                for (Payment p : RideDAO.getInstance().findAllPayments()) {
                    if (p.getIdRide1() == idAddedRide || p.getIdRide2() == idAddedRide) {
                        wasPaid = true;
                        break;
                    }
                }
                if (!wasPaid) {
                    textViewDataRide3.setText("id: " + ride.getId() + " - date: " + ride.getDate());
                    payment.setIdRide3(idAddedRide);
                }
            }
        }
        if (wasPaid) {
            Toast toast = Toast.makeText(getBaseContext(), "The ride was already added in one payment.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
}