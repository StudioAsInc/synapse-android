package com.synapse.social.studioasinc.animations.layout;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0001\fB\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0007R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/synapse/social/studioasinc/animations/layout/layoutshaker;", "", "<init>", "()V", "SHAKE_DURATION", "", "INITIAL_AMPLITUDE", "", "shake", "", "view", "Landroid/view/View;", "SpringShakeAnimation", "app_release"})
public final class layoutshaker {
    private static final long SHAKE_DURATION = 600L;
    private static final float INITIAL_AMPLITUDE = 14.0F;
    @org.jetbrains.annotations.NotNull()
    public static final com.synapse.social.studioasinc.animations.layout.layoutshaker INSTANCE = null;
    
    private layoutshaker() {
        super();
    }
    
    @kotlin.jvm.JvmStatic()
    public static final void shake(@org.jetbrains.annotations.NotNull()
    android.view.View view) {
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0018\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\fH\u0014R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0082D\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/synapse/social/studioasinc/animations/layout/layoutshaker$SpringShakeAnimation;", "Landroid/view/animation/Animation;", "<init>", "()V", "amplitude", "", "cycles", "beta", "applyTransformation", "", "interpolatedTime", "t", "Landroid/view/animation/Transformation;", "app_release"})
    static final class SpringShakeAnimation extends android.view.animation.Animation {
        private final float amplitude = 14.0F;
        private final float cycles = 6.5F;
        private final float beta = 3.5F;
        
        public SpringShakeAnimation() {
            super();
        }
        
        @java.lang.Override()
        protected void applyTransformation(float interpolatedTime, @org.jetbrains.annotations.NotNull()
        android.view.animation.Transformation t) {
        }
    }
}