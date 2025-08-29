package com.synapse.social.studioasinc.fragments;

import android.Manifest;
import android.animation.*;
import android.app.*;
import android.content.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.*;
import android.graphics.*;
import android.graphics.Typeface;
import android.graphics.drawable.*;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.*;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.*;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.*;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.bumptech.glide.*;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.*;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import androidx.browser.customtabs.CustomTabsIntent;
import com.google.firebase.database.Query;
import com.synapse.social.studioasinc.CreateLineVideoActivity;
import com.synapse.social.studioasinc.CreatePostActivity;
import com.synapse.social.studioasinc.PostCommentsBottomSheetDialog;
import com.synapse.social.studioasinc.PostMoreBottomSheetDialog;
import com.synapse.social.studioasinc.ProfileActivity;
import com.synapse.social.studioasinc.R;
import com.synapse.social.studioasinc.SynapseApp;
import com.synapse.social.studioasinc.adapter.PublicPostsListAdapter;


import java.io.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.os.Handler;
import android.os.Looper;

public class HomeFragment extends Fragment implements PublicPostsListAdapter.HeaderAdapterListener {

    private FirebaseDatabase _firebase;
    private DatabaseReference udb;
    private DatabaseReference postsRef;
    private DatabaseReference storiesDbRef;

    private HashMap<String, Object> createPostMap = new HashMap<>();
    private String currentPostFilter = "PUBLIC";

    private ArrayList<HashMap<String, Object>> PostsList = new ArrayList<>();

    private LinearLayout loadingBody;
    private SwipeRefreshLayout swipeLayout;
    private RecyclerView PublicPostsList;
    private TextView PublicPostsListNotFound;
	private ProgressBar loading_bar;

    private Intent intent = new Intent();
    private Vibrator vbr;
    private FirebaseAuth auth;
    private Calendar cc = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _firebase = FirebaseDatabase.getInstance();
        udb = _firebase.getReference("skyline/users");
        postsRef = _firebase.getReference("skyline/posts");
        storiesDbRef = _firebase.getReference("skyline/stories");
        postsRef.keepSynced(true);
        storiesDbRef.keepSynced(true);

