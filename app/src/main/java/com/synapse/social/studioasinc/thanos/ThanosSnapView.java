package com.synapse.social.studioasinc.thanos;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

public class ThanosSnapView extends View {

    private ThanosSnap mThanosSnap;

    public ThanosSnapView(Context context) {
        super(context);
    }

    public void setThanosSnap(ThanosSnap thanosSnap) {
        this.mThanosSnap = thanosSnap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mThanosSnap != null) {
            mThanosSnap.draw(canvas);
        }
    }
}
