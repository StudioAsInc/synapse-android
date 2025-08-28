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

import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ImageGalleryActivity extends AppCompatActivity {
    private static final String TAG = "ImageGalleryActivity";
    
    private ViewPager2 viewPager;
    private TextView counterText;
    private ImageView closeButton;
    private FloatingActionButton downloadFab;
    private ArrayList<HashMap<String, Object>> attachments;
    private ImageGalleryPagerAdapter adapter;
    private int initialPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Full screen immersive mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
        
        setContentView(R.layout.activity_image_gallery);
        
        initViews();
        handleIntent();
        setupViewPager();
        setupClickListeners();
    }
    
    private void initViews() {
        viewPager = findViewById(R.id.viewPager);
        counterText = findViewById(R.id.counterText);
        closeButton = findViewById(R.id.closeButton);
        downloadFab = findViewById(R.id.downloadFab);
    }
    
    private void handleIntent() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("attachments")) {
            attachments = (ArrayList<HashMap<String, Object>>) intent.getSerializableExtra("attachments");
            initialPosition = intent.getIntExtra("position", 0);
        } else {
            // No attachments provided, finish activity
            finish();
            return;
        }
        
        if (attachments == null || attachments.isEmpty()) {
            finish();
            return;
        }
    }
    
    private void setupViewPager() {
        adapter = new ImageGalleryPagerAdapter(this, attachments);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(initialPosition, false);
        
        updateCounter(initialPosition);
        
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateCounter(position);
            }
        });
    }
    
    private void setupClickListeners() {
        closeButton.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        });
        
        downloadFab.setOnClickListener(v -> downloadCurrentImage());
        
        // Toggle UI visibility on tap
        findViewById(R.id.rootLayout).setOnClickListener(v -> toggleUIVisibility());
    }
    
    private void updateCounter(int position) {
        String counterFormat = String.format("%d of %d", position + 1, attachments.size());
        counterText.setText(counterFormat);
    }
    
    private void downloadCurrentImage() {
        if (attachments == null || attachments.isEmpty()) return;
        
        int currentPosition = viewPager.getCurrentItem();
        if (currentPosition >= 0 && currentPosition < attachments.size()) {
            HashMap<String, Object> attachment = attachments.get(currentPosition);
            String imageUrl = (String) attachment.get("url");
            
            if (imageUrl != null && !imageUrl.isEmpty()) {
                downloadImage(imageUrl);
            } else {
                Toast.makeText(this, "Unable to download image", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private void downloadImage(String imageUrl) {
        // Use Android DownloadManager for image download
        try {
            android.app.DownloadManager downloadManager = (android.app.DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            android.app.DownloadManager.Request request = new android.app.DownloadManager.Request(Uri.parse(imageUrl));
            
            String fileName = "synapse_image_" + System.currentTimeMillis() + ".jpg";
            request.setTitle("Synapse Image");
            request.setDescription("Downloading image...");
            request.setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
            
            downloadManager.enqueue(request);
            Toast.makeText(this, "Download started", Toast.LENGTH_SHORT).show();
            
        } catch (Exception e) {
            Toast.makeText(this, "Download failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    private void toggleUIVisibility() {
        boolean isVisible = counterText.getVisibility() == View.VISIBLE;
        int visibility = isVisible ? View.GONE : View.VISIBLE;
        
        counterText.setVisibility(visibility);
        closeButton.setVisibility(visibility);
        downloadFab.setVisibility(visibility);
        
        if (isVisible) {
            // Hide system UI
            getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        } else {
            // Show system UI
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (viewPager != null && viewPager.getAdapter() != null) {
            viewPager.setAdapter(null);
        }
    }
}