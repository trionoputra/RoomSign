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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.trionoputra.roomsign.R;
import com.trionoputra.roomsign.Utils.Shared;
import com.trionoputra.roomsign.entity.Event;
import com.trionoputra.roomsign.entity.Room;

import java.text.ParseException;

/**
 * Created by Bakwan on 25/05/2018.
 */

public class RoomInfo extends Dialog {

    private Context mContext;
    private Room room;
    public RoomInfo(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }
    public RoomInfo(@NonNull Context context, Room room) {
        super(context);
        this.mContext = context;
        this.room = room;
    }

    public RoomInfo(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    protected RoomInfo(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_room_info);

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        if(room != null)
        {
            TextView txtName = (TextView)findViewById(R.id.txtName);
            TextView txtBuilding = (TextView)findViewById(R.id.txtBuilding);
            TextView txtFloor = (TextView)findViewById(R.id.txtFloor);
            TextView txtDepartment = (TextView)findViewById(R.id.txtDepartment);
            TextView txtCapacity = (TextView)findViewById(R.id.txtCapacity);
            TextView txtCategory = (TextView)findViewById(R.id.txtCategory);

            txtName.setText(room.getName());
            txtBuilding.setText(room.getBuilding());
            txtFloor.setText(room.getFloor());
            txtDepartment.setText(room.getDepartment());
            txtCapacity.setText(room.getCapacity());
            txtCategory.setText(room.getCategory());

            ImageView imageView = (ImageView) findViewById(R.id.imageView2);
            Glide.with(mContext)
                    .load(room.getImg())
                    .apply(RequestOptions.placeholderOf(R.mipmap.meeting_root))
                    .into(imageView);
        }

        findViewById(R.id.btnExit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }
}
