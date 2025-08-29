package com.synapse.social.studioasinc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class HomeFeedFragment extends Fragment {

    private FirebaseDatabase _firebase;
    private DatabaseReference udb;
    private DatabaseReference postsRef;
    private DatabaseReference storiesDbRef;

    private HashMap<String, Object> createPostMap = new HashMap<>();
    private String currentPostFilter = "PUBLIC"; 
    
    private ArrayList<HashMap<String, Object>> storiesList = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> PostsList = new ArrayList<>(); 
    
    // UI Components
    private LinearLayout body;
    private SwipeRefreshLayout swipeLayout;
    private LinearLayout stories;
    private LinearLayout miniPostLayout;
    private RecyclerView storiesView;
    private EditText miniPostLayoutTextPostInput;
    private ImageView miniPostLayoutImagePost;
    private ImageView miniPostLayoutVideoPost;
    private TextView miniPostLayoutTextPostPublish;
    private LinearLayout PublicPostsBody;
    private RecyclerView PublicPostsList;
    private TextView PublicPostsListNotFound;
    private ProgressBar loading_bar;
    
    private Intent intent = new Intent();
    private Vibrator vbr;
    private FirebaseAuth auth;
    private ChildEventListener _udb_child_listener;
    private Calendar cc = Calendar.getInstance();
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_feed, container, false);
        
        FirebaseApp.initializeApp(getActivity());
        _firebase = FirebaseDatabase.getInstance();
        udb = _firebase.getReference("skyline/users");
        postsRef = _firebase.getReference("skyline/posts");
        storiesDbRef = _firebase.getReference("skyline/stories");

        postsRef.keepSynced(true);
        storiesDbRef.keepSynced(true);

        initialize(view);
        initializeLogic();
        
        return view;
    }
    
    private void initialize(View view) {
        body = view.findViewById(R.id.body);
        swipeLayout = view.findViewById(R.id.swipeLayout);
        stories = view.findViewById(R.id.stories);
        miniPostLayout = view.findViewById(R.id.miniPostLayout);
        storiesView = view.findViewById(R.id.storiesView);
        miniPostLayoutTextPostInput = view.findViewById(R.id.miniPostLayoutTextPostInput);
        miniPostLayoutImagePost = view.findViewById(R.id.miniPostLayoutImagePost);
        miniPostLayoutVideoPost = view.findViewById(R.id.miniPostLayoutVideoPost);
        miniPostLayoutTextPostPublish = view.findViewById(R.id.miniPostLayoutTextPostPublish);
        PublicPostsBody = view.findViewById(R.id.PublicPostsBody);
        PublicPostsList = view.findViewById(R.id.PublicPostsList);
        PublicPostsListNotFound = view.findViewById(R.id.PublicPostsListNotFound);
        loading_bar = view.findViewById(R.id.loading_bar);
        
        vbr = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        auth = FirebaseAuth.getInstance();
        
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                _loadPosts(currentPostFilter); 
                _loadStories();
            }
        });
        
        miniPostLayoutTextPostInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
                final String _charSeq = _param1.toString();
                if (_charSeq.length() == 0) {
                    miniPostLayoutTextPostPublish.setVisibility(View.GONE);
                } else {
                    miniPostLayoutTextPostPublish.setVisibility(View.VISIBLE);
                }
            }
            
            @Override
            public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
            }
            
            @Override
            public void afterTextChanged(Editable _param1) {
            }
        });
        
        miniPostLayoutImagePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                intent.setClass(getActivity(), CreatePostActivity.class);
                startActivity(intent);
            }
        });
        
        miniPostLayoutVideoPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                intent.setClass(getActivity(), CreateLineVideoActivity.class);
                startActivity(intent);
            }
        });
        
        miniPostLayoutTextPostPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                if (miniPostLayoutTextPostInput.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.please_enter_text), Toast.LENGTH_SHORT).show();
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
                        FirebaseDatabase.getInstance().getReference("skyline/posts").child(uniqueKey).updateChildren(createPostMap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    Toast.makeText(getActivity(), getResources().getString(R.string.post_publish_success), Toast.LENGTH_SHORT).show();
                                    currentPostFilter = "PUBLIC"; 
                                    _loadPosts(currentPostFilter);
                                } else {
                                    Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        
                        miniPostLayoutTextPostInput.setText("");
                    }
                }
                vbr.vibrate((long)(48));
            }
        });
        
        _udb_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DatabaseReference _param1, DataSnapshot _param2) {
            }
            
            @Override
            public void onChildChanged(DatabaseReference _param1, DataSnapshot _param2) {
            }
            
            @Override
            public void onChildMoved(DatabaseReference _param1, DataSnapshot _param2) {
            }
            
            @Override
            public void onChildRemoved(DatabaseReference _param1) {
            }
            
            @Override
            public void onCancelled(DatabaseError _param1) {
            }
        };
        udb.addChildEventListener(_udb_child_listener);
    }
    
    private void initializeLogic() {
        _loadPosts(currentPostFilter);
        _loadStories();
    }
    
    private void _loadPosts(final String _filter) {
        // Implementation from HomeActivity
        // This should load posts based on the filter
        // For now, just a placeholder
    }
    
    private void _loadStories() {
        // Implementation from HomeActivity
        // This should load stories
        // For now, just a placeholder
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (_udb_child_listener != null) {
            udb.removeEventListener(_udb_child_listener);
        }
    }
}