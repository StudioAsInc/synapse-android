package com.synapse.social.studioasinc.util;

import android.content.Context;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;

import com.synapse.social.studioasinc.model.EncryptedMessageModel;

/**
 * Main encryption manager for the chat system
 * - Coordinates between encryption utilities and Firebase
 * - Handles message encryption/decryption workflow
 * - Manages session keys for performance
 */
public class ChatEncryptionManager {
    private static final String TAG = "ChatEncryptionManager";
    private static final String SKYLINE_REF = "skyline";
    private static final String CHATS_REF = "chats";
    private static final String USERS_REF = "users";
    private static final String PUBLIC_KEYS_REF = "public_keys";
    
    private final Context context;
    private final EncryptionKeyManager keyManager;
    private final FirebaseEncryptionService firebaseService;
    private final FirebaseDatabase database;
    
    public ChatEncryptionManager(Context context) {
        this.context = context;
        this.keyManager = new EncryptionKeyManager(context);
        this.firebaseService = new FirebaseEncryptionService(context);
        this.database = FirebaseDatabase.getInstance();
    }
    
    /**
     * Initializes encryption for the current user
     */
    public void initializeCurrentUserEncryption(EncryptionCallback callback) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            callback.onError("User not authenticated");
            return;
        }
        
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (currentUserId == null) {
            callback.onError("User not authenticated");
            return;
        }
        
        firebaseService.initializeUserEncryption(currentUserId, new FirebaseEncryptionService.EncryptionCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "User encryption initialized successfully");
                callback.onSuccess();
            }
            
            @Override
            public void onError(String error) {
                Log.e(TAG, "Failed to initialize user encryption: " + error);
                callback.onError(error);
            }
        });
    }
    
    /**
     * Sends an encrypted message
     */
    public void sendEncryptedMessage(String messageText, String recipientUid, 
                                   Map<String, Object> attachments, String repliedMessageId,
                                   MessageCallback callback) {
        try {
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                callback.onError("User not authenticated");
                return;
            }
            
            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            if (currentUserId == null) {
                callback.onError("User not authenticated");
                return;
            }
            
            // Check if we have a session key for faster encryption
            SecretKey sessionKey = keyManager.getSessionKey(currentUserId, recipientUid);
            MessageEncryption.EncryptedMessage encryptedMessage;
            
            if (sessionKey != null) {
                // Use session key for faster encryption
                encryptedMessage = MessageEncryption.encryptWithSessionKey(messageText, sessionKey);
                Log.d(TAG, "Using session key for encryption");
            } else {
                // Get recipient's public key and use RSA hybrid encryption
                firebaseService.getPublicKey(recipientUid, new FirebaseEncryptionService.PublicKeyCallback() {
                    @Override
                    public void onSuccess(String publicKey) {
                        // Encrypt with RSA hybrid
                        MessageEncryption.EncryptedMessage encrypted = 
                            MessageEncryption.encryptMessage(messageText, publicKey);
                        
                        if (encrypted != null) {
                            // Generate session key for future messages
                            keyManager.generateSessionKey(currentUserId, recipientUid);
                            
                            // Store encrypted message in Firebase
                            storeEncryptedMessageInFirebase(encrypted, currentUserId, recipientUid, 
                                                         attachments, repliedMessageId, callback);
                        } else {
                            callback.onError("Failed to encrypt message");
                        }
                    }
                    
                    @Override
                    public void onError(String error) {
                        callback.onError("Failed to get recipient public key: " + error);
                    }
                });
                return;
            }
            
            if (encryptedMessage != null) {
                // Store encrypted message in Firebase
                storeEncryptedMessageInFirebase(encryptedMessage, currentUserId, recipientUid, 
                                             attachments, repliedMessageId, callback);
            } else {
                callback.onError("Failed to encrypt message");
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Failed to send encrypted message", e);
            callback.onError("Failed to send encrypted message: " + e.getMessage());
        }
    }
    
    /**
     * Stores the encrypted message in Firebase
     */
    private void storeEncryptedMessageInFirebase(MessageEncryption.EncryptedMessage encryptedMessage,
                                               String senderUid, String recipientUid,
                                               Map<String, Object> attachments, String repliedMessageId,
                                               MessageCallback callback) {
        try {
            // Generate unique message ID
            String messageId = database.getReference(SKYLINE_REF).child(CHATS_REF).push().getKey();
            
            // Create encrypted message model
            EncryptedMessageModel encryptedMsg = new EncryptedMessageModel(
                messageId, senderUid, recipientUid,
                encryptedMessage.encryptedAesKey, encryptedMessage.encryptedMessage,
                encryptedMessage.encryptionType, encryptedMessage.timestamp,
                "MESSAGE", attachments, repliedMessageId
            );
            
            // Store in both chat nodes for real-time updates
            database.getReference(SKYLINE_REF).child(CHATS_REF)
                .child(senderUid).child(recipientUid).child(messageId)
                .setValue(encryptedMsg)
                .addOnSuccessListener(aVoid -> {
                    database.getReference(SKYLINE_REF).child(CHATS_REF)
                        .child(recipientUid).child(senderUid).child(messageId)
                        .setValue(encryptedMsg)
                        .addOnSuccessListener(aVoid2 -> {
                            Log.d(TAG, "Encrypted message stored successfully");
                            callback.onSuccess(messageId, encryptedMsg);
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Failed to store in recipient chat node", e);
                            callback.onError("Failed to store message: " + e.getMessage());
                        });
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to store in sender chat node", e);
                    callback.onError("Failed to store message: " + e.getMessage());
                });
                
        } catch (Exception e) {
            Log.e(TAG, "Failed to store encrypted message", e);
            callback.onError("Failed to store encrypted message: " + e.getMessage());
        }
    }
    
    /**
     * Decrypts a received message
     */
    public void decryptReceivedMessage(EncryptedMessageModel encryptedMsg, DecryptCallback callback) {
        try {
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                callback.onError("User not authenticated");
                return;
            }
            
            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            if (currentUserId == null) {
                callback.onError("User not authenticated");
                return;
            }
            
            // Check if this is a session key encrypted message
            if ("AES_SESSION".equals(encryptedMsg.getEncryptionType())) {
                SecretKey sessionKey = keyManager.getSessionKey(
                    encryptedMsg.getSenderUid(), encryptedMsg.getRecipientUid());
                
                if (sessionKey != null) {
                    // Create encrypted message object for decryption
                    MessageEncryption.EncryptedMessage encrypted = new MessageEncryption.EncryptedMessage();
                    encrypted.encryptedMessage = encryptedMsg.getEncryptedMessage();
                    encrypted.encryptionType = encryptedMsg.getEncryptionType();
                    encrypted.timestamp = encryptedMsg.getTimestamp();
                    
                    String decryptedText = MessageEncryption.decryptWithSessionKey(encrypted, sessionKey);
                    if (decryptedText != null) {
                        callback.onSuccess(decryptedText);
                    } else {
                        callback.onError("Failed to decrypt with session key");
                    }
                    return;
                }
            }
            
            // Fall back to RSA decryption
            if (encryptedMsg.getEncryptedAesKey() != null) {
                MessageEncryption.EncryptedMessage encrypted = new MessageEncryption.EncryptedMessage();
                encrypted.encryptedAesKey = encryptedMsg.getEncryptedAesKey();
                encrypted.encryptedMessage = encryptedMsg.getEncryptedMessage();
                encrypted.encryptionType = encryptedMsg.getEncryptionType();
                encrypted.timestamp = encryptedMsg.getTimestamp();
                
                PrivateKey privateKey = keyManager.getPrivateKey(currentUserId);
                if (privateKey != null) {
                    String decryptedText = MessageEncryption.decryptMessage(encrypted, privateKey);
                    if (decryptedText != null) {
                        callback.onSuccess(decryptedText);
                    } else {
                        callback.onError("Failed to decrypt message");
                    }
                } else {
                    callback.onError("Private key not found");
                }
            } else {
                callback.onError("Message not properly encrypted");
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Failed to decrypt received message", e);
            callback.onError("Failed to decrypt message: " + e.getMessage());
        }
    }
    
    // Callback interfaces
    public interface EncryptionCallback {
        void onSuccess();
        void onError(String error);
    }
    
    public interface MessageCallback {
        void onSuccess(String messageId, EncryptedMessageModel encryptedMessage);
        void onError(String error);
    }
    
    public interface DecryptCallback {
        void onSuccess(String decryptedMessage);
        void onError(String error);
    }
}
