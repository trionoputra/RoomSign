package com.trionoputra.roomsign.app;

import com.trionoputra.roomsign.Utils.FontsOverride;

public class MyApplication extends com.orm.SugarApp {
    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this);
    }
}