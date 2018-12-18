package com.ecoone.mindfulmealplanner;

import com.ecoone.mindfulmealplanner.database.Plan;
import com.ecoone.mindfulmealplanner.pledge.PledgeLogic;
import com.ecoone.mindfulmealplanner.tools.Calculator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class PledgeLogicTest {

    public Plan testPlan = new Plan() {
        {
            beef = 50;
            pork = 75;
            chicken = 50;
            fish = 50;
            eggs = 50;
            beans = 25;
            vegetables = 100;
        }
    };

    Plan newPlan = new Plan() {
        {
            beef = 100;
            pork = 100;
            chicken = 100;
            fish = 100;
            eggs = 100;
            beans = 100;
            vegetables = 100;
        }
    };

    PlanPledgeInterface mInterface = new PlanPledgeInterface() {
        @Override
        public void updatePledgeTip() {

        }
    };

    PledgeLogic testLogic = new PledgeLogic(mInterface);

    @Test
    public void updateCurrentPlan() {
        testLogic.updateCurrentPlan(newPlan);
        assertNotEquals(testLogic.getUsersCurrentPlan().beans, testPlan.beans,0.001);
        assertEquals(testLogic.getCurrentPlanCO2PerDay(), Calculator.calculateCO2ePerDay(newPlan),0.001);
    }

    @Test
    public void getUsersCurrentPlan() {
        assertEquals(testLogic.getUsersCurrentPlan().beef, newPlan.beef, 0.01);
        assertNotEquals(testLogic.getUsersCurrentPlan().eggs, testPlan.eggs, 0.01);
        assertEquals(testLogic.getUsersCurrentPlan().vegetables, newPlan.vegetables, 0.01);
    }

    @Test
    public void updatePledgeAmount() {
        testLogic.updatePledgeAmount(123);
        assertEquals(testLogic.getCurrentPledgePerWeek(), 123, 0.01);
    }

    @Test
    public void getCurrentPledgePerWeek() {
        assertEquals(testLogic.getCurrentPledgePerWeek(), 123, 0.01);
    }

    @Test
    public void getCurrentPledgePerDay() {
        assertEquals(testLogic.getCurrentPledgePerDay(), (float)123/7, 0.01);
    }

    @Test
    public void getCurrentPlanCO2PerDay() {
        testLogic.updateCurrentPlan(newPlan);
        float currentCo2 = Calculator.calculateCO2ePerDay(newPlan);
        assertEquals(testLogic.getCurrentPlanCO2PerDay(), currentCo2,0.01);
    }

    @Test
    public void getCurrentPlanCO2PerWeek() {
        float currentCo2 = Calculator.calculateCO2ePerDay(testPlan);
        currentCo2 = currentCo2*7/1000;
        assertNotEquals(testLogic.getCurrentPlanCO2PerWeek(), currentCo2,0.01);
    }


    @Test
    public void updateCurrentPlanName() {
        testLogic.updateCurrentPlanName("AAAA");
        assertNotEquals(testLogic.getUsersCurrentPlanName(), "aaaa");
    }

    @Test
    public void getUsersCurrentPlanName() {
        testLogic.updateCurrentPlanName("AAAA");
        assertEquals(testLogic.getUsersCurrentPlanName(), "AAAA");
    }


}