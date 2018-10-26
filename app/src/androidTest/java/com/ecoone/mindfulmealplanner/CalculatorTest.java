package com.ecoone.mindfulmealplanner;

import com.ecoone.mindfulmealplanner.db.Plan;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
    public void testCalculateCO2ePerYear() {
        Calculator myCalculator = new Calculator();
        float getCO2e = myCalculator.calculateCO2ePerYear(testPlan);

        float getCO2eManually = 0;
        getCO2eManually += testPlan.beef * 27;
        getCO2eManually += testPlan.pork * 12.1;
        getCO2eManually += testPlan.chicken * 6.9;
        getCO2eManually += testPlan.fish * 6.1;
        getCO2eManually += testPlan.eggs * 4.8;
        getCO2eManually += testPlan.beans * 2;
        getCO2eManually += testPlan.vegetables * 2;

        getCO2eManually *= 365;
        getCO2eManually /= 1000000;

        assertEquals(getCO2e, getCO2eManually, 1);

    }

    @Test
    public void testCalculateCO2ePerDay() {
        Calculator myCalculator = new Calculator();
        float getCO2e = myCalculator.calculateCO2ePerDay(testPlan);

        float getCO2eManually = 0;
        getCO2eManually += testPlan.beef * 27;
        getCO2eManually += testPlan.pork * 12.1;
        getCO2eManually += testPlan.chicken * 6.9;
        getCO2eManually += testPlan.fish * 6.1;
        getCO2eManually += testPlan.eggs * 4.8;
        getCO2eManually += testPlan.beans * 2;
        getCO2eManually += testPlan.vegetables * 2;

        assertEquals(getCO2e, getCO2eManually, 1);

    }

    @Test
    public void testComparePlan() {
        Calculator myCalculator = new Calculator();
        float testCO2e = 2;
        float getCO2eTestPlan = myCalculator.calculateCO2ePerYear(testPlan);
        testCO2e -= getCO2eTestPlan;
        float getComparison = myCalculator.comparePlan(2, testPlan);
        //assert(getComparison == testCO2e);
        assertEquals(testCO2e, getComparison, 1); // Delta denotes max loss in precision allowed.
    }

    @Test
    public void testSumCO2ePerDayPlanList(){
        Calculator myCalculator = new Calculator();

        List<Plan> testList = new ArrayList<>();
        testList.add(testPlan);
        testList.add(testPlan);

        float getSumManually = myCalculator.calculateCO2ePerDay(testPlan);
        getSumManually *= 2;

        float getSum = myCalculator.sumCO2ePerDayPlanList(testList);

        assertEquals(getSumManually, getSum, 1);
    }

    @Test
    public void testSumServings() {
        Calculator myCalculator = new Calculator();
        float getServing = myCalculator.sumServings(testPlan);
        //assert(getServing == 400);
        assertEquals(getServing, 400, 0);
    }

    @Test
    public void testGetScalingFactor() {
        Calculator myCalculator = new Calculator();
        float testScalingFactorM = myCalculator.getScalingFactor(700, "male");
        //assert(testScalingFactorM == 2);
        assertEquals(testScalingFactorM, 2, 0.1);

        float testScalingFactorF = myCalculator.getScalingFactor(500, "female");
        //assert(testScalingFactorF == 2);
        assertEquals(testScalingFactorF, 2, 0.1);
    }

    @Test
    public void testCalculateVancouver() {
        Calculator myCalculator = new Calculator();
        float testCO2e = myCalculator.calculateCO2ePerYear(testPlan);

        float testVancouverCalculation = myCalculator.calculateVancouver(testPlan);

        testCO2e = 2460000 * testCO2e;
        //assert(testVancouverCalculation == testCO2e);
        assertEquals(testVancouverCalculation, testCO2e, 1);
    }

    @Test
    public void testUsePlanVancouver() {
        float avgCO2eVancouver = (float)1.5;
        float manualCalculationVancouver = (float) (1.5 * 2460000);

        Calculator myCalculator = new Calculator();

        float getPlanCO2e = myCalculator.calculateVancouver(testPlan);

        manualCalculationVancouver -= getPlanCO2e;
        float getNewCO2eTest = myCalculator.usePlanVancouver(testPlan);

        assertEquals(manualCalculationVancouver, getNewCO2eTest, 1);
    }

    @Test
    public void testCalculateCO2eEachFood(){
        float[] testCO2eAmount = new float[7];
        Calculator myCalculator = new Calculator();

        testCO2eAmount = myCalculator.calculateCO2eEachFood(testPlan);

        assertEquals(50*27, testCO2eAmount[0],0);
        assertEquals(75*12.1, testCO2eAmount[1],0.1);
        assertEquals(50*6.9,testCO2eAmount[2], 0.1);
        assertEquals(50*6.1, testCO2eAmount[3],0.1);
        assertEquals(50*4.8, testCO2eAmount[4], 0.1);
        assertEquals(25*2, testCO2eAmount[5], 0.1);
        assertEquals(100*2, testCO2eAmount[6], 0.1);
    }

    @Test
    public void testCalculateSavingsInKm(){
        Calculator myCalculator = new Calculator();
        float testCO2eManually = 2;

        float getSavings = myCalculator.calculateSavingsInKm(2);

        testCO2eManually *= 1000000;
        testCO2eManually /= 178;

        assertEquals(testCO2eManually, getSavings, 1);
    }
}