package com.ecoone.mindfulmealplanner;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT id FROM User WHERE id = :userId")
    String getUserId(String userId);

    @Query("SELECT name FROM User")
    List<String> getUserList();

//    @Query("SELECT currentPlan FROM User WHERE id = :userId")
//    String getCurrentPlanName(int userId);

    @Insert
    void addUser(User user);

    @Update
    int updateUser(User user);

    @Delete
    void deleteUser(User user);
}
