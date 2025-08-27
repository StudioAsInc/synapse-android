package com.synapse.social.studioasinc.groupchat.data.model;

@com.google.firebase.database.IgnoreExtraProperties()
@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u001d\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001Ba\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\f\u001a\u00020\b\u00a2\u0006\u0004\b\r\u0010\u000eB\t\b\u0016\u00a2\u0006\u0004\b\r\u0010\u000fJ\t\u0010\u001b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001c\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001d\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001e\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001f\u001a\u00020\bH\u00c6\u0003J\t\u0010 \u001a\u00020\u0003H\u00c6\u0003J\t\u0010!\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\"\u001a\u00020\u0003H\u00c6\u0003J\t\u0010#\u001a\u00020\bH\u00c6\u0003Jc\u0010$\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\bH\u00c6\u0001J\u0006\u0010%\u001a\u00020&J\u0013\u0010\'\u001a\u00020\u00032\b\u0010(\u001a\u0004\u0018\u00010)H\u00d6\u0003J\t\u0010*\u001a\u00020&H\u00d6\u0001J\t\u0010+\u001a\u00020,H\u00d6\u0001J\u0016\u0010-\u001a\u00020.2\u0006\u0010/\u001a\u0002002\u0006\u00101\u001a\u00020&R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0011R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0011R\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0011R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\t\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0011R\u0011\u0010\n\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0011R\u0011\u0010\u000b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0011R\u0011\u0010\f\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0016\u00a8\u00062"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/data/model/GroupSettings;", "Landroid/os/Parcelable;", "allowMembersToAddOthers", "", "allowMembersToEditInfo", "onlyAdminsCanMessage", "disappearingMessagesEnabled", "disappearingMessagesDuration", "", "muteNotifications", "allowMediaSharing", "allowFileSharing", "maxFileSize", "<init>", "(ZZZZJZZZJ)V", "()V", "getAllowMembersToAddOthers", "()Z", "getAllowMembersToEditInfo", "getOnlyAdminsCanMessage", "getDisappearingMessagesEnabled", "getDisappearingMessagesDuration", "()J", "getMuteNotifications", "getAllowMediaSharing", "getAllowFileSharing", "getMaxFileSize", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "describeContents", "", "equals", "other", "", "hashCode", "toString", "", "writeToParcel", "", "dest", "Landroid/os/Parcel;", "flags", "app_release"})
@kotlinx.parcelize.Parcelize()
public final class GroupSettings implements android.os.Parcelable {
    private final boolean allowMembersToAddOthers = false;
    private final boolean allowMembersToEditInfo = false;
    private final boolean onlyAdminsCanMessage = false;
    private final boolean disappearingMessagesEnabled = false;
    private final long disappearingMessagesDuration = 0L;
    private final boolean muteNotifications = false;
    private final boolean allowMediaSharing = false;
    private final boolean allowFileSharing = false;
    private final long maxFileSize = 0L;
    
    @java.lang.Override()
    public final int describeContents() {
        return 0;
    }
    
    @java.lang.Override()
    public final void writeToParcel(@org.jetbrains.annotations.NotNull()
    android.os.Parcel dest, int flags) {
    }
    
    public GroupSettings(boolean allowMembersToAddOthers, boolean allowMembersToEditInfo, boolean onlyAdminsCanMessage, boolean disappearingMessagesEnabled, long disappearingMessagesDuration, boolean muteNotifications, boolean allowMediaSharing, boolean allowFileSharing, long maxFileSize) {
        super();
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
    
    public final boolean getDisappearingMessagesEnabled() {
        return false;
    }
    
    public final long getDisappearingMessagesDuration() {
        return 0L;
    }
    
    public final boolean getMuteNotifications() {
        return false;
    }
    
    public final boolean getAllowMediaSharing() {
        return false;
    }
    
    public final boolean getAllowFileSharing() {
        return false;
    }
    
    public final long getMaxFileSize() {
        return 0L;
    }
    
    public GroupSettings() {
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
    
    public final boolean component4() {
        return false;
    }
    
    public final long component5() {
        return 0L;
    }
    
    public final boolean component6() {
        return false;
    }
    
    public final boolean component7() {
        return false;
    }
    
    public final boolean component8() {
        return false;
    }
    
    public final long component9() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.synapse.social.studioasinc.groupchat.data.model.GroupSettings copy(boolean allowMembersToAddOthers, boolean allowMembersToEditInfo, boolean onlyAdminsCanMessage, boolean disappearingMessagesEnabled, long disappearingMessagesDuration, boolean muteNotifications, boolean allowMediaSharing, boolean allowFileSharing, long maxFileSize) {
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