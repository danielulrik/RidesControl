package com.ulrik.rides.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.ulrik.rides.R;
import com.ulrik.rides.model.Ride;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel Ulrik
 * Date: 01/04/2015
 * Time: 10:59
 */
public class SpinnerRideAdapter extends ArrayAdapter<Ride> implements SpinnerAdapter {

    private int positionItem1 = 0, positionItem2 = 0;

    private Context context;
    private int resource;

    public SpinnerRideAdapter(Context context, int textViewResourceId, List<Ride> rides) {
        super(context, textViewResourceId, rides);
        this.context = context;
        this.resource = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, null);
        }

        TextView textViewIdRide = (TextView) convertView.findViewById(R.id.tvRideId);
        TextView textViewDateRide = (TextView) convertView.findViewById(R.id.tvRideDate);

        Ride ride = getItem(position);

        if (ride.getId() == -1) {
            convertView.findViewById(R.id.tvlabelRideId).setVisibility(View.GONE);
            convertView.findViewById(R.id.tvlabelRideDate).setVisibility(View.GONE);

            textViewIdRide.setText("Selecione um carona");
            textViewDateRide.setVisibility(View.GONE);
        } else {
            convertView.findViewById(R.id.tvlabelRideId).setVisibility(View.VISIBLE);
            convertView.findViewById(R.id.tvlabelRideDate).setVisibility(View.VISIBLE);
            textViewDateRide.setVisibility(View.VISIBLE);

            textViewIdRide.setText("" + ride.getId());
            textViewDateRide.setText(ride.getDate());
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, null);
        }

        Ride ride = getItem(position);

        TextView textViewIdRide = (TextView) convertView.findViewById(R.id.tvRideId);
        TextView textViewDateRide = (TextView) convertView.findViewById(R.id.tvRideDate);

        if (ride.getId() == -1) {
            convertView.findViewById(R.id.tvlabelRideId).setVisibility(View.GONE);
            convertView.findViewById(R.id.tvlabelRideDate).setVisibility(View.GONE);
            textViewIdRide.setText("Selecione uma carona");
            textViewDateRide.setText("");
        } else {
            convertView.findViewById(R.id.tvlabelRideId).setVisibility(View.VISIBLE);
            convertView.findViewById(R.id.tvlabelRideDate).setVisibility(View.VISIBLE);
            textViewIdRide.setText("" + ride.getId());
            textViewDateRide.setText(ride.getDate());
        }

        if (position > 0) {
            if (position == positionItem1 || position == positionItem2) {
                convertView.setVisibility(View.GONE);
            } else {
                if (!ride.isIsPaid()) {
                    convertView.setVisibility(View.VISIBLE);
                } else {
                    convertView.setVisibility(View.GONE);
                }
            }
        }

        return convertView;
    }

    public void setFirstSelectedItem(int positionItem1) {
        this.positionItem1 = positionItem1;
    }

    public void setSecondSelectedItem(int positionItem2) {
        this.positionItem2 = positionItem2;
    }

    public int getPositionItem1() {
        return positionItem1;
    }

    public int getPositionItem2() {
        return positionItem2;
    }
}
