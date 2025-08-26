package com.synapse.social.studioasinc.styling

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.synapse.social.studioasinc.ProfileActivity
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonVisitor
import io.noties.markwon.core.CorePlugin
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
import io.noties.markwon.syntax.SyntaxHighlightPlugin
import org.commonmark.node.Text
import java.util.regex.Pattern

class TextStylingUtil(private val context: Context) {

    private val markwon = Markwon.builder(context)
        .usePlugin(CorePlugin.create())
        .usePlugin(StrikethroughPlugin.create())
        .usePlugin(TablePlugin.create(context))
        .usePlugin(TaskListPlugin.create(context))
        .usePlugin(SyntaxHighlightPlugin.create())
        .usePlugin(object : AbstractMarkwonPlugin() {
            override fun configureVisitor(builder: MarkwonVisitor.Builder) {
                builder.on(Text::class.java) { visitor, text ->
                    val spannableBuilder = visitor.builder()
                    val matcher = Pattern.compile("([@#])([A-Za-z0-9_.-]+)").matcher(text.literal)
                    var lastEnd = 0

                    while (matcher.find()) {
                        val start = matcher.start()
                        if (start > lastEnd) {
                            visitor.visit(Text(text.literal.substring(lastEnd, start)))
                        }

                        val symbol = matcher.group(1)
                        val handle = matcher.group(0)
                        val span = if (symbol == "@") ProfileSpan(context, handle) else HashtagSpan(handle)
                        val spanStart = spannableBuilder.length()
                        spannableBuilder.append(handle)
                        val spanEnd = spannableBuilder.length()
                        spannableBuilder.setSpan(span, spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                        lastEnd = matcher.end()
                    }

                    if (lastEnd < text.literal.length) {
                        visitor.visit(Text(text.literal.substring(lastEnd)))
                    }
                }
            }
        })
        .build()

    fun applyStyling(text: String, textView: TextView) {
        markwon.setMarkdown(textView, text)
        textView.movementMethod = LinkMovementMethod.getInstance()
    }

    private class ProfileSpan(private val context: Context, private val handle: String) : ClickableSpan() {
        override fun onClick(view: View) {
            val username = handle.substring(1)
            val db = FirebaseDatabase.getInstance().getReference("synapse/username").child(username)
            db.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val uid = snapshot.child("uid").getValue(String::class.java)
                    if (uid != null && uid != "null") {
                        context.startActivity(
                            Intent(context, ProfileActivity::class.java)
                                .putExtra("uid", uid)
                        )
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("ProfileSpan", "Database error", error.toException())
                }
            })
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.color = Color.parseColor("#445E91")
            ds.isUnderlineText = false
            ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
    }

    private class HashtagSpan(private val hashtag: String) : ClickableSpan() {
        override fun onClick(view: View) {
            Log.d("HashtagSpan", "Clicked: $hashtag")
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.color = Color.parseColor("#445E91")
            ds.isUnderlineText = false
            ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
    }
}