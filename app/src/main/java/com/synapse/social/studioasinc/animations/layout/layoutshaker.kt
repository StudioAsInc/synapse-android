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



package com.synapse.social.studioasinc.animations.layout

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.Interpolator
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

object layoutshaker {

    private const val DEFAULT_AMPLITUDE = 20f
    private const val DEFAULT_CYCLES = 7f
    private const val DEFAULT_DURATION = 700L

    @JvmStatic
    fun shake(view: View) {
        shake(view, DEFAULT_AMPLITUDE, DEFAULT_CYCLES, DEFAULT_DURATION)
    }

    @JvmStatic
    fun shake(
        view: View,
        amplitude: Float = DEFAULT_AMPLITUDE,
        cycles: Float = DEFAULT_CYCLES,
        duration: Long = DEFAULT_DURATION
    ) {
        view.clearAnimation() // Clears legacy anims
        ObjectAnimator.ofFloat(view, "translationX", 0f).apply {
            interpolator = SpringInterpolator(amplitude, cycles)
            this.duration = duration
            start()
        }
    }

    private class SpringInterpolator(
        private val amplitude: Float,
        private val cycles: Float
    ) : Interpolator {
        private val beta = 4.0f // Damping factor

        override fun getInterpolation(time: Float): Float {
            // Damped sine wave
            return (sin(2 * PI * cycles * time) * exp(-beta * time)).toFloat()
        }
    }
}