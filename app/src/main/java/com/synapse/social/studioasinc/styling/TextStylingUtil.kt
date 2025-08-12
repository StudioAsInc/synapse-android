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
    // FIX: A specific marker for mentions/hashtags to avoid conflicts with ProcessedSpan
    private class ProcessedMentionOrHashtagSpan

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
            // FIX: This robust regex correctly handles start-of-line and whitespace boundaries.
            Pattern.compile("(^|\\s)([@#])([A-Za-z0-9_.-]+)", Pattern.MULTILINE) to StyleType.MENTION_HASHTAG,
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
                    // FIX: Check for the appropriate "processed" span type.
                    val alreadyProcessed = when (type) {
                        StyleType.MENTION_HASHTAG -> ssb.getSpans(matcher.start(), matcher.end(), ProcessedMentionOrHashtagSpan::class.java).isNotEmpty()
                        else -> ssb.getSpans(matcher.start(), matcher.end(), ProcessedSpan::class.java).isNotEmpty()
                    }
                    if (alreadyProcessed) {
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
                StyleType.BOLD, StyleType.ITALIC, StyleType.UNDERLINE,
                StyleType.BOLD_ITALIC_UNDERLINE, StyleType.STRIKETHROUGH, StyleType.HIGHLIGHT,
                StyleType.LINK, StyleType.HEADING, StyleType.QUOTE, StyleType.SUPERSCRIPT,
                StyleType.SUBSCRIPT, StyleType.TEXT_COLOR -> {
                    // This block handles all replacement-based styles
                    // Content is extracted, delimiters are removed, and a new span is applied.
                    val (content, newStart, newEnd) = when (matchToProcess.type) {
                        StyleType.LINK -> Triple(result.group(1)!!, start, start + result.group(1)!!.length)
                        StyleType.HEADING -> Triple(result.group(2)!!, start, start + result.group(2)!!.length)
                        StyleType.QUOTE -> Triple(result.group(1)!!, start, start + result.group(1)!!.length)
                        StyleType.TEXT_COLOR -> Triple(result.group(2)!!, start, start + result.group(2)!!.length)
                        else -> Triple(result.group(1)!!, start, start + result.group(1)!!.length) // For BOLD, ITALIC, etc.
                    }

                    ssb.replace(start, end, content)
                    finalEnd = newEnd

                    when (matchToProcess.type) {
                        StyleType.BOLD -> ssb.setSpan(StyleSpan(Typeface.BOLD), newStart, newEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        StyleType.ITALIC -> ssb.setSpan(StyleSpan(Typeface.ITALIC), newStart, newEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        StyleType.UNDERLINE -> ssb.setSpan(UnderlineSpan(), newStart, newEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        StyleType.BOLD_ITALIC_UNDERLINE -> {
                            ssb.setSpan(StyleSpan(Typeface.BOLD_ITALIC), newStart, newEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                            ssb.setSpan(UnderlineSpan(), newStart, newEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        }
                        StyleType.STRIKETHROUGH -> ssb.setSpan(StrikethroughSpan(), newStart, newEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        StyleType.HIGHLIGHT -> ssb.setSpan(CodeBlockSpan(Color.parseColor(HIGHLIGHT_COLOR), 6f), newStart, newEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        StyleType.LINK -> if (Patterns.WEB_URL.matcher(result.group(2)!!).matches()) {
                            ssb.setSpan(LinkClickableSpan(result.group(2)!!, context), newStart, newEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        }
                        StyleType.HEADING -> {
                            val size = 2.0f - (result.group(1)!!.length * 0.2f)
                            ssb.setSpan(RelativeSizeSpan(size), newStart, newEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                            ssb.setSpan(StyleSpan(Typeface.BOLD), newStart, newEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        }
                        StyleType.QUOTE -> {
                            ssb.setSpan(LeadingMarginSpan.Standard(60, 10), newStart, newEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                            ssb.setSpan(StyleSpan(Typeface.ITALIC), newStart, newEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        }
                        StyleType.SUPERSCRIPT -> {
                            ssb.setSpan(SuperscriptSpan(), newStart, newEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                            ssb.setSpan(RelativeSizeSpan(0.75f), newStart, newEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        }
                        StyleType.SUBSCRIPT -> {
                            ssb.setSpan(SubscriptSpan(), newStart, newEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                            ssb.setSpan(RelativeSizeSpan(0.75f), newStart, newEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        }
                        StyleType.TEXT_COLOR -> try {
                            ssb.setSpan(ForegroundColorSpan(Color.parseColor("#${result.group(1)!!}")), newStart, newEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        } catch (e: IllegalArgumentException) {
                            Log.w(TAG, "Invalid hex color")
                        }
                        else -> { /* No-op for MENTION_HASHTAG */ }
                    }
                    ssb.setSpan(ProcessedSpan(), newStart, newEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                StyleType.MENTION_HASHTAG -> {
                    val symbol = result.group(2)!!
                    val span = if (symbol == "@") ProfileSpan(context, result) else HashtagSpan(result)
                    
                    // Span is applied from the symbol to the end of the content
                    val spanStart = result.start(2)
                    val spanEnd = result.end(3)
                    ssb.setSpan(span, spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                    // Mark this specific type as processed
                    ssb.setSpan(ProcessedMentionOrHashtagSpan(), spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    finalEnd = end // Mark the whole match range to advance the main loop correctly
                }
            }
        }

        ssb.getSpans(0, ssb.length, ProcessedSpan::class.java).forEach { ssb.removeSpan(it) }
        ssb.getSpans(0, ssb.length, ProcessedMentionOrHashtagSpan::class.java).forEach { ssb.removeSpan(it) }
        textView.text = ssb
    }

    private class CodeBlockSpan(private val backgroundColor: Int, private val cornerRadius: Float, private val padding: Int = 12) : LineBackgroundSpan { /* ... implementation ... */ }
    private class LinkClickableSpan(private val url: String, private val context: Context) : ClickableSpan() { /* ... implementation ... */ }

    private class ProfileSpan(private val context: Context, private val matchResult: MatchResult) : ClickableSpan() {
        override fun onClick(view: View) {
            val rawUsername = matchResult.group(3) ?: return
            val sanitizedUsername = rawUsername.replace(".", "_").replace("#", "").replace("$", "").replace("[", "").replace("]", "")
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
                        Log.d(TAG, "UID not found for sanitized username: '$sanitizedUsername'")
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

    // FIX: Removed unnecessary Context from constructor
    private class HashtagSpan(private val matchResult: MatchResult) : ClickableSpan() {
        override fun onClick(view: View) {
            val hashtagContent = matchResult.group(3) ?: return
            Log.d(TAG, "Hashtag clicked: #$hashtagContent")
        }
        override fun updateDrawState(ds: TextPaint) {
            ds.color = Color.parseColor(MENTION_HASHTAG_COLOR)
            ds.isUnderlineText = false
            ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
    }
}