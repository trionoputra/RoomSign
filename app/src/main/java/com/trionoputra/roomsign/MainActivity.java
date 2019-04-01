package com.trionoputra.roomsign;

import android.content.ComponentCallbacks2;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.trionoputra.roomsign.Utils.Constant;
import com.trionoputra.roomsign.Utils.Shared;
import com.trionoputra.roomsign.adapter.ScheduleAdapter;
import com.trionoputra.roomsign.entity.Event;
import com.trionoputra.roomsign.entity.Room;
import com.trionoputra.roomsign.services.RSService;
import com.trionoputra.roomsign.settings.LoginActivity;
import com.trionoputra.roomsign.widget.EvenDetail;
import com.trionoputra.roomsign.widget.Loader;
import com.trionoputra.roomsign.widget.RoomInfo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    private ListView lv;
    private ScheduleAdapter adapter;
    private Loader loader;
    private TextView txtnodata;
    private TextView txtTile;
    private TextView txtTime;
    private RoomInfo roomInfo;
    private AlertDialog delDialog;
    private TextView toolbarTitle;
    private TextClock textClock,textDate;
    public static boolean hasLogin = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Shared.initialize(getBaseContext());


        setContentView(R.layout.activity_main);

        textClock =  (TextClock)findViewById(R.id.textClock);
        textDate = (TextClock)findViewById(R.id.txtDate);

        textClock.setFormat12Hour(null);
        textDate.setFormat12Hour(null);
        textClock.setFormat24Hour("HH:mm");
        textDate.setFormat24Hour("EEEE, d MMM yyyy");

        lv = (ListView)findViewById(R.id.listview);
        txtnodata = (TextView)findViewById(R.id.txtnodata);

        adapter = new ScheduleAdapter(this);
        lv.setAdapter(adapter);

        loader = new Loader(this);

        lv.setOnItemLongClickListener(lvLongClick);
        lv.setOnItemClickListener(lvItemClick);

        TextView txtDate = (TextView)findViewById(R.id.txtDate);
        txtDate.setText(Shared.dateformatDay.format(new Date()));

        txtTile = (TextView)findViewById(R.id.txtTitle);
        txtTime = (TextView)findViewById(R.id.txtTime);
        toolbarTitle = (TextView)findViewById(R.id.toolbarTitle);

        findViewById(R.id.imageView3).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                return true;
            }
        });

        if(Shared.read(Constant.KEY_FIRSTIME,"false").equals("false"))
        {
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            finish();
        }

        Intent intent = new Intent(this, RSService.class);
        startService(intent);


    }

    @Override
    protected void onResume() {
        super.onResume();
        GetEventTask event = new GetEventTask();
        event.execute();
        handler.removeCallbacks(runnable);
        handler.post(runnable);
        toolbarTitle.setText(Shared.read(Constant.KEY_ROOM_NAME,Constant.DEFAULT_ROOM_NAME));
        hasLogin = false;
    }

    public void MenuOnClick(View v)
    {
        Intent intent = null;
        switch (v.getId())
        {
            case R.id.btnInfo:
                showRoomInfo();
            break;
            case R.id.btnStatus:
                intent = new Intent(MainActivity.this,CalendarActivity.class);
                break;
            case R.id.btnBooking:
                if(Shared.read(Constant.KEY_BOOKING_STATUS,Constant.DEFAULT_BOOKING_STATUS).equals("true"))
                    intent = new Intent(MainActivity.this,FormScheduleActivity.class);
                else
                    Toast.makeText(MainActivity.this,R.string.error_booking_disable,Toast.LENGTH_SHORT).show();

                break;
        }

        if(intent != null)
        {
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        }

    }

    private class GetEventTask extends AsyncTask<String, String, ArrayList<Event>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
         //   loader.show();
        }

        @Override
        protected ArrayList<Event> doInBackground(String... strings) {
            ArrayList<Event> data = (ArrayList<Event>) Event.findWithQuery(Event.class,"SELECT * FROM Event where strftime('%Y-%m-%d',start) = strftime('%Y-%m-%d','now','localtime') order by strftime('%Y-%m-%d %H:%M',start) asc");
            try {
                Thread.sleep(1200);
            } catch (InterruptedException e) {
            }

            return data;
        }

        @Override
        protected void onPostExecute(ArrayList<Event> events) {
            super.onPostExecute(events);
            if(events.size() == 0)
                txtnodata.setVisibility(View.VISIBLE);
            else
                txtnodata.setVisibility(View.GONE);

            adapter.set(events);
            checkOngoing();
            loader.dismiss();
        }
    }

    private AdapterView.OnItemLongClickListener lvLongClick = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view,final int i, long l) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(getString(R.string.confirm_delete))
                    .setPositiveButton(getString(R.string.ok),  new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Event event = Event.findById(Event.class, adapter.getItem(i).getId());
                            event.delete();

                            GetEventTask events = new GetEventTask();
                            events.execute();
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                        }
                    });

            delDialog = builder.create();
            delDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            delDialog.show();
            return true;
        }
    };

    private AdapterView.OnItemClickListener lvItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            EvenDetail info = new EvenDetail(MainActivity.this,adapter.getItem(i));
            info.show();
        }
    };

    private void showRoomInfo()
    {
        Room room = new Room();
        room.setName(Shared.read(Constant.KEY_ROOM_NAME,Constant.DEFAULT_ROOM_NAME));
        room.setBuilding(Shared.read(Constant.KEY_ROOM_BUILDING,Constant.DEFAULT_ROOM_BUILDING));
        room.setFloor(String.valueOf(Shared.read(Constant.KEY_ROOM_FLOOR,Constant.DEFAULT_ROOM_FLOOR)));
        room.setDepartment(Shared.read(Constant.KEY_ROOM_DEPARTMENT,Constant.DEFAULT_ROOM_DEPARTMENT));
        room.setCapacity(String.valueOf(Shared.read(Constant.KEY_ROOM_CAPACITY,Constant.DEFAULT_ROOM_CAPACITY)));
        room.setCategory(Shared.read(Constant.KEY_ROOM_CATAGORY,Constant.DEFAULT_ROOM_CATAGORY));
        room.setImg("");

        roomInfo = new RoomInfo(MainActivity.this,room);
        roomInfo.show();
    }

    Handler handler = new Handler();
    private int timer = 0;
    private int timer2 = 0;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            checkOngoing();
            if(timer2 >= Shared.read(Constant.KEY_TIMER_DATA,Constant.DEFAULT_TIMER_DATA))
            {
                GetEventTask event = new GetEventTask();
                event.execute();
                timer2 = 0;
            }

            if(timer >= Shared.read(Constant.KEY_TIMER_ACTIVITY,Constant.DEFAULT_TIMER_ACTIVITY))
            {
                resetActivity();
            }

            timer++;
            timer2++;
            handler.postDelayed(this, Shared.read(Constant.KEY_TIMER_TICK,Constant.DEFAULT_TIMER_TICK));
        }
    };

    private void checkOngoing()
    {
        ArrayList<Event> current = (ArrayList<Event>) Event.findWithQuery(Event.class,"SELECT * FROM Event where  ( strftime('%Y-%m-%d %H:%M','"+Shared.datetimeformat.format(new Date())+"') BETWEEN start and  end  ) ");
        if(current.size() != 0 )
        {
            Event event = current.get(0);
            adapter.setOngoing(event);
            txtTile.setText(event.getTitle());
            txtTime.setText(event.getTimeFrom() + " - " + event.getTimeTo());
        }
        else
        {
            txtTime.setText("");
            txtTile.setText(getString(R.string.room_available));
        }
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        timer = 0;
    }

    private void resetActivity()
    {
        if(roomInfo != null)
            roomInfo.dismiss();
        if(delDialog != null)
            delDialog.dismiss();

        timer = 0;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
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
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            Log.d("ROOMSIGN",hasLogin + " CLOSE");
            hasLogin = false;
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.putExtra("isClose",true);
           // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK );
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        }

    }
}