package com.synapse.social.studioasinc.groupchat.presentation.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0018\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\n2\b\b\u0002\u0010\u0016\u001a\u00020\u0017J$\u0010\u0018\u001a\u00020\u00142\u0006\u0010\u0019\u001a\u00020\u001a2\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001c0\tH\u0082@\u00a2\u0006\u0002\u0010\u001dJ\u0006\u0010\u001e\u001a\u00020\u0014J\u0006\u0010\u001f\u001a\u00020\u0014J\u000e\u0010 \u001a\u00020\u00142\u0006\u0010!\u001a\u00020\u001aJ\u0006\u0010\"\u001a\u00020\u0014J\u000e\u0010#\u001a\u00020\u00142\u0006\u0010$\u001a\u00020\u001aJ\u000e\u0010%\u001a\u00020\u00142\u0006\u0010&\u001a\u00020\u001aJ\u000e\u0010\'\u001a\u00020\u00142\u0006\u0010(\u001a\u00020\u001aJ\u0010\u0010)\u001a\u00020\u00142\b\u0010*\u001a\u0004\u0018\u00010+J\u000e\u0010,\u001a\u00020\u00142\u0006\u0010-\u001a\u00020\u001aJ\u000e\u0010.\u001a\u00020\u00142\u0006\u0010/\u001a\u000200J\u000e\u00101\u001a\u00020\u00142\u0006\u00102\u001a\u000203J\u0016\u00104\u001a\u00020\u00142\u0006\u0010!\u001a\u00020\u001a2\u0006\u00105\u001a\u00020\u0017J\b\u00106\u001a\u00020\u0014H\u0002R\u001a\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\f0\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0010R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00067"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/CreateGroupViewModel;", "Landroidx/lifecycle/ViewModel;", "groupRepository", "Lcom/synapse/social/studioasinc/groupchat/data/repository/GroupRepository;", "userRepository", "Lcom/synapse/social/studioasinc/groupchat/data/repository/UserRepository;", "(Lcom/synapse/social/studioasinc/groupchat/data/repository/GroupRepository;Lcom/synapse/social/studioasinc/groupchat/data/repository/UserRepository;)V", "_searchResults", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "Lcom/synapse/social/studioasinc/groupchat/data/model/User;", "_uiState", "Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/CreateGroupUiState;", "searchResults", "Lkotlinx/coroutines/flow/StateFlow;", "getSearchResults", "()Lkotlinx/coroutines/flow/StateFlow;", "uiState", "getUiState", "addMember", "", "user", "role", "Lcom/synapse/social/studioasinc/groupchat/data/model/UserRole;", "addMembersToGroup", "groupId", "", "members", "Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/SelectedMember;", "(Ljava/lang/String;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clearError", "createGroup", "removeMember", "userId", "resetCreationState", "searchUsers", "query", "toggleGroupSetting", "setting", "updateGroupDescription", "description", "updateGroupIcon", "iconFile", "Ljava/io/File;", "updateGroupName", "name", "updateIsPrivate", "isPrivate", "", "updateMaxMembers", "maxMembers", "", "updateMemberRole", "newRole", "validateForm", "app_release"})
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