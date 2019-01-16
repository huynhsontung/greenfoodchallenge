package com.ecoone.mindfulmealplanner.tools;

import com.ecoone.mindfulmealplanner.database.Plan;

public class NewPlan {
    private Plan usersCurrentPlan;
    private float currentCO2e;
    private float currentCO2eAfterAdjustment;
    // new plan will hold the refactored user plan
    // will equal the users current plan on construction
    private Plan newPlan = new Plan();
    private float newCO2e = 0;
    // the plan that we suggest to everyone
    public Plan ourChosenRecommendedPlan;
    private float bestCO2e;
    private String usersGender;
    int numIngredients = 7;
    int adjustmentFlag;

    //Constructor
    public NewPlan(Plan plan, String gender) {
        adjustmentFlag = 0;
        usersCurrentPlan = plan;
        copyPlan(plan);
        checkRelativeSize(newPlan);
        currentCO2e = Calculator.calculateCO2ePerYear(usersCurrentPlan);
        currentCO2eAfterAdjustment = Calculator.calculateCO2ePerYear(newPlan);
        usersGender = gender;
        ourChosenRecommendedPlan(gender);
    }

    //Returns a new plan based on the users current plan.
    //The new plan return could either be:
    //1. A new plan with adjusted values of ingredients
    //2. The same plan as before because the co2e of that plan is better than what we recommend
    public Plan suggestPlan() {
        float usersDailyServing = calculateDailyServing(newPlan);
        float scaleFactor = Calculator.getScalingFactor(usersDailyServing, usersGender);
        bestCO2e = Calculator.calculateCO2ePerYear(ourChosenRecommendedPlan);
        System.out.println("before scale with scale factor: " + scaleFactor);
        printPlan(ourChosenRecommendedPlan);
        scaleRecommendedPlan(scaleFactor);
        System.out.println("Recommended plan: ");
        printPlan(ourChosenRecommendedPlan);
        if (currentCO2e <= bestCO2e) {
            if (adjustmentFlag == 0) {
                return newPlan;
                // Users' plan is better than our recommended plan and relative size was fine; no new plan suggested
                // return newPlan, which is a copy of the users current plan (avoids returning the plan that is passed in)
            }
            else if (adjustmentFlag == 1 && currentCO2eAfterAdjustment <= bestCO2e) {
                return newPlan;
                // Users plan was fine, but we changed relative size of the plan
            }
            else {
                return usersCurrentPlan;
                // Relative size changed but made co2 higher than before, return original plan
            }
        }
        else {
            float goalCO2e = currentCO2eAfterAdjustment * (float)0.9;
            for (int i = 0; i < numIngredients; i++) {
                adjustNewPlan(i);
                if(newCO2e <= goalCO2e) {
                    break;
                }
            }
            if(newCO2e > goalCO2e) {
                return newPlan;
                //Cannot reach goal unless serving size is decreased
                //the best plan possible is suggested, but that best plan wont be at the goal CO2
            }
        }
        return newPlan;
    }

