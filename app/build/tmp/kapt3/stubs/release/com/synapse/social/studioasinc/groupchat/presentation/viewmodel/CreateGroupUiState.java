package com.synapse.social.studioasinc.groupchat.presentation.viewmodel;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b/\b\u0086\b\u0018\u00002\u00020\u0001B\u00a9\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f\u0012\b\b\u0002\u0010\u000e\u001a\u00020\b\u0012\b\b\u0002\u0010\u000f\u001a\u00020\b\u0012\b\b\u0002\u0010\u0010\u001a\u00020\b\u0012\b\b\u0002\u0010\u0011\u001a\u00020\b\u0012\b\b\u0002\u0010\u0012\u001a\u00020\b\u0012\b\b\u0002\u0010\u0013\u001a\u00020\b\u0012\b\b\u0002\u0010\u0014\u001a\u00020\b\u0012\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0016\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0004\b\u0017\u0010\u0018J\t\u0010(\u001a\u00020\u0003H\u00c6\u0003J\t\u0010)\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010*\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003J\t\u0010+\u001a\u00020\bH\u00c6\u0003J\t\u0010,\u001a\u00020\nH\u00c6\u0003J\u000f\u0010-\u001a\b\u0012\u0004\u0012\u00020\r0\fH\u00c6\u0003J\t\u0010.\u001a\u00020\bH\u00c6\u0003J\t\u0010/\u001a\u00020\bH\u00c6\u0003J\t\u00100\u001a\u00020\bH\u00c6\u0003J\t\u00101\u001a\u00020\bH\u00c6\u0003J\t\u00102\u001a\u00020\bH\u00c6\u0003J\t\u00103\u001a\u00020\bH\u00c6\u0003J\t\u00104\u001a\u00020\bH\u00c6\u0003J\u000b\u00105\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u00106\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u00ab\u0001\u00107\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\n2\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f2\b\b\u0002\u0010\u000e\u001a\u00020\b2\b\b\u0002\u0010\u000f\u001a\u00020\b2\b\b\u0002\u0010\u0010\u001a\u00020\b2\b\b\u0002\u0010\u0011\u001a\u00020\b2\b\b\u0002\u0010\u0012\u001a\u00020\b2\b\b\u0002\u0010\u0013\u001a\u00020\b2\b\b\u0002\u0010\u0014\u001a\u00020\b2\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0016\u001a\u0004\u0018\u00010\u0003H\u00c6\u0001J\u0013\u00108\u001a\u00020\b2\b\u00109\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010:\u001a\u00020\nH\u00d6\u0001J\t\u0010;\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001aR\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\u001eR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\"R\u0011\u0010\u000e\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\u001eR\u0011\u0010\u000f\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u001eR\u0011\u0010\u0010\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u001eR\u0011\u0010\u0011\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u001eR\u0011\u0010\u0012\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u001eR\u0011\u0010\u0013\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u001eR\u0011\u0010\u0014\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u001eR\u0013\u0010\u0015\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\u001aR\u0013\u0010\u0016\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010\u001a\u00a8\u0006<"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/CreateGroupUiState;", "", "groupName", "", "groupDescription", "groupIconFile", "Ljava/io/File;", "isPrivate", "", "maxMembers", "", "selectedMembers", "", "Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/SelectedMember;", "allowMembersToAddOthers", "allowMembersToEditInfo", "onlyAdminsCanMessage", "isSearchingUsers", "isCreating", "isFormValid", "isGroupCreated", "createdGroupId", "error", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/io/File;ZILjava/util/List;ZZZZZZZLjava/lang/String;Ljava/lang/String;)V", "getGroupName", "()Ljava/lang/String;", "getGroupDescription", "getGroupIconFile", "()Ljava/io/File;", "()Z", "getMaxMembers", "()I", "getSelectedMembers", "()Ljava/util/List;", "getAllowMembersToAddOthers", "getAllowMembersToEditInfo", "getOnlyAdminsCanMessage", "getCreatedGroupId", "getError", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "component10", "component11", "component12", "component13", "component14", "component15", "copy", "equals", "other", "hashCode", "toString", "app_release"})
public final class CreateGroupUiState {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String groupName = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String groupDescription = null;
    @org.jetbrains.annotations.Nullable()
    private final java.io.File groupIconFile = null;
    private final boolean isPrivate = false;
    private final int maxMembers = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.synapse.social.studioasinc.groupchat.presentation.viewmodel.SelectedMember> selectedMembers = null;
    private final boolean allowMembersToAddOthers = false;
    private final boolean allowMembersToEditInfo = false;
    private final boolean onlyAdminsCanMessage = false;
    private final boolean isSearchingUsers = false;
    private final boolean isCreating = false;
    private final boolean isFormValid = false;
    private final boolean isGroupCreated = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String createdGroupId = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String error = null;
    
