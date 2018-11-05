package com.ecoone.mindfulmealplanner.db;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public abstract class FirebaseDatabaseInterface {
    static final String USERS_NODE = "users";
    static final String PLANS_NODE = "plans";
    private static final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private static final String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private static final String TAG = "testActivity";

    public static void writeUser(User user){
        mDatabase.child(USERS_NODE).child(userUid).setValue(user);
    }

    public static void getUserGender() {

    }

    public static void writePlan(Plan plan){
        HashMap<String, Object> planMap = new HashMap<>();
        planMap.put("beef", plan.beef);
        planMap.put("pork", plan.pork);
        planMap.put("chicken", plan.chicken);
        planMap.put("fish", plan.fish);
        planMap.put("eggs", plan.eggs);
        planMap.put("beans", plan.beans);
        planMap.put("vegetables", plan.vegetables);
        mDatabase.child(USERS_NODE).child(userUid).child(PLANS_NODE).child(plan.planName).setValue(planMap);
    }

    public static void updatePlan(Plan plan) {
        deletePlan(plan.planName);
        writePlan(plan);
    }

    public static void deletePlan(String planName){
        mDatabase.child(USERS_NODE).child(userUid).child(PLANS_NODE).child(planName).removeValue();
    }

    public static void updateCurrentPlanName(String newName){
        mDatabase.child(USERS_NODE).child(userUid).child("currentPlanName").setValue(newName);
    }
}
