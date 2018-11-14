package com.ecoone.mindfulmealplanner.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM User WHERE username = :username")
    User getUser(String username);

    @Query("SELECT username FROM User")
    List<String> getUserList();

    @Query("SELECT username FROM User WHERE username = :username")
    String getUsername(String username);

    @Query("SELECT displayName FROM User WHERE username = :username")
    String getDisplayName(String username);

    @Query("SELECT currentPlanName FROM User WHERE username = :username")
    String getCurrentPlanName(String username);

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
