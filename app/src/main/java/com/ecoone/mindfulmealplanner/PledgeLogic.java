package com.ecoone.mindfulmealplanner;

import com.ecoone.mindfulmealplanner.db.Plan;
import com.ecoone.mindfulmealplanner.Calculator;

public class PledgeLogic {
    private static Plan usersCurrentPlan;
    private static float currentPledgePerWeek;
    private static float currentPledgePerDay;
    private static float currentPlanCO2PerDay;

    public PledgeLogic(Plan plan, float pledgeAmount) {
        usersCurrentPlan = plan;
        currentPlanCO2PerDay = Calculator.calculateCO2ePerDay(usersCurrentPlan);
        currentPledgePerWeek = pledgeAmount;
        currentPledgePerDay = currentPledgePerWeek/7;
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
        return currentPledgePerDay;
    }

    public void updateCurrentPlan(Plan newPlan) {
        usersCurrentPlan = newPlan;
        currentPlanCO2PerDay = Calculator.calculateCO2ePerDay(usersCurrentPlan);
    }

    public void updatePledgeAmount(float newPledge) {
        currentPledgePerWeek = newPledge;
        currentPledgePerDay = currentPledgePerWeek/7;
    }

    public static float differenceInCO2() {
        float difference;
        if(currentPledgePerDay >= currentPlanCO2PerDay) {
            difference = currentPledgePerDay - currentPlanCO2PerDay;
        }
        else {
            difference = currentPlanCO2PerDay - currentPlanCO2PerDay;
        }
        return difference;
    }

    public static void checkEveryHourForChange() {
        if(Calculator.calculateCO2ePerDay(usersCurrentPlan) != currentPlanCO2PerDay) {
            currentPlanCO2PerDay = Calculator.calculateCO2ePerDay(usersCurrentPlan);
        }
    }
}
