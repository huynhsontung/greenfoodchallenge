package com.ecoone.mindfulmealplanner;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import 	android.arch.persistence.room.RoomDatabase;
import android.content.Context;

// Database object implemented by Room.
// Please read https://developer.android.com/training/data-storage/room/
// before committing any change.

@Database(entities = {User.class, Plan.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PlanDao planDao();
    public abstract UserDao userDao();

    private static volatile AppDatabase INSTANCE;

    static AppDatabase getDatabase(final Context applicationContext){
        if (INSTANCE ==  null){
            synchronized (AppDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(applicationContext,AppDatabase.class,"PlansDatabase").allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }
}


