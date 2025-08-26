package com.synapse.social.studioasinc.styling

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.core.spans.LinkSpan
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
import io.noties.markwon.image.glide.GlideImagesPlugin
import io.noties.markwon.linkify.LinkifyPlugin
import io.noties.markwon.syntax.Prism4jSyntaxHighlight
import io.noties.markwon.syntax.Prism4jThemeDarkula
import io.noties.prism4j.Prism4j
import io.noties.prism4j.annotations.PrismBundle

@PrismBundle(include = [
    "markup", "css", "clike", "javascript", "java", "kotlin", "swift", "python",
    "bash", "c", "cpp", "csharp", "go", "json", "yaml", "xml", "sql", "php",
], grammarLocatorClassName = "com.synapse.social.studioasinc.styling.PrismLocator")
class MarkdownRenderer private constructor(private val markwon: Markwon) {

    fun render(textView: TextView, markdown: String) {
        textView.movementMethod = LinkMovementMethod.getInstance()
        markwon.setMarkdown(textView, markdown)
    }

    companion object {
        @Volatile private var instance: MarkdownRenderer? = null

        fun get(context: Context): MarkdownRenderer {
            return instance ?: synchronized(this) {
                instance ?: build(context.applicationContext).also { instance = it }
            }
        }

        private fun build(context: Context): MarkdownRenderer {
            val prism4j = Prism4j(PrismLocator())

            val markwon = Markwon.builder(context)
                .usePlugin(GlideImagesPlugin.create(context))
                .usePlugin(TablePlugin.create(context))
                .usePlugin(TaskListPlugin.create(context))
                .usePlugin(LinkifyPlugin.create())
                .usePlugin(object : AbstractMarkwonPlugin() {
                    override fun configureSpansFactory(builder: io.noties.markwon.SpansFactory.Builder) {
                        super.configureSpansFactory(builder)
                        builder.setFactory(LinkSpan::class.java) { configuration, props ->
                            val dest = io.noties.markwon.core.MarkwonTheme.getLinkDest(props)
                            arrayOf(object : ClickableSpan() {
                                override fun onClick(widget: View) {
                                    try {
                                        CustomTabsIntent.Builder().build().launchUrl(widget.context, Uri.parse(dest))
                                    } catch (e: Exception) {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(dest))
                                        if (intent.resolveActivity(widget.context.packageManager) != null) {
                                            widget.context.startActivity(intent)
                                        } else {
                                            Log.e("MarkdownRenderer", "No activity for URL: $dest")
                                        }
                                    }
                                }
                                override fun updateDrawState(ds: TextPaint) {
                                    ds.color = Color.parseColor("#445E91")
                                    ds.isUnderlineText = true
                                }
                            })
                        }
                    }
                })
                .usePlugin(Prism4jSyntaxHighlight.create(prism4j, Prism4jThemeDarkula.create()))
                .usePlugin(object : AbstractMarkwonPlugin() {
                    override fun afterSetText(textView: TextView) {
                        super.afterSetText(textView)
                        applyMentionHashtagSpans(textView)
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
                val symbol = matcher.group(1)
                val full = matcher.group(0)
                val span = if (symbol == "@") object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        // Keep behavior same as TextStylingUtil.ProfileSpan by delegating to ProfileActivity via Firebase lookup there if needed
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
    }
}