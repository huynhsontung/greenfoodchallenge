package com.ecoone.mindfulmealplanner;

import android.arch.lifecycle.ViewModel;

import com.ecoone.mindfulmealplanner.db.Plan;


public class MyPledgeViewModel extends ViewModel {

    private static float myPledge;
    private static String myUserName;
    private static String planName;
    private static Plan myPlan;

    // Post: Sets pledge and updates database if user changes their pledge.
    public static void setCurrentPledge(){

    }

    // Post: Gets user's pledge from the database.
    public static float getCurrentPledge(){

        return myPledge;
    }

    // Post: Gets current username (For the database)
    public static String getUserName(){

        return myUserName;
    }

    // Post: Gets the user's current plan name
    public static String getPlanNamePledge(){

        return planName;
    }

    // Post: Gets the user's current plan.
    public static Plan getPlanPledge(){

        return myPlan;
    }
}
