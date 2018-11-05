package com.ecoone.mindfulmealplanner;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.ecoone.mindfulmealplanner.db.AppDatabase;

import java.util.concurrent.TimeUnit;
import android.os.Handler;

public class DailyPledgeService extends IntentService {

   // AppDatabase mDb = AppDatabase.getDatabase(getApplicationContext());
   Handler mHandler;
    private static final String pTag = "pledge";

    public DailyPledgeService() {
        super("DailyPledgeService");
    }

    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
        Log.i(pTag, "in create");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(pTag, "in handle");
        onStartCommand();
    }

    public void onStartCommand() {
        Log.i(pTag, "in start");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DailyPledgeService.this,"service is running in the background", Toast.LENGTH_LONG).show();
            }
        });
    }
}
