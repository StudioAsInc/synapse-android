package com.synapse.social.studioasinc.thanos;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

public class Utils {
    public static Bitmap createBitmapFromView(View view) {
        if (view == null || view.getWidth() == 0 || view.getHeight() == 0) {
            return null;
        }
        view.clearFocus();
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        if (bitmap != null) {
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
        }
        return bitmap;
    }
}
