package com.trionoputra.roomsign;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.trionoputra.roomsign.Utils.Constant;
import com.trionoputra.roomsign.Utils.Shared;
import com.trionoputra.roomsign.entity.Event;
import com.trionoputra.roomsign.widget.Loader;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FormScheduleActivity extends AppCompatActivity {

    private DatePickerDialog dateFromPickerDialog;
    private DatePickerDialog dateToPickerDialog;
    private TimePickerDialog timeFromPickerDialog;
    private TimePickerDialog timeToPickerDialog;
    public EditText txtTitle;
    public EditText txtDivision;
    public EditText txtPIC;
    public EditText txtAmount;
    public EditText txtEmail;
    private TextView txtDateFrom;
    private TextView txtDateTo;
    private TextView txtTimeFrom;
    private TextView txtTimeTo;


    public String selectedDateFrom;
    public String selectedDateTo;
    public String selectedTimeFrom;
    public String selectedTimeTo;
    private Loader loader;
    private TextView toolbarTitle;
    private TextClock textClock,textDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Shared.initialize(getBaseContext());

        setContentView(R.layout.activity_form_schedule);

        textClock =  (TextClock)findViewById(R.id.textClock);
        textDate = (TextClock)findViewById(R.id.txtDt);


        textClock.setFormat12Hour(null);
        textDate.setFormat12Hour(null);
        textClock.setFormat24Hour("HH:mm");
        textDate.setFormat24Hour("EEEE, d MMM yyyy");

        txtTitle = (EditText)findViewById(R.id.txtTitle) ;
        txtDivision = (EditText)findViewById(R.id.txtDivision) ;
        txtPIC = (EditText)findViewById(R.id.txtPic) ;
        txtAmount = (EditText)findViewById(R.id.txtAmount) ;
        txtDateFrom = (TextView)findViewById(R.id.txtDate) ;
        txtDateTo = (TextView)findViewById(R.id.txtDateTo) ;
        txtTimeFrom = (TextView)findViewById(R.id.txtFromtime) ;
        txtTimeTo = (TextView)findViewById(R.id.txtTotime) ;
        txtEmail = (EditText)findViewById(R.id.txtEmail) ;

        toolbarTitle = (TextView)findViewById(R.id.toolbarTitle);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            String data = extras.getString("date");
            try
            {
                selectedDateFrom = Shared.dateformatDay.format(Shared.dateformatDate.parse(data));
                selectedDateTo = Shared.dateformatDay.format(Shared.dateformatDate.parse(data));
            }
            catch (ParseException e)
            {
                selectedDateFrom = Shared.dateformatDay.format(new Date());
                selectedDateTo = Shared.dateformatDay.format(new Date());
            }
        }
        else
        {
            selectedDateFrom = Shared.dateformatDay.format(new Date());
            selectedDateTo = Shared.dateformatDay.format(new Date());
        }


        selectedTimeFrom = Shared.dateformatTime.format(new Date());
        selectedTimeTo = Shared.dateformatTime.format(new Date());

        txtTimeTo.setText(selectedTimeTo);
        txtTimeFrom.setText(selectedTimeFrom);
        txtDateFrom.setText(selectedDateFrom);
        txtDateTo.setText(selectedDateTo);

        Calendar c = Calendar.getInstance();

        dateFromPickerDialog = new DatePickerDialog(this, dateFromListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dateToPickerDialog = new DatePickerDialog(this, dateToListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        timeFromPickerDialog = new TimePickerDialog(this,timeFromListener,c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),true);
        timeToPickerDialog = new TimePickerDialog(this,timeToListener,c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),true);


        loader = new Loader(this);

        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(preSave())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FormScheduleActivity.this);
                    builder.setMessage(getString(R.string.booking_confirmation_2,
                            txtTitle.getText().toString(),
                            selectedTimeFrom,
                            selectedDateFrom,
                            selectedTimeTo,
                            selectedDateTo,
                            txtDivision.getText().toString(),
                            txtPIC.getText().toString(),
                            txtEmail.getText().toString(),
                            txtAmount.getText().toString()))
                            .setPositiveButton(getString(R.string.yes),  new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    save();
                                }
                            })
                            .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    dialog.show();
                }
            }
        });
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

    public void showTimePicker(View v)
    {
        switch (v.getId())
        {
            case R.id.btnTimeFrom:
                timeFromPickerDialog.show();
                break;
            case R.id.btnTimeTo:
                timeToPickerDialog.show();
                break;
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private boolean preSave()
    {
        if(txtTitle.getText().toString().equals("") ||
                txtPIC.getText().toString().equals("") ||
                txtDivision.getText().toString().equals("") ||
                txtAmount.getText().toString().equals("") ||
                txtEmail.getText().toString().equals(""))

        {
            Toast.makeText(FormScheduleActivity.this,getString(R.string.error_empty_field),Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!isValidEmail(txtEmail.getText()))
        {
            Toast.makeText(FormScheduleActivity.this,getString(R.string.error_invalid_email),Toast.LENGTH_SHORT).show();
            return false;
        }


        try {

            Date today = new Date();

            Date todayCompare = Shared.dateformatDate.parse(Shared.dateformatDate.format(today));
            Date selectedDateFrom = Shared.dateformatDay.parse(this.selectedDateFrom);

            long differenceMillis = selectedDateFrom.getTime() - todayCompare.getTime();
            int daysDifference = (int) (differenceMillis / (1000 * 60 * 60 * 24));

            if(selectedDateFrom.before(todayCompare))
            {
                Toast.makeText(FormScheduleActivity.this,getString(R.string.error_backdate),Toast.LENGTH_SHORT).show();
                return false;
            }
            else
            {

                int maxDay = Shared.read(Constant.KEY_ROOM_MAX_BOOKING_DAY,Constant.DEFAULT_MAX_BOOKING_DAY);
                if(daysDifference > maxDay )
                {
                    Toast.makeText(FormScheduleActivity.this,getString(R.string.error_7day_ahead,maxDay),Toast.LENGTH_SHORT).show();
                    return false;
                }
            }


            Date todayDateTime = Shared.datetimeformat.parse(Shared.datetimeformat.format(today));
            Date selectedFrom = Shared.dateformatFull.parse(this.selectedDateFrom + " " + this.selectedTimeFrom);
            Date selectedTo = Shared.dateformatFull.parse(this.selectedDateTo + " " + this.selectedTimeTo);

            if(selectedFrom.before(todayDateTime))
            {
                Toast.makeText(FormScheduleActivity.this,getString(R.string.error_backtime),Toast.LENGTH_SHORT).show();
                return false;
            }

            if(selectedTo.before(selectedFrom))
            {
                Toast.makeText(FormScheduleActivity.this,getString(R.string.error_endtime),Toast.LENGTH_SHORT).show();
                return false;
            }

            ArrayList<Event> data = (ArrayList<Event>) Event.findWithQuery(Event.class,"SELECT * FROM Event where " +
                    " ( strftime('%Y-%m-%d %H:%M','"+Shared.datetimeformat.format(selectedFrom)+"') BETWEEN start and  end ) or " +
                    " ( strftime('%Y-%m-%d %H:%M','"+Shared.datetimeformat.format(selectedTo)+"') BETWEEN start and  end  ) ");

            differenceMillis = selectedTo.getTime() - selectedFrom.getTime();
            int minutesDifference = (int) (differenceMillis / (1000 * 60 ));
            int maxTime = Shared.read(Constant.KEY_ROOM_MAX_BOOKING_TIME,Constant.DEFAULT_MAX_BOOKING_TIME);

            if(minutesDifference > maxTime )
            {
                Toast.makeText(FormScheduleActivity.this,getString(R.string.error_max_booking,maxTime),Toast.LENGTH_SHORT).show();
                return false;
            }


            if(data.size() != 0)
            {
                Toast.makeText(FormScheduleActivity.this,getString(R.string.error_already_book),Toast.LENGTH_SHORT).show();
                return false;
            }

        } catch (ParseException e) {
            Toast.makeText(FormScheduleActivity.this,getString(R.string.error),Toast.LENGTH_SHORT).show();
            return false;
        }

        try{

            Integer.parseInt(txtAmount.getText().toString());
        }
        catch (Exception e)
        {
            Toast.makeText(FormScheduleActivity.this,getString(R.string.error_invalid_integer),Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }
    private void save()
    {

        try {

            Event event = new Event();
            event.setTitle(txtTitle.getText().toString());
            event.setPic(txtPIC.getText().toString());
            event.setEmail(txtEmail.getText().toString());
            event.setDivision(txtDivision.getText().toString());
            event.setStart(Shared.dateformatDate.format(Shared.dateformatDay.parse(this.selectedDateFrom)) + " " + txtTimeFrom.getText().toString());
            event.setEnd(Shared.dateformatDate.format(Shared.dateformatDay.parse(this.selectedDateTo)) + " "  + txtTimeTo.getText().toString());
            event.setAmount(Integer.valueOf((txtAmount.getText().toString())));

            SaveTask save = new SaveTask();
            save.execute(event);

        } catch (ParseException e) {
            Toast.makeText(FormScheduleActivity.this,getString(R.string.error),Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void showDatePicker(View v)
    {
        dateFromPickerDialog.show();
    }
    public void showDateToPicker(View v)
    {
        dateToPickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener dateFromListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);
            dateFromPickerDialog.updateDate(year, monthOfYear, dayOfMonth);
            selectedDateFrom = Shared.dateformatDay.format(calendar.getTime());
            txtDateFrom.setText(selectedDateFrom);
        }
    };

    private DatePickerDialog.OnDateSetListener dateToListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);
            dateToPickerDialog.updateDate(year, monthOfYear, dayOfMonth);
            selectedDateTo = Shared.dateformatDay.format(calendar.getTime());
            txtDateTo.setText(selectedDateTo);
        }
    };

    private TimePickerDialog.OnTimeSetListener timeFromListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),hourOfDay,minute);
            timeFromPickerDialog.updateTime(hourOfDay,minute);

            selectedTimeFrom =  Shared.dateformatTime.format(calendar.getTime());
            txtTimeFrom.setText(selectedTimeFrom);
        }
    };

    private TimePickerDialog.OnTimeSetListener timeToListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),hourOfDay,minute);
            timeToPickerDialog.updateTime(hourOfDay,minute);

            selectedTimeTo =  Shared.dateformatTime.format(calendar.getTime());
            txtTimeTo.setText(selectedTimeTo);
        }
    };

    private class SaveTask extends AsyncTask<Event, String, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader.show();
        }

        @Override
        protected String doInBackground(Event... events) {

            String result = "ok";

            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                result = getString(R.string.error);
            }

            events[0].save();

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loader.dismiss();
            if(s.equals("ok"))
            {
                Toast.makeText(FormScheduleActivity.this,getString(R.string.book_success),Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
            else
                Toast.makeText(FormScheduleActivity.this,s,Toast.LENGTH_SHORT).show();

        }
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
        Intent intent =  new Intent(FormScheduleActivity.this,MainActivity.class);
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
    }
}
