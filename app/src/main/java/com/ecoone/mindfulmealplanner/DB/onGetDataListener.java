package com.ecoone.mindfulmealplanner.DB;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public interface onGetDataListener {
    public void onStart();
    public void onSuccess(DataSnapshot data);
    public void onFailed(DatabaseError databaseError);
}
