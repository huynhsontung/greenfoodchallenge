package com.ecoone.mindfulmealplanner;

import java.util.*;

public class NewPlan {
    Calculator myCalculator = new Calculator();
    private Plan usersCurrentPlan;
    private float currentCO2e;
    // new plan will hold the refactored user plan
    // will equal the users current plan on construction
    private Plan newPlan;
    private float newCO2e = 0;
    // the plan that we suggest to everyone
    public Plan ourChosenRecommendedPlan;
    private float bestCO2e;
    private String usersGender;
    int numIngredients = 7;

    //Constructor
    public NewPlan(Plan plan, String gender) {
        usersCurrentPlan = plan;
        currentCO2e = myCalculator.calculateCO2e(usersCurrentPlan);
        newPlan = plan;
        usersGender = gender;
        ourChosenRecommendedPlan(gender);
    }

    //Returns a new plan based on the users current plan.
    //The new plan return could either be:
    //1. A new plan with adjusted values of ingredients
    //2. The same plan as before because the co2e of that plan is better than what we recommend
    public Plan suggestPlan() {
        float usersDailyServing = calculateDailyServing(usersCurrentPlan);
        System.out.println("Daily serving amount: " + usersDailyServing);
        float scaleFactor = myCalculator.getScalingFactor(usersDailyServing, usersGender);
        System.out.println("Scale factor: " + scaleFactor);
        bestCO2e = myCalculator.calculateCO2e(ourChosenRecommendedPlan);
        scalePlan(scaleFactor);
        System.out.print("Current Plan = ");
        printPlan(usersCurrentPlan);
        System.out.print("Our recommended plan = ");
        printPlan(ourChosenRecommendedPlan);
        System.out.println("Current CO2e: " + currentCO2e + " Best CO2e Obtainable: " + bestCO2e);

        if(currentCO2e <= bestCO2e) {
            System.out.println("Your plan is good already!");
            // Users' plan is better than out recommended plan; no new plan suggested
        }
        else if (scaleFactor <= 1) {
            System.out.println("You plan is good already!");
            // User's plan is already good
            // Can't recommend any lower; they will be under-eating
        }
        else {
            float goalCO2e = currentCO2e * (float)0.9;
            System.out.println("Goal CO2e = " + goalCO2e);
            for (int i = 0; i < numIngredients; i++) {
                //float co2eBeforeChange = myCalculator.calculateCO2e(newPlan);
                adjustNewPlan(i);
                System.out.println("New C02e = " + newCO2e);
                if(newCO2e <= goalCO2e) {
                    System.out.println("Newly adjusted plan with newCO2e = " + newCO2e + " was created on iteration " + i);
                    break;
                }
            }
            if(newCO2e > goalCO2e) {
                //Cannot reach goal unless serving size is decreased
            }
        }
        printPlan(newPlan);
        System.out.println("Goal reach: new co2e with new plan: " + myCalculator.calculateCO2e(newPlan));
        return newPlan;
    }

