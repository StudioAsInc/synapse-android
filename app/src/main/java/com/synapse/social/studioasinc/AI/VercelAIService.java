package com.synapse.social.studioasinc.AI;

import android.util.Log;
import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Service to communicate with Vercel backend for AI operations
 * Replaces local Gemini AI processing with serverless backend calls
 */
public class VercelAIService {
    
    private static final String TAG = "VercelAIService";
    private static final String BASE_URL = "https://your-vercel-app.vercel.app/api";
    
    private ExecutorService executorService;
    
    public interface VercelCallback {
        void onSuccess(String response);
        void onError(String error);
    }
    
    public VercelAIService() {
        this.executorService = Executors.newCachedThreadPool();
    }
    
    /**
     * Generate AI response for mentions
     */
    public void generateMentionResponse(String message, String userId, String userName, 
                                      String context, VercelCallback callback) {
        executorService.execute(() -> {
            try {
                JSONObject requestBody = new JSONObject();
                requestBody.put("message", message);
                requestBody.put("userId", userId);
                requestBody.put("userName", userName);
                requestBody.put("context", context);
                requestBody.put("botUserId", SyraAIConfig.BOT_UID);
                
                String response = makeApiCall("/ai/mention-response", requestBody);
                JSONObject responseJson = new JSONObject(response);
                
                if (responseJson.getBoolean("success")) {
                    String aiResponse = responseJson.getString("response");
                    callback.onSuccess(aiResponse);
                } else {
                    callback.onError("API returned error");
                }
                
            } catch (Exception e) {
                Log.e(TAG, "Error generating mention response: " + e.getMessage());
                callback.onError(e.getMessage());
            }
        });
    }
    
    /**
     * Generate automatic post content
     */
    public void generateAutoPost(String postType, VercelCallback callback) {
        executorService.execute(() -> {
            try {
                JSONObject requestBody = new JSONObject();
                requestBody.put("botUserId", SyraAIConfig.BOT_UID);
                requestBody.put("postType", postType);
                
                String response = makeApiCall("/ai/auto-post", requestBody);
                JSONObject responseJson = new JSONObject(response);
                
                if (responseJson.getBoolean("success")) {
                    String postContent = responseJson.getString("content");
                    callback.onSuccess(postContent);
                } else {
                    callback.onError("API returned error");
                }
                
            } catch (Exception e) {
                Log.e(TAG, "Error generating auto post: " + e.getMessage());
                callback.onError(e.getMessage());
            }
        });
    }
    
    /**
     * Generate comment for posts
     */
    public void generateComment(String postContent, String authorName, String commentType, 
                              VercelCallback callback) {
        executorService.execute(() -> {
            try {
                JSONObject requestBody = new JSONObject();
                requestBody.put("postContent", postContent);
                requestBody.put("authorName", authorName);
                requestBody.put("commentType", commentType);
                requestBody.put("botUserId", SyraAIConfig.BOT_UID);
                
                String response = makeApiCall("/ai/comment-generation", requestBody);
                JSONObject responseJson = new JSONObject(response);
                
                if (responseJson.getBoolean("success")) {
                    String comment = responseJson.getString("comment");
                    callback.onSuccess(comment);
                } else {
                    callback.onError("API returned error");
                }
                
            } catch (Exception e) {
                Log.e(TAG, "Error generating comment: " + e.getMessage());
                callback.onError(e.getMessage());
            }
        });
    }
    
    /**
     * Send Firebase trigger webhook to Vercel
     */
    public void sendFirebaseTrigger(String eventType, JSONObject data) {
        executorService.execute(() -> {
            try {
                JSONObject requestBody = new JSONObject();
                requestBody.put("eventType", eventType);
                requestBody.put("data", data);
                
                JSONObject auth = new JSONObject();
                auth.put("token", getWebhookSecret()); // You'll need to store this securely
                requestBody.put("auth", auth);
                
                makeApiCall("/webhooks/firebase-trigger", requestBody);
                Log.d(TAG, "Firebase trigger sent: " + eventType);
                
            } catch (Exception e) {
                Log.e(TAG, "Error sending Firebase trigger: " + e.getMessage());
            }
        });
    }
    
    /**
     * Make HTTP API call to Vercel backend
     */
    private String makeApiCall(String endpoint, JSONObject requestBody) throws IOException, JSONException {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        try {
            // Configure connection
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(30000);
            
            // Send request body
            try (OutputStreamWriter writer = new OutputStreamWriter(
                    connection.getOutputStream(), StandardCharsets.UTF_8)) {
                writer.write(requestBody.toString());
                writer.flush();
            }
            
            // Read response
            int responseCode = connection.getResponseCode();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                responseCode >= 400 ? connection.getErrorStream() : connection.getInputStream(),
                StandardCharsets.UTF_8
            ));
            
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            
            if (responseCode >= 200 && responseCode < 300) {
                return response.toString();
            } else {
                throw new IOException("HTTP " + responseCode + ": " + response.toString());
            }
            
        } finally {
            connection.disconnect();
        }
    }
    
    /**
     * Get webhook secret for authentication
     * In production, store this securely (encrypted SharedPreferences, etc.)
     */
    private String getWebhookSecret() {
        // For demo purposes - in production, use secure storage
        return "your-webhook-secret-here";
    }
    
    /**
     * Shutdown the service
     */
    public void shutdown() {
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}