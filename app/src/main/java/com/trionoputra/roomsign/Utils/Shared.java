package com.trionoputra.roomsign.Utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.view.Display;
import android.view.WindowManager;
import java.text.SimpleDateFormat;
import java.util.Locale;

public final class Shared
{
	private static ContextWrapper instance;
	private static SharedPreferences pref;
	public static Typeface appfont;
	public static Typeface appfontBold;
	public static Typeface appfontLight;
	public static Typeface appfontThin;

	public static SimpleDateFormat datetimeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public static SimpleDateFormat dateformatTime = new SimpleDateFormat("HH:mm");
	public static SimpleDateFormat dateformatTimeFull = new SimpleDateFormat("HH:mm:ss");
	public static SimpleDateFormat dateformatDate = new SimpleDateFormat("yyyy-MM-dd");

	public static SimpleDateFormat dateformatDay = new SimpleDateFormat("EEEE, d MMM yyyy");
    public static SimpleDateFormat dateformatFull = new SimpleDateFormat("EEEE, d MMM yyyy HH:mm");

	public static void initialize(Context base)
	{
		instance = new ContextWrapper(base);
		pref = instance.getSharedPreferences("com.trionoputra.roomsign", Context.MODE_PRIVATE);
		appfont = Typeface.createFromAsset(instance.getAssets(),"fonts/Roboto-Regular.ttf");
		appfontBold = Typeface.createFromAsset(instance.getAssets(),"fonts/Roboto-Bold.ttf");
		appfontThin = Typeface.createFromAsset(instance.getAssets(),"fonts/Roboto-Thin.ttf");
		appfontLight = Typeface.createFromAsset(instance.getAssets(),"fonts/Roboto-Light.ttf");
	}
	
	public static void write(String key, String value)
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, value);
		editor.commit();
	}
	public static void write(Integer key, String value)
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(String.valueOf(key), value);
		editor.commit();
	}


	public static void write(String key, Integer value)
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(String.valueOf(key), value);
		editor.commit();
	}

	public static void write(Integer key, Integer value)
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(String.valueOf(key), value);
		editor.commit();
	}
	
	public static String read(String key)
	{
		return Shared.read(key, null);
	}
	
	public static String read(String key, String defValue)
	{
		return pref.getString(key, defValue);
	}

	public static String read(int key, String defValue)
	{
		return pref.getString(String.valueOf(key), defValue);
	}

	public static Integer read(Integer key, int defValue)
	{
		return pref.getInt(String.valueOf(key),defValue);
	}


	
	public static void clear()
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.clear();
		editor.commit();
	}
	
	public static void clear(String key)
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.remove(key);
		editor.commit();
	}
	public static Context getContext()
	{
		return instance.getBaseContext();
	}

}

