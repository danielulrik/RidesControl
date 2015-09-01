package com.ulrik.rides.view.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.*;
import com.ulrik.rides.R;
import com.ulrik.rides.database.RideDAO;
import com.ulrik.rides.model.Ride;
import com.ulrik.rides.service.RidesControlWs;
import com.ulrik.rides.view.MyActivity;
import com.ulrik.rides.view.adapter.RideAdapter;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Leonardo Neves
 * Date: 30/03/2015
 * Time: 14:57
 */
public class FragmentRide extends Fragment implements View.OnClickListener, ListView.OnItemLongClickListener {

    private ListView listView;
    private Date mDate;
    private TextView textViewRidesNotPaid;
    private RideAdapter rideAdapter;
    private int mYear;
    private int mMonth;
    private int mDay;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rides_rides_fragment_ride, null);
        setHasOptionsMenu(true);

        mDate = new Date();
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        textViewRidesNotPaid = (TextView) view.findViewById(R.id.textView_count_rides_no_paid);
        listView = (ListView) view.findViewById(R.id.listView_rides);
        rideAdapter = new RideAdapter(getActivity(), R.layout.rides_ride_adapter, RideDAO.getInstance().findAllRides());
        listView.setAdapter(rideAdapter);
        listView.setOnItemLongClickListener(this);

        view.findViewById(R.id.button_requestData).setOnClickListener(this);
        view.findViewById(R.id.button_sendData).setOnClickListener(this);

        updateViewRidesUnpaid();

        return view;
    }

    @Override
    public void onResume() {
        rideAdapter = new RideAdapter(getActivity(), R.layout.rides_ride_adapter, RideDAO.getInstance().findAllRides());
        listView.setAdapter(rideAdapter);
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.rides_menu_ride, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_ride:
                final NewRide newRide = new NewRide(getActivity());
                newRide.init(new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;
                        mDate = new Date(mYear, mMonth, mDay);
                    }
                });

                newRide.rideRecord(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Ride mRide = new Ride();
                        mDate = new Date(newRide.getDatePicker().getYear(), newRide.getDatePicker().getMonth(), newRide.getDatePicker().getDayOfMonth());
                        String formatDate = new SimpleDateFormat(getActivity().getString(R.string.format_date)).format(mDate);
                        mRide.setDate(formatDate);
                        addRide(mRide);
                        updateViewRidesUnpaid();
                        listView.setAdapter(new RideAdapter(getActivity(), R.layout.rides_ride_adapter, RideDAO.getInstance().findAllRides()));
                        dialog.dismiss();
                    }
                });

                newRide.show();
                return true;
            case R.id.delete_all:
                RideDAO.getInstance().clearAll();
                ((MyActivity) getActivity()).notifyAdapters();
                textViewRidesNotPaid.setText(String.valueOf(RideDAO.getInstance().findAllRides().size()));
                return true;
            case R.id.add_ride_today:
                Ride ride = new Ride();
                String formattedDate = new SimpleDateFormat(getActivity().getString(R.string.format_date)).format(new Date());
                ride.setDate(formattedDate);
                addRide(ride);
                updateViewRidesUnpaid();
                listView.setAdapter(new RideAdapter(getActivity(), R.layout.rides_ride_adapter, RideDAO.getInstance().findAllRides()));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateViewRidesUnpaid() {
        int c = 0;
        for (Ride ride : RideDAO.getInstance().findAllRides()) {
            if (!ride.isIsPaid()) {
                c++;
            }
        }
        textViewRidesNotPaid.setText(String.valueOf(c));
    }

    private void addRide(Ride mRide) {
        boolean rideOfTheDayAlreadyAdded = false;
        for (Ride ride : RideDAO.getInstance().findAllRides()) {
            if (ride.getDate().equals(mRide.getDate())) {
                rideOfTheDayAlreadyAdded = true;
            }
        }
        if (!rideOfTheDayAlreadyAdded) {
            RideDAO.getInstance().insertRide(mRide);
        } else {
            Toast.makeText(getActivity(), "Ride of this day(" + new SimpleDateFormat(getActivity().getString(R.string.format_date)).format(mDate) + ") is already added.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        RidesControlWs ridesControlWs = new RidesControlWs(getActivity(), progressDialog);
        progressDialog.setTitle(getString(R.string.text_wait));

        switch (v.getId()) {
            case R.id.button_requestData:
                ridesControlWs.receiveRidesFromServer();
                break;

            case R.id.button_sendData:
                ridesControlWs.sendRidesToWs();
                break;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final Ride ride = (Ride) listView.getAdapter().getItem(position);

        if (ride.isSentWs()) {
            Toast.makeText(getActivity(), getString(R.string.msg_received_ride_server), Toast.LENGTH_LONG).show();

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getString(R.string.text_attention));
            builder.setIcon(android.R.drawable.stat_sys_warning);
            builder.setMessage(String.format(getString(R.string.msg_remove_ride), ride.getDate()));
            builder.setNegativeButton(getString(R.string.text_nao), null);
            builder.setPositiveButton(getString(R.string.text_sim), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    RideDAO.getInstance().deleteRide(ride);
                    String oldDate = ride.getDate();
                    listView.setAdapter(new RideAdapter(getActivity(), R.layout.rides_ride_adapter, RideDAO.getInstance().findAllRides()));
                    dialog.dismiss();

                    updateViewRidesUnpaid();

                    Toast.makeText(getActivity(), String.format(getString(R.string.msg_removed_ride_success), oldDate), Toast.LENGTH_LONG).show();
                }
            });
            builder.show();
        }
        return false;
    }

    public ListView getListViewRide() {
        return listView;
    }

    private class NewRide extends AlertDialog {
        private DatePicker datePicker;

        protected NewRide(Context context) {
            super(context);
            View view = LayoutInflater.from(context).inflate(R.layout.rides_new_ride, null);
            datePicker = (DatePicker) view.findViewById(R.id.datePicker);

            setView(view);
            setTitle("New ride");
        }

        public void init(DatePicker.OnDateChangedListener listener) {
            datePicker.init(mYear, mMonth, mDay, listener);
        }

        public void rideRecord(OnClickListener listener) {
            setButton("Ok", listener);
        }

        public DatePicker getDatePicker() {
            return datePicker;
        }
    }

}
