package com.synapse.social.studioasinc.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.synapse.social.studioasinc.CreatePostActivity;
import com.synapse.social.studioasinc.PostCommentsBottomSheetDialog;
import com.synapse.social.studioasinc.PostMoreBottomSheetDialog;
import com.synapse.social.studioasinc.ProfileActivity;
import com.synapse.social.studioasinc.R;
import com.synapse.social.studioasinc.CreateLineVideoActivity;
import com.synapse.social.studioasinc.util.NotificationUtils;
import com.synapse.social.studioasinc.styling.MarkdownRenderer;
import com.synapse.social.studioasinc.fragments.HomeFragment;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PublicPostsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public interface HeaderAdapterListener {
        void onImagePostClick();
        void onVideoPostClick();
        void onTextPostClick(String text);
        void onFilterClick(String filter);
    }

    private ArrayList<HashMap<String, Object>> _data;
    private HeaderAdapterListener listener;
    private Context context;
    private Vibrator vbr;
    private Intent intent = new Intent();
    private HashMap<String, Object> UserInfoCacheMap = new HashMap<>();
    private HashMap<String, Object> postLikeCountCache = new HashMap<>();
    private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();


    public PublicPostsListAdapter(ArrayList<HashMap<String, Object>> _arr, HeaderAdapterListener listener, Context context) {
        _data = _arr;
        this.listener = listener;
        this.context = context;
        vbr = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void setData(ArrayList<HashMap<String, Object>> data) {
        this._data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View _v = LayoutInflater.from(context).inflate(R.layout.home_feed_header, parent, false);
            return new HeaderViewHolder(_v);
        }
        View _v = LayoutInflater.from(context).inflate(R.layout.synapse_post_cv, parent, false);
        return new ItemViewHolder(_v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder _holder, final int _position) {
        if (_holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) _holder;
            // TODO: Implement stories
            // headerHolder.storiesView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false));
            // headerHolder.storiesView.setAdapter(new StoriesViewAdapter(storiesList));
            // _loadStories();

            headerHolder.miniPostLayoutTextPostInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
                    final String _charSeq = _param1.toString();
                    if (_charSeq.length() == 0) {
                        headerHolder.miniPostLayoutTextPostPublish.setVisibility(View.GONE);
                    } else {
                        _viewGraphics(headerHolder.miniPostLayoutTextPostPublish, context.getResources().getColor(R.color.colorPrimary), 0xFFC5CAE9, 300, 0, Color.TRANSPARENT);
                        headerHolder.miniPostLayoutTextPostPublish.setVisibility(View.VISIBLE);
                    }
                }
                @Override
                public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {}
                @Override
                public void afterTextChanged(Editable _param1) {}
            });

            headerHolder.miniPostLayoutImagePost.setOnClickListener(v -> listener.onImagePostClick());

            headerHolder.miniPostLayoutVideoPost.setOnClickListener(v -> listener.onVideoPostClick());

            headerHolder.miniPostLayoutTextPostPublish.setOnClickListener(v -> {
                listener.onTextPostClick(headerHolder.miniPostLayoutTextPostInput.getText().toString());
                headerHolder.miniPostLayoutTextPostInput.setText("");
            });

            DatabaseReference getReference = _firebase.getReference("skyline/users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            getReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        if (dataSnapshot.child("avatar").getValue(String.class) != null && !dataSnapshot.child("avatar").getValue(String.class).equals("null")) {
                            Glide.with(context).load(Uri.parse(dataSnapshot.child("avatar").getValue(String.class))).into(headerHolder.miniPostLayoutProfileImage);
                        } else {
                            headerHolder.miniPostLayoutProfileImage.setImageResource(R.drawable.avatar);
                        }
                    } else {
                        headerHolder.miniPostLayoutProfileImage.setImageResource(R.drawable.avatar);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(context, "Error fetching user profile: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    headerHolder.miniPostLayoutProfileImage.setImageResource(R.drawable.avatar);
                }
            });

            headerHolder.miniPostLayoutFiltersScrollBodyFilterLOCAL.setOnClickListener(v -> {
                listener.onFilterClick("LOCAL");
                _viewGraphics(headerHolder.miniPostLayoutFiltersScrollBodyFilterLOCAL, context.getResources().getColor(R.color.colorPrimary), 0xFF9FA8DA, 300, 0, Color.TRANSPARENT);
                _viewGraphics(headerHolder.miniPostLayoutFiltersScrollBodyFilterPUBLIC, 0xFFFFFFFF, 0xFFEEEEEE, 300, 2, 0xFFEEEEEE);
                _viewGraphics(headerHolder.miniPostLayoutFiltersScrollBodyFilterFOLLOWED, 0xFFFFFFFF, 0xFFEEEEEE, 300, 2, 0xFFEEEEEE);
                _viewGraphics(headerHolder.miniPostLayoutFiltersScrollBodyFilterFAVORITE, 0xFFFFFFFF, 0xFFEEEEEE, 300, 2, 0xFFEEEEEE);
                headerHolder.miniPostLayoutFiltersScrollBodyFilterLOCAL.setTextColor(0xFFFFFFFF);
                headerHolder.miniPostLayoutFiltersScrollBodyFilterPUBLIC.setTextColor(0xFF616161);
                headerHolder.miniPostLayoutFiltersScrollBodyFilterFOLLOWED.setTextColor(0xFF616161);
                headerHolder.miniPostLayoutFiltersScrollBodyFilterFAVORITE.setTextColor(0xFF616161);
            });

            headerHolder.miniPostLayoutFiltersScrollBodyFilterPUBLIC.setOnClickListener(v -> {
                listener.onFilterClick("PUBLIC");
                _viewGraphics(headerHolder.miniPostLayoutFiltersScrollBodyFilterLOCAL, 0xFFFFFFFF, 0xFFEEEEEE, 300, 2, 0xFFEEEEEE);
                _viewGraphics(headerHolder.miniPostLayoutFiltersScrollBodyFilterPUBLIC, context.getResources().getColor(R.color.colorPrimary), 0xFF1A237E, 300, 0, Color.TRANSPARENT);
                _viewGraphics(headerHolder.miniPostLayoutFiltersScrollBodyFilterFOLLOWED, 0xFFFFFFFF, 0xFFEEEEEE, 300, 2, 0xFFEEEEEE);
                _viewGraphics(headerHolder.miniPostLayoutFiltersScrollBodyFilterFAVORITE, 0xFFFFFFFF, 0xFFEEEEEE, 300, 2, 0xFFEEEEEE);
                headerHolder.miniPostLayoutFiltersScrollBodyFilterLOCAL.setTextColor(0xFF616161);
                headerHolder.miniPostLayoutFiltersScrollBodyFilterPUBLIC.setTextColor(0xFFFFFFFF);
                headerHolder.miniPostLayoutFiltersScrollBodyFilterFOLLOWED.setTextColor(0xFF616161);
                headerHolder.miniPostLayoutFiltersScrollBodyFilterFAVORITE.setTextColor(0xFF616161);
            });

            headerHolder.miniPostLayoutFiltersScrollBodyFilterFOLLOWED.setOnClickListener(v -> {
                listener.onFilterClick("FOLLOWED");
                _viewGraphics(headerHolder.miniPostLayoutFiltersScrollBodyFilterLOCAL, 0xFFFFFFFF, 0xFFEEEEEE, 300, 2, 0xFFEEEEEE);
                _viewGraphics(headerHolder.miniPostLayoutFiltersScrollBodyFilterPUBLIC, 0xFFFFFFFF, 0xFFEEEEEE, 300, 2, 0xFFEEEEEE);
                _viewGraphics(headerHolder.miniPostLayoutFiltersScrollBodyFilterFOLLOWED, context.getResources().getColor(R.color.colorPrimary), 0xFF616161, 300, 0, Color.TRANSPARENT);
                _viewGraphics(headerHolder.miniPostLayoutFiltersScrollBodyFilterFAVORITE, 0xFFFFFFFF, 0xFFEEEEEE, 300, 2, 0xFFEEEEEE);
                headerHolder.miniPostLayoutFiltersScrollBodyFilterLOCAL.setTextColor(0xFF616161);
                headerHolder.miniPostLayoutFiltersScrollBodyFilterPUBLIC.setTextColor(0xFF616161);
                headerHolder.miniPostLayoutFiltersScrollBodyFilterFOLLOWED.setTextColor(0xFFFFFFFF);
                headerHolder.miniPostLayoutFiltersScrollBodyFilterFAVORITE.setTextColor(0xFF616161);
            });

            headerHolder.miniPostLayoutFiltersScrollBodyFilterFAVORITE.setOnClickListener(v -> {
                listener.onFilterClick("FAVORITE");
                _viewGraphics(headerHolder.miniPostLayoutFiltersScrollBodyFilterLOCAL, 0xFFFFFFFF, 0xFFEEEEEE, 300, 2, 0xFFEEEEEE);
                _viewGraphics(headerHolder.miniPostLayoutFiltersScrollBodyFilterPUBLIC, 0xFFFFFFFF, 0xFFEEEEEE, 300, 2, 0xFFEEEEEE);
                _viewGraphics(headerHolder.miniPostLayoutFiltersScrollBodyFilterFOLLOWED, 0xFFFFFFFF, 0xFFEEEEEE, 300, 2, 0xFFEEEEEE);
                _viewGraphics(headerHolder.miniPostLayoutFiltersScrollBodyFilterFAVORITE, context.getResources().getColor(R.color.colorPrimary), 0xFF9FA8DA, 300, 0, Color.TRANSPARENT);
                headerHolder.miniPostLayoutFiltersScrollBodyFilterLOCAL.setTextColor(0xFF616161);
                headerHolder.miniPostLayoutFiltersScrollBodyFilterPUBLIC.setTextColor(0xFF616161);
                headerHolder.miniPostLayoutFiltersScrollBodyFilterFOLLOWED.setTextColor(0xFF616161);
                headerHolder.miniPostLayoutFiltersScrollBodyFilterFAVORITE.setTextColor(0xFFFFFFFF);
            });
        } else if (_holder instanceof ItemViewHolder) {
            ItemViewHolder itemHolder = (ItemViewHolder) _holder;
            HashMap<String, Object> itemData = _data.get(_position -1);
            // ... (item view holder logic)
        }
    }

    @Override
    public int getItemCount() {
        return _data.size() + 1;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public ItemViewHolder(View v) {
            super(v);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        RecyclerView storiesView;
        EditText miniPostLayoutTextPostInput;
        TextView miniPostLayoutTextPostPublish;
        ImageView miniPostLayoutProfileImage;
        ImageView miniPostLayoutImagePost;
        ImageView miniPostLayoutVideoPost;
        TextView miniPostLayoutFiltersScrollBodyFilterLOCAL;
        TextView miniPostLayoutFiltersScrollBodyFilterPUBLIC;
        TextView miniPostLayoutFiltersScrollBodyFilterFOLLOWED;
        TextView miniPostLayoutFiltersScrollBodyFilterFAVORITE;

        public HeaderViewHolder(View v) {
            super(v);
            storiesView = v.findViewById(R.id.storiesView);
            miniPostLayoutTextPostInput = v.findViewById(R.id.miniPostLayoutTextPostInput);
            miniPostLayoutTextPostPublish = v.findViewById(R.id.miniPostLayoutTextPostPublish);
            miniPostLayoutProfileImage = v.findViewById(R.id.miniPostLayoutProfileImage);
            miniPostLayoutImagePost = v.findViewById(R.id.miniPostLayoutImagePost);
            miniPostLayoutVideoPost = v.findViewById(R.id.miniPostLayoutVideoPost);
            miniPostLayoutFiltersScrollBodyFilterLOCAL = v.findViewById(R.id.miniPostLayoutFiltersScrollBodyFilterLOCAL);
            miniPostLayoutFiltersScrollBodyFilterPUBLIC = v.findViewById(R.id.miniPostLayoutFiltersScrollBodyFilterPUBLIC);
            miniPostLayoutFiltersScrollBodyFilterFOLLOWED = v.findViewById(R.id.miniPostLayoutFiltersScrollBodyFilterFOLLOWED);
            miniPostLayoutFiltersScrollBodyFilterFAVORITE = v.findViewById(R.id.miniPostLayoutFiltersScrollBodyFilterFAVORITE);
        }
    }

    public void _viewGraphics(final View _view, final int _onFocus, final int _onRipple, final double _radius, final double _stroke, final int _strokeColor) {
        android.graphics.drawable.GradientDrawable GG = new android.graphics.drawable.GradientDrawable();
        GG.setColor(_onFocus);
        GG.setCornerRadius((float)_radius);
        GG.setStroke((int) _stroke, _strokeColor);
        android.graphics.drawable.RippleDrawable RE = new android.graphics.drawable.RippleDrawable(new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{ _onRipple}), GG, null);
        _view.setBackground(RE);
    }

    public void _setTime(final double _currentTime, final TextView _txt) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		double time_diff = c1.getTimeInMillis() - _currentTime;
		if (time_diff < 60000) {
			if ((time_diff / 1000) < 2) {
				_txt.setText("1" + " " + context.getResources().getString(R.string.seconds_ago));
			} else {
				_txt.setText(String.valueOf((long)(time_diff / 1000)).concat(" " + context.getResources().getString(R.string.seconds_ago)));
			}
		} else {
			if (time_diff < (60 * 60000)) {
				if ((time_diff / 60000) < 2) {
					_txt.setText("1" + " " + context.getResources().getString(R.string.minutes_ago));
				} else {
					_txt.setText(String.valueOf((long)(time_diff / 60000)).concat(" " + context.getResources().getString(R.string.minutes_ago)));
				}
			} else {
				if (time_diff < (24 * (60 * 60000))) {
					if ((time_diff / (60 * 60000)) < 2) {
						_txt.setText(String.valueOf((long)(time_diff / (60 * 60000))).concat(" " + context.getResources().getString(R.string.hours_ago)));
					} else {
						_txt.setText(String.valueOf((long)(time_diff / (60 * 60000))).concat(" " + context.getResources().getString(R.string.hours_ago)));
					}
				} else {
					if (time_diff < (7 * (24 * (60 * 60000)))) {
						if ((time_diff / (24 * (60 * 60000))) < 2) {
							_txt.setText(String.valueOf((long)(time_diff / (24 * (60 * 60000)))).concat(" " + context.getResources().getString(R.string.days_ago)));
						} else {
							_txt.setText(String.valueOf((long)(time_diff / (24 * (60 * 60000)))).concat(" " + context.getResources().getString(R.string.days_ago)));
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
        customtabsintent.launchUrl(context, Uri.parse(AndroidDevelopersBlogURL));
    }

    public void _updatePostViewVisibility(LinearLayout body, ImageView postPrivateStateIcon, String postUid, String postVisibility) {
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

    public void _displayUserInfoFromCache(String postUid, ImageView userInfoProfileImage, TextView userInfoUsername, ImageView userInfoGenderBadge, ImageView userInfoUsernameVerifiedBadge) {
        if (UserInfoCacheMap.get("banned-".concat(postUid)).toString().equals("true")) {
            userInfoProfileImage.setImageResource(R.drawable.banned_avatar);
        } else {
            if (UserInfoCacheMap.get("avatar-".concat(postUid)).toString().equals("null")) {
                userInfoProfileImage.setImageResource(R.drawable.avatar);
            } else {
                Glide.with(context).load(Uri.parse(UserInfoCacheMap.get("avatar-".concat(postUid)).toString())).into(userInfoProfileImage);
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
