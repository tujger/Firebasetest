package com.pantheon_inc.firebasetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pantheon_inc.firebasetest.helpers.Group;
import com.pantheon_inc.firebasetest.helpers.Message;
import com.pantheon_inc.firebasetest.helpers.Post;
import com.pantheon_inc.firebasetest.helpers.User;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference myRef;
    private DatabaseReference myMessages;
    private DatabaseReference commonMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        /*myRef.addValueEventListener(new ValueEventListener() {
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
        myMessages.addListenerForSingleValueEvent(new ValueEventListener() {
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
        myMessages.addChildEventListener(new ChildEventListener() {
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
        findViewById(R.id.bCreateToken).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText et = (EditText) findViewById(R.id.etValue);

                String text = et.getText().toString();
                System.out.println("VALUE:"+text);


                String groupId = new BigInteger(48, new SecureRandom()).toString(32).toUpperCase();
                String userId = new BigInteger(48, new SecureRandom()).toString(32).toUpperCase();

                DatabaseReference ref = database.getReference();

                Group group = new Group();
                group.users.put("0",userId);
                ref.child("groups").child(groupId).setValue(group);

                ref.child("users").child(userId).setValue(new User(text, text + "1"));
                ref.child("groups").child(groupId);
                ref.child("users").child(userId);


//                ref.setValue(groupId,message);

//                ref = database.getReference("messages/users");
//                ref.setValue(groupId,message);


                commonMessages = database.getReference("messages/groups").child(groupId);
//                commonMessages.setValue(messageId, message);

                myMessages = database.getReference("messages/users").child(userId);
//                myMessages.setValue(messageId, message);



//                System.out.println("REFERENCE:"+myRef);

//                writeNewUser(text, text, text);
//                myRef.setValue(text);
            }
        });

        findViewById(R.id.bCreatePost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText et = (EditText) findViewById(R.id.etValue);

                String text = et.getText().toString();
                System.out.println("VALUE:"+text);

                System.out.println("REFERENCE:"+myRef);

//                myRef.setValue(text);
//                writeNewUser(text, text, text);
                writeNewPost(text, text+" user", text+" title", text+" body");
            }
        });



    }

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        myRef.child("users").child(userId).setValue(user);
    }


    private void writeNewPost(String userId, String username, String title, String body) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously

        String key = myMessages.push().getKey();

        Message message = new Message(userId,"userto","Body "+userId);

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, message);

        myMessages.updateChildren(childUpdates);
        commonMessages.updateChildren(childUpdates);
    }
}
