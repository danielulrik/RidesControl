package com.ulrik.rides.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.ulrik.rides.R;
import com.ulrik.rides.model.Ride;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Leonardo Neves
 * Date: 27/03/2015
 * Time: 15:09
 */
public class RideAdapter extends ArrayAdapter<Ride> {

    private Context context;
    private int resource;

    public RideAdapter(Context context, int resource, List<Ride> rides) {
        super(context, resource, rides);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        TextView idRide = (TextView) convertView.findViewById(R.id.textView_id_ride);
        TextView dateRide = (TextView) convertView.findViewById(R.id.textView_dataCarona);
        TextView paid = (TextView) convertView.findViewById(R.id.textView_paid);
        TextView received = (TextView) convertView.findViewById(R.id.textView_received);

        Ride ride = getItem(position);
        idRide.setText(String.valueOf(ride.getId()));

        if (ride.getDate() != null && !ride.getDate().isEmpty()) {
            dateRide.setText(ride.getDate() + ", " + getDay(ride.getDate()));
        } else {
            dateRide.setText("");
        }

        paid.setText(ride.isIsPaid() ? context.getString(R.string.text_sim) : context.getString(R.string.text_nao));
        received.setText(ride.isSentWs() ? context.getString(R.string.text_sim) : context.getString(R.string.text_nao));

        if (ride.isIsPaid()) {
            paid.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        } else {
            paid.setTextColor(context.getResources().getColor(android.R.color.holo_red_light));
        }

        if (ride.isSentWs()) {
            received.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        } else {
            received.setTextColor(context.getResources().getColor(android.R.color.holo_red_light));
        }

        return convertView;
    }

    public String getDay(String formattedDate) {
        int ano = Calendar.getInstance().get(Calendar.YEAR) - 1900;

        Date date = new Date(ano, Integer.parseInt(formattedDate.split("-")[1]) - 1, Integer.parseInt(formattedDate.split("-")[0]));
        int day = date.getDay();
        return getDayOfTheWeek(day);
    }

    public String getDayOfTheWeek(int day) {
        switch (day) {
            case 0:
                return "Sunday";
            case 1:
                return "Monday";
            case 2:
                return "Tuesday";
            case 3:
                return "Wednesday";
            case 4:
                return "Thursday";
            case 5:
                return "Friday";
            case 6:
                return "Saturday";
        }
        return "";
    }
}
