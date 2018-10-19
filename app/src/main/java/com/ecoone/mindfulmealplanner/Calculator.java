package com.ecoone.mindfulmealplanner;

public class Calculator {

    // CO2e per kilo of food consumed.
    private final double co2Beef = 27;
    private final double co2Pork = 12.1;
    private final double co2Chicken = 6.9;
    private final double co2Fish = 6.1;
    private final double co2Eggs = 4.8;
    private final double co2Beans = 2;
    private final double co2Veggies = 2;

    private final int populationVancouver = 2460000;


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

        // This current answer will give you grams. Converting to metric tonnes.
        co2ePerYear = myCO2e * 365;
        co2ePerYear = co2ePerYear / 1000000;

        return co2ePerYear;
    }

    // Parameters: CO2e of previous plan, the new plan to be compared.
    // Post: Calculates CO2e of the new plan, then finds the difference.
    //       Note that this difference might be negative. In this case, the new plan produces MORE
    //       CO2e than the old.
    float comparePlan(float previousCO2e, Plan newPlan){

        float getNewCO2e = calculateCO2e(newPlan);
        float differenceCO2e = previousCO2e - getNewCO2e;

        // Note: difference might be negative if new plan is worse than old plan.
        return differenceCO2e;
    }

    // Parameters: The daily serving of the user, their gender in string.
    // Post: Calculates the scaling factor, used to come up with the plan suggestion.
    float getScalingFactor(int currentDailyServing, String gender){

        final int recommendedServingMen = 350;
        final int recommendedServingWomen = 250;

        float scalingFactor = 0;

        if(gender.equals("male")){
            scalingFactor = currentDailyServing / recommendedServingMen;
        }
        else if(gender.equals("female")){
            scalingFactor = currentDailyServing / recommendedServingWomen;
        }
        return scalingFactor;
    }

    // Parameters: User's current plan
    // Post: Calculates the grand total CO2e if everyone in Vancouver used user's plan
    float calculateSavingsVancouver(Plan myPlan){

        float getCO2e = calculateCO2e(myPlan);

        float grandTotalCO2e = getCO2e * populationVancouver;

        return grandTotalCO2e;
    }

}
