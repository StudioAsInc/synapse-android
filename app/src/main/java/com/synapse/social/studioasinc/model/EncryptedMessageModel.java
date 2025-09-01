package com.synapse.social.studioasinc.model;

import java.util.Map;

/**
 * Model class for encrypted messages stored in Firebase
 */
public class EncryptedMessageModel {
    private String messageId;
    private String senderUid;
    private String recipientUid;
    private String encryptedAesKey;
    private String encryptedMessage;
    private String encryptionType;
    private long timestamp;
    private String messageType;
    private Map<String, Object> attachments;
    private String repliedMessageId;
    
    public EncryptedMessageModel() {
        // Required for Firebase
    }
    
    public EncryptedMessageModel(String messageId, String senderUid, String recipientUid,
                               String encryptedAesKey, String encryptedMessage, String encryptionType,
                               long timestamp, String messageType, Map<String, Object> attachments,
                               String repliedMessageId) {
        this.messageId = messageId;
        this.senderUid = senderUid;
        this.recipientUid = recipientUid;
        this.encryptedAesKey = encryptedAesKey;
        this.encryptedMessage = encryptedMessage;
        this.encryptionType = encryptionType;
        this.timestamp = timestamp;
        this.messageType = messageType;
        this.attachments = attachments;
        this.repliedMessageId = repliedMessageId;
    }
    
    // Getters and setters
    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }
    
    public String getSenderUid() { return senderUid; }
    public void setSenderUid(String senderUid) { this.senderUid = senderUid; }
    
    public String getRecipientUid() { return recipientUid; }
    public void setRecipientUid(String recipientUid) { this.recipientUid = recipientUid; }
    
    public String getEncryptedAesKey() { return encryptedAesKey; }
    public void setEncryptedAesKey(String encryptedAesKey) { this.encryptedAesKey = encryptedAesKey; }
    
    public String getEncryptedMessage() { return encryptedMessage; }
    public void setEncryptedMessage(String encryptedMessage) { this.encryptedMessage = encryptedMessage; }
    
    public String getEncryptionType() { return encryptionType; }
    public void setEncryptionType(String encryptionType) { this.encryptionType = encryptionType; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    
    public String getMessageType() { return messageType; }
    public void setMessageType(String messageType) { this.messageType = messageType; }
    
    public Map<String, Object> getAttachments() { return attachments; }
    public void setAttachments(Map<String, Object> attachments) { this.attachments = attachments; }
    
    public String getRepliedMessageId() { return repliedMessageId; }
    public void setRepliedMessageId(String repliedMessageId) { this.repliedMessageId = repliedMessageId; }
}
