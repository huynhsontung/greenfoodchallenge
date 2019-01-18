package com.ecoone.mindfulmealplanner.database;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.Semaphore;

public class FirebaseDatabaseInterface {
    public static final String ALLUSERSUID_NODE = "uids";
    public static final String USERINFO_NODE = "userInfo";
    public static final String PLANINFO_NODE = "planInfo";
    public static final String PLEDGEINFO_NODE = "pledgeInfo";
    private static final DatabaseReference mDatabase = getDatabaseInstance();
    private static boolean alreadySetPersistence;
    private static Semaphore mutex;
//    private static final FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();
//    private static String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "FirebaseDatabaseInterface";

    public static DatabaseReference getDatabaseInstance(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if(!alreadySetPersistence) {
            try {
                database.setPersistenceEnabled(true);
            } catch (Exception e){
                Log.e(TAG,CLASSTAG, e );
            }
            alreadySetPersistence = true;
        }
        return database.getReference();
    }

    public static String getUid(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public static void deleteUserData() {
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.i(TAG, CLASSTAG + "check UID: " + userUid);
        mDatabase.child(ALLUSERSUID_NODE).child(userUid).removeValue();
    }

    public static void writeUser(User user) {
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.i(TAG, CLASSTAG + "check UID: " + userUid);
        mDatabase.child(ALLUSERSUID_NODE).child(userUid).child(USERINFO_NODE).setValue(user);
    }

    public static void updateUserIconName(String iconName) {
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.i(TAG, CLASSTAG + "check UID: " + userUid);
        mDatabase.child(ALLUSERSUID_NODE).child(userUid).child(USERINFO_NODE).child("iconName").setValue(iconName);
    }

    public static void writePlan(Plan plan){
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.i(TAG, CLASSTAG + "check UID: " + userUid);
        HashMap<String, Object> planMap = new HashMap<>();
        planMap.put("beef", plan.beef);
        planMap.put("pork", plan.pork);
        planMap.put("chicken", plan.chicken);
        planMap.put("fish", plan.fish);
        planMap.put("eggs", plan.eggs);
        planMap.put("beans", plan.beans);
        planMap.put("vegetables", plan.vegetables);
        mDatabase.child(ALLUSERSUID_NODE).child(userUid).child(PLANINFO_NODE).child(plan.planName).setValue(planMap);
    }

    public static float[] getPlanArray(final Plan plan) {
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

    public static void updatePlan(Plan plan) {
        deletePlan(plan.planName);
        writePlan(plan);
    }

    public static StringBuilder getPlanDatatoString(final Plan plan) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.CANADA,
                "Beef: %d, Pork: %d, Chicken: %d, Fish: %d, " +
                        "Eggs: %d, Beans: %d, Vegetables: %d",
                (int) plan.beef, (int) plan.pork ,(int) plan.chicken,
                (int) plan.fish, (int) plan.eggs, (int) plan.beans, (int) plan.vegetables));
        return sb;
    }

    public static void deletePlan(String planName){
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child(ALLUSERSUID_NODE).child(userUid).child(PLANINFO_NODE).child(planName).removeValue();
    }

    public static void updateCurrentPlanName(String planName) {
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child(ALLUSERSUID_NODE).child(userUid).child(USERINFO_NODE).child("currentPlanName").setValue(planName);
    }

    public static void updateCurrentPlanNameAndPlan(Plan plan, String oldName, String newName){
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child(ALLUSERSUID_NODE).child(userUid).child(USERINFO_NODE).child("currentPlanName").setValue(newName);
        deletePlan(oldName);
        plan.planName = newName;
        writePlan(plan);
    }

    public static void writePledge(Pledge pledge) {
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child(ALLUSERSUID_NODE).child(userUid).child(PLEDGEINFO_NODE).setValue(pledge);
    }

    public static void updatePledgeAmount(int amount) {
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child(ALLUSERSUID_NODE).child(userUid).child(PLEDGEINFO_NODE).child("amount").setValue(amount);
    }

    public static void updatePledgeLocation(String location) {
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child(ALLUSERSUID_NODE).child(userUid).child(PLEDGEINFO_NODE).child("location").setValue(location);
    }

    public static void writeMeal(Meal meal, String identifier) {
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child(ALLUSERSUID_NODE).child(userUid).child("mealInfo").child(identifier).setValue(meal);
    }

    public static void deleteMeal(String identifier) {
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child(ALLUSERSUID_NODE).child(userUid).child("mealInfo").child(identifier).removeValue();
    }

}
