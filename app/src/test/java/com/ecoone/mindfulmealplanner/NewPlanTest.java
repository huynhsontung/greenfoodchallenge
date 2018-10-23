package com.ecoone.mindfulmealplanner;

import com.ecoone.mindfulmealplanner.db.Plan;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class NewPlanTest {

    public Plan smallServingThanRecommended = new Plan() {
        {
            beef = 20;
            pork = 49;
            chicken = 49;
            fish = 40;
            eggs = 20;
            beans = 20;
            vegetables = 100;
        }
    };

    public Plan largeServingPlan = new Plan() {
        {
            beef = 100;
            pork = 100;
            chicken = 100;
            fish = 100;
            eggs = 100;
            beans = 100;
            vegetables = 100;
        }
    };

    public Plan edgeCasePlan = new Plan() {
        {
            beef = 300;
            pork = 0;
            chicken = 0;
            fish = 0;
            eggs = 0;
            beans = 0;
            vegetables = 0;
        }
    };

    public Plan edgeCasePlan2 = new Plan() {
        {
            beef = 0;
            pork = 0;
            chicken = 0;
            fish = 0;
            eggs = 0;
            beans = 300;
            vegetables = 0;
        }
    };

    public Plan edgeCasePlan3 = new Plan() {
        {
            beef = 150;
            pork = 150;
            chicken = 0;
            fish = 0;
            eggs = 0;
            beans = 0;
            vegetables = 0;
        }
    };


    public NewPlan smallPlanTest = new NewPlan(smallServingThanRecommended, "male");
    public NewPlan largePlanTest = new NewPlan(largeServingPlan, "male");
    public NewPlan edgeCaseTest = new NewPlan(edgeCasePlan, "male");
    public NewPlan edgeCaseTest2 = new NewPlan(edgeCasePlan2, "male");
    public NewPlan edgeCaseTest3 = new NewPlan(edgeCasePlan3, "male");

    @Test
    public void suggestPlan() {
        Plan suggestedPlan;
        Plan suggestedPlan2;
        suggestedPlan = smallPlanTest.suggestPlan();
        assertEquals(suggestedPlan.beef, smallServingThanRecommended.beef, 5);
        System.out.println("=========== new test ============");
        ArrayList<Plan> x = new ArrayList<Plan>();
        x.add(largeServingPlan); // hold previous plans, mimics history
        suggestedPlan2 = largePlanTest.suggestPlan();
        assertNotEquals(x.get(0).beef,suggestedPlan2.beef,5);
        assertNotEquals(x.get(0).vegetables,suggestedPlan2.vegetables);
        System.out.println("=========== new test ============");
        x.add(suggestedPlan2);
        NewPlan suggestTheSuggested = new NewPlan(suggestedPlan2,"male");
        suggestedPlan2 = suggestTheSuggested.suggestPlan();
        assertEquals(x.get(1).chicken, suggestedPlan2.chicken,5);
        assertNotEquals(x.get(1).beans, suggestedPlan2.beans,5);
        System.out.println("=========== new test ============");
        x.add(suggestedPlan2);
        NewPlan finalSuggestTest = new NewPlan(suggestedPlan2,"male");
        suggestedPlan2 = finalSuggestTest.suggestPlan();
        assertEquals(x.get(2).vegetables, suggestedPlan2.vegetables,5); // no more plans available to recommend
        System.out.println("=========== new test ============");
        edgeCasePlan = edgeCaseTest.suggestPlan();
        System.out.println("=========== new test ============");
        edgeCasePlan2  =edgeCaseTest2.suggestPlan();
        System.out.println("=========== new test ============");
        edgeCasePlan3 = edgeCaseTest3.suggestPlan()
;    }

    @Test
    public void getScaledPlan() {
    }

    @Test
    public void adjustNewPlan() {
    }

    @Test
    public void scalePlan() {
    }
}