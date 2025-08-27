package com.synapse.social.studioasinc.groupchat.presentation.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0014\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001BS\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\r\u00a2\u0006\u0002\u0010\u000eJ\t\u0010\u0017\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u00c6\u0003J\u000b\u0010\u001d\u001a\u0004\u0018\u00010\rH\u00c6\u0003JW\u0010\u001e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00032\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n2\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\rH\u00c6\u0001J\u0013\u0010\u001f\u001a\u00020\u00032\b\u0010 \u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010!\u001a\u00020\"H\u00d6\u0001J\t\u0010#\u001a\u00020\rH\u00d6\u0001R\u0011\u0010\b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0013\u0010\f\u001a\u0004\u0018\u00010\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0010R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u0010R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0010R\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016\u00a8\u0006$"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/GroupChatUiState;", "", "isLoading", "", "isSearching", "isUploadingAttachment", "currentUserRole", "Lcom/synapse/social/studioasinc/groupchat/data/model/UserRole;", "canSendMessages", "searchResults", "", "Lcom/synapse/social/studioasinc/groupchat/data/model/GroupMessage;", "error", "", "(ZZZLcom/synapse/social/studioasinc/groupchat/data/model/UserRole;ZLjava/util/List;Ljava/lang/String;)V", "getCanSendMessages", "()Z", "getCurrentUserRole", "()Lcom/synapse/social/studioasinc/groupchat/data/model/UserRole;", "getError", "()Ljava/lang/String;", "getSearchResults", "()Ljava/util/List;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "other", "hashCode", "", "toString", "app_release"})
public final class GroupChatUiState {
    private final boolean isLoading = false;
    private final boolean isSearching = false;
    private final boolean isUploadingAttachment = false;
    @org.jetbrains.annotations.NotNull()
    private final com.synapse.social.studioasinc.groupchat.data.model.UserRole currentUserRole = null;
    private final boolean canSendMessages = false;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.synapse.social.studioasinc.groupchat.data.model.GroupMessage> searchResults = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String error = null;
    
    public GroupChatUiState(boolean isLoading, boolean isSearching, boolean isUploadingAttachment, @org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.model.UserRole currentUserRole, boolean canSendMessages, @org.jetbrains.annotations.NotNull()
    java.util.List<com.synapse.social.studioasinc.groupchat.data.model.GroupMessage> searchResults, @org.jetbrains.annotations.Nullable()
    java.lang.String error) {
        super();
    }
    
    public final boolean isLoading() {
        return false;
    }
    
    public final boolean isSearching() {
        return false;
    }
    
    public final boolean isUploadingAttachment() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.synapse.social.studioasinc.groupchat.data.model.UserRole getCurrentUserRole() {
        return null;
    }
    
    public final boolean getCanSendMessages() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.synapse.social.studioasinc.groupchat.data.model.GroupMessage> getSearchResults() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getError() {
        return null;
    }
    
    public GroupChatUiState() {
        super();
    }
    
    public final boolean component1() {
        return false;
    }
    
    public final boolean component2() {
        return false;
    }
    
    public final boolean component3() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.synapse.social.studioasinc.groupchat.data.model.UserRole component4() {
        return null;
    }
    
    public final boolean component5() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.synapse.social.studioasinc.groupchat.data.model.GroupMessage> component6() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.synapse.social.studioasinc.groupchat.presentation.viewmodel.GroupChatUiState copy(boolean isLoading, boolean isSearching, boolean isUploadingAttachment, @org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.model.UserRole currentUserRole, boolean canSendMessages, @org.jetbrains.annotations.NotNull()
    java.util.List<com.synapse.social.studioasinc.groupchat.data.model.GroupMessage> searchResults, @org.jetbrains.annotations.Nullable()
    java.lang.String error) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}