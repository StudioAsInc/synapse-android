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

import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.synapse.social.studioasinc.config.CloudinaryConfig;
import com.synapse.social.studioasinc.model.Attachment;
import com.synapse.social.studioasinc.util.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageImageCarouselAdapter extends RecyclerView.Adapter<MessageImageCarouselAdapter.ImageCarouselViewHolder> {
    
    private static final String TAG = "MessageImageCarouselAdapter";
    
    private final Context context;
    private final ArrayList<HashMap<String, Object>> attachments;
    private final OnImageClickListener onImageClickListener;
    private final int imageSize;
    
    public interface OnImageClickListener {
        void onImageClick(int position, ArrayList<HashMap<String, Object>> attachments);
    }
    
    public MessageImageCarouselAdapter(Context context, ArrayList<HashMap<String, Object>> attachments, OnImageClickListener listener) {
        this.context = context;
        this.attachments = attachments;
        this.onImageClickListener = listener;
        this.imageSize = UIUtils.dpToPx(context, 200); // Standard size for carousel images
    }
    
    @NonNull
    @Override
    public ImageCarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message_carousel_image, parent, false);
        return new ImageCarouselViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ImageCarouselViewHolder holder, int position) {
        HashMap<String, Object> attachment = attachments.get(position);
        String publicId = (String) attachment.get("publicId");
        
        // Set consistent size for all carousel images
        ViewGroup.LayoutParams layoutParams = holder.cardView.getLayoutParams();
        layoutParams.width = imageSize;
        layoutParams.height = imageSize;
        holder.cardView.setLayoutParams(layoutParams);
        
        if (publicId != null && !publicId.isEmpty()) {
            String imageUrl = CloudinaryConfig.buildCarouselImageUrl(publicId);
            
            Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.ph_imgbluredsqure)
                .error(R.drawable.ph_imgbluredsqure)
                .transform(new RoundedCorners(UIUtils.dpToPx(context, 16)))
                .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.ph_imgbluredsqure);
        }
        
        // Set click listener to open gallery
        holder.itemView.setOnClickListener(v -> {
            if (onImageClickListener != null) {
                onImageClickListener.onImageClick(position, attachments);
            }
        });
        
        // Add ripple effect with hover animation
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        holder.cardView.setForeground(context.getDrawable(outValue.resourceId));
        
        // Add subtle scale animation on touch
        holder.itemView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case android.view.MotionEvent.ACTION_DOWN:
                    v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).start();
                    break;
                case android.view.MotionEvent.ACTION_UP:
                case android.view.MotionEvent.ACTION_CANCEL:
                    v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start();
                    break;
            }
            return false; // Allow the click listener to handle the click
        });
    }
    
    @Override
    public int getItemCount() {
        return attachments != null ? attachments.size() : 0;
    }
    
    static class ImageCarouselViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView;
        
        public ImageCarouselViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.carouselImageCard);
            imageView = itemView.findViewById(R.id.carouselImageView);
        }
    }
}