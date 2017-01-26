package com.pantheon_inc.firebasetest.helpers;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

/**
 * Created by eduardm on 026, 1/26/2017.
 */

@IgnoreExtraProperties
public class Group {

    public int ttl;
    public int delay;
    public HashMap<String,String> users;


    public Group() {
        delay = 15;
        ttl = 15;
        users = new HashMap<>();
    }


}