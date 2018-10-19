package com.ecoone.mindfulmealplanner;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT name FROM User")
    public List<String> getUserList();

    @Query("SELECT currentPlan FROM User WHERE id = :userID")
    public String getCurrentPlanName(int userID);

    @Insert
    public void addUser(User user);

    @Update
    public int updateUser(User user);

    @Delete
    public void deleteUser(User user);
}
