package com.ecoone.mindfulmealplanner;

public class Calculator {

    // CO2e per kilo of food consumed.
    private double co2Beef = 27;
    private double co2Pork = 12.1;
    private double co2Chicken = 6.9;
    private double co2Fish = 6.1;
    private double co2Eggs = 4.8;
    private double co2Beans = 2;
    private double co2Veggies = 2;

    // Parameters: Plan, so that co2e can be calculated.
    // Post: Calculates and returns co2e.
    float calculateCO2e(Plan myCurrentPlan){

        float myCO2e = 0;
        float co2ePerYear;
        int dailyServingTest = 500; // This exists as a test until the Plan class + database
                                    // is updated
        // Equation to calculate co2e: %Co2e * daily serving * co2e/1000
        myCO2e += myCurrentPlan.beef * dailyServingTest * co2Beef;
        myCO2e += myCurrentPlan.pork * dailyServingTest * co2Pork;
        myCO2e += myCurrentPlan.chicken * dailyServingTest * co2Chicken;
        myCO2e += myCurrentPlan.fish * dailyServingTest * co2Fish;
        myCO2e += myCurrentPlan.eggs * dailyServingTest * co2Eggs;
        myCO2e += myCurrentPlan.beans * dailyServingTest * co2Beans;
        myCO2e += myCurrentPlan.vegetables * dailyServingTest * co2Veggies;

        // This current answer will give you grams. Convert to metric tonnes.
        co2ePerYear = myCO2e * 365;
        co2ePerYear = co2ePerYear / 1000000;

        return co2ePerYear;
    }

    double comparePlan(double previousCO2e, Plan newPlan){

        double getNewCO2e = calculateCO2e(newPlan);
        double differenceCO2e = previousCO2e - getNewCO2e;

        // Note: difference might be negative if new plan is worse than old plan.
        return differenceCO2e;
    }

}
