package com.ulrik.rides.view.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.ulrik.rides.R;
import com.ulrik.rides.database.RideDAO;
import com.ulrik.rides.model.Payment;
import com.ulrik.rides.service.RidesControlWs;
import com.ulrik.rides.view.PaymentExpandableListActivity;
import com.ulrik.rides.view.adapter.PaymentAdapter;


import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Leonardo Neves
 * Date: 30/03/2015
 * Time: 14:58
 */
public class FragmentPayment extends Fragment {

    private PaymentAdapter paymentAdapter;
    private View view;
    private ListView listViewPayment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.rides_rides_fragment_payment, null);
        setHasOptionsMenu(true);

        view.findViewById(R.id.button_sendData).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setTitle("Loading...");
                RidesControlWs ridesControlWs = new RidesControlWs(getActivity(), progressDialog);
                ridesControlWs.sendPaymentsToWs();
            }
        });

        view.findViewById(R.id.button_synchronize).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setTitle("Loading...");
                RidesControlWs ridesControlWs = new RidesControlWs(getActivity(), progressDialog);
                ridesControlWs.receivePaymentsFromServer();
            }
        });

        listViewPayment = (ListView) view.findViewById(R.id.listView_payments);
        paymentAdapter = new PaymentAdapter(getActivity(), R.layout.rides_payment_adapter, RideDAO.getInstance().findAllPayments());
        paymentAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<Payment> payments = RideDAO.getInstance().findAllPayments();

                Intent intent = new Intent(getActivity(), PaymentExpandableListActivity.class);
                intent.putExtra("positionPayment", position);
                intent.putExtra("idPayment", payments.get(position).getIdPayment());
                intent.putExtra("idRide1", payments.get(position).getIdRide1());
                intent.putExtra("idRide2", payments.get(position).getIdRide2());
                intent.putExtra("idRide3", payments.get(position).getIdRide3());

                if (payments.get(position).getIdRide1() > 0) {
                    intent.putExtra("dateRide1", RideDAO.getInstance().findAllRides().get(payments.get(position).getIdRide1() - 1).getDate());
                }
                if (payments.get(position).getIdRide2() > 0) {
                    intent.putExtra("dateRide2", RideDAO.getInstance().findAllRides().get(payments.get(position).getIdRide2() - 1).getDate());
                }
                if (payments.get(position).getIdRide3() > 0) {
                    intent.putExtra("dateRide3", RideDAO.getInstance().findAllRides().get(payments.get(position).getIdRide3() - 1).getDate());
                }

                getActivity().startActivity(intent);
            }
        });
        listViewPayment.setAdapter(paymentAdapter);
        ((TextView) view.findViewById(R.id.text_view_count_number_of_payments)).setText(RideDAO.getInstance().findAllPayments().size() + "");
        return view;
    }

    @Override
    public void onResume() {
        ((TextView) view.findViewById(R.id.text_view_count_number_of_payments)).setText(RideDAO.getInstance().findAllPayments().size() + "");
        ListView listViewPayment = (ListView) view.findViewById(R.id.listView_payments);
        paymentAdapter = new PaymentAdapter(getActivity(), R.layout.rides_payment_adapter, RideDAO.getInstance().findAllPayments());
        listViewPayment.setAdapter(paymentAdapter);

        paymentAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<Payment> payments = RideDAO.getInstance().findAllPayments();

                Intent intent = new Intent(getActivity(), PaymentExpandableListActivity.class);
                intent.putExtra("positionPayment", position);
                intent.putExtra("idPayment", payments.get(position).getIdPayment());
                intent.putExtra("idRide1", payments.get(position).getIdRide1());
                intent.putExtra("idRide2", payments.get(position).getIdRide2());
                intent.putExtra("idRide3", payments.get(position).getIdRide3());

                if (payments.get(position).getIdRide1() > 0) {
                    intent.putExtra("dateRide1", RideDAO.getInstance().findAllRides().get(payments.get(position).getIdRide1() - 1).getDate());
                }
                if (payments.get(position).getIdRide2() > 0) {
                    intent.putExtra("dateRide2", RideDAO.getInstance().findAllRides().get(payments.get(position).getIdRide2() - 1).getDate());
                }
                if (payments.get(position).getIdRide3() > 0) {
                    intent.putExtra("dateRide3", RideDAO.getInstance().findAllRides().get(payments.get(position).getIdRide3() - 1).getDate());
                }

                getActivity().startActivity(intent);
            }
        });

        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.rides_menu_payment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_payment:
                getActivity().startActivity(new Intent(getActivity(), PaymentExpandableListActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public PaymentAdapter getPaymentAdapter() {
        return paymentAdapter;
    }

    public ListView getListViewPayment() {
        return listViewPayment;
    }

    public void updateViewPaymentsMade() {
        ((TextView) view.findViewById(R.id.text_view_count_number_of_payments)).setText("" + RideDAO.getInstance().findAllPayments().size());
    }
}
