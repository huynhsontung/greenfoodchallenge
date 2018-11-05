package com.ecoone.mindfulmealplanner;

import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;

public class DiscoverViewModel extends ViewModel {

    private static int countPledges;
    private static float totalCO2ePledges;
    private static float averageCO2ePledges;
    private static ArrayList<String> municipalities;

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

    public static ArrayList<String> getMunicipalitiesList(){

        return municipalities;
    }
}
