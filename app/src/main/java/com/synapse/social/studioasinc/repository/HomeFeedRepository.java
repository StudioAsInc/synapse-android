package com.synapse.social.studioasinc.repository;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.synapse.social.studioasinc.adapter.PublicPostsListAdapter;
import com.synapse.social.studioasinc.model.Post;
import com.synapse.social.studioasinc.model.Story;

import java.util.ArrayList;
import java.util.List;

public class HomeFeedRepository {

    private final DatabaseReference postsRef;
    private final DatabaseReference storiesRef;
    private final DatabaseReference followingRef;
    private final DatabaseReference favoritePostsRef;
    private final DatabaseReference usersRef;
    private final String currentUserId;

    public HomeFeedRepository() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        postsRef = firebaseDatabase.getReference("skyline/posts");
        storiesRef = firebaseDatabase.getReference("skyline/stories");
        followingRef = firebaseDatabase.getReference("skyline/following");
        favoritePostsRef = firebaseDatabase.getReference("skyline/favorite-posts");
        usersRef = firebaseDatabase.getReference("skyline/users");
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void getHomeFeed(String filterType, OnHomeFeedLoadedListener listener) {
        List<Object> items = new ArrayList<>();
        items.add(new PublicPostsListAdapter.PostComposer());
        items.add(new PublicPostsListAdapter.Filter());

        storiesRef.orderByChild("publish_date").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<Story> stories = new ArrayList<>();
                    for (DataSnapshot storySnap : dataSnapshot.getChildren()) {
                        Story story = storySnap.getValue(Story.class);
                        stories.add(story);
                    }
                    items.add(new PublicPostsListAdapter.Stories(stories));
                }

                Query query;
                switch (filterType) {
                    case "LOCAL":
                        usersRef.child(currentUserId).child("user_region").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String userRegion = dataSnapshot.getValue(String.class);
                                    postsRef.orderByChild("post_region").equalTo(userRegion).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                for (DataSnapshot postSnap : dataSnapshot.getChildren()) {
                                                    Post post = postSnap.getValue(Post.class);
                                                    items.add(post);
                                                }
                                            }
                                            listener.onHomeFeedLoaded(items);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            listener.onError(databaseError.getMessage());
                                        }
                                    });
                                } else {
                                    listener.onHomeFeedLoaded(items);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                listener.onError(databaseError.getMessage());
                            }
                        });
                        break;
                    case "FOLLOWED":
                        followingRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                List<String> followedUids = new ArrayList<>();
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot followSnap : dataSnapshot.getChildren()) {
                                        followedUids.add(followSnap.getKey());
                                    }
                                }

                                if (followedUids.isEmpty()) {
                                    listener.onHomeFeedLoaded(items);
                                    return;
                                }

                                List<Task<DataSnapshot>> tasks = new ArrayList<>();
                                for (String uid : followedUids) {
                                    tasks.add(postsRef.orderByChild("uid").equalTo(uid).get());
                                }

                                Tasks.whenAllSuccess(tasks).addOnSuccessListener(snapshots -> {
                                    for (Object snapshot : snapshots) {
                                        for (DataSnapshot postSnap : ((DataSnapshot) snapshot).getChildren()) {
                                            Post post = postSnap.getValue(Post.class);
                                            items.add(post);
                                        }
                                    }
                                    listener.onHomeFeedLoaded(items);
                                }).addOnFailureListener(e -> listener.onError(e.getMessage()));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                listener.onError(databaseError.getMessage());
                            }
                        });
                        break;
                    case "FAVORITE":
                        favoritePostsRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                List<String> favoritePostKeys = new ArrayList<>();
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot favSnap : dataSnapshot.getChildren()) {
                                        favoritePostKeys.add(favSnap.getKey());
                                    }
                                }

                                if (favoritePostKeys.isEmpty()) {
                                    listener.onHomeFeedLoaded(items);
                                    return;
                                }

                                List<Task<DataSnapshot>> tasks = new ArrayList<>();
                                for (String key : favoritePostKeys) {
                                    tasks.add(postsRef.child(key).get());
                                }

                                Tasks.whenAllSuccess(tasks).addOnSuccessListener(snapshots -> {
                                    for (Object snapshot : snapshots) {
                                        Post post = ((DataSnapshot) snapshot).getValue(Post.class);
                                        items.add(post);
                                    }
                                    listener.onHomeFeedLoaded(items);
                                }).addOnFailureListener(e -> listener.onError(e.getMessage()));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                listener.onError(databaseError.getMessage());
                            }
                        });
                        break;
                    case "PUBLIC":
                    default:
                        query = postsRef.orderByChild("publish_date");
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot postSnap : dataSnapshot.getChildren()) {
                                        Post post = postSnap.getValue(Post.class);
                                        items.add(post);
                                    }
                                }
                                listener.onHomeFeedLoaded(items);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                listener.onError(databaseError.getMessage());
                            }
                        });
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }

    public interface OnHomeFeedLoadedListener {
        void onHomeFeedLoaded(List<Object> items);
        void onError(String errorMessage);
    }

    public void createPost(String postText, OnPostCreatedListener listener) {
        String key = postsRef.push().getKey();
        Post post = new Post();
        post.setKey(key);
        post.setUid(currentUserId);
        post.setPost_text(postText);
        post.setPost_type("TEXT");
        post.setPublish_date(String.valueOf(System.currentTimeMillis()));
        postsRef.child(key).setValue(post)
                .addOnSuccessListener(aVoid -> listener.onPostCreated())
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }

    public interface OnPostCreatedListener {
        void onPostCreated();
        void onError(String errorMessage);
    }

    public void likePost(Post post, OnPostActionListener listener) {
        DatabaseReference likeRef = postsRef.child(post.getKey()).child("likes").child(currentUserId);
        likeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    likeRef.removeValue()
                            .addOnSuccessListener(aVoid -> listener.onSuccess())
                            .addOnFailureListener(e -> listener.onError(e.getMessage()));
                } else {
                    likeRef.setValue(true)
                            .addOnSuccessListener(aVoid -> listener.onSuccess())
                            .addOnFailureListener(e -> listener.onError(e.getMessage()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }

    public void commentOnPost(Post post, String comment, OnPostActionListener listener) {
        String commentId = postsRef.child(post.getKey()).child("comments").push().getKey();
        Comment newComment = new Comment(currentUserId, comment, System.currentTimeMillis());
        postsRef.child(post.getKey()).child("comments").child(commentId).setValue(newComment)
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }

    public void sharePost(Post post, OnPostActionListener listener) {
        // The original implementation was not complete.
        // For now, we will just simulate a success event.
        listener.onSuccess();
    }

    public interface OnPostActionListener {
        void onSuccess();
        void onError(String errorMessage);
    }
}
