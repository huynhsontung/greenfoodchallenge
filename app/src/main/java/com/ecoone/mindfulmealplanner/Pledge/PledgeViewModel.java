package com.ecoone.mindfulmealplanner.Pledge;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.ecoone.mindfulmealplanner.DB.Plan;
import com.ecoone.mindfulmealplanner.DB.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;


public class PledgeViewModel extends ViewModel {

//    public float myPledge;
//    public String myUserName;
//    public String planName;
//    public Plan myPlan;
    MutableLiveData<String> cityFilter = new MutableLiveData<>();
    MutableLiveData<Map<String,Object>> userList = new MutableLiveData<>();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    DatabaseReference personalPledgeReference = mDatabase.child("uids").child(user.getUid()).child("pledgeInfo");
    DatabaseReference totalPledgeReference = mDatabase.child("pledgeResult");
    DatabaseReference totalPledgeLocationReference = mDatabase.child("pledgeResult/pledgeLocation");
    private static int countPledges;
    private static float totalCO2ePledges;
    private static float averageCO2ePledges;
    private static List<PeoplePledging> myPeoplePledgingList;
    private static List<String> municipalities;

    private static float equivalenceCar;

    public static int getCountPledges(){

        return countPledges;
    }

    public static float getTotalCO2ePledges(){

        return totalCO2ePledges;
    }

    public static float getAverageCO2ePledges(){

        return averageCO2ePledges;
    }

    public static float getEquivalenceCarDiscovery(){
        if(totalCO2ePledges <= 0){
            return 0;
        }

        float convertToGrams = totalCO2ePledges * 1000000;
        float gramsPerKmToyota = 178;

        return (convertToGrams / gramsPerKmToyota);
    }

    public static void addPersonPledging(PeoplePledging newPerson){
        myPeoplePledgingList.add(newPerson);
    }

    public static List<PeoplePledging> getPledgeList(){
        return myPeoplePledgingList;
    }

    public static List<String> getMunicipalities(){

        return municipalities;
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
