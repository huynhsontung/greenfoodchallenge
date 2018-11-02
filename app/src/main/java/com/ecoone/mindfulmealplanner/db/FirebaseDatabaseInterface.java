package com.ecoone.mindfulmealplanner.db;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public abstract class FirebaseDatabaseInterface {
    static final String USERS_NODE = "users";
    static final String PLANS_NODE = "plans";
    private static final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public static void writeUser(String userId, User user){
        mDatabase.child(USERS_NODE).child(userId).setValue(user);
    }

    public static void writePlan(String userId, Plan plan){
        HashMap<String, Object> planMap = new HashMap<>();
        planMap.put("beef", plan.beef);
        planMap.put("pork", plan.pork);
        planMap.put("chicken", plan.chicken);
        planMap.put("fish", plan.fish);
        planMap.put("eggs", plan.eggs);
        planMap.put("beans", plan.beans);
        planMap.put("vegetables", plan.vegetables);
        mDatabase.child(USERS_NODE).child(userId).child(PLANS_NODE).child(plan.planName).setValue(planMap);
    }

    public static void deletePlan(String userId, String planName){
        mDatabase.child(USERS_NODE).child(userId).child(PLANS_NODE).child(planName).removeValue();
    }

    public static void updateCurrentPlanName(String userID, String newName){
        mDatabase.child(USERS_NODE).child(userID).child("currentPlanName").setValue(newName);
    }
}
