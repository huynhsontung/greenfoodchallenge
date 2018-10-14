package com.ecoone.mindfulmealplanner;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

// Database object implemented by Room.
// Please read https://developer.android.com/training/data-storage/room/
// before committing any change.

@Entity
public class Plan {

    @PrimaryKey(autoGenerate = true)
    public int key;

    public float beef;
    public float pork;
    public float chicken;
    public float fish;
    public float eggs;
    public float beans;
    public float vegetables;


}
