package com.ecoone.mindfulmealplanner.pledge;

import android.util.Log;

import com.ecoone.mindfulmealplanner.PlanPledgeInterface;
import com.ecoone.mindfulmealplanner.tools.Calculator;
import com.ecoone.mindfulmealplanner.database.Plan;

public class PledgeLogic {
    private static Plan usersCurrentPlan;
    private static String usersCurrentPlanName;
    private static float currentPledgePerWeek;
    private static float currentPledgePerDay;
    private static float currentPlanCO2PerDay;
    private static float currentPlanCO2PerWeek;
    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(PledgeLogic)";
    private static PlanPledgeInterface mInterface;

    public PledgeLogic(PlanPledgeInterface mNewInterface) {
        Log.i(TAG,CLASSTAG + "created");
        mInterface = mNewInterface;
    }

    public static Plan getUsersCurrentPlan() {
        return usersCurrentPlan;
    }

    public static float getCurrentPledgePerWeek() {
        return currentPledgePerWeek;
    }

    public static float getCurrentPledgePerDay() {
        return currentPledgePerDay;
    }

    public static float getCurrentPlanCO2PerDay() {
        return currentPlanCO2PerDay;
    }

    public static float getCurrentPlanCO2PerWeek() {
        return currentPlanCO2PerWeek;
    }

    public static String getUsersCurrentPlanName() {
        return usersCurrentPlanName;
    }

    public static void updateCurrentPlan(Plan newPlan) {
        Log.i(TAG,CLASSTAG + "update plan ");
        usersCurrentPlan = newPlan;
        currentPlanCO2PerDay = Calculator.calculateCO2ePerDay(usersCurrentPlan);
        currentPlanCO2PerWeek = (currentPlanCO2PerDay*7)/1000;
        mInterface.updatePledgeTip();
    }

    public static void updateCurrentPlanName(String name) {
        usersCurrentPlanName = name;
    }

    // after button press
    public void updatePledgeAmount(float newPledge) {
        currentPledgePerWeek = newPledge;
        currentPledgePerDay = currentPledgePerWeek/7;
    }
}
