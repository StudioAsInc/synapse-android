package com.synapse.social.studioasinc.thanos;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.Random;

public class ThanosSnap {

    private ArrayList<Particle> mParticles;
    private ValueAnimator mAnimator;
    private View mContainer;
    private OnAnimationListener mListener;

    public ThanosSnap(View view, OnAnimationListener listener) {
        this.mListener = listener;
        this.mContainer = view;
        mParticles = new ArrayList<>();
        mAnimator = ValueAnimator.ofFloat(0f, 1f).setDuration(2000);
        mAnimator.setInterpolator(new LinearInterpolator());

        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                for (Particle p : mParticles) {
                    p.mAlpha = 1 - animation.getAnimatedFraction();
                    p.mCx += (float) (Math.random() - 0.5f) * 20f;
                    p.mCy += (float) (Math.random() - 0.5f) * 20f;
                }
                mContainer.invalidate();
            }
        });

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (mListener != null) {
                    mListener.onAnimationStart();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mListener != null) {
                    mListener.onAnimationEnd();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if (mListener != null) {
                    mListener.onAnimationEnd();
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void start() {
        Bitmap bitmap = Utils.createBitmapFromView(mContainer);
        if (bitmap == null) {
            if(mListener != null) {
                mListener.onAnimationEnd();
            }
            return;
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int particleSize = 8;

        for (int i = 0; i < width; i += particleSize) {
            for (int j = 0; j < height; j += particleSize) {
                int color = bitmap.getPixel(i, j);
                if (color != 0) {
                    mParticles.add(new Particle(color, i, j, particleSize));
                }
            }
        }

        mAnimator.start();
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        for (Particle p : mParticles) {
            paint.setColor(p.mColor);
            paint.setAlpha((int) (p.mAlpha * 255));
            canvas.drawCircle(p.mCx, p.mCy, p.mRadius, paint);
        }
    }

    public interface OnAnimationListener {
        void onAnimationStart();
        void onAnimationEnd();
    }
}
