package com.ecoone.mindfulmealplanner.pledge;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.ecoone.mindfulmealplanner.database.Plan;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.os.Handler;

// ** NOTE:
// ** THIS FILE IS NOT USED
// ** ORIGNALLY MEANT TO CALCULATE PLEDGE DETAILS IN THE BACKGROUND
// ** KEPT IN PROJECT FOR FUTURE PURPOSES

public class DailyPledgeService extends IntentService {

    // AppDatabase mDb = AppDatabase.getDatabase(getApplicationContext());
    Handler mHandler;
    private static final String pTag = "pledge";
    public static Plan usersPlanAfterDay;
    public static float usersCO2SavedAfterDay;
    public static int flagToCheckServiceStatus = 0;

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

        if (flagToCheckServiceStatus == 0) {
            flagToCheckServiceStatus = 1;
            mHandler.post(() -> Toast.makeText(DailyPledgeService.this, "service is running in the background", Toast.LENGTH_LONG).show());
            Map<Integer, Integer> daysUntilWeekPassed = new HashMap<>();
            // Week starts on monday
        /*daysUntilWeekPassed.put(Calendar.MONDAY, 7);
        daysUntilWeekPassed.put(Calendar.TUESDAY, 6);
        daysUntilWeekPassed.put(Calendar.WEDNESDAY, 5);
        daysUntilWeekPassed.put(Calendar.THURSDAY, 4);
        daysUntilWeekPassed.put(Calendar.FRIDAY, 3);
        daysUntilWeekPassed.put(Calendar.SATURDAY, 2);
        daysUntilWeekPassed.put(Calendar.SUNDAY, 1);*/
            //int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
            //int numDaysUntilMonday = daysUntilWeekPassed.get(currentDay);
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


  /*      Log.i(pTag, "current day = " + currentDay);
        Log.i(pTag, "days until monday = " + numDaysUntilMonday);*/
            // loops every hour
            final ScheduledFuture<?> everyHour = scheduler.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    usersPlanAfterDay = PledgeLogic.getUsersCurrentPlan();
                    usersCO2SavedAfterDay = PledgeLogic.getCurrentPlanCO2PerDay();
                    // update the users current plan and CO2 saved every hour
                    Log.i(pTag, "running every 1 seconds in hour!");
                }
            }, 0, 1, TimeUnit.SECONDS);

            // delays for 24 hours before cancelling everyHour
            final ScheduledFuture<?> everyDay = scheduler.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    // update firebase with users daily pledge
                    Log.i(pTag, "running every 5 seconds in day");
                }
            }, 5, 5, TimeUnit.SECONDS);

            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    everyDay.cancel(true);
                    everyHour.cancel(true);
                    // update firebase with the users total pledge for the week
                    Log.i(pTag, "running every 7 seconds in week");
                    flagToCheckServiceStatus = 0;
                }
            }, 35, TimeUnit.SECONDS);

        }
    }
}
