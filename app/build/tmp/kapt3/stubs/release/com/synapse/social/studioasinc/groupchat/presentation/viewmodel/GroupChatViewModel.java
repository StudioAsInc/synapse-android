package com.synapse.social.studioasinc.groupchat.presentation.viewmodel;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000x\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0013\b\u0007\u0018\u00002\u00020\u0001B!\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0004\b\b\u0010\tJ\u000e\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u0013\u001a\u00020\u0012J\b\u0010-\u001a\u00020\u001dH\u0002J\b\u0010.\u001a\u00020\u001dH\u0002J\u000e\u0010/\u001a\u00020\u001d2\u0006\u00100\u001a\u00020\u0012J\u001c\u00101\u001a\u00020\u001d2\u0006\u00100\u001a\u00020\u00122\f\u00102\u001a\b\u0012\u0004\u0012\u0002030\'J\u0016\u00104\u001a\u00020\u001d2\u0006\u00105\u001a\u00020\u00122\u0006\u00106\u001a\u00020\u0012J\u0018\u00107\u001a\u00020\u001d2\u0006\u00105\u001a\u00020\u00122\b\b\u0002\u00108\u001a\u000209J\u0016\u0010:\u001a\u00020\u001d2\u0006\u00105\u001a\u00020\u00122\u0006\u0010;\u001a\u00020\u0012J\u0016\u0010<\u001a\u00020\u001d2\u0006\u00105\u001a\u00020\u00122\u0006\u0010;\u001a\u00020\u0012J\u0016\u0010=\u001a\u00020\u001d2\u0006\u0010>\u001a\u00020\u00122\u0006\u0010?\u001a\u00020\u0012J\u000e\u0010@\u001a\u00020\u001d2\u0006\u0010A\u001a\u00020\u0012J\u0006\u0010B\u001a\u00020\u001dJ\u0006\u0010C\u001a\u00020\u001dJ\u0006\u0010D\u001a\u00020\u001dJ\u0006\u0010E\u001a\u00020\u001dJ\b\u0010F\u001a\u000209H\u0002J\u000e\u0010G\u001a\u0002092\u0006\u0010H\u001a\u00020!J\u000e\u0010I\u001a\u0002092\u0006\u0010H\u001a\u00020!J\u0006\u0010J\u001a\u00020\u001dJ\b\u0010K\u001a\u00020\u001dH\u0014R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00020\f0\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0013\u001a\u00020\u00128F\u00a2\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015R\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001a\u001a\u0004\u0018\u00010\u001bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\'\u0010\u001e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020!0 0\u001f8FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b$\u0010%\u001a\u0004\b\"\u0010#R\'\u0010&\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001b0\'0\u001f8FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b)\u0010%\u001a\u0004\b(\u0010#R#\u0010*\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00190\u001f8FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b,\u0010%\u001a\u0004\b+\u0010#\u00a8\u0006L"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/GroupChatViewModel;", "Landroidx/lifecycle/ViewModel;", "messageRepository", "Lcom/synapse/social/studioasinc/groupchat/data/repository/MessageRepository;", "groupRepository", "Lcom/synapse/social/studioasinc/groupchat/data/repository/GroupRepository;", "userRepository", "Lcom/synapse/social/studioasinc/groupchat/data/repository/UserRepository;", "<init>", "(Lcom/synapse/social/studioasinc/groupchat/data/repository/MessageRepository;Lcom/synapse/social/studioasinc/groupchat/data/repository/GroupRepository;Lcom/synapse/social/studioasinc/groupchat/data/repository/UserRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/GroupChatUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "_groupId", "", "groupId", "getGroupId", "()Ljava/lang/String;", "currentUser", "Lcom/synapse/social/studioasinc/groupchat/data/model/User;", "currentGroup", "Lcom/synapse/social/studioasinc/groupchat/data/model/Group;", "currentMember", "Lcom/synapse/social/studioasinc/groupchat/data/model/GroupMember;", "initialize", "", "messagesPagingData", "Lkotlinx/coroutines/flow/Flow;", "Landroidx/paging/PagingData;", "Lcom/synapse/social/studioasinc/groupchat/data/model/GroupMessage;", "getMessagesPagingData", "()Lkotlinx/coroutines/flow/Flow;", "messagesPagingData$delegate", "Lkotlin/Lazy;", "groupMembersFlow", "", "getGroupMembersFlow", "groupMembersFlow$delegate", "groupFlow", "getGroupFlow", "groupFlow$delegate", "loadInitialData", "startMessageListener", "sendMessage", "text", "sendMessageWithAttachments", "attachmentFiles", "Ljava/io/File;", "editMessage", "messageId", "newText", "deleteMessage", "isHardDelete", "", "addReaction", "emoji", "removeReaction", "replyToMessage", "originalMessageId", "replyText", "searchMessages", "query", "clearSearch", "startTyping", "stopTyping", "retryFailedMessages", "canUserSendMessages", "canUserDeleteMessage", "message", "canUserEditMessage", "clearError", "onCleared", "app_release"})
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