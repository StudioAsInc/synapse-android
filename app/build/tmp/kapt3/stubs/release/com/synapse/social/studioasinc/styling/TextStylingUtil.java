package com.synapse.social.studioasinc.styling;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001:\u0007\u000f\u0010\u0011\u0012\u0013\u0014\u0015B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lcom/synapse/social/studioasinc/styling/TextStylingUtil;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "stylingRules", "", "Ljava/util/regex/Pattern;", "Lcom/synapse/social/studioasinc/styling/TextStylingUtil$StyleType;", "applyStyling", "", "text", "", "textView", "Landroid/widget/TextView;", "CodeBlockSpan", "HashtagSpan", "LinkClickableSpan", "ProcessedSpan", "ProfileSpan", "StyleType", "StylingMatch", "app_release"})
public final class TextStylingUtil {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<java.util.regex.Pattern, com.synapse.social.studioasinc.styling.TextStylingUtil.StyleType> stylingRules = null;
    
    public TextStylingUtil(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    public final void applyStyling(@org.jetbrains.annotations.NotNull()
    java.lang.String text, @org.jetbrains.annotations.NotNull()
    android.widget.TextView textView) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\r\n\u0002\b\u0004\b\u0002\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0007J`\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\t2\u0006\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u0012\u001a\u00020\u00032\u0006\u0010\u0013\u001a\u00020\u00032\u0006\u0010\u0014\u001a\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u00032\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00032\u0006\u0010\u0019\u001a\u00020\u00032\u0006\u0010\u001a\u001a\u00020\u0003H\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2 = {"Lcom/synapse/social/studioasinc/styling/TextStylingUtil$CodeBlockSpan;", "Landroid/text/style/LineBackgroundSpan;", "backgroundColor", "", "cornerRadius", "", "padding", "(IFI)V", "paint", "Landroid/graphics/Paint;", "rect", "Landroid/graphics/RectF;", "drawBackground", "", "c", "Landroid/graphics/Canvas;", "p", "left", "right", "top", "baseline", "bottom", "text", "", "start", "end", "lnum", "app_release"})
    static final class CodeBlockSpan implements android.text.style.LineBackgroundSpan {
        private final int backgroundColor = 0;
        private final float cornerRadius = 0.0F;
        private final int padding = 0;
        @org.jetbrains.annotations.NotNull()
        private final android.graphics.RectF rect = null;
        @org.jetbrains.annotations.NotNull()
        private final android.graphics.Paint paint = null;
        
        public CodeBlockSpan(int backgroundColor, float cornerRadius, int padding) {
            super();
        }
        
        @java.lang.Override()
        public void drawBackground(@org.jetbrains.annotations.NotNull()
        android.graphics.Canvas c, @org.jetbrains.annotations.NotNull()
        android.graphics.Paint p, int left, int right, int top, int baseline, int bottom, @org.jetbrains.annotations.NotNull()
        java.lang.CharSequence text, int start, int end, int lnum) {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\t\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u000bH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcom/synapse/social/studioasinc/styling/TextStylingUtil$HashtagSpan;", "Landroid/text/style/ClickableSpan;", "hashtag", "", "(Ljava/lang/String;)V", "onClick", "", "view", "Landroid/view/View;", "updateDrawState", "ds", "Landroid/text/TextPaint;", "app_release"})
    static final class HashtagSpan extends android.text.style.ClickableSpan {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String hashtag = null;
        
        public HashtagSpan(@org.jetbrains.annotations.NotNull()
        java.lang.String hashtag) {
            super();
        }
        
        @java.lang.Override()
        public void onClick(@org.jetbrains.annotations.NotNull()
        android.view.View view) {
        }
        
        @java.lang.Override()
        public void updateDrawState(@org.jetbrains.annotations.NotNull()
        android.text.TextPaint ds) {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0016J\u0010\u0010\r\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/synapse/social/studioasinc/styling/TextStylingUtil$LinkClickableSpan;", "Landroid/text/style/ClickableSpan;", "url", "", "context", "Landroid/content/Context;", "preserveBold", "", "(Ljava/lang/String;Landroid/content/Context;Z)V", "onClick", "", "view", "Landroid/view/View;", "updateDrawState", "ds", "Landroid/text/TextPaint;", "app_release"})
    static final class LinkClickableSpan extends android.text.style.ClickableSpan {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String url = null;
        @org.jetbrains.annotations.NotNull()
        private final android.content.Context context = null;
        private final boolean preserveBold = false;
        
        public LinkClickableSpan(@org.jetbrains.annotations.NotNull()
        java.lang.String url, @org.jetbrains.annotations.NotNull()
        android.content.Context context, boolean preserveBold) {
            super();
        }
        
        @java.lang.Override()
        public void onClick(@org.jetbrains.annotations.NotNull()
        android.view.View view) {
        }
        
        @java.lang.Override()
        public void updateDrawState(@org.jetbrains.annotations.NotNull()
        android.text.TextPaint ds) {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/synapse/social/studioasinc/styling/TextStylingUtil$ProcessedSpan;", "", "()V", "app_release"})
    static final class ProcessedSpan {
        
        public ProcessedSpan() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0010\u0010\u000b\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\rH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcom/synapse/social/studioasinc/styling/TextStylingUtil$ProfileSpan;", "Landroid/text/style/ClickableSpan;", "context", "Landroid/content/Context;", "handle", "", "(Landroid/content/Context;Ljava/lang/String;)V", "onClick", "", "view", "Landroid/view/View;", "updateDrawState", "ds", "Landroid/text/TextPaint;", "app_release"})
    static final class ProfileSpan extends android.text.style.ClickableSpan {
        @org.jetbrains.annotations.NotNull()
        private final android.content.Context context = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String handle = null;
        
        public ProfileSpan(@org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.NotNull()
        java.lang.String handle) {
            super();
        }
        
        @java.lang.Override()
        public void onClick(@org.jetbrains.annotations.NotNull()
        android.view.View view) {
        }
        
        @java.lang.Override()
        public void updateDrawState(@org.jetbrains.annotations.NotNull()
        android.text.TextPaint ds) {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u000f\b\u0082\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\rj\u0002\b\u000ej\u0002\b\u000f\u00a8\u0006\u0010"}, d2 = {"Lcom/synapse/social/studioasinc/styling/TextStylingUtil$StyleType;", "", "(Ljava/lang/String;I)V", "BOLD", "ITALIC", "UNDERLINE", "STRIKETHROUGH", "BOLD_ITALIC_UNDERLINE", "HIGHLIGHT", "LINK", "HEADING", "QUOTE", "SUPERSCRIPT", "SUBSCRIPT", "TEXT_COLOR", "MENTION_HASHTAG", "app_release"})
    static enum StyleType {
        /*public static final*/ BOLD /* = new BOLD() */,
        /*public static final*/ ITALIC /* = new ITALIC() */,
        /*public static final*/ UNDERLINE /* = new UNDERLINE() */,
        /*public static final*/ STRIKETHROUGH /* = new STRIKETHROUGH() */,
        /*public static final*/ BOLD_ITALIC_UNDERLINE /* = new BOLD_ITALIC_UNDERLINE() */,
        /*public static final*/ HIGHLIGHT /* = new HIGHLIGHT() */,
        /*public static final*/ LINK /* = new LINK() */,
        /*public static final*/ HEADING /* = new HEADING() */,
        /*public static final*/ QUOTE /* = new QUOTE() */,
        /*public static final*/ SUPERSCRIPT /* = new SUPERSCRIPT() */,
        /*public static final*/ SUBSCRIPT /* = new SUBSCRIPT() */,
        /*public static final*/ TEXT_COLOR /* = new TEXT_COLOR() */,
        /*public static final*/ MENTION_HASHTAG /* = new MENTION_HASHTAG() */;
        
        StyleType() {
        }
        
        @org.jetbrains.annotations.NotNull()
        public static kotlin.enums.EnumEntries<com.synapse.social.studioasinc.styling.TextStylingUtil.StyleType> getEntries() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\f\u001a\u00020\u0005H\u00c6\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0011\u001a\u00020\u0012H\u00d6\u0001J\t\u0010\u0013\u001a\u00020\u0014H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0015"}, d2 = {"Lcom/synapse/social/studioasinc/styling/TextStylingUtil$StylingMatch;", "", "result", "Ljava/util/regex/MatchResult;", "type", "Lcom/synapse/social/studioasinc/styling/TextStylingUtil$StyleType;", "(Ljava/util/regex/MatchResult;Lcom/synapse/social/studioasinc/styling/TextStylingUtil$StyleType;)V", "getResult", "()Ljava/util/regex/MatchResult;", "getType", "()Lcom/synapse/social/studioasinc/styling/TextStylingUtil$StyleType;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app_release"})
    static final class StylingMatch {
        @org.jetbrains.annotations.NotNull()
        private final java.util.regex.MatchResult result = null;
        @org.jetbrains.annotations.NotNull()
        private final com.synapse.social.studioasinc.styling.TextStylingUtil.StyleType type = null;
        
        public StylingMatch(@org.jetbrains.annotations.NotNull()
        java.util.regex.MatchResult result, @org.jetbrains.annotations.NotNull()
        com.synapse.social.studioasinc.styling.TextStylingUtil.StyleType type) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.regex.MatchResult getResult() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.synapse.social.studioasinc.styling.TextStylingUtil.StyleType getType() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.regex.MatchResult component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.synapse.social.studioasinc.styling.TextStylingUtil.StyleType component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.synapse.social.studioasinc.styling.TextStylingUtil.StylingMatch copy(@org.jetbrains.annotations.NotNull()
        java.util.regex.MatchResult result, @org.jetbrains.annotations.NotNull()
        com.synapse.social.studioasinc.styling.TextStylingUtil.StyleType type) {
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
}