package com.synapse.social.studioasinc;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButtonGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import android.content.Context;


public class UserInfoFragment extends Fragment {

    private String uid;
    private HashMap<String, Object> UserInfoCacheMap = new HashMap<>();

    private ImageView ProfilePageTabUserInfoCoverImage;
    private ImageView ProfilePageTabUserInfoProfileImage;
    private LinearLayout likeUserProfileButton;
    private ImageView likeUserProfileButtonIc;
    private TextView likeUserProfileButtonLikeCount;
    private TextView ProfilePageTabUserInfoNickname;
    private TextView ProfilePageTabUserInfoBioLayoutText;
    private TextView ProfilePageTabUserInfoUsername;
    private TextView ProfilePageTabUserInfoStatus;
    private TextView ProfilePageTabUserInfoFollowersCount;
    private TextView ProfilePageTabUserInfoFollowingCount;
    private MaterialButtonGroup ProfilePageTabUserInfoSecondaryButtons;
    private Button btnEditProfile;
    private Button btnFollow;
    private Button btnMessage;
    private TextView join_date_layout_text;
    private TextView user_uid_layout_text;
    private Vibrator vbr;


    public static UserInfoFragment newInstance(String uid) {
        UserInfoFragment fragment = new UserInfoFragment();
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
        return inflater.inflate(R.layout.fragment_user_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProfilePageTabUserInfoCoverImage = view.findViewById(R.id.ProfilePageTabUserInfoCoverImage);
        ProfilePageTabUserInfoProfileImage = view.findViewById(R.id.ProfilePageTabUserInfoProfileImage);
        likeUserProfileButton = view.findViewById(R.id.likeUserProfileButton);
        likeUserProfileButtonIc = view.findViewById(R.id.likeUserProfileButtonIc);
        likeUserProfileButtonLikeCount = view.findViewById(R.id.likeUserProfileButtonLikeCount);
        ProfilePageTabUserInfoNickname = view.findViewById(R.id.ProfilePageTabUserInfoNickname);
        ProfilePageTabUserInfoBioLayoutText = view.findViewById(R.id.ProfilePageTabUserInfoBioLayoutText);
        ProfilePageTabUserInfoUsername = view.findViewById(R.id.ProfilePageTabUserInfoUsername);
        ProfilePageTabUserInfoStatus = view.findViewById(R.id.ProfilePageTabUserInfoStatus);
        ProfilePageTabUserInfoFollowersCount = view.findViewById(R.id.ProfilePageTabUserInfoFollowersCount);
        ProfilePageTabUserInfoFollowingCount = view.findViewById(R.id.ProfilePageTabUserInfoFollowingCount);
        ProfilePageTabUserInfoSecondaryButtons = view.findViewById(R.id.ProfilePageTabUserInfoSecondaryButtons);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnFollow = view.findViewById(R.id.btnFollow);
        btnMessage = view.findViewById(R.id.btnMessage);
        join_date_layout_text = view.findViewById(R.id.join_date_layout_text);
        user_uid_layout_text = view.findViewById(R.id.user_uid_layout_text);
        vbr = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);


        _getUserReference();
        _getUserCountReference();
    }

