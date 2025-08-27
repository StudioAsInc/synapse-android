package com.synapse.social.studioasinc.groupchat.presentation.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000|\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0017\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0016\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020\n2\u0006\u0010-\u001a\u00020\nJ\u000e\u0010.\u001a\u00020/2\u0006\u00100\u001a\u00020#J\u000e\u00101\u001a\u00020/2\u0006\u00100\u001a\u00020#J\b\u00102\u001a\u00020/H\u0002J\u0006\u00103\u001a\u00020+J\u0006\u00104\u001a\u00020+J\u0018\u00105\u001a\u00020+2\u0006\u0010,\u001a\u00020\n2\b\b\u0002\u00106\u001a\u00020/J\u0016\u00107\u001a\u00020+2\u0006\u0010,\u001a\u00020\n2\u0006\u00108\u001a\u00020\nJ\u000e\u00109\u001a\u00020+2\u0006\u0010\u001a\u001a\u00020\nJ\b\u0010:\u001a\u00020+H\u0002J\b\u0010;\u001a\u00020+H\u0014J\u0016\u0010<\u001a\u00020+2\u0006\u0010,\u001a\u00020\n2\u0006\u0010-\u001a\u00020\nJ\u0016\u0010=\u001a\u00020+2\u0006\u0010>\u001a\u00020\n2\u0006\u0010?\u001a\u00020\nJ\u0006\u0010@\u001a\u00020+J\u000e\u0010A\u001a\u00020+2\u0006\u0010B\u001a\u00020\nJ\u000e\u0010C\u001a\u00020+2\u0006\u0010D\u001a\u00020\nJ\u001c\u0010E\u001a\u00020+2\u0006\u0010D\u001a\u00020\n2\f\u0010F\u001a\b\u0012\u0004\u0012\u00020G0\u001eJ\b\u0010H\u001a\u00020+H\u0002J\u0006\u0010I\u001a\u00020+J\u0006\u0010J\u001a\u00020+R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R#\u0010\u0014\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000f0\u00158FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0018\u0010\u0019\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\u001a\u001a\u00020\n8F\u00a2\u0006\u0006\u001a\u0004\b\u001b\u0010\u001cR\'\u0010\u001d\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00110\u001e0\u00158FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b \u0010\u0019\u001a\u0004\b\u001f\u0010\u0017R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\'\u0010!\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020#0\"0\u00158FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b%\u0010\u0019\u001a\u0004\b$\u0010\u0017R\u0017\u0010&\u001a\b\u0012\u0004\u0012\u00020\r0\'\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010)R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006K"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/GroupChatViewModel;", "Landroidx/lifecycle/ViewModel;", "messageRepository", "Lcom/synapse/social/studioasinc/groupchat/data/repository/MessageRepository;", "groupRepository", "Lcom/synapse/social/studioasinc/groupchat/data/repository/GroupRepository;", "userRepository", "Lcom/synapse/social/studioasinc/groupchat/data/repository/UserRepository;", "(Lcom/synapse/social/studioasinc/groupchat/data/repository/MessageRepository;Lcom/synapse/social/studioasinc/groupchat/data/repository/GroupRepository;Lcom/synapse/social/studioasinc/groupchat/data/repository/UserRepository;)V", "_groupId", "", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/GroupChatUiState;", "currentGroup", "Lcom/synapse/social/studioasinc/groupchat/data/model/Group;", "currentMember", "Lcom/synapse/social/studioasinc/groupchat/data/model/GroupMember;", "currentUser", "Lcom/synapse/social/studioasinc/groupchat/data/model/User;", "groupFlow", "Lkotlinx/coroutines/flow/Flow;", "getGroupFlow", "()Lkotlinx/coroutines/flow/Flow;", "groupFlow$delegate", "Lkotlin/Lazy;", "groupId", "getGroupId", "()Ljava/lang/String;", "groupMembersFlow", "", "getGroupMembersFlow", "groupMembersFlow$delegate", "messagesPagingData", "Landroidx/paging/PagingData;", "Lcom/synapse/social/studioasinc/groupchat/data/model/GroupMessage;", "getMessagesPagingData", "messagesPagingData$delegate", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "addReaction", "", "messageId", "emoji", "canUserDeleteMessage", "", "message", "canUserEditMessage", "canUserSendMessages", "clearError", "clearSearch", "deleteMessage", "isHardDelete", "editMessage", "newText", "initialize", "loadInitialData", "onCleared", "removeReaction", "replyToMessage", "originalMessageId", "replyText", "retryFailedMessages", "searchMessages", "query", "sendMessage", "text", "sendMessageWithAttachments", "attachmentFiles", "Ljava/io/File;", "startMessageListener", "startTyping", "stopTyping", "app_release"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class GroupChatViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.synapse.social.studioasinc.groupchat.data.repository.MessageRepository messageRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.synapse.social.studioasinc.groupchat.data.repository.GroupRepository groupRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.synapse.social.studioasinc.groupchat.data.repository.UserRepository userRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.synapse.social.studioasinc.groupchat.presentation.viewmodel.GroupChatUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.synapse.social.studioasinc.groupchat.presentation.viewmodel.GroupChatUiState> uiState = null;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String _groupId = "";
    @org.jetbrains.annotations.Nullable()
    private com.synapse.social.studioasinc.groupchat.data.model.User currentUser;
    @org.jetbrains.annotations.Nullable()
    private com.synapse.social.studioasinc.groupchat.data.model.Group currentGroup;
    @org.jetbrains.annotations.Nullable()
    private com.synapse.social.studioasinc.groupchat.data.model.GroupMember currentMember;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy messagesPagingData$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy groupMembersFlow$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy groupFlow$delegate = null;
    
    @javax.inject.Inject()
    public GroupChatViewModel(@org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.repository.MessageRepository messageRepository, @org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.repository.GroupRepository groupRepository, @org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.repository.UserRepository userRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.synapse.social.studioasinc.groupchat.presentation.viewmodel.GroupChatUiState> getUiState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getGroupId() {
        return null;
    }
    
    public final void initialize(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<androidx.paging.PagingData<com.synapse.social.studioasinc.groupchat.data.model.GroupMessage>> getMessagesPagingData() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.synapse.social.studioasinc.groupchat.data.model.GroupMember>> getGroupMembersFlow() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.synapse.social.studioasinc.groupchat.data.model.Group> getGroupFlow() {
        return null;
    }
    
    private final void loadInitialData() {
    }
    
    private final void startMessageListener() {
    }
    
    public final void sendMessage(@org.jetbrains.annotations.NotNull()
    java.lang.String text) {
    }
    
    public final void sendMessageWithAttachments(@org.jetbrains.annotations.NotNull()
    java.lang.String text, @org.jetbrains.annotations.NotNull()
    java.util.List<? extends java.io.File> attachmentFiles) {
    }
    
    public final void editMessage(@org.jetbrains.annotations.NotNull()
    java.lang.String messageId, @org.jetbrains.annotations.NotNull()
    java.lang.String newText) {
    }
    
    public final void deleteMessage(@org.jetbrains.annotations.NotNull()
    java.lang.String messageId, boolean isHardDelete) {
    }
    
    public final void addReaction(@org.jetbrains.annotations.NotNull()
    java.lang.String messageId, @org.jetbrains.annotations.NotNull()
    java.lang.String emoji) {
    }
    
    public final void removeReaction(@org.jetbrains.annotations.NotNull()
    java.lang.String messageId, @org.jetbrains.annotations.NotNull()
    java.lang.String emoji) {
    }
    
    public final void replyToMessage(@org.jetbrains.annotations.NotNull()
    java.lang.String originalMessageId, @org.jetbrains.annotations.NotNull()
    java.lang.String replyText) {
    }
    
    public final void searchMessages(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
    }
    
    public final void clearSearch() {
    }
    
    public final void startTyping() {
    }
    
    public final void stopTyping() {
    }
    
    public final void retryFailedMessages() {
    }
    
    private final boolean canUserSendMessages() {
        return false;
    }
    
    public final boolean canUserDeleteMessage(@org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.model.GroupMessage message) {
        return false;
    }
    
    public final boolean canUserEditMessage(@org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.model.GroupMessage message) {
        return false;
    }
    
    public final void clearError() {
    }
    
    @java.lang.Override()
    protected void onCleared() {
    }
}