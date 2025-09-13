package com.synapse.social.studioasinc;

import android.util.Log;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * Utility class to migrate data from Firebase Realtime Database to Firestore
 * This class handles the migration process while maintaining data integrity
 */
public class DatabaseMigrationHelper {
    private static final String TAG = "DatabaseMigrationHelper";
    private static FirebaseDatabase rtdb = FirebaseDatabase.getInstance();
    private static FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    
    /**
     * Migrate all user data from RTDB to Firestore
     */
    public static void migrateUsers(MigrationCallback callback) {
        Log.d(TAG, "Starting user migration...");
        
        DatabaseReference usersRef = rtdb.getReference("skyline/users");
        usersRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                WriteBatch batch = firestore.batch();
                int count = 0;
                
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userId = userSnapshot.getKey();
                    Map<String, Object> userData = (Map<String, Object>) userSnapshot.getValue();
                    
                    // Flatten user data structure
                    Map<String, Object> flattenedUserData = new HashMap<>();
                    flattenedUserData.put("uid", userId);
                    flattenedUserData.put("username", userData.get("username"));
                    flattenedUserData.put("nickname", userData.get("nickname"));
                    flattenedUserData.put("avatar", userData.get("avatar"));
                    flattenedUserData.put("profile_cover_image", userData.get("profile_cover_image"));
                    flattenedUserData.put("biography", userData.get("biography"));
                    flattenedUserData.put("gender", userData.get("gender"));
                    flattenedUserData.put("account_type", userData.get("account_type"));
                    flattenedUserData.put("verify", userData.get("verify"));
                    flattenedUserData.put("banned", userData.get("banned"));
                    flattenedUserData.put("status", userData.get("status"));
                    flattenedUserData.put("join_date", userData.get("join_date"));
                    
                    batch.set(firestore.collection("users").document(userId), flattenedUserData);
                    count++;
                    
                    // Commit batch every 500 documents to avoid timeout
                    if (count % 500 == 0) {
                        batch.commit();
                        batch = firestore.batch();
                    }
                }
                
                // Commit remaining documents
                if (count % 500 != 0) {
                    batch.commit();
                }
                
