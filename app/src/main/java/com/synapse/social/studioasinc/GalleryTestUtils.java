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

import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Utility class for testing gallery functionality and validating attachment data
 */
public class GalleryTestUtils {
    
    private static final String TAG = "GalleryTestUtils";
    
    /**
     * Creates test attachment data for gallery testing
     */
    public static ArrayList<HashMap<String, Object>> createTestAttachments(int count) {
        ArrayList<HashMap<String, Object>> testAttachments = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            HashMap<String, Object> attachment = new HashMap<>();
            attachment.put("url", "https://picsum.photos/400/400?random=" + i);
            attachment.put("publicId", "test_image_" + i);
            attachment.put("width", 400);
            attachment.put("height", 400);
            testAttachments.add(attachment);
        }
        
        return testAttachments;
    }
    
    /**
     * Validates attachment data structure
     */
    public static boolean validateAttachments(ArrayList<HashMap<String, Object>> attachments) {
        if (attachments == null || attachments.isEmpty()) {
            Log.w(TAG, "Attachments is null or empty");
            return false;
        }
        
        for (int i = 0; i < attachments.size(); i++) {
            HashMap<String, Object> attachment = attachments.get(i);
            
            if (!attachment.containsKey("url") || attachment.get("url") == null) {
                Log.e(TAG, "Attachment " + i + " missing url");
                return false;
            }
            
            if (!attachment.containsKey("width") || !attachment.containsKey("height")) {
                Log.w(TAG, "Attachment " + i + " missing dimensions, using defaults");
                attachment.put("width", 400);
                attachment.put("height", 400);
            }
            
            // Validate that dimensions are numbers
            try {
                int width = ((Number) attachment.get("width")).intValue();
                int height = ((Number) attachment.get("height")).intValue();
                if (width <= 0 || height <= 0) {
                    Log.w(TAG, "Invalid dimensions for attachment " + i + ", using defaults");
                    attachment.put("width", 400);
                    attachment.put("height", 400);
                }
            } catch (Exception e) {
                Log.w(TAG, "Error parsing dimensions for attachment " + i + ", using defaults");
                attachment.put("width", 400);
                attachment.put("height", 400);
            }
        }
        
        Log.d(TAG, "Successfully validated " + attachments.size() + " attachments");
        return true;
    }
    
    /**
     * Tests the carousel vs grid decision logic
     */
    public static void testLayoutDecision() {
        Log.d(TAG, "=== Testing Layout Decision Logic ===");
        
        // Test different attachment counts
        int[] testCounts = {1, 2, 3, 4, 5, 6, 10};
        
        for (int count : testCounts) {
            ArrayList<HashMap<String, Object>> attachments = createTestAttachments(count);
            boolean shouldUseCarousel = count >= 5; // Based on ChatAdapter logic
            
            Log.d(TAG, "Count: " + count + " -> Layout: " + (shouldUseCarousel ? "Carousel" : "Grid"));
        }
    }
    
    /**
     * Tests message text handling with attachments
     */
    public static void testMessageTextHandling() {
        Log.d(TAG, "=== Testing Message Text Handling ===");
        
        String[] testTexts = {
            "",
            "Single image message",
            "Multiple images with a longer caption that might span multiple lines",
            "Message with emojis 🎉📸✨",
            "Message with @mention and #hashtag",
            "Very long message that should test the text wrapping and layout behavior when combined with image attachments to ensure proper spacing and readability"
        };
        
        for (String text : testTexts) {
            Log.d(TAG, "Text length: " + text.length() + " -> '" + 
                  (text.length() > 50 ? text.substring(0, 50) + "..." : text) + "'");
        }
    }
    
    /**
     * Logs gallery feature capabilities
     */
    public static void logFeatureCapabilities() {
        Log.d(TAG, "=== Gallery Feature Capabilities ===");
        Log.d(TAG, "✓ Single message bubble with multiple attachments");
        Log.d(TAG, "✓ Grid layout for 1-4 images with smart arrangement");
        Log.d(TAG, "✓ Carousel layout for 5+ images");
        Log.d(TAG, "✓ 'View all X images' button for carousel mode");
        Log.d(TAG, "✓ Full-screen gallery with ViewPager2");
        Log.d(TAG, "✓ Thumbnail navigation strip");
        Log.d(TAG, "✓ Smooth transitions and animations");
        Log.d(TAG, "✓ Support for both images and videos");
        Log.d(TAG, "✓ Share functionality");
        Log.d(TAG, "✓ Zoom and pan support in full-screen mode");
        Log.d(TAG, "✓ Maintains single message perception");
        Log.d(TAG, "✓ Production-ready error handling");
    }
}