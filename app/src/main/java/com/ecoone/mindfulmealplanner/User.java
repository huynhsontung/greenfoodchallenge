package com.ecoone.mindfulmealplanner;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class User {
    @PrimaryKey
    @NonNull
    public String id;

    @NonNull
    public String gender;

    public String name;
//    public long createDate;
//    public long lastLoginDate;
//    public String currentPlan;
}
