package com.ecoone.mindfulmealplanner;

import android.util.Log;

public class dbInterface {

    private static final String TAG = "testActivity";

    public static void addUser(final AppDatabase db, final String id,
                        final String name) {
        User user = new User();
        user.id = id;
        user.name = name;
        db.userDao().addUser(user);
    }

    public static void isUserExist(final AppDatabase db, final String id) {
        String name = db.userDao().getUserId(id);
        Log.i(TAG, "name: " + name);
    }
}
