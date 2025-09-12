package com.synapse.social.studioasinc;

import android.util.Log;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.ServerTimestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * Helper class for Firestore operations with flattened database structure
 * This class provides methods to interact with Firestore collections in a flat structure
 * to improve performance and scalability
 */
public class FirestoreHelper {
    private static final String TAG = "FirestoreHelper";
    private static FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    
    // Collection names for flattened structure
    public static final String COLLECTION_USERS = "users";
    public static final String COLLECTION_POSTS = "posts";
    public static final String COLLECTION_FOLLOWERS = "followers";
    public static final String COLLECTION_FOLLOWING = "following";
    public static final String COLLECTION_POST_LIKES = "post_likes";
    public static final String COLLECTION_POST_COMMENTS = "post_comments";
    public static final String COLLECTION_PROFILE_LIKES = "profile_likes";
    public static final String COLLECTION_INBOX = "inbox";
    public static final String COLLECTION_FAVORITE_POSTS = "favorite_posts";
    
    /**
     * Get a reference to a specific collection
     */
    public static CollectionReference getCollection(String collectionName) {
        return firestore.collection(collectionName);
    }
    
    /**
     * Get a reference to a specific document
     */
    public static DocumentReference getDocument(String collectionName, String documentId) {
        return firestore.collection(collectionName).document(documentId);
    }
    
    /**
     * Create or update a user document
     */
    public static void createOrUpdateUser(String userId, Map<String, Object> userData) {
        getDocument(COLLECTION_USERS, userId)
            .set(userData)
            .addOnSuccessListener(aVoid -> Log.d(TAG, "User document written successfully"))
            .addOnFailureListener(e -> Log.e(TAG, "Error writing user document", e));
    }
    
    /**
     * Get user data by user ID
     */
    public static void getUser(String userId, FirestoreCallback<DocumentSnapshot> callback) {
        getDocument(COLLECTION_USERS, userId)
            .get()
            .addOnSuccessListener(callback::onSuccess)
            .addOnFailureListener(callback::onFailure);
    }
    
    /**
     * Create a new post
     */
    public static void createPost(String postId, Map<String, Object> postData) {
        getDocument(COLLECTION_POSTS, postId)
            .set(postData)
            .addOnSuccessListener(aVoid -> Log.d(TAG, "Post document written successfully"))
            .addOnFailureListener(e -> Log.e(TAG, "Error writing post document", e));
    }
    
