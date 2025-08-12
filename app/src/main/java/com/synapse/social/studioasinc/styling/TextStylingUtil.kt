package com.synapse.social.studioasinc.styling

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.net.Uri
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.LeadingMarginSpan
import android.text.style.LineBackgroundSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.synapse.social.studioasinc.ProfileActivity
import java.util.regex.MatchResult
import java.util.regex.Pattern

class TextStylingUtil(private val context: Context) {

    private class ProcessedSpan

    private enum class StyleType {
        BOLD, ITALIC, UNDERLINE, STRIKETHROUGH, BOLD_ITALIC_UNDERLINE,
        HIGHLIGHT, LINK, HEADING, QUOTE,
        SUPERSCRIPT, SUBSCRIPT, TEXT_COLOR, MENTION_HASHTAG
    }

    private data class StylingMatch(val result: MatchResult, val type: StyleType)

    companion object {
        private const val TAG = "TextStylingUtil"
        private const val MENTION_HASHTAG_COLOR = "#445E91"
        private const val LINK_COLOR = "#445E91"
        private const val HIGHLIGHT_COLOR = "#E0E0E0"

        private val stylingRules: Map<Pattern, StyleType> = linkedMapOf(
            Pattern.compile("\\[([^\\]]+)\\]\\(([^)]+)\\)") to StyleType.LINK,
            Pattern.compile("(?<![^\\s]|^)([@#])([A-Za-z0-9_.-]+)") to StyleType.MENTION_HASHTAG,
            Pattern.compile("///(.*?)///") to StyleType.BOLD_ITALIC_UNDERLINE,
            Pattern.compile("\\*\\*(.*?)\\*\\*") to StyleType.BOLD,
            Pattern.compile("__(.*?)__") to StyleType.ITALIC,
            Pattern.compile("~~(.*?)~~") to StyleType.STRIKETHROUGH,
            Pattern.compile("\\*(.*?)\\*") to StyleType.ITALIC,
            Pattern.compile("_(.*?)_") to StyleType.UNDERLINE,
            Pattern.compile("`(.+?)`") to StyleType.HIGHLIGHT,
            Pattern.compile("^(#{1,5})\\s+(.+?)(?=\\n|$)", Pattern.MULTILINE) to StyleType.HEADING,
            Pattern.compile("^>\\s+(.+?)(?=\\n|$)", Pattern.MULTILINE) to StyleType.QUOTE,
            Pattern.compile("\\^(.*?)\\^") to StyleType.SUPERSCRIPT,
            Pattern.compile("~(.*?)~") to StyleType.SUBSCRIPT,
            Pattern.compile("\\{#([0-9a-fA-F]{6})\\}\\((.*?)\\)") to StyleType.TEXT_COLOR
        )
    }

