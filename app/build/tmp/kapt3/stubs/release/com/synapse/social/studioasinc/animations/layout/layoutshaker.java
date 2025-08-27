package com.synapse.social.studioasinc.animations.layout;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0001\u000bB\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcom/synapse/social/studioasinc/animations/layout/layoutshaker;", "", "()V", "INITIAL_AMPLITUDE", "", "SHAKE_DURATION", "", "shake", "", "view", "Landroid/view/View;", "SpringShakeAnimation", "app_release"})
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u000bH\u0014R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcom/synapse/social/studioasinc/animations/layout/layoutshaker$SpringShakeAnimation;", "Landroid/view/animation/Animation;", "()V", "amplitude", "", "beta", "cycles", "applyTransformation", "", "interpolatedTime", "t", "Landroid/view/animation/Transformation;", "app_release"})
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