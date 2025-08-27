package com.synapse.social.studioasinc.groupchat.di;

@dagger.Module()
@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010\u0004\u001a\u00020\u00052\b\b\u0001\u0010\u0006\u001a\u00020\u0007H\u0007J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u0005H\u0007J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\n\u001a\u00020\u0005H\u0007J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\n\u001a\u00020\u0005H\u0007J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\n\u001a\u00020\u0005H\u0007\u00a8\u0006\u0011"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/di/DatabaseModule;", "", "<init>", "()V", "provideGroupChatDatabase", "Lcom/synapse/social/studioasinc/groupchat/data/local/database/GroupChatDatabase;", "context", "Landroid/content/Context;", "provideGroupDao", "Lcom/synapse/social/studioasinc/groupchat/data/local/dao/GroupDao;", "database", "provideGroupMemberDao", "Lcom/synapse/social/studioasinc/groupchat/data/local/dao/GroupMemberDao;", "provideGroupMessageDao", "Lcom/synapse/social/studioasinc/groupchat/data/local/dao/GroupMessageDao;", "provideUserDao", "Lcom/synapse/social/studioasinc/groupchat/data/local/dao/UserDao;", "app_release"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class DatabaseModule {
    @org.jetbrains.annotations.NotNull()
    public static final com.synapse.social.studioasinc.groupchat.di.DatabaseModule INSTANCE = null;
    
    private DatabaseModule() {
        super();
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.synapse.social.studioasinc.groupchat.data.local.database.GroupChatDatabase provideGroupChatDatabase(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.synapse.social.studioasinc.groupchat.data.local.dao.GroupDao provideGroupDao(@org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.local.database.GroupChatDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.synapse.social.studioasinc.groupchat.data.local.dao.GroupMemberDao provideGroupMemberDao(@org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.local.database.GroupChatDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.synapse.social.studioasinc.groupchat.data.local.dao.GroupMessageDao provideGroupMessageDao(@org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.local.database.GroupChatDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.synapse.social.studioasinc.groupchat.data.local.dao.UserDao provideUserDao(@org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.local.database.GroupChatDatabase database) {
        return null;
    }
}