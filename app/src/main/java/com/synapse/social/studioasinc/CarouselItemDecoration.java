/**
 * CONFIDENTIAL AND PROPRIETARY
 * 
 * This source code is the sole property of StudioAs Inc. Synapse. (Ashik).
 * Any reproduction, modification, distribution, or exploitation in any form
 * without explicit written permission from the owner is strictly prohibited.
 * 
 * Copyright (c) 2025 StudioAs Inc. Synapse. (Ashik)
 * All rights reserved.
 */

package com.synapse.social.studioasinc;

import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CarouselItemDecoration extends RecyclerView.ItemDecoration {
    
    private final int spacing;
    
    public CarouselItemDecoration(int spacingDp) {
        this.spacing = spacingDp;
    }
    
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, 
                              @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int itemCount = state.getItemCount();
        
        int spacingPx = dpToPx(view, spacing);
        
        // Add spacing to start of first item
        if (position == 0) {
            outRect.left = spacingPx;
        }
        
        // Add spacing to end of all items
        outRect.right = spacingPx;
        
        // Add extra spacing to end of last item for better UX
        if (position == itemCount - 1) {
            outRect.right = spacingPx * 2;
        }
    }
    
    private int dpToPx(View view, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, 
                view.getResources().getDisplayMetrics());
    }
}