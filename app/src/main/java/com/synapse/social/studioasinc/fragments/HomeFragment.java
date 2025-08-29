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

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.os.Handler;
import android.os.Looper;

public class HomeFragment extends Fragment {

    private FirebaseDatabase _firebase;
    private DatabaseReference udb;
    private DatabaseReference postsRef;
    private DatabaseReference storiesDbRef;

    private HashMap<String, Object> createPostMap = new HashMap<>();
    private HashMap<String, Object> postLikeCountCache = new HashMap<>();
    private HashMap<String, Object> UserInfoCacheMap = new HashMap<>();
    private HashMap<String, Object> postFavoriteCountCache = new HashMap<>();
    private String currentPostFilter = "PUBLIC";

    private ArrayList<HashMap<String, Object>> storiesList = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> PostsList = new ArrayList<>();

    private CoordinatorLayout m_coordinator_layout;
    private LinearLayout loadingBody;
    private AppBarLayout m_coordinator_layout_appbar;
    private SwipeRefreshLayout swipeLayout;
    private LinearLayout m_coordinator_layout_collapsing_toolbar_body;
    private LinearLayout stories;
    private LinearLayout miniPostLayout;
    private HorizontalScrollView miniPostLayoutFiltersScroll;
    private RecyclerView storiesView;
    private RecyclerView PublicPostsList;
    private TextView PublicPostsListNotFound;
    private CardView miniPostLayoutProfileCard;
    private EditText miniPostLayoutTextPostInput;
    private ImageView miniPostLayoutProfileImage;
    private ImageView miniPostLayoutImagePost;
    private ImageView miniPostLayoutVideoPost;
    private ImageView miniPostLayoutTextPost;
    private ImageView miniPostLayoutMoreButton;
    private TextView miniPostLayoutTextPostPublish;
    private TextView miniPostLayoutFiltersScrollBodyFilterLOCAL;
    private TextView miniPostLayoutFiltersScrollBodyFilterPUBLIC;
    private TextView miniPostLayoutFiltersScrollBodyFilterFOLLOWED;
    private TextView miniPostLayoutFiltersScrollBodyFilterFAVORITE;
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
        m_coordinator_layout = view.findViewById(R.id.m_coordinator_layout);
        loadingBody = view.findViewById(R.id.loadingBody);
        m_coordinator_layout_appbar = view.findViewById(R.id.m_coordinator_layout_appbar);
        swipeLayout = view.findViewById(R.id.swipeLayout);
        m_coordinator_layout_collapsing_toolbar_body = view.findViewById(R.id.m_coordinator_layout_collapsing_toolbar_body);
        stories = view.findViewById(R.id.stories);
        miniPostLayout = view.findViewById(R.id.miniPostLayout);
        miniPostLayoutFiltersScroll = view.findViewById(R.id.miniPostLayoutFiltersScroll);
        storiesView = view.findViewById(R.id.storiesView);
        miniPostLayoutProfileCard = view.findViewById(R.id.miniPostLayoutProfileCard);
        miniPostLayoutTextPostInput = view.findViewById(R.id.miniPostLayoutTextPostInput);
        miniPostLayoutProfileImage = view.findViewById(R.id.miniPostLayoutProfileImage);
        miniPostLayoutImagePost = view.findViewById(R.id.miniPostLayoutImagePost);
        miniPostLayoutVideoPost = view.findViewById(R.id.miniPostLayoutVideoPost);
        miniPostLayoutTextPost = view.findViewById(R.id.miniPostLayoutTextPost);
        miniPostLayoutMoreButton = view.findViewById(R.id.miniPostLayoutMoreButton);
        miniPostLayoutTextPostPublish = view.findViewById(R.id.miniPostLayoutTextPostPublish);
        miniPostLayoutFiltersScrollBodyFilterLOCAL = view.findViewById(R.id.miniPostLayoutFiltersScrollBodyFilterLOCAL);
        miniPostLayoutFiltersScrollBodyFilterPUBLIC = view.findViewById(R.id.miniPostLayoutFiltersScrollBodyFilterPUBLIC);
        miniPostLayoutFiltersScrollBodyFilterFOLLOWED = view.findViewById(R.id.miniPostLayoutFiltersScrollBodyFilterFOLLOWED);
        miniPostLayoutFiltersScrollBodyFilterFAVORITE = view.findViewById(R.id.miniPostLayoutFiltersScrollBodyFilterFAVORITE);
        PublicPostsList = view.findViewById(R.id.PublicPostsList);
        PublicPostsListNotFound = view.findViewById(R.id.PublicPostsListNotFound);
		loading_bar = view.findViewById(R.id.loading_bar);

        vbr = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        auth = FirebaseAuth.getInstance();

        swipeLayout.setOnRefreshListener(() -> {
            _loadPosts(currentPostFilter);
            _loadStories();
        });

        miniPostLayoutTextPostInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
                final String _charSeq = _param1.toString();
                if (_charSeq.length() == 0) {
                    miniPostLayoutTextPostPublish.setVisibility(View.GONE);
                } else {
                    _viewGraphics(miniPostLayoutTextPostPublish, getResources().getColor(R.color.colorPrimary), 0xFFC5CAE9, 300, 0, Color.TRANSPARENT);
                    miniPostLayoutTextPostPublish.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {}

            @Override
            public void afterTextChanged(Editable _param1) {}
        });

        miniPostLayoutImagePost.setOnClickListener(v -> {
            intent.setClass(getContext(), CreatePostActivity.class);
            startActivity(intent);
        });

        miniPostLayoutVideoPost.setOnClickListener(v -> {
            intent.setClass(getContext(), CreateLineVideoActivity.class);
            startActivity(intent);
        });

        miniPostLayoutTextPostPublish.setOnClickListener(v -> {
            if (miniPostLayoutTextPostInput.getText().toString().trim().equals("")) {
                Toast.makeText(getContext(), getResources().getString(R.string.please_enter_text), Toast.LENGTH_SHORT).show();
            } else {
                if (!(miniPostLayoutTextPostInput.getText().toString().length() > 1500)) {
                    String uniqueKey = udb.push().getKey();
                    cc = Calendar.getInstance();
                    createPostMap = new HashMap<>();
                    createPostMap.put("key", uniqueKey);
                    createPostMap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    createPostMap.put("post_text", miniPostLayoutTextPostInput.getText().toString().trim());
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
                    miniPostLayoutTextPostInput.setText("");
                }
            }
            vbr.vibrate((long)(48));
        });

        miniPostLayoutFiltersScrollBodyFilterLOCAL.setOnClickListener(v -> {
            _viewGraphics(miniPostLayoutFiltersScrollBodyFilterLOCAL, getResources().getColor(R.color.colorPrimary), 0xFF9FA8DA, 300, 0, Color.TRANSPARENT);
            _viewGraphics(miniPostLayoutFiltersScrollBodyFilterPUBLIC, 0xFFFFFFFF, 0xFFEEEEEE, 300, 2, 0xFFEEEEEE);
            _viewGraphics(miniPostLayoutFiltersScrollBodyFilterFOLLOWED, 0xFFFFFFFF, 0xFFEEEEEE, 300, 2, 0xFFEEEEEE);
            _viewGraphics(miniPostLayoutFiltersScrollBodyFilterFAVORITE, 0xFFFFFFFF, 0xFFEEEEEE, 300, 2, 0xFFEEEEEE);
            miniPostLayoutFiltersScrollBodyFilterLOCAL.setTextColor(0xFFFFFFFF);
            miniPostLayoutFiltersScrollBodyFilterPUBLIC.setTextColor(0xFF616161);
            miniPostLayoutFiltersScrollBodyFilterFOLLOWED.setTextColor(0xFF616161);
            miniPostLayoutFiltersScrollBodyFilterFAVORITE.setTextColor(0xFF616161);
            currentPostFilter = "LOCAL";
            _loadPosts(currentPostFilter);
        });

        miniPostLayoutFiltersScrollBodyFilterPUBLIC.setOnClickListener(v -> {
            _viewGraphics(miniPostLayoutFiltersScrollBodyFilterLOCAL, 0xFFFFFFFF, 0xFFEEEEEE, 300, 2, 0xFFEEEEEE);
            _viewGraphics(miniPostLayoutFiltersScrollBodyFilterPUBLIC, getResources().getColor(R.color.colorPrimary), 0xFF1A237E, 300, 0, Color.TRANSPARENT);
            _viewGraphics(miniPostLayoutFiltersScrollBodyFilterFOLLOWED, 0xFFFFFFFF, 0xFFEEEEEE, 300, 2, 0xFFEEEEEE);
            _viewGraphics(miniPostLayoutFiltersScrollBodyFilterFAVORITE, 0xFFFFFFFF, 0xFFEEEEEE, 300, 2, 0xFFEEEEEE);
            miniPostLayoutFiltersScrollBodyFilterLOCAL.setTextColor(0xFF616161);
            miniPostLayoutFiltersScrollBodyFilterPUBLIC.setTextColor(0xFFFFFFFF);
            miniPostLayoutFiltersScrollBodyFilterFOLLOWED.setTextColor(0xFF616161);
            miniPostLayoutFiltersScrollBodyFilterFAVORITE.setTextColor(0xFF616161);
            currentPostFilter = "PUBLIC";
            _loadPosts(currentPostFilter);
        });

        miniPostLayoutFiltersScrollBodyFilterFOLLOWED.setOnClickListener(v -> {
            _viewGraphics(miniPostLayoutFiltersScrollBodyFilterLOCAL, 0xFFFFFFFF, 0xFFEEEEEE, 300, 2, 0xFFEEEEEE);
            _viewGraphics(miniPostLayoutFiltersScrollBodyFilterPUBLIC, 0xFFFFFFFF, 0xFFEEEEEE, 300, 2, 0xFFEEEEEE);
            _viewGraphics(miniPostLayoutFiltersScrollBodyFilterFOLLOWED, getResources().getColor(R.color.colorPrimary), 0xFF616161, 300, 0, Color.TRANSPARENT);
            _viewGraphics(miniPostLayoutFiltersScrollBodyFilterFAVORITE, 0xFFFFFFFF, 0xFFEEEEEE, 300, 2, 0xFFEEEEEE);
            miniPostLayoutFiltersScrollBodyFilterLOCAL.setTextColor(0xFF616161);
            miniPostLayoutFiltersScrollBodyFilterPUBLIC.setTextColor(0xFF616161);
            miniPostLayoutFiltersScrollBodyFilterFOLLOWED.setTextColor(0xFFFFFFFF);
            miniPostLayoutFiltersScrollBodyFilterFAVORITE.setTextColor(0xFF616161);
            currentPostFilter = "FOLLOWED";
            _loadPosts(currentPostFilter);
        });

        miniPostLayoutFiltersScrollBodyFilterFAVORITE.setOnClickListener(v -> {
            _viewGraphics(miniPostLayoutFiltersScrollBodyFilterLOCAL, 0xFFFFFFFF, 0xFFEEEEEE, 300, 2, 0xFFEEEEEE);
            _viewGraphics(miniPostLayoutFiltersScrollBodyFilterPUBLIC, 0xFFFFFFFF, 0xFFEEEEEE, 300, 2, 0xFFEEEEEE);
            _viewGraphics(miniPostLayoutFiltersScrollBodyFilterFOLLOWED, 0xFFFFFFFF, 0xFFEEEEEE, 300, 2, 0xFFEEEEEE);
            _viewGraphics(miniPostLayoutFiltersScrollBodyFilterFAVORITE, getResources().getColor(R.color.colorPrimary), 0xFF9FA8DA, 300, 0, Color.TRANSPARENT);
            miniPostLayoutFiltersScrollBodyFilterLOCAL.setTextColor(0xFF616161);
            miniPostLayoutFiltersScrollBodyFilterPUBLIC.setTextColor(0xFF616161);
            miniPostLayoutFiltersScrollBodyFilterFOLLOWED.setTextColor(0xFF616161);
            miniPostLayoutFiltersScrollBodyFilterFAVORITE.setTextColor(0xFFFFFFFF);
            currentPostFilter = "FAVORITE";
            _loadPosts(currentPostFilter);
        });
    }

    private void initializeLogic() {
        _loadPosts(currentPostFilter);
        _loadStories();

        DatabaseReference getReference = udb.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        getReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    if (dataSnapshot.child("avatar").getValue(String.class) != null && !dataSnapshot.child("avatar").getValue(String.class).equals("null")) {
                        Glide.with(getContext()).load(Uri.parse(dataSnapshot.child("avatar").getValue(String.class))).into(miniPostLayoutProfileImage);
                    } else {
                        miniPostLayoutProfileImage.setImageResource(R.drawable.avatar);
                    }
                    SynapseApp.setUserStatus();
                } else {
                    miniPostLayoutProfileImage.setImageResource(R.drawable.avatar);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error fetching user profile: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                miniPostLayoutProfileImage.setImageResource(R.drawable.avatar);
            }
        });

        _viewGraphics(miniPostLayoutFiltersScrollBodyFilterLOCAL, 0xFFFFFFFF, 0xFFEEEEEE, 300, 2, 0xFFEEEEEE);
        _viewGraphics(miniPostLayoutFiltersScrollBodyFilterPUBLIC, getResources().getColor(R.color.colorPrimary), 0xFF9FA8DA, 300, 0, Color.TRANSPARENT);
        _viewGraphics(miniPostLayoutFiltersScrollBodyFilterFOLLOWED, 0xFFFFFFFF, 0xFFEEEEEE, 300, 2, 0xFFEEEEEE);
        _viewGraphics(miniPostLayoutFiltersScrollBodyFilterFAVORITE, 0xFFFFFFFF, 0xFFEEEEEE, 300, 2, 0xFFEEEEEE);
        miniPostLayoutFiltersScrollBodyFilterLOCAL.setTextColor(0xFF616161);
        miniPostLayoutFiltersScrollBodyFilterPUBLIC.setTextColor(0xFFFFFFFF);
        miniPostLayoutFiltersScrollBodyFilterFOLLOWED.setTextColor(0xFF616161);
        miniPostLayoutTextPostPublish.setVisibility(View.GONE);
        miniPostLayoutFiltersScrollBodyFilterFAVORITE.setTextColor(0xFF616161);
        _ImageColor(miniPostLayoutImagePost, 0xFF445E91);
        _ImageColor(miniPostLayoutVideoPost, 0xFF445E91);
        _ImageColor(miniPostLayoutTextPost, 0xFF445E91);
        _ImageColor(miniPostLayoutMoreButton, 0xFF445E91);
        _viewGraphics(miniPostLayoutImagePost, 0xFFFFFFFF, 0xFFEEEEEE, 300, 1, 0xFFEEEEEE);
        _viewGraphics(miniPostLayoutVideoPost, 0xFFFFFFFF, 0xFFEEEEEE, 300, 1, 0xFFEEEEEE);
        _viewGraphics(miniPostLayoutTextPost, 0xFFFFFFFF, 0xFFEEEEEE, 300, 1, 0xFFEEEEEE);
        _viewGraphics(miniPostLayoutMoreButton, 0xFFFFFFFF, 0xFFEEEEEE, 300, 1, 0xFFEEEEEE);

        storiesView.setAdapter(new StoriesViewAdapter(storiesList));
        storiesView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));
        PublicPostsList.setLayoutManager(new LinearLayoutManager(getContext()));
        PublicPostsList.setAdapter(new PublicPostsListAdapter(PostsList));
        _viewGraphics(miniPostLayoutTextPostPublish, Color.TRANSPARENT, Color.TRANSPARENT, 300, 2, 0xFF616161);
    }

    public void _ImageColor(final ImageView _image, final int _color) {
        _image.setColorFilter(_color,PorterDuff.Mode.SRC_ATOP);
    }

    public void _viewGraphics(final View _view, final int _onFocus, final int _onRipple, final double _radius, final double _stroke, final int _strokeColor) {
        android.graphics.drawable.GradientDrawable GG = new android.graphics.drawable.GradientDrawable();
        GG.setColor(_onFocus);
        GG.setCornerRadius((float)_radius);
        GG.setStroke((int) _stroke, _strokeColor);
        android.graphics.drawable.RippleDrawable RE = new android.graphics.drawable.RippleDrawable(new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{ _onRipple}), GG, null);
        _view.setBackground(RE);
    }

    private void _loadStories() {
        storiesDbRef.orderByChild("publish_date")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        storiesList.clear();
                        HashMap<String, Object> myStoryPlaceholder = new HashMap<>();
                        myStoryPlaceholder.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        storiesList.add(myStoryPlaceholder);

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot storySnap : dataSnapshot.getChildren()) {
                                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                                HashMap<String, Object> storyMap = storySnap.getValue(_ind);
                                if (storyMap != null) {
                                    if (!storyMap.containsKey("uid") || !storyMap.get("uid").equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                        storiesList.add(storyMap);
                                    }
                                }
                            }
                        }
                        if (storiesView.getAdapter() != null) {
                            storiesView.getAdapter().notifyDataSetChanged();
                        } else {
                            storiesView.setAdapter(new StoriesViewAdapter(storiesList));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), "Error loading stories: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        if (storiesView.getAdapter() != null) {
                            storiesView.getAdapter().notifyDataSetChanged();
                        } else {
                            storiesView.setAdapter(new StoriesViewAdapter(storiesList));
                        }
                    }
                });
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
            PublicPostsList.setAdapter(new PublicPostsListAdapter(PostsList));
        } else {
            ((PublicPostsListAdapter)PublicPostsList.getAdapter()).notifyDataSetChanged();
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

    public void _setTime(final double _currentTime, final TextView _txt) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		double time_diff = c1.getTimeInMillis() - _currentTime;
		if (time_diff < 60000) {
			if ((time_diff / 1000) < 2) {
				_txt.setText("1" + " " + getResources().getString(R.string.seconds_ago));
			} else {
				_txt.setText(String.valueOf((long)(time_diff / 1000)).concat(" " + getResources().getString(R.string.seconds_ago)));
			}
		} else {
			if (time_diff < (60 * 60000)) {
				if ((time_diff / 60000) < 2) {
					_txt.setText("1" + " " + getResources().getString(R.string.minutes_ago));
				} else {
					_txt.setText(String.valueOf((long)(time_diff / 60000)).concat(" " + getResources().getString(R.string.minutes_ago)));
				}
			} else {
				if (time_diff < (24 * (60 * 60000))) {
					if ((time_diff / (60 * 60000)) < 2) {
						_txt.setText(String.valueOf((long)(time_diff / (60 * 60000))).concat(" " + getResources().getString(R.string.hours_ago)));
					} else {
						_txt.setText(String.valueOf((long)(time_diff / (60 * 60000))).concat(" " + getResources().getString(R.string.hours_ago)));
					}
				} else {
					if (time_diff < (7 * (24 * (60 * 60000)))) {
						if ((time_diff / (24 * (60 * 60000))) < 2) {
							_txt.setText(String.valueOf((long)(time_diff / (24 * (60 * 60000)))).concat(" " + getResources().getString(R.string.days_ago)));
						} else {
							_txt.setText(String.valueOf((long)(time_diff / (24 * (60 * 60000)))).concat(" " + getResources().getString(R.string.days_ago)));
						}
					} else {
						c2.setTimeInMillis((long)(_currentTime));
						_txt.setText(new SimpleDateFormat("dd-MM-yyyy").format(c2.getTime()));
					}
				}
			}
		}
	}

    public void _setCount(final TextView _txt, final double _number) {
        if (_number < 10000) {
            _txt.setText(String.valueOf((long) _number));
        } else {
            DecimalFormat decimalFormat = new DecimalFormat("0.0");
            String numberFormat;
            double formattedNumber;
            if (_number < 1000000) {
                numberFormat = "K";
                formattedNumber = _number / 1000;
            } else if (_number < 1000000000) {
                numberFormat = "M";
                formattedNumber = _number / 1000000;
            } else if (_number < 1000000000000L) {
                numberFormat = "B";
                formattedNumber = _number / 1000000000;
            } else {
                numberFormat = "T";
                formattedNumber = _number / 1000000000000L;
            }
            _txt.setText(decimalFormat.format(formattedNumber) + numberFormat);
        }
    }

    public void _ImgRound(final ImageView _imageview, final double _value) {
        android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable ();
        gd.setColor(android.R.color.transparent);
        gd.setCornerRadius((int)_value);
        _imageview.setClipToOutline(true);
        _imageview.setBackground(gd);
    }

    public void _OpenWebView(final String _URL) {
        String AndroidDevelopersBlogURL = _URL;
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(Color.parseColor("#242D39"));
        CustomTabsIntent customtabsintent = builder.build();
        customtabsintent.launchUrl(getContext(), Uri.parse(AndroidDevelopersBlogURL));
    }

    public class StoriesViewAdapter extends RecyclerView.Adapter<StoriesViewAdapter.ViewHolder> {
        ArrayList<HashMap<String, Object>> _data;
        public StoriesViewAdapter(ArrayList<HashMap<String, Object>> _arr) {
            _data = _arr;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater _inflater = getLayoutInflater();
            View _v = _inflater.inflate(R.layout.synapse_story_cv, null);
            RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            _v.setLayoutParams(_lp);
            return new ViewHolder(_v);
        }
        @Override
        public void onBindViewHolder(ViewHolder _holder, final int _position) {
            View _view = _holder.itemView;
            final LinearLayout storiesMyStory = _view.findViewById(R.id.storiesMyStory);
            final LinearLayout storiesSecondStory = _view.findViewById(R.id.storiesSecondStory);
            final androidx.cardview.widget.CardView storiesMyStoryProfileCard = _view.findViewById(R.id.storiesMyStoryProfileCard);
            final TextView storiesMyStoryTitle = _view.findViewById(R.id.storiesMyStoryTitle);
            final ImageView storiesMyStoryProfileImage = _view.findViewById(R.id.storiesMyStoryProfileImage);
            final LinearLayout storiesMyStoryRelativeAddBody = _view.findViewById(R.id.storiesMyStoryRelativeAddBody);
            final ImageView storiesMyStoryRelativeAdd = _view.findViewById(R.id.storiesMyStoryRelativeAdd);
            final androidx.cardview.widget.CardView storiesSecondStoryProfileCard = _view.findViewById(R.id.storiesSecondStoryProfileCard);
            final TextView storiesSecondStoryTitle = _view.findViewById(R.id.storiesSecondStoryTitle);
            final ImageView storiesSecondStoryProfileImage = _view.findViewById(R.id.storiesSecondStoryProfileImage);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
            (int) (getResources().getDisplayMetrics().density * 80),
            ViewGroup.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(
            (int) (getResources().getDisplayMetrics().density * 4),
            (int) (getResources().getDisplayMetrics().density * 8),
            (int) (getResources().getDisplayMetrics().density * 4),
            (int) (getResources().getDisplayMetrics().density * 8)
            );
            _view.setLayoutParams(layoutParams);

            _ImageColor(storiesMyStoryRelativeAdd, 0xFFFFFFFF);
            _viewGraphics(storiesMyStory, 0xFFFFFFFF, 0xFFEEEEEE, 18, 0, Color.TRANSPARENT);
            _viewGraphics(storiesSecondStory, 0xFFFFFFFF, 0xFFEEEEEE, 18, 0, Color.TRANSPARENT);
            storiesMyStoryRelativeAddBody.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)0, 0x7B000000));
            storiesMyStoryProfileCard.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)300, Color.TRANSPARENT));
            storiesSecondStoryProfileCard.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)300, Color.TRANSPARENT));

            if (_position == 0) {
                storiesMyStoryTitle.setText(getResources().getString(R.string.add_story));
                DatabaseReference getReference = _firebase.getReference().child("skyline/users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                getReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            if (dataSnapshot.child("avatar").getValue(String.class) != null && dataSnapshot.child("avatar").getValue(String.class).equals("null")) {
                                storiesMyStoryProfileImage.setImageResource(R.drawable.avatar);
                            } else {
                                Glide.with(getContext()).load(Uri.parse(dataSnapshot.child("avatar").getValue(String.class))).into(storiesMyStoryProfileImage);
                            }
                        } else {
                            storiesMyStoryProfileImage.setImageResource(R.drawable.avatar);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("StoriesAdapter", "Failed to load user avatar for My Story: " + databaseError.getMessage());
                        storiesMyStoryProfileImage.setImageResource(R.drawable.avatar);
                    }
                });
                storiesMyStory.setVisibility(View.VISIBLE);
                storiesSecondStory.setVisibility(View.GONE);
            } else {
                storiesMyStory.setVisibility(View.GONE);
                storiesSecondStory.setVisibility(View.VISIBLE);
                HashMap<String, Object> storyMap = _data.get(_position);
                String storyUid = (String) storyMap.get("uid");
                if (UserInfoCacheMap.containsKey("uid-" + storyUid)) {
                    _displayUserInfoForStory(storyUid, storiesSecondStoryProfileImage, storiesSecondStoryTitle);
                } else {
                    udb.child(storyUid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                UserInfoCacheMap.put("uid-" + storyUid, storyUid);
                                UserInfoCacheMap.put("nickname-" + storyUid, dataSnapshot.child("nickname").getValue(String.class));
                                UserInfoCacheMap.put("username-" + storyUid, dataSnapshot.child("username").getValue(String.class));
                                UserInfoCacheMap.put("avatar-" + storyUid, dataSnapshot.child("avatar").getValue(String.class));
                                _displayUserInfoForStory(storyUid, storiesSecondStoryProfileImage, storiesSecondStoryTitle);
                            } else {
                                storiesSecondStoryProfileImage.setImageResource(R.drawable.avatar);
                                storiesSecondStoryTitle.setText("Unknown User");
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("StoriesAdapter", "Failed to load user info for story: " + databaseError.getMessage());
                            storiesSecondStoryProfileImage.setImageResource(R.drawable.avatar);
                            storiesSecondStoryTitle.setText("Error User");
                        }
                    });
                }
            }
            storiesMyStory.setOnClickListener(_view1 -> Toast.makeText(getContext(), "Add story clicked", Toast.LENGTH_SHORT).show());
            storiesSecondStory.setOnClickListener(_view1 -> Toast.makeText(getContext(), "Story ".concat(String.valueOf(_position)).concat(" clicked"), Toast.LENGTH_SHORT).show());
        }
        @Override
        public int getItemCount() {
            return _data.size();
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View v) {
                super(v);
            }
        }
        private void _displayUserInfoForStory(String uid, ImageView profileImage, TextView titleTextView) {
            String avatarUrl = (String) UserInfoCacheMap.get("avatar-" + uid);
            String nickname = (String) UserInfoCacheMap.get("nickname-" + uid);
            String username = (String) UserInfoCacheMap.get("username-" + uid);

            if (avatarUrl != null && !avatarUrl.equals("null")) {
                Glide.with(getContext()).load(Uri.parse(avatarUrl)).into(profileImage);
            } else {
                profileImage.setImageResource(R.drawable.avatar);
            }

            if (nickname != null && !nickname.equals("null") && !nickname.isEmpty()) {
                titleTextView.setText(nickname);
            } else if (username != null && !username.equals("null") && !username.isEmpty()) {
                titleTextView.setText("@" + username);
            } else {
                titleTextView.setText("User Story");
            }
        }
    }

    public class PublicPostsListAdapter extends RecyclerView.Adapter<PublicPostsListAdapter.ViewHolder> {
        ArrayList<HashMap<String, Object>> _data;
        public PublicPostsListAdapter(ArrayList<HashMap<String, Object>> _arr) {
            _data = _arr;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater _inflater = getLayoutInflater();
            View _v = _inflater.inflate(R.layout.synapse_post_cv, null);
            RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            _v.setLayoutParams(_lp);
            return new ViewHolder(_v);
        }
        @Override
        public void onBindViewHolder(ViewHolder _holder, final int _position) {
            View _view = _holder.itemView;
            final LinearLayout body = _view.findViewById(R.id.body);
            final ImageView topMoreButton = _view.findViewById(R.id.topMoreButton);
            final androidx.cardview.widget.CardView userInfoProfileCard = _view.findViewById(R.id.userInfoProfileCard);
            final ImageView userInfoProfileImage = _view.findViewById(R.id.userInfoProfileImage);
            final TextView userInfoUsername = _view.findViewById(R.id.userInfoUsername);
            final ImageView userInfoGenderBadge = _view.findViewById(R.id.userInfoGenderBadge);
            final ImageView userInfoUsernameVerifiedBadge = _view.findViewById(R.id.userInfoUsernameVerifiedBadge);
            final TextView postPublishDate = _view.findViewById(R.id.postPublishDate);
            final ImageView postPrivateStateIcon = _view.findViewById(R.id.postPrivateStateIcon);
            final TextView postMessageTextMiddle = _view.findViewById(R.id.postMessageTextMiddle);
            final ImageView postImage = _view.findViewById(R.id.postImage);
            final LinearLayout likeButton = _view.findViewById(R.id.likeButton);
            final LinearLayout commentsButton = _view.findViewById(R.id.commentsButton);
            final ImageView favoritePostButton = _view.findViewById(R.id.favoritePostButton);
            final ImageView likeButtonIc = _view.findViewById(R.id.likeButtonIc);
            final TextView likeButtonCount = _view.findViewById(R.id.likeButtonCount);
            final TextView commentsButtonCount = _view.findViewById(R.id.commentsButtonCount);

            body.setVisibility(View.GONE);
            userInfoProfileCard.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)300, Color.TRANSPARENT));
            _ImageColor(postPrivateStateIcon, 0xFF616161);
            _viewGraphics(topMoreButton, 0xFFFFFFFF, 0xFFEEEEEE, 300, 0, Color.TRANSPARENT);

            postImage.setVisibility(View.GONE);

            if (_data.get(_position).containsKey("post_text")) {
                String postText = _data.get(_position).get("post_text").toString();
                com.synapse.social.studioasinc.styling.MarkdownRenderer.get(postMessageTextMiddle.getContext()).render(postMessageTextMiddle, postText);
                postMessageTextMiddle.setVisibility(View.VISIBLE);
            } else {
                postMessageTextMiddle.setVisibility(View.GONE);
            }

            if (_data.get(_position).containsKey("post_image")) {
                Glide.with(getContext()).load(Uri.parse(_data.get(_position).get("post_image").toString())).into(postImage);
                postImage.setOnClickListener(_view1 -> _OpenWebView(_data.get(_position).get("post_image").toString()));
                postImage.setVisibility(View.VISIBLE);
            } else {
                postImage.setVisibility(View.GONE);
            }

            if (_data.get(_position).get("post_hide_like_count").toString().equals("true")) {
                likeButtonCount.setVisibility(View.GONE);
            } else {
                likeButtonCount.setVisibility(View.VISIBLE);
            }
            if (_data.get(_position).get("post_hide_comments_count").toString().equals("true")) {
                commentsButtonCount.setVisibility(View.GONE);
            } else {
                commentsButtonCount.setVisibility(View.VISIBLE);
            }
            if (_data.get(_position).get("post_disable_comments").toString().equals("true")) {
                commentsButton.setVisibility(View.GONE);
            } else {
                commentsButton.setVisibility(View.VISIBLE);
            }
            _setTime(Double.parseDouble(_data.get(_position).get("publish_date").toString()), postPublishDate);

            final String postUid = _data.get(_position).get("uid").toString();
            if (UserInfoCacheMap.containsKey("uid-".concat(postUid))) {
                _updatePostViewVisibility(body, postPrivateStateIcon, postUid, _data.get(_position).get("post_visibility").toString());
                _displayUserInfoFromCache(postUid, userInfoProfileImage, userInfoUsername, userInfoGenderBadge, userInfoUsernameVerifiedBadge);
            } else {
                ExecutorService mExecutorService = Executors.newSingleThreadExecutor();
                Handler mMainHandler = new Handler(Looper.getMainLooper());
                mExecutorService.execute(() -> {
                    DatabaseReference userRef = _firebase.getReference().child("skyline/users").child(postUid);
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                UserInfoCacheMap.put("uid-".concat(postUid), postUid);
                                UserInfoCacheMap.put("banned-".concat(postUid), dataSnapshot.child("banned").getValue(String.class));
                                UserInfoCacheMap.put("nickname-".concat(postUid), dataSnapshot.child("nickname").getValue(String.class));
                                UserInfoCacheMap.put("username-".concat(postUid), dataSnapshot.child("username").getValue(String.class));
                                UserInfoCacheMap.put("avatar-".concat(postUid), dataSnapshot.child("avatar").getValue(String.class));
                                UserInfoCacheMap.put("gender-".concat(postUid), dataSnapshot.child("gender").getValue(String.class));
                                UserInfoCacheMap.put("verify-".concat(postUid), dataSnapshot.child("verify").getValue(String.class));
                                UserInfoCacheMap.put("acc_type-".concat(postUid), dataSnapshot.child("account_type").getValue(String.class));
                                mMainHandler.post(() -> {
                                    _updatePostViewVisibility(body, postPrivateStateIcon, postUid, _data.get(_position).get("post_visibility").toString());
                                    _displayUserInfoFromCache(postUid, userInfoProfileImage, userInfoUsername, userInfoGenderBadge, userInfoUsernameVerifiedBadge);
                                });
                            } else {
                                mMainHandler.post(() -> {
                                    userInfoProfileImage.setImageResource(R.drawable.avatar);
                                    userInfoUsername.setText("Unknown User");
                                    userInfoGenderBadge.setVisibility(View.GONE);
                                    userInfoUsernameVerifiedBadge.setVisibility(View.GONE);
                                    body.setVisibility(View.GONE);
                                });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            mMainHandler.post(() -> {
                                userInfoProfileImage.setImageResource(R.drawable.avatar);
                                userInfoUsername.setText("Error User");
                                userInfoGenderBadge.setVisibility(View.GONE);
                                userInfoUsernameVerifiedBadge.setVisibility(View.GONE);
                                body.setVisibility(View.GONE);
                            });
                        }
                    });
                });
            }

            DatabaseReference getLikeCheck = _firebase.getReference("skyline/posts-likes").child(_data.get(_position).get("key").toString()).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            DatabaseReference getCommentsCount = _firebase.getReference("skyline/posts-comments").child(_data.get(_position).get("key").toString());
            DatabaseReference getLikesCount = _firebase.getReference("skyline/posts-likes").child(_data.get(_position).get("key").toString());
            DatabaseReference getFavoriteCheck = _firebase.getReference("skyline/favorite-posts").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(_data.get(_position).get("key").toString());

            getLikeCheck.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        likeButtonIc.setImageResource(R.drawable.post_icons_1_2);
                    } else {
                        likeButtonIc.setImageResource(R.drawable.post_icons_1_1);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
            getCommentsCount.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    _setCount(commentsButtonCount, dataSnapshot.getChildrenCount());
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
            getLikesCount.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long count = dataSnapshot.getChildrenCount();
                    _setCount(likeButtonCount, count);
                    postLikeCountCache.put(_data.get(_position).get("key").toString(), String.valueOf(count));
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
            getFavoriteCheck.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        favoritePostButton.setImageResource(R.drawable.delete_favorite_post_ic);
                    } else {
                        favoritePostButton.setImageResource(R.drawable.add_favorite_post_ic);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });

            likeButton.setOnClickListener(_view1 -> {
                DatabaseReference likeRef = _firebase.getReference("skyline/posts-likes").child(_data.get(_position).get("key").toString()).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                likeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            likeRef.removeValue();
                            double currentLikes = Double.parseDouble(postLikeCountCache.get(_data.get(_position).get("key").toString()).toString());
                            postLikeCountCache.put(_data.get(_position).get("key").toString(), String.valueOf((long)(currentLikes - 1)));
                            _setCount(likeButtonCount, currentLikes - 1);
                            likeButtonIc.setImageResource(R.drawable.post_icons_1_1);
                        } else {
                            likeRef.setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            com.synapse.social.studioasinc.util.NotificationUtils.sendPostLikeNotification(_data.get(_position).get("key").toString(), _data.get(_position).get("uid").toString());
                            double currentLikes = Double.parseDouble(postLikeCountCache.get(_data.get(_position).get("key").toString()).toString());
                            postLikeCountCache.put(_data.get(_position).get("key").toString(), String.valueOf((long)(currentLikes + 1)));
                            _setCount(likeButtonCount, currentLikes + 1);
                            likeButtonIc.setImageResource(R.drawable.post_icons_1_2);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });
                vbr.vibrate((long)(24));
            });
            commentsButton.setOnClickListener(_view1 -> {
                Bundle sendPostKey = new Bundle();
                sendPostKey.putString("postKey", _data.get(_position).get("key").toString());
                sendPostKey.putString("postPublisherUID", _data.get(_position).get("uid").toString());
                sendPostKey.putString("postPublisherAvatar", UserInfoCacheMap.get("avatar-".concat(_data.get(_position).get("uid").toString())).toString());
                PostCommentsBottomSheetDialog postCommentsBottomSheet = new PostCommentsBottomSheetDialog();
                postCommentsBottomSheet.setArguments(sendPostKey);
                postCommentsBottomSheet.show(getParentFragmentManager(), postCommentsBottomSheet.getTag());
            });
            userInfoProfileImage.setOnClickListener(_view1 -> {
                intent.setClass(getContext(), ProfileActivity.class);
                intent.putExtra("uid", _data.get(_position).get("uid").toString());
                startActivity(intent);
            });
            favoritePostButton.setOnClickListener(_view1 -> {
                DatabaseReference favoriteRef = _firebase.getReference("skyline/favorite-posts").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(_data.get(_position).get("key").toString());
                favoriteRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            favoriteRef.removeValue();
                            favoritePostButton.setImageResource(R.drawable.add_favorite_post_ic);
                        } else {
                            favoriteRef.setValue(_data.get(_position).get("key").toString());
                            favoritePostButton.setImageResource(R.drawable.delete_favorite_post_ic);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });
                vbr.vibrate((long)(24));
            });
            topMoreButton.setOnClickListener(_view1 -> {
                Bundle sendPostKey = new Bundle();
                sendPostKey.putString("postKey", _data.get(_position).get("key").toString());
                sendPostKey.putString("postPublisherUID", _data.get(_position).get("uid").toString());
                sendPostKey.putString("postType", _data.get(_position).get("post_type").toString());
                if (_data.get(_position).containsKey("post_text") && _data.get(_position).get("post_text") != null) {
                    sendPostKey.putString("postText", _data.get(_position).get("post_text").toString());
                } else {
                    sendPostKey.putString("postText", "");
                }
                if (_data.get(_position).containsKey("post_image") && _data.get(_position).get("post_image") != null && !_data.get(_position).get("post_image").toString().isEmpty()) {
                    sendPostKey.putString("postImg", _data.get(_position).get("post_image").toString());
                }
                PostMoreBottomSheetDialog postMoreBottomSheetDialog = new PostMoreBottomSheetDialog();
                postMoreBottomSheetDialog.setArguments(sendPostKey);
                postMoreBottomSheetDialog.show(getParentFragmentManager(), postMoreBottomSheetDialog.getTag());
            });
        }
        @Override
        public int getItemCount() {
            return _data.size();
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View v) {
                super(v);
            }
        }
    }

    private void _updatePostViewVisibility(LinearLayout body, ImageView postPrivateStateIcon, String postUid, String postVisibility) {
        if ("private".equals(postVisibility)) {
            if (postUid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                postPrivateStateIcon.setVisibility(View.VISIBLE);
                body.setVisibility(View.VISIBLE);
            } else {
                body.setVisibility(View.GONE);
            }
        } else {
            body.setVisibility(View.VISIBLE);
            postPrivateStateIcon.setVisibility(View.GONE);
        }
    }

    private void _displayUserInfoFromCache(String postUid, ImageView userInfoProfileImage, TextView userInfoUsername, ImageView userInfoGenderBadge, ImageView userInfoUsernameVerifiedBadge) {
        if (UserInfoCacheMap.get("banned-".concat(postUid)).toString().equals("true")) {
            userInfoProfileImage.setImageResource(R.drawable.banned_avatar);
        } else {
            if (UserInfoCacheMap.get("avatar-".concat(postUid)).toString().equals("null")) {
                userInfoProfileImage.setImageResource(R.drawable.avatar);
            } else {
                Glide.with(getContext()).load(Uri.parse(UserInfoCacheMap.get("avatar-".concat(postUid)).toString())).into(userInfoProfileImage);
            }
        }

        if (UserInfoCacheMap.get("nickname-".concat(postUid)).toString().equals("null")) {
            userInfoUsername.setText("@" + UserInfoCacheMap.get("username-".concat(postUid)).toString());
        } else {
            userInfoUsername.setText(UserInfoCacheMap.get("nickname-".concat(postUid)).toString());
        }

        if (UserInfoCacheMap.get("gender-".concat(postUid)).toString().equals("hidden")) {
            userInfoGenderBadge.setVisibility(View.GONE);
        } else {
            if (UserInfoCacheMap.get("gender-".concat(postUid)).toString().equals("male")) {
                userInfoGenderBadge.setImageResource(R.drawable.male_badge);
                userInfoGenderBadge.setVisibility(View.VISIBLE);
            } else if (UserInfoCacheMap.get("gender-".concat(postUid)).toString().equals("female")) {
                userInfoGenderBadge.setImageResource(R.drawable.female_badge);
                userInfoGenderBadge.setVisibility(View.VISIBLE);
            }
        }

        String accountType = UserInfoCacheMap.get("acc_type-".concat(postUid)).toString();
        if ("admin".equals(accountType)) {
            userInfoUsernameVerifiedBadge.setImageResource(R.drawable.admin_badge);
            userInfoUsernameVerifiedBadge.setVisibility(View.VISIBLE);
        } else if ("moderator".equals(accountType)) {
            userInfoUsernameVerifiedBadge.setImageResource(R.drawable.moderator_badge);
            userInfoUsernameVerifiedBadge.setVisibility(View.VISIBLE);
        } else if ("support".equals(accountType)) {
            userInfoUsernameVerifiedBadge.setImageResource(R.drawable.support_badge);
            userInfoUsernameVerifiedBadge.setVisibility(View.VISIBLE);
        } else if ("user".equals(accountType)) {
            if (UserInfoCacheMap.get("verify-".concat(postUid)).toString().equals("true")) {
                userInfoUsernameVerifiedBadge.setVisibility(View.VISIBLE);
            } else {
                userInfoUsernameVerifiedBadge.setVisibility(View.GONE);
            }
        }
    }
}
