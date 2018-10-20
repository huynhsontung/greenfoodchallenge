package com.ecoone.mindfulmealplanner;

import com.ecoone.mindfulmealplanner.db.AppDatabase;
import com.ecoone.mindfulmealplanner.db.User;

public class dbInterface {

    private static final String TAG = "testActivity";

    public static void addUser(final AppDatabase db, final String username,
                        final String gender) {
        User user = new User();
        user.username = username;
        user.gender = gender;
        db.userDao().addUser(user);
    }

    public static String getUserbyUsername(final AppDatabase db, final String username) {
        return db.userDao().getUsername(username);
    }
}
