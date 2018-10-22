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
    // Metric tonnes of co2e per capita according to the pdf.
    private final double tCO2ePerCapita = 1.5;


    // Parameters: Plan, so that co2e can be calculated.
    // Post: Calculates and returns co2e.
    float calculateCO2e(Plan myCurrentPlan){

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

    // Parameters: CO2e of previous plan, the new plan to be compared.
    // Post: Calculates CO2e of the new plan, then finds the difference.
    //       Note that this difference might be negative. In this case, the new plan produces MORE
    //       CO2e than the old.
    float comparePlan(float previousCO2e, Plan newPlan){

        float getNewCO2e = calculateCO2e(newPlan);

        // Note: difference might be negative if new plan is worse than old plan.
        return (previousCO2e - getNewCO2e);
    }

    // Parameters: User's plan.
    // Post: Sums up all the serving sizes of all food types. Returns total daily serving.
    float sumServings(Plan myPlan){

        return (myPlan.beef + myPlan.pork + myPlan.chicken + myPlan.fish +
                myPlan.eggs + myPlan.beans + myPlan.vegetables);
    }

    // Parameters: The daily serving of the user, their gender in string.
    // Post: Calculates the scaling factor, used to come up with the plan suggestion.
    float getScalingFactor(float currentDailyServing, String gender){

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
    float calculateVancouver(Plan myPlan){

        float getCO2e = calculateCO2e(myPlan);
        float grandTotalCO2e = getCO2e * populationVancouver;

        return grandTotalCO2e;
    }

    // Parameters: User's current plan
    // Post: Calculates how many metric tonnes Vancouver would save if everyone used plan
    float usePlanVancouver(Plan myPlan){

        float getNewTotalCO2e = calculateVancouver(myPlan);

        double oldTotalVancouver = populationVancouver * tCO2ePerCapita;

        return (float) (oldTotalVancouver - getNewTotalCO2e);
    }

}
