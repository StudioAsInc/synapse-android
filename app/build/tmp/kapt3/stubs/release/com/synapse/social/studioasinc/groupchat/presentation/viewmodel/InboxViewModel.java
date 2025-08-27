package com.synapse.social.studioasinc.groupchat.presentation.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\u0018\u001a\u00020\u0019J\u0006\u0010\u001a\u001a\u00020\u0019J\u0006\u0010\u001b\u001a\u00020\u0019J\b\u0010\u001c\u001a\u00020\u0019H\u0014J\u0006\u0010\u001d\u001a\u00020\u0019J\u000e\u0010\u001e\u001a\u00020\u00192\u0006\u0010\u001f\u001a\u00020 J\b\u0010!\u001a\u00020\u0019H\u0002R\u0010\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\f0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u001d\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\u00110\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u000fR\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00150\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\""}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/InboxViewModel;", "Landroidx/lifecycle/ViewModel;", "groupRepository", "Lcom/synapse/social/studioasinc/groupchat/data/repository/GroupRepository;", "userRepository", "Lcom/synapse/social/studioasinc/groupchat/data/repository/UserRepository;", "(Lcom/synapse/social/studioasinc/groupchat/data/repository/GroupRepository;Lcom/synapse/social/studioasinc/groupchat/data/repository/UserRepository;)V", "_uiState", "error/NonExistentClass", "Lerror/NonExistentClass;", "groupsPagingData", "Lkotlinx/coroutines/flow/Flow;", "Landroidx/paging/PagingData;", "Lcom/synapse/social/studioasinc/groupchat/data/model/Group;", "getGroupsPagingData", "()Lkotlinx/coroutines/flow/Flow;", "recentGroups", "", "getRecentGroups", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/InboxUiState;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "clearError", "", "clearSearch", "loadInitialData", "onCleared", "refreshGroups", "searchGroups", "query", "", "startPeriodicSync", "app_release"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class InboxViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.synapse.social.studioasinc.groupchat.data.repository.GroupRepository groupRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.synapse.social.studioasinc.groupchat.data.repository.UserRepository userRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final error.NonExistentClass _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.synapse.social.studioasinc.groupchat.presentation.viewmodel.InboxUiState> uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<androidx.paging.PagingData<com.synapse.social.studioasinc.groupchat.data.model.Group>> groupsPagingData = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.synapse.social.studioasinc.groupchat.data.model.Group>> recentGroups = null;
    
    @javax.inject.Inject()
    public InboxViewModel(@org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.repository.GroupRepository groupRepository, @org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.repository.UserRepository userRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.synapse.social.studioasinc.groupchat.presentation.viewmodel.InboxUiState> getUiState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<androidx.paging.PagingData<com.synapse.social.studioasinc.groupchat.data.model.Group>> getGroupsPagingData() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.synapse.social.studioasinc.groupchat.data.model.Group>> getRecentGroups() {
        return null;
    }
    
    public final void loadInitialData() {
    }
    
    public final void searchGroups(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
    }
    
    public final void clearSearch() {
    }
    
    public final void refreshGroups() {
    }
    
    public final void clearError() {
    }
    
    private final void startPeriodicSync() {
    }
    
    @java.lang.Override()
    protected void onCleared() {
    }
}