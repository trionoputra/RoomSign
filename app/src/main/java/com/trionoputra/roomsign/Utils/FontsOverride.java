package com.trionoputra.roomsign.Utils;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;



public final class FontsOverride {

    public static void setDefaultFont(Context context)
    {
        Shared.initialize(context);
        replaceFont("MONOSPACE", Shared.appfont);
    }
    protected static void replaceFont(String staticTypefaceFieldName,final Typeface newTypeface) {
        try {
            final Field staticField = Typeface.class.getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);

            staticField.set(null, newTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}