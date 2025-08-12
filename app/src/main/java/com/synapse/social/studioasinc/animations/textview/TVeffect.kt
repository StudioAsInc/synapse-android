/*
Copyright 2025 StudioAs Inc. (Ashik)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.synapse.social.studioasinc.animations.textview

import android.animation.ValueAnimator
import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.CharacterStyle
import android.text.style.UpdateAppearance
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import java.lang.Float.max
import java.lang.Float.min

class TVeffects @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    private var charDelay: Long = 50L
    private var fadeDuration: Long = 200L
    private var totalDuration: Long = 0L
    private var animator: ValueAnimator? = null
    private var spans: Array<AlphaSpan>? = null

    fun setCharDelay(delay: Long) {
        charDelay = delay
    }

    fun setFadeDuration(duration: Long) {
        fadeDuration = max(duration, 0)
    }

    fun setTotalDuration(duration: Long) {
        totalDuration = max(duration, 0)
    }

    fun startTyping(text: CharSequence) {
        stopAnimation()
        
        if (text.isEmpty()) {
            setText("")
            return
        }

        val delayPerChar = if (totalDuration > 0) totalDuration / text.length else charDelay
        startTypingInternal(text, delayPerChar)
    }

    private fun startTypingInternal(text: CharSequence, charDelay: Long) {
        val spannable = SpannableStringBuilder(text)
        setText(spannable, BufferType.SPANNABLE)

        val textLength = text.length
        spans = Array(textLength) { AlphaSpan(0f) }

        for (i in 0 until textLength) {
            spannable.setSpan(spans!![i], i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        animator = ValueAnimator.ofFloat(0f, (textLength * charDelay + fadeDuration).toFloat()).apply {
            duration = (textLength * charDelay + fadeDuration)
            
            addUpdateListener { animation ->
                val currentTime = animation.animatedValue as Float
                val fadeDurationF = fadeDuration.toFloat()
                val charDelayF = charDelay.toFloat()
                
                for (i in 0 until textLength) {
                    val startTime = i * charDelayF
                    if (currentTime < startTime) break
                    
                    val fraction = if (fadeDurationF <= 0f) {
                        if (currentTime >= startTime) 1f else 0f
                    } else {
                        min(1f, max(0f, (currentTime - startTime) / fadeDurationF))
                    }
                    
                    spans!![i].alpha = fraction
                }
                invalidate()
            }
            
            start()
        }
    }

    fun stopAnimation() {
        animator?.let {
            if (it.isRunning) it.cancel()
            it.removeAllUpdateListeners()
        }
        animator = null
        spans = null
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnimation()
    }

    private class AlphaSpan(var alpha: Float) : CharacterStyle(), UpdateAppearance {
        override fun updateDrawState(tp: android.text.TextPaint) {
            tp.alpha = (alpha * 255).toInt().coerceIn(0, 255)
        }
    }
}