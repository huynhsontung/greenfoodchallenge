package com.ecoone.mindfulmealplanner;

import org.junit.Test;

import static org.junit.Assert.*;
public class CalculatorTest {

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

    @Test
    public void testCalculateCO2e() {
        Calculator myCalculator = new Calculator();
        float getCO2e = myCalculator.calculateCO2e(testPlan);

    }

    @Test
    public void testComparePlan() {
        Calculator myCalculator = new Calculator();
        float testCO2e = 2;
        float getCO2eTestPlan = myCalculator.calculateCO2e(testPlan);
        testCO2e -= getCO2eTestPlan;
        float getComparison = myCalculator.comparePlan(testCO2e, testPlan);
        assert(getComparison == testCO2e);
    }

    @Test
    public void testSumServings() {
        Calculator myCalculator = new Calculator();
        float getServing = myCalculator.sumServings(testPlan);
        assert(getServing == 400);
    }

    @Test
    public void testGetScalingFactor() {
        Calculator myCalculator = new Calculator();
        float testScalingFactorM = myCalculator.getScalingFactor(700, "male");
        assert(testScalingFactorM == 2);

        float testScalingFactorF = myCalculator.getScalingFactor(500, "female");
        assert(testScalingFactorF == 2);
    }

    @Test
    public void testCalculateVancouver() {
        Calculator myCalculator = new Calculator();
        float testCO2e = myCalculator.calculateCO2e(testPlan);

        float testVancouverCalculation = myCalculator.calculateVancouver(testPlan);

        testCO2e = 365 * testCO2e;
        assert(testVancouverCalculation == testCO2e);
    }

    @Test
    public void testUsePlanVancouver() {
    }
}