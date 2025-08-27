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
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.core.MarkwonTheme
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.latex.JLatexMathPlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.syntax.Prism4jTheme
import io.noties.markwon.syntax.SyntaxHighlightPlugin
import io.noties.prism4j.Prism4j
import io.noties.prism4j.annotations.PrismBundle
import io.noties.markwon.image.glide.GlideImagesPlugin
import io.noties.markwon.linkify.LinkifyPlugin
import io.noties.markwon.LinkResolver

@PrismBundle(includeAll = true, grammarLocatorClassName = ".GrammarLocatorDef")
class MarkdownRenderer private constructor(private val markwon: Markwon) {

    fun render(textView: TextView, markdown: String) {
        textView.movementMethod = LinkMovementMethod.getInstance()
        markwon.setMarkdown(textView, markdown)
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
                .usePlugin(GlideImagesPlugin.create(context))
                .usePlugin(TablePlugin.create(context))
                .usePlugin(TaskListPlugin.create(context))
                .usePlugin(LinkifyPlugin.create())
                .usePlugin(StrikethroughPlugin.create())
                .usePlugin(JLatexMathPlugin.create(context))
                .usePlugin(HtmlPlugin.create())
                .usePlugin(SyntaxHighlightPlugin.create(Prism4j(GrammarLocatorDef.create()), Prism4jTheme.defaultTheme()))
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
    }
}