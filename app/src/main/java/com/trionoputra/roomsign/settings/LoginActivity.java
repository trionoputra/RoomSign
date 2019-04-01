package com.trionoputra.roomsign.settings;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.trionoputra.roomsign.FormScheduleActivity;
import com.trionoputra.roomsign.MainActivity;
import com.trionoputra.roomsign.R;
import com.trionoputra.roomsign.Utils.Constant;
import com.trionoputra.roomsign.Utils.Shared;

public class LoginActivity extends AppCompatActivity {
    private boolean isClosing = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText txtPassword = (EditText)findViewById(R.id.txtPassword);
        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtPassword.getText().toString().equals(String.valueOf(Shared.read(Constant.KEY_PASSWORD,Constant.DEFAULT_PASSWORD))))
                {
                    if(!isClosing)
                    {
                        Intent intent = new Intent(LoginActivity.this, SettingActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    }
                    else
                    {
                        MainActivity.hasLogin = true;
                        finish();
                    }

                }
                else
                {
                    Toast.makeText(LoginActivity.this,getString(R.string.error_password),Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView txtVersion = (TextView)findViewById(R.id.txtVersion);
        PackageManager manager = this.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            txtVersion.setText("v" + info.versionName + "-" + info.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
          //  e.printStackTrace();
        }

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            isClosing = extras.getBoolean("isClose");
        }
    }

    public void back(View v)
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
}
