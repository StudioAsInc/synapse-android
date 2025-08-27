package com.synapse.social.studioasinc.groupchat.service;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 -2\u00020\u0001:\u0001-B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\f\u001a\u00020\rH\u0002J\u001c\u0010\u000e\u001a\u00020\r2\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u00110\u0010H\u0002J\u001c\u0010\u0012\u001a\u00020\r2\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u00110\u0010H\u0002J\u001c\u0010\u0013\u001a\u00020\r2\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u00110\u0010H\u0002J\u001c\u0010\u0014\u001a\u00020\r2\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u00110\u0010H\u0002J\u001c\u0010\u0015\u001a\u00020\r2\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u00110\u0010H\u0002J$\u0010\u0016\u001a\u00020\r2\u0006\u0010\u0017\u001a\u00020\u00182\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u00110\u0010H\u0002J\u001c\u0010\u0019\u001a\u00020\r2\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u00110\u0010H\u0002J\b\u0010\u001a\u001a\u00020\rH\u0016J\u0010\u0010\u001b\u001a\u00020\r2\u0006\u0010\u001c\u001a\u00020\u001dH\u0016J\u0010\u0010\u001e\u001a\u00020\r2\u0006\u0010\u001f\u001a\u00020\u0011H\u0016JZ\u0010 \u001a\u00020\r2\u0006\u0010!\u001a\u00020\u00112\u0006\u0010\"\u001a\u00020\u00112\b\u0010#\u001a\u0004\u0018\u00010\u00112\b\u0010$\u001a\u0004\u0018\u00010\u00112\n\b\u0002\u0010%\u001a\u0004\u0018\u00010\u00112\n\b\u0002\u0010&\u001a\u0004\u0018\u00010\u00112\n\b\u0002\u0010\'\u001a\u0004\u0018\u00010\u00112\b\b\u0002\u0010(\u001a\u00020)H\u0002J\u001a\u0010*\u001a\u00020\r2\u0006\u0010+\u001a\u00020,2\b\u0010#\u001a\u0004\u0018\u00010\u0011H\u0002R\u0010\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0005R\u001e\u0010\u0006\u001a\u00020\u00078\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000b\u00a8\u0006."}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/service/GroupChatFirebaseMessagingService;", "Lcom/google/firebase/messaging/FirebaseMessagingService;", "()V", "serviceScope", "error/NonExistentClass", "Lerror/NonExistentClass;", "userRepository", "Lcom/synapse/social/studioasinc/groupchat/data/repository/UserRepository;", "getUserRepository", "()Lcom/synapse/social/studioasinc/groupchat/data/repository/UserRepository;", "setUserRepository", "(Lcom/synapse/social/studioasinc/groupchat/data/repository/UserRepository;)V", "createNotificationChannel", "", "handleDataMessage", "data", "", "", "handleGroupInvite", "handleGroupMessage", "handleMemberAdded", "handleMemberRemoved", "handleNotificationMessage", "notification", "Lcom/google/firebase/messaging/RemoteMessage$Notification;", "handleRoleChanged", "onDestroy", "onMessageReceived", "remoteMessage", "Lcom/google/firebase/messaging/RemoteMessage;", "onNewToken", "token", "showNotification", "title", "message", "groupId", "groupName", "senderId", "senderName", "senderPhotoUrl", "isSystemMessage", "", "showNotificationInternal", "notificationBuilder", "Landroidx/core/app/NotificationCompat$Builder;", "Companion", "app_release"})
public final class GroupChatFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    @javax.inject.Inject()
    public com.synapse.social.studioasinc.groupchat.data.repository.UserRepository userRepository;
    @org.jetbrains.annotations.NotNull()
    private final error.NonExistentClass serviceScope = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String CHANNEL_ID = "group_chat_messages";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String CHANNEL_NAME = "Group Chat Messages";
    private static final int NOTIFICATION_ID_BASE = 1000;
    @org.jetbrains.annotations.NotNull()
    public static final com.synapse.social.studioasinc.groupchat.service.GroupChatFirebaseMessagingService.Companion Companion = null;
    
    public GroupChatFirebaseMessagingService() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.synapse.social.studioasinc.groupchat.data.repository.UserRepository getUserRepository() {
        return null;
    }
    
    public final void setUserRepository(@org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.repository.UserRepository p0) {
    }
    
    @java.lang.Override()
    public void onNewToken(@org.jetbrains.annotations.NotNull()
    java.lang.String token) {
    }
    
    @java.lang.Override()
    public void onMessageReceived(@org.jetbrains.annotations.NotNull()
    com.google.firebase.messaging.RemoteMessage remoteMessage) {
    }
    
    private final void handleDataMessage(java.util.Map<java.lang.String, java.lang.String> data) {
    }
    
    private final void handleNotificationMessage(com.google.firebase.messaging.RemoteMessage.Notification notification, java.util.Map<java.lang.String, java.lang.String> data) {
    }
    
    private final void handleGroupMessage(java.util.Map<java.lang.String, java.lang.String> data) {
    }
    
    private final void handleGroupInvite(java.util.Map<java.lang.String, java.lang.String> data) {
    }
    
    private final void handleMemberAdded(java.util.Map<java.lang.String, java.lang.String> data) {
    }
    
    private final void handleMemberRemoved(java.util.Map<java.lang.String, java.lang.String> data) {
    }
    
    private final void handleRoleChanged(java.util.Map<java.lang.String, java.lang.String> data) {
    }
    
    private final void showNotification(java.lang.String title, java.lang.String message, java.lang.String groupId, java.lang.String groupName, java.lang.String senderId, java.lang.String senderName, java.lang.String senderPhotoUrl, boolean isSystemMessage) {
    }
    
    private final void showNotificationInternal(androidx.core.app.NotificationCompat.Builder notificationBuilder, java.lang.String groupId) {
    }
    
    private final void createNotificationChannel() {
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/service/GroupChatFirebaseMessagingService$Companion;", "", "()V", "CHANNEL_ID", "", "CHANNEL_NAME", "NOTIFICATION_ID_BASE", "", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}