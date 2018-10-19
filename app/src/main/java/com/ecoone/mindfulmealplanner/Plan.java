package com.ecoone.mindfulmealplanner;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

// Database object implemented by Room.
// Please read https://developer.android.com/training/data-storage/room/
// before committing any change.

@Entity(foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "id",
        childColumns = "userID",
        onDelete = CASCADE))
public class Plan {

    @PrimaryKey(autoGenerate = true)
    public  int key;

    public String planName;

    public int userID;

    public float beef;
    public float pork;
    public float chicken;
    public float fish;
    public float eggs;
    public float beans;
    public float vegetables;


}
