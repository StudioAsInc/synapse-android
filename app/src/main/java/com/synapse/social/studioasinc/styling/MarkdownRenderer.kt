package com.synapse.social.studioasinc.styling

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.core.MarkwonTheme
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
import io.noties.markwon.image.glide.GlideImagesPlugin
import io.noties.markwon.linkify.LinkifyPlugin
import io.noties.markwon.LinkResolver
import io.noties.markwon.core.CorePlugin
import io.noties.markwon.html.HtmlPlugin

class MarkdownRenderer private constructor(private val markwon: Markwon) {

    fun render(textView: TextView, markdown: String) {
        // CRITICAL FIX: Reset TextView styling before rendering to prevent RecyclerView pollution
        resetTextViewStyling(textView)
        
        textView.movementMethod = LinkMovementMethod.getInstance()
        markwon.setMarkdown(textView, markdown)
    }
    
    /**
     * Resets TextView styling to prevent RecyclerView recycling issues
     * This ensures each message gets a clean state
     */
    private fun resetTextViewStyling(textView: TextView) {
        textView.post {
            // Reset background to transparent
            textView.setBackgroundColor(Color.TRANSPARENT)
            
            // Reset padding to default
            textView.setPadding(0, 0, 0, 0)
            
            // Reset text size to default (will be overridden by markdown if needed)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        }
    }

    companion object {
        @Volatile private var instance: MarkdownRenderer? = null

        @JvmStatic
        fun get(context: Context): MarkdownRenderer {
            return instance ?: synchronized(this) {
                instance ?: build(context.applicationContext).also { instance = it }
            }
        }

        private fun build(context: Context): MarkdownRenderer {
            val linkResolver = LinkResolver { view, link ->
                try {
                    CustomTabsIntent.Builder().build().launchUrl(view.context, Uri.parse(link))
                } catch (e: Exception) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                    if (intent.resolveActivity(view.context.packageManager) != null) {
                        view.context.startActivity(intent)
                    } else {
                        Log.e("MarkdownRenderer", "No activity for URL: $link")
                    }
                }
            }

            val markwon = Markwon.builder(context)
                .usePlugin(CorePlugin.create())
                .usePlugin(GlideImagesPlugin.create(context))
                .usePlugin(TablePlugin.create(context))
                .usePlugin(TaskListPlugin.create(context))
                .usePlugin(LinkifyPlugin.create())
                .usePlugin(HtmlPlugin.create())
                .usePlugin(object : AbstractMarkwonPlugin() {
                    override fun configureTheme(builder: MarkwonTheme.Builder) {
                        builder.linkColor(Color.parseColor("#445E91")).isLinkUnderlined(true)
                    }
                })
                .usePlugin(object : AbstractMarkwonPlugin() {
                    override fun configureConfiguration(builder: MarkwonConfiguration.Builder) {
                        builder.linkResolver(linkResolver)
                    }
                    override fun afterSetText(textView: TextView) {
                        super.afterSetText(textView)
                        applyMentionHashtagSpans(textView)
                        applyRobustTableStyling(textView)
                    }
                })
                .build()

            return MarkdownRenderer(markwon)
        }

        private fun applyMentionHashtagSpans(textView: TextView) {
            val text = textView.text
            if (text !is android.text.Spannable) return
            val pattern = java.util.regex.Pattern.compile("(?<![^\\s])([@#])([A-Za-z0-9_.-]+)")
            val matcher = pattern.matcher(text)
            while (matcher.find()) {
                val start = matcher.start()
                val end = matcher.end()
                val symbol = matcher.group(1) ?: continue
                val full = matcher.group(0) ?: continue
                val span = if (symbol == "@") object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        val ctx = widget.context
                        ctx.startActivity(Intent(ctx, com.synapse.social.studioasinc.ProfileActivity::class.java).putExtra("username", full.substring(1)))
                    }
                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = Color.parseColor("#445E91")
                        ds.isUnderlineText = false
                        ds.typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
                    }
                } else object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        Log.d("MarkdownRenderer", "Hashtag clicked: $full")
                    }
                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = Color.parseColor("#445E91")
                        ds.isUnderlineText = false
                        ds.typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
                    }
                }
                text.setSpan(span, start, end, android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }

        /**
         * Robust table detection and styling that only applies to actual markdown tables
         * Uses a more sophisticated pattern to avoid false positives
         */
        private fun applyRobustTableStyling(textView: TextView) {
            val text = textView.text
            if (text !is android.text.Spannable) return
            
            // More robust table detection - looks for proper markdown table structure
            // Must have at least 2 pipe-delimited lines with proper separator line
            val lines = text.toString().split("\n")
            if (lines.size < 3) return // Need at least header, separator, and one data row
            
            val hasTableStructure = lines.any { line ->
                line.trim().startsWith("|") && line.trim().endsWith("|") && 
                line.contains("|") && line.split("|").size > 2
            }
            
            val hasSeparator = lines.any { line ->
                val trimmed = line.trim()
                trimmed.startsWith("|") && trimmed.endsWith("|") &&
                trimmed.replace("|", "").replace("-", "").replace(":", "").isEmpty()
            }
            
            if (hasTableStructure && hasSeparator) {
                Log.d("MarkdownRenderer", "Valid table detected, applying styling")
                
                // Apply table-specific styling only to this TextView
                textView.post {
                    // Use a subtle background for table content
                    textView.setBackgroundColor(Color.parseColor("#FAFAFA"))
                    
                    // Add padding for better table readability
                    val padding = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 
                        4f, 
                        textView.resources.displayMetrics
                    ).toInt()
                    textView.setPadding(padding, padding, padding, padding)
                    
                    // Force redraw
                    textView.requestLayout()
                    textView.invalidate()
                }
            }
        }

        /**
         * Test method to verify table rendering
         */
        fun testTableRendering(textView: TextView) {
            val testTable = """
                | Name | Age | City |
                |------|-----|------|
                | Ashik | 18 | Dinajpur |
                | Rina | 17 | Dhaka |
                | Tuhin | 19 | Chittagong |
            """.trimIndent()
            
            render(textView, testTable)
        }
    }
}