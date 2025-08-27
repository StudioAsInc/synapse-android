package com.synapse.social.studioasinc.groupchat.data.local.database;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 \f2\u00020\u0001:\u0001\fB\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u0004\u001a\u00020\u0005H&J\b\u0010\u0006\u001a\u00020\u0007H&J\b\u0010\b\u001a\u00020\tH&J\b\u0010\n\u001a\u00020\u000bH&\u00a8\u0006\r"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/data/local/database/GroupChatDatabase;", "Landroidx/room/RoomDatabase;", "<init>", "()V", "groupDao", "Lcom/synapse/social/studioasinc/groupchat/data/local/dao/GroupDao;", "groupMemberDao", "Lcom/synapse/social/studioasinc/groupchat/data/local/dao/GroupMemberDao;", "groupMessageDao", "Lcom/synapse/social/studioasinc/groupchat/data/local/dao/GroupMessageDao;", "userDao", "Lcom/synapse/social/studioasinc/groupchat/data/local/dao/UserDao;", "Companion", "app_release"})
@androidx.room.Database(entities = {com.synapse.social.studioasinc.groupchat.data.model.Group.class, com.synapse.social.studioasinc.groupchat.data.model.GroupMember.class, com.synapse.social.studioasinc.groupchat.data.model.GroupMessage.class, com.synapse.social.studioasinc.groupchat.data.model.User.class}, version = 1, exportSchema = false)
@androidx.room.TypeConverters(value = {com.synapse.social.studioasinc.groupchat.data.local.database.Converters.class})
public abstract class GroupChatDatabase extends androidx.room.RoomDatabase {
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile com.synapse.social.studioasinc.groupchat.data.local.database.GroupChatDatabase INSTANCE;
    @org.jetbrains.annotations.NotNull()
    public static final com.synapse.social.studioasinc.groupchat.data.local.database.GroupChatDatabase.Companion Companion = null;
    
    public GroupChatDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.synapse.social.studioasinc.groupchat.data.local.dao.GroupDao groupDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.synapse.social.studioasinc.groupchat.data.local.dao.GroupMemberDao groupMemberDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.synapse.social.studioasinc.groupchat.data.local.dao.GroupMessageDao groupMessageDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.synapse.social.studioasinc.groupchat.data.local.dao.UserDao userDao();
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\bR\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/data/local/database/GroupChatDatabase$Companion;", "", "<init>", "()V", "INSTANCE", "Lcom/synapse/social/studioasinc/groupchat/data/local/database/GroupChatDatabase;", "getDatabase", "context", "Landroid/content/Context;", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.synapse.social.studioasinc.groupchat.data.local.database.GroupChatDatabase getDatabase(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
    }
}