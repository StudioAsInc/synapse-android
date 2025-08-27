package com.synapse.social.studioasinc.groupchat.data.repository.impl;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u008e\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0010 \n\u0002\b\u0011\n\u0002\u0010\t\n\u0002\b(\b\u0007\u0018\u00002\u00020\u0001B/\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ4\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u00162\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u00192\u0006\u0010\u001b\u001a\u00020\u0019H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001c\u0010\u001dJ$\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00170\u00162\u0006\u0010\u001f\u001a\u00020 H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b!\u0010\"J,\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00170\u00162\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010$\u001a\u00020%H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b&\u0010\'J,\u0010(\u001a\b\u0012\u0004\u0012\u00020\u00170\u00162\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010)\u001a\u00020*H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b+\u0010,J,\u0010-\u001a\b\u0012\u0004\u0012\u00020\u00170\u00162\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010.\u001a\u00020\u0019H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b/\u00100J\u0010\u00101\u001a\u00020\u00192\u0006\u00102\u001a\u00020*H\u0002J\u001c\u00103\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020605042\u0006\u00107\u001a\u00020\u0019H\u0016J&\u00108\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001060\u00162\u0006\u0010\u0018\u001a\u00020\u0019H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b9\u0010:J\u0018\u0010;\u001a\n\u0012\u0006\u0012\u0004\u0018\u000106042\u0006\u0010\u0018\u001a\u00020\u0019H\u0016J$\u0010<\u001a\b\u0012\u0004\u0012\u00020=0\u00162\u0006\u00107\u001a\u00020\u0019H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b>\u0010:J6\u0010?\u001a\u001a\u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00190A0@0\u00162\u0006\u0010\u0018\u001a\u00020\u0019H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bB\u0010:J*\u0010C\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002060A0\u00162\u0006\u0010\u0018\u001a\u00020\u0019H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bD\u0010:J2\u0010E\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002060A0\u00162\u0006\u00107\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0019H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bF\u00100J\u0010\u0010G\u001a\u00020\u00192\u0006\u00102\u001a\u00020*H\u0002J*\u0010H\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002060A0\u00162\u0006\u00107\u001a\u00020\u0019H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bI\u0010:J2\u0010J\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002060A0\u00162\u0006\u00107\u001a\u00020\u00192\u0006\u0010K\u001a\u00020=H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bL\u0010MJ$\u0010N\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002060A042\u0006\u00107\u001a\u00020\u00192\u0006\u0010K\u001a\u00020=H\u0016J,\u0010O\u001a\b\u0012\u0004\u0012\u00020=0\u00162\u0006\u00107\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0019H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bP\u00100J:\u0010Q\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002060A0\u00162\u0006\u00107\u001a\u00020\u00192\u0006\u0010R\u001a\u00020S2\u0006\u0010K\u001a\u00020=H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bT\u0010UJ,\u0010V\u001a\b\u0012\u0004\u0012\u00020\u00170\u00162\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0019H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bW\u00100J,\u0010X\u001a\b\u0012\u0004\u0012\u00020\u00170\u00162\u0006\u00107\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0019H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bY\u00100J4\u0010Z\u001a\b\u0012\u0004\u0012\u00020\u00170\u00162\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u00192\u0006\u0010\u001b\u001a\u00020\u0019H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b[\u0010\u001dJ,\u0010\\\u001a\b\u0012\u0004\u0012\u00020\u00190\u00162\u0006\u0010]\u001a\u00020\u00192\u0006\u0010^\u001a\u000206H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b_\u0010`J$\u0010a\u001a\b\u0012\u0004\u0012\u00020\u00170\u00162\u0006\u00107\u001a\u00020\u0019H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bb\u0010:J2\u0010c\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002060A0\u00162\u0006\u00107\u001a\u00020\u00192\u0006\u0010d\u001a\u00020\u0019H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\be\u00100J$\u0010f\u001a\b\u0012\u0004\u0012\u00020\u00190\u00162\u0006\u0010g\u001a\u000206H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bh\u0010iJ2\u0010j\u001a\b\u0012\u0004\u0012\u00020\u00190\u00162\u0006\u0010g\u001a\u0002062\f\u0010k\u001a\b\u0012\u0004\u0012\u00020*0AH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bl\u0010mJ\u0016\u0010n\u001a\b\u0012\u0004\u0012\u000206042\u0006\u0010\u0018\u001a\u00020\u0019H\u0016J\u0016\u0010o\u001a\b\u0012\u0004\u0012\u000206042\u0006\u00107\u001a\u00020\u0019H\u0016J\u0010\u0010p\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019H\u0016J\u0010\u0010q\u001a\u00020\u00172\u0006\u00107\u001a\u00020\u0019H\u0016J$\u0010r\u001a\b\u0012\u0004\u0012\u00020\u00170\u00162\u0006\u00107\u001a\u00020\u0019H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bs\u0010:J,\u0010t\u001a\b\u0012\u0004\u0012\u00020\u00170\u00162\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010u\u001a\u00020\u0019H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bv\u00100J4\u0010w\u001a\b\u0012\u0004\u0012\u00020 0\u00162\u0006\u00107\u001a\u00020\u00192\u0006\u00102\u001a\u00020*2\u0006\u0010x\u001a\u00020\u0019H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\by\u0010zR\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0011R\u000e\u0010\u0012\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006{"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/data/repository/impl/MessageRepositoryImpl;", "Lcom/synapse/social/studioasinc/groupchat/data/repository/MessageRepository;", "messageDao", "Lcom/synapse/social/studioasinc/groupchat/data/local/dao/GroupMessageDao;", "memberDao", "Lcom/synapse/social/studioasinc/groupchat/data/local/dao/GroupMemberDao;", "firebaseAuth", "Lcom/google/firebase/auth/FirebaseAuth;", "firebaseDatabase", "Lcom/google/firebase/database/FirebaseDatabase;", "firebaseStorage", "Lcom/google/firebase/storage/FirebaseStorage;", "(Lcom/synapse/social/studioasinc/groupchat/data/local/dao/GroupMessageDao;Lcom/synapse/social/studioasinc/groupchat/data/local/dao/GroupMemberDao;Lcom/google/firebase/auth/FirebaseAuth;Lcom/google/firebase/database/FirebaseDatabase;Lcom/google/firebase/storage/FirebaseStorage;)V", "groupsRef", "Lcom/google/firebase/database/DatabaseReference;", "messageListeners", "error/NonExistentClass", "Lerror/NonExistentClass;", "messagesRef", "storageRef", "Lcom/google/firebase/storage/StorageReference;", "addReaction", "Lkotlin/Result;", "", "messageId", "", "userId", "emoji", "addReaction-BWLJW6A", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteAttachment", "attachment", "Lcom/synapse/social/studioasinc/groupchat/data/model/MessageAttachment;", "deleteAttachment-gIAlu-s", "(Lcom/synapse/social/studioasinc/groupchat/data/model/MessageAttachment;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteMessage", "isHardDelete", "", "deleteMessage-0E7RQCE", "(Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "downloadAttachment", "destinationFile", "Ljava/io/File;", "downloadAttachment-0E7RQCE", "(Lcom/synapse/social/studioasinc/groupchat/data/model/MessageAttachment;Ljava/io/File;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "editMessage", "newText", "editMessage-0E7RQCE", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAttachmentType", "file", "getGroupMessagesPaged", "Lkotlinx/coroutines/flow/Flow;", "Landroidx/paging/PagingData;", "Lcom/synapse/social/studioasinc/groupchat/data/model/GroupMessage;", "groupId", "getMessageById", "getMessageById-gIAlu-s", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getMessageByIdFlow", "getMessageCount", "", "getMessageCount-gIAlu-s", "getMessageReactions", "", "", "getMessageReactions-gIAlu-s", "getMessageReplies", "getMessageReplies-gIAlu-s", "getMessagesByUser", "getMessagesByUser-0E7RQCE", "getMimeType", "getPendingMessages", "getPendingMessages-gIAlu-s", "getRecentMessages", "limit", "getRecentMessages-0E7RQCE", "(Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getRecentMessagesFlow", "getUnreadMessageCount", "getUnreadMessageCount-0E7RQCE", "loadMoreMessages", "beforeTimestamp", "", "loadMoreMessages-BWLJW6A", "(Ljava/lang/String;JILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "markMessageAsSeen", "markMessageAsSeen-0E7RQCE", "markMessagesAsSeen", "markMessagesAsSeen-0E7RQCE", "removeReaction", "removeReaction-BWLJW6A", "replyToMessage", "originalMessageId", "replyMessage", "replyToMessage-0E7RQCE", "(Ljava/lang/String;Lcom/synapse/social/studioasinc/groupchat/data/model/GroupMessage;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "retryFailedMessages", "retryFailedMessages-gIAlu-s", "searchMessages", "query", "searchMessages-0E7RQCE", "sendMessage", "message", "sendMessage-gIAlu-s", "(Lcom/synapse/social/studioasinc/groupchat/data/model/GroupMessage;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "sendMessageWithAttachments", "attachmentFiles", "sendMessageWithAttachments-0E7RQCE", "(Lcom/synapse/social/studioasinc/groupchat/data/model/GroupMessage;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "startListeningToMessageUpdates", "startListeningToMessages", "stopListeningToMessageUpdates", "stopListeningToMessages", "syncMessages", "syncMessages-gIAlu-s", "updateDeliveryStatus", "status", "updateDeliveryStatus-0E7RQCE", "uploadAttachment", "attachmentType", "uploadAttachment-BWLJW6A", "(Ljava/lang/String;Ljava/io/File;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_release"})
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
    private final error.NonExistentClass messageListeners = null;
    
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