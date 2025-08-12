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
import kotlin.math.cos
import kotlin.math.exp

class TVeffects @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    companion object {
        private const val DEFAULT_CHAR_DELAY = 50L
        private const val SPRING_DURATION = 1000L
        private const val DEFAULT_DAMPING = 8.0
        private const val DEFAULT_FREQUENCY = 30.0
    }

    private var charDelay = DEFAULT_CHAR_DELAY
    private var totalDuration: Long = 0
    private var animator: ValueAnimator? = null
    private var dampingFactor = DEFAULT_DAMPING
    private var springFrequency = DEFAULT_FREQUENCY

    fun setCharDelay(delay: Long) {
        charDelay = delay.coerceAtLeast(0)
    }

    fun setTotalDuration(duration: Long) {
        totalDuration = duration.coerceAtLeast(0)
    }

    fun setSpringDamping(damping: Double) {
        dampingFactor = damping.coerceAtLeast(0.1)
    }

    fun setSpringFrequency(frequency: Double) {
        springFrequency = frequency.coerceAtLeast(1.0)
    }

    fun startTyping(text: CharSequence) {
        animator?.cancel()
        if (text.isEmpty()) {
            setText(text)
            return
        }

        val spannable = SpannableStringBuilder(text)
        val charDelayPerChar = calculateCharDelay(text.length)
        val spans = createAlphaSpans(spannable, text.length)
        val totalAnimDuration = (text.length * charDelayPerChar + SPRING_DURATION).coerceAtLeast(0)

        animator = ValueAnimator.ofFloat(0f, text.length.toFloat()).apply {
            duration = totalAnimDuration
            addUpdateListener { animation ->
                val progress = animation.animatedValue as Float
                updateAlphaForSpans(spans, charDelayPerChar, progress)
                invalidate()
            }
            start()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
        animator = null
    }

    private fun calculateCharDelay(textLength: Int): Long {
        return if (totalDuration > 0) totalDuration / textLength else charDelay
    }

    private fun createAlphaSpans(spannable: SpannableStringBuilder, length: Int): List<AlphaSpan> {
        return List(length) { index ->
            AlphaSpan(0f).also {
                spannable.setSpan(it, index, index + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }.also {
            setText(spannable)
        }
    }

    private fun updateAlphaForSpans(
        spans: List<AlphaSpan>,
        charDelay: Long,
        currentProgress: Float
    ) {
        spans.forEachIndexed { index, span ->
            val elapsed = (currentProgress - index) * charDelay
            val alpha = when {
                elapsed < 0 -> 0f
                elapsed > SPRING_DURATION -> 1f
                else -> springAlpha(elapsed.toDouble())
            }
            if (span.alpha != alpha) {
                span.alpha = alpha
            }
        }
    }

    private fun springAlpha(elapsedMillis: Double): Float {
        val t = elapsedMillis / 1000.0
        return (1 - exp(-dampingFactor * t) * cos(springFrequency * t)).toFloat()
    }

    private class AlphaSpan(var alpha: Float) : CharacterStyle(), UpdateAppearance {
        override fun updateDrawState(tp: android.text.TextPaint) {
            tp.alpha = (alpha * 255).toInt().coerceIn(0, 255)
        }
    }
}