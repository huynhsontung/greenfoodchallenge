package com.ecoone.mindfulmealplanner;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

@Entity
public class User {
    @PrimaryKey
    @NonNull
    public String username; // Name has to be unique

    @NonNull
    public String gender;
    public long createDate;
    public long lastLoginDate;
    public String currentPlan;
}
