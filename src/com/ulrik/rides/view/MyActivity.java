package com.ulrik.rides.view;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import com.ulrik.rides.R;
import com.ulrik.rides.database.RideDAO;
import com.ulrik.rides.model.Ride;
import com.ulrik.rides.view.adapter.PaymentAdapter;
import com.ulrik.rides.view.adapter.RideAdapter;
import com.ulrik.rides.view.fragment.FragmentPayment;
import com.ulrik.rides.view.fragment.FragmentRide;
import com.ulrik.rides.view.fragment.NavegationFragment;

import java.util.List;

public class MyActivity extends FragmentActivity {

    private static final int MY_NOTIFICATION_ID = 1;
    private FragmentRide fragmentRide;
    private FragmentPayment fragmentPayment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rides_rides_fragment_activity);

        RideDAO.getInstance().setContexDatabase(this);

        fragmentRide = new FragmentRide();
        fragmentPayment = new FragmentPayment();

        NavegationFragment navegationFragment = new NavegationFragment();
        navegationFragment.addTitles("Ride", "Payment");
        navegationFragment.addFragments(fragmentRide, fragmentPayment);
        navegationFragment.makeTabs(getSupportFragmentManager(), (ViewPager) findViewById(R.id.viewPager), getActionBar());

    }

    public void notifyAdapters() {
        fragmentRide.getListViewRide().setAdapter(new RideAdapter(this, R.layout.rides_ride_adapter, RideDAO.getInstance().findAllRides()));
        fragmentPayment.getListViewPayment().setAdapter(new PaymentAdapter(this, R.layout.rides_payment_adapter, RideDAO.getInstance().findAllPayments()));
        fragmentRide.updateViewRidesUnpaid();
        fragmentPayment.updateViewPaymentsMade();
    }

    private void testeNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setAutoCancel(true);

//        Intent resultIntent = new Intent(this, ResultActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addParentStack(ResultActivity.class);
//        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MY_NOTIFICATION_ID, mBuilder.build());
    }

    public void receiveAllInformationsFromServer() {

        new AlertDialog.Builder(this).setTitle("Attention").setMessage("You must send your new information before synchronization.").setNegativeButton("Ok", null);
    }
}
