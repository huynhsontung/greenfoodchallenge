package com.ecoone.mindfulmealplanner.Pledge;

import com.ecoone.mindfulmealplanner.DB.Plan;
import com.ecoone.mindfulmealplanner.Tool.Calculator;

import org.junit.Test;

import static org.junit.Assert.*;

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

    PledgeLogic testLogic = new PledgeLogic(testPlan,50);

    @Test
    public void getUsersCurrentPlan() {
        assertEquals(testLogic.getUsersCurrentPlan().beef, testPlan.beef, 0.01);
        assertEquals(testLogic.getUsersCurrentPlan().eggs, testPlan.eggs, 0.01);
        assertEquals(testLogic.getUsersCurrentPlan().vegetables, testPlan.vegetables, 0.01);
    }

    @Test
    public void getCurrentPledgePerWeek() {
        float testCo2perweek = Calculator.calculateCO2ePerDay(testPlan);
        testCo2perweek = (testCo2perweek*7)/1000;
        assertEquals(testLogic.getCurrentPledgePerWeek(), 50, 0.01);
    }

    @Test
    public void getCurrentPledgePerDay() {
        assertEquals(testLogic.getCurrentPledgePerDay(), (float)50/7, 0.01);
    }

    @Test
    public void getCurrentPlanCO2PerDay() {
        float currentCo2 = Calculator.calculateCO2ePerDay(testPlan);
        assertEquals(testLogic.getCurrentPlanCO2PerDay(), currentCo2,0.01);
    }

    @Test
    public void getCurrentPlanCO2PerWeek() {
        float currentCo2 = Calculator.calculateCO2ePerDay(testPlan);
        currentCo2 = currentCo2*7/1000;
        assertEquals(testLogic.getCurrentPlanCO2PerWeek(), currentCo2,0.01);
    }

    @Test
    public void updateCurrentPlan() {
        testLogic.updateCurrentPlan(newPlan);
        assertNotEquals(testLogic.getUsersCurrentPlan().beans, testPlan.beans);
        assertNotEquals(testLogic.getCurrentPlanCO2PerDay(), Calculator.calculateCO2ePerDay(testPlan));
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

    @Test
    public void updatePledgeAmount() {
        testLogic.updatePledgeAmount(123);
        assertEquals(testLogic.getCurrentPledgePerWeek(), 123, 0.01);
    }
}