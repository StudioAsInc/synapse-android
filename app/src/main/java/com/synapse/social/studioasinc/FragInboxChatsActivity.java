package com.synapse.social.studioasinc;

import android.animation.*;
import android.app.*;
import android.content.*;
import android.content.Intent;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.os.Bundle;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.*;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.gridlayout.*;
import androidx.recyclerview.widget.*;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.*;
import com.google.android.material.chip.ChipGroup;
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
import com.theartofdev.edmodo.cropper.*;
import com.yalantis.ucrop.*;
import java.io.*;
import java.io.InputStream;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.*;
import org.json.*;
import androidx.core.widget.NestedScrollView;
import com.google.firebase.database.Query;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.appcompat.app.AppCompatDelegate;
import com.bumptech.glide.Glide;

public class FragInboxChatsActivity extends Fragment {

    private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();

    private HashMap<String, Object> UserInfoCacheMap = new HashMap<>();

    private ArrayList<HashMap<String, Object>> ChatInboxList = new ArrayList<>();

    private LinearLayout linear2;
    private HorizontalScrollView hscroll1;
    private RecyclerView inboxListRecyclerView;
    private ChipGroup linear9;
    private Chip linear10;
    private Chip linear29;
    private Chip linear30;
    private Chip linear31;
    private Chip linear32;
    private Chip linear33;

    private FirebaseAuth auth;
    private OnCompleteListener<AuthResult> _auth_create_user_listener;
    private OnCompleteListener<AuthResult> _auth_sign_in_listener;
    private OnCompleteListener<Void> _auth_reset_password_listener;
    private OnCompleteListener<Void> auth_updateEmailListener;
    private OnCompleteListener<Void> auth_updatePasswordListener;
    private OnCompleteListener<Void> auth_emailVerificationSentListener;
    private OnCompleteListener<Void> auth_deleteUserListener;
    private OnCompleteListener<Void> auth_updateProfileListener;
    private OnCompleteListener<AuthResult> auth_phoneAuthListener;
    private OnCompleteListener<AuthResult> auth_googleSignInListener;
    private DatabaseReference main = _firebase.getReference("skyline");
    private ChildEventListener _main_child_listener;
    private Intent intent = new Intent();