    //Takes in an ingredient index and adjusts the value of that indexed ingredient in our newPlan
    // i.e index 0 = beef, index 1 = pork, etc..
    //Takes the grams from ingredient reduced, split it between vegetables and beans at a 70/30 ratio
    public void adjustNewPlan(int ingredientIndex) {
        float gramsRemovedFromIngredient;
        switch(ingredientIndex) {
            case 0:
                gramsRemovedFromIngredient = newPlan.beef - ourChosenRecommendedPlan.beef;
                if(newPlan.beef > ourChosenRecommendedPlan.beef) {
                    newPlan.beef = ourChosenRecommendedPlan.beef;
                    newPlan.vegetables = newPlan.vegetables + Math.round((float) (gramsRemovedFromIngredient * 0.7));
                    newPlan.beans = newPlan.beans + Math.round((float) (gramsRemovedFromIngredient * 0.3));
                }
                newCO2e = Calculator.calculateCO2ePerYear(newPlan);
                break;
            case 1:
                if(newPlan.pork > ourChosenRecommendedPlan.pork) {
                    gramsRemovedFromIngredient = newPlan.pork - ourChosenRecommendedPlan.pork;
                    newPlan.pork = ourChosenRecommendedPlan.pork;
                    newPlan.vegetables = newPlan.vegetables + Math.round((float) (gramsRemovedFromIngredient * 0.7));
                    newPlan.beans = newPlan.beans + Math.round((float) (gramsRemovedFromIngredient * 0.3));
                }
                newCO2e = Calculator.calculateCO2ePerYear(newPlan);
                break;
            case 2:
                if(newPlan.chicken > ourChosenRecommendedPlan.chicken) {
                    gramsRemovedFromIngredient = newPlan.chicken - ourChosenRecommendedPlan.chicken;
                    newPlan.chicken = ourChosenRecommendedPlan.chicken;
                    newPlan.vegetables = newPlan.vegetables + Math.round((float) (gramsRemovedFromIngredient * 0.7));
                    newPlan.beans = newPlan.beans + Math.round((float) (gramsRemovedFromIngredient * 0.3));
                }
                newCO2e = Calculator.calculateCO2ePerYear(newPlan);
                break;
            case 3:
                if(newPlan.fish > ourChosenRecommendedPlan.fish) {
                    gramsRemovedFromIngredient = newPlan.fish - ourChosenRecommendedPlan.fish;
                    newPlan.fish = ourChosenRecommendedPlan.fish;
                    newPlan.vegetables = newPlan.vegetables + Math.round((float) (gramsRemovedFromIngredient * 0.7));
                    newPlan.beans = newPlan.beans + Math.round((float) (gramsRemovedFromIngredient * 0.3));
                }
                newCO2e = Calculator.calculateCO2ePerYear(newPlan);
                break;
            case 4:
                if(newPlan.eggs > ourChosenRecommendedPlan.eggs) {
                    gramsRemovedFromIngredient = newPlan.eggs - ourChosenRecommendedPlan.eggs;
                    newPlan.eggs = ourChosenRecommendedPlan.eggs;
                    newPlan.vegetables = newPlan.vegetables + Math.round((float) (gramsRemovedFromIngredient * 0.7));
                    newPlan.beans = newPlan.beans + Math.round((float) (gramsRemovedFromIngredient * 0.3));
                }
                newCO2e = Calculator.calculateCO2ePerYear(newPlan);
                break;
            case 5:
                if(newPlan.beans > ourChosenRecommendedPlan.beans) {
                    gramsRemovedFromIngredient = newPlan.beans - ourChosenRecommendedPlan.beans;
                    newPlan.beans = ourChosenRecommendedPlan.beans;
                    newPlan.vegetables = newPlan.vegetables + Math.round((float) (gramsRemovedFromIngredient));
                }
                newCO2e = Calculator.calculateCO2ePerYear(newPlan);
                break;
            default:
                break;
        }
    }

    //Adjusts our recommended plan ingredient values based on the scale factor,
    //which is needed to have recommended values for different serving sizes
    public void scaleRecommendedPlan(float scale) {
        ourChosenRecommendedPlan.beef = Math.round(ourChosenRecommendedPlan.beef * scale);
        ourChosenRecommendedPlan.pork = Math.round(ourChosenRecommendedPlan.pork * scale);
        ourChosenRecommendedPlan.chicken = Math.round(ourChosenRecommendedPlan.chicken * scale);
        ourChosenRecommendedPlan.fish = Math.round(ourChosenRecommendedPlan.fish * scale);
        ourChosenRecommendedPlan.eggs = Math.round(ourChosenRecommendedPlan.eggs * scale);
        ourChosenRecommendedPlan.beans = Math.round(ourChosenRecommendedPlan.beans * scale);
        ourChosenRecommendedPlan.vegetables = Math.round(ourChosenRecommendedPlan.vegetables * scale);
    }