    //Takes in an ingredient index and adjusts the value of that indexed ingredient in our newPlan
    //Takes the grams from ingredient reduced, split it between vegetables and beans at a 70/30 ratio
    public void adjustNewPlan(int ingredientIndex) {
        float gramsRemovedFromIngregient;
        switch(ingredientIndex) {
            case 0:
                gramsRemovedFromIngregient = newPlan.beef - ourChosenRecommendedPlan.beef;
                if(newPlan.beef > ourChosenRecommendedPlan.beef) {
                    newPlan.beef = ourChosenRecommendedPlan.beef;
                    newPlan.vegetables = newPlan.vegetables + Math.round((float) (gramsRemovedFromIngregient * 0.7));
                    newPlan.beans = newPlan.beans + Math.round((float) (gramsRemovedFromIngregient * 0.3));
                }
                newCO2e = myCalculator.calculateCO2e(newPlan);
                break;
            case 1:
                if(newPlan.pork > ourChosenRecommendedPlan.pork) {
                    gramsRemovedFromIngregient = newPlan.pork - ourChosenRecommendedPlan.pork;
                    newPlan.pork = ourChosenRecommendedPlan.pork;
                    newPlan.vegetables = newPlan.vegetables + Math.round((float) (gramsRemovedFromIngregient * 0.7));
                    newPlan.beans = newPlan.beans + Math.round((float) (gramsRemovedFromIngregient * 0.3));
                }
                newCO2e = myCalculator.calculateCO2e(newPlan);
                break;
            case 2:
                if(newPlan.chicken > ourChosenRecommendedPlan.chicken) {
                    gramsRemovedFromIngregient = newPlan.chicken - ourChosenRecommendedPlan.chicken;
                    newPlan.chicken = ourChosenRecommendedPlan.chicken;
                    newPlan.vegetables = newPlan.vegetables + Math.round((float) (gramsRemovedFromIngregient * 0.7));
                    newPlan.beans = newPlan.beans + Math.round((float) (gramsRemovedFromIngregient * 0.3));
                }
                newCO2e = myCalculator.calculateCO2e(newPlan);
                break;
            case 3:
                if(newPlan.fish > ourChosenRecommendedPlan.fish) {
                    gramsRemovedFromIngregient = newPlan.fish - ourChosenRecommendedPlan.fish;
                    newPlan.fish = ourChosenRecommendedPlan.fish;
                    newPlan.vegetables = newPlan.vegetables + Math.round((float) (gramsRemovedFromIngregient * 0.7));
                    newPlan.beans = newPlan.beans + Math.round((float) (gramsRemovedFromIngregient * 0.3));
                }
                newCO2e = myCalculator.calculateCO2e(newPlan);
                break;
            case 4:
                if(newPlan.eggs > ourChosenRecommendedPlan.eggs) {
                    gramsRemovedFromIngregient = newPlan.eggs - ourChosenRecommendedPlan.eggs;
                    newPlan.eggs = ourChosenRecommendedPlan.eggs;
                    newPlan.vegetables = newPlan.vegetables + Math.round((float) (gramsRemovedFromIngregient * 0.7));
                    newPlan.beans = newPlan.beans + Math.round((float) (gramsRemovedFromIngregient * 0.3));
                }
                newCO2e = myCalculator.calculateCO2e(newPlan);
                break;
            case 5:
                if(newPlan.beans > ourChosenRecommendedPlan.beans) {
                    gramsRemovedFromIngregient = newPlan.beans - ourChosenRecommendedPlan.beans;
                    newPlan.beans = ourChosenRecommendedPlan.beans;
                    newPlan.vegetables = newPlan.vegetables + Math.round((float)(gramsRemovedFromIngregient * 0.7));
                }
                newCO2e = myCalculator.calculateCO2e(newPlan);
                break;
            case 6:
                if(newPlan.vegetables > ourChosenRecommendedPlan.vegetables) {
                    gramsRemovedFromIngregient = newPlan.vegetables - ourChosenRecommendedPlan.vegetables;
                    newPlan.vegetables = ourChosenRecommendedPlan.vegetables;
                }
                newCO2e = myCalculator.calculateCO2e(newPlan);
                break;
            default:
                break;
        }
        printPlan(newPlan);
    }

    //Adjusts our recommended plan ingredient values based on the scale factor,
    //which is needed to have recommended values for different serving sizes
    public void scalePlan(float scale) {
        ourChosenRecommendedPlan.beef = Math.round(ourChosenRecommendedPlan.beef*scale);
        ourChosenRecommendedPlan.pork = Math.round(ourChosenRecommendedPlan.pork*scale);
        ourChosenRecommendedPlan.chicken = Math.round(ourChosenRecommendedPlan.chicken*scale);
        ourChosenRecommendedPlan.fish = Math.round(ourChosenRecommendedPlan.fish*scale);
        ourChosenRecommendedPlan.eggs = Math.round(ourChosenRecommendedPlan.eggs*scale);
        ourChosenRecommendedPlan.beans = Math.round(ourChosenRecommendedPlan.beans*scale);
        ourChosenRecommendedPlan.vegetables = Math.round(ourChosenRecommendedPlan.vegetables*scale);
    }

    //Initializes our recommended plan with values based on gender
    public void ourChosenRecommendedPlan(String gender) {
        if(gender == "male") {
            ourChosenRecommendedPlan = new Plan() {
                {
                    beef = 25;
                    pork = 50;
                    chicken = 50;
                    fish = 50;
                    eggs = 25;
                    beans = 25;
                    vegetables = 125;
                }
            };
        }
        else if(gender == "female") {
            ourChosenRecommendedPlan = new Plan() {
                {
                    beef = 15;
                    pork = 30;
                    chicken = 45;
                    fish = 30;
                    eggs = 15;
                    beans = 15;
                    vegetables = 100;
                }
            };
        }
    }

    // Temporary until database is implemented
    public float calculateDailyServing(Plan plan) {
        return (plan.beef + plan.pork + plan.chicken + plan.fish + plan.eggs + plan.beans + plan.vegetables);
    }

    // For testing
    public void printPlan(Plan plan) {
        System.out.println(plan.beef+ " " + plan.pork + " " + plan.chicken + " " + plan.fish + " " + plan.eggs + " " + plan.beans + " " + plan.vegetables);
    }


}
