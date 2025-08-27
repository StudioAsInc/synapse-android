package com.synapse.social.studioasinc.AI;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Manager class for Syra AI Bot functionality
 * Provides easy integration points for existing activities
 */
public class SyraAIBotManager {
    
    private static final String TAG = "SyraAIBotManager";
    private static SyraAIBotManager instance;
    private Context context;
    private boolean isServiceRunning = false;
    
    private SyraAIBotManager(Context context) {
        this.context = context.getApplicationContext();
    }
    
    public static synchronized SyraAIBotManager getInstance(Context context) {
        if (instance == null) {
            instance = new SyraAIBotManager(context);
        }
        return instance;
    }
    
    /**
     * Start the Syra AI Bot service - only if authorized
     * Only the official @syra account should run the bot
     */
    public void startBot() {
        // Check if current user is authorized to run the bot
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Log.d(TAG, "No authenticated user - bot service not started");
            return;
        }
        
        String currentUserId = currentUser.getUid();
        if (!SyraAIConfig.isAuthorizedToRunBot(currentUserId)) {
            Log.d(TAG, "User " + currentUserId + " not authorized to run bot service");
            return;
        }
        
        if (!isServiceRunning) {
            Intent serviceIntent = new Intent(context, SyraAIBotService.class);
            context.startService(serviceIntent);
            isServiceRunning = true;
            Log.d(TAG, "Syra AI Bot service started for authorized user: " + currentUserId);
        }
    }
    
    /**
     * Check if the current user is authorized to run the bot
     */
    public boolean isCurrentUserAuthorized() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        return SyraAIConfig.isAuthorizedToRunBot(currentUser.getUid());
    }
    
    /**
     * Stop the Syra AI Bot service
     */
    public void stopBot() {
        if (isServiceRunning) {
            Intent serviceIntent = new Intent(context, SyraAIBotService.class);
            context.stopService(serviceIntent);
            isServiceRunning = false;
            Log.d(TAG, "Syra AI Bot service stopped");
        }
    }
    
    /**
     * Check if a message contains a mention of @syra
     */
    public static boolean containsSyraMention(String text) {
        return SyraAIConfig.containsSyraMention(text);
    }
    
    /**
     * Check if the current user is the Syra bot
     */
    public static boolean isSyraBot(String userId) {
        return SyraAIConfig.isSyraBot(userId);
    }
    
    /**
     * Get the Syra bot user ID
     */
    public static String getSyraUID() {
        return SyraAIConfig.BOT_UID;
    }
    
    /**
     * Get the Syra bot username
     */
    public static String getSyraUsername() {
        return SyraAIConfig.BOT_USERNAME;
    }
}