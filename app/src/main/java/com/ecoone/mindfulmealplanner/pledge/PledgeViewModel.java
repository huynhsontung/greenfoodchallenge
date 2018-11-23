package com.ecoone.mindfulmealplanner.pledge;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.ecoone.mindfulmealplanner.database.FirebaseDatabaseInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.List;
import java.util.Map;


public class PledgeViewModel extends ViewModel {

    MutableLiveData<String> cityFilter = new MutableLiveData<>();
    MutableLiveData<Map<String,Object>> userList = new MutableLiveData<>();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference mDatabase = FirebaseDatabaseInterface.getDatabaseInstance();
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

}
