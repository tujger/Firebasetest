package com.pantheon_inc.firebasetestserver;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.tasks.OnSuccessListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MyServer {

    public static void main(final String[] args) throws IOException {

        File file = new File("Server/fir-test-81658-firebase-adminsdk-xifhr-62d9348798.json");
        System.out.println(file.getCanonicalPath());

        FileInputStream serviceAccount = new FileInputStream(file.getCanonicalPath());

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
                .setDatabaseUrl("https://fir-test-81658.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(options);
        System.out.println("START");

String uid = "4Mq5r61VuFbVfMgwp6ed1w2fbrZ2";

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference ref = database.getReference();

        ref.child("2HQO4QO3PC").child("users").child(uid).setValue("1");

        FirebaseAuth.getInstance().createCustomToken(uid)
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String customToken) {
                        System.out.println("GOT:"+customToken);
                        // Send token back to client
                    }
                });
    }
}