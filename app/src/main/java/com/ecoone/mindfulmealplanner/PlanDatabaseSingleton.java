package com.ecoone.mindfulmealplanner;

import android.arch.persistence.room.Room;
import android.content.Context;

public class PlanDatabaseSingleton {
    private static PlanDatabase singleInstance;
    private PlanDatabaseSingleton(){}

    public static PlanDatabase getSingleInstanceDatabase(Context applicationContext){
        if (singleInstance == null){
            singleInstance = Room.databaseBuilder(applicationContext,PlanDatabase.class,"PlanDatabase").build();
        }
        return singleInstance;
    }
}
