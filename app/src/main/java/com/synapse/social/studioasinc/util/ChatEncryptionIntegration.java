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
                                   Map<String, Object> attachments, String repliedMessageId,
                                   ChatEncryptionManager.MessageCallback callback) {
        if ((messageText == null || messageText.trim().isEmpty()) && (attachments == null || attachments.isEmpty())) {
            Log.w(TAG, "Cannot send empty message");
            callback.onError("Cannot send an empty message without attachments.");
            return;
        }
        
        encryptionManager.sendEncryptedMessage(
            messageText != null ? messageText.trim() : "",
            recipientUid,
            attachments,
            repliedMessageId,
            callback
        );
    }
    
    /**
     * Modified message listener for encrypted messages
     * Replace the existing Firebase listener in ChatActivity
     */
    public void setupEncryptedMessageListener(String chatId, String currentUserId, 
                                           MessageListenerCallback callback) {
        DatabaseReference chatRef = database.getReference(SKYLINE_REF)
            .child(CHATS_REF).child(currentUserId).child(chatId);
        
        chatRef.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                if (dataSnapshot.exists()) {
                    GenericTypeIndicator<Map<String, Object>> t = new GenericTypeIndicator<Map<String, Object>>() {};
                    Map<String, Object> messageData = dataSnapshot.getValue(t);
                    if (messageData != null) {
                        handleEncryptedMessage(messageData, currentUserId, "added", callback);
                    }
                }
            }
            
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                if (dataSnapshot.exists()) {
                    GenericTypeIndicator<Map<String, Object>> t = new GenericTypeIndicator<Map<String, Object>>() {};
                    Map<String, Object> messageData = dataSnapshot.getValue(t);
                    if (messageData != null) {
                        handleEncryptedMessage(messageData, currentUserId, "changed", callback);
                    }
                }
            }
            
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                 if (dataSnapshot.exists()) {
                    String messageId = dataSnapshot.getKey();
                    if (messageId != null) {
                        callback.onMessageRemoved(messageId);
                    }
                }
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
    private void handleEncryptedMessage(Map<String, Object> messageData, String currentUserId, String eventType,
                                      MessageListenerCallback callback) {
        try {
            // Safely extract message details with type checking
            Object keyObj = messageData.get("KEY");
            String messageId = keyObj instanceof String ? (String) keyObj : null;

            Object uidObj = messageData.get("uid");
            String senderUid = uidObj instanceof String ? (String) uidObj : null;

            Object aesKeyObj = messageData.get("encrypted_aes_key");
            String encryptedAesKey = aesKeyObj instanceof String ? (String) aesKeyObj : null;

            Object encMsgObj = messageData.get("encrypted_message");
            String encryptedMessage = encMsgObj instanceof String ? (String) encMsgObj : null;

            Object encTypeObj = messageData.get("encryption_type");
            String encryptionType = encTypeObj instanceof String ? (String) encTypeObj : null;

            Object timestampObj = messageData.get("push_date");
            Long timestamp = (timestampObj instanceof Long) ? (Long) timestampObj : null;

            Object typeObj = messageData.get("TYPE");
            String messageType = typeObj instanceof String ? (String) typeObj : null;

            Object repliedIdObj = messageData.get("replied_message_id");
            String repliedMessageId = repliedIdObj instanceof String ? (String) repliedIdObj : null;

            // Validate essential fields
            if (messageId == null || senderUid == null || encryptedMessage == null) {
                Log.e(TAG, "Malformed encrypted message received. Missing critical fields.");
                callback.onDecryptionError(messageId, "Malformed message data");
                return;
            }

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
                    Map<String, Object> decryptedMessageMap = new HashMap<>(messageData);
                    decryptedMessageMap.put("message_text", decryptedMessage);
                    
                    if ("added".equals(eventType)) {
                        callback.onMessageAdded(decryptedMessageMap);
                    } else {
                        callback.onMessageChanged(decryptedMessageMap);
                    }
                }
                
                @Override
                public void onError(String error) {
                    Log.e(TAG, "Failed to decrypt message: " + error);
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
        return FirebaseAuth.getInstance().getCurrentUser() != null && encryptionManager != null;
    }
    
    /**
     * Callback interface for received messages
     */
    public interface MessageListenerCallback {
        void onMessageAdded(Map<String, Object> decryptedMessage);
        void onMessageChanged(Map<String, Object> decryptedMessage);
        void onMessageRemoved(String messageId);
        void onDecryptionError(String messageId, String error);
    }

    public void decryptMessage(Map<String, Object> messageData, DecryptCallback callback) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        try {
            // Safely extract message details with type checking
            Object keyObj = messageData.get("KEY");
            String messageId = keyObj instanceof String ? (String) keyObj : null;

            Object uidObj = messageData.get("uid");
            String senderUid = uidObj instanceof String ? (String) uidObj : null;

            Object aesKeyObj = messageData.get("encrypted_aes_key");
            String encryptedAesKey = aesKeyObj instanceof String ? (String) aesKeyObj : null;

            Object encMsgObj = messageData.get("encrypted_message");
            String encryptedMessage = encMsgObj instanceof String ? (String) encMsgObj : null;

            Object encTypeObj = messageData.get("encryption_type");
            String encryptionType = encTypeObj instanceof String ? (String) encTypeObj : null;

            Object timestampObj = messageData.get("push_date");
            Long timestamp = (timestampObj instanceof Long) ? (Long) timestampObj : null;

            Object typeObj = messageData.get("TYPE");
            String messageType = typeObj instanceof String ? (String) typeObj : null;

            Object repliedIdObj = messageData.get("replied_message_id");
            String repliedMessageId = repliedIdObj instanceof String ? (String) repliedIdObj : null;

            // Validate essential fields
            if (messageId == null || senderUid == null || encryptedMessage == null) {
                Log.e(TAG, "Malformed encrypted message received. Missing critical fields.");
                callback.onError("Malformed message data");
                return;
            }

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
                    Map<String, Object> decryptedMessageMap = new HashMap<>(messageData);
                    decryptedMessageMap.put("message_text", decryptedMessage);

                    callback.onSuccess(decryptedMessageMap);
                }

                @Override
                public void onError(String error) {
                    Log.e(TAG, "Failed to decrypt message: " + error);
                    callback.onError(error);
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Failed to handle encrypted message", e);
            callback.onError(e.getMessage());
        }
    }

    public interface DecryptCallback {
        void onSuccess(Map<String, Object> decryptedMessage);
        void onError(String error);
    }
}
