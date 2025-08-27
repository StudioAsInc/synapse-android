package com.synapse.social.studioasinc.groupchat.presentation.viewmodel;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0007\u0018\u00002\u00020\u0001B\u0019\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0004\b\u0006\u0010\u0007J\u000e\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017J\u000e\u0010\u0018\u001a\u00020\u00152\u0006\u0010\u0019\u001a\u00020\u0017J\u0010\u0010\u001a\u001a\u00020\u00152\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cJ\u000e\u0010\u001d\u001a\u00020\u00152\u0006\u0010\u001e\u001a\u00020\u001fJ\u000e\u0010 \u001a\u00020\u00152\u0006\u0010!\u001a\u00020\"J\u000e\u0010#\u001a\u00020\u00152\u0006\u0010$\u001a\u00020\u0017J\u0018\u0010%\u001a\u00020\u00152\u0006\u0010&\u001a\u00020\u00112\b\b\u0002\u0010\'\u001a\u00020(J\u000e\u0010)\u001a\u00020\u00152\u0006\u0010*\u001a\u00020\u0017J\u0016\u0010+\u001a\u00020\u00152\u0006\u0010*\u001a\u00020\u00172\u0006\u0010,\u001a\u00020(J\u0006\u0010-\u001a\u00020\u0015J$\u0010.\u001a\u00020\u00152\u0006\u0010/\u001a\u00020\u00172\f\u00100\u001a\b\u0012\u0004\u0012\u0002010\u0010H\u0082@\u00a2\u0006\u0002\u00102J\u000e\u00103\u001a\u00020\u00152\u0006\u00104\u001a\u00020\u0017J\b\u00105\u001a\u00020\u0015H\u0002J\u0006\u00106\u001a\u00020\u0015J\u0006\u00107\u001a\u00020\u0015R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\n0\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00110\u00100\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00110\u00100\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u000e\u00a8\u00068"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/CreateGroupViewModel;", "Landroidx/lifecycle/ViewModel;", "groupRepository", "Lcom/synapse/social/studioasinc/groupchat/data/repository/GroupRepository;", "userRepository", "Lcom/synapse/social/studioasinc/groupchat/data/repository/UserRepository;", "<init>", "(Lcom/synapse/social/studioasinc/groupchat/data/repository/GroupRepository;Lcom/synapse/social/studioasinc/groupchat/data/repository/UserRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/CreateGroupUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "_searchResults", "", "Lcom/synapse/social/studioasinc/groupchat/data/model/User;", "searchResults", "getSearchResults", "updateGroupName", "", "name", "", "updateGroupDescription", "description", "updateGroupIcon", "iconFile", "Ljava/io/File;", "updateIsPrivate", "isPrivate", "", "updateMaxMembers", "maxMembers", "", "searchUsers", "query", "addMember", "user", "role", "Lcom/synapse/social/studioasinc/groupchat/data/model/UserRole;", "removeMember", "userId", "updateMemberRole", "newRole", "createGroup", "addMembersToGroup", "groupId", "members", "Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/SelectedMember;", "(Ljava/lang/String;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toggleGroupSetting", "setting", "validateForm", "clearError", "resetCreationState", "app_release"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class CreateGroupViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.synapse.social.studioasinc.groupchat.data.repository.GroupRepository groupRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.synapse.social.studioasinc.groupchat.data.repository.UserRepository userRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.synapse.social.studioasinc.groupchat.presentation.viewmodel.CreateGroupUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.synapse.social.studioasinc.groupchat.presentation.viewmodel.CreateGroupUiState> uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.synapse.social.studioasinc.groupchat.data.model.User>> _searchResults = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.synapse.social.studioasinc.groupchat.data.model.User>> searchResults = null;
    
    @javax.inject.Inject()
    public CreateGroupViewModel(@org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.repository.GroupRepository groupRepository, @org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.repository.UserRepository userRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.synapse.social.studioasinc.groupchat.presentation.viewmodel.CreateGroupUiState> getUiState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.synapse.social.studioasinc.groupchat.data.model.User>> getSearchResults() {
        return null;
    }
    
    public final void updateGroupName(@org.jetbrains.annotations.NotNull()
    java.lang.String name) {
    }
    
    public final void updateGroupDescription(@org.jetbrains.annotations.NotNull()
    java.lang.String description) {
    }
    
    public final void updateGroupIcon(@org.jetbrains.annotations.Nullable()
    java.io.File iconFile) {
    }
    
    public final void updateIsPrivate(boolean isPrivate) {
    }
    
    public final void updateMaxMembers(int maxMembers) {
    }
    
    public final void searchUsers(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
    }
    
    public final void addMember(@org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.model.User user, @org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.model.UserRole role) {
    }
    
    public final void removeMember(@org.jetbrains.annotations.NotNull()
    java.lang.String userId) {
    }
    
    public final void updateMemberRole(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.model.UserRole newRole) {
    }
    
    public final void createGroup() {
    }
    
    private final java.lang.Object addMembersToGroup(java.lang.String groupId, java.util.List<com.synapse.social.studioasinc.groupchat.presentation.viewmodel.SelectedMember> members, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    public final void toggleGroupSetting(@org.jetbrains.annotations.NotNull()
    java.lang.String setting) {
    }
    
    private final void validateForm() {
    }
    
    public final void clearError() {
    }
    
    public final void resetCreationState() {
    }
}