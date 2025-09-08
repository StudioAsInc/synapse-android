package com.synapse.social.studioasinc;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.browser.customtabs.CustomTabsIntent;

public class UserPostsFragment extends Fragment {

    private String uid;
    private RecyclerView recyclerView;
    private TextView noPostsSubtitle;
    private ArrayList<HashMap<String, Object>> userPostsList = new ArrayList<>();
    private HashMap<String, Object> UserInfoCacheMap = new HashMap<>();
    private HashMap<String, Object> postLikeCountCache = new HashMap<>();
    private Vibrator vbr;
    private Intent intent = new Intent();
    private String handle = "";
    private String object_clicked = "";
    private String AndroidDevelopersBlogURL = "";


    public static UserPostsFragment newInstance(String uid) {
        UserPostsFragment fragment = new UserPostsFragment();
        Bundle args = new Bundle();
        args.putString("uid", uid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uid = getArguments().getString("uid");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.ProfilePageTabUserPostsRecyclerView);
        noPostsSubtitle = view.findViewById(R.id.ProfilePageTabUserPostsNoPostsSubtitle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        vbr = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        _getUserPostsReference();
    }

    public void _getUserPostsReference() {
        Query getUserPostsRef = FirebaseDatabase.getInstance().getReference("skyline/posts").orderByChild("uid").equalTo(uid);
        getUserPostsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    recyclerView.setVisibility(View.VISIBLE);
                    noPostsSubtitle.setVisibility(View.GONE);
                    userPostsList.clear();
                    try {
                        GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                        for (DataSnapshot _data : dataSnapshot.getChildren()) {
                            HashMap<String, Object> _map = _data.getValue(_ind);
                            userPostsList.add(_map);
                        }
                    } catch (Exception _e) {
                        _e.printStackTrace();
                    }
                    SketchwareUtil.sortListMap(userPostsList, "publish_date", false, false);
                    recyclerView.setAdapter(new ProfilePageTabUserPostsRecyclerViewAdapter(userPostsList));
                } else {
                    recyclerView.setVisibility(View.GONE);
                    noPostsSubtitle.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

    public void _textview_mh(final TextView _txt, final String _value) {
        _txt.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
        //_txt.setTextIsSelectable(true);
        updateSpan(_value, _txt);
    }

    private void updateSpan(String str, TextView _txt){
        SpannableStringBuilder ssb = new SpannableStringBuilder(str);
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(?<![^\\s])(([@]{1}|[#]{1})([A-Za-z0-9_-]\\.?)+)(?![^\\s,])|\\*\\*(.*?)\\*\\*|__(.*?)__|~~(.*?)~~|_(.*?)_|\\*(.*?)\\*|///(.*?)///");
        java.util.regex.Matcher matcher = pattern.matcher(str);
        int offset = 0;

        while (matcher.find()) {
            int start = matcher.start() + offset;
            int end = matcher.end() + offset;

            if (matcher.group(3) != null) {
                // For mentions or hashtags
                ProfileSpan span = new ProfileSpan();
                ssb.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (matcher.group(4) != null) {
                // For bold text (**bold**)
                String boldText = matcher.group(4); // Extract text inside **
                ssb.replace(start, end, boldText);
                ssb.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), start, start + boldText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                offset -= 4; // Update offset for bold text replacement
            } else if (matcher.group(5) != null) {
                // For italic text (__italic__)
                String italicText = matcher.group(5);
                ssb.replace(start, end, italicText);
                ssb.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.ITALIC), start, start + italicText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                offset -= 4; // Update offset for italic text replacement
            } else if (matcher.group(6) != null) {
                // For strikethrough text (~~strikethrough~~)
                String strikethroughText = matcher.group(6);
                ssb.replace(start, end, strikethroughText);
                ssb.setSpan(new android.text.style.StrikethroughSpan(), start, start + strikethroughText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                offset -= 4; // Update offset for strikethrough text replacement
            } else if (matcher.group(7) != null) {
                // For underline text (_underline_)
                String underlineText = matcher.group(7);
                ssb.replace(start, end, underlineText);
                ssb.setSpan(new android.text.style.UnderlineSpan(), start, start + underlineText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                offset -= 2; // Update offset for underline text replacement
            } else if (matcher.group(8) != null) {
                // For italic text (*italic*)
                String italicText = matcher.group(8);
                ssb.replace(start, end, italicText);
                ssb.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.ITALIC), start, start + italicText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                offset -= 2; // Update offset for italic text replacement
            } else if (matcher.group(9) != null) {
                // For bold-italic text (///bold-italic///)
                String boldItalicText = matcher.group(9);
                ssb.replace(start, end, boldItalicText);
                ssb.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD_ITALIC), start, start + boldItalicText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                offset -= 6; // Update offset for bold-italic text replacement
            }
        }
        _txt.setText(ssb);
    }

    private class ProfileSpan extends android.text.style.ClickableSpan{


        @Override
        public void onClick(View view){

            if(view instanceof TextView){
                TextView tv = (TextView)view;

                if(tv.getText() instanceof Spannable){
                    Spannable sp = (Spannable)tv.getText();

                    int start = sp.getSpanStart(this);
                    int end = sp.getSpanEnd(this);
                    object_clicked = sp.subSequence(start,end).toString();
                    handle = object_clicked.replace("@", "");
                    DatabaseReference getReference = FirebaseDatabase.getInstance().getReference()
                    .child("synapse/username")
                    .child(handle);  // This points directly to "synapse/username/[handle]"
                    getReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                if (!dataSnapshot.child("uid").getValue(String.class).equals("null")) {
                                    intent.setClass(getContext(), ProfileActivity.class);
                                    intent.putExtra("uid", dataSnapshot.child("uid").getValue(String.class));
                                    startActivity(intent);
                                } else {

                                }
                            } else {
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            //  swipeLayout.setVisibility(View.GONE);
                            //noInternetBody.setVisibility(View.VISIBLE);
                            //  loadingBody.setVisibility(View.GONE);
                        }
                    });
                }
            }

        }
        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
            ds.setColor(Color.parseColor("#445E91"));
            ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        }
    }

