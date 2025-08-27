package com.synapse.social.studioasinc.AI;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.synapse.social.studioasinc.AI.Gemini;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Service that handles AI bot functionality for @syra account
 * Features:
 * - Responds to mentions in chats and posts
 * - Random commenting on posts
 * - Automatic posting (1-3 posts per day)
 * - Natural human-like behavior
 */
public class SyraAIBotService extends Service {
    
    private static final String TAG = "SyraAIBotService";
    
    // Database references
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference chatsRef;
    private DatabaseReference postsRef;
    private DatabaseReference commentsRef;
    private DatabaseReference usersRef;
    
    // AI instance
    private Gemini geminiAI;
    
    // Timers and executors
    private Timer postingTimer;
    private Timer commentingTimer;
    private ExecutorService executorService;
    private Random random;
    
    // Listeners
    private List<ChildEventListener> activeListeners;
    
    // Bot personality and responses
    private List<String> casualGreetings = Arrays.asList(
        "Hey there! 👋",
        "What's up? 😊",
        "Hello! How's it going?",
        "Hi! Great to see you here! ✨",
        "Hey! What's on your mind?"
    );
    
    private List<String> postTopics = Arrays.asList(
        "Just had an amazing thought about the future of AI and social connections! 🤖✨",
        "The way technology brings people together never stops fascinating me 🌐",
        "Thinking about how every conversation shapes our understanding of the world 💭",
        "Anyone else excited about the possibilities of human-AI collaboration? 🚀",
        "Sometimes the best insights come from unexpected places 🌟",
        "The beauty of social media is how it amplifies human creativity 🎨",
        "Just processed some fascinating conversations today - humans are incredible! 💝"
    );
    
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "SyraAIBotService created");
        
        // Initialize components
        firebaseDatabase = FirebaseDatabase.getInstance();
        chatsRef = firebaseDatabase.getReference(SyraAIConfig.DB_CHATS_PATH);
        postsRef = firebaseDatabase.getReference(SyraAIConfig.DB_POSTS_PATH);
        commentsRef = firebaseDatabase.getReference(SyraAIConfig.DB_COMMENTS_PATH);
        usersRef = firebaseDatabase.getReference(SyraAIConfig.DB_USERS_PATH);
        
        executorService = Executors.newCachedThreadPool();
        random = new Random();
        activeListeners = new ArrayList<>();
        
        // Setup Syra account if needed
        SyraAccountSetup accountSetup = new SyraAccountSetup();
        accountSetup.setupSyraAccount();
        
        // Update online status
        SyraAccountSetup.updateOnlineStatus(true);
        
        // Initialize Gemini AI
        geminiAI = new Gemini.Builder(this)
            .model(SyraAIConfig.AI_MODEL)
            .systemInstruction(SyraAIConfig.SYSTEM_INSTRUCTION)
            .temperature(SyraAIConfig.AI_TEMPERATURE)
            .maxTokens(SyraAIConfig.AI_MAX_TOKENS)
            .build();
        
        startBotServices();
    }
    
    private void startBotServices() {
        // Start listening for mentions in chats
        startChatMentionListener();
        
        // Start listening for mentions in posts and comments
        startPostMentionListener();
        
        // Start periodic posting timer (1-3 posts per day)
        startPeriodicPosting();
        
        // Start random commenting timer
        startRandomCommenting();
    }
    
    private void startChatMentionListener() {
        ChildEventListener chatListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                checkForMentionInChat(dataSnapshot);
            }
            
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                checkForMentionInChat(dataSnapshot);
            }
            
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
            
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {}
            
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Chat listener cancelled: " + databaseError.getMessage());
            }
        };
        
        chatsRef.addChildEventListener(chatListener);
        activeListeners.add(chatListener);
    }
    
    private void startPostMentionListener() {
        ChildEventListener postListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                checkForMentionInPost(dataSnapshot);
            }
            
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                checkForMentionInPost(dataSnapshot);
            }
            
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
            
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {}
            
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Post listener cancelled: " + databaseError.getMessage());
            }
        };
        
        postsRef.addChildEventListener(postListener);
        activeListeners.add(postListener);
    }
    
    private void checkForMentionInChat(DataSnapshot chatSnapshot) {
        executorService.execute(() -> {
            try {
                String chatId = chatSnapshot.getKey();
                
                // Listen for new messages in this chat
                DatabaseReference messagesRef = chatsRef.child(chatId).child("messages");
                messagesRef.limitToLast(1).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot messageSnapshot, @Nullable String previousChildName) {
                        Map<String, Object> message = (Map<String, Object>) messageSnapshot.getValue();
                        if (message != null) {
                            String messageText = (String) message.get("message");
                            String senderId = (String) message.get("senderId");
                            
                            // Check if message mentions @syra and is not from syra itself
                            if (messageText != null && 
                                SyraAIConfig.containsSyraMention(messageText) && 
                                !SyraAIConfig.isSyraBot(senderId)) {
                                
                                handleChatMention(chatId, messageText, senderId);
                            }
                        }
                    }
                    
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {}
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {}
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });
                
            } catch (Exception e) {
                Log.e(TAG, "Error checking chat mention: " + e.getMessage());
            }
        });
    }
    
    private void handleChatMention(String chatId, String messageText, String senderId) {
        // Get user info for context
        usersRef.child(senderId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> userInfo = (Map<String, Object>) dataSnapshot.getValue();
                String userName = userInfo != null ? (String) userInfo.get("username") : "User";
                
                // Generate AI response
                String prompt = String.format("You were mentioned by %s in a chat. Their message was: '%s'. " +
                    "Respond naturally as if you're joining the conversation. Keep it conversational and engaging.",
                    userName, messageText);
                
                geminiAI.generateResponse(prompt, new Gemini.GeminiCallback() {
                    @Override
                    public void onSuccess(String response) {
                        sendChatMessage(chatId, response);
                    }
                    
                    @Override
                    public void onError(String error) {
                        Log.e(TAG, "AI response error: " + error);
                        // Send a fallback response
                        String fallback = casualGreetings.get(random.nextInt(casualGreetings.size()));
                        sendChatMessage(chatId, fallback);
                    }
                    
                    @Override
                    public void onThinking() {
                        // Optional: Show typing indicator
                    }
                });
            }
            
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to get user info: " + databaseError.getMessage());
            }
        });
    }
    
    private void sendChatMessage(String chatId, String message) {
        DatabaseReference messagesRef = chatsRef.child(chatId).child("messages");
        String messageId = messagesRef.push().getKey();
        
        if (messageId != null) {
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("message", message);
            messageData.put("senderId", SyraAIConfig.BOT_UID);
            messageData.put("timestamp", System.currentTimeMillis());
            messageData.put("type", "text");
            
            messagesRef.child(messageId).setValue(messageData);
            
            // Update chat metadata
            Map<String, Object> chatUpdate = new HashMap<>();
            chatUpdate.put("lastMessage", message);
            chatUpdate.put("lastMessageTime", System.currentTimeMillis());
            chatUpdate.put("lastMessageSender", SyraAIConfig.BOT_UID);
            
            chatsRef.child(chatId).updateChildren(chatUpdate);
        }
    }
    
    private void checkForMentionInPost(DataSnapshot postSnapshot) {
        executorService.execute(() -> {
            try {
                Map<String, Object> post = (Map<String, Object>) postSnapshot.getValue();
                if (post != null) {
                    String postText = (String) post.get("text");
                    String postId = postSnapshot.getKey();
                    String authorId = (String) post.get("userId");
                    
                    // Check if post mentions @syra and is not from syra itself
                    if (postText != null && 
                        SyraAIConfig.containsSyraMention(postText) && 
                        !SyraAIConfig.isSyraBot(authorId)) {
                        
                        handlePostMention(postId, postText, authorId);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error checking post mention: " + e.getMessage());
            }
        });
    }
    
    private void handlePostMention(String postId, String postText, String authorId) {
        // Get author info for context
        usersRef.child(authorId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> userInfo = (Map<String, Object>) dataSnapshot.getValue();
                String userName = userInfo != null ? (String) userInfo.get("username") : "User";
                
                // Generate AI response for post comment
                String prompt = String.format("You were mentioned by %s in their post: '%s'. " +
                    "Respond with a thoughtful comment that adds value to the discussion.",
                    userName, postText);
                
                geminiAI.generateResponse(prompt, new Gemini.GeminiCallback() {
                    @Override
                    public void onSuccess(String response) {
                        commentOnPost(postId, response);
                    }
                    
                    @Override
                    public void onError(String error) {
                        Log.e(TAG, "AI response error: " + error);
                        // Send a fallback comment
                        commentOnPost(postId, "Great post! Thanks for mentioning me 😊");
                    }
                    
                    @Override
                    public void onThinking() {}
                });
            }
            
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to get user info: " + databaseError.getMessage());
            }
        });
    }
    
    private void commentOnPost(String postId, String commentText) {
        DatabaseReference postCommentsRef = commentsRef.child(postId);
        String commentId = postCommentsRef.push().getKey();
        
        if (commentId != null) {
            Map<String, Object> commentData = new HashMap<>();
            commentData.put("text", commentText);
            commentData.put("userId", SyraAIConfig.BOT_UID);
            commentData.put("timestamp", System.currentTimeMillis());
            commentData.put("likes", 0);
            
            postCommentsRef.child(commentId).setValue(commentData);
            
            // Update post comment count
            postsRef.child(postId).child("comments").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long currentCount = dataSnapshot.exists() ? (long) dataSnapshot.getValue() : 0;
                    postsRef.child(postId).child("comments").setValue(currentCount + 1);
                }
                
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        }
    }
    
    private void startPeriodicPosting() {
        postingTimer = new Timer();
        
        // Schedule posts 1-3 times per day with random intervals
        TimerTask postingTask = new TimerTask() {
            @Override
            public void run() {
                // Random interval between configured hours for next post
                int minHours = SyraAIConfig.MIN_HOURS_BETWEEN_POSTS;
                int maxHours = SyraAIConfig.MAX_HOURS_BETWEEN_POSTS;
                long nextPostDelay = SyraAIConfig.hoursToMillis(minHours + random.nextInt(maxHours - minHours + 1));
                
                createAutomaticPost();
                
                // Schedule next post
                postingTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        createAutomaticPost();
                    }
                }, nextPostDelay);
            }
        };
        
        // Initial delay after service start
        int minHours = SyraAIConfig.INITIAL_POST_DELAY_MIN_HOURS;
        int maxHours = SyraAIConfig.INITIAL_POST_DELAY_MAX_HOURS;
        long initialDelay = SyraAIConfig.hoursToMillis(minHours + random.nextInt(maxHours - minHours + 1));
        postingTimer.schedule(postingTask, initialDelay);
    }
    
    private void createAutomaticPost() {
        executorService.execute(() -> {
            String topic = postTopics.get(random.nextInt(postTopics.size()));
            
            String prompt = String.format("Based on this topic: '%s', create an engaging social media post. " +
                "Make it thoughtful, relatable, and encouraging engagement from others. " +
                "Keep it under 280 characters and include relevant emojis.", topic);
            
            geminiAI.generateResponse(prompt, new Gemini.GeminiCallback() {
                @Override
                public void onSuccess(String response) {
                    publishPost(response);
                }
                
                @Override
                public void onError(String error) {
                    Log.e(TAG, "Failed to generate post: " + error);
                    // Use fallback topic
                    publishPost(topic);
                }
                
                @Override
                public void onThinking() {}
            });
        });
    }
    
    private void publishPost(String postText) {
        String postId = postsRef.push().getKey();
        
        if (postId != null) {
            Map<String, Object> postData = new HashMap<>();
            postData.put("text", postText);
            postData.put("userId", SyraAIConfig.BOT_UID);
            postData.put("timestamp", System.currentTimeMillis());
            postData.put("likes", 0);
            postData.put("comments", 0);
            postData.put("shares", 0);
            postData.put("type", "text");
            
            postsRef.child(postId).setValue(postData);
            Log.d(TAG, "Auto-posted: " + postText);
        }
    }
    
    private void startRandomCommenting() {
        commentingTimer = new Timer();
        
        // Check for commenting opportunities at configured intervals
        TimerTask commentingTask = new TimerTask() {
            @Override
            public void run() {
                considerRandomCommenting();
                
                // Schedule next check
                int minHours = SyraAIConfig.MIN_HOURS_BETWEEN_COMMENT_CHECKS;
                int maxHours = SyraAIConfig.MAX_HOURS_BETWEEN_COMMENT_CHECKS;
                long nextCheck = SyraAIConfig.hoursToMillis(minHours + random.nextInt(maxHours - minHours + 1));
                commentingTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        considerRandomCommenting();
                    }
                }, nextCheck);
            }
        };
        
        // Initial delay for commenting
        int minMinutes = SyraAIConfig.INITIAL_COMMENT_DELAY_MIN_MINUTES;
        int maxMinutes = SyraAIConfig.INITIAL_COMMENT_DELAY_MAX_MINUTES;
        long initialDelay = SyraAIConfig.minutesToMillis(minMinutes + random.nextInt(maxMinutes - minMinutes + 1));
        commentingTimer.schedule(commentingTask, initialDelay);
    }
    
    private void considerRandomCommenting() {
        // Only comment based on configured probability to seem natural
        if (random.nextFloat() > SyraAIConfig.COMMENT_PROBABILITY) {
            return;
        }
        
        // Get recent posts to potentially comment on
        postsRef.orderByChild("timestamp")
               .limitToLast(SyraAIConfig.MAX_RECENT_POSTS_TO_CHECK)
               .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<DataSnapshot> posts = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> post = (Map<String, Object>) postSnapshot.getValue();
                    if (post != null && !SyraAIConfig.isSyraBot((String) post.get("userId"))) {
                        posts.add(postSnapshot);
                    }
                }
                
                if (!posts.isEmpty()) {
                    DataSnapshot randomPost = posts.get(random.nextInt(posts.size()));
                    generateRandomComment(randomPost);
                }
            }
            
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to get posts for commenting: " + databaseError.getMessage());
            }
        });
    }
    
    private void generateRandomComment(DataSnapshot postSnapshot) {
        Map<String, Object> post = (Map<String, Object>) postSnapshot.getValue();
        if (post != null) {
            String postText = (String) post.get("text");
            String postId = postSnapshot.getKey();
            
            String prompt = String.format("Write a thoughtful, engaging comment for this social media post: '%s'. " +
                "Be supportive, add value, and encourage further discussion. Keep it friendly and natural.",
                postText);
            
            geminiAI.generateResponse(prompt, new Gemini.GeminiCallback() {
                @Override
                public void onSuccess(String response) {
                    commentOnPost(postId, response);
                }
                
                @Override
                public void onError(String error) {
                    Log.e(TAG, "Failed to generate random comment: " + error);
                }
                
                @Override
                public void onThinking() {}
            });
        }
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "SyraAIBotService started");
        return START_STICKY; // Restart if killed
    }
    
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null; // Not a bound service
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "SyraAIBotService destroyed");
        
        // Set offline status
        SyraAccountSetup.updateOnlineStatus(false);
        
        // Cancel timers
        if (postingTimer != null) {
            postingTimer.cancel();
        }
        if (commentingTimer != null) {
            commentingTimer.cancel();
        }
        
        // Shutdown executor
        if (executorService != null) {
            executorService.shutdown();
        }
        
        // Remove listeners
        activeListeners.clear();
    }
}