package com.synapse.social.studioasinc.util;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Utility class to manage authentication state with backup mechanism
 * This helps ensure authentication persists even when Firebase Auth fails
 */
public final class AuthStateManager {
    
    private static final String PREFS_NAME = "auth_prefs";
    private static final String KEY_IS_AUTHENTICATED = "is_authenticated";
    private static final String KEY_USER_UID = "user_uid";
    
    // Prevent instantiation
    private AuthStateManager() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * Save authentication state to SharedPreferences as backup
     * @param context Application context
     * @param uid User UID
     */
    public static void saveAuthenticationState(Context context, String uid) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit()
                .putBoolean(KEY_IS_AUTHENTICATED, true)
                .putString(KEY_USER_UID, uid)
                .apply();
    }
    
    /**
     * Clear authentication state from SharedPreferences
     * @param context Application context
     */
    public static void clearAuthenticationState(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }
    
    /**
     * Check if user is authenticated (either via Firebase or backup)
     * @param context Application context
     * @return true if user is authenticated
     */
    public static boolean isUserAuthenticated(Context context) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return true;
        }
        
        // Check backup authentication
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_IS_AUTHENTICATED, false);
    }
    
    /**
     * Get user UID from Firebase or backup authentication
     * @param context Application context
     * @return User UID or null if not authenticated
     */
    public static String getUserUid(Context context) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUid();
        }
        
        // Try to get UID from backup authentication
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_USER_UID, null);
    }
    
    /**
     * Sign out user from both Firebase and backup authentication
     * @param context Application context
     */
    public static void signOut(Context context) {
        FirebaseAuth.getInstance().signOut();
        clearAuthenticationState(context);
    }
}