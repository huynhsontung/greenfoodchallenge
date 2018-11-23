package com.ecoone.mindfulmealplanner.tools;

import com.ecoone.mindfulmealplanner.database.Plan;

import java.util.List;


public abstract class Calculator {

    // CO2e per kilo of Food consumed.
    private static final float co2Beef = (float) 27.0;
    private static final float co2Pork = (float) 12.1;
    private static final float co2Chicken = (float) 6.9;
    private static final float co2Fish = (float) 6.1;
    private static final float co2Eggs = (float) 4.8;
    private static final float co2Beans = 2;
    private static final float co2Veggies = 2;

    private static final int populationVancouver = 2460000;
    // Metric tonnes of co2e per capita according to the pdf.
    private static final float tCO2ePerCapita = (float) 1.5;

    // Parameters: Array of float.
    // Post: Calculates and returns percentage of each entry in an array.
    public static float[] toPercentage(float[] dataArray) {
        float sum = 0;
        for (float entry : dataArray) {
            sum += entry;
        }
        float result[] = new float[dataArray.length];
        for (int i = 0; i < dataArray.length; i++) {
            result[i] = dataArray[i] / sum * 100;
        }
        return result;
    }

    // Parameters: Plan, so that co2e can be calculated.
    // Post: Calculates and returns co2e.
    public static float calculateCO2ePerYear(Plan myCurrentPlan) {

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
    public static float calculateCO2ePerDay(Plan myCurrentPlan) {

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
    public static float sumCO2ePerDayPlanList(List<Plan> myPlanList) {

        float sumListPerDay = 0;

        for (int i = 0; i < myPlanList.size(); i++) {
            // Get from plan list, calculate its CO2e per day, add to running sum.
            sumListPerDay += calculateCO2ePerDay(myPlanList.get(i));
        }

        return sumListPerDay;
    }

    // Parameters: Plan
    // Post: A array with CO2e amount for each field
    public static float[] calculateCO2eEachFood(Plan myCurrentPlan) {
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
    public static float comparePlan(float previousCO2e, Plan newPlan) {

        float getNewCO2e = calculateCO2ePerYear(newPlan);

        // Note: difference might be negative if new plan is worse than old plan.
        return (previousCO2e - getNewCO2e);
    }

    // Parameters: User's plan.
    // Post: Sums up all the serving sizes of all Food types. Returns total daily serving.
    public static float sumServings(Plan myPlan) {

        return (myPlan.beef + myPlan.pork + myPlan.chicken + myPlan.fish +
                myPlan.eggs + myPlan.beans + myPlan.vegetables);
    }

    // Parameters: The daily serving of the user, their gender in string.
    // Post: Calculates the scaling factor, used to come up with the plan suggestion.
    public static float getScalingFactor(float currentDailyServing, String gender) {

        final int recommendedServingMen = 350;
        final int recommendedServingWomen = 250;

        float scalingFactor = 0;

        if (gender.equals("male")) {
            scalingFactor = currentDailyServing / recommendedServingMen;
        } else if (gender.equals("female")) {
            scalingFactor = currentDailyServing / recommendedServingWomen;
        }
        return scalingFactor;
    }

    // Parameters: User's current plan
    // Post: Calculates the grand total CO2e if everyone in Vancouver used user's plan
    public static float calculateVancouver(Plan myPlan) {

        float getCO2e = calculateCO2ePerYear(myPlan);
        float grandTotalCO2e = getCO2e * populationVancouver;

        return grandTotalCO2e;
    }

    // Parameters: User's current plan
    // Post: Calculates how many metric tonnes Vancouver would save if everyone used plan
    public static float usePlanVancouver(Plan newPlan, Plan oldPlan) {
        float oldPlanCO2ForVancouver = calculateVancouver(oldPlan);
        float newPlanCO2ForVancouver = calculateVancouver(newPlan);
        // old co2 should be greater than new co2
        float savingsIfEveryoneSwitchedPlans = oldPlanCO2ForVancouver - newPlanCO2ForVancouver;
        return savingsIfEveryoneSwitchedPlans;
        //float getNewTotalCO2e = calculateVancouver(newPlan);
        // float oldTotalVancouver = populationVancouver * tCO2ePerCapita;
        //return (float) (oldTotalVancouver - getNewTotalCO2e);

    }

    // Parameters: CO2eInTonnes
    // Post: Calculates how many km for driving saved.
    // Relevant information/ Sources:
    // 1 litre of gas = 2.3 kg CO2
    // Average Canadian vehicle = 2000L of gas per year
    // CO2 emissions (g/km) of a 2018 Toyota Corolla: 178 (Note this is pretty decent)
    // Source: https://www.nrcan.gc.ca/sites/www.nrcan.gc.ca/files/oee/pdf/transportation/tools/fuelratings/Model%20Year%202018%20Vehicle%20Tables.pdf
    public static float calculateSavingsInKm(float CO2eInTonnes) {

        if (CO2eInTonnes <= 0) {
            return 0;
        }

        float convertToGrams = CO2eInTonnes * 1000000;
        float gramsPerKmToyota = 178;


        float savingsInKm = convertToGrams / gramsPerKmToyota;
        return savingsInKm;
    }

    // Relevant information/sources:
    // Average mature tree consumes 48 pounds (approx 21772 grams) of CO2 per year.
    // If tree exists for 40 years, it will consume approximately 870,880 grams of CO2
    // Source: https://onetreeplanted.org/blogs/news/14245701-how-planting-trees-can-help-reduce-your-carbon-footprint
    public static float calculateTreesPlanted(float CO2eInTonnes) {

        float numberOfTrees;
        float co2ConsumedPerTree = (float) 0.871;
        numberOfTrees = CO2eInTonnes / co2ConsumedPerTree;

        numberOfTrees = Math.round(numberOfTrees); // So you don't plant a fraction of a tree
        return numberOfTrees;
    }

}
