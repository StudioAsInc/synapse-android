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
        public void onBindViewHolder(ViewHolder _holder, final int _position) {
            View _view = _holder.itemView;

            final androidx.cardview.widget.CardView cardview1 = _view.findViewById(R.id.cardview1);
            final LinearLayout main = _view.findViewById(R.id.main);
            final LinearLayout body = _view.findViewById(R.id.body);
            final LinearLayout spcBottom = _view.findViewById(R.id.spcBottom);
            final RelativeLayout profileCardRelative = _view.findViewById(R.id.profileCardRelative);
            final LinearLayout lin = _view.findViewById(R.id.lin);
            final androidx.cardview.widget.CardView profileCard = _view.findViewById(R.id.profileCard);
            final LinearLayout ProfileRelativeUp = _view.findViewById(R.id.ProfileRelativeUp);
            final ImageView profileCardImage = _view.findViewById(R.id.profileCardImage);
            final LinearLayout userStatusCircleBG = _view.findViewById(R.id.userStatusCircleBG);
            final LinearLayout userStatusCircleIN = _view.findViewById(R.id.userStatusCircleIN);
            final LinearLayout usr = _view.findViewById(R.id.usr);
            final LinearLayout btnss = _view.findViewById(R.id.btnss);
            final LinearLayout spc = _view.findViewById(R.id.spc);
            final TextView push = _view.findViewById(R.id.push);
            final TextView username = _view.findViewById(R.id.username);
            final ImageView genderBadge = _view.findViewById(R.id.genderBadge);
            final ImageView verifiedBadge = _view.findViewById(R.id.verifiedBadge);
            final TextView last_message = _view.findViewById(R.id.last_message);
            final ImageView message_state = _view.findViewById(R.id.message_state);
            final TextView unread_messages_count_badge = _view.findViewById(R.id.unread_messages_count_badge);

            try{
                RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                _view.setLayoutParams(_lp);
                _viewGraphics(main, 0xFFFFFFFF, 0xFFEEEEEE, 0, 0, Color.TRANSPARENT);
                userStatusCircleBG.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)300, 0xFFFFFFFF));
                userStatusCircleIN.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)300, 0xFF388E3C));
                unread_messages_count_badge.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)300, getResources().getColor(R.color.colorPrimary)));
                unread_messages_count_badge.setVisibility(View.GONE);
                main.setVisibility(View.GONE);
                if (_data.get((int)_position).get("last_message_text").toString().equals("null")) {
                    last_message.setText(getResources().getString(R.string.m_no_chats));
                } else {
                    last_message.setText(_data.get((int)_position).get("last_message_text").toString());
                }
                if (_data.get((int)_position).get("last_message_uid").toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    if (_data.get((int)_position).get("last_message_state").toString().equals("sended")) {
                        message_state.setImageResource(R.drawable.icon_done_round);
                    } else {
                        message_state.setImageResource(R.drawable.icon_done_all_round);
                    }
                    last_message.setTextColor(0xFF616161);
                    push.setTextColor(0xFF616161);
                    message_state.setVisibility(View.VISIBLE);
                    unread_messages_count_badge.setVisibility(View.GONE);
                } else {
                    message_state.setVisibility(View.GONE);
                    // CRITICAL FIX: Use fragment's managed ExecutorService and track listeners
                    if (FragInboxChatsActivity.this.mExecutorService != null && !FragInboxChatsActivity.this.mExecutorService.isShutdown()) {
                        String otherUid = _data.get((int)_position).get("uid").toString();

                        // CRITICAL FIX: Remove any existing listener for this user before adding new one
                        ValueEventListener existingListener = unreadCountListeners.get(otherUid);
                        if (existingListener != null) {
                            Query existingQuery = FirebaseDatabase.getInstance().getReference("skyline/chats")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child(otherUid)
                                .orderByChild("message_state").equalTo("sended");
                            existingQuery.removeEventListener(existingListener);
                            unreadCountListeners.remove(otherUid);
                        }

                        FragInboxChatsActivity.this.mExecutorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                Query getUnreadMessagesCount = FirebaseDatabase.getInstance().getReference("skyline/chats").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(otherUid).orderByChild("message_state").equalTo("sended");

                                ValueEventListener unreadListener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        // CRITICAL FIX: Use fragment's managed Handler and check if fragment is still attached
                                        if (FragInboxChatsActivity.this.mMainHandler != null && isAdded() && getContext() != null) {
                                            FragInboxChatsActivity.this.mMainHandler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    // CRITICAL FIX: Additional check before UI updates
                                                    if (!isAdded() || getContext() == null) {
                                                        return;
                                                    }
                                                    long unReadMessageCount = dataSnapshot.getChildrenCount();
                                                    if(dataSnapshot.exists()) {
                                                        last_message.setTextColor(0xFF000000);
                                                        push.setTextColor(0xFF000000);
                                                        //    last_message.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/appfont.ttf"), 1);
                                                        //    push.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/appfont.ttf"), 1);
                                                        unread_messages_count_badge.setText(String.valueOf((long)(unReadMessageCount)));
                                                        unread_messages_count_badge.setVisibility(View.VISIBLE);
                                                    } else {
                                                        last_message.setTextColor(0xFF616161);
                                                        push.setTextColor(0xFF616161);
                                                        //    last_message.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/appfont.ttf"), 0);
                                                        //    push.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/appfont.ttf"), 0);
                                                        unread_messages_count_badge.setVisibility(View.GONE);
                                                    }
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                };

                                // CRITICAL FIX: Store listener reference and add to Firebase
                                unreadCountListeners.put(otherUid, unreadListener);
                                getUnreadMessagesCount.addValueEventListener(unreadListener);
                            }
                        });
                    }
                }
                _setTime(Double.parseDouble(_data.get((int)_position).get("push_date").toString()), push);
                if (UserInfoCacheMap.containsKey("uid-".concat(_data.get((int)_position).get("uid").toString()))) {
                    main.setVisibility(View.VISIBLE);

                    // Get uid once to avoid repeated calls
                    String uid = _data.get((int)_position).get("uid").toString();

                    // Handle banned status with null check
                    Object bannedObj = UserInfoCacheMap.get("banned-".concat(uid));
                    if (bannedObj != null && bannedObj.toString().equals("true")) {
                        profileCardImage.setImageResource(R.drawable.banned_avatar);
                    } else {
                        // Handle avatar with null check
                        Object avatarObj = UserInfoCacheMap.get("avatar-".concat(uid));
                        if (avatarObj == null || avatarObj.toString().equals("null")) {
                            profileCardImage.setImageResource(R.drawable.avatar);
                        } else {
                            // CRITICAL FIX: Use proper context for Glide and add lifecycle checks
                            if (isAdded() && getContext() != null) {
                                try {
                                    Glide.with(FragInboxChatsActivity.this)
                                    .load(Uri.parse(avatarObj.toString()))
                                    .into(profileCardImage);
                                } catch (Exception e) {
                                    Log.e("FragInboxChatsActivity", "Error loading avatar: " + e.getMessage());
                                    profileCardImage.setImageResource(R.drawable.avatar);
                                }
                            } else {
                                profileCardImage.setImageResource(R.drawable.avatar);
                            }
                        }
                    }

                    // Handle nickname with null check
                    Object nicknameObj = UserInfoCacheMap.get("nickname-".concat(uid));
                    if (nicknameObj == null || nicknameObj.toString().equals("null")) {
                        Object usernameObj = UserInfoCacheMap.get("username-".concat(uid));
                        username.setText("@" + (usernameObj != null ? usernameObj.toString() : "unknown"));
                    } else {
                        username.setText(nicknameObj.toString());
                    }

                    // Handle status with null check
                    Object statusObj = UserInfoCacheMap.get("status-".concat(uid));
                    userStatusCircleBG.setVisibility(statusObj != null && statusObj.toString().equals("online")
                    ? View.VISIBLE : View.GONE);

                    // Handle gender with null check
                    Object genderObj = UserInfoCacheMap.get("gender-".concat(uid));
                    if (genderObj == null || genderObj.toString().equals("hidden")) {
                        genderBadge.setVisibility(View.GONE);
                    } else {
                        genderBadge.setVisibility(View.VISIBLE);
                        String gender = genderObj.toString();
                        if (gender.equals("male")) {
                            genderBadge.setImageResource(R.drawable.male_badge);
                        } else if (gender.equals("female")) {
                            genderBadge.setImageResource(R.drawable.female_badge);
                        }
                    }

                    // Handle account type and badges with null checks
                    Object accountTypeObj = UserInfoCacheMap.get("account_type-".concat(uid));
                    Object premiumObj = UserInfoCacheMap.get("account_premium-".concat(uid));
                    Object verifyObj = UserInfoCacheMap.get("verify-".concat(uid));

                    if (accountTypeObj != null) {
                        String accountType = accountTypeObj.toString();
                        if (accountType.equals("admin")) {
                            verifiedBadge.setImageResource(R.drawable.admin_badge);
                            verifiedBadge.setVisibility(View.VISIBLE);
                        } else if (accountType.equals("moderator")) {
                            verifiedBadge.setImageResource(R.drawable.moderator_badge);
                            verifiedBadge.setVisibility(View.VISIBLE);
                        } else if (accountType.equals("support")) {
                            verifiedBadge.setImageResource(R.drawable.support_badge);
                            verifiedBadge.setVisibility(View.VISIBLE);
                        } else if (premiumObj != null && premiumObj.toString().equals("true")) {
                            verifiedBadge.setImageResource(R.drawable.premium_badge);
                            verifiedBadge.setVisibility(View.VISIBLE);
                        } else if (verifyObj != null && verifyObj.toString().equals("true")) {
                            verifiedBadge.setImageResource(R.drawable.verified_badge);
                            verifiedBadge.setVisibility(View.VISIBLE);
                        } else {
                            verifiedBadge.setVisibility(View.GONE);
                        }
                    } else if (premiumObj != null && premiumObj.toString().equals("true")) {
                        verifiedBadge.setImageResource(R.drawable.premium_badge);
                        verifiedBadge.setVisibility(View.VISIBLE);
                    } else if (verifyObj != null && verifyObj.toString().equals("true")) {
                        verifiedBadge.setImageResource(R.drawable.verified_badge);
                        verifiedBadge.setVisibility(View.VISIBLE);
                    } else {
                        verifiedBadge.setVisibility(View.GONE);
                    }
                } else {
                    // CRITICAL FIX: Use fragment's managed ExecutorService instead of creating new ones
                    if (FragInboxChatsActivity.this.mExecutorService != null && !FragInboxChatsActivity.this.mExecutorService.isShutdown()) {
                        FragInboxChatsActivity.this.mExecutorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                DatabaseReference getUserReference = FirebaseDatabase.getInstance().getReference("skyline/users").child(_data.get((int)_position).get("uid").toString());
                                getUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        // CRITICAL FIX: Use fragment's managed Handler and check fragment state
                                        if (FragInboxChatsActivity.this.mMainHandler != null && isAdded() && getContext() != null) {
                                            FragInboxChatsActivity.this.mMainHandler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    // CRITICAL FIX: Additional check before UI updates
                                                    if (!isAdded() || getContext() == null) {
                                                        return;
                                                    }
                                                    if(dataSnapshot.exists()) {
                                                    UserInfoCacheMap.put("uid-".concat(_data.get((int)_position).get("uid").toString()), _data.get((int)_position).get("uid").toString());
                                                    UserInfoCacheMap.put("avatar-".concat(_data.get((int)_position).get("uid").toString()), dataSnapshot.child("avatar").getValue(String.class));
                                                    UserInfoCacheMap.put("banned-".concat(_data.get((int)_position).get("uid").toString()), dataSnapshot.child("banned").getValue(String.class));
                                                    UserInfoCacheMap.put("username-".concat(_data.get((int)_position).get("uid").toString()), dataSnapshot.child("username").getValue(String.class));
                                                    UserInfoCacheMap.put("nickname-".concat(_data.get((int)_position).get("uid").toString()), dataSnapshot.child("nickname").getValue(String.class));
                                                    UserInfoCacheMap.put("status-".concat(_data.get((int)_position).get("uid").toString()), dataSnapshot.child("status").getValue(String.class));
                                                    UserInfoCacheMap.put("gender-".concat(_data.get((int)_position).get("uid").toString()), dataSnapshot.child("gender").getValue(String.class));
                                                    UserInfoCacheMap.put("account_type-".concat(_data.get((int)_position).get("uid").toString()), dataSnapshot.child("account_type").getValue(String.class));
                                                    UserInfoCacheMap.put("account_premium-".concat(_data.get((int)_position).get("uid").toString()), dataSnapshot.child("account_premium").getValue(String.class));
                                                    UserInfoCacheMap.put("verify-".concat(_data.get((int)_position).get("uid").toString()), dataSnapshot.child("verify").getValue(String.class));
                                                    main.setVisibility(View.VISIBLE);
                                                    if (dataSnapshot.child("banned").getValue(String.class).equals("true")) {
                                                        profileCardImage.setImageResource(R.drawable.banned_avatar);
                                                    } else {
                                                        if (dataSnapshot.child("avatar").getValue(String.class).equals("null")) {
                                                            profileCardImage.setImageResource(R.drawable.avatar);
                                                        } else {
                                                            // CRITICAL FIX: Use proper context for Glide and add lifecycle checks
                                                            if (isAdded() && getContext() != null) {
                                                                try {
                                                                    Glide.with(FragInboxChatsActivity.this).load(Uri.parse(dataSnapshot.child("avatar").getValue(String.class))).into(profileCardImage);
                                                                } catch (Exception e) {
                                                                    Log.e("FragInboxChatsActivity", "Error loading avatar from Firebase: " + e.getMessage());
                                                                    profileCardImage.setImageResource(R.drawable.avatar);
                                                                }
                                                            } else {
                                                                profileCardImage.setImageResource(R.drawable.avatar);
                                                            }
                                                        }
                                                    }
                                                    if (dataSnapshot.child("nickname").getValue(String.class).equals("null")) {
                                                        username.setText("@" + dataSnapshot.child("username").getValue(String.class));
                                                    } else {
                                                        username.setText(dataSnapshot.child("nickname").getValue(String.class));
                                                    }
                                                    if (dataSnapshot.child("status").getValue(String.class).equals("online")) {
                                                        userStatusCircleBG.setVisibility(View.VISIBLE);
                                                    } else {
                                                        userStatusCircleBG.setVisibility(View.GONE);
                                                    }
                                                    if (dataSnapshot.child("gender").getValue(String.class).equals("hidden")) {
                                                        genderBadge.setVisibility(View.GONE);
                                                    } else {
                                                        if (dataSnapshot.child("gender").getValue(String.class).equals("male")) {
                                                            genderBadge.setImageResource(R.drawable.male_badge);
                                                            genderBadge.setVisibility(View.VISIBLE);
                                                        } else {
                                                            if (dataSnapshot.child("gender").getValue(String.class).equals("female")) {
                                                                genderBadge.setImageResource(R.drawable.female_badge);
                                                                genderBadge.setVisibility(View.VISIBLE);
                                                            }
                                                        }
                                                    }
                                                    if (dataSnapshot.child("account_type").getValue(String.class).equals("admin")) {
                                                        verifiedBadge.setImageResource(R.drawable.admin_badge);
                                                        verifiedBadge.setVisibility(View.VISIBLE);
                                                    } else {
                                                        if (dataSnapshot.child("account_type").getValue(String.class).equals("moderator")) {
                                                            verifiedBadge.setImageResource(R.drawable.moderator_badge);
                                                            verifiedBadge.setVisibility(View.VISIBLE);
                                                        } else {
                                                            if (dataSnapshot.child("account_type").getValue(String.class).equals("support")) {
                                                                verifiedBadge.setImageResource(R.drawable.support_badge);
                                                                verifiedBadge.setVisibility(View.VISIBLE);
                                                            } else {
                                                                if (dataSnapshot.child("account_premium").getValue(String.class).equals("true")) {
                                                                    verifiedBadge.setImageResource(R.drawable.premium_badge);
                                                                    verifiedBadge.setVisibility(View.VISIBLE);
                                                                } else {
                                                                    if (dataSnapshot.child("verify").getValue(String.class).equals("true")) {
                                                                        verifiedBadge.setImageResource(R.drawable.verified_badge);
                                                                        verifiedBadge.setVisibility(View.VISIBLE);
                                                                    } else {
                                                                        verifiedBadge.setVisibility(View.GONE);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                } else {
                                                }
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Log.e("FragInboxChatsActivity", "Unread count listener cancelled: " + databaseError.getMessage());
                                    }
                                });
                            }
                        });
                    }

                }
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
}
