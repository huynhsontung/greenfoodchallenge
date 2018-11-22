//package com.ecoone.mindfulmealplanner.database;
//import android.arch.persistence.room.Database;
//import android.arch.persistence.room.Room;
//import 	android.arch.persistence.room.RoomDatabase;
//import android.content.Context;
//
//// Database object implemented by Room.
//// Please read https://developer.android.com/training/data-storage/room/
//// before committing any change.
//
//@Database(entities = {User.class, Plan.class}, version = 1)
//public abstract class AppDatabase extends RoomDatabase {
//
//    private static AppDatabase INSTANCE;
//
//    public abstract PlanDao planDao();
//    public abstract UserDao userDao();
//
//
//    public static AppDatabase getDatabase(final Context context){
//        if (INSTANCE ==  null){
//            INSTANCE =
//                    Room.databaseBuilder(context.getApplicationContext(),
//                            AppDatabase.class, "db")
//                    .allowMainThreadQueries().build();
//        }
//        return INSTANCE;
//    }
//}
//
//
