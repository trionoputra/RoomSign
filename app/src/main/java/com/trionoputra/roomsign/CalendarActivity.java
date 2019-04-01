package com.trionoputra.roomsign;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextClock;
import android.widget.TextView;

import com.trionoputra.roomsign.Utils.Constant;
import com.trionoputra.roomsign.Utils.Shared;
import com.trionoputra.roomsign.entity.Event;
import com.trionoputra.roomsign.interfaces.WebCalendarAppInterface;
import com.trionoputra.roomsign.widget.Loader;

import java.util.ArrayList;

public class CalendarActivity extends AppCompatActivity {
    private WebView webView;
    private Loader loader;
    private TextView toolbarTitle;
    private TextClock textClock,textDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Shared.initialize(getBaseContext());

        setContentView(R.layout.activity_calendar);

        textClock =  (TextClock)findViewById(R.id.textClock);
        textDate = (TextClock)findViewById(R.id.txtDate);

        textClock.setFormat12Hour(null);
        textDate.setFormat12Hour(null);
        textClock.setFormat24Hour("HH:mm");
        textDate.setFormat24Hour("EEEE, d MMM yyyy");

        toolbarTitle = (TextView)findViewById(R.id.toolbarTitle);

        loader = new Loader(this);
        loader.show();

        webView = (WebView) findViewById(R.id.webview);
        webView.setBackgroundColor(0);
        webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);

        WebCalendarAppInterface WebInterface = new WebCalendarAppInterface(this);
        webView.addJavascriptInterface(WebInterface,"Android");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished (WebView view, String url) {
                ArrayList<Event> data = (ArrayList<Event>) Event.findWithQuery(Event.class,"SELECT * FROM Event where strftime('%Y-%m-%d',start) >= strftime('%Y-%m-%d','now','localtime')");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    view.evaluateJavascript("initCalendar("+data.toString()+")", null);
                } else {
                    view.loadUrl("javascript:initCalendar("+data.toString()+")");
                }

                loader.dismiss();
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    public void back(View v)
    {
        finish();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    Handler handler = new Handler();
    private int timer = 0;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            if(timer >= Shared.read(Constant.KEY_TIMER_ACTIVITY,Constant.DEFAULT_TIMER_ACTIVITY))
            {
                resetActivity();
            }

            timer++;
            handler.postDelayed(this, Shared.read(Constant.KEY_TIMER_TICK,Constant.DEFAULT_TIMER_TICK));
        }
    };

    private void resetActivity()
    {
        handler.removeCallbacks(runnable);
        Intent intent =  new Intent(CalendarActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(intent);

    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        timer = 0;
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.removeCallbacks(runnable);
        handler.post(runnable);
        toolbarTitle.setText(Shared.read(Constant.KEY_ROOM_NAME,Constant.DEFAULT_ROOM_NAME));
        webView.loadUrl("file:///android_asset/index.html");
    }
}
