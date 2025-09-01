package com.synapse.social.studioasinc.util;

import android.content.Context;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import com.synapse.social.studioasinc.model.EncryptedMessageModel;

/**
 * Example integration class showing how to modify existing ChatActivity
 * to use E2E encryption
 */
public class ChatEncryptionIntegration {
    private static final String TAG = "ChatEncryptionIntegration";
    private static final String SKYLINE_REF = "skyline";
    private static final String CHATS_REF = "chats";
    
    private final Context context;
    private final ChatEncryptionManager encryptionManager;
    private final FirebaseDatabase database;
    
    public ChatEncryptionIntegration(Context context) {
        this.context = context;
        this.encryptionManager = new ChatEncryptionManager(context);
        this.database = FirebaseDatabase.getInstance();
    }
    
    /**
     * Initialize encryption when ChatActivity starts
     */
    public void initializeEncryption() {
        encryptionManager.initializeCurrentUserEncryption(new ChatEncryptionManager.EncryptionCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Encryption initialized successfully");
            }
            
            @Override
            public void onError(String error) {
                Log.e(TAG, "Failed to initialize encryption: " + error);
            }
        });
    }
    
    /**
     * Modified _send_btn() method for encrypted messages
     * Replace the existing _send_btn() in ChatActivity with this
     */
    public void sendEncryptedMessage(String messageText, String recipientUid, 
                                   Map<String, Object> attachments, String repliedMessageId) {
        if (messageText == null || messageText.trim().isEmpty()) {
            Log.w(TAG, "Cannot send empty message");
            return;
        }
        
        encryptionManager.sendEncryptedMessage(
            messageText.trim(),
            recipientUid,
            attachments,
            repliedMessageId,
            new ChatEncryptionManager.MessageCallback() {
                @Override
                public void onSuccess(String messageId, Object encryptedMessage) {
                    Log.d(TAG, "Encrypted message sent successfully: " + messageId);
                    // Update UI or handle success
                }
                
                @Override
                public void onError(String error) {
                    Log.e(TAG, "Failed to send encrypted message: " + error);
                    // Handle error in UI
                }
            }
        );
    }
    
    /**
     * Modified message listener for encrypted messages
     * Replace the existing Firebase listener in ChatActivity
     */
    public void setupEncryptedMessageListener(String chatId, String currentUserId, 
                                           MessageReceivedCallback callback) {
        DatabaseReference chatRef = database.getReference(SKYLINE_REF)
            .child(CHATS_REF).child(currentUserId).child(chatId);
        
        chatRef.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                if (dataSnapshot.exists()) {
                    // Parse the encrypted message
                    Map<String, Object> messageData = (Map<String, Object>) dataSnapshot.getValue();
                    if (messageData != null) {
                        handleEncryptedMessage(messageData, currentUserId, callback);
                    }
                }
            }
            
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                // Handle message updates if needed
            }
            
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // Handle message deletion if needed
            }
            
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                // Not used for chat
            }
            
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Message listener cancelled", databaseError.toException());
            }
        });
    }
    
    /**
     * Handle incoming encrypted messages
     */
    private void handleEncryptedMessage(Map<String, Object> messageData, String currentUserId,
                                      MessageReceivedCallback callback) {
        try {
            // Extract message details
            String messageId = (String) messageData.get("KEY");
            String senderUid = (String) messageData.get("uid");
            String encryptedAesKey = (String) messageData.get("encrypted_aes_key");
            String encryptedMessage = (String) messageData.get("encrypted_message");
            String encryptionType = (String) messageData.get("encryption_type");
            Long timestamp = (Long) messageData.get("push_date");
            String messageType = (String) messageData.get("TYPE");
            String repliedMessageId = (String) messageData.get("replied_message_id");
            
            // Create encrypted message model
            EncryptedMessageModel encryptedMsg = new EncryptedMessageModel();
            encryptedMsg.setMessageId(messageId);
            encryptedMsg.setSenderUid(senderUid);
            encryptedMsg.setRecipientUid(currentUserId);
            encryptedMsg.setEncryptedAesKey(encryptedAesKey);
            encryptedMsg.setEncryptedMessage(encryptedMessage);
            encryptedMsg.setEncryptionType(encryptionType);
            encryptedMsg.setTimestamp(timestamp != null ? timestamp : System.currentTimeMillis());
            encryptedMsg.setMessageType(messageType);
            encryptedMsg.setRepliedMessageId(repliedMessageId);
            
            // Decrypt the message
            encryptionManager.decryptReceivedMessage(encryptedMsg, new ChatEncryptionManager.DecryptCallback() {
                @Override
                public void onSuccess(String decryptedMessage) {
                    Log.d(TAG, "Message decrypted successfully");
                    
                    // Create message map for existing chat system
                    Map<String, Object> decryptedMessageMap = new HashMap<>();
                    decryptedMessageMap.put("KEY", messageId);
                    decryptedMessageMap.put("uid", senderUid);
                    decryptedMessageMap.put("message_text", decryptedMessage);
                    decryptedMessageMap.put("TYPE", messageType);
                    decryptedMessageMap.put("push_date", timestamp);
                    decryptedMessageMap.put("replied_message_id", repliedMessageId);
                    
                    // Call the callback with decrypted message
                    callback.onMessageReceived(decryptedMessageMap);
                }
                
                @Override
                public void onError(String error) {
                    Log.e(TAG, "Failed to decrypt message: " + error);
                    // Handle decryption error - maybe show encrypted message indicator
                    callback.onDecryptionError(messageId, error);
                }
            });
            
        } catch (Exception e) {
            Log.e(TAG, "Failed to handle encrypted message", e);
        }
    }
    
    /**
     * Check if encryption is available
     */
    public boolean isEncryptionAvailable() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return currentUserId != null && encryptionManager != null;
    }
    
    /**
     * Callback interface for received messages
     */
    public interface MessageReceivedCallback {
        void onMessageReceived(Map<String, Object> decryptedMessage);
        void onDecryptionError(String messageId, String error);
    }
}
