package com.ecoone.mindfulmealplanner;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public long createDate;
    public long lastLoginDate;
    public String currentPlan;
}
