package com.ecoone.mindfulmealplanner;

import android.util.Log;

public class DatabaseInterface {

    private static final String TAG = "testActivity";

    public void addUser(final AppDatabase db, final String id,
                        final String name) {
        User user = new User();
        user.id = id;
        user.name = name;
        db.userDao().addUser(user);
    }

    public void isUserExist(final AppDatabase db, final String id) {
        Log.i(TAG, "1111111111");
        String name = db.userDao().getUserId(id);
        Log.i(TAG, "222222222");
        Log.i(TAG, name);
    }
}
