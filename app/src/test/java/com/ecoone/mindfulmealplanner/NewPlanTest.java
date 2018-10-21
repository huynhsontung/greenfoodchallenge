package com.ecoone.mindfulmealplanner;

import org.junit.Test;

import static org.junit.Assert.*;

public class NewPlanTest {

    public Plan testPlan = new Plan() {
        {
            beef = 20;
            pork = 49;
            chicken = 49;
            fish = 40;
            eggs = 20;
            beans = 20;
            vegetables = 100;
        }
    };

    public NewPlan testNewPlan = new NewPlan(testPlan, "female");

    @Test
    public void suggestPlan() {
        assertEquals(testNewPlan.suggestPlan(), testPlan);
    }

    @Test
    public void getScaledPlan() {
    }
}