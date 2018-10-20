package com.ecoone.mindfulmealplanner;

import org.junit.Test;

import static org.junit.Assert.*;

public class NewPlanTest {

    public Plan testPlan = new Plan() {
        {
            beef = 73;
            pork = 80;
            chicken = 30;
            fish = 50;
            eggs = 50;
            beans = 50;
            vegetables = 125;
        }
    };

    public NewPlan testNewPlan = new NewPlan(testPlan, "male");

    @Test
    public void suggestPlan() {
        assertEquals(testNewPlan.suggestPlan(), testPlan);
    }

    @Test
    public void getScaledPlan() {
    }
}