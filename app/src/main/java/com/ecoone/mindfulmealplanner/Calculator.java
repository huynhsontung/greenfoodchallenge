package com.ecoone.mindfulmealplanner;

import com.ecoone.mindfulmealplanner.db.Plan;

import java.util.List;


public class Calculator {

    // CO2e per kilo of food consumed.
    private static final float co2Beef = 27;
    private static final float co2Pork = (float) 12.1;
    private static final float co2Chicken = (float) 6.9;
    private static final float co2Fish = (float) 6.1;
    private static final float co2Eggs = (float) 4.8;
    private static final float co2Beans = 2;
    private static final float co2Veggies = 2;

    private static final int populationVancouver = 2460000;
    // Metric tonnes of co2e per capita according to the pdf.
    private static final float tCO2ePerCapita = (float) 1.5;


    // Parameters: Plan, so that co2e can be calculated.
    // Post: Calculates and returns co2e.
    public static float calculateCO2ePerYear(Plan myCurrentPlan){

        float myCO2e = 0;
        float co2ePerYear;

        // Equation to calculate co2e: %Co2e * daily serving * co2e/1000
        myCO2e += myCurrentPlan.beef * co2Beef;
        myCO2e += myCurrentPlan.pork * co2Pork;
        myCO2e += myCurrentPlan.chicken * co2Chicken;
        myCO2e += myCurrentPlan.fish * co2Fish;
        myCO2e += myCurrentPlan.eggs * co2Eggs;
        myCO2e += myCurrentPlan.beans * co2Beans;
        myCO2e += myCurrentPlan.vegetables * co2Veggies;

        // Per year
        co2ePerYear = myCO2e * 365;

        // This current answer will give you grams. Converting to metric tonnes.
        co2ePerYear = co2ePerYear / 1000000;

        return co2ePerYear;
    }

    // Param: Current plan.
    // Post: Calculates CO2e per day of given plan. *Note the answer is in grams*
    public static float calculateCO2ePerDay(Plan myCurrentPlan){

        float myCO2e = 0;
        float co2ePerDay;

        // Equation to calculate co2e: %Co2e * daily serving * co2e/1000
        myCO2e += myCurrentPlan.beef * co2Beef;
        myCO2e += myCurrentPlan.pork * co2Pork;
        myCO2e += myCurrentPlan.chicken * co2Chicken;
        myCO2e += myCurrentPlan.fish * co2Fish;
        myCO2e += myCurrentPlan.eggs * co2Eggs;
        myCO2e += myCurrentPlan.beans * co2Beans;
        myCO2e += myCurrentPlan.vegetables * co2Veggies;

        // Per DAY
        co2ePerDay = myCO2e;

        // This current answer will give you GRAMS.
        return co2ePerDay;
    }

    // Parameters: A list of plans
    // Post: Sums up the CO2e per day of the plans in the list. *Note the answer is in grams*
    public static float sumCO2ePerDayPlanList(List<Plan> myPlanList){

        float sumListPerDay = 0;

        for (int i = 0; i < myPlanList.size(); i++){
            // Get from plan list, calculate its CO2e per day, add to running sum.
            sumListPerDay += calculateCO2ePerDay(myPlanList.get(i));
        }

        return sumListPerDay;
    }

    // Parameters: Plan
    // Post: A array with CO2e amount for each field
    public static float[] calculateCO2eEachFood(Plan myCurrentPlan){
        float[] co2Amount = new float[7];
        co2Amount[0] = myCurrentPlan.beef * co2Beef;
        co2Amount[1] = myCurrentPlan.pork * co2Pork;
        co2Amount[2] = myCurrentPlan.chicken * co2Chicken;
        co2Amount[3] = myCurrentPlan.fish * co2Fish;
        co2Amount[4] = myCurrentPlan.eggs * co2Eggs;
        co2Amount[5] = myCurrentPlan.beans * co2Beans;
        co2Amount[6] = myCurrentPlan.vegetables * co2Veggies;
        return co2Amount;
    }

    // Parameters: CO2e of previous plan, the new plan to be compared.
    // Post: Calculates CO2e of the new plan, then finds the difference.
    //       Note that this difference might be negative. In this case, the new plan produces MORE
    //       CO2e than the old.
    public static float comparePlan(float previousCO2e, Plan newPlan){

        float getNewCO2e = calculateCO2ePerYear(newPlan);

        // Note: difference might be negative if new plan is worse than old plan.
        return (previousCO2e - getNewCO2e);
    }

    // Parameters: User's plan.
    // Post: Sums up all the serving sizes of all food types. Returns total daily serving.
    public static float sumServings(Plan myPlan){

        return (myPlan.beef + myPlan.pork + myPlan.chicken + myPlan.fish +
                myPlan.eggs + myPlan.beans + myPlan.vegetables);
    }

    // Parameters: The daily serving of the user, their gender in string.
    // Post: Calculates the scaling factor, used to come up with the plan suggestion.
    public static float getScalingFactor(float currentDailyServing, String gender){

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
    public static float calculateVancouver(Plan myPlan){

        float getCO2e = calculateCO2ePerYear(myPlan);
        float grandTotalCO2e = getCO2e * populationVancouver;

        return grandTotalCO2e;
    }

    // Parameters: User's current plan
    // Post: Calculates how many metric tonnes Vancouver would save if everyone used plan
    public static float usePlanVancouver(Plan myPlan){

        float getNewTotalCO2e = calculateVancouver(myPlan);

        float oldTotalVancouver = populationVancouver * tCO2ePerCapita;

        return (float) (oldTotalVancouver - getNewTotalCO2e);
    }

}
