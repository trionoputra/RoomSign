package com.trionoputra.roomsign.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.trionoputra.roomsign.R;
import com.trionoputra.roomsign.Utils.Shared;
import com.trionoputra.roomsign.entity.Event;

import org.w3c.dom.Text;

import java.text.ParseException;

/**
 * Created by Bakwan on 25/05/2018.
 */

public class EvenDetail extends Dialog {

    private Context mContext;
    private Event event;
    public EvenDetail(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }
    public EvenDetail(@NonNull Context context,Event event) {
        super(context);
        this.mContext = context;
        this.event = event;
    }

    public EvenDetail(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    protected EvenDetail(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_event_details);

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        if(event != null)
        {
            TextView txtTitle = (TextView)findViewById(R.id.txtTitle);
            TextView txtDate = (TextView)findViewById(R.id.txtDate);
            TextView txtTime = (TextView)findViewById(R.id.txtTime);
            TextView txtPic = (TextView)findViewById(R.id.txtPic);
            TextView txtDivision = (TextView)findViewById(R.id.txtDivision);
            TextView txtAmount = (TextView)findViewById(R.id.txtAmount);
            TextView txtEmail = (TextView)findViewById(R.id.txtEmail);

            txtTitle.setText(event.getTitle());
            txtTime.setText(event.getTimeFrom() + " - " + event.getTimeTo());
            txtPic.setText(event.getPic());
            txtDivision.setText(event.getDivision());
            txtAmount.setText(String.valueOf(event.getAmount()) +  " " + mContext.getString(R.string.person));
            txtEmail.setText(event.getEmail());
            try {
                txtDate.setText(Shared.dateformatDay.format(Shared.datetimeformat.parse(event.getStart())));
            } catch (ParseException e) {

            }
        }

        findViewById(R.id.btnExit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }
}
