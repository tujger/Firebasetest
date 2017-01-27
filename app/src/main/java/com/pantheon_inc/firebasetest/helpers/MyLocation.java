package com.pantheon_inc.firebasetest.helpers;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

/**
 * Created by eduardm on 026, 1/26/2017.
 */

@IgnoreExtraProperties
public class MyLocation {

    public long timestamp;
    public float latitude;
    public float longitude;
    public float accuracy;
    public float bearing;
    public float speed;
    public String provider;

    public MyLocation() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)

        timestamp = new Date().getTime();
        latitude = (float) Math.random();
        longitude = (float) Math.random();
        accuracy = (float) Math.random();
        bearing = (float) Math.random();
        speed = (float) Math.random();
        provider = "gps";

    }

}