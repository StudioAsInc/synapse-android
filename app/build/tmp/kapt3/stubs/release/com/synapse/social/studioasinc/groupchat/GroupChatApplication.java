package com.synapse.social.studioasinc.groupchat;

@dagger.hilt.android.HiltAndroidApp()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\f\u001a\u00020\rH\u0016R\u0010\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0005R\u001e\u0010\u0006\u001a\u00020\u00078\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000b\u00a8\u0006\u000e"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/GroupChatApplication;", "Landroid/app/Application;", "()V", "applicationScope", "error/NonExistentClass", "Lerror/NonExistentClass;", "userRepository", "Lcom/synapse/social/studioasinc/groupchat/data/repository/impl/UserRepositoryImpl;", "getUserRepository", "()Lcom/synapse/social/studioasinc/groupchat/data/repository/impl/UserRepositoryImpl;", "setUserRepository", "(Lcom/synapse/social/studioasinc/groupchat/data/repository/impl/UserRepositoryImpl;)V", "onCreate", "", "app_release"})
public final class GroupChatApplication extends android.app.Application {
    @javax.inject.Inject()
    public com.synapse.social.studioasinc.groupchat.data.repository.impl.UserRepositoryImpl userRepository;
    @org.jetbrains.annotations.NotNull()
    private final error.NonExistentClass applicationScope = null;
    
    public GroupChatApplication() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.synapse.social.studioasinc.groupchat.data.repository.impl.UserRepositoryImpl getUserRepository() {
        return null;
    }
    
    public final void setUserRepository(@org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.repository.impl.UserRepositoryImpl p0) {
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
}