    public void _OpenWebView(final String _URL) {
        AndroidDevelopersBlogURL = _URL;
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(Color.parseColor("#242D39"));
        CustomTabsIntent customtabsintent = builder.build();
        customtabsintent.launchUrl(getContext(), Uri.parse(AndroidDevelopersBlogURL));
    }


    public class ProfilePageTabUserPostsRecyclerViewAdapter extends RecyclerView.Adapter<ProfilePageTabUserPostsRecyclerViewAdapter.ViewHolder> {

        ArrayList<HashMap<String, Object>> _data;

        public ProfilePageTabUserPostsRecyclerViewAdapter(ArrayList<HashMap<String, Object>> _arr) {
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
            final LinearLayout top = _view.findViewById(R.id.top);
            final LinearLayout linear1 = _view.findViewById(R.id.linear1);
            final LinearLayout middle = _view.findViewById(R.id.middle);
            final LinearLayout bottom = _view.findViewById(R.id.bottom);
            final LinearLayout userInfo = _view.findViewById(R.id.userInfo);
            final LinearLayout topSpace = _view.findViewById(R.id.topSpace);
            final ImageView topMoreButton = _view.findViewById(R.id.topMoreButton);
            final androidx.cardview.widget.CardView userInfoProfileCard = _view.findViewById(R.id.userInfoProfileCard);
            final LinearLayout userInfoRight = _view.findViewById(R.id.userInfoRight);
            final ImageView userInfoProfileImage = _view.findViewById(R.id.userInfoProfileImage);
            final LinearLayout userInfoUsernameRightTop = _view.findViewById(R.id.userInfoUsernameRightTop);
            final LinearLayout btmPost = _view.findViewById(R.id.btmPost);
            final TextView userInfoUsername = _view.findViewById(R.id.userInfoUsername);
            final ImageView userInfoGenderBadge = _view.findViewById(R.id.userInfoGenderBadge);
            final ImageView userInfoUsernameVerifiedBadge = _view.findViewById(R.id.userInfoUsernameVerifiedBadge);
            final TextView postPublishDate = _view.findViewById(R.id.postPublishDate);
            final ImageView postPrivateStateIcon = _view.findViewById(R.id.postPrivateStateIcon);
            final TextView postMessageTextMiddle = _view.findViewById(R.id.postMessageTextMiddle);
            final ImageView postImage = _view.findViewById(R.id.postImage);
            final LinearLayout likeButton = _view.findViewById(R.id.likeButton);
            final LinearLayout commentsButton = _view.findViewById(R.id.commentsButton);
            final LinearLayout shareButton = _view.findViewById(R.id.shareButton);
            final LinearLayout bottomSpc = _view.findViewById(R.id.bottomSpc);
            final ImageView favoritePostButton = _view.findViewById(R.id.favoritePostButton);
            final ImageView likeButtonIc = _view.findViewById(R.id.likeButtonIc);
            final TextView likeButtonCount = _view.findViewById(R.id.likeButtonCount);
            final TextView textview1 = _view.findViewById(R.id.textview1);
            final TextView tv_1 = _view.findViewById(R.id.tv_1);
            final ImageView commentsButtonIc = _view.findViewById(R.id.commentsButtonIc);
            final TextView commentsButtonCount = _view.findViewById(R.id.commentsButtonCount);
            final TextView textview2 = _view.findViewById(R.id.textview2);
            final TextView tv_2 = _view.findViewById(R.id.tv_2);
            final ImageView shareButtonIc = _view.findViewById(R.id.shareButtonIc);
            final TextView shareButtonCount = _view.findViewById(R.id.shareButtonCount);

            RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            _view.setLayoutParams(_lp);
            body.setVisibility(View.GONE);
            if (_data.get((int)_position).get("post_type").toString().equals("TEXT") || (_data.get((int)_position).get("post_type").toString().equals("IMAGE") || _data.get((int)_position).get("post_type").toString().equals("VIDEO"))) {
                if (_data.get((int)_position).containsKey("post_text")) {
                    //      postMessageTextMiddle.setText(_data.get((int)_position).get("post_text").toString());
                    //postMessageTextMiddle.setText(_data.get((int)_position).get("post_text").toString());
                    _textview_mh(postMessageTextMiddle, _data.get((int)_position).get("post_text").toString());

                    postMessageTextMiddle.setVisibility(View.VISIBLE);
                } else {
                    postMessageTextMiddle.setVisibility(View.GONE);
                }
                if (_data.get((int)_position).containsKey("post_image")) {
                    Glide.with(getContext()).load(Uri.parse(_data.get((int)_position).get("post_image").toString())).into(postImage);
                    postImage.setVisibility(View.VISIBLE);
                } else {
                    postImage.setVisibility(View.GONE);
                }
            } else {
                postImage.setVisibility(View.GONE);
                postMessageTextMiddle.setVisibility(View.GONE);
            }

            if (_data.get((int)_position).get("post_hide_like_count").toString().equals("true")) {
                likeButtonCount.setVisibility(View.GONE);
            } else {
                likeButtonCount.setVisibility(View.VISIBLE);
            }
            if (_data.get((int)_position).get("post_hide_comments_count").toString().equals("true")) {
                commentsButtonCount.setVisibility(View.GONE);
            } else {
                commentsButtonCount.setVisibility(View.VISIBLE);
            }
            if (_data.get((int)_position).get("post_disable_comments").toString().equals("true")) {
                commentsButton.setVisibility(View.GONE);
            } else {
                commentsButton.setVisibility(View.VISIBLE);
            }
            _setTime(Double.parseDouble(_data.get((int)_position).get("publish_date").toString()), postPublishDate);
            if (UserInfoCacheMap.containsKey("uid-".concat(_data.get((int)_position).get("uid").toString()))) {
                if (_data.get((int)_position).get("post_visibility").toString().equals("private")) {
                    if (_data.get((int)_position).get("uid").toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        postPrivateStateIcon.setVisibility(View.VISIBLE);
                        body.setVisibility(View.VISIBLE);
                    } else {
                        body.setVisibility(View.GONE);
                    }
                } else {
                    body.setVisibility(View.VISIBLE);
                    postPrivateStateIcon.setVisibility(View.GONE);
                }
                if (UserInfoCacheMap.get("banned-".concat(_data.get((int)_position).get("uid").toString())).toString().equals("true")) {
                    userInfoProfileImage.setImageResource(R.drawable.avatar);
                } else {
                    if (UserInfoCacheMap.get("avatar-".concat(_data.get((int)_position).get("uid").toString())).toString().equals("null")) {
                        userInfoProfileImage.setImageResource(R.drawable.avatar);
                    } else {
                        Glide.with(getContext()).load(Uri.parse(UserInfoCacheMap.get("avatar-".concat(_data.get((int)_position).get("uid").toString())).toString())).into(userInfoProfileImage);
                    }
                }
                if (UserInfoCacheMap.get("nickname-".concat(_data.get((int)_position).get("uid").toString())).toString().equals("null")) {
                    userInfoUsername.setText("@" + UserInfoCacheMap.get("username-".concat(_data.get((int)_position).get("uid").toString())).toString());
                } else {
                    userInfoUsername.setText(UserInfoCacheMap.get("nickname-".concat(_data.get((int)_position).get("uid").toString())).toString());
                }
                if (UserInfoCacheMap.get("gender-".concat(_data.get((int)_position).get("uid").toString())).toString().equals("hidden")) {
                    userInfoGenderBadge.setVisibility(View.GONE);
                } else {
                    if (UserInfoCacheMap.get("gender-".concat(_data.get((int)_position).get("uid").toString())).toString().equals("male")) {
                        userInfoGenderBadge.setImageResource(R.drawable.male_badge);
                        userInfoGenderBadge.setVisibility(View.VISIBLE);
                    } else {
                        if (UserInfoCacheMap.get("gender-".concat(_data.get((int)_position).get("uid").toString())).toString().equals("female")) {
                            userInfoGenderBadge.setImageResource(R.drawable.female_badge);
                            userInfoGenderBadge.setVisibility(View.VISIBLE);
                        }
                    }
                }
                if (UserInfoCacheMap.get("acc_type-".concat(_data.get((int)_position).get("uid").toString())).toString().equals("admin")) {
                    userInfoUsernameVerifiedBadge.setImageResource(R.drawable.admin_badge);
                    userInfoUsernameVerifiedBadge.setVisibility(View.VISIBLE);
                } else {
                    if (UserInfoCacheMap.get("acc_type-".concat(_data.get((int)_position).get("uid").toString())).toString().equals("moderator")) {
                        userInfoUsernameVerifiedBadge.setImageResource(R.drawable.moderator_badge);
                        userInfoUsernameVerifiedBadge.setVisibility(View.VISIBLE);
                    } else {
                        if (UserInfoCacheMap.get("acc_type-".concat(_data.get((int)_position).get("uid").toString())).toString().equals("support")) {
                            userInfoUsernameVerifiedBadge.setImageResource(R.drawable.support_badge);
                            userInfoUsernameVerifiedBadge.setVisibility(View.VISIBLE);
                        } else {
                            if (UserInfoCacheMap.get("acc_type-".concat(_data.get((int)_position).get("uid").toString())).toString().equals("user")) {
                                if (UserInfoCacheMap.get("verify-".concat(_data.get((int)_position).get("uid").toString())).toString().equals("true")) {
                                    userInfoUsernameVerifiedBadge.setVisibility(View.VISIBLE);
                                } else {
                                    userInfoUsernameVerifiedBadge.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                }

            } else {
                DatabaseReference getReference = FirebaseDatabase.getInstance().getReference().child("skyline/users").child(_data.get((int)_position).get("uid").toString());
                getReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            UserInfoCacheMap.put("uid-".concat(_data.get((int)_position).get("uid").toString()), _data.get((int)_position).get("uid").toString());
                            UserInfoCacheMap.put("banned-".concat(_data.get((int)_position).get("uid").toString()), dataSnapshot.child("banned").getValue(String.class));
                            UserInfoCacheMap.put("nickname-".concat(_data.get((int)_position).get("uid").toString()), dataSnapshot.child("nickname").getValue(String.class));
                            UserInfoCacheMap.put("username-".concat(_data.get((int)_position).get("uid").toString()), dataSnapshot.child("username").getValue(String.class));
                            UserInfoCacheMap.put("gender-".concat(_data.get((int)_position).get("uid").toString()), dataSnapshot.child("gender").getValue(String.class));
                            UserInfoCacheMap.put("verify-".concat(_data.get((int)_position).get("uid").toString()), dataSnapshot.child("verify").getValue(String.class));
                            UserInfoCacheMap.put("acc_type-".concat(_data.get((int)_position).get("uid").toString()), dataSnapshot.child("account_type").getValue(String.class));
                            if (_data.get((int)_position).get("post_visibility").toString().equals("private")) {
                                if (_data.get((int)_position).get("uid").toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                    postPrivateStateIcon.setVisibility(View.VISIBLE);
                                    body.setVisibility(View.VISIBLE);
                                } else {
                                    body.setVisibility(View.GONE);
                                }
                            } else {
                                body.setVisibility(View.VISIBLE);
                                postPrivateStateIcon.setVisibility(View.GONE);
                            }
                            if (dataSnapshot.child("banned").getValue(String.class).equals("true")) {
                                userInfoProfileImage.setImageResource(R.drawable.avatar);
                                UserInfoCacheMap.put("avatar-".concat(_data.get((int)_position).get("uid").toString()), "null");
                            } else {
                                UserInfoCacheMap.put("avatar-".concat(_data.get((int)_position).get("uid").toString()), dataSnapshot.child("avatar").getValue(String.class));
                                if (dataSnapshot.child("avatar").getValue(String.class).equals("null")) {
                                    userInfoProfileImage.setImageResource(R.drawable.avatar);
                                } else {
                                    Glide.with(getContext()).load(Uri.parse(dataSnapshot.child("avatar").getValue(String.class))).into(userInfoProfileImage);
                                }
                            }
                            if (dataSnapshot.child("nickname").getValue(String.class).equals("null")) {
                                userInfoUsername.setText("@" + dataSnapshot.child("username").getValue(String.class));
                            } else {
                                userInfoUsername.setText(dataSnapshot.child("nickname").getValue(String.class));
                            }
                            if (dataSnapshot.child("gender").getValue(String.class).equals("hidden")) {
                                userInfoGenderBadge.setVisibility(View.GONE);
                            } else {
                                if (dataSnapshot.child("gender").getValue(String.class).equals("male")) {
                                    userInfoGenderBadge.setImageResource(R.drawable.male_badge);
                                    userInfoGenderBadge.setVisibility(View.VISIBLE);
                                } else {
                                    if (dataSnapshot.child("gender").getValue(String.class).equals("female")) {
                                        userInfoGenderBadge.setImageResource(R.drawable.female_badge);
                                        userInfoGenderBadge.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                            if (dataSnapshot.child("account_type").getValue(String.class).equals("admin")) {
                                userInfoUsernameVerifiedBadge.setImageResource(R.drawable.admin_badge);
                                userInfoUsernameVerifiedBadge.setVisibility(View.VISIBLE);
                            } else {
                                if (dataSnapshot.child("account_type").getValue(String.class).equals("moderator")) {
                                    userInfoUsernameVerifiedBadge.setImageResource(R.drawable.moderator_badge);
                                    userInfoUsernameVerifiedBadge.setVisibility(View.VISIBLE);
                                } else {
                                    if (dataSnapshot.child("account_type").getValue(String.class).equals("support")) {
                                        userInfoUsernameVerifiedBadge.setImageResource(R.drawable.support_badge);
                                        userInfoUsernameVerifiedBadge.setVisibility(View.VISIBLE);
                                    } else {
                                        if (dataSnapshot.child("account_type").getValue(String.class).equals("user")) {
                                            if (dataSnapshot.child("verify").getValue(String.class).equals("true")) {
                                                userInfoUsernameVerifiedBadge.setImageResource(R.drawable.verified_badge);
                                                userInfoUsernameVerifiedBadge.setVisibility(View.VISIBLE);
                                            } else {
                                                userInfoUsernameVerifiedBadge.setVisibility(View.GONE);
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
            DatabaseReference getLikeCheck = FirebaseDatabase.getInstance().getReference("skyline/posts-likes").child(_data.get((int)_position).get("key").toString()).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            DatabaseReference getCommentsCount = FirebaseDatabase.getInstance().getReference("skyline/posts-comments").child(_data.get((int)_position).get("key").toString());
            DatabaseReference getLikesCount = FirebaseDatabase.getInstance().getReference("skyline/posts-likes").child(_data.get((int)_position).get("key").toString());
            DatabaseReference getFavoriteCheck = FirebaseDatabase.getInstance().getReference("skyline/favorite-posts").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(_data.get((int)_position).get("key").toString());

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
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            getCommentsCount.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long count = dataSnapshot.getChildrenCount();
                    _setCount(commentsButtonCount, count);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            getLikesCount.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long count = dataSnapshot.getChildrenCount();
                    _setCount(likeButtonCount, count);
                    postLikeCountCache.put(_data.get((int)_position).get("key").toString(), String.valueOf((long)(count)));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
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
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _view) {
                    DatabaseReference getLikeCheck = FirebaseDatabase.getInstance().getReference("skyline/posts-likes").child(_data.get((int)_position).get("key").toString()).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    getLikeCheck.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                getLikeCheck.removeValue();
                                postLikeCountCache.put(_data.get((int)_position).get("key").toString(), String.valueOf((long)(Double.parseDouble(postLikeCountCache.get(_data.get((int)_position).get("key").toString()).toString()) - 1)));
                                _setCount(likeButtonCount, Double.parseDouble(postLikeCountCache.get(_data.get((int)_position).get("key").toString()).toString()));
                                likeButtonIc.setImageResource(R.drawable.post_icons_1_1);
                            } else {
                                getLikeCheck.setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                com.synapse.social.studioasinc.util.NotificationUtils.sendPostLikeNotification(_data.get((int)_position).get("key").toString(), _data.get((int)_position).get("uid").toString());
                                postLikeCountCache.put(_data.get((int)_position).get("key").toString(), String.valueOf((long)(Double.parseDouble(postLikeCountCache.get(_data.get((int)_position).get("key").toString()).toString()) + 1)));
                                _setCount(likeButtonCount, Double.parseDouble(postLikeCountCache.get(_data.get((int)_position).get("key").toString()).toString()));
                                likeButtonIc.setImageResource(R.drawable.post_icons_1_2);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    vbr.vibrate((long)(24));
                }
            });
            commentsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _view) {
                    Bundle sendPostKey = new Bundle();
                    sendPostKey.putString("postKey", _data.get((int)_position).get("key").toString());
                    sendPostKey.putString("postPublisherUID", _data.get((int)_position).get("uid").toString());
                    sendPostKey.putString("postPublisherAvatar", UserInfoCacheMap.get("avatar-".concat(_data.get((int)_position).get("uid").toString())).toString());
                    PostCommentsBottomSheetDialog postCommentsBottomSheet = new PostCommentsBottomSheetDialog();
                    postCommentsBottomSheet.setArguments(sendPostKey);
                    postCommentsBottomSheet.show(getActivity().getSupportFragmentManager(), postCommentsBottomSheet.getTag());
                }
            });
            userInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _view) {
                    intent.setClass(getContext(), ProfileActivity.class);
                    intent.putExtra("uid", _data.get((int)_position).get("uid").toString());
                    startActivity(intent);
                }
            });
            favoritePostButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _view) {
                    DatabaseReference getFavoriteCheck = FirebaseDatabase.getInstance().getReference("skyline/favorite-posts").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(_data.get((int)_position).get("key").toString());
                    getFavoriteCheck.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                getFavoriteCheck.removeValue();
                                favoritePostButton.setImageResource(R.drawable.add_favorite_post_ic);
                            } else {
                                getFavoriteCheck.setValue(_data.get((int)_position).get("key").toString());
                                favoritePostButton.setImageResource(R.drawable.delete_favorite_post_ic);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    vbr.vibrate((long)(24));
                }
            });
            topMoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _view) {
                    Bundle sendPostKey = new Bundle();
                    sendPostKey.putString("postKey", _data.get((int)_position).get("key").toString());
                    sendPostKey.putString("postPublisherUID", _data.get((int)_position).get("uid").toString());
                    sendPostKey.putString("postType", _data.get((int)_position).get("post_type").toString());
                    PostMoreBottomSheetDialog postMoreBottomSheetDialog = new PostMoreBottomSheetDialog();
                    postMoreBottomSheetDialog.setArguments(sendPostKey);
                    postMoreBottomSheetDialog.show(getActivity().getSupportFragmentManager(), postMoreBottomSheetDialog.getTag());
                }
            });
            likeButton.setBackground(new android.graphics.drawable.GradientDrawable() { public android.graphics.drawable.GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)300, (int)1, 0xFFE0E0E0, 0xFFF5F5F5));
            commentsButton.setBackground(new android.graphics.drawable.GradientDrawable() { public android.graphics.drawable.GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)300, (int)1, 0xFFE0E0E0, 0xFFF5F5F5));
            postImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _view) {
                    _OpenWebView(_data.get((int)_position).get("post_image").toString());
                }
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
}
