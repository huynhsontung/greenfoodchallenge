package com.ecoone.mindfulmealplanner.Pledge;

import android.arch.lifecycle.ViewModel;

import com.ecoone.mindfulmealplanner.DB.Plan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MyPledgeViewModel extends ViewModel {

//    public float myPledge;
//    public String myUserName;
//    public String planName;
//    public Plan myPlan;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    DatabaseReference personalPledgeReference = mDatabase.child("uids").child(user.getUid()).child("pledgeInfo");
    DatabaseReference totalPledgeReference = mDatabase.child("pledgeResult");
    DatabaseReference getPledgeLocationList(String municipality){
        return mDatabase.child("pledgeResult/pledgeLocation").child(municipality);
    }


    //    // Post: Sets pledge and updates database if user changes their pledge.
//    public static void setCurrentPledge(){
//
//    }
//
//    // Post: Gets user's pledge from the database.
//    public static float getCurrentPledge(){
//
//        return myPledge;
//    }
//
//    // Post: Gets current username (For the database)
//    public static String getUserName(){
//
//        return myUserName;
//    }
//
//    // Post: Gets the user's current plan name
//    public static String getPlanNamePledge(){
//
//        return planName;
//    }
//
//    // Post: Gets the user's current plan.
//    public static Plan getPlanPledge(){
//
//        return myPlan;
//    }
}
