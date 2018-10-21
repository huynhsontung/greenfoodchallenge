package com.ecoone.mindfulmealplanner;

import com.ecoone.mindfulmealplanner.db.AppDatabase;
import com.ecoone.mindfulmealplanner.db.Plan;
import com.ecoone.mindfulmealplanner.db.User;

import java.util.List;
import java.util.Locale;

public class dbInterface {

    private AppDatabase mDb;

    private static final String TAG = "testActivity";

    public dbInterface(AppDatabase db) {
        mDb = db;
    }

    public void addUser(final String username,
                        final String gender,
                        final String currentPlan) {
        User user = new User();
        user.username = username;
        user.gender = gender;
        user.currentPlan =currentPlan;
        mDb.userDao().addUser(user);
    }

    public String getGenderbyUsername(final String username) {
        return mDb.userDao().getUserGender(username);
    }

    public void addPlan(final String username,
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

    public StringBuilder fetchPlanDatatoString(final String username) {
        StringBuilder sb = new StringBuilder();
        List<Plan> allPlans = mDb.planDao().getAllPlansFromUser(username);
        for (Plan plan: allPlans) {
            sb.append(String.format(Locale.CANADA,
                    "%s: Beef: %d, Pork: %d, Chicken: %d, Fish: %d" +
                            "Eggs: %d, Beans: %d, Vegetables: %d\n", plan.planName,
                    plan.beef, plan.pork, plan.chicken, plan.fish, plan.eggs,
                    plan.beans, plan.vegetables));
        }
        return sb;
    }
}