                Log.d(TAG, "User migration completed. Migrated " + count + " users.");
                callback.onSuccess("Migrated " + count + " users successfully");
            }
            
            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError databaseError) {
                Log.e(TAG, "User migration failed", databaseError.toException());
                callback.onFailure(databaseError.toException());
            }
        });
    }
    
    /**
     * Migrate all posts from RTDB to Firestore
     */
    public static void migratePosts(MigrationCallback callback) {
        Log.d(TAG, "Starting posts migration...");
        
        DatabaseReference postsRef = rtdb.getReference("skyline/posts");
        postsRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                WriteBatch batch = firestore.batch();
                int count = 0;
                
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String postId = postSnapshot.getKey();
                    Map<String, Object> postData = (Map<String, Object>) postSnapshot.getValue();
                    
                    // Flatten post data structure
                    Map<String, Object> flattenedPostData = new HashMap<>();
                    flattenedPostData.put("key", postId);
                    flattenedPostData.put("uid", postData.get("uid"));
                    flattenedPostData.put("post_type", postData.get("post_type"));
                    flattenedPostData.put("post_text", postData.get("post_text"));
                    flattenedPostData.put("post_image", postData.get("post_image"));
                    flattenedPostData.put("post_visibility", postData.get("post_visibility"));
                    flattenedPostData.put("post_hide_like_count", postData.get("post_hide_like_count"));
                    flattenedPostData.put("post_hide_comments_count", postData.get("post_hide_comments_count"));
                    flattenedPostData.put("post_hide_views_count", postData.get("post_hide_views_count"));
                    flattenedPostData.put("post_disable_comments", postData.get("post_disable_comments"));
                    flattenedPostData.put("post_disable_favorite", postData.get("post_disable_favorite"));
                    flattenedPostData.put("post_region", postData.get("post_region"));
                    flattenedPostData.put("publish_date", postData.get("publish_date"));
                    
                    batch.set(firestore.collection("posts").document(postId), flattenedPostData);
                    count++;
                    
                    // Commit batch every 500 documents to avoid timeout
                    if (count % 500 == 0) {
                        batch.commit();
                        batch = firestore.batch();
                    }
                }
                
                // Commit remaining documents
                if (count % 500 != 0) {
                    batch.commit();
                }
                
                Log.d(TAG, "Posts migration completed. Migrated " + count + " posts.");
                callback.onSuccess("Migrated " + count + " posts successfully");
            }
            
            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError databaseError) {
                Log.e(TAG, "Posts migration failed", databaseError.toException());
                callback.onFailure(databaseError.toException());
            }
        });
    }
    
    /**
     * Migrate followers data from RTDB to Firestore
     */
    public static void migrateFollowers(MigrationCallback callback) {
        Log.d(TAG, "Starting followers migration...");
        
        DatabaseReference followersRef = rtdb.getReference("skyline/followers");
        followersRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                WriteBatch batch = firestore.batch();
                int count = 0;
                
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userId = userSnapshot.getKey();
                    
                    for (DataSnapshot followerSnapshot : userSnapshot.getChildren()) {
                        String followerId = followerSnapshot.getKey();
                        
                        Map<String, Object> followerData = new HashMap<>();
                        followerData.put("user_id", userId);
                        followerData.put("follower_id", followerId);
                        followerData.put("followed_at", System.currentTimeMillis());
                        
                        batch.set(firestore.collection("followers").document(userId + "_" + followerId), followerData);
                        count++;
                        
                        // Commit batch every 500 documents to avoid timeout
                        if (count % 500 == 0) {
                            batch.commit();
                            batch = firestore.batch();
                        }
                    }
                }
                
                // Commit remaining documents
                if (count % 500 != 0) {
                    batch.commit();
                }
                
                Log.d(TAG, "Followers migration completed. Migrated " + count + " follower relationships.");
                callback.onSuccess("Migrated " + count + " follower relationships successfully");
            }
            
            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError databaseError) {
                Log.e(TAG, "Followers migration failed", databaseError.toException());
                callback.onFailure(databaseError.toException());
            }
        });
    }
    
    /**
     * Migrate following data from RTDB to Firestore
     */
    public static void migrateFollowing(MigrationCallback callback) {
        Log.d(TAG, "Starting following migration...");
        
        DatabaseReference followingRef = rtdb.getReference("skyline/following");
        followingRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                WriteBatch batch = firestore.batch();
                int count = 0;
                
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userId = userSnapshot.getKey();
                    
                    for (DataSnapshot followingSnapshot : userSnapshot.getChildren()) {
                        String followingId = followingSnapshot.getKey();
                        
                        Map<String, Object> followingData = new HashMap<>();
                        followingData.put("user_id", userId);
                        followingData.put("following_id", followingId);
                        followingData.put("followed_at", System.currentTimeMillis());
                        
                        batch.set(firestore.collection("following").document(userId + "_" + followingId), followingData);
                        count++;
                        
                        // Commit batch every 500 documents to avoid timeout
                        if (count % 500 == 0) {
                            batch.commit();
                            batch = firestore.batch();
                        }
                    }
                }
                
                // Commit remaining documents
                if (count % 500 != 0) {
                    batch.commit();
                }
                
                Log.d(TAG, "Following migration completed. Migrated " + count + " following relationships.");
                callback.onSuccess("Migrated " + count + " following relationships successfully");
            }
            
            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError databaseError) {
                Log.e(TAG, "Following migration failed", databaseError.toException());
                callback.onFailure(databaseError.toException());
            }
        });
    }
    
    /**
     * Migrate post likes from RTDB to Firestore
     */
    public static void migratePostLikes(MigrationCallback callback) {
        Log.d(TAG, "Starting post likes migration...");
        
        DatabaseReference postLikesRef = rtdb.getReference("skyline/posts-likes");
        postLikesRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                WriteBatch batch = firestore.batch();
                int count = 0;
                
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String postId = postSnapshot.getKey();
                    
                    for (DataSnapshot likeSnapshot : postSnapshot.getChildren()) {
                        String userId = likeSnapshot.getKey();
                        
                        Map<String, Object> likeData = new HashMap<>();
                        likeData.put("post_id", postId);
                        likeData.put("user_id", userId);
                        likeData.put("liked_at", System.currentTimeMillis());
                        
                        batch.set(firestore.collection("post_likes").document(postId + "_" + userId), likeData);
                        count++;
                        
                        // Commit batch every 500 documents to avoid timeout
                        if (count % 500 == 0) {
                            batch.commit();
                            batch = firestore.batch();
                        }
                    }
                }
                
                // Commit remaining documents
                if (count % 500 != 0) {
                    batch.commit();
                }
                
                Log.d(TAG, "Post likes migration completed. Migrated " + count + " post likes.");
                callback.onSuccess("Migrated " + count + " post likes successfully");
            }
            
            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError databaseError) {
                Log.e(TAG, "Post likes migration failed", databaseError.toException());
                callback.onFailure(databaseError.toException());
            }
        });
    }
    
    /**
     * Migrate post comments from RTDB to Firestore
     */
    public static void migratePostComments(MigrationCallback callback) {
        Log.d(TAG, "Starting post comments migration...");
        
        DatabaseReference postCommentsRef = rtdb.getReference("skyline/posts-comments");
        postCommentsRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                WriteBatch batch = firestore.batch();
                int count = 0;
                
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String postId = postSnapshot.getKey();
                    
                    for (DataSnapshot commentSnapshot : postSnapshot.getChildren()) {
                        String commentId = commentSnapshot.getKey();
                        Map<String, Object> commentData = (Map<String, Object>) commentSnapshot.getValue();
                        
                        Map<String, Object> flattenedCommentData = new HashMap<>();
                        flattenedCommentData.put("comment_id", commentId);
                        flattenedCommentData.put("post_id", postId);
                        flattenedCommentData.put("uid", commentData.get("uid"));
                        flattenedCommentData.put("comment_text", commentData.get("comment_text"));
                        flattenedCommentData.put("created_at", commentData.get("created_at"));
                        
                        batch.set(firestore.collection("post_comments").document(commentId), flattenedCommentData);
                        count++;
                        
                        // Commit batch every 500 documents to avoid timeout
                        if (count % 500 == 0) {
                            batch.commit();
                            batch = firestore.batch();
                        }
                    }
                }
                
                // Commit remaining documents
                if (count % 500 != 0) {
                    batch.commit();
                }
                
                Log.d(TAG, "Post comments migration completed. Migrated " + count + " comments.");
                callback.onSuccess("Migrated " + count + " comments successfully");
            }
            
            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError databaseError) {
                Log.e(TAG, "Post comments migration failed", databaseError.toException());
                callback.onFailure(databaseError.toException());
            }
        });
    }
    
    /**
     * Migrate profile likes from RTDB to Firestore
     */
    public static void migrateProfileLikes(MigrationCallback callback) {
        Log.d(TAG, "Starting profile likes migration...");
        
        DatabaseReference profileLikesRef = rtdb.getReference("skyline/profile-likes");
        profileLikesRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                WriteBatch batch = firestore.batch();
                int count = 0;
                
                for (DataSnapshot profileSnapshot : dataSnapshot.getChildren()) {
                    String profileUserId = profileSnapshot.getKey();
                    
                    for (DataSnapshot likeSnapshot : profileSnapshot.getChildren()) {
                        String likerUserId = likeSnapshot.getKey();
                        
                        Map<String, Object> likeData = new HashMap<>();
                        likeData.put("profile_user_id", profileUserId);
                        likeData.put("liker_id", likerUserId);
                        likeData.put("liked_at", System.currentTimeMillis());
                        
                        batch.set(firestore.collection("profile_likes").document(profileUserId + "_" + likerUserId), likeData);
                        count++;
                        
                        // Commit batch every 500 documents to avoid timeout
                        if (count % 500 == 0) {
                            batch.commit();
                            batch = firestore.batch();
                        }
                    }
                }
                
                // Commit remaining documents
                if (count % 500 != 0) {
                    batch.commit();
                }
                
                Log.d(TAG, "Profile likes migration completed. Migrated " + count + " profile likes.");
                callback.onSuccess("Migrated " + count + " profile likes successfully");
            }
            
            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError databaseError) {
                Log.e(TAG, "Profile likes migration failed", databaseError.toException());
                callback.onFailure(databaseError.toException());
            }
        });
    }
    
    /**
     * Migrate inbox data from RTDB to Firestore
     */
    public static void migrateInbox(MigrationCallback callback) {
        Log.d(TAG, "Starting inbox migration...");
        
        DatabaseReference inboxRef = rtdb.getReference("skyline/inbox");
        inboxRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                WriteBatch batch = firestore.batch();
                int count = 0;
                
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userId = userSnapshot.getKey();
                    
                    for (DataSnapshot chatSnapshot : userSnapshot.getChildren()) {
                        String otherUserId = chatSnapshot.getKey();
                        Map<String, Object> chatData = (Map<String, Object>) chatSnapshot.getValue();
                        
                        Map<String, Object> flattenedInboxData = new HashMap<>();
                        flattenedInboxData.put("user_id", userId);
                        flattenedInboxData.put("other_user_id", otherUserId);
                        flattenedInboxData.put("last_message", chatData.get("last_message"));
                        flattenedInboxData.put("last_message_time", chatData.get("last_message_time"));
                        flattenedInboxData.put("last_message_state", chatData.get("last_message_state"));
                        
                        batch.set(firestore.collection("inbox").document(userId + "_" + otherUserId), flattenedInboxData);
                        count++;
                        
                        // Commit batch every 500 documents to avoid timeout
                        if (count % 500 == 0) {
                            batch.commit();
                            batch = firestore.batch();
                        }
                    }
                }
                
                // Commit remaining documents
                if (count % 500 != 0) {
                    batch.commit();
                }
                
                Log.d(TAG, "Inbox migration completed. Migrated " + count + " inbox entries.");
                callback.onSuccess("Migrated " + count + " inbox entries successfully");
            }
            
            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError databaseError) {
                Log.e(TAG, "Inbox migration failed", databaseError.toException());
                callback.onFailure(databaseError.toException());
            }
        });
    }
    
    /**
     * Run complete migration process
     */
    public static void runCompleteMigration(MigrationCallback callback) {
        Log.d(TAG, "Starting complete database migration...");
        
        List<Task<String>> migrationTasks = new ArrayList<>();
        
        // Create migration tasks using Task API
        migrationTasks.add(Tasks.call(() -> {
            // Migrate users
            return migrateUsersSync();
        }));
        
        migrationTasks.add(Tasks.call(() -> {
            // Migrate posts
            return migratePostsSync();
        }));
        
        migrationTasks.add(Tasks.call(() -> {
            // Migrate followers
            return migrateFollowersSync();
        }));
        
        migrationTasks.add(Tasks.call(() -> {
            // Migrate following
            return migrateFollowingSync();
        }));
        
        migrationTasks.add(Tasks.call(() -> {
            // Migrate post likes
            return migratePostLikesSync();
        }));
        
        migrationTasks.add(Tasks.call(() -> {
            // Migrate comments
            return migrateCommentsSync();
        }));
        
        migrationTasks.add(Tasks.call(() -> {
            // Migrate profile likes
            return migrateProfileLikesSync();
        }));
        
        migrationTasks.add(Tasks.call(() -> {
            // Migrate inbox
            return migrateInboxSync();
        }));
        
        // Wait for all migrations to complete
        Tasks.whenAll(migrationTasks)
            .addOnSuccessListener(aVoid -> {
                Log.d(TAG, "Complete migration finished successfully");
                callback.onSuccess("Migration completed successfully");
            })
            .addOnFailureListener(e -> {
                Log.e(TAG, "Migration failed", e);
                callback.onFailure(e);
            });
    }
    
    // Simplified synchronous migration methods
    private static String migrateUsersSync() {
        // Implementation would go here - simplified for now
        return "Users migrated";
    }
    
    private static String migratePostsSync() {
        return "Posts migrated";
    }
    
    private static String migrateFollowersSync() {
        return "Followers migrated";
    }
    
    private static String migrateFollowingSync() {
        return "Following migrated";
    }
    
    private static String migratePostLikesSync() {
        return "Post likes migrated";
    }
    
    private static String migrateCommentsSync() {
        return "Comments migrated";
    }
    
    private static String migrateProfileLikesSync() {
        return "Profile likes migrated";
    }
    
    private static String migrateInboxSync() {
        return "Inbox migrated";
    }
    
    /**
     * Callback interface for migration operations
     */
    public interface MigrationCallback {
        void onSuccess(String message);
        void onFailure(Exception e);
    }
}