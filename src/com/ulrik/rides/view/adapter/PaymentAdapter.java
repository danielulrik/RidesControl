package com.ulrik.rides.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.ulrik.rides.R;
import com.ulrik.rides.database.RideDAO;
import com.ulrik.rides.model.Payment;


import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel Ulrik
 * Date: 01/04/2015
 * Time: 10:32
 */
public class PaymentAdapter extends ArrayAdapter<Payment> {

    private Context context;
    private int resource;
    private AdapterView.OnItemClickListener onItemClickListener;

    public PaymentAdapter(Context context, int textViewResourceId, List<Payment> payments) {
        super(context, textViewResourceId, payments);
        this.context = context;
        this.resource = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        TextView textViewSentToServer = (TextView) convertView.findViewById(R.id.textView_sent_to_server);

        TextView textViewPaymentId = (TextView) convertView.findViewById(R.id.textViewPaymentId);
        TextView textViewRide1Id = (TextView) convertView.findViewById(R.id.textViewRide1);
        TextView textViewRide2Id = (TextView) convertView.findViewById(R.id.textViewRide2);
        TextView textViewRide3Id = (TextView) convertView.findViewById(R.id.textViewRide3);

        TextView textViewDateRide1 = (TextView) convertView.findViewById(R.id.textViewDateRide1);
        TextView textViewDateRide2 = (TextView) convertView.findViewById(R.id.textViewDateRide2);
        TextView textViewDateRide3 = (TextView) convertView.findViewById(R.id.textViewDateRide3);

        Payment payment = getItem(position);

        textViewSentToServer.setText(payment.isSentWs() ? "Yes" : "No");

        if (textViewSentToServer.getText().toString().equals("Yes")) {
            textViewSentToServer.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        } else {
            textViewSentToServer.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        }

        textViewPaymentId.setText("" + payment.getIdPayment());
        textViewRide1Id.setText("" + payment.getIdRide1());
        textViewRide2Id.setText("" + payment.getIdRide2());
        textViewRide3Id.setText("" + payment.getIdRide3());

        if (RideDAO.getInstance().findAllRides().size() > 0) {
            if (payment.getIdRide1() > 0) {
                textViewDateRide1.setText(RideDAO.getInstance().findAllRides().get(payment.getIdRide1() - 1).getDate());
            } else {
                textViewRide1Id.setText("");
                textViewDateRide1.setText("");
            }
            if (payment.getIdRide2() > 0) {
                textViewDateRide2.setText(RideDAO.getInstance().findAllRides().get(payment.getIdRide2() - 1).getDate());
            } else {
                textViewRide2Id.setText("");
                textViewDateRide2.setText("");
            }
            if (payment.getIdRide3() > 0) {
                textViewDateRide3.setText(RideDAO.getInstance().findAllRides().get(payment.getIdRide3() - 1).getDate());
            } else {
                textViewRide3Id.setText("");
                textViewDateRide3.setText("");
            }
        } else {
            textViewRide1Id.setText("");
            textViewDateRide1.setText("");
            textViewRide2Id.setText("");
            textViewDateRide2.setText("");
            textViewRide3Id.setText("");
            textViewDateRide3.setText("");
        }

        ((ListView) parent).setOnItemClickListener(onItemClickListener);

        return convertView;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
