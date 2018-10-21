package com.ecoone.mindfulmealplanner;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

// Database object implemented by Room.
// Please read https://developer.android.com/training/data-storage/room/
// before committing any change.

@Entity(foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "username",
        childColumns = "username",
        onDelete = CASCADE),
        primaryKeys = {"planName", "username"})
public class Plan {


    @NonNull
    public String planName;

    @NonNull
    public String username;

    public float beef=1;
    public float pork=2;
    public float chicken=3;
    public float fish=4;
    public float eggs=5;
    public float beans=6;
    public float vegetables=7;

    public float getBeef() {
        return beef;
    }

    public float getPork(){
        return pork;
    }

    public float getChicken() {
        return chicken;
    }

    public float getBeans() {
        return beans;
    }

    public float getEggs() {
        return eggs;
    }

    public float getVegetables() {
        return vegetables;
    }

    public float getFish() {
        return fish;
    }

}
