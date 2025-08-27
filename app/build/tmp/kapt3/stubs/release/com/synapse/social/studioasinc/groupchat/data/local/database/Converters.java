package com.synapse.social.studioasinc.groupchat.data.local.database;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0010\t\n\u0002\b\t\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0005\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u0007J\u0010\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\fH\u0007J\u001c\u0010\r\u001a\u00020\u00062\u0012\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00100\u000fH\u0007J\"\u0010\u0011\u001a\u00020\u00062\u0018\u0010\u000e\u001a\u0014\u0012\u0004\u0012\u00020\u0006\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\b0\u000fH\u0007J\u0016\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\u0013\u001a\u00020\u0006H\u0007J\u0010\u0010\u0014\u001a\u00020\f2\u0006\u0010\u0015\u001a\u00020\u0006H\u0007J\u001c\u0010\u0016\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00100\u000f2\u0006\u0010\u0017\u001a\u00020\u0006H\u0007J\"\u0010\u0018\u001a\u0014\u0012\u0004\u0012\u00020\u0006\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\b0\u000f2\u0006\u0010\u0017\u001a\u00020\u0006H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/data/local/database/Converters;", "", "()V", "gson", "Lcom/google/gson/Gson;", "fromAttachmentList", "", "attachments", "", "Lcom/synapse/social/studioasinc/groupchat/data/model/MessageAttachment;", "fromGroupSettings", "settings", "Lcom/synapse/social/studioasinc/groupchat/data/model/GroupSettings;", "fromStringLongMap", "map", "", "", "fromStringStringListMap", "toAttachmentList", "attachmentsString", "toGroupSettings", "settingsString", "toStringLongMap", "mapString", "toStringStringListMap", "app_release"})
public final class Converters {
    @org.jetbrains.annotations.NotNull()
    private final com.google.gson.Gson gson = null;
    
    public Converters() {
        super();
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String fromAttachmentList(@org.jetbrains.annotations.NotNull()
    java.util.List<com.synapse.social.studioasinc.groupchat.data.model.MessageAttachment> attachments) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.synapse.social.studioasinc.groupchat.data.model.MessageAttachment> toAttachmentList(@org.jetbrains.annotations.NotNull()
    java.lang.String attachmentsString) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String fromStringLongMap(@org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, java.lang.Long> map) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.lang.Long> toStringLongMap(@org.jetbrains.annotations.NotNull()
    java.lang.String mapString) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String fromStringStringListMap(@org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, ? extends java.util.List<java.lang.String>> map) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.util.List<java.lang.String>> toStringStringListMap(@org.jetbrains.annotations.NotNull()
    java.lang.String mapString) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String fromGroupSettings(@org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.model.GroupSettings settings) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.NotNull()
    public final com.synapse.social.studioasinc.groupchat.data.model.GroupSettings toGroupSettings(@org.jetbrains.annotations.NotNull()
    java.lang.String settingsString) {
        return null;
    }
}