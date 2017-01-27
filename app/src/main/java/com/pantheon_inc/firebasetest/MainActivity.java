package com.pantheon_inc.firebasetest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pantheon_inc.firebasetest.helpers.Group;
import com.pantheon_inc.firebasetest.helpers.Message;
import com.pantheon_inc.firebasetest.helpers.MyLocation;
import com.pantheon_inc.firebasetest.helpers.User;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference refGroup;
    private DatabaseReference refUser;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    System.out.println("onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    System.out.println("onAuthStateChanged:signed_out");
                }
            }
        };

        /*refDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Object value = dataSnapshot.getValue();
                System.out.println("DATACHANGED:"+value);
//                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                System.out.println("CANCELLED:"+error.getMessage());
//                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        refMyMessages.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Object value = dataSnapshot.getValue();
                System.out.println("SINGLECHANGED:"+value);
//                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                System.out.println("SINGLECANCELLED:"+error.getMessage());
//                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        refMyMessages.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.out.println("CHILDADDED:"+s+":"+dataSnapshot.getValue());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                System.out.println("CHILDCHANGED:"+ s);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                System.out.println("CHILDREMOVED:"+dataSnapshot.getValue());

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                System.out.println("CHILDMOVED:"+s);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("CHILDCANCELLED:"+databaseError.getMessage());
            }
        });*/


        findViewById(R.id.bAuth).setOnClickListener(onAuthListener);
        findViewById(R.id.bLogout).setOnClickListener(onLogoutListener);
        findViewById(R.id.bCreateUser).setOnClickListener(onCreateUserListener);
        findViewById(R.id.bWithToken).setOnClickListener(onWithTokenListener);
        findViewById(R.id.bCreatePost).setOnClickListener(onCreatePostListener);
        findViewById(R.id.bCreateLocation).setOnClickListener(onCreateLocationListener);

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private View.OnClickListener onLogoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mAuth.signOut();
        }
    };

    private View.OnClickListener onWithTokenListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String token = "eyJhbGciOiJSUzI1NiJ9.eyJhdWQiOiJodHRwczovL2lkZW50aXR5dG9vbGtpdC5nb29nbGVhcGlzLmNvbS9nb29nbGUuaWRlbnRpdHkuaWRlbnRpdHl0b29sa2l0LnYxLklkZW50aXR5VG9vbGtpdCIsImV4cCI6MTQ4NTU1NzI5MiwiaWF0IjoxNDg1NTUzNjkyLCJpc3MiOiJmaXJlYmFzZS1hZG1pbnNkay14aWZockBmaXItdGVzdC04MTY1OC5pYW0uZ3NlcnZpY2VhY2NvdW50LmNvbSIsInN1YiI6ImZpcmViYXNlLWFkbWluc2RrLXhpZmhyQGZpci10ZXN0LTgxNjU4LmlhbS5nc2VydmljZWFjY291bnQuY29tIiwidWlkIjoiNE1xNXI2MVZ1RmJWZk1nd3A2ZWQxdzJmYnJaMiJ9.OoXIRm2mCC84aRy_rinAJCk6D6usKPDrQkFsmeYrFGRqIXOpmmCdEGPt6ipbXglhnBsfpMvzW35FVyEGCi1boZ6HNqDgPT2VFmbIpFjAxLrw_7SFdNsOmskHXTee0gSPuXWchT8j1AEvn_aocmwROOHyviLt2j2kmUg2C1GixoNvM8CjhnoUt0mJH_0FOszdkmTGsnQImgiNlPDjUheqbvAT_5MW4hWmdoj4rP8YAxHB9loCih3jMohtzRIAYlSRWnDq-RWxrES18tA926Vs48s2vJZv66yWl9vdjf7E5N_JEh2QZQSyabwEUfNG42scC2EkDuO5x16mR87rJ0KO5Q";

            mAuth.signInWithCustomToken(token)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            System.out.println("signInWithCustomToken:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                System.out.println("signInWithCustomToken:"+ task.getException());
                                return;
                            }

                            String text = ((EditText) findViewById(R.id.etValue)).getText().toString();
                            user = new User(task.getResult().getUser().getUid(), text, text + "@mail.com");

                            EditText et = (EditText) findViewById(R.id.etValue);

                            System.out.println("VALUE:" + text);


                            String groupId = "2HQO4QO3PC";//new BigInteger(48, new SecureRandom()).toString(32).toUpperCase();
//                String userId = new BigInteger(48, new SecureRandom()).toString(32).toUpperCase();

                            DatabaseReference ref = database.getReference();

                            Group group = new Group();
                            group.users.put(user.id, user);
//            ref.child(groupId).setValue(group);
//user.id = "vArMrEIa2POvL8at8eKbETd8Jli2";
                            refGroup = ref.child(groupId);
                            refUser = refGroup.child("users").child(user.id);
                            refGroupMessages = refGroup.child("messages").child("group");
                            refUserMessages = refGroup.child("messages").child("users").child(user.id);

                            Map<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put(user.id, user);

                            refGroup.child("users").updateChildren(childUpdates);

                        }
                    });
        }
    };

    private View.OnClickListener onAuthListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            mAuth.signInAnonymously().addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    System.out.println("signInAnonymously:onComplete:" + task.isSuccessful());

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (task.isSuccessful()) {
                        System.out.println("signIn" + task.getResult() + ":" + task.getResult().getUser().getUid());
                        String text = ((EditText) findViewById(R.id.etValue)).getText().toString();
                        user = new User(task.getResult().getUser().getUid(), text, text + "@mail.com");
                    } else {
                        System.out.println("signInAnonymously" + task.getException());
                    }
                }
            });

        }
    };

    private DatabaseReference refGroupMessages;
    private DatabaseReference refUserMessages;
    private View.OnClickListener onCreateUserListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            EditText et = (EditText) findViewById(R.id.etValue);

            String text = et.getText().toString();
            System.out.println("VALUE:" + text);


            String groupId = "2HQO4QO3PC";//new BigInteger(48, new SecureRandom()).toString(32).toUpperCase();
//                String userId = new BigInteger(48, new SecureRandom()).toString(32).toUpperCase();

            DatabaseReference ref = database.getReference();

            Group group = new Group();
            group.users.put(user.id, user);
//            ref.child(groupId).setValue(group);
//user.id = "vArMrEIa2POvL8at8eKbETd8Jli2";
            refGroup = ref.child(groupId);
            refUser = refGroup.child("users").child(user.id);
            refGroupMessages = refGroup.child("messages").child("group");
            refUserMessages = refGroup.child("messages").child("users").child(user.id);

            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put(user.id, user);

            refGroup.child("users").updateChildren(childUpdates);

        }
    };

    private View.OnClickListener onCreatePostListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            EditText et = (EditText) findViewById(R.id.etValue);

            String text = et.getText().toString();
            System.out.println("VALUE:"+text);

            String key = refGroup.child("messages").push().getKey();

            Message message = new Message(user.id,"userfrom","userto","Body "+user.id);

            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put(key, message);

            refUserMessages.updateChildren(childUpdates);
            refGroupMessages.updateChildren(childUpdates);
        }
    };

    private View.OnClickListener onCreateLocationListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String key = refGroup.push().getKey();

            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put(key, new MyLocation());

            refGroup.child("locations").child(user.id).updateChildren(childUpdates);


        }
    };

}
