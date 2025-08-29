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
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.chip.Chip;
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

public class HomeFragment extends Fragment implements PublicPostsListAdapter.OnHeaderInteractionListener {

    private FirebaseDatabase _firebase;
    private DatabaseReference udb;
    private DatabaseReference postsRef;
    private DatabaseReference storiesDbRef;

    private HashMap<String, Object> createPostMap = new HashMap<>();
    private HashMap<String, Object> postLikeCountCache = new HashMap<>();
    private HashMap<String, Object> UserInfoCacheMap = new HashMap<>();
    private HashMap<String, Object> postFavoriteCountCache = new HashMap<>();
    private String currentPostFilter = "PUBLIC";

    private ArrayList<Object> feedItems = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> storiesList = new ArrayList<>();

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
        loadingBody = view.findViewById(R.id.loadingBody);
        swipeLayout = view.findViewById(R.id.swipeLayout);
        PublicPostsList = view.findViewById(R.id.PublicPostsList);
        PublicPostsListNotFound = view.findViewById(R.id.PublicPostsListNotFound);
		loading_bar = view.findViewById(R.id.loading_bar);

        vbr = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        auth = FirebaseAuth.getInstance();

        swipeLayout.setOnRefreshListener(() -> {
            _loadFeed();
        });
    }

    private void initializeLogic() {
        PublicPostsList.setLayoutManager(new LinearLayoutManager(getContext()));
        PublicPostsList.setAdapter(new PublicPostsListAdapter(feedItems, this));
        _loadFeed();
    }

    @Override
    public void onPublishPost(String text) {
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
                        _loadFeed();
                    } else {
                        Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        vbr.vibrate((long)(48));
    }

    @Override
    public void onFilterChanged(String filter) {
        currentPostFilter = filter;
        _loadPosts(currentPostFilter);
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

    private void _loadFeed() {
        swipeLayout.setRefreshing(true);
        feedItems.clear();
        feedItems.add(new HeaderItem());
        feedItems.add(new StoriesItem());
        _loadStories();
        _loadPosts(currentPostFilter);
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
                        // Instead of notifying storiesView adapter, we find the StoriesItem and update it
                        for (Object item : feedItems) {
                            if (item instanceof StoriesItem) {
                                ((StoriesItem) item).stories = storiesList;
                                break;
                            }
                        }
                        PublicPostsList.getAdapter().notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), "Error loading stories: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void _loadPosts(final String filterType) {
        Query query = null;
        String notFoundMessage = "There are no public posts available at the moment.";

        // Clear only posts from feedItems
        feedItems.removeIf(item -> item instanceof HashMap);


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
                            _finalizePostDisplay("No regional posts found or your region is not set.");
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), "Error fetching user region, showing cached data if available: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        _finalizePostDisplay("Error loading regional posts (showing cached if available).");
                    }
                });
                break;
            case "FOLLOWED":
                _firebase.getReference("skyline/following").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            final List<String> followedUids = new ArrayList<>();
                            for (DataSnapshot followSnap : dataSnapshot.getChildren()) {
                                followedUids.add(followSnap.getKey());
                            }

                            if (followedUids.isEmpty()) {
                                _finalizePostDisplay("You are not following any users yet. Follow users to see their posts here.");
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
                                                        feedItems.add(_map);
                                                    }
                                                }
                                                completedQueries[0]++;
                                                if (completedQueries[0] == totalQueries) {
                                                    _finalizePostDisplay("No posts from followed users found.");
                                                }
                                            });
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            mainHandler.post(() -> {
                                                completedQueries[0]++;
                                                Toast.makeText(getContext(), "Error loading followed users' posts, showing cached data if available: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                                if (completedQueries[0] == totalQueries) {
                                                    _finalizePostDisplay("Error loading followed users' posts (showing cached if available).");
                                                }
                                            });
                                        }
                                    });
                                });
                            }
                        } else {
                            _finalizePostDisplay("You are not following any users yet. Follow users to see their posts here.");
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), "Error fetching followed users keys, showing cached data if available: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        _finalizePostDisplay("Error loading followed users' posts (showing cached if available).");
                    }
                });
                break;
            case "FAVORITE":
                _firebase.getReference("skyline/favorite-posts").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            final List<String> favoritePostKeys = new ArrayList<>();
                            for (DataSnapshot favSnap : dataSnapshot.getChildren()) {
                                favoritePostKeys.add(favSnap.getKey());
                            }

                            if (favoritePostKeys.isEmpty()) {
                                _finalizePostDisplay("You have no saved posts yet.");
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
                                                    feedItems.add(_map);
                                                }
                                                completedQueries[0]++;
                                                if (completedQueries[0] == totalQueries) {
                                                    _finalizePostDisplay("You have no saved posts yet.");
                                                }
                                            });
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            mainHandler.post(() -> {
                                                completedQueries[0]++;
                                                Toast.makeText(getContext(), "Error loading saved posts, showing cached data if available: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                                if (completedQueries[0] == totalQueries) {
                                                    _finalizePostDisplay("Error loading saved posts (showing cached if available).");
                                                }
                                            });
                                        }
                                    });
                                });
                            }
                        } else {
                            _finalizePostDisplay("You have no saved posts yet.");
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), "Error fetching favorite posts keys, showing cached data if available: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        _finalizePostDisplay("Error loading saved posts (showing cached if available).");
                    }
                });
                break;
        }
    }

    private void _fetchAndDisplayPosts(Query query, final String notFoundMessage) {
        if (query == null) {
            _finalizePostDisplay(notFoundMessage);
            return;
        }

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {
                if (_dataSnapshot.exists()) {
                    try {
                        GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                        for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                            HashMap<String, Object> _map = _data.getValue(_ind);
                            feedItems.add(_map);
                        }
                    } catch (Exception _e) {
                        _e.printStackTrace();
                    }
                    _finalizePostDisplay(notFoundMessage);
                } else {
                    _finalizePostDisplay(notFoundMessage);
                }
            }

            @Override
            public void onCancelled(DatabaseError _databaseError) {
                Toast.makeText(getContext(), "Failed to fetch latest posts, showing cached data. Error: " + _databaseError.getMessage(), Toast.LENGTH_LONG).show();
                _finalizePostDisplay(notFoundMessage);
            }
        });
    }

    private void _finalizePostDisplay(String notFoundMessage) {
        // Sort only the posts, not the header or stories
        ArrayList<HashMap<String, Object>> posts = new ArrayList<>();
        for (Object item : feedItems) {
            if (item instanceof HashMap) {
                posts.add((HashMap<String, Object>) item);
            }
        }
        Collections.sort(posts, (o1, o2) -> {
            long date1 = Long.parseLong(o1.get("publish_date").toString());
            long date2 = Long.parseLong(o2.get("publish_date").toString());
            return Long.compare(date2, date1);
        });

        // Clear and re-add sorted posts
        feedItems.removeIf(item -> item instanceof HashMap);
        feedItems.addAll(posts);


        if (PublicPostsList.getAdapter() == null) {
            PublicPostsList.setAdapter(new PublicPostsListAdapter(feedItems));
        } else {
            PublicPostsList.getAdapter().notifyDataSetChanged();
        }

        if (posts.isEmpty()) {
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

    public class HeaderItem {}
    public class StoriesItem {
        public ArrayList<HashMap<String, Object>> stories = new ArrayList<>();
    }

    public class PublicPostsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int VIEW_TYPE_HEADER = 0;
        private static final int VIEW_TYPE_STORIES = 1;
        private static final int VIEW_TYPE_POST = 2;

        private ArrayList<Object> items;
        private OnHeaderInteractionListener mListener;

        public interface OnHeaderInteractionListener {
            void onPublishPost(String text);
            void onFilterChanged(String filter);
        }

        public PublicPostsListAdapter(ArrayList<Object> items, OnHeaderInteractionListener listener) {
            this.items = items;
            this.mListener = listener;
        }

        @Override
        public int getItemViewType(int position) {
            Object item = items.get(position);
            if (item instanceof HeaderItem) {
                return VIEW_TYPE_HEADER;
            } else if (item instanceof StoriesItem) {
                return VIEW_TYPE_STORIES;
            } else {
                return VIEW_TYPE_POST;
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            if (viewType == VIEW_TYPE_HEADER) {
                View view = inflater.inflate(R.layout.list_item_header, parent, false);
                return new HeaderViewHolder(view);
            } else if (viewType == VIEW_TYPE_STORIES) {
                View view = inflater.inflate(R.layout.list_item_stories, parent, false);
                return new StoriesViewHolder(view);
            } else {
                View view = inflater.inflate(R.layout.synapse_post_cv, parent, false);
                return new PostViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            int viewType = getItemViewType(position);
            if (viewType == VIEW_TYPE_HEADER) {
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                // Setup header views and listeners
                headerViewHolder.miniPostLayoutTextPostInput.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
                        final String _charSeq = _param1.toString();
                        if (_charSeq.length() == 0) {
                            headerViewHolder.miniPostLayoutTextPostPublish.setVisibility(View.GONE);
                        } else {
                            _viewGraphics(headerViewHolder.miniPostLayoutTextPostPublish, getResources().getColor(R.color.colorPrimary), 0xFFC5CAE9, 300, 0, Color.TRANSPARENT);
                            headerViewHolder.miniPostLayoutTextPostPublish.setVisibility(View.VISIBLE);
                        }
                    }
                    @Override
                    public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {}
                    @Override
                    public void afterTextChanged(Editable _param1) {}
                });
                headerViewHolder.miniPostLayoutImagePost.setOnClickListener(v -> {
                    intent.setClass(getContext(), CreatePostActivity.class);
                    startActivity(intent);
                });
                headerViewHolder.miniPostLayoutVideoPost.setOnClickListener(v -> {
                    intent.setClass(getContext(), CreateLineVideoActivity.class);
                    startActivity(intent);
                });
                 headerViewHolder.miniPostLayoutTextPostPublish.setOnClickListener(v -> {
                    if (mListener != null) {
                        mListener.onPublishPost(headerViewHolder.miniPostLayoutTextPostInput.getText().toString());
                        headerViewHolder.miniPostLayoutTextPostInput.setText("");
                    }
                 });
                DatabaseReference getReference = udb.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                getReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            if (dataSnapshot.child("avatar").getValue(String.class) != null && !dataSnapshot.child("avatar").getValue(String.class).equals("null")) {
                                Glide.with(getContext()).load(Uri.parse(dataSnapshot.child("avatar").getValue(String.class))).into(headerViewHolder.miniPostLayoutProfileImage);
                            } else {
                                headerViewHolder.miniPostLayoutProfileImage.setImageResource(R.drawable.avatar);
                            }
                        } else {
                            headerViewHolder.miniPostLayoutProfileImage.setImageResource(R.drawable.avatar);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), "Error fetching user profile: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        headerViewHolder.miniPostLayoutProfileImage.setImageResource(R.drawable.avatar);
                    }
                });

                headerViewHolder.filterChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
                    if (mListener != null) {
                        String filter = "PUBLIC";
                        if (checkedId == R.id.chip_ai_based) {
                            filter = "LOCAL";
                        } else if (checkedId == R.id.chip_global) {
                            filter = "PUBLIC";
                        } else if (checkedId == R.id.chip_following) {
                            filter = "FOLLOWED";
                        } else if (checkedId == R.id.chip_saved_posts) {
                            filter = "FAVORITE";
                        }
                        mListener.onFilterChanged(filter);
                    }
                });

            } else if (viewType == VIEW_TYPE_STORIES) {
                StoriesViewHolder storiesViewHolder = (StoriesViewHolder) holder;
                StoriesItem storiesItem = (StoriesItem) items.get(position);
                storiesViewHolder.storiesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                storiesViewHolder.storiesRecyclerView.setAdapter(new StoriesViewAdapter(storiesItem.stories));
            } else {
                PostViewHolder postViewHolder = (PostViewHolder) holder;
                HashMap<String, Object> postData = (HashMap<String, Object>) items.get(position);

                postViewHolder.body.setVisibility(View.GONE);
                postViewHolder.userInfoProfileCard.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)300, Color.TRANSPARENT));
                _ImageColor(postViewHolder.postPrivateStateIcon, 0xFF616161);
                _viewGraphics(postViewHolder.topMoreButton, 0xFFFFFFFF, 0xFFEEEEEE, 300, 0, Color.TRANSPARENT);

                postViewHolder.postImage.setVisibility(View.GONE);

                if (postData.containsKey("post_text")) {
                    String postText = postData.get("post_text").toString();
                    com.synapse.social.studioasinc.styling.MarkdownRenderer.get(postViewHolder.postMessageTextMiddle.getContext()).render(postViewHolder.postMessageTextMiddle, postText);
                    postViewHolder.postMessageTextMiddle.setVisibility(View.VISIBLE);
                } else {
                    postViewHolder.postMessageTextMiddle.setVisibility(View.GONE);
                }

                if (postData.containsKey("post_image")) {
                    Glide.with(getContext()).load(Uri.parse(postData.get("post_image").toString())).into(postViewHolder.postImage);
                    postViewHolder.postImage.setOnClickListener(_view1 -> _OpenWebView(postData.get("post_image").toString()));
                    postViewHolder.postImage.setVisibility(View.VISIBLE);
                } else {
                    postViewHolder.postImage.setVisibility(View.GONE);
                }

                if (postData.get("post_hide_like_count").toString().equals("true")) {
                    postViewHolder.likeButtonCount.setVisibility(View.GONE);
                } else {
                    postViewHolder.likeButtonCount.setVisibility(View.VISIBLE);
                }
                if (postData.get("post_hide_comments_count").toString().equals("true")) {
                    postViewHolder.commentsButtonCount.setVisibility(View.GONE);
                } else {
                    postViewHolder.commentsButtonCount.setVisibility(View.VISIBLE);
                }
                if (postData.get("post_disable_comments").toString().equals("true")) {
                    postViewHolder.commentsButton.setVisibility(View.GONE);
                } else {
                    postViewHolder.commentsButton.setVisibility(View.VISIBLE);
                }
                _setTime(Double.parseDouble(postData.get("publish_date").toString()), postViewHolder.postPublishDate);

                final String postUid = postData.get("uid").toString();
                if (UserInfoCacheMap.containsKey("uid-".concat(postUid))) {
                    _updatePostViewVisibility(postViewHolder.body, postViewHolder.postPrivateStateIcon, postUid, postData.get("post_visibility").toString());
                    _displayUserInfoFromCache(postUid, postViewHolder.userInfoProfileImage, postViewHolder.userInfoUsername, postViewHolder.userInfoGenderBadge, postViewHolder.userInfoUsernameVerifiedBadge);
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
                                        _updatePostViewVisibility(body, postPrivateStateIcon, postUid, postData.get("post_visibility").toString());
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

                DatabaseReference getLikeCheck = _firebase.getReference("skyline/posts-likes").child(postData.get("key").toString()).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                DatabaseReference getCommentsCount = _firebase.getReference("skyline/posts-comments").child(postData.get("key").toString());
                DatabaseReference getLikesCount = _firebase.getReference("skyline/posts-likes").child(postData.get("key").toString());
                DatabaseReference getFavoriteCheck = _firebase.getReference("skyline/favorite-posts").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(postData.get("key").toString());

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
                        postLikeCountCache.put(postData.get("key").toString(), String.valueOf(count));
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
                    DatabaseReference likeRef = _firebase.getReference("skyline/posts-likes").child(postData.get("key").toString()).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    likeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                likeRef.removeValue();
                                double currentLikes = Double.parseDouble(postLikeCountCache.get(postData.get("key").toString()).toString());
                                postLikeCountCache.put(postData.get("key").toString(), String.valueOf((long)(currentLikes - 1)));
                                _setCount(likeButtonCount, currentLikes - 1);
                                likeButtonIc.setImageResource(R.drawable.post_icons_1_1);
                            } else {
                                likeRef.setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                com.synapse.social.studioasinc.util.NotificationUtils.sendPostLikeNotification(postData.get("key").toString(), postData.get("uid").toString());
                                double currentLikes = Double.parseDouble(postLikeCountCache.get(postData.get("key").toString()).toString());
                                postLikeCountCache.put(postData.get("key").toString(), String.valueOf((long)(currentLikes + 1)));
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
                    sendPostKey.putString("postKey", postData.get("key").toString());
                    sendPostKey.putString("postPublisherUID", postData.get("uid").toString());
                    sendPostKey.putString("postPublisherAvatar", UserInfoCacheMap.get("avatar-".concat(postData.get("uid").toString())).toString());
                    PostCommentsBottomSheetDialog postCommentsBottomSheet = new PostCommentsBottomSheetDialog();
                    postCommentsBottomSheet.setArguments(sendPostKey);
                    postCommentsBottomSheet.show(getParentFragmentManager(), postCommentsBottomSheet.getTag());
                });
                userInfoProfileImage.setOnClickListener(_view1 -> {
                    intent.setClass(getContext(), ProfileActivity.class);
                    intent.putExtra("uid", postData.get("uid").toString());
                    startActivity(intent);
                });
                favoritePostButton.setOnClickListener(_view1 -> {
                    DatabaseReference favoriteRef = _firebase.getReference("skyline/favorite-posts").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(postData.get("key").toString());
                    favoriteRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                favoriteRef.removeValue();
                                favoritePostButton.setImageResource(R.drawable.add_favorite_post_ic);
                            } else {
                                favoriteRef.setValue(postData.get("key").toString());
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
                    sendPostKey.putString("postKey", postData.get("key").toString());
                    sendPostKey.putString("postPublisherUID", postData.get("uid").toString());
                    sendPostKey.putString("postType", postData.get("post_type").toString());
                    if (postData.containsKey("post_text") && postData.get("post_text") != null) {
                        sendPostKey.putString("postText", postData.get("post_text").toString());
                    } else {
                        sendPostKey.putString("postText", "");
                    }
                    if (postData.containsKey("post_image") && postData.get("post_image") != null && !postData.get("post_image").toString().isEmpty()) {
                        sendPostKey.putString("postImg", postData.get("post_image").toString());
                    }
                    PostMoreBottomSheetDialog postMoreBottomSheetDialog = new PostMoreBottomSheetDialog();
                    postMoreBottomSheetDialog.setArguments(sendPostKey);
                    postMoreBottomSheetDialog.show(getParentFragmentManager(), postMoreBottomSheetDialog.getTag());
                });
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class HeaderViewHolder extends RecyclerView.ViewHolder {
            EditText miniPostLayoutTextPostInput;
            TextView miniPostLayoutTextPostPublish;
            ImageView miniPostLayoutProfileImage;
            ImageView miniPostLayoutImagePost;
            ImageView miniPostLayoutVideoPost;
            ChipGroup filterChipGroup;

            public HeaderViewHolder(@NonNull View itemView) {
                super(itemView);
                miniPostLayoutTextPostInput = itemView.findViewById(R.id.miniPostLayoutTextPostInput);
                miniPostLayoutTextPostPublish = itemView.findViewById(R.id.miniPostLayoutTextPostPublish);
                miniPostLayoutProfileImage = itemView.findViewById(R.id.miniPostLayoutProfileImage);
                miniPostLayoutImagePost = itemView.findViewById(R.id.miniPostLayoutImagePost);
                miniPostLayoutVideoPost = itemView.findViewById(R.id.miniPostLayoutVideoPost);
                filterChipGroup = itemView.findViewById(R.id.filterChipGroup);
            }
        }

        public class StoriesViewHolder extends RecyclerView.ViewHolder {
            RecyclerView storiesRecyclerView;
            public StoriesViewHolder(@NonNull View itemView) {
                super(itemView);
                storiesRecyclerView = itemView.findViewById(R.id.storiesView);
            }
        }

        public class PostViewHolder extends RecyclerView.ViewHolder {
            final LinearLayout body;
            final ImageView topMoreButton;
            final androidx.cardview.widget.CardView userInfoProfileCard;
            final ImageView userInfoProfileImage;
            final TextView userInfoUsername;
            final ImageView userInfoGenderBadge;
            final ImageView userInfoUsernameVerifiedBadge;
            final TextView postPublishDate;
            final ImageView postPrivateStateIcon;
            final TextView postMessageTextMiddle;
            final ImageView postImage;
            final LinearLayout likeButton;
            final LinearLayout commentsButton;
            final ImageView favoritePostButton;
            final ImageView likeButtonIc;
            final TextView likeButtonCount;
            final TextView commentsButtonCount;

            public PostViewHolder(View v) {
                super(v);
                body = v.findViewById(R.id.body);
                topMoreButton = v.findViewById(R.id.topMoreButton);
                userInfoProfileCard = v.findViewById(R.id.userInfoProfileCard);
                userInfoProfileImage = v.findViewById(R.id.userInfoProfileImage);
                userInfoUsername = v.findViewById(R.id.userInfoUsername);
                userInfoGenderBadge = v.findViewById(R.id.userInfoGenderBadge);
                userInfoUsernameVerifiedBadge = v.findViewById(R.id.userInfoUsernameVerifiedBadge);
                postPublishDate = v.findViewById(R.id.postPublishDate);
                postPrivateStateIcon = v.findViewById(R.id.postPrivateStateIcon);
                postMessageTextMiddle = v.findViewById(R.id.postMessageTextMiddle);
                postImage = v.findViewById(R.id.postImage);
                likeButton = v.findViewById(R.id.likeButton);
                commentsButton = v.findViewById(R.id.commentsButton);
                favoritePostButton = v.findViewById(R.id.favoritePostButton);
                likeButtonIc = v.findViewById(R.id.likeButtonIc);
                likeButtonCount = v.findViewById(R.id.likeButtonCount);
                commentsButtonCount = v.findViewById(R.id.commentsButtonCount);
            }
        }
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
