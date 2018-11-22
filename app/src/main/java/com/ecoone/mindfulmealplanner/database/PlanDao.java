package com.ecoone.mindfulmealplanner.database;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

// Database object implemented by Room.
// Please read https://developer.android.com/training/data-storage/room/
// before committing any change.

@Dao
public interface PlanDao {
    @Query("SELECT * FROM `Plan` WHERE username = :username AND planName = :planName LIMIT 1")
    Plan getPlan(String username, String planName);

    @Query("SELECT * FROM `Plan` WHERE username = :username")
    List<Plan> getAllPlans(String username);

    @Query("SELECT planName FROM `Plan` WHERE username = :username")
    List<String> getAllPlansName(String username);

    @Query("SELECT count(*) FROM `Plan` WHERE username = :username")
    int getPlansCount(String username);

    @Query("UPDATE `Plan` SET planname = :newPlanName WHERE username = :username AND planName = :oldPlanName")
    void changePlanName(String username, String oldPlanName, String newPlanName);

    @Insert
    void addPlan(Plan plan);

    @Update
    int updatePlan(Plan plan);

    @Delete
    int deletePlan(Plan plan);

    @Query("DELETE FROM `Plan` WHERE username = :username")
    void deleteAllPlansbyUsername(String username);

    @Query("DELETE FROM `plan`")
    void deleteAll();

}
