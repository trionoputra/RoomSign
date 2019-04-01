package com.trionoputra.roomsign.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class ServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context.getApplicationContext(), RSService.class);
        context.startService(service);
        Log.d("ROOMSIGN","start service again");
    }
}