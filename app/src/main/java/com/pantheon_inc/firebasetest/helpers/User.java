package com.pantheon_inc.firebasetest.helpers;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

/**
 * Created by eduardm on 026, 1/26/2017.
 */

@IgnoreExtraProperties
public class User {

    transient public String id;
    public String username;
    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String id,String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

}