    // CRITICAL FIX: Add ExecutorService for proper thread management
    private ExecutorService mExecutorService;
    private Handler mMainHandler;
    private ValueEventListener inboxValueEventListener;
    // CRITICAL FIX: Track ValueEventListeners to prevent leaks
    private final HashMap<String, ValueEventListener> unreadCountListeners = new HashMap<>();

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater _inflater, @Nullable ViewGroup _container, @Nullable Bundle _savedInstanceState) {
        View _view = _inflater.inflate(R.layout.frag_inbox_chats, _container, false);
        initialize(_savedInstanceState, _view);
        FirebaseApp.initializeApp(getContext());

        // CRITICAL FIX: Initialize ExecutorService and Handler properly
        mExecutorService = Executors.newSingleThreadExecutor();
        mMainHandler = new Handler(Looper.getMainLooper());

        initializeLogic();
        return _view;
    }

    private void initialize(Bundle _savedInstanceState, View _view) {
        linear2 = _view.findViewById(R.id.linear2);
        hscroll1 = _view.findViewById(R.id.hscroll1);
        inboxListRecyclerView = _view.findViewById(R.id.inboxListRecyclerView);
        linear9 = _view.findViewById(R.id.linear9);
        linear10 = _view.findViewById(R.id.linear10);
        linear29 = _view.findViewById(R.id.linear29);
        linear30 = _view.findViewById(R.id.linear30);
        linear31 = _view.findViewById(R.id.linear31);
        linear32 = _view.findViewById(R.id.linear32);
        linear33 = _view.findViewById(R.id.linear33);
        auth = FirebaseAuth.getInstance();

        _main_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);

            }

            @Override
            public void onChildChanged(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);

            }

            @Override
            public void onChildMoved(DataSnapshot _param1, String _param2) {

            }

            @Override
            public void onChildRemoved(DataSnapshot _param1) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);

            }

            @Override
            public void onCancelled(DatabaseError _param1) {
                final int _errorCode = _param1.getCode();
                final String _errorMessage = _param1.getMessage();

            }
        };
        main.addChildEventListener(_main_child_listener);

        auth_updateEmailListener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> _param1) {
                final boolean _success = _param1.isSuccessful();
                final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";

            }
        };

        auth_updatePasswordListener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> _param1) {
                final boolean _success = _param1.isSuccessful();
                final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";

            }
        };

        auth_emailVerificationSentListener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> _param1) {
                final boolean _success = _param1.isSuccessful();
                final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";

            }
        };

        auth_deleteUserListener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> _param1) {
                final boolean _success = _param1.isSuccessful();
                final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";

            }
        };

        auth_phoneAuthListener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                final boolean _success = task.isSuccessful();
                final String _errorMessage = task.getException() != null ? task.getException().getMessage() : "";

            }
        };

        auth_updateProfileListener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> _param1) {
                final boolean _success = _param1.isSuccessful();
                final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";

            }
        };

        auth_googleSignInListener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                final boolean _success = task.isSuccessful();
                final String _errorMessage = task.getException() != null ? task.getException().getMessage() : "";

            }
        };

        _auth_create_user_listener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> _param1) {
                final boolean _success = _param1.isSuccessful();
                final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";

            }
        };

        _auth_sign_in_listener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> _param1) {
                final boolean _success = _param1.isSuccessful();
                final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";

            }
        };

        _auth_reset_password_listener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> _param1) {
                final boolean _success = _param1.isSuccessful();

            }
        };
    }

    private void initializeLogic() {
        inboxListRecyclerView.setAdapter(new InboxListRecyclerViewAdapter(ChatInboxList));
        inboxListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        _getInboxReference();
    }

    public void _ImgRound(final ImageView _imageview, final double _value) {
        android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable ();
        gd.setColor(android.R.color.transparent);
        gd.setCornerRadius((int)_value);
        _imageview.setClipToOutline(true);
        _imageview.setBackground(gd);
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


    public void _getInboxReference() {
        // CRITICAL FIX: Store listener reference for proper cleanup
        Query getInboxRef = FirebaseDatabase.getInstance().getReference("skyline/inbox").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        inboxValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // CRITICAL FIX: Check if fragment is still attached before processing
                if (!isAdded() || getContext() == null) {
                    return;
                }

                if(dataSnapshot.exists()) {
                    if (inboxListRecyclerView != null) {
                        inboxListRecyclerView.setVisibility(View.VISIBLE);
                    }
                    ChatInboxList.clear();
                    try {
                        GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                        for (DataSnapshot _data : dataSnapshot.getChildren()) {
                            HashMap<String, Object> _map = _data.getValue(_ind);
                            if (_map != null) { // CRITICAL FIX: Null check
                                ChatInboxList.add(_map);
                            }
                        }
                    } catch (Exception _e) {
                        Log.e("FragInboxChatsActivity", "Error processing inbox data: " + _e.getMessage());
                        _e.printStackTrace();
                    }

                    try {
                        SketchwareUtil.sortListMap(ChatInboxList, "push_date", false, false);
                        if (inboxListRecyclerView != null && inboxListRecyclerView.getAdapter() != null) {
                            inboxListRecyclerView.getAdapter().notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        Log.e("FragInboxChatsActivity", "Error updating adapter: " + e.getMessage());
                    }
                } else {
                    if (inboxListRecyclerView != null) {
                        inboxListRecyclerView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FragInboxChatsActivity", "Inbox listener cancelled: " + databaseError.getMessage());
            }
        };
        getInboxRef.addValueEventListener(inboxValueEventListener);
    }


    public void _viewGraphics(final View _view, final int _onFocus, final int _onRipple, final double _radius, final double _stroke, final int _strokeColor) {
        android.graphics.drawable.GradientDrawable GG = new android.graphics.drawable.GradientDrawable();
        GG.setColor(_onFocus);
        GG.setCornerRadius((float)_radius);
        GG.setStroke((int) _stroke, _strokeColor);
        android.graphics.drawable.RippleDrawable RE = new android.graphics.drawable.RippleDrawable(new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{ _onRipple}), GG, null);
        _view.setBackground(RE);
    }


    public void _ImageColor(final ImageView _image, final int _color) {
        _image.setColorFilter(_color,PorterDuff.Mode.SRC_ATOP);
    }

    // CRITICAL FIX: Add proper lifecycle management
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // CRITICAL FIX: Clean up Firebase listeners
        if (inboxValueEventListener != null) {
            try {
                Query getInboxRef = FirebaseDatabase.getInstance().getReference("skyline/inbox").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                getInboxRef.removeEventListener(inboxValueEventListener);
                inboxValueEventListener = null;
            } catch (Exception e) {
                Log.e("FragInboxChatsActivity", "Error removing inbox listener: " + e.getMessage());
            }
        }

        // CRITICAL FIX: Clean up all unread count listeners
        for (Map.Entry<String, ValueEventListener> entry : unreadCountListeners.entrySet()) {
            try {
                String otherUid = entry.getKey();
                ValueEventListener listener = entry.getValue();
                Query query = FirebaseDatabase.getInstance().getReference("skyline/chats")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(otherUid)
                    .orderByChild("message_state").equalTo("sended");
                query.removeEventListener(listener);
                Log.d("FragInboxChatsActivity", "Removed unread count listener for uid: " + otherUid);
            } catch (Exception e) {
                Log.e("FragInboxChatsActivity", "Error removing unread count listener: " + e.getMessage());
            }
        }
        unreadCountListeners.clear();

        if (_main_child_listener != null) {
            try {
                main.removeEventListener(_main_child_listener);
                _main_child_listener = null;
            } catch (Exception e) {
                Log.e("FragInboxChatsActivity", "Error removing main listener: " + e.getMessage());
            }
        }

        // CRITICAL FIX: Clean up ExecutorService
        if (mExecutorService != null && !mExecutorService.isShutdown()) {
            try {
                mExecutorService.shutdown();
                if (!mExecutorService.awaitTermination(1, java.util.concurrent.TimeUnit.SECONDS)) {
                    mExecutorService.shutdownNow();
                }
            } catch (Exception e) {
                Log.e("FragInboxChatsActivity", "Error shutting down executor: " + e.getMessage());
                mExecutorService.shutdownNow();
            }
            mExecutorService = null;
        }

        // CRITICAL FIX: Clean up Handler
        if (mMainHandler != null) {
            mMainHandler.removeCallbacksAndMessages(null);
            mMainHandler = null;
        }

        // CRITICAL FIX: Clear data structures
        if (ChatInboxList != null) {
            ChatInboxList.clear();
        }

        if (UserInfoCacheMap != null) {
            UserInfoCacheMap.clear();
        }

        // CRITICAL FIX: Nullify view references
        inboxListRecyclerView = null;

        Log.d("FragInboxChatsActivity", "Fragment cleanup completed");
    }

    public class InboxListRecyclerViewAdapter extends RecyclerView.Adapter<InboxListRecyclerViewAdapter.ViewHolder> {

        ArrayList<HashMap<String, Object>> _data;

        public InboxListRecyclerViewAdapter(ArrayList<HashMap<String, Object>> _arr) {
            _data = _arr;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater _inflater = getActivity().getLayoutInflater();
            View _v = _inflater.inflate(R.layout.inbox_msg_list_cv_synapse, null);
            RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            _v.setLayoutParams(_lp);
            return new ViewHolder(_v);
        }

        @Override
        public void onBindViewHolder(ViewHolder _holder, int _position) {
            try {
                View _view = _holder.itemView;
                
                LinearLayout main = _view.findViewById(R.id.main);
                ImageView circleimageview1 = _view.findViewById(R.id.circleimageview);
                TextView textview1 = _view.findViewById(R.id.textview1);
                TextView textview2 = _view.findViewById(R.id.textview2);
                TextView textview3 = _view.findViewById(R.id.textview3);
                ImageView verifiedBadge = _view.findViewById(R.id.imageview2);
                TextView unreadCountText = _view.findViewById(R.id.textview3);
                
                // Set user data
                textview1.setText(_data.get((int)_position).get("display_name").toString());
                textview2.setText(_data.get((int)_position).get("last_message").toString());
                textview3.setText(_data.get((int)_position).get("timestamp").toString());
                
                // Load profile image
                String useruid = _data.get((int)_position).get("uid").toString();
                if (useruid != null && !useruid.isEmpty()) {
                    DatabaseReference userRef = _firebase.getReference("users").child(useruid);
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String profileImage = dataSnapshot.child("image").getValue(String.class);
                                if (profileImage != null && !profileImage.isEmpty()) {
                                    if (getContext() != null) {
                                        Glide.with(getContext()).load(profileImage).into(circleimageview1);
                                    }
                                }
                                
                                // Handle verified badge
                                Boolean isVerified = dataSnapshot.child("verified").getValue(Boolean.class);
                                if (isVerified != null && isVerified) {
                                    verifiedBadge.setVisibility(View.VISIBLE);
                                } else {
                                    verifiedBadge.setVisibility(View.GONE);
                                }
                            }
                        }
                        
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("FragInboxChatsActivity", "Profile load cancelled: " + databaseError.getMessage());
                        }
                    });
                    
                    // Setup unread count listener
                    String chatId = getChatId(FirebaseAuth.getInstance().getCurrentUser().getUid(), useruid);
                    DatabaseReference unreadRef = _firebase.getReference("chats").child(chatId).child("unread_count").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    
                    ValueEventListener unreadListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Long unreadCount = dataSnapshot.getValue(Long.class);
                            if (unreadCount != null && unreadCount > 0) {
                                unreadCountText.setVisibility(View.VISIBLE);
                                unreadCountText.setText(String.valueOf(unreadCount));
                            } else {
                                unreadCountText.setVisibility(View.GONE);
                            }
                        }
                        
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("FragInboxChatsActivity", "Unread count listener cancelled: " + databaseError.getMessage());
                        }
                    };
                    
                    unreadRef.addValueEventListener(unreadListener);
                    unreadCountListeners.put(useruid, unreadListener);
                }
                
                // Set click listener
                main.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View _view) {
                        intent.setClass(getContext().getApplicationContext(), ChatActivity.class);
                        intent.putExtra("uid", _data.get((int)_position).get("uid").toString());
                        intent.putExtra("origin", "InboxActivity");
                        startActivity(intent);
                    }
                });
                
            } catch(Exception e) {
                Log.e("FragInboxChatsActivity", "Error in onBindViewHolder: " + e.getMessage());
            }
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

    private String getChatId(String uid1, String uid2) {
        return uid1.compareTo(uid2) < 0 ? uid1 + "_" + uid2 : uid2 + "_" + uid1;
    }
}