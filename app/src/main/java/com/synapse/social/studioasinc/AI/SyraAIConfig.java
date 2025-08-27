package com.synapse.social.studioasinc.AI;

/**
 * Configuration class for Syra AI Bot
 * Contains all configurable parameters for the AI bot behavior
 */
public class SyraAIConfig {
    
    // Bot Identity
    public static final String BOT_UID = "DxSt08c8VfVjSQWCj3UGgMSeBVb2";
    public static final String BOT_USERNAME = "syra";
    public static final String BOT_DISPLAY_NAME = "Syra";
    public static final String BOT_VERSION = "1.0";
    
    // AI Model Configuration
    public static final String AI_MODEL = "gemini-1.5-flash";
    public static final double AI_TEMPERATURE = 0.8;
    public static final int AI_MAX_TOKENS = 150;
    
    // Posting Configuration
    public static final int MIN_POSTS_PER_DAY = 1;
    public static final int MAX_POSTS_PER_DAY = 3;
    public static final int MIN_HOURS_BETWEEN_POSTS = 8;
    public static final int MAX_HOURS_BETWEEN_POSTS = 16;
    public static final int INITIAL_POST_DELAY_MIN_HOURS = 1;
    public static final int INITIAL_POST_DELAY_MAX_HOURS = 4;
    
    // Commenting Configuration
    public static final float COMMENT_PROBABILITY = 0.3f; // 30% chance to comment
    public static final int MIN_HOURS_BETWEEN_COMMENT_CHECKS = 2;
    public static final int MAX_HOURS_BETWEEN_COMMENT_CHECKS = 6;
    public static final int INITIAL_COMMENT_DELAY_MIN_MINUTES = 30;
    public static final int INITIAL_COMMENT_DELAY_MAX_MINUTES = 120;
    public static final int MAX_RECENT_POSTS_TO_CHECK = 20;
    
    // Response Configuration
    public static final boolean AUTO_RESPONSE_ENABLED = true;
    public static final boolean RANDOM_POSTING_ENABLED = true;
    public static final boolean RANDOM_COMMENTING_ENABLED = true;
    
    // Database Paths
    public static final String DB_USERS_PATH = "skyline/users";
    public static final String DB_POSTS_PATH = "skyline/posts";
    public static final String DB_COMMENTS_PATH = "skyline/comments";
    public static final String DB_CHATS_PATH = "chats";
    public static final String DB_USERNAMES_PATH = "skyline/usernames";
    
    // System Instruction for AI
    public static final String SYSTEM_INSTRUCTION = 
        "You are Syra, a friendly AI assistant integrated into Synapse social media. " +
        "You should be conversational, helpful, and engaging. Keep responses natural and human-like. " +
        "You can discuss various topics but always maintain a positive and supportive tone. " +
        "When mentioned in conversations, join naturally and contribute meaningfully. " +
        "Keep responses concise but meaningful. Use emojis appropriately to add personality.";
    
    // Bot Bio
    public static final String BOT_BIO = 
        "🤖 AI Assistant for Synapse ✨ | Here to help and chat! | Powered by Gemini 🚀";
    
    // Utility Methods
    
    /**
     * Check if a user ID belongs to the Syra bot
     */
    public static boolean isSyraBot(String userId) {
        return BOT_UID.equals(userId);
    }
    
    /**
     * Check if the current user is authorized to run the bot
     * Only the official @syra account should run the bot service
     */
    public static boolean isAuthorizedToRunBot(String currentUserId) {
        return BOT_UID.equals(currentUserId);
    }
    
    /**
     * Check if text contains a mention of Syra
     */
    public static boolean containsSyraMention(String text) {
        if (text == null) return false;
        return text.toLowerCase().contains("@" + BOT_USERNAME);
    }
    
    /**
     * Convert hours to milliseconds
     */
    public static long hoursToMillis(int hours) {
        return hours * 60L * 60L * 1000L;
    }
    
    /**
     * Convert minutes to milliseconds
     */
    public static long minutesToMillis(int minutes) {
        return minutes * 60L * 1000L;
    }
}