    //Initializes our recommended plan with values based on gender
    public void ourChosenRecommendedPlan(String gender) {
        ourChosenRecommendedPlan = new Plan();
        if (gender.equals("male")) {
            ourChosenRecommendedPlan.beef = 25;
            ourChosenRecommendedPlan.pork = 50;
            ourChosenRecommendedPlan.chicken = 50;
            ourChosenRecommendedPlan.fish = 50;
            ourChosenRecommendedPlan.eggs = 25;
            ourChosenRecommendedPlan.beans = 25;
            ourChosenRecommendedPlan.vegetables = 125;

        } else {
            ourChosenRecommendedPlan.beef = 15;
            ourChosenRecommendedPlan.pork = 30;
            ourChosenRecommendedPlan.chicken = 45;
            ourChosenRecommendedPlan.fish = 30;
            ourChosenRecommendedPlan.eggs = 15;
            ourChosenRecommendedPlan.beans = 15;
            ourChosenRecommendedPlan.vegetables = 100;
        }
    }

    //Adjusts the sizes of each ingredient if an ingredient is found to have
    //more than 80% of the total daily serving
    //adjustmentFlag is raised for suggestPlan to function properly
    public void checkRelativeSize(Plan newPlan) {
        float dailyServing = calculateDailyServing(newPlan);
        if ((newPlan.beef) / dailyServing >= 0.8) {
            newPlan.pork = Math.round(newPlan.pork + (newPlan.beef / 5));
            newPlan.chicken = Math.round(newPlan.chicken + (newPlan.beef / 5));
            newPlan.fish = Math.round(newPlan.fish + (newPlan.beef / 5));
            newPlan.eggs = Math.round(newPlan.eggs + (newPlan.beef / 5));
            newPlan.beef = Math.round(newPlan.beef / 5);
            adjustmentFlag = 1;
        } else if ((newPlan.pork / dailyServing) >= 0.8) {
            newPlan.chicken = Math.round(newPlan.chicken + (newPlan.pork / 4));
            newPlan.fish = Math.round(newPlan.fish + (newPlan.pork / 4));
            newPlan.eggs = Math.round(newPlan.eggs + (newPlan.pork / 4));
            newPlan.pork = Math.round(newPlan.pork / 4);
            adjustmentFlag = 1;
        } else if ((newPlan.chicken / dailyServing) >= 0.8) {
            newPlan.fish = Math.round(newPlan.fish + (newPlan.chicken / 3));
            newPlan.eggs = Math.round(newPlan.eggs + (newPlan.chicken / 3));
            newPlan.chicken = Math.round(newPlan.chicken / 3);
            adjustmentFlag = 1;
        } else if ((newPlan.fish / dailyServing) >= 0.8) {
            newPlan.chicken = Math.round(newPlan.chicken + (newPlan.fish / 3));
            newPlan.eggs = Math.round(newPlan.eggs + (newPlan.fish / 3));
            newPlan.fish = Math.round(newPlan.fish / 3);
            adjustmentFlag = 1;
        } else if ((newPlan.eggs / dailyServing) >= 0.8) {
            newPlan.chicken = Math.round(newPlan.chicken + (newPlan.eggs / 3));
            newPlan.fish = Math.round(newPlan.fish + (newPlan.eggs / 3));
            newPlan.eggs = Math.round(newPlan.eggs / 3);
            adjustmentFlag = 1;
        } else if ((newPlan.beans / dailyServing) >= 0.8) {
            newPlan.beans = Math.round(newPlan.beans / 3);
            newPlan.vegetables = Math.round(newPlan.vegetables + (newPlan.beans * 2));
            adjustmentFlag = 1;
        }

    }

    // Temporary until database is implemented(DataBase was completed)
    public float calculateDailyServing(Plan plan) {
        return (plan.beef + plan.pork + plan.chicken + plan.fish + plan.eggs + plan.beans + plan.vegetables);
    }

    // For component_card_layout_about
    public void printPlan(Plan plan) {
        System.out.println(plan.beef + " " + plan.pork + " " + plan.chicken + " " + plan.fish + " " + plan.eggs + " " + plan.beans + " " + plan.vegetables);
    }

    //Copies the values from the users previous plan into newPlan
    private void copyPlan(Plan usersPlan) {
        newPlan.beef = usersPlan.beef;
        newPlan.pork = usersPlan.pork;
        newPlan.chicken = usersPlan.chicken;
        newPlan.fish = usersPlan.fish;
        newPlan.eggs = usersPlan.eggs;
        newPlan.beans = usersPlan.beans;
        newPlan.vegetables = usersPlan.vegetables;
    }

}