    public CreateGroupUiState(@org.jetbrains.annotations.NotNull()
    java.lang.String groupName, @org.jetbrains.annotations.NotNull()
    java.lang.String groupDescription, @org.jetbrains.annotations.Nullable()
    java.io.File groupIconFile, boolean isPrivate, int maxMembers, @org.jetbrains.annotations.NotNull()
    java.util.List<com.synapse.social.studioasinc.groupchat.presentation.viewmodel.SelectedMember> selectedMembers, boolean allowMembersToAddOthers, boolean allowMembersToEditInfo, boolean onlyAdminsCanMessage, boolean isSearchingUsers, boolean isCreating, boolean isFormValid, boolean isGroupCreated, @org.jetbrains.annotations.Nullable()
    java.lang.String createdGroupId, @org.jetbrains.annotations.Nullable()
    java.lang.String error) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getGroupName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getGroupDescription() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.io.File getGroupIconFile() {
        return null;
    }
    
    public final boolean isPrivate() {
        return false;
    }
    
    public final int getMaxMembers() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.synapse.social.studioasinc.groupchat.presentation.viewmodel.SelectedMember> getSelectedMembers() {
        return null;
    }
    
    public final boolean getAllowMembersToAddOthers() {
        return false;
    }
    
    public final boolean getAllowMembersToEditInfo() {
        return false;
    }
    
    public final boolean getOnlyAdminsCanMessage() {
        return false;
    }
    
    public final boolean isSearchingUsers() {
        return false;
    }
    
    public final boolean isCreating() {
        return false;
    }
    
    public final boolean isFormValid() {
        return false;
    }
    
    public final boolean isGroupCreated() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getCreatedGroupId() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getError() {
        return null;
    }
    
    public CreateGroupUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    public final boolean component10() {
        return false;
    }
    
    public final boolean component11() {
        return false;
    }
    
    public final boolean component12() {
        return false;
    }
    
    public final boolean component13() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component14() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component15() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.io.File component3() {
        return null;
    }
    
    public final boolean component4() {
        return false;
    }
    
    public final int component5() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.synapse.social.studioasinc.groupchat.presentation.viewmodel.SelectedMember> component6() {
        return null;
    }
    
    public final boolean component7() {
        return false;
    }
    
    public final boolean component8() {
        return false;
    }
    
    public final boolean component9() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.synapse.social.studioasinc.groupchat.presentation.viewmodel.CreateGroupUiState copy(@org.jetbrains.annotations.NotNull()
    java.lang.String groupName, @org.jetbrains.annotations.NotNull()
    java.lang.String groupDescription, @org.jetbrains.annotations.Nullable()
    java.io.File groupIconFile, boolean isPrivate, int maxMembers, @org.jetbrains.annotations.NotNull()
    java.util.List<com.synapse.social.studioasinc.groupchat.presentation.viewmodel.SelectedMember> selectedMembers, boolean allowMembersToAddOthers, boolean allowMembersToEditInfo, boolean onlyAdminsCanMessage, boolean isSearchingUsers, boolean isCreating, boolean isFormValid, boolean isGroupCreated, @org.jetbrains.annotations.Nullable()
    java.lang.String createdGroupId, @org.jetbrains.annotations.Nullable()
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