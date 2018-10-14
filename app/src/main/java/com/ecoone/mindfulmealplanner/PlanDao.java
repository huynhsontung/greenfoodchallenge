package com.ecoone.mindfulmealplanner;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

// Database object implemented by Room.
// Please read https://developer.android.com/training/data-storage/room/
// before committing any change.

@Dao
public interface PlanDao {
    @Query("SELECT * FROM `Plan` ORDER BY `key` DESC LIMIT 1")
    public Plan loadCurrentPlan();

    @Insert
    public void addPlan(Plan plan);

    @Delete
    public void deletePlan(Plan plan);


}
