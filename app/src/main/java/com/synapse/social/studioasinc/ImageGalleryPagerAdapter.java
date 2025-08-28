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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.HashMap;

public class ImageGalleryPagerAdapter extends RecyclerView.Adapter<ImageGalleryPagerAdapter.ImageViewHolder> {
    private final Context context;
    private final ArrayList<HashMap<String, Object>> attachments;
    
    public ImageGalleryPagerAdapter(Context context, ArrayList<HashMap<String, Object>> attachments) {
        this.context = context;
        this.attachments = attachments;
    }
    
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_gallery_image, parent, false);
        return new ImageViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        HashMap<String, Object> attachment = attachments.get(position);
        String imageUrl = (String) attachment.get("url");
        
        if (imageUrl != null && !imageUrl.isEmpty()) {
            holder.progressBar.setVisibility(View.VISIBLE);
            
            Glide.with(context)
                .load(imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .into(new com.bumptech.glide.request.target.CustomTarget<android.graphics.drawable.Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull android.graphics.drawable.Drawable resource, 
                                              com.bumptech.glide.request.transition.Transition<? super android.graphics.drawable.Drawable> transition) {
                        holder.progressBar.setVisibility(View.GONE);
                        holder.photoView.setImageDrawable(resource);
                    }
                    
                    @Override
                    public void onLoadCleared(android.graphics.drawable.Drawable placeholder) {
                        holder.progressBar.setVisibility(View.GONE);
                    }
                    
                    @Override
                    public void onLoadFailed(android.graphics.drawable.Drawable errorDrawable) {
                        holder.progressBar.setVisibility(View.GONE);
                        holder.photoView.setImageResource(R.drawable.ph_imgbluredsqure);
                    }
                });
        } else {
            holder.progressBar.setVisibility(View.GONE);
            holder.photoView.setImageResource(R.drawable.ph_imgbluredsqure);
        }
    }
    
    @Override
    public int getItemCount() {
        return attachments != null ? attachments.size() : 0;
    }
    
    static class ImageViewHolder extends RecyclerView.ViewHolder {
        PhotoView photoView;
        ProgressBar progressBar;
        
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            photoView = itemView.findViewById(R.id.photoView);
            progressBar = itemView.findViewById(R.id.progressBar);
            
            // Configure PhotoView for zoom and pan
            photoView.setMediumScale(2.0f);
            photoView.setMaximumScale(4.0f);
            photoView.setMinimumScale(0.5f);
        }
    }
}