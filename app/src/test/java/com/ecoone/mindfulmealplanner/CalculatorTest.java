package com.ecoone.mindfulmealplanner;

import com.ecoone.mindfulmealplanner.database.Plan;
import com.ecoone.mindfulmealplanner.tools.Calculator;
import com.ecoone.mindfulmealplanner.tools.NewPlan;

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
        float getCO2e = Calculator.calculateCO2ePerYear(testPlan);

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
        float getCO2e = Calculator.calculateCO2ePerDay(testPlan);

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
        float testCO2e = 2;
        float getCO2eTestPlan = Calculator.calculateCO2ePerYear(testPlan);
        testCO2e -= getCO2eTestPlan;
        float getComparison = Calculator.comparePlan(2, testPlan);
        //assert(getComparison == testCO2e);
        assertEquals(testCO2e, getComparison, 1); // Delta denotes max loss in precision allowed.
    }

    @Test
    public void testSumCO2ePerDayPlanList(){
        List<Plan> testList = new ArrayList<>();
        testList.add(testPlan);
        testList.add(testPlan);

        float getSumManually = Calculator.calculateCO2ePerDay(testPlan);
        getSumManually *= 2;

        float getSum = Calculator.sumCO2ePerDayPlanList(testList);

        assertEquals(getSumManually, getSum, 1);
    }

    @Test
    public void testSumServings() {
        float getServing = Calculator.sumServings(testPlan);
        //assert(getServing == 400);
        assertEquals(getServing, 400, 0);
    }

    @Test
    public void testGetScalingFactor() {
        float testScalingFactorM = Calculator.getScalingFactor(700, "male");
        //assert(testScalingFactorM == 2);
        assertEquals(testScalingFactorM, 2, 0.1);

        float testScalingFactorF = Calculator.getScalingFactor(500, "female");
        //assert(testScalingFactorF == 2);
        assertEquals(testScalingFactorF, 2, 0.1);
    }

    @Test
    public void testCalculateVancouver() {
        float testCO2e = Calculator.calculateCO2ePerYear(testPlan);

        float testVancouverCalculation = Calculator.calculateVancouver(testPlan);

        testCO2e = 2460000 * testCO2e;
        //assert(testVancouverCalculation == testCO2e);
        assertEquals(testVancouverCalculation, testCO2e, 1);
    }

    @Test
    public void testUsePlanVancouver() {
        /*float avgCO2eVancouver = (float)1.5;
        float manualCalculationVancouver = (float) (1.5 * 2460000);

        float getPlanCO2e = Calculator.calculateVancouver(testPlan);

        manualCalculationVancouver -= getPlanCO2e;
        float getNewCO2eTest = Calculator.usePlanVancouver(testPlan);

        assertEquals(manualCalculationVancouver, getNewCO2eTest, 1);*/
        NewPlan mNewPlan = new NewPlan(testPlan,"male");
        Plan improvedPlan = mNewPlan.suggestPlan();
        float vancouverSaved = Calculator.usePlanVancouver(improvedPlan,testPlan);
        float oldCo2 = Calculator.calculateVancouver(testPlan);
        float newCo2 = Calculator.calculateVancouver(improvedPlan);
        assertEquals(vancouverSaved, oldCo2 - newCo2, 0.001);

    }

    @Test
    public void testCalculateCO2eEachFood(){
        float[] testCO2eAmount = new float[7];

        testCO2eAmount = Calculator.calculateCO2eEachFood(testPlan);

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
        float testCO2eManually = 2;

        float getSavings = Calculator.calculateSavingsInKm(2);

        testCO2eManually *= 1000000;
        testCO2eManually /= 178;

        assertEquals(testCO2eManually, getSavings, 1);

        float testZero = Calculator.calculateSavingsInKm(0);
        assertEquals(0, testZero, 0);
    }

    @Test
    public void testCalculateTreesPlanted(){
        float testCO2eManually = 2;
        float savedCO2e = (float) 0.871;

        testCO2eManually = testCO2eManually / savedCO2e;
        testCO2eManually = Math.round(testCO2eManually);

        float getTreesPlanted = Calculator.calculateTreesPlanted(2);

        assertEquals(testCO2eManually, getTreesPlanted, 0);

        float testZeroCase = Calculator.calculateTreesPlanted(0);

        assertEquals(0, testZeroCase, 0);
    }


    @Test
    public void toPercentage() {
        float sum = 0;
        float[] testArr = new float[7];
        testArr[0] = 50;
        testArr[1] = 75;
        testArr[2] = 50;
        testArr[3] = 50;
        testArr[4] = 50;
        testArr[5] = 50;
        testArr[6] = 50;
        assertNotEquals(Calculator.toPercentage(testArr), testArr);


    }


}