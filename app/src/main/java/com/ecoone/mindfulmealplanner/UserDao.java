package com.ecoone.mindfulmealplanner;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT username FROM User")
    public List<String> getUserList();

    @Query("SELECT currentPlan FROM User WHERE username = :username")
    public String getCurrentPlanName(String username);

    @Query("SELECT gender FROM User WHERE username = :username")
    public String getUserGender(String username);

    @Insert
    public void addUser(User user);

    @Update
    public int updateUser(User user);

    @Delete
    public void deleteUser(User user);
}
