package com.synapse.social.studioasinc.permissionreq;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\u0018\u0000 \u00122\u00020\u0001:\u0001\u0012B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\n\u001a\u00020\u000bH\u0002J\u0006\u0010\f\u001a\u00020\rJ\u0006\u0010\u000e\u001a\u00020\rJ\b\u0010\u000f\u001a\u00020\rH\u0002J\b\u0010\u0010\u001a\u00020\rH\u0002J\b\u0010\u0011\u001a\u00020\rH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/synapse/social/studioasinc/permissionreq/AskPermission;", "", "activity", "Landroid/app/Activity;", "(Landroid/app/Activity;)V", "maxPermissionChecks", "", "permissionCheckCount", "timer", "Ljava/util/Timer;", "areAllPermissionsGranted", "", "checkAndRequestPermissions", "", "cleanup", "requestNeededPermissions", "showPermissionDeniedMessage", "startMainActivity", "Companion", "app_release"})
public final class AskPermission {
    @org.jetbrains.annotations.NotNull()
    private final android.app.Activity activity = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Timer timer = null;
    private int permissionCheckCount = 0;
    private final int maxPermissionChecks = 5;
    public static final int PERMISSION_REQUEST_CODE = 1000;
    @org.jetbrains.annotations.NotNull()
    public static final com.synapse.social.studioasinc.permissionreq.AskPermission.Companion Companion = null;
    
    public AskPermission(@org.jetbrains.annotations.NotNull()
    android.app.Activity activity) {
        super();
    }
    
    public final void checkAndRequestPermissions() {
    }
    
    private final boolean areAllPermissionsGranted() {
        return false;
    }
    
    private final void requestNeededPermissions() {
    }
    
    private final void startMainActivity() {
    }
    
    private final void showPermissionDeniedMessage() {
    }
    
    public final void cleanup() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/synapse/social/studioasinc/permissionreq/AskPermission$Companion;", "", "()V", "PERMISSION_REQUEST_CODE", "", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}