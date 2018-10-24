package com.ecoone.mindfulmealplanner;

import com.ecoone.mindfulmealplanner.db.AppDatabase;
import com.ecoone.mindfulmealplanner.db.Plan;
import com.ecoone.mindfulmealplanner.db.User;

import java.util.List;
import java.util.Locale;

public abstract class DbInterface {

    private static AppDatabase mDb;

    public static void setDb(AppDatabase db){mDb = db;}

    public static void addUser(final String username,
                        final String gender,
                        final String currentPlan) {
        User user = new User();
        user.username = username;
        user.gender = gender;
        user.currentPlan =currentPlan;
        mDb.userDao().addUser(user);
    }

    public static String getGenderByUsername(final String username) {
        return mDb.userDao().getUserGender(username);
    }

    public static String getCurrentPlanNameByUsername(final String username) {
        return mDb.userDao().getCurrentPlanName(username);
    }

    public static void addPlan(final String username,
                               final int[] foodAmount) {
        Plan plan = new Plan();
        plan.username = username;
        plan.planName = "Plan" + (mDb.planDao().getPlansCount(username) + 1);
        plan.beef = foodAmount[0];
        plan.pork = foodAmount[1];
        plan.chicken = foodAmount[2];
        plan.fish = foodAmount[3];
        plan.eggs = foodAmount[4];
        plan.beans = foodAmount[5];
        plan.vegetables = foodAmount[6];
        mDb.planDao().addPlan(plan);
    }

    public static float[] getCurrentPlanArray(final String username, final String planName) {
        Plan plan = mDb.planDao().getPlanFromUser(username, planName);
        float[] foodAmount = new float[7];
        foodAmount[0] = plan.beef;
        foodAmount[1] = plan.pork;
        foodAmount[2] = plan.chicken;
        foodAmount[3] = plan.fish;
        foodAmount[4] = plan.eggs;
        foodAmount[5] = plan.beans;
        foodAmount[6] = plan.vegetables;
        return foodAmount;
    }

    public static StringBuilder getPlanDatatoString(final String username) {
        StringBuilder sb = new StringBuilder();
        List<Plan> allPlans = mDb.planDao().getAllPlansFromUser(username);
        for (Plan plan: allPlans) {
            sb.append(String.format(Locale.CANADA,
                    "%s: Beef: %d, Pork: %d, Chicken: %d, Fish: %d" +
                            "Eggs: %d, Beans: %d, Vegetables: %d\n\n", plan.planName,
                    plan.beef, plan.pork, plan.chicken, plan.fish, plan.eggs,
                    plan.beans, plan.vegetables));
        }
        return sb;
    }
}
