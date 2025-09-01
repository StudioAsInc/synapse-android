package com.synapse.social.studioasinc.util;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.PrivateKey;
import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;

/**
 * Service for handling E2E encryption with Firebase
 * - Manages public key storage and retrieval
 * - Handles encrypted message storage
 * - Coordinates with EncryptionKeyManager and MessageEncryption
 */
public class FirebaseEncryptionService {
    private static final String TAG = "FirebaseEncryptionService";
    private static final String USERS_REF = "users";
    private static final String PUBLIC_KEYS_REF = "public_keys";
    private static final String ENCRYPTED_MESSAGES_REF = "encrypted_messages";
    
    private final Context context;
    private final FirebaseDatabase database;
    private final EncryptionKeyManager keyManager;
    
    public FirebaseEncryptionService(Context context) {
        this.context = context;
        this.database = FirebaseDatabase.getInstance();
        this.keyManager = new EncryptionKeyManager(context);
    }
    
    /**
     * Initializes encryption for a user (called during signup/login)
     * @param userId The user's UID
     * @param callback Callback for completion
     */
    public void initializeUserEncryption(String userId, EncryptionCallback callback) {
        try {
            // Generate key pair if it doesn't exist
            if (!keyManager.hasKeys(userId)) {
                boolean success = keyManager.generateUserKeyPair(userId);
                if (!success) {
                    callback.onError("Failed to generate encryption keys");
                    return;
                }
            }
            
            // Upload public key to Firebase
            String publicKey = keyManager.getPublicKey(userId);
            if (publicKey != null) {
                uploadPublicKey(userId, publicKey, callback);
            } else {
                callback.onError("Failed to retrieve public key");
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Failed to initialize user encryption", e);
            callback.onError("Encryption initialization failed: " + e.getMessage());
        }
    }
    
    /**
     * Uploads a user's public key to Firebase
     */
    private void uploadPublicKey(String userId, String publicKey, EncryptionCallback callback) {
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
    
    /**
     * Retrieves a user's public key from Firebase
     * @param userId The user's UID
     * @param callback Callback with the public key
     */
    public void getPublicKey(String userId, PublicKeyCallback callback) {
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
    
    /**
     * Stores an encrypted message in Firebase
     * @param chatId The chat identifier
     * @param messageId The message identifier
     * @param encryptedMessage The encrypted message data
     * @param callback Callback for completion
     */
    public void storeEncryptedMessage(String chatId, String messageId, 
                                   MessageEncryption.EncryptedMessage encryptedMessage, 
                                   EncryptionCallback callback) {
        try {
            DatabaseReference messageRef = database.getReference(ENCRYPTED_MESSAGES_REF).child(chatId).child(messageId);
            
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("encrypted_aes_key", encryptedMessage.encryptedAesKey);
            messageData.put("encrypted_message", encryptedMessage.encryptedMessage);
            messageData.put("encryption_type", encryptedMessage.encryptionType);
            messageData.put("timestamp", encryptedMessage.timestamp);
            
            messageRef.setValue(messageData)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Encrypted message stored successfully");
                    callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to store encrypted message", e);
                    callback.onError("Failed to store encrypted message: " + e.getMessage());
                });
                
        } catch (Exception e) {
            Log.e(TAG, "Failed to store encrypted message", e);
            callback.onError("Failed to store encrypted message: " + e.getMessage());
        }
    }
    
    /**
     * Retrieves and decrypts a message
     * @param chatId The chat identifier
     * @param messageId The message identifier
     * @param currentUserId The current user's UID
     * @param callback Callback with the decrypted message
     */
    public void retrieveAndDecryptMessage(String chatId, String messageId, String currentUserId, 
                                       DecryptMessageCallback callback) {
        try {
            DatabaseReference messageRef = database.getReference(ENCRYPTED_MESSAGES_REF).child(chatId).child(messageId);
            
            messageRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        try {
                            String encryptedAesKey = dataSnapshot.child("encrypted_aes_key").getValue(String.class);
                            String encryptedMessage = dataSnapshot.child("encrypted_message").getValue(String.class);
                            String encryptionType = dataSnapshot.child("encryption_type").getValue(String.class);
                            
                            if (encryptedMessage == null) {
                                callback.onError("Encrypted message is null");
                                return;
                            }
                            
                            MessageEncryption.EncryptedMessage encryptedMsg = new MessageEncryption.EncryptedMessage();
                            encryptedMsg.encryptedAesKey = encryptedAesKey;
                            encryptedMsg.encryptedMessage = encryptedMessage;
                            encryptedMsg.encryptionType = encryptionType;
                            encryptedMsg.timestamp = dataSnapshot.child("timestamp").getValue(Long.class);
                            
                                        // Decrypt the message
            String decryptedMessage = decryptMessage(encryptedMsg, currentUserId, chatId);
                            if (decryptedMessage != null) {
                                callback.onSuccess(decryptedMessage);
                            } else {
                                callback.onError("Failed to decrypt message");
                            }
                            
                        } catch (Exception e) {
                            Log.e(TAG, "Failed to parse encrypted message", e);
                            callback.onError("Failed to parse encrypted message: " + e.getMessage());
                        }
                    } else {
                        callback.onError("Message not found");
                    }
                }
                
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "Failed to retrieve encrypted message", databaseError.toException());
                    callback.onError("Failed to retrieve encrypted message: " + databaseError.getMessage());
                }
            });
            
        } catch (Exception e) {
            Log.e(TAG, "Failed to retrieve encrypted message", e);
            callback.onError("Failed to retrieve encrypted message: " + e.getMessage());
        }
    }
    
    /**
     * Decrypts a message using the appropriate method
     */
    private String decryptMessage(MessageEncryption.EncryptedMessage encryptedMessage, String userId, String chatId) {
        try {
            if ("AES_SESSION".equals(encryptedMessage.encryptionType)) {
                // Try to get session key using the chatId parameter
                String[] chatUsers = getChatUsersFromChatId(chatId);
                if (chatUsers != null && chatUsers.length == 2) {
                    SecretKey sessionKey = keyManager.getSessionKey(chatUsers[0], chatUsers[1]);
                    if (sessionKey != null) {
                        return MessageEncryption.decryptWithSessionKey(encryptedMessage, sessionKey);
                    }
                }
            }
            
            // Fall back to RSA decryption
            PrivateKey privateKey = keyManager.getPrivateKey(userId);
            if (privateKey != null) {
                return MessageEncryption.decryptMessage(encryptedMessage, privateKey);
            }
            
            return null;
            
        } catch (Exception e) {
            Log.e(TAG, "Failed to decrypt message", e);
            return null;
        }
    }
    
    /**
     * Helper method to extract user IDs from chat ID
     * This is a simplified implementation - adjust based on your chat ID format
     */
    private String[] getChatUsersFromChatId(String chatId) {
        // Assuming chat ID format is "user1_user2" or similar
        if (chatId != null && chatId.contains("_")) {
            return chatId.split("_", 2);
        }
        return null;
    }
    
    // Callback interfaces
    public interface EncryptionCallback {
        void onSuccess();
        void onError(String error);
    }
    
    public interface PublicKeyCallback {
        void onSuccess(String publicKey);
        void onError(String error);
    }
    
    public interface DecryptMessageCallback {
        void onSuccess(String decryptedMessage);
        void onError(String error);
    }
}
