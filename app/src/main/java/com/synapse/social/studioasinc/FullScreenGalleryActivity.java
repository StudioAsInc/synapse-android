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

import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;
import java.util.HashMap;

public class FullScreenGalleryActivity extends AppCompatActivity {
    
    private ViewPager2 viewPager;
    private TextView imageCounter;
    private ImageView backButton;
    private ImageView shareButton;
    private RecyclerView thumbnailRecycler;
    
    private ArrayList<HashMap<String, Object>> attachments;
    private int initialPosition = 0;
    private String messageText = "";
    
    private FullScreenImageAdapter imageAdapter;
    private ThumbnailAdapter thumbnailAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Make it fullscreen with immersive mode
        setupFullscreenUI();
        
        setContentView(R.layout.fullscreen_gallery);
        
        initializeViews();
        loadData();
        setupViewPager();
        setupThumbnails();
        setupListeners();
    }
    
    private void setupFullscreenUI() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                           WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
    }
    
    private void initializeViews() {
        viewPager = findViewById(R.id.viewPager);
        imageCounter = findViewById(R.id.imageCounter);
        backButton = findViewById(R.id.backButton);
        shareButton = findViewById(R.id.shareButton);
        thumbnailRecycler = findViewById(R.id.thumbnailRecycler);
    }
    
    private void loadData() {
        Intent intent = getIntent();
        if (intent.hasExtra("attachments")) {
            attachments = (ArrayList<HashMap<String, Object>>) intent.getSerializableExtra("attachments");
        }
        if (intent.hasExtra("position")) {
            initialPosition = intent.getIntExtra("position", 0);
        }
        if (intent.hasExtra("messageText")) {
            messageText = intent.getStringExtra("messageText");
        }
        
        if (attachments == null || attachments.isEmpty()) {
            Toast.makeText(this, "No images to display", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }
    
    private void setupViewPager() {
        imageAdapter = new FullScreenImageAdapter(attachments);
        viewPager.setAdapter(imageAdapter);
        viewPager.setCurrentItem(initialPosition, false);
        updateImageCounter(initialPosition);
        
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateImageCounter(position);
                thumbnailAdapter.setSelectedPosition(position);
                scrollThumbnailToPosition(position);
            }
        });
    }
    
    private void setupThumbnails() {
        if (attachments.size() > 1) {
            thumbnailRecycler.setVisibility(View.VISIBLE);
            thumbnailAdapter = new ThumbnailAdapter(attachments, initialPosition);
            thumbnailRecycler.setAdapter(thumbnailAdapter);
            thumbnailRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            
            // Add snap helper for smooth scrolling
            PagerSnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(thumbnailRecycler);
        } else {
            thumbnailRecycler.setVisibility(View.GONE);
        }
    }
    
    private void setupListeners() {
        backButton.setOnClickListener(v -> onBackPressed());
        
        shareButton.setOnClickListener(v -> {
            if (attachments != null && !attachments.isEmpty()) {
                int currentPosition = viewPager.getCurrentItem();
                HashMap<String, Object> currentAttachment = attachments.get(currentPosition);
                String url = currentAttachment.get("url").toString();
                shareImage(url);
            }
        });
    }
    
    private void updateImageCounter(int position) {
        if (attachments != null && !attachments.isEmpty()) {
            imageCounter.setText((position + 1) + " / " + attachments.size());
        }
    }
    
    private void scrollThumbnailToPosition(int position) {
        if (thumbnailRecycler != null && thumbnailAdapter != null) {
            thumbnailRecycler.smoothScrollToPosition(position);
        }
    }
    
    private void shareImage(String imageUrl) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, imageUrl);
            startActivity(Intent.createChooser(shareIntent, "Share Image"));
        } catch (Exception e) {
            Toast.makeText(this, "Failed to share image", Toast.LENGTH_SHORT).show();
        }
    }
    
    // Full screen image adapter
    private class FullScreenImageAdapter extends RecyclerView.Adapter<FullScreenImageAdapter.ImageViewHolder> {
        private ArrayList<HashMap<String, Object>> images;
        
        public FullScreenImageAdapter(ArrayList<HashMap<String, Object>> images) {
            this.images = images;
        }
        
        @NonNull
        @Override
        public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.fullscreen_image_item, parent, false);
            return new ImageViewHolder(view);
        }
        
        @Override
        public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
            HashMap<String, Object> attachment = images.get(position);
            String imageUrl = attachment.get("url").toString();
            
            Glide.with(FullScreenGalleryActivity.this)
                .load(imageUrl)
                .placeholder(R.drawable.ph_imgbluredsqure)
                .error(R.drawable.ph_imgbluredsqure)
                .into(holder.imageView);
                
            // Add click to open in browser for full quality
            holder.imageView.setOnClickListener(v -> {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(Color.parseColor("#242D39"));
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(FullScreenGalleryActivity.this, Uri.parse(imageUrl));
            });
        }
        
        @Override
        public int getItemCount() {
            return images.size();
        }
        
        class ImageViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            
            public ImageViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.fullscreenImage);
            }
        }
    }
    
    // Thumbnail adapter
    private class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailAdapter.ThumbnailViewHolder> {
        private ArrayList<HashMap<String, Object>> images;
        private int selectedPosition;
        
        public ThumbnailAdapter(ArrayList<HashMap<String, Object>> images, int initialPosition) {
            this.images = images;
            this.selectedPosition = initialPosition;
        }
        
        public void setSelectedPosition(int position) {
            int oldPosition = selectedPosition;
            selectedPosition = position;
            notifyItemChanged(oldPosition);
            notifyItemChanged(position);
        }
        
        @NonNull
        @Override
        public ThumbnailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.thumbnail_item, parent, false);
            return new ThumbnailViewHolder(view);
        }
        
        @Override
        public void onBindViewHolder(@NonNull ThumbnailViewHolder holder, int position) {
            HashMap<String, Object> attachment = images.get(position);
            String imageUrl = attachment.get("url").toString();
            
            Glide.with(FullScreenGalleryActivity.this)
                .load(imageUrl)
                .transform(new RoundedCorners(16))
                .placeholder(R.drawable.ph_imgbluredsqure)
                .error(R.drawable.ph_imgbluredsqure)
                .into(holder.thumbnailImage);
                
            // Highlight selected thumbnail
            if (position == selectedPosition) {
                holder.thumbnailCard.setCardElevation(8f);
                holder.thumbnailCard.setStrokeWidth(4);
                holder.thumbnailCard.setStrokeColor(getResources().getColor(R.color.colorPrimary));
            } else {
                holder.thumbnailCard.setCardElevation(2f);
                holder.thumbnailCard.setStrokeWidth(0);
            }
            
            holder.itemView.setOnClickListener(v -> {
                viewPager.setCurrentItem(position, true);
            });
        }
        
        @Override
        public int getItemCount() {
            return images.size();
        }
        
        class ThumbnailViewHolder extends RecyclerView.ViewHolder {
            CardView thumbnailCard;
            ImageView thumbnailImage;
            
            public ThumbnailViewHolder(@NonNull View itemView) {
                super(itemView);
                thumbnailCard = itemView.findViewById(R.id.thumbnailCard);
                thumbnailImage = itemView.findViewById(R.id.thumbnailImage);
            }
        }
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_out_down);
    }
}