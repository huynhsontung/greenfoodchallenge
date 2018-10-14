package com.ecoone.mindfulmealplanner;
import android.arch.persistence.room.Database;
import 	android.arch.persistence.room.RoomDatabase;

// Database object implemented by Room.
// Please read https://developer.android.com/training/data-storage/room/
// before committing any change.

@Database(entities = Plan.class, version = 1)
public abstract class PlanDatabase extends RoomDatabase {
    public abstract PlanDao PlanDao();
}


