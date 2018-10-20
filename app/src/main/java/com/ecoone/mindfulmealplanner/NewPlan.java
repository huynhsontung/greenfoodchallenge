package com.ecoone.mindfulmealplanner;

import java.util.*;

public class NewPlan {
    private Plan usersCurrentPlan;
    private Plan newPlan;
    public Plan bestPlan;
    private String usersGender;
    private float bestCO2e;
    private float newCO2e = 0;
    int numIngredients = 7;
    Calculator myCalculator = new Calculator();
    private ArrayList<Integer> recommendedValues = new ArrayList<Integer>();

    public NewPlan(Plan plan, String gender) {
        usersCurrentPlan = plan;
        newPlan = plan;
        usersGender = gender;
        bestPlan(gender);
        //spawnRecommendedValues(gender);
    }

    public Plan suggestPlan() {
        float currentCO2e = myCalculator.calculateCO2e(usersCurrentPlan);
        float goalCO2e = currentCO2e * (float)0.9;
        float usersDailyServing = calculateDailyServing(usersCurrentPlan);
        float scaleFactor = myCalculator.getScalingFactor(usersDailyServing, usersGender);
        bestCO2e = myCalculator.calculateCO2e(bestPlan);
        scalePlan(scaleFactor);
        System.out.println("Current Plan: " + usersCurrentPlan.beef + " " + usersCurrentPlan.pork);
        System.out.println("Goal CO2e = " + goalCO2e);
        System.out.println("Current CO2e: " + currentCO2e + " Best CO2e: " + bestCO2e);
        if(currentCO2e <= bestCO2e) {
            // Users' plan is better than out recommended plan; no new plan suggested
        }
        else {
            for (int i = 0; i < numIngredients; i++) {
                //float co2eBeforeChange = myCalculator.calculateCO2e(newPlan);
                adjustNewPlan(i);
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

    public void adjustNewPlan(int ingredientIndex) {
        float gramsRemovedFromIngregient;
        switch(ingredientIndex) {
            case 0:
                gramsRemovedFromIngregient = newPlan.beef - bestPlan.beef;
                newPlan.beef = bestPlan.beef;
                newPlan.vegetables = newPlan.vegetables + (float)(gramsRemovedFromIngregient*0.7);
                newPlan.beans = newPlan.beans + (float)(gramsRemovedFromIngregient*0.3);
                newCO2e = myCalculator.calculateCO2e(newPlan);
                break;
            case 1:
                gramsRemovedFromIngregient = newPlan.beef - bestPlan.beef;
                newPlan.pork = bestPlan.pork;
                newPlan.vegetables = newPlan.vegetables + (float)(gramsRemovedFromIngregient*0.7);
                newPlan.beans = newPlan.beans + (float)(gramsRemovedFromIngregient*0.3);
                newCO2e = myCalculator.calculateCO2e(newPlan);
                break;
            case 2:
                gramsRemovedFromIngregient = newPlan.chicken - bestPlan.chicken;
                newPlan.chicken = bestPlan.chicken;
                newPlan.vegetables = newPlan.vegetables + (float)(gramsRemovedFromIngregient*0.7);
                newPlan.beans = newPlan.beans + (float)(gramsRemovedFromIngregient*0.3);
                newCO2e = myCalculator.calculateCO2e(newPlan);
                break;
            case 3:
                gramsRemovedFromIngregient = newPlan.fish - bestPlan.fish;
                newPlan.fish = bestPlan.fish;
                newPlan.vegetables = newPlan.vegetables + (float)(gramsRemovedFromIngregient*0.7);
                newPlan.beans = newPlan.beans + (float)(gramsRemovedFromIngregient*0.3);
                newCO2e = myCalculator.calculateCO2e(newPlan);
                break;
            case 4:
                gramsRemovedFromIngregient = newPlan.eggs - bestPlan.eggs;
                newPlan.eggs = bestPlan.eggs;
                newPlan.vegetables = newPlan.vegetables + (float)(gramsRemovedFromIngregient*0.7);
                newPlan.beans = newPlan.beans + (float)(gramsRemovedFromIngregient*0.3);
                newCO2e = myCalculator.calculateCO2e(newPlan);
                break;
            case 5:
                gramsRemovedFromIngregient = newPlan.beans - bestPlan.beans;
                newPlan.beans = bestPlan.beans;
                newPlan.vegetables = newPlan.vegetables + (float)(gramsRemovedFromIngregient*0.7);
                newPlan.beans = newPlan.beans + (float)(gramsRemovedFromIngregient*0.3);
                newCO2e = myCalculator.calculateCO2e(newPlan);
                break;
            case 6:
                gramsRemovedFromIngregient = newPlan.vegetables - bestPlan.vegetables;
                newPlan.vegetables = bestPlan.vegetables;
                newCO2e = myCalculator.calculateCO2e(newPlan);
                break;
            default:
                break;
        }
        printPlan(newPlan);
    }

    public void scalePlan(float scale) {
        bestPlan.beef = bestPlan.beef*scale;
        bestPlan.pork = bestPlan.pork*scale;
        bestPlan.chicken = bestPlan.chicken*scale;
        bestPlan.fish = bestPlan.fish*scale;
        bestPlan.eggs = bestPlan.eggs*scale;
        bestPlan.beans = bestPlan.beans*scale;
        bestPlan.vegetables = bestPlan.vegetables*scale;
    }

    public void bestPlan(String gender) {
        if(gender == "male") {
            bestPlan = new Plan() {
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
            bestPlan = new Plan() {
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

    public float calculateDailyServing(Plan plan) {
        return (plan.beef + plan.pork + plan.chicken + plan.fish + plan.eggs + plan.beans + plan.vegetables);
    }

    public void printPlan(Plan plan) {
        System.out.println(plan.beef+ " " + plan.pork + " " + plan.chicken + " " + plan.fish + " " + plan.eggs + " " + plan.beans + " " + plan.vegetables);
    }
 /*   public void spawnRecommendedValues(String gender) {
        if(gender.equals("male")) {
            recommendedValues.add(0,25);
            recommendedValues.add(1, 50);
            recommendedValues.add(2, 50);
            recommendedValues.add(3, 50);
            recommendedValues.add(4, 25);
            recommendedValues.add(5, 25);
            recommendedValues.add(6, 125);
        }
        if(gender.equals("female")) {
            recommendedValues.add(0,15);
            recommendedValues.add(1, 30);
            recommendedValues.add(2, 45);
            recommendedValues.add(3, 30);
            recommendedValues.add(4, 15);
            recommendedValues.add(5, 15);
            recommendedValues.add(6, 100);
        }
    }
*/

}