    /**
     * Get posts by user ID
     */
    public static void getPostsByUser(String userId, FirestoreCallback<QuerySnapshot> callback) {
        getCollection(COLLECTION_POSTS)
            .whereEqualTo("uid", userId)
            .orderBy("publish_date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener(callback::onSuccess)
            .addOnFailureListener(callback::onFailure);
    }
    
    /**
     * Add a follower relationship
     */
    public static void addFollower(String userId, String followerId) {
        Map<String, Object> followerData = new HashMap<>();
        followerData.put("follower_id", followerId);
        followerData.put("followed_at", FieldValue.serverTimestamp());
        
        getDocument(COLLECTION_FOLLOWERS, userId + "_" + followerId)
            .set(followerData)
            .addOnSuccessListener(aVoid -> Log.d(TAG, "Follower relationship added"))
            .addOnFailureListener(e -> Log.e(TAG, "Error adding follower relationship", e));
    }
    
    /**
     * Remove a follower relationship
     */
    public static void removeFollower(String userId, String followerId) {
        getDocument(COLLECTION_FOLLOWERS, userId + "_" + followerId)
            .delete()
            .addOnSuccessListener(aVoid -> Log.d(TAG, "Follower relationship removed"))
            .addOnFailureListener(e -> Log.e(TAG, "Error removing follower relationship", e));
    }
    
    /**
     * Add a following relationship
     */
    public static void addFollowing(String userId, String followingId) {
        Map<String, Object> followingData = new HashMap<>();
        followingData.put("following_id", followingId);
        followingData.put("followed_at", FieldValue.serverTimestamp());
        
        getDocument(COLLECTION_FOLLOWING, userId + "_" + followingId)
            .set(followingData)
            .addOnSuccessListener(aVoid -> Log.d(TAG, "Following relationship added"))
            .addOnFailureListener(e -> Log.e(TAG, "Error adding following relationship", e));
    }
    
    /**
     * Remove a following relationship
     */
    public static void removeFollowing(String userId, String followingId) {
        getDocument(COLLECTION_FOLLOWING, userId + "_" + followingId)
            .delete()
            .addOnSuccessListener(aVoid -> Log.d(TAG, "Following relationship removed"))
            .addOnFailureListener(e -> Log.e(TAG, "Error removing following relationship", e));
    }
    
    /**
     * Get followers for a user
     */
    public static void getFollowers(String userId, FirestoreCallback<QuerySnapshot> callback) {
        getCollection(COLLECTION_FOLLOWERS)
            .whereEqualTo("user_id", userId)
            .get()
            .addOnSuccessListener(callback::onSuccess)
            .addOnFailureListener(callback::onFailure);
    }
    
    /**
     * Get following for a user
     */
    public static void getFollowing(String userId, FirestoreCallback<QuerySnapshot> callback) {
        getCollection(COLLECTION_FOLLOWING)
            .whereEqualTo("user_id", userId)
            .get()
            .addOnSuccessListener(callback::onSuccess)
            .addOnFailureListener(callback::onFailure);
    }
    
    /**
     * Like a post
     */
    public static void likePost(String postId, String userId) {
        Map<String, Object> likeData = new HashMap<>();
        likeData.put("user_id", userId);
        likeData.put("liked_at", FieldValue.serverTimestamp());
        
        getDocument(COLLECTION_POST_LIKES, postId + "_" + userId)
            .set(likeData)
            .addOnSuccessListener(aVoid -> Log.d(TAG, "Post liked successfully"))
            .addOnFailureListener(e -> Log.e(TAG, "Error liking post", e));
    }
    
    /**
     * Unlike a post
     */
    public static void unlikePost(String postId, String userId) {
        getDocument(COLLECTION_POST_LIKES, postId + "_" + userId)
            .delete()
            .addOnSuccessListener(aVoid -> Log.d(TAG, "Post unliked successfully"))
            .addOnFailureListener(e -> Log.e(TAG, "Error unliking post", e));
    }
    
    /**
     * Check if user liked a post
     */
    public static void checkPostLike(String postId, String userId, FirestoreCallback<DocumentSnapshot> callback) {
        getDocument(COLLECTION_POST_LIKES, postId + "_" + userId)
            .get()
            .addOnSuccessListener(callback::onSuccess)
            .addOnFailureListener(callback::onFailure);
    }
    
    /**
     * Get post likes count
     */
    public static void getPostLikesCount(String postId, FirestoreCallback<QuerySnapshot> callback) {
        getCollection(COLLECTION_POST_LIKES)
            .whereEqualTo("post_id", postId)
            .get()
            .addOnSuccessListener(callback::onSuccess)
            .addOnFailureListener(callback::onFailure);
    }
    
    /**
     * Add a comment to a post
     */
    public static void addComment(String postId, String commentId, Map<String, Object> commentData) {
        getDocument(COLLECTION_POST_COMMENTS, commentId)
            .set(commentData)
            .addOnSuccessListener(aVoid -> Log.d(TAG, "Comment added successfully"))
            .addOnFailureListener(e -> Log.e(TAG, "Error adding comment", e));
    }
    
    /**
     * Get comments for a post
     */
    public static void getPostComments(String postId, FirestoreCallback<QuerySnapshot> callback) {
        getCollection(COLLECTION_POST_COMMENTS)
            .whereEqualTo("post_id", postId)
            .orderBy("created_at", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener(callback::onSuccess)
            .addOnFailureListener(callback::onFailure);
    }
    
    /**
     * Like a profile
     */
    public static void likeProfile(String profileUserId, String likerUserId) {
        Map<String, Object> likeData = new HashMap<>();
        likeData.put("liker_id", likerUserId);
        likeData.put("liked_at", FieldValue.serverTimestamp());
        
        getDocument(COLLECTION_PROFILE_LIKES, profileUserId + "_" + likerUserId)
            .set(likeData)
            .addOnSuccessListener(aVoid -> Log.d(TAG, "Profile liked successfully"))
            .addOnFailureListener(e -> Log.e(TAG, "Error liking profile", e));
    }
    
    /**
     * Unlike a profile
     */
    public static void unlikeProfile(String profileUserId, String likerUserId) {
        getDocument(COLLECTION_PROFILE_LIKES, profileUserId + "_" + likerUserId)
            .delete()
            .addOnSuccessListener(aVoid -> Log.d(TAG, "Profile unliked successfully"))
            .addOnFailureListener(e -> Log.e(TAG, "Error unliking profile", e));
    }
    
    /**
     * Check if user liked a profile
     */
    public static void checkProfileLike(String profileUserId, String likerUserId, FirestoreCallback<DocumentSnapshot> callback) {
        getDocument(COLLECTION_PROFILE_LIKES, profileUserId + "_" + likerUserId)
            .get()
            .addOnSuccessListener(callback::onSuccess)
            .addOnFailureListener(callback::onFailure);
    }
    
    /**
     * Get profile likes count
     */
    public static void getProfileLikesCount(String profileUserId, FirestoreCallback<QuerySnapshot> callback) {
        getCollection(COLLECTION_PROFILE_LIKES)
            .whereEqualTo("profile_user_id", profileUserId)
            .get()
            .addOnSuccessListener(callback::onSuccess)
            .addOnFailureListener(callback::onFailure);
    }
    
    /**
     * Update inbox with last message
     */
    public static void updateInbox(String userId, String otherUserId, Map<String, Object> messageData) {
        getDocument(COLLECTION_INBOX, userId + "_" + otherUserId)
            .set(messageData)
            .addOnSuccessListener(aVoid -> Log.d(TAG, "Inbox updated successfully"))
            .addOnFailureListener(e -> Log.e(TAG, "Error updating inbox", e));
    }
    
    /**
     * Get inbox for a user
     */
    public static void getInbox(String userId, FirestoreCallback<QuerySnapshot> callback) {
        getCollection(COLLECTION_INBOX)
            .whereEqualTo("user_id", userId)
            .orderBy("last_message_time", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener(callback::onSuccess)
            .addOnFailureListener(callback::onFailure);
    }
    
    /**
     * Add post to favorites
     */
    public static void addToFavorites(String userId, String postId) {
        Map<String, Object> favoriteData = new HashMap<>();
        favoriteData.put("post_id", postId);
        favoriteData.put("added_at", FieldValue.serverTimestamp());
        
        getDocument(COLLECTION_FAVORITE_POSTS, userId + "_" + postId)
            .set(favoriteData)
            .addOnSuccessListener(aVoid -> Log.d(TAG, "Post added to favorites"))
            .addOnFailureListener(e -> Log.e(TAG, "Error adding post to favorites", e));
    }
    
    /**
     * Remove post from favorites
     */
    public static void removeFromFavorites(String userId, String postId) {
        getDocument(COLLECTION_FAVORITE_POSTS, userId + "_" + postId)
            .delete()
            .addOnSuccessListener(aVoid -> Log.d(TAG, "Post removed from favorites"))
            .addOnFailureListener(e -> Log.e(TAG, "Error removing post from favorites", e));
    }
    
    /**
     * Check if post is in favorites
     */
    public static void checkFavorite(String userId, String postId, FirestoreCallback<DocumentSnapshot> callback) {
        getDocument(COLLECTION_FAVORITE_POSTS, userId + "_" + postId)
            .get()
            .addOnSuccessListener(callback::onSuccess)
            .addOnFailureListener(callback::onFailure);
    }
    
    /**
     * Batch operations for better performance
     */
    public static WriteBatch createBatch() {
        return firestore.batch();
    }
    
    /**
     * Commit a batch operation
     */
    public static void commitBatch(WriteBatch batch, FirestoreCallback<Void> callback) {
        batch.commit()
            .addOnSuccessListener(callback::onSuccess)
            .addOnFailureListener(callback::onFailure);
    }
    
    /**
     * Generic callback interface for Firestore operations
     */
    public interface FirestoreCallback<T> {
        void onSuccess(T result);
        void onFailure(Exception e);
    }
}