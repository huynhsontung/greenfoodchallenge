package com.ecoone.mindfulmealplanner.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.ecoone.mindfulmealplanner.db.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT username FROM User")
    List<String> getUserList();

    @Query("SELECT username FROM User WHERE username = :username")
    String getUsername(String username);

//    @Query("SELECT currentPlan FROM User WHERE username = :username")
//    String getCurrentPlanName(String username);

    @Query("SELECT gender FROM User WHERE username = :username")
    String getUserGender(String username);

    @Insert
    void addUser(User user);

    @Update
    int updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("DELETE FROM User")
    void deleteALL();
}
