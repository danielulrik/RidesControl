package com.ulrik.rides.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel Ulrik
 * Date: 14/04/2015
 * Time: 16:03
 */
public class ReceiverResource extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.w("TESTE_BOOT", "PASSOU POR AQUI ");
    }
}
