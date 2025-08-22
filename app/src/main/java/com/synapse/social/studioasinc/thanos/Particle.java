package com.synapse.social.studioasinc.thanos;

import android.graphics.Point;
import android.graphics.Rect;

public class Particle {
    float mAlpha;
    int mColor;
    float mCx;
    float mCy;
    float mRadius;
    float mInitialX;
    float mInitialY;

    public Particle(int color, float x, float y, int radius) {
        this.mColor = color;
        this.mCx = x;
        this.mCy = y;
        this.mRadius = radius;
        this.mAlpha = 1.0f;
        this.mInitialX = x;
        this.mInitialY = y;
    }
}