    fun applyStyling(text: String, textView: TextView) {
        textView.movementMethod = LinkMovementMethod.getInstance()
        val ssb = SpannableStringBuilder(text)

        while (true) {
            var earliestMatch: StylingMatch? = null

            for ((pattern, type) in stylingRules) {
                val matcher = pattern.matcher(ssb)
                while (matcher.find()) {
                    if (ssb.getSpans(matcher.start(), matcher.end(), ProcessedSpan::class.java).isNotEmpty()) {
                        continue
                    }
                    if (earliestMatch == null || matcher.start() < earliestMatch.result.start()) {
                        earliestMatch = StylingMatch(matcher.toMatchResult(), type)
                    }
                }
            }

            val matchToProcess = earliestMatch ?: break
            val result = matchToProcess.result
            val start = result.start()
            val end = result.end()
            var finalEnd = end

            when (matchToProcess.type) {
                StyleType.BOLD -> {
                    val content = result.group(1)!!
                    ssb.replace(start, end, content)
                    finalEnd = start + content.length
                    ssb.setSpan(StyleSpan(Typeface.BOLD), start, finalEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                StyleType.ITALIC -> {
                    val content = result.group(1)!!
                    ssb.replace(start, end, content)
                    finalEnd = start + content.length
                    ssb.setSpan(StyleSpan(Typeface.ITALIC), start, finalEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                StyleType.UNDERLINE -> {
                    val content = result.group(1)!!
                    ssb.replace(start, end, content)
                    finalEnd = start + content.length
                    ssb.setSpan(UnderlineSpan(), start, finalEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                StyleType.BOLD_ITALIC_UNDERLINE -> {
                    val content = result.group(1)!!
                    ssb.replace(start, end, content)
                    finalEnd = start + content.length
                    ssb.setSpan(StyleSpan(Typeface.BOLD_ITALIC), start, finalEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    ssb.setSpan(UnderlineSpan(), start, finalEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                StyleType.STRIKETHROUGH -> {
                    val content = result.group(1)!!
                    ssb.replace(start, end, content)
                    finalEnd = start + content.length
                    ssb.setSpan(StrikethroughSpan(), start, finalEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                StyleType.HIGHLIGHT -> {
                    val content = result.group(1)!!
                    ssb.replace(start, end, content)
                    finalEnd = start + content.length
                    ssb.setSpan(CodeBlockSpan(Color.parseColor(HIGHLIGHT_COLOR), 6f), start, finalEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                StyleType.LINK -> {
                    val linkText = result.group(1)!!
                    val linkUrl = result.group(2)!!
                    ssb.replace(start, end, linkText)
                    finalEnd = start + linkText.length
                    if (Patterns.WEB_URL.matcher(linkUrl).matches()) {
                        ssb.setSpan(LinkClickableSpan(linkUrl, context), start, finalEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                }
                StyleType.HEADING -> {
                    val headingText = result.group(2)!!
                    val headingLevel = result.group(1)!!.length
                    val size = 2.0f - (headingLevel * 0.2f)
                    ssb.replace(start, end, headingText)
                    finalEnd = start + headingText.length
                    ssb.setSpan(RelativeSizeSpan(size), start, finalEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    ssb.setSpan(StyleSpan(Typeface.BOLD), start, finalEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                StyleType.QUOTE -> {
                    val quoteText = result.group(1)!!
                    ssb.replace(start, end, quoteText)
                    finalEnd = start + quoteText.length
                    ssb.setSpan(LeadingMarginSpan.Standard(60, 10), start, finalEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    ssb.setSpan(StyleSpan(Typeface.ITALIC), start, finalEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                StyleType.SUPERSCRIPT -> {
                    val content = result.group(1)!!
                    ssb.replace(start, end, content)
                    finalEnd = start + content.length
                    ssb.setSpan(SuperscriptSpan(), start, finalEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    ssb.setSpan(RelativeSizeSpan(0.75f), start, finalEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                StyleType.SUBSCRIPT -> {
                    val content = result.group(1)!!
                    ssb.replace(start, end, content)
                    finalEnd = start + content.length
                    ssb.setSpan(SubscriptSpan(), start, finalEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    ssb.setSpan(RelativeSizeSpan(0.75f), start, finalEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                StyleType.TEXT_COLOR -> {
                    val colorString = result.group(1)!!
                    val content = result.group(2)!!
                    ssb.replace(start, end, content)
                    finalEnd = start + content.length
                    try {
                        val color = Color.parseColor("#$colorString")
                        ssb.setSpan(ForegroundColorSpan(color), start, finalEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    } catch (e: IllegalArgumentException) {
                        Log.w(TAG, "Invalid hex color: #$colorString")
                    }
                }
                StyleType.MENTION_HASHTAG -> {
                    finalEnd = end
                    val symbol = result.group(1)!!
                    val span = if (symbol == "@") ProfileSpan(context, result) else HashtagSpan(result)
                    ssb.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
            ssb.setSpan(ProcessedSpan(), start, finalEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        ssb.getSpans(0, ssb.length, ProcessedSpan::class.java).forEach { ssb.removeSpan(it) }
        textView.text = ssb
    }

    private class CodeBlockSpan(
        private val backgroundColor: Int,
        private val cornerRadius: Float,
        private val padding: Int = 12
    ) : LineBackgroundSpan {
        private val rect = RectF()
        private val paint = Paint().apply {
            color = backgroundColor
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        override fun drawBackground(c: Canvas, p: Paint, left: Int, right: Int, top: Int, baseline: Int, bottom: Int, text: CharSequence, start: Int, end: Int, lnum: Int) {
            val textWidth = p.measureText(text, start, end)
            rect.set(left.toFloat() - padding, top.toFloat(), left.toFloat() + textWidth + padding, bottom.toFloat())
            c.drawRoundRect(rect, cornerRadius, cornerRadius, paint)
        }
    }

    private class LinkClickableSpan(private val url: String, private val context: Context) : ClickableSpan() {
        override fun onClick(view: View) {
            try {
                CustomTabsIntent.Builder().build().launchUrl(context, Uri.parse(url))
            } catch (e: Exception) {
                Log.e(TAG, "Failed to open URL with Custom Tabs. Falling back.", e)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                } else {
                    Log.e(TAG, "No activity found to handle URL: $url")
                }
            }
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.color = Color.parseColor(LINK_COLOR)
            ds.isUnderlineText = true
        }
    }

    private class ProfileSpan(private val context: Context, private val matchResult: MatchResult) : ClickableSpan() {
        override fun onClick(view: View) {
            val rawUsername = matchResult.group(2) ?: return

            val sanitizedUsername = rawUsername.replace(".", "_")
                .replace("#", "").replace("$", "")
                .replace("[", "").replace("]", "")

            if (sanitizedUsername.isBlank()) {
                Log.w(TAG, "Sanitized username is blank for original mention: '${matchResult.group(0)}'")
                return
            }

            val db = FirebaseDatabase.getInstance().getReference("synapse/username").child(sanitizedUsername)
            db.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val uid = snapshot.child("uid").getValue(String::class.java)
                    if (uid != null && uid != "null") {
                        context.startActivity(Intent(context, ProfileActivity::class.java).putExtra("uid", uid))
                    } else {
                        Log.d(TAG, "UID not found for sanitized username: '$sanitizedUsername' (from '${matchResult.group(0)}')")
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "DB error for username: '$sanitizedUsername'", error.toException())
                }
            })
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.color = Color.parseColor(MENTION_HASHTAG_COLOR)
            ds.isUnderlineText = false
            ds.typeface = Typeface.create(ds.typeface, Typeface.BOLD)
        }
    }

    private class HashtagSpan(private val matchResult: MatchResult) : ClickableSpan() {
        override fun onClick(view: View) {
            val hashtagContent = matchResult.group(2) ?: return
            Log.d(TAG, "Hashtag clicked: #$hashtagContent")
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.color = Color.parseColor(MENTION_HASHTAG_COLOR)
            ds.isUnderlineText = false
            ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
    }
}