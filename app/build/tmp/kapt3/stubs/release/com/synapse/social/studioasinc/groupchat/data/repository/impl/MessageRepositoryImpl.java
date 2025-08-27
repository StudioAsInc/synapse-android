package com.synapse.social.studioasinc.groupchat.data.repository.impl;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u009a\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0016\n\u0002\u0010$\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u001b\n\u0002\u0010\t\n\u0002\b\r\b\u0007\u0018\u00002\u00020\u0001B1\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0004\b\f\u0010\rJ\u001c\u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0\u00190\u00182\u0006\u0010\u001b\u001a\u00020\u0015H\u0016J$\u0010\u001c\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0\u001d0\u00182\u0006\u0010\u001b\u001a\u00020\u00152\u0006\u0010\u001e\u001a\u00020\u001fH\u0016J,\u0010 \u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0\u001d0!2\u0006\u0010\u001b\u001a\u00020\u00152\u0006\u0010\u001e\u001a\u00020\u001fH\u0096@\u00a2\u0006\u0004\b\"\u0010#J \u0010$\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u001a0!2\u0006\u0010%\u001a\u00020\u0015H\u0096@\u00a2\u0006\u0004\b&\u0010\'J\u0018\u0010(\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u001a0\u00182\u0006\u0010%\u001a\u00020\u0015H\u0016J\u001e\u0010)\u001a\b\u0012\u0004\u0012\u00020\u00150!2\u0006\u0010*\u001a\u00020\u001aH\u0096@\u00a2\u0006\u0004\b+\u0010,J,\u0010-\u001a\b\u0012\u0004\u0012\u00020\u00150!2\u0006\u0010*\u001a\u00020\u001a2\f\u0010.\u001a\b\u0012\u0004\u0012\u00020/0\u001dH\u0096@\u00a2\u0006\u0004\b0\u00101J&\u00102\u001a\b\u0012\u0004\u0012\u0002030!2\u0006\u0010%\u001a\u00020\u00152\u0006\u00104\u001a\u00020\u0015H\u0096@\u00a2\u0006\u0004\b5\u00106J&\u00107\u001a\b\u0012\u0004\u0012\u0002030!2\u0006\u0010%\u001a\u00020\u00152\u0006\u00108\u001a\u000209H\u0096@\u00a2\u0006\u0004\b:\u0010;J,\u0010<\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0\u001d0!2\u0006\u0010\u001b\u001a\u00020\u00152\u0006\u0010=\u001a\u00020\u0015H\u0096@\u00a2\u0006\u0004\b>\u00106J&\u0010?\u001a\b\u0012\u0004\u0012\u0002030!2\u0006\u0010%\u001a\u00020\u00152\u0006\u0010@\u001a\u00020\u0015H\u0096@\u00a2\u0006\u0004\bA\u00106J&\u0010B\u001a\b\u0012\u0004\u0012\u0002030!2\u0006\u0010\u001b\u001a\u00020\u00152\u0006\u0010@\u001a\u00020\u0015H\u0096@\u00a2\u0006\u0004\bC\u00106J&\u0010D\u001a\b\u0012\u0004\u0012\u0002030!2\u0006\u0010%\u001a\u00020\u00152\u0006\u0010E\u001a\u00020\u0015H\u0096@\u00a2\u0006\u0004\bF\u00106J&\u0010G\u001a\b\u0012\u0004\u0012\u00020\u001f0!2\u0006\u0010\u001b\u001a\u00020\u00152\u0006\u0010@\u001a\u00020\u0015H\u0096@\u00a2\u0006\u0004\bH\u00106J.\u0010I\u001a\b\u0012\u0004\u0012\u0002030!2\u0006\u0010%\u001a\u00020\u00152\u0006\u0010@\u001a\u00020\u00152\u0006\u0010J\u001a\u00020\u0015H\u0096@\u00a2\u0006\u0004\bK\u0010LJ.\u0010M\u001a\b\u0012\u0004\u0012\u0002030!2\u0006\u0010%\u001a\u00020\u00152\u0006\u0010@\u001a\u00020\u00152\u0006\u0010J\u001a\u00020\u0015H\u0096@\u00a2\u0006\u0004\bN\u0010LJ0\u0010O\u001a\u001a\u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\u0015\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00150\u001d0P0!2\u0006\u0010%\u001a\u00020\u0015H\u0096@\u00a2\u0006\u0004\bQ\u0010\'J.\u0010R\u001a\b\u0012\u0004\u0012\u00020S0!2\u0006\u0010\u001b\u001a\u00020\u00152\u0006\u0010T\u001a\u00020/2\u0006\u0010U\u001a\u00020\u0015H\u0096@\u00a2\u0006\u0004\bV\u0010WJ&\u0010X\u001a\b\u0012\u0004\u0012\u0002030!2\u0006\u0010Y\u001a\u00020S2\u0006\u0010Z\u001a\u00020/H\u0096@\u00a2\u0006\u0004\b[\u0010\\J\u001e\u0010]\u001a\b\u0012\u0004\u0012\u0002030!2\u0006\u0010Y\u001a\u00020SH\u0096@\u00a2\u0006\u0004\b^\u0010_J&\u0010`\u001a\b\u0012\u0004\u0012\u00020\u00150!2\u0006\u0010a\u001a\u00020\u00152\u0006\u0010b\u001a\u00020\u001aH\u0096@\u00a2\u0006\u0004\bc\u0010dJ$\u0010e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0\u001d0!2\u0006\u0010%\u001a\u00020\u0015H\u0096@\u00a2\u0006\u0004\bf\u0010\'J$\u0010g\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0\u001d0!2\u0006\u0010\u001b\u001a\u00020\u0015H\u0096@\u00a2\u0006\u0004\bh\u0010\'J\u001e\u0010i\u001a\b\u0012\u0004\u0012\u0002030!2\u0006\u0010\u001b\u001a\u00020\u0015H\u0096@\u00a2\u0006\u0004\bj\u0010\'J\u001e\u0010k\u001a\b\u0012\u0004\u0012\u0002030!2\u0006\u0010\u001b\u001a\u00020\u0015H\u0096@\u00a2\u0006\u0004\bl\u0010\'J4\u0010m\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0\u001d0!2\u0006\u0010\u001b\u001a\u00020\u00152\u0006\u0010n\u001a\u00020o2\u0006\u0010\u001e\u001a\u00020\u001fH\u0096@\u00a2\u0006\u0004\bp\u0010qJ,\u0010r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0\u001d0!2\u0006\u0010\u001b\u001a\u00020\u00152\u0006\u0010@\u001a\u00020\u0015H\u0096@\u00a2\u0006\u0004\bs\u00106J\u001e\u0010t\u001a\b\u0012\u0004\u0012\u00020\u001f0!2\u0006\u0010\u001b\u001a\u00020\u0015H\u0096@\u00a2\u0006\u0004\bu\u0010\'J\u0016\u0010v\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00182\u0006\u0010\u001b\u001a\u00020\u0015H\u0016J\u0010\u0010w\u001a\u0002032\u0006\u0010\u001b\u001a\u00020\u0015H\u0016J\u0016\u0010x\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00182\u0006\u0010%\u001a\u00020\u0015H\u0016J\u0010\u0010y\u001a\u0002032\u0006\u0010%\u001a\u00020\u0015H\u0016J\u0010\u0010z\u001a\u00020\u00152\u0006\u0010T\u001a\u00020/H\u0002J\u0010\u0010{\u001a\u00020\u00152\u0006\u0010T\u001a\u00020/H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020\u00160\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006|"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/data/repository/impl/MessageRepositoryImpl;", "Lcom/synapse/social/studioasinc/groupchat/data/repository/MessageRepository;", "messageDao", "Lcom/synapse/social/studioasinc/groupchat/data/local/dao/GroupMessageDao;", "memberDao", "Lcom/synapse/social/studioasinc/groupchat/data/local/dao/GroupMemberDao;", "firebaseAuth", "Lcom/google/firebase/auth/FirebaseAuth;", "firebaseDatabase", "Lcom/google/firebase/database/FirebaseDatabase;", "firebaseStorage", "Lcom/google/firebase/storage/FirebaseStorage;", "<init>", "(Lcom/synapse/social/studioasinc/groupchat/data/local/dao/GroupMessageDao;Lcom/synapse/social/studioasinc/groupchat/data/local/dao/GroupMemberDao;Lcom/google/firebase/auth/FirebaseAuth;Lcom/google/firebase/database/FirebaseDatabase;Lcom/google/firebase/storage/FirebaseStorage;)V", "messagesRef", "Lcom/google/firebase/database/DatabaseReference;", "groupsRef", "storageRef", "Lcom/google/firebase/storage/StorageReference;", "messageListeners", "", "", "Lcom/google/firebase/database/ValueEventListener;", "getGroupMessagesPaged", "Lkotlinx/coroutines/flow/Flow;", "Landroidx/paging/PagingData;", "Lcom/synapse/social/studioasinc/groupchat/data/model/GroupMessage;", "groupId", "getRecentMessagesFlow", "", "limit", "", "getRecentMessages", "Lkotlin/Result;", "getRecentMessages-0E7RQCE", "(Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getMessageById", "messageId", "getMessageById-gIAlu-s", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getMessageByIdFlow", "sendMessage", "message", "sendMessage-gIAlu-s", "(Lcom/synapse/social/studioasinc/groupchat/data/model/GroupMessage;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "sendMessageWithAttachments", "attachmentFiles", "Ljava/io/File;", "sendMessageWithAttachments-0E7RQCE", "(Lcom/synapse/social/studioasinc/groupchat/data/model/GroupMessage;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "editMessage", "", "newText", "editMessage-0E7RQCE", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteMessage", "isHardDelete", "", "deleteMessage-0E7RQCE", "(Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "searchMessages", "query", "searchMessages-0E7RQCE", "markMessageAsSeen", "userId", "markMessageAsSeen-0E7RQCE", "markMessagesAsSeen", "markMessagesAsSeen-0E7RQCE", "updateDeliveryStatus", "status", "updateDeliveryStatus-0E7RQCE", "getUnreadMessageCount", "getUnreadMessageCount-0E7RQCE", "addReaction", "emoji", "addReaction-BWLJW6A", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "removeReaction", "removeReaction-BWLJW6A", "getMessageReactions", "", "getMessageReactions-gIAlu-s", "uploadAttachment", "Lcom/synapse/social/studioasinc/groupchat/data/model/MessageAttachment;", "file", "attachmentType", "uploadAttachment-BWLJW6A", "(Ljava/lang/String;Ljava/io/File;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "downloadAttachment", "attachment", "destinationFile", "downloadAttachment-0E7RQCE", "(Lcom/synapse/social/studioasinc/groupchat/data/model/MessageAttachment;Ljava/io/File;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteAttachment", "deleteAttachment-gIAlu-s", "(Lcom/synapse/social/studioasinc/groupchat/data/model/MessageAttachment;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "replyToMessage", "originalMessageId", "replyMessage", "replyToMessage-0E7RQCE", "(Ljava/lang/String;Lcom/synapse/social/studioasinc/groupchat/data/model/GroupMessage;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getMessageReplies", "getMessageReplies-gIAlu-s", "getPendingMessages", "getPendingMessages-gIAlu-s", "retryFailedMessages", "retryFailedMessages-gIAlu-s", "syncMessages", "syncMessages-gIAlu-s", "loadMoreMessages", "beforeTimestamp", "", "loadMoreMessages-BWLJW6A", "(Ljava/lang/String;JILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getMessagesByUser", "getMessagesByUser-0E7RQCE", "getMessageCount", "getMessageCount-gIAlu-s", "startListeningToMessages", "stopListeningToMessages", "startListeningToMessageUpdates", "stopListeningToMessageUpdates", "getAttachmentType", "getMimeType", "app_release"})
public final class MessageRepositoryImpl implements com.synapse.social.studioasinc.groupchat.data.repository.MessageRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.synapse.social.studioasinc.groupchat.data.local.dao.GroupMessageDao messageDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.synapse.social.studioasinc.groupchat.data.local.dao.GroupMemberDao memberDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.auth.FirebaseAuth firebaseAuth = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.database.FirebaseDatabase firebaseDatabase = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.storage.FirebaseStorage firebaseStorage = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.database.DatabaseReference messagesRef = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.database.DatabaseReference groupsRef = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.storage.StorageReference storageRef = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<java.lang.String, com.google.firebase.database.ValueEventListener> messageListeners = null;
    
    @javax.inject.Inject()
    public MessageRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.local.dao.GroupMessageDao messageDao, @org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.local.dao.GroupMemberDao memberDao, @org.jetbrains.annotations.NotNull()
    com.google.firebase.auth.FirebaseAuth firebaseAuth, @org.jetbrains.annotations.NotNull()
    com.google.firebase.database.FirebaseDatabase firebaseDatabase, @org.jetbrains.annotations.NotNull()
    com.google.firebase.storage.FirebaseStorage firebaseStorage) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<androidx.paging.PagingData<com.synapse.social.studioasinc.groupchat.data.model.GroupMessage>> getGroupMessagesPaged(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.synapse.social.studioasinc.groupchat.data.model.GroupMessage>> getRecentMessagesFlow(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId, int limit) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<com.synapse.social.studioasinc.groupchat.data.model.GroupMessage> getMessageByIdFlow(@org.jetbrains.annotations.NotNull()
    java.lang.String messageId) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<com.synapse.social.studioasinc.groupchat.data.model.GroupMessage> startListeningToMessages(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId) {
        return null;
    }
    
    @java.lang.Override()
    public void stopListeningToMessages(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId) {
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<com.synapse.social.studioasinc.groupchat.data.model.GroupMessage> startListeningToMessageUpdates(@org.jetbrains.annotations.NotNull()
    java.lang.String messageId) {
        return null;
    }
    
    @java.lang.Override()
    public void stopListeningToMessageUpdates(@org.jetbrains.annotations.NotNull()
    java.lang.String messageId) {
    }
    
    private final java.lang.String getAttachmentType(java.io.File file) {
        return null;
    }
    
    private final java.lang.String getMimeType(java.io.File file) {
        return null;
    }
}