package com.trionoputra.roomsign.interfaces;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.google.gson.Gson;
import com.trionoputra.roomsign.FormScheduleActivity;
import com.trionoputra.roomsign.MainActivity;
import com.trionoputra.roomsign.R;
import com.trionoputra.roomsign.Utils.Constant;
import com.trionoputra.roomsign.Utils.Shared;
import com.trionoputra.roomsign.entity.Event;
import com.trionoputra.roomsign.widget.EvenDetail;

/**
 * Created by Bakwan on 25/05/2018.
 */

public class WebCalendarAppInterface {
    private Activity mContext;

    public WebCalendarAppInterface(Activity c) {
        mContext = c;
    }

    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void goToform(final String date) {
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(Shared.read(Constant.KEY_BOOKING_STATUS,Constant.DEFAULT_BOOKING_STATUS).equals("true"))
                {
                    Intent intent = new Intent(mContext, FormScheduleActivity.class);
                    intent.putExtra("date",date);
                    mContext.startActivity(intent);
                    mContext.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                }
                else
                    Toast.makeText(mContext,R.string.error_booking_disable,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @JavascriptInterface
    public void showDetail(String detail) {
        Event event = new Gson().fromJson(detail, Event.class);
        EvenDetail info = new EvenDetail(mContext,event);
        info.show();
    }

}