package com.synapse.social.studioasinc.groupchat.utils;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0007\u0018\u0000 &2\u00020\u0001:\u0001&B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J \u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u0006H\u0002J\u0006\u0010\u000b\u001a\u00020\fJ\u0016\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u0010\u0010J\u0016\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u0010\u0010J.\u0010\u0012\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0013\u001a\u00020\u00062\u0006\u0010\u0014\u001a\u00020\u00062\u0006\u0010\u0015\u001a\u00020\u0006H\u0082@\u00a2\u0006\u0002\u0010\u0016J \u0010\u0017\u001a\u00020\u00182\u0006\u0010\u000f\u001a\u00020\u000e2\b\b\u0002\u0010\u0019\u001a\u00020\u001aH\u0086@\u00a2\u0006\u0002\u0010\u001bJ\u0018\u0010\u001c\u001a\u00020\u001a2\u0006\u0010\u000f\u001a\u00020\u000e2\b\b\u0002\u0010\u0019\u001a\u00020\u001aJ\u0018\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u001e2\u0006\u0010 \u001a\u00020!H\u0002J\u0018\u0010\"\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u001e2\u0006\u0010#\u001a\u00020$H\u0002J \u0010%\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u001e2\u0006\u0010\u0013\u001a\u00020\u00062\u0006\u0010\u0014\u001a\u00020\u0006H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\'"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/utils/ImageCompressor;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "calculateInSampleSize", "", "options", "Landroid/graphics/BitmapFactory$Options;", "reqWidth", "reqHeight", "cleanupTempFiles", "", "compressImageForAvatar", "Ljava/io/File;", "imageFile", "(Ljava/io/File;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "compressImageForMessage", "compressImageManually", "maxWidth", "maxHeight", "maxSizeKB", "(Ljava/io/File;IIILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getEstimatedCompressedSize", "", "isAvatar", "", "(Ljava/io/File;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "needsCompression", "rotateImage", "Landroid/graphics/Bitmap;", "bitmap", "degrees", "", "rotateImageIfRequired", "imagePath", "", "scaleBitmapToFit", "Companion", "app_release"})
public final class ImageCompressor {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    private static final int MAX_IMAGE_SIZE_KB = 500;
    private static final int MAX_AVATAR_SIZE_KB = 100;
    private static final int MAX_IMAGE_WIDTH = 1280;
    private static final int MAX_IMAGE_HEIGHT = 1280;
    private static final int MAX_AVATAR_SIZE = 512;
    private static final int COMPRESSION_QUALITY = 80;
    @org.jetbrains.annotations.NotNull()
    public static final com.synapse.social.studioasinc.groupchat.utils.ImageCompressor.Companion Companion = null;
    
    @javax.inject.Inject()
    public ImageCompressor(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    /**
     * Compress image for group chat messages
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object compressImageForMessage(@org.jetbrains.annotations.NotNull()
    java.io.File imageFile, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.io.File> $completion) {
        return null;
    }
    
    /**
     * Compress image for avatars (group icons, profile pictures)
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object compressImageForAvatar(@org.jetbrains.annotations.NotNull()
    java.io.File imageFile, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.io.File> $completion) {
        return null;
    }
    
    /**
     * Manual image compression fallback
     */
    private final java.lang.Object compressImageManually(java.io.File imageFile, int maxWidth, int maxHeight, int maxSizeKB, kotlin.coroutines.Continuation<? super java.io.File> $completion) {
        return null;
    }
    
    /**
     * Calculate sample size for efficient memory usage
     */
    private final int calculateInSampleSize(android.graphics.BitmapFactory.Options options, int reqWidth, int reqHeight) {
        return 0;
    }
    
    /**
     * Rotate image based on EXIF orientation data
     */
    private final android.graphics.Bitmap rotateImageIfRequired(android.graphics.Bitmap bitmap, java.lang.String imagePath) {
        return null;
    }
    
    /**
     * Rotate bitmap by specified degrees
     */
    private final android.graphics.Bitmap rotateImage(android.graphics.Bitmap bitmap, float degrees) {
        return null;
    }
    
    /**
     * Scale bitmap to fit within specified dimensions while maintaining aspect ratio
     */
    private final android.graphics.Bitmap scaleBitmapToFit(android.graphics.Bitmap bitmap, int maxWidth, int maxHeight) {
        return null;
    }
    
    /**
     * Get estimated file size after compression
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getEstimatedCompressedSize(@org.jetbrains.annotations.NotNull()
    java.io.File imageFile, boolean isAvatar, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    /**
     * Check if image needs compression
     */
    public final boolean needsCompression(@org.jetbrains.annotations.NotNull()
    java.io.File imageFile, boolean isAvatar) {
        return false;
    }
    
    /**
     * Clean up temporary compressed files
     */
    public final void cleanupTempFiles() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/utils/ImageCompressor$Companion;", "", "()V", "COMPRESSION_QUALITY", "", "MAX_AVATAR_SIZE", "MAX_AVATAR_SIZE_KB", "MAX_IMAGE_HEIGHT", "MAX_IMAGE_SIZE_KB", "MAX_IMAGE_WIDTH", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}