    public void _getUserReference() {
        DatabaseReference getUserReference = FirebaseDatabase.getInstance().getReference("skyline/users").child(uid);
        getUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    user_uid_layout_text.setText(dataSnapshot.child("uid").getValue(String.class));
                    Calendar JoinDateCC = Calendar.getInstance();
                    JoinDateCC.setTimeInMillis((long)(Double.parseDouble(dataSnapshot.child("join_date").getValue(String.class))));
                    join_date_layout_text.setText(new SimpleDateFormat("dd-MM-yyyy").format(JoinDateCC.getTime()));

                    if (dataSnapshot.child("banned").getValue(String.class).equals("true")) {
                        ProfilePageTabUserInfoProfileImage.setImageResource(R.drawable.banned_avatar);
                        ProfilePageTabUserInfoCoverImage.setImageResource(R.drawable.banned_cover_photo);
                    } else {
                        if (dataSnapshot.child("profile_cover_image").getValue(String.class).equals("null")) {
                            ProfilePageTabUserInfoCoverImage.setImageResource(R.drawable.user_null_cover_photo);
                        } else {
                            Glide.with(getContext()).load(dataSnapshot.child("profile_cover_image").getValue(String.class)).into(ProfilePageTabUserInfoCoverImage);
                        }
                        if (dataSnapshot.child("avatar").getValue(String.class).equals("null")) {
                            ProfilePageTabUserInfoProfileImage.setImageResource(R.drawable.avatar);
                        } else {
                            Glide.with(getContext()).load(dataSnapshot.child("avatar").getValue(String.class)).into(ProfilePageTabUserInfoProfileImage);
                        }
                    }
                    if (dataSnapshot.child("status").getValue(String.class).equals("online")) {
                        ProfilePageTabUserInfoStatus.setText(getResources().getString(R.string.online));
                        ProfilePageTabUserInfoStatus.setTextColor(0xFF2196F3);
                    } else {
                        if (dataSnapshot.child("status").getValue(String.class).equals("offline")) {
                            ProfilePageTabUserInfoStatus.setText(getResources().getString(R.string.offline));
                        } else {
                            // _setUserLastSeen(Double.parseDouble(dataSnapshot.child("status").getValue(String.class)), ProfilePageTabUserInfoStatus);
                        }
                        ProfilePageTabUserInfoStatus.setTextColor(0xFF757575);
                    }
                    ProfilePageTabUserInfoUsername.setText("@" + dataSnapshot.child("username").getValue(String.class));
                    if (dataSnapshot.child("nickname").getValue(String.class).equals("null")) {
                        ProfilePageTabUserInfoNickname.setText("@" + dataSnapshot.child("username").getValue(String.class));
                    } else {
                        ProfilePageTabUserInfoNickname.setText(dataSnapshot.child("nickname").getValue(String.class));
                    }
                    if (dataSnapshot.child("biography").getValue(String.class).equals("null")) {

                    } else {
                        ProfilePageTabUserInfoBioLayoutText.setText(dataSnapshot.child("biography").getValue(String.class));
                    }
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void _getUserCountReference() {
        DatabaseReference getFollowersCount = FirebaseDatabase.getInstance().getReference("skyline/followers").child(uid);
        getFollowersCount.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                ProfilePageTabUserInfoFollowersCount.setText(_getStyledNumber(count).concat(" ".concat(getResources().getString(R.string.followers))));
                UserInfoCacheMap.put("followers_count".concat(uid), String.valueOf((long)(count)));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DatabaseReference getFollowingCount = FirebaseDatabase.getInstance().getReference("skyline/following").child(uid);
        getFollowingCount.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                ProfilePageTabUserInfoFollowingCount.setText(_getStyledNumber(count).concat(" ".concat(getResources().getString(R.string.following))));
                UserInfoCacheMap.put("following_count".concat(uid), String.valueOf((long)(count)));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DatabaseReference checkFollowUser = FirebaseDatabase.getInstance().getReference("skyline/followers").child(uid).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        checkFollowUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    btnFollow.setText(getResources().getString(R.string.unfollow));
                } else {
                    btnFollow.setText(getResources().getString(R.string.follow));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference checkProfileLike = FirebaseDatabase.getInstance().getReference("skyline/profile-likes").child(uid).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        checkProfileLike.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    likeUserProfileButtonIc.setImageResource(R.drawable.post_icons_1_2);
                } else {
                    likeUserProfileButtonIc.setImageResource(R.drawable.post_icons_1_1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference getProfileLikesCount = FirebaseDatabase.getInstance().getReference("skyline/profile-likes").child(uid);
        getProfileLikesCount.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                likeUserProfileButtonLikeCount.setText(_getStyledNumber(count));
                UserInfoCacheMap.put("profile_like_count".concat(uid), String.valueOf((long)(count)));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public String _getStyledNumber(final double _number) {
        if (_number < 10000) {
            return String.valueOf((long) _number);
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
            return decimalFormat.format(formattedNumber) + numberFormat;
        }
    }
}
