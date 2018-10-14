package com.ecoone.mindfulmealplanner;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import 	android.arch.persistence.room.RoomDatabase;
import android.content.Context;

// Database object implemented by Room.
// Please read https://developer.android.com/training/data-storage/room/
// before committing any change.

@Database(entities = Plan.class, version = 1)
public abstract class PlanDatabase extends RoomDatabase {
    public abstract PlanDao PlanDao();

    private static volatile PlanDatabase INSTANCE;

    static PlanDatabase getDatabase(final Context context){
        if (INSTANCE ==  null){
            synchronized (PlanDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context,PlanDatabase.class,"PlanDatabase").build();
                }
            }
        }
        return INSTANCE;
    }
}


