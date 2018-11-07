package com.ecoone.mindfulmealplanner.DB;

import android.support.annotation.NonNull;
import android.text.StaticLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;

import java.util.HashMap;

public abstract class FirebaseDatabaseInterface {
    public static final String USERS_NODE = "userInfo";
    public static final String PLANS_NODE = "planInfo";
    public static final String PLEDGE_NODE = "pledgeInfo";
    private static final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private static final String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private static final FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();

    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(FirebaseDatabaseInterface)";


    public static void writeUser(User user) {
        mDatabase.child(userUid).child(USERS_NODE).setValue(user);
    }

    public static void updateUser(String userAttribute, String value) {
        mDatabase.child(userUid).child(USERS_NODE).child(userAttribute).setValue(value);
    }

    public static void DoesUserExist(@NonNull final mCallback<Boolean> finishedCallback) {
        mDatabase.child(userUid).child(USERS_NODE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
//                    Log.i(TAG, "exist");
                    finishedCallback.callback(true);
                }
                else {
//                    Log.i(TAG, "not exist");
                    finishedCallback.callback(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//    public static Task<User> getUser() {
//
//        final TaskCompletionSource<User> dbSource = new TaskCompletionSource<>();
//
//        mDatabase.child(USERS_NODE).child(userUid).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
//                dbSource.setResult(user);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                dbSource.setException(databaseError.toException());
//            }
//        });
//        return dbSource.getTask();
//    }

//    public static Task<Plan> getPlan() {
//        final TaskCompletionSource<Plan> dbSource = new TaskCompletionSource<>();
//        mDatabase.child(USERS_NODE).child(userUid).child(PLANS_NODE)
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        Plan plan = dataSnapshot.getValue(Plan.class);
//                        dbSource.setResult(plan);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        dbSource.setException(databaseError.toException());
//                    }
//                });
//        return dbSource.getTask();
//    }

    public static void writePlan(Plan plan){
        HashMap<String, Object> planMap = new HashMap<>();
        planMap.put("beef", plan.beef);
        planMap.put("pork", plan.pork);
        planMap.put("chicken", plan.chicken);
        planMap.put("fish", plan.fish);
        planMap.put("eggs", plan.eggs);
        planMap.put("beans", plan.beans);
        planMap.put("vegetables", plan.vegetables);
        mDatabase.child(userUid).child(PLANS_NODE).child(plan.planName).setValue(planMap);
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

    public static void deletePlan(String planName){
        mDatabase.child(userUid).child(PLANS_NODE).child(planName).removeValue();
    }

    public static void updateCurrentPlanName(String planName) {
        mDatabase.child(userUid).child(USERS_NODE).child("currentPlanName").setValue(planName);
    }

    public static void updateCurrentPlanNameAndPlan(Plan plan, String oldName, String newName){
        mDatabase.child(userUid).child(USERS_NODE).child("currentPlanName").setValue(newName);
        deletePlan(oldName);
        plan.planName = newName;
        writePlan(plan);
    }

    public static void writePledge(Pledge pledge) {
        mDatabase.child(userUid).child(PLEDGE_NODE).setValue(pledge);
    }

    public static void updatePledgeAmount(int amount) {
        mDatabase.child(userUid).child(PLEDGE_NODE).child("amount").setValue(amount);
    }

    public static void updatePledgeLocation(String location) {
        mDatabase.child(USERS_NODE).child(userUid).child(PLEDGE_NODE).child("location").setValue(location);
    }

}
