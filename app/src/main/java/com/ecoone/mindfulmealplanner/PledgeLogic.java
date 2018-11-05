package com.ecoone.mindfulmealplanner;

import com.ecoone.mindfulmealplanner.db.Plan;
import com.ecoone.mindfulmealplanner.Calculator;

public class PledgeLogic {
    private Plan usersCurrentPlan;
    private float currentPledgePerWeek;
    private float currentPledgePerDay;
    private float currentPlanCO2PerDay;

    public PledgeLogic(Plan plan, float pledgeAmount) {
        usersCurrentPlan = plan;
        currentPlanCO2PerDay = Calculator.calculateCO2ePerDay(usersCurrentPlan);
        currentPledgePerWeek = pledgeAmount;
        currentPledgePerDay = currentPledgePerWeek/7;
    }

    public void updateCurrentPlan(Plan newPlan) {
        usersCurrentPlan = newPlan;
        currentPlanCO2PerDay = Calculator.calculateCO2ePerDay(usersCurrentPlan);
    }

    public float differenceInCO2() {
        float difference;
        if(currentPledgePerDay >= currentPlanCO2PerDay) {
            difference = currentPledgePerDay - currentPlanCO2PerDay;
        }
        else {
            difference = currentPlanCO2PerDay - currentPlanCO2PerDay;
        }
        return difference;
    }

}