        initialize(view);
        initializeLogic();
    }

    private void initialize(View view) {
        PublicPostsList = view.findViewById(R.id.PublicPostsList);
        PublicPostsListNotFound = view.findViewById(R.id.PublicPostsListNotFound);
		loading_bar = view.findViewById(R.id.loading_bar);
        swipeLayout = view.findViewById(R.id.swipeLayout);
        loadingBody = view.findViewById(R.id.loadingBody);

        vbr = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        auth = FirebaseAuth.getInstance();

        swipeLayout.setOnRefreshListener(() -> {
            _loadPosts(currentPostFilter);
        });
    }

    private void initializeLogic() {
        _loadPosts(currentPostFilter);
        PublicPostsList.setLayoutManager(new LinearLayoutManager(getContext()));
        PublicPostsList.setAdapter(new PublicPostsListAdapter(new ArrayList<>(), this, getContext()));
    }

	@Override
	public void onImagePostClick() {
		intent.setClass(getContext(), CreatePostActivity.class);
		startActivity(intent);
	}

	@Override
	public void onVideoPostClick() {
		intent.setClass(getContext(), CreateLineVideoActivity.class);
		startActivity(intent);
	}

	@Override
	public void onTextPostClick(String text) {
		if (text.trim().equals("")) {
			Toast.makeText(getContext(), getResources().getString(R.string.please_enter_text), Toast.LENGTH_SHORT).show();
		} else {
			if (!(text.length() > 1500)) {
				String uniqueKey = udb.push().getKey();
				cc = Calendar.getInstance();
				createPostMap = new HashMap<>();
				createPostMap.put("key", uniqueKey);
				createPostMap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
				createPostMap.put("post_text", text.trim());
				createPostMap.put("post_type", "TEXT");
				createPostMap.put("post_hide_views_count", "false");
				createPostMap.put("post_region", "none");
				createPostMap.put("post_hide_like_count", "false");
				createPostMap.put("post_hide_comments_count", "false");
				createPostMap.put("post_visibility", "public");
				createPostMap.put("post_disable_favorite", "false");
				createPostMap.put("post_disable_comments", "false");
				createPostMap.put("publish_date", String.valueOf((long)(cc.getTimeInMillis())));
				FirebaseDatabase.getInstance().getReference("skyline/posts").child(uniqueKey).updateChildren(createPostMap, (databaseError, databaseReference) -> {
					if (databaseError == null) {
						Toast.makeText(getContext(), getResources().getString(R.string.post_publish_success), Toast.LENGTH_SHORT).show();
						currentPostFilter = "PUBLIC";
						_loadPosts(currentPostFilter);
					} else {
						Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
					}
				});
			}
		}
		vbr.vibrate((long)(48));
	}

	@Override
	public void onFilterClick(String filter) {
		currentPostFilter = filter;
		_loadPosts(currentPostFilter);
	}

    public void _loadPosts(final String filterType) {
        swipeLayout.setRefreshing(true);
        Query query = null;
        String notFoundMessage = "There are no public posts available at the moment.";

        switch (filterType) {
            case "PUBLIC":
                query = postsRef.orderByChild("publish_date");
                notFoundMessage = "There are no public posts available at the moment.";
                _fetchAndDisplayPosts(query, notFoundMessage);
                break;
            case "LOCAL":
                udb.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists() && dataSnapshot.child("user_region").exists()) {
                            String userRegion = dataSnapshot.child("user_region").getValue(String.class);
                            Query localPostsQuery = postsRef.orderByChild("post_region").equalTo(userRegion);
                            _fetchAndDisplayPosts(localPostsQuery, "No regional posts found for your area.");
                        } else {
                            _finalizePostDisplay("No regional posts found or your region is not set.", false);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), "Error fetching user region, showing cached data if available: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        _finalizePostDisplay("Error loading regional posts (showing cached if available).", false);
                    }
                });
                break;
            case "FOLLOWED":
                _firebase.getReference("skyline/following").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        PostsList.clear();
                        if (dataSnapshot.exists()) {
                            final List<String> followedUids = new ArrayList<>();
                            for (DataSnapshot followSnap : dataSnapshot.getChildren()) {
                                followedUids.add(followSnap.getKey());
                            }

                            if (followedUids.isEmpty()) {
                                _finalizePostDisplay("You are not following any users yet. Follow users to see their posts here.", false);
                                return;
                            }

                            final int[] completedQueries = {0};
                            final int totalQueries = followedUids.size();
                            ExecutorService fetchExecutor = Executors.newFixedThreadPool(Math.min(followedUids.size(), 5));
                            Handler mainHandler = new Handler(Looper.getMainLooper());

                            for (String uid : followedUids) {
                                fetchExecutor.execute(() -> {
                                    postsRef.orderByChild("uid").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot userPostsSnapshot) {
                                            mainHandler.post(() -> {
                                                for (DataSnapshot postSnap : userPostsSnapshot.getChildren()) {
                                                    GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                                                    HashMap<String, Object> _map = postSnap.getValue(_ind);
                                                    if (_map != null) {
                                                        PostsList.add(_map);
                                                    }
                                                }
                                                completedQueries[0]++;
                                                if (completedQueries[0] == totalQueries) {
                                                    _finalizePostDisplay("No posts from followed users found.", true);
                                                }
                                            });
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            mainHandler.post(() -> {
                                                completedQueries[0]++;
                                                Toast.makeText(getContext(), "Error loading followed users' posts, showing cached data if available: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                                if (completedQueries[0] == totalQueries) {
                                                    _finalizePostDisplay("Error loading followed users' posts (showing cached if available).", true);
                                                }
                                            });
                                        }
                                    });
                                });
                            }
                        } else {
                            _finalizePostDisplay("You are not following any users yet. Follow users to see their posts here.", false);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), "Error fetching followed users keys, showing cached data if available: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        _finalizePostDisplay("Error loading followed users' posts (showing cached if available).", false);
                    }
                });
                break;
            case "FAVORITE":
                _firebase.getReference("skyline/favorite-posts").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        PostsList.clear();
                        if (dataSnapshot.exists()) {
                            final List<String> favoritePostKeys = new ArrayList<>();
                            for (DataSnapshot favSnap : dataSnapshot.getChildren()) {
                                favoritePostKeys.add(favSnap.getKey());
                            }

                            if (favoritePostKeys.isEmpty()) {
                                _finalizePostDisplay("You have no saved posts yet.", false);
                                return;
                            }

                            final int[] completedQueries = {0};
                            final int totalQueries = favoritePostKeys.size();

                            ExecutorService fetchExecutor = Executors.newFixedThreadPool(Math.min(favoritePostKeys.size(), 5));
                            Handler mainHandler = new Handler(Looper.getMainLooper());

                            for (String key : favoritePostKeys) {
                                fetchExecutor.execute(() -> {
                                    postsRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot postSnapshot) {
                                            mainHandler.post(() -> {
                                                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                                                HashMap<String, Object> _map = postSnapshot.getValue(_ind);
                                                if (_map != null) {
                                                    PostsList.add(_map);
                                                }
                                                completedQueries[0]++;
                                                if (completedQueries[0] == totalQueries) {
                                                    _finalizePostDisplay("You have no saved posts yet.", true);
                                                }
                                            });
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            mainHandler.post(() -> {
                                                completedQueries[0]++;
                                                Toast.makeText(getContext(), "Error loading saved posts, showing cached data if available: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                                if (completedQueries[0] == totalQueries) {
                                                    _finalizePostDisplay("Error loading saved posts (showing cached if available).", true);
                                                }
                                            });
                                        }
                                    });
                                });
                            }
                        } else {
                            _finalizePostDisplay("You have no saved posts yet.", false);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), "Error fetching favorite posts keys, showing cached data if available: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        _finalizePostDisplay("Error loading saved posts (showing cached if available).", false);
                    }
                });
                break;
        }
    }

    private void _fetchAndDisplayPosts(Query query, final String notFoundMessage) {
        if (query == null) {
            _finalizePostDisplay(notFoundMessage, false);
            return;
        }

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {
                PostsList.clear();
                if (_dataSnapshot.exists()) {
                    try {
                        GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                        for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                            HashMap<String, Object> _map = _data.getValue(_ind);
                            PostsList.add(_map);
                        }
                    } catch (Exception _e) {
                        _e.printStackTrace();
                    }
                    _finalizePostDisplay(notFoundMessage, true);
                } else {
                    _finalizePostDisplay(notFoundMessage, false);
                }
            }

            @Override
            public void onCancelled(DatabaseError _databaseError) {
                Toast.makeText(getContext(), "Failed to fetch latest posts, showing cached data. Error: " + _databaseError.getMessage(), Toast.LENGTH_LONG).show();
                _finalizePostDisplay(notFoundMessage, false);
            }
        });
    }

    private void _finalizePostDisplay(String notFoundMessage, boolean sortAndNotify) {
        if (sortAndNotify) {
            Collections.sort(PostsList, (o1, o2) -> {
                long date1 = Long.parseLong(o1.get("publish_date").toString());
                long date2 = Long.parseLong(o2.get("publish_date").toString());
                return Long.compare(date2, date1);
            });
        }

        if (PublicPostsList.getAdapter() == null || !(PublicPostsList.getAdapter() instanceof PublicPostsListAdapter)) {
            PublicPostsList.setAdapter(new PublicPostsListAdapter(PostsList, this, getContext()));
        } else {
            ((PublicPostsListAdapter)PublicPostsList.getAdapter()).setData(PostsList);
        }

        if (PostsList.isEmpty()) {
            PublicPostsList.setVisibility(View.GONE);
            PublicPostsListNotFound.setText(notFoundMessage);
            PublicPostsListNotFound.setVisibility(View.VISIBLE);
        } else {
            PublicPostsList.setVisibility(View.VISIBLE);
            PublicPostsListNotFound.setVisibility(View.GONE);
        }
        loadingBody.setVisibility(View.GONE);
        swipeLayout.setRefreshing(false);
    }
}
