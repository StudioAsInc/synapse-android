package com.synapse.social.studioasinc.util;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class FirebaseEncryptionService {
    private static final String TAG = "FirebaseEncryptionSvc";
    private static final String USERS_REF = "users";
    private static final String PUBLIC_KEYS_REF = "public_keys";

    private final FirebaseDatabase database;

    public FirebaseEncryptionService() {
        this.database = FirebaseDatabase.getInstance();
    }

    public void uploadPublicKey(String userId, String publicKey, ChatEncryptionManager.EncryptionCallback callback) {
        DatabaseReference publicKeyRef = database.getReference(USERS_REF).child(userId).child(PUBLIC_KEYS_REF);

        Map<String, Object> keyData = new HashMap<>();
        keyData.put("public_key", publicKey);
        keyData.put("timestamp", System.currentTimeMillis());
        keyData.put("algorithm", "RSA_2048");

        publicKeyRef.setValue(keyData)
            .addOnSuccessListener(aVoid -> {
                Log.d(TAG, "Public key uploaded successfully for user: " + userId);
                callback.onSuccess();
            })
            .addOnFailureListener(e -> {
                Log.e(TAG, "Failed to upload public key", e);
                callback.onError("Failed to upload public key: " + e.getMessage());
            });
    }

    public void getPublicKey(String userId, ChatEncryptionManager.PublicKeyCallback callback) {
        DatabaseReference publicKeyRef = database.getReference(USERS_REF).child(userId).child(PUBLIC_KEYS_REF).child("public_key");

        publicKeyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String publicKey = dataSnapshot.getValue(String.class);
                    if (publicKey != null) {
                        callback.onSuccess(publicKey);
                    } else {
                        callback.onError("Public key is null");
                    }
                } else {
                    callback.onError("Public key not found for user: " + userId);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Failed to retrieve public key", databaseError.toException());
                callback.onError("Failed to retrieve public key: " + databaseError.getMessage());
            }
        });
    }
}
