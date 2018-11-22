package com.ecoone.mindfulmealplanner;

import com.ecoone.mindfulmealplanner.database.Plan;
import com.ecoone.mindfulmealplanner.tools.NewPlan;

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

    public Plan edgeCasePlan4 = new Plan() {
        {
            beef = 0;
            pork = 300;
            chicken = 0;
            fish = 0;
            eggs = 0;
            beans = 0;
            vegetables = 0;
        }
    };

    public Plan edgeCasePlan5 = new Plan() {
        {
            beef = 0;
            pork = 0;
            chicken = 0;
            fish = 500;
            eggs = 0;
            beans = 0;
            vegetables = 0;
        }
    };

    public Plan edgeCasePlan6 = new Plan() {
        {
            beef = 0;
            pork = 0;
            chicken = 0;
            fish = 0;
            eggs = 300;
            beans = 0;
            vegetables = 0;
        }
    };

    public Plan edgeCasePlan7 = new Plan() {
        {
            beef = 0;
            pork = 0;
            chicken = 0;
            fish = 0;
            eggs = 0;
            beans = 0;
            vegetables = 300;
        }
    };
    public Plan edgeCasePlan8 = new Plan() {
        {
            beef = 0;
            pork = 0;
            chicken = 300;
            fish = 0;
            eggs = 0;
            beans = 0;
            vegetables = 0;
        }
    };


    public Plan bugPlan = new Plan() {
        {
            beef = 300;
            pork = 300;
            chicken = 300;
            fish = 300;
            eggs = 300;
            beans = 300;
            vegetables = 300;
        }
    };

    public Plan bugPlan2 = new Plan() {
        {
            beef = 139;
            pork = 43;
            chicken = 135;
            fish = 128;
            eggs = 69;
            beans = 116;
            vegetables = 165;
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
        ArrayList<Plan> x = new ArrayList<Plan>();
        suggestedPlan = smallPlanTest.suggestPlan();
        assertEquals(suggestedPlan.beef, smallServingThanRecommended.beef, 5);
        x.add(largeServingPlan); // hold previous plans, mimics history 0
        suggestedPlan2 = largePlanTest.suggestPlan();
        assertNotEquals(x.get(0).beef, suggestedPlan2.beef, 5);
        assertNotEquals(x.get(0).vegetables, suggestedPlan2.vegetables);
        x.add(suggestedPlan2); //original suggested 1
        NewPlan suggestTheSuggested = new NewPlan(suggestedPlan2, "male");
        suggestedPlan2 = suggestTheSuggested.suggestPlan();
        assertEquals(x.get(1).chicken, suggestedPlan2.chicken, 5); // only pork should have lowered
        assertNotEquals(x.get(1).beans, suggestedPlan2.beans, 5);
        x.add(suggestedPlan2); // suggested plan for the suggested 2
        NewPlan finalSuggestTest = new NewPlan(suggestedPlan2, "male");
        suggestedPlan2 = finalSuggestTest.suggestPlan();
        assertEquals(x.get(2).vegetables, suggestedPlan2.vegetables, 5); // no more plans available to recommend
        x.add(edgeCasePlan); // original edgeCasePlan 3
        x.add(edgeCasePlan2); // original edgeCasePlan 4
        x.add(edgeCasePlan3); // original edgeCasePlan 5
        edgeCasePlan = edgeCaseTest.suggestPlan();
        edgeCasePlan2 = edgeCaseTest2.suggestPlan();
        edgeCasePlan3 = edgeCaseTest3.suggestPlan();
        assertEquals(x.get(4).beef, edgeCasePlan2.beef, 5);
        assertNotEquals(x.get(5).vegetables, edgeCasePlan3.vegetables, 5);
        assertNotEquals(edgeCasePlan.beef, x.get(3).beef, 5);
        assertNotEquals(edgeCasePlan.pork, x.get(3).pork, 5);
        x.add(edgeCasePlan); // suggest version of edgecaseplan 6
        NewPlan suggestTheSuggested2 = new NewPlan(edgeCasePlan, "male");
        edgeCasePlan = suggestTheSuggested2.suggestPlan();
        assertEquals(x.get(6).beef, edgeCasePlan.beef, 5);
        assertNotEquals(x.get(6).pork, edgeCasePlan.pork, 5);

        /*NewPlan edge2 = new NewPlan(edgeCasePlan2,"female");
        Plan newEdge2 = edge2.suggestPlan();
        assertNotEquals((newEdge2.beans), edgeCasePlan2.beans);*/

        NewPlan edge3 = new NewPlan(edgeCasePlan3,"female");
        Plan newEdge3 = edge3.suggestPlan();
        assertNotEquals((newEdge3), edgeCasePlan3);

        NewPlan edge4 = new NewPlan(edgeCasePlan4,"female");
        Plan newEdge4 = edge4.suggestPlan();
        assertNotEquals((newEdge4.pork), edgeCasePlan4.pork);

        NewPlan edge5 = new NewPlan(edgeCasePlan5,"male");
        Plan newEdge5 = edge5.suggestPlan();
        assertNotEquals((newEdge5.fish), edgeCasePlan5.fish);

        NewPlan edge6 = new NewPlan(edgeCasePlan6,"male");
        Plan newEdge6 = edge6.suggestPlan();
        assertNotEquals((newEdge6.eggs), edgeCasePlan6.eggs);

        NewPlan edge7 = new NewPlan(edgeCasePlan7,"female");
        Plan newEdge7 = edge7.suggestPlan();
        assertEquals((newEdge7.vegetables), edgeCasePlan7.vegetables, 0.01);

        NewPlan edge8 = new NewPlan(edgeCasePlan8,"female");
        Plan newEdge8 = edge8.suggestPlan();
        assertNotEquals((newEdge8.chicken), edgeCasePlan8.chicken);
        /*System.out.println("-----");
        Plan bugPlanFinal;
        NewPlan bugTest = new NewPlan(bugPlan, "male");
        System.out.println("Current Plan:");
        bugTest.printPlan(bugPlan);
        bugPlanFinal = bugTest.suggestPlan();
        System.out.println("New Plan: ");
        bugTest.printPlan(bugPlanFinal);
        System.out.println("----");
        System.out.println("Current plan:");
        NewPlan bugTest2 = new NewPlan(bugPlanFinal, "male");
        bugTest2.printPlan(bugPlanFinal);
        bugPlanFinal = bugTest2.suggestPlan();
        System.out.println("New Plan: ");
        bugTest2.printPlan(bugPlanFinal);
        NewPlan bugTest3 = new NewPlan(bugPlanFinal, "male");
        bugPlanFinal = bugTest3.suggestPlan();
        bugTest3.printPlan(bugPlanFinal);  // should be the same as previous recommended plan
        NewPlan bugTest4 = new NewPlan(bugPlan2, "male");
        bugTest4.printPlan(bugPlan2);
        Plan bugPlan4 = bugTest4.suggestPlan();
        bugTest4.printPlan(bugPlan4);*/

    }
}