package com.synapse.social.studioasinc.groupchat.utils;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u000b\u001a\u00020\fJ\u0006\u0010\r\u001a\u00020\tJ\u0006\u0010\u000e\u001a\u00020\tJ\u0006\u0010\u000f\u001a\u00020\tJ\u0006\u0010\u0010\u001a\u00020\tR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\n\u00a8\u0006\u0011"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/utils/NetworkMonitor;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "connectivityManager", "Landroid/net/ConnectivityManager;", "isOnline", "Lkotlinx/coroutines/flow/Flow;", "", "()Lkotlinx/coroutines/flow/Flow;", "getNetworkType", "", "isConnectedToCellular", "isConnectedToWifi", "isCurrentlyConnected", "isMeteredConnection", "app_release"})
public final class NetworkMonitor {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final android.net.ConnectivityManager connectivityManager = null;
    
    /**
     * Flow that emits network connectivity status
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.lang.Boolean> isOnline = null;
    
    @javax.inject.Inject()
    public NetworkMonitor(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    /**
     * Flow that emits network connectivity status
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Boolean> isOnline() {
        return null;
    }
    
    /**
     * Check current connectivity status
     */
    public final boolean isCurrentlyConnected() {
        return false;
    }
    
    /**
     * Check if connected to WiFi
     */
    public final boolean isConnectedToWifi() {
        return false;
    }
    
    /**
     * Check if connected to cellular
     */
    public final boolean isConnectedToCellular() {
        return false;
    }
    
    /**
     * Get network type as string
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getNetworkType() {
        return null;
    }
    
    /**
     * Check if connection is metered (cellular or limited WiFi)
     */
    public final boolean isMeteredConnection() {
        return false;
    }
}