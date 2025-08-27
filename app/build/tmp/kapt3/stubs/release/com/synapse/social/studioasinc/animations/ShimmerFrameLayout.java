package com.synapse.social.studioasinc.animations;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\u0018\u00002\u00020\u0001B%\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\b\u0010\u0016\u001a\u00020\u0013H\u0002J\b\u0010\u0017\u001a\u00020\fH\u0002J\u0010\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001bH\u0014J\b\u0010\u001c\u001a\u00020\u0019H\u0014J0\u0010\u001d\u001a\u00020\u00192\u0006\u0010\u001e\u001a\u00020\n2\u0006\u0010\u001f\u001a\u00020\u00072\u0006\u0010 \u001a\u00020\u00072\u0006\u0010!\u001a\u00020\u00072\u0006\u0010\"\u001a\u00020\u0007H\u0014J\u0006\u0010#\u001a\u00020\u0019J\u0006\u0010$\u001a\u00020\u0019R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006%"}, d2 = {"Lcom/synapse/social/studioasinc/animations/ShimmerFrameLayout;", "Landroid/widget/FrameLayout;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "isShimmering", "", "shader", "Landroid/graphics/Shader;", "shimmerColor", "shimmerDuration", "", "shimmerPaint", "Landroid/graphics/Paint;", "valueAnimator", "Landroid/animation/ValueAnimator;", "viewRect", "Landroid/graphics/RectF;", "createAnimator", "createShader", "dispatchDraw", "", "canvas", "Landroid/graphics/Canvas;", "onDetachedFromWindow", "onLayout", "changed", "left", "top", "right", "bottom", "startShimmer", "stopShimmer", "app_release"})
public final class ShimmerFrameLayout extends android.widget.FrameLayout {
    @org.jetbrains.annotations.NotNull()
    private final android.graphics.Paint shimmerPaint = null;
    @org.jetbrains.annotations.Nullable()
    private android.animation.ValueAnimator valueAnimator;
    @org.jetbrains.annotations.NotNull()
    private final android.graphics.RectF viewRect = null;
    private boolean isShimmering = false;
    private long shimmerDuration = 1200L;
    @org.jetbrains.annotations.Nullable()
    private android.graphics.Shader shader;
    private int shimmerColor;
    
    @kotlin.jvm.JvmOverloads()
    public ShimmerFrameLayout(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super(null);
    }
    
    @kotlin.jvm.JvmOverloads()
    public ShimmerFrameLayout(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.Nullable()
    android.util.AttributeSet attrs) {
        super(null);
    }
    
    @kotlin.jvm.JvmOverloads()
    public ShimmerFrameLayout(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.Nullable()
    android.util.AttributeSet attrs, int defStyleAttr) {
        super(null);
    }
    
    private final android.graphics.Shader createShader() {
        return null;
    }
    
    public final void startShimmer() {
    }
    
    public final void stopShimmer() {
    }
    
    @java.lang.Override()
    protected void onDetachedFromWindow() {
    }
    
    @java.lang.Override()
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    }
    
    @java.lang.Override()
    protected void dispatchDraw(@org.jetbrains.annotations.NotNull()
    android.graphics.Canvas canvas) {
    }
    
    private final android.animation.ValueAnimator createAnimator() {
        return null;
    }
}