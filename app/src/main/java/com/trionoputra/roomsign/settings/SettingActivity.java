package com.trionoputra.roomsign.settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.trionoputra.roomsign.FormScheduleActivity;
import com.trionoputra.roomsign.R;
import com.trionoputra.roomsign.Utils.Constant;
import com.trionoputra.roomsign.Utils.Shared;

public class SettingActivity extends AppCompatActivity {

    private EditText txtRefresh;
    private EditText txtTimeout;
    private EditText txtName;
    private EditText txtBuilding;
    private EditText txtFloor;
    private EditText txtDepartment;
    private EditText txtCapacity;
    private EditText txtCategory;
    private EditText txtPassword;
    private EditText txtMaxDay;
    private EditText txtMaxTime;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        txtRefresh = (EditText)findViewById(R.id.txtRefresh);
        txtTimeout = (EditText)findViewById(R.id.txtTimout);
        txtName = (EditText)findViewById(R.id.txtName);
        txtBuilding = (EditText)findViewById(R.id.txtBuilding);
        txtFloor = (EditText)findViewById(R.id.txtFloor);
        txtDepartment = (EditText)findViewById(R.id.txtDepartment);
        txtCapacity = (EditText)findViewById(R.id.txtCapacity);
        txtCategory = (EditText)findViewById(R.id.txtCategory);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        txtMaxDay = (EditText)findViewById(R.id.txtMaxDay);
        txtMaxTime = (EditText)findViewById(R.id.txtMaxTime);
        radioGroup  = (RadioGroup) findViewById(R.id.grupStatus);

        txtRefresh.setText(String.valueOf(Shared.read(Constant.KEY_TIMER_DATA,Constant.DEFAULT_TIMER_DATA)));
        txtTimeout.setText(String.valueOf(Shared.read(Constant.KEY_TIMER_ACTIVITY,Constant.DEFAULT_TIMER_ACTIVITY)));
        txtName.setText(Shared.read(Constant.KEY_ROOM_NAME,Constant.DEFAULT_ROOM_NAME));
        txtBuilding.setText(Shared.read(Constant.KEY_ROOM_BUILDING,Constant.DEFAULT_ROOM_BUILDING));
        txtFloor.setText(String.valueOf(Shared.read(Constant.KEY_ROOM_FLOOR,Constant.DEFAULT_ROOM_FLOOR)));
        txtDepartment.setText(Shared.read(Constant.KEY_ROOM_DEPARTMENT,Constant.DEFAULT_ROOM_DEPARTMENT));
        txtCapacity.setText(String.valueOf(Shared.read(Constant.KEY_ROOM_CAPACITY,Constant.DEFAULT_ROOM_CAPACITY)));
        txtCategory.setText(Shared.read(Constant.KEY_ROOM_CATAGORY,Constant.DEFAULT_ROOM_CATAGORY));
        txtMaxDay.setText(String.valueOf(Shared.read(Constant.KEY_ROOM_MAX_BOOKING_DAY,Constant.DEFAULT_MAX_BOOKING_DAY)));
        txtMaxTime.setText(String.valueOf(Shared.read(Constant.KEY_ROOM_MAX_BOOKING_TIME,Constant.DEFAULT_MAX_BOOKING_TIME)));

       if(Shared.read(Constant.KEY_BOOKING_STATUS,Constant.DEFAULT_BOOKING_STATUS).equals("true"))
           radioGroup.check(R.id.radioEnable);
        else
           radioGroup.check(R.id.radioDisable);

        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
    }

    private void save()
    {
        if(txtRefresh.getText().toString().equals("") ||
                txtMaxDay.getText().toString().equals("") ||
                txtMaxTime.getText().toString().equals("") ||
                txtTimeout.getText().toString().equals(""))
        {
            Toast.makeText(SettingActivity.this,getString(R.string.error_empty_general_field),Toast.LENGTH_SHORT).show();
            return;
        }

        if(txtName.getText().toString().equals("") ||
                txtBuilding.getText().toString().equals("") ||
                txtFloor.getText().toString().equals("") ||
                txtDepartment.getText().toString().equals("") ||
                txtCapacity.getText().toString().equals("") ||
                txtCategory.getText().toString().equals("")
                )
        {
            Toast.makeText(SettingActivity.this,getString(R.string.error_empty_meeting_field),Toast.LENGTH_SHORT).show();
            return;
        }

        if(!txtPassword.getText().toString().equals(""))
        {
            if(Integer.valueOf(txtPassword.getText().toString()) < 4)
            {
                Toast.makeText(SettingActivity.this,getString(R.string.error_password_field),Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Shared.write(Constant.KEY_TIMER_DATA,Integer.valueOf(txtRefresh.getText().toString()));
        Shared.write(Constant.KEY_TIMER_ACTIVITY,Integer.valueOf(txtTimeout.getText().toString()));
        Shared.write(Constant.KEY_ROOM_NAME,txtName.getText().toString());
        Shared.write(Constant.KEY_ROOM_BUILDING,txtBuilding.getText().toString());
        Shared.write(Constant.KEY_ROOM_FLOOR,Integer.valueOf(txtFloor.getText().toString()));
        Shared.write(Constant.KEY_ROOM_DEPARTMENT,txtDepartment.getText().toString());
        Shared.write(Constant.KEY_ROOM_CAPACITY,Integer.valueOf(txtCapacity.getText().toString()));
        Shared.write(Constant.KEY_ROOM_CATAGORY,txtCategory.getText().toString());
        Shared.write(Constant.KEY_ROOM_MAX_BOOKING_DAY,Integer.valueOf(txtMaxDay.getText().toString()));
        Shared.write(Constant.KEY_ROOM_MAX_BOOKING_TIME,Integer.valueOf(txtMaxTime.getText().toString()));

        Shared.write(Constant.KEY_BOOKING_STATUS,radioGroup.getCheckedRadioButtonId() == R.id.radioEnable ? "true" : "false");

        if(!txtPassword.getText().toString().equals(""))
        {
            Shared.write(Constant.KEY_PASSWORD,txtPassword.getText().toString());
        }

        Toast.makeText(SettingActivity.this,getString(R.string.data_updated),Toast.LENGTH_SHORT).show();


    }

    public void back(View v)
    {
        finish();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
}
