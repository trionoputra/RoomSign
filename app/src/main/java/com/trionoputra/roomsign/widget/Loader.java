package com.trionoputra.roomsign.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.Window;
import com.trionoputra.roomsign.R;

/**
 * Created by Bakwan on 25/05/2018.
 */

public class Loader extends Dialog {

    private Context mContext;
    public Loader(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public Loader(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    protected Loader(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_loader);

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        GifView gifView1 = (GifView)findViewById(R.id.loader);

        this.setCancelable(false);
    }
}
