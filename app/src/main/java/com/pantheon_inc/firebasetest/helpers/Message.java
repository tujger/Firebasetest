package com.pantheon_inc.firebasetest.helpers;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

/**
 * Created by eduardm on 026, 1/26/2017.
 */

@IgnoreExtraProperties
public class Message {

    public long timestamp;
    public String from;
    public String to;
    public String body;
    transient public String aaa;
    private String bbb;

    public Message() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Message(String from, String to, String body) {
        this.from  = from;
        this.to   = to;
        this.body  = body;
        timestamp = new Date().getTime();
        aaa  ="AAA";
        bbb = "BBB";
    }

}