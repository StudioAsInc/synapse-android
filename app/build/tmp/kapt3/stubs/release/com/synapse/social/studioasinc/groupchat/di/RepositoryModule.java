package com.synapse.social.studioasinc.groupchat.di;

@dagger.Module()
@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0010\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\'J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\'J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\'\u00a8\u0006\u0010"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/di/RepositoryModule;", "", "<init>", "()V", "bindGroupRepository", "Lcom/synapse/social/studioasinc/groupchat/data/repository/GroupRepository;", "groupRepositoryImpl", "Lcom/synapse/social/studioasinc/groupchat/data/repository/impl/GroupRepositoryImpl;", "bindMessageRepository", "Lcom/synapse/social/studioasinc/groupchat/data/repository/MessageRepository;", "messageRepositoryImpl", "Lcom/synapse/social/studioasinc/groupchat/data/repository/impl/MessageRepositoryImpl;", "bindUserRepository", "Lcom/synapse/social/studioasinc/groupchat/data/repository/UserRepository;", "userRepositoryImpl", "Lcom/synapse/social/studioasinc/groupchat/data/repository/impl/UserRepositoryImpl;", "app_release"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public abstract class RepositoryModule {
    
    public RepositoryModule() {
        super();
    }
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.synapse.social.studioasinc.groupchat.data.repository.GroupRepository bindGroupRepository(@org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.repository.impl.GroupRepositoryImpl groupRepositoryImpl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.synapse.social.studioasinc.groupchat.data.repository.MessageRepository bindMessageRepository(@org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.repository.impl.MessageRepositoryImpl messageRepositoryImpl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.synapse.social.studioasinc.groupchat.data.repository.UserRepository bindUserRepository(@org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.repository.impl.UserRepositoryImpl userRepositoryImpl);
}