package com.ecoone.mindfulmealplanner;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
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
    public Plan getPlanFromUser(String username, String planName);

    @Query("SELECT * FROM `Plan` WHERE username = :username")
    public List<Plan> getAllPlansFromUser(String username);

    @Query("SELECT count(*) FROM `Plan` WHERE username = :username")
    public int getPlansCount(String username);

    @Insert
    public void addPlan(Plan plan);

    @Update
    public int updatePlan(Plan plan);

    @Delete
    public int deletePlan(Plan plan);

    @Query("DELETE FROM `Plan` WHERE username = :username")
    public void deleteAllPlans(String username);



}
