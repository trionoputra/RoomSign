package com.trionoputra.roomsign.services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.List;

public class RSService extends Service {

    public boolean isRunning = false;
    private mThread mthread;
    private String TAG = "ROOMSIGN";

    public RSService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void doIng()
    {
        Log.d(TAG,"DoinDoin");
        if(isSettingOpen(getApplicationContext()))
        {
            Log.d(TAG,"open");
        }
    }

    public boolean isSettingOpen(Context ctx){
        ActivityManager manager = (ActivityManager) ctx.getSystemService(ACTIVITY_SERVICE);
        List< ActivityManager.RunningTaskInfo > runningAppProcessInfo = manager.getRunningTasks(1);
        Log.d(TAG,runningAppProcessInfo.isEmpty() + "1");
        boolean appFound = false;
        if (!runningAppProcessInfo.isEmpty()) {
            final ComponentName topActivity = runningAppProcessInfo.get(0).topActivity;
            Log.d(TAG,topActivity.getPackageName());
            if (topActivity.getPackageName().equals(ctx.getPackageName())) {
                appFound =  true;
            }
        }

        return appFound;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCreate");
        mthread = new mThread();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        Log.d(TAG,"onStartCommand");
        if(!isRunning){
            mthread.start();
            isRunning = true;
            Log.d(TAG,"startTread");
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.d(TAG,"onDestroy");
        if(!isRunning)
        {
            mthread.interrupt();
            mthread.stop();
            Log.d(TAG,"stopTread");
        }

        sendBroadcast(new Intent("YouWillNeverKillMe"));

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.d(TAG,"onTaksRemove");
        sendBroadcast(new Intent("YouWillNeverKillMe"));
    }

    class mThread extends Thread{
        final long DELAY = 1000;
        @Override
        public void run(){
            while(isRunning){
                try
                {
                    doIng();

                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    isRunning = false;
                    Log.d(TAG,"Doin ERROR");
                    e.printStackTrace();

                }
            }
        }
    }
}
