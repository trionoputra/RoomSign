package com.trionoputra.roomsign;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.trionoputra.roomsign.Utils.Constant;
import com.trionoputra.roomsign.Utils.Shared;
import com.trionoputra.roomsign.settings.LoginActivity;
import com.trionoputra.roomsign.settings.SettingActivity;

public class SignInActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        final EditText txtPassword = (EditText)findViewById(R.id.txtPassword);
        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtPassword.getText().toString().equals(String.valueOf(Shared.read(Constant.KEY_SN,Constant.DEFAULT_SN))))
                {
                    Shared.write(Constant.KEY_FIRSTIME,"true");
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                }
                else
                {
                    Toast.makeText(SignInActivity.this,getString(R.string.error_password),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void back(View v)
    {
        finish();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

}
