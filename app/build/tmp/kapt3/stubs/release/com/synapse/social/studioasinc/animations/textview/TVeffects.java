package com.synapse.social.studioasinc.animations.textview;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\r\n\u0002\b\u0002\u0018\u00002\u00020\u0001:\u0001\u0014B\u001b\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bJ\u000e\u0010\u000e\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\bJ\u000e\u0010\u0010\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\bJ\u000e\u0010\u0011\u001a\u00020\f2\u0006\u0010\u0012\u001a\u00020\u0013J \u0010\u0011\u001a\u00020\f2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bH\u0002R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/synapse/social/studioasinc/animations/textview/TVeffects;", "Landroidx/appcompat/widget/AppCompatTextView;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "charDelay", "", "fadeDuration", "totalDuration", "setCharDelay", "", "delay", "setFadeDuration", "duration", "setTotalDuration", "startTyping", "text", "", "AlphaSpan", "app_release"})
public final class TVeffects extends androidx.appcompat.widget.AppCompatTextView {
    private long charDelay = 50L;
    private long fadeDuration = 200L;
    private long totalDuration = 0L;
    
    @kotlin.jvm.JvmOverloads()
    public TVeffects(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super(null);
    }
    
    @kotlin.jvm.JvmOverloads()
    public TVeffects(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.Nullable()
    android.util.AttributeSet attrs) {
        super(null);
    }
    
    public final void setCharDelay(long delay) {
    }
    
    public final void setFadeDuration(long duration) {
    }
    
    public final void setTotalDuration(long duration) {
    }
    
    public final void startTyping(@org.jetbrains.annotations.NotNull()
    java.lang.CharSequence text) {
    }
    
    private final void startTyping(java.lang.CharSequence text, long charDelay, long fadeDuration) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u00012\u00020\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0016R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\u0005\u00a8\u0006\r"}, d2 = {"Lcom/synapse/social/studioasinc/animations/textview/TVeffects$AlphaSpan;", "Landroid/text/style/CharacterStyle;", "Landroid/text/style/UpdateAppearance;", "alpha", "", "(F)V", "getAlpha", "()F", "setAlpha", "updateDrawState", "", "tp", "Landroid/text/TextPaint;", "app_release"})
    static final class AlphaSpan extends android.text.style.CharacterStyle implements android.text.style.UpdateAppearance {
        private float alpha;
        
        public AlphaSpan(float alpha) {
            super();
        }
        
        public final float getAlpha() {
            return 0.0F;
        }
        
        public final void setAlpha(float p0) {
        }
        
        @java.lang.Override()
        public void updateDrawState(@org.jetbrains.annotations.NotNull()
        android.text.TextPaint tp) {
        }
    }
}