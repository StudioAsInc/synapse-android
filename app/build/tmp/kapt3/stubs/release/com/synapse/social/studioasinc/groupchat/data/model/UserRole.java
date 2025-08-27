package com.synapse.social.studioasinc.groupchat.data.model;

/**
 * Represents the role of a user within a group
 */
@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0006\b\u0086\u0081\u0002\u0018\u0000 \u00162\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001\u0016B\u0019\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0004\b\u0006\u0010\u0007J\u0006\u0010\u0010\u001a\u00020\u0011J\u0006\u0010\u0012\u001a\u00020\u0011J\u0006\u0010\u0013\u001a\u00020\u0011J\u0006\u0010\u0014\u001a\u00020\u0011J\u0006\u0010\u0015\u001a\u00020\u0011R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bj\u0002\b\fj\u0002\b\rj\u0002\b\u000ej\u0002\b\u000f\u00a8\u0006\u0017"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/data/model/UserRole;", "", "displayName", "", "priority", "", "<init>", "(Ljava/lang/String;ILjava/lang/String;I)V", "getDisplayName", "()Ljava/lang/String;", "getPriority", "()I", "OWNER", "ADMIN", "MANAGER", "MEMBER", "canManageMembers", "", "canDeleteMessages", "canEditGroupInfo", "canPromoteMembers", "canRemoveMembers", "Companion", "app_release"})
public enum UserRole {
    /*public static final*/ OWNER /* = new OWNER(null, 0) */,
    /*public static final*/ ADMIN /* = new ADMIN(null, 0) */,
    /*public static final*/ MANAGER /* = new MANAGER(null, 0) */,
    /*public static final*/ MEMBER /* = new MEMBER(null, 0) */;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String displayName = null;
    private final int priority = 0;
    @org.jetbrains.annotations.NotNull()
    public static final com.synapse.social.studioasinc.groupchat.data.model.UserRole.Companion Companion = null;
    
    UserRole(java.lang.String displayName, int priority) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDisplayName() {
        return null;
    }
    
    public final int getPriority() {
        return 0;
    }
    
    public final boolean canManageMembers() {
        return false;
    }
    
    public final boolean canDeleteMessages() {
        return false;
    }
    
    public final boolean canEditGroupInfo() {
        return false;
    }
    
    public final boolean canPromoteMembers() {
        return false;
    }
    
    public final boolean canRemoveMembers() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.synapse.social.studioasinc.groupchat.data.model.UserRole> getEntries() {
        return null;
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007\u00a8\u0006\b"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/data/model/UserRole$Companion;", "", "<init>", "()V", "fromString", "Lcom/synapse/social/studioasinc/groupchat/data/model/UserRole;", "role", "", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.synapse.social.studioasinc.groupchat.data.model.UserRole fromString(@org.jetbrains.annotations.NotNull()
        java.lang.String role) {
            return null;
        }
    }
}