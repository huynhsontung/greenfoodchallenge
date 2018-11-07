package com.ecoone.mindfulmealplanner.DB;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity
public class User {


    @PrimaryKey
    @NonNull
    public String username; // Name has to be unique

    @NonNull
    public String gender;
    public String displayName;
    public String email;
//    public String photoUrl;
//    public long createDate;
//    public long lastLoginDate;
    public String currentPlanName;
}
