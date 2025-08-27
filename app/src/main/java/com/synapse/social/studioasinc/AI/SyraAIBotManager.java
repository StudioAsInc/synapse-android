package com.synapse.social.studioasinc.AI;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

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
     * Start the Syra AI Bot service
     */
    public void startBot() {
        if (!isServiceRunning) {
            Intent serviceIntent = new Intent(context, SyraAIBotService.class);
            context.startService(serviceIntent);
            isServiceRunning = true;
            Log.d(TAG, "Syra AI Bot service started");
        }
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