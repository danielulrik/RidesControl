package com.ulrik.rides.view.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ulrik.rides.R;

/**
 * Created with IntelliJ IDEA.
 * User: Leonardo Neves
 * Date: 30/03/2015
 * Time: 15:55
 */
public class NewPaymentView extends LinearLayout {

    private TextView tvRide1;
    private TextView tvRide2;
    private TextView tvRide3;

    private ImageView imageViewRide1;
    private ImageView imageViewRide2;
    private ImageView imageViewRide3;

    public NewPaymentView(Context context) {
        this(context, null);
    }

    public NewPaymentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewPaymentView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.rides_new_payment, this);

        tvRide1 = (TextView) findViewById(R.id.tvRide1);
        tvRide2 = (TextView) findViewById(R.id.tvRide2);
        tvRide3 = (TextView) findViewById(R.id.tvRide3);

        imageViewRide1 = (ImageView) findViewById(R.id.imageViewRide1);
        imageViewRide2 = (ImageView) findViewById(R.id.imageViewRide2);
        imageViewRide3 = (ImageView) findViewById(R.id.imageViewRide3);

    }

    public TextView getTvRide1() {
        return tvRide1;
    }

    public TextView getTvRide2() {
        return tvRide2;
    }

    public TextView getTvRide3() {
        return tvRide3;
    }

    public ImageView getImageViewRide1() {
        return imageViewRide1;
    }

    public ImageView getImageViewRide2() {
        return imageViewRide2;
    }

    public ImageView getImageViewRide3() {
        return imageViewRide3;
    }

}
