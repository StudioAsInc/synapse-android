package com.synapse.social.studioasinc.AI;

import android.util.Log;
import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper class to setup the @syra account in the database
 * Creates the AI bot user profile if it doesn't exist
 */
public class SyraAccountSetup {
    
    private static final String TAG = "SyraAccountSetup";
    
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersRef;
    
    public SyraAccountSetup() {
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.usersRef = firebaseDatabase.getReference(SyraAIConfig.DB_USERS_PATH);
    }
    
    /**
     * Setup the Syra bot account if it doesn't exist
     */
    public void setupSyraAccount() {
        // Check if the account already exists
        usersRef.child(SyraAIConfig.BOT_UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    createSyraAccount();
                } else {
                    Log.d(TAG, "Syra account already exists");
                    updateSyraAccountIfNeeded(dataSnapshot);
                }
            }
            
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to check Syra account: " + databaseError.getMessage());
            }
        });
    }
    
    /**
     * Create the Syra bot account
     */
    private void createSyraAccount() {
        Map<String, Object> syraAccountData = new HashMap<>();
        
        // Basic profile information
        syraAccountData.put("username", SyraAIConfig.BOT_USERNAME);
        syraAccountData.put("nickname", SyraAIConfig.BOT_DISPLAY_NAME);
        syraAccountData.put("bio", SyraAIConfig.BOT_BIO);
        syraAccountData.put("email", "syra@synapse.ai");
        syraAccountData.put("isVerified", true);
        syraAccountData.put("isBot", true);
        syraAccountData.put("joinDate", System.currentTimeMillis());
        syraAccountData.put("lastSeen", System.currentTimeMillis());
        syraAccountData.put("isOnline", true);
        
        // Profile stats
        syraAccountData.put("followersCount", 0);
        syraAccountData.put("followingCount", 0);
        syraAccountData.put("postsCount", 0);
        
        // Bot specific settings
        syraAccountData.put("aiModel", SyraAIConfig.AI_MODEL);
        syraAccountData.put("botVersion", SyraAIConfig.BOT_VERSION);
        syraAccountData.put("autoResponseEnabled", SyraAIConfig.AUTO_RESPONSE_ENABLED);
        syraAccountData.put("randomPostingEnabled", SyraAIConfig.RANDOM_POSTING_ENABLED);
        syraAccountData.put("randomCommentingEnabled", SyraAIConfig.RANDOM_COMMENTING_ENABLED);
        
        // Profile picture and cover (you can update these URLs with actual images)
        syraAccountData.put("profilePicture", "https://i.imgur.com/default-ai-avatar.png");
        syraAccountData.put("coverPhoto", "https://i.imgur.com/default-ai-cover.png");
        
        // Privacy settings
        syraAccountData.put("isPrivate", false);
        syraAccountData.put("allowDirectMessages", true);
        syraAccountData.put("allowMentions", true);
        
        // Save the account
        usersRef.child(SyraAIConfig.BOT_UID).setValue(syraAccountData)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Syra account created successfully");
                    // Also add to username lookup
                    createUsernameMapping();
                } else {
                    Log.e(TAG, "Failed to create Syra account: " + task.getException());
                }
            });
    }
    
    /**
     * Create username mapping for the bot
     */
    private void createUsernameMapping() {
        DatabaseReference usernameRef = firebaseDatabase.getReference(SyraAIConfig.DB_USERNAMES_PATH);
        usernameRef.child(SyraAIConfig.BOT_USERNAME).setValue(SyraAIConfig.BOT_UID)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Syra username mapping created successfully");
                } else {
                    Log.e(TAG, "Failed to create username mapping: " + task.getException());
                }
            });
    }
    
    /**
     * Update account data if needed (for version updates)
     */
    private void updateSyraAccountIfNeeded(DataSnapshot dataSnapshot) {
        Map<String, Object> accountData = (Map<String, Object>) dataSnapshot.getValue();
        boolean needsUpdate = false;
        Map<String, Object> updates = new HashMap<>();
        
        // Check if bot-specific fields need to be added
        if (!accountData.containsKey("isBot")) {
            updates.put("isBot", true);
            needsUpdate = true;
        }
        
        if (!accountData.containsKey("botVersion")) {
            updates.put("botVersion", SyraAIConfig.BOT_VERSION);
            needsUpdate = true;
        }
        
        if (!accountData.containsKey("autoResponseEnabled")) {
            updates.put("autoResponseEnabled", true);
            needsUpdate = true;
        }
        
        if (!accountData.containsKey("randomPostingEnabled")) {
            updates.put("randomPostingEnabled", true);
            needsUpdate = true;
        }
        
        if (!accountData.containsKey("randomCommentingEnabled")) {
            updates.put("randomCommentingEnabled", true);
            needsUpdate = true;
        }
        
        // Update last seen
        updates.put("lastSeen", System.currentTimeMillis());
        updates.put("isOnline", true);
        needsUpdate = true;
        
        if (needsUpdate) {
            usersRef.child(SyraAIConfig.BOT_UID).updateChildren(updates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Syra account updated successfully");
                    } else {
                        Log.e(TAG, "Failed to update Syra account: " + task.getException());
                    }
                });
        }
    }
    
    /**
     * Update Syra's online status
     */
    public static void updateOnlineStatus(boolean isOnline) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference(SyraAIConfig.DB_USERS_PATH);
        Map<String, Object> statusUpdate = new HashMap<>();
        statusUpdate.put("isOnline", isOnline);
        statusUpdate.put("lastSeen", System.currentTimeMillis());
        
        usersRef.child(SyraAIConfig.BOT_UID).updateChildren(statusUpdate);
    }
    
    /**
     * Get Syra's UID
     */
    public static String getSyraUID() {
        return SyraAIConfig.BOT_UID;
    }
    
    /**
     * Get Syra's username
     */
    public static String getSyraUsername() {
        return SyraAIConfig.BOT_USERNAME;
    }
}