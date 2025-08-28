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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageCarouselAdapter extends RecyclerView.Adapter<MessageCarouselAdapter.CarouselViewHolder> {
    
    private ArrayList<HashMap<String, Object>> attachments;
    private Context context;
    private OnImageClickListener onImageClickListener;
    
    public interface OnImageClickListener {
        void onImageClick(int position, ArrayList<HashMap<String, Object>> attachments);
    }
    
    public MessageCarouselAdapter(ArrayList<HashMap<String, Object>> attachments, Context context) {
        this.attachments = attachments;
        this.context = context;
    }
    
    public void setOnImageClickListener(OnImageClickListener listener) {
        this.onImageClickListener = listener;
    }
    
    @NonNull
    @Override
    public CarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.carousel_image_item, parent, false);
        return new CarouselViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull CarouselViewHolder holder, int position) {
        HashMap<String, Object> attachment = attachments.get(position);
        String imageUrl = String.valueOf(attachment.get("url"));
        String publicId = String.valueOf(attachment.getOrDefault("publicId", ""));
        
        // Load image with rounded corners
        Glide.with(context)
            .load(imageUrl)
            .transform(new RoundedCorners(24))
            .placeholder(R.drawable.ph_imgbluredsqure)
            .error(R.drawable.ph_imgbluredsqure)
            .into(holder.carouselImage);
        
        // Show video overlay if it's a video
        if (publicId.contains("|video")) {
            holder.overlayContainer.setVisibility(View.VISIBLE);
        } else {
            holder.overlayContainer.setVisibility(View.GONE);
        }
        
        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (onImageClickListener != null) {
                onImageClickListener.onImageClick(position, attachments);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return attachments != null ? attachments.size() : 0;
    }
    
    class CarouselViewHolder extends RecyclerView.ViewHolder {
        ImageView carouselImage;
        RelativeLayout overlayContainer;
        
        public CarouselViewHolder(@NonNull View itemView) {
            super(itemView);
            carouselImage = itemView.findViewById(R.id.carouselImage);
            overlayContainer = itemView.findViewById(R.id.overlayContainer);
        }
    }
}