package com.synapse.social.studioasinc.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.synapse.social.studioasinc.ChatActivity;
import com.synapse.social.studioasinc.R;
import com.synapse.social.studioasinc.SketchwareUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatsFragment extends Fragment {

  private SwipeRefreshLayout swipeLayout;
  private RecyclerView InboxRecyclerView;
  private TextView noInbox;

  private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
  private FirebaseAuth auth = FirebaseAuth.getInstance();
  private DatabaseReference main = _firebase.getReference("skyline");

  private ArrayList<HashMap<String, Object>> ChatInboxList = new ArrayList<>();
  private HashMap<String, Object> UserInfoCacheMap = new HashMap<>();

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_chats, container, false);
    initialize(view);
    initializeLogic();
    return view;
  }

  private void initialize(View view) {
    swipeLayout = view.findViewById(R.id.swipeLayout);
    InboxRecyclerView = view.findViewById(R.id.InboxRecyclerView);
    noInbox = view.findViewById(R.id.noInbox);

    swipeLayout.setOnRefreshListener(
        new SwipeRefreshLayout.OnRefreshListener() {
          @Override
          public void onRefresh() {
            _getInboxReference();
          }
        });
  }

  private void initializeLogic() {
    InboxRecyclerView.setAdapter(new InboxRecyclerViewAdapter(ChatInboxList));
    InboxRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    _getInboxReference();
  }

  @Override
  public void onResume() {
    super.onResume();
    _getInboxReference();
  }

  public void _getInboxReference() {
    Query getInboxRef =
        FirebaseDatabase.getInstance()
            .getReference("skyline/inbox")
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    getInboxRef.addValueEventListener(
        new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
              InboxRecyclerView.setVisibility(View.VISIBLE);
              noInbox.setVisibility(View.GONE);
              ChatInboxList.clear();
              try {
                GenericTypeIndicator<HashMap<String, Object>> _ind =
                    new GenericTypeIndicator<HashMap<String, Object>>() {};
                for (DataSnapshot _data : dataSnapshot.getChildren()) {
                  HashMap<String, Object> _map = _data.getValue(_ind);
                  ChatInboxList.add(_map);
                }
              } catch (Exception _e) {
                _e.printStackTrace();
              }
              SketchwareUtil.sortListMap(ChatInboxList, "push_date", false, false);
              InboxRecyclerView.getAdapter().notifyDataSetChanged();
            } else {
              InboxRecyclerView.setVisibility(View.GONE);
              noInbox.setVisibility(View.VISIBLE);
            }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {}
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
        _txt.setText(
            String.valueOf((long) (time_diff / 1000)) + " " + getResources().getString(R.string.seconds_ago));
      }
    } else {
      if (time_diff < (60 * 60000)) {
        if ((time_diff / 60000) < 2) {
          _txt.setText("1" + " " + getResources().getString(R.string.minutes_ago));
        } else {
          _txt.setText(
              String.valueOf((long) (time_diff / 60000)) + " " + getResources().getString(R.string.minutes_ago));
        }
      } else {
        if (time_diff < (24 * (60 * 60000))) {
          if ((time_diff / (60 * 60000)) < 2) {
            _txt.setText(
                String.valueOf((long) (time_diff / (60 * 60000)))
                    + " "
                    + getResources().getString(R.string.hours_ago));
          } else {
            _txt.setText(
                String.valueOf((long) (time_diff / (60 * 60000)))
                    + " "
                    + getResources().getString(R.string.hours_ago));
          }
        } else {
          if (time_diff < (7 * (24 * (60 * 60000)))) {
            if ((time_diff / (24 * (60 * 60000))) < 2) {
              _txt.setText(
                  String.valueOf((long) (time_diff / (24 * (60 * 60000))))
                      + " "
                      + getResources().getString(R.string.days_ago));
            } else {
              _txt.setText(
                  String.valueOf((long) (time_diff / (24 * (60 * 60000))))
                      + " "
                      + getResources().getString(R.string.days_ago));
            }
          } else {
            c2.setTimeInMillis((long) _currentTime);
            _txt.setText(new SimpleDateFormat("dd-MM-yyyy").format(c2.getTime()));
          }
        }
      }
    }
  }

  public class InboxRecyclerViewAdapter
      extends RecyclerView.Adapter<InboxRecyclerViewAdapter.ViewHolder> {

    private ArrayList<HashMap<String, Object>> _data;

    public InboxRecyclerViewAdapter(ArrayList<HashMap<String, Object>> _arr) {
      _data = _arr;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      LayoutInflater _inflater = LayoutInflater.from(getContext());
      View _v = _inflater.inflate(R.layout.inbox_msg_list_cv_synapse, null);
      RecyclerView.LayoutParams _lp =
          new RecyclerView.LayoutParams(
              ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
      _v.setLayoutParams(_lp);
      return new ViewHolder(_v);
    }

    @Override
    public void onBindViewHolder(ViewHolder _holder, final int _position) {
      View _view = _holder.itemView;
      final LinearLayout main = _view.findViewById(R.id.main);
      final ImageView profileCardImage = _view.findViewById(R.id.profileCardImage);
      final TextView username = _view.findViewById(R.id.username);
      final TextView last_message = _view.findViewById(R.id.last_message);
      final TextView push = _view.findViewById(R.id.push);
      final ImageView message_state = _view.findViewById(R.id.message_state);
      final TextView unread_messages_count_badge =
          _view.findViewById(R.id.unread_messages_count_badge);
      final LinearLayout userStatusCircleBG = _view.findViewById(R.id.userStatusCircleBG);
      final ImageView genderBadge = _view.findViewById(R.id.genderBadge);
      final ImageView verifiedBadge = _view.findViewById(R.id.verifiedBadge);

      try {
        RecyclerView.LayoutParams _lp =
            new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        _view.setLayoutParams(_lp);
        main.setVisibility(View.GONE);

        if (_data.get(_position).get("last_message_text").toString().equals("null")) {
          last_message.setText(getResources().getString(R.string.m_no_chats));
        } else {
          last_message.setText(_data.get(_position).get("last_message_text").toString());
        }

        if (_data
            .get(_position)
            .get("last_message_uid")
            .toString()
            .equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
          if (_data.get(_position).get("last_message_state").toString().equals("sended")) {
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
          ExecutorService mExecutorService = Executors.newSingleThreadExecutor();
          Handler mMainHandler = new Handler(Looper.getMainLooper());
          mExecutorService.execute(
              () -> {
                Query getUnreadMessagesCount =
                    FirebaseDatabase.getInstance()
                        .getReference("skyline/chats")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(_data.get(_position).get("uid").toString())
                        .orderByChild("message_state")
                        .equalTo("sended");
                getUnreadMessagesCount.addValueEventListener(
                    new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mMainHandler.post(
                            () -> {
                              long unReadMessageCount = dataSnapshot.getChildrenCount();
                              if (dataSnapshot.exists()) {
                                last_message.setTextColor(0xFF000000);
                                push.setTextColor(0xFF000000);
                                unread_messages_count_badge.setText(
                                    String.valueOf(unReadMessageCount));
                                unread_messages_count_badge.setVisibility(View.VISIBLE);
                              } else {
                                last_message.setTextColor(0xFF616161);
                                push.setTextColor(0xFF616161);
                                unread_messages_count_badge.setVisibility(View.GONE);
                              }
                            });
                      }

                      @Override
                      public void onCancelled(@NonNull DatabaseError databaseError) {}
                    });
              });
        }

        _setTime(
            Double.parseDouble(_data.get(_position).get("push_date").toString()), push);

        if (UserInfoCacheMap.containsKey("uid-" + _data.get(_position).get("uid").toString())) {
          main.setVisibility(View.VISIBLE);
          String uid = _data.get(_position).get("uid").toString();
          Object bannedObj = UserInfoCacheMap.get("banned-" + uid);
          if (bannedObj != null && bannedObj.toString().equals("true")) {
            profileCardImage.setImageResource(R.drawable.banned_avatar);
          } else {
            Object avatarObj = UserInfoCacheMap.get("avatar-" + uid);
            if (avatarObj == null || avatarObj.toString().equals("null")) {
              profileCardImage.setImageResource(R.drawable.avatar);
            } else {
              Glide.with(getContext()).load(Uri.parse(avatarObj.toString())).into(profileCardImage);
            }
          }

          Object nicknameObj = UserInfoCacheMap.get("nickname-" + uid);
          if (nicknameObj == null || nicknameObj.toString().equals("null")) {
            Object usernameObj = UserInfoCacheMap.get("username-" + uid);
            username.setText("@" + (usernameObj != null ? usernameObj.toString() : "unknown"));
          } else {
            username.setText(nicknameObj.toString());
          }

          Object statusObj = UserInfoCacheMap.get("status-" + uid);
          userStatusCircleBG.setVisibility(
              statusObj != null && statusObj.toString().equals("online")
                  ? View.VISIBLE
                  : View.GONE);

          Object genderObj = UserInfoCacheMap.get("gender-" + uid);
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

          Object accountTypeObj = UserInfoCacheMap.get("account_type-" + uid);
          Object premiumObj = UserInfoCacheMap.get("account_premium-" + uid);
          Object verifyObj = UserInfoCacheMap.get("verify-" + uid);

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
          ExecutorService mExecutorService = Executors.newSingleThreadExecutor();
          Handler mMainHandler = new Handler(Looper.getMainLooper());
          mExecutorService.execute(
              () -> {
                DatabaseReference getUserReference =
                    FirebaseDatabase.getInstance()
                        .getReference("skyline/users")
                        .child(_data.get(_position).get("uid").toString());
                getUserReference.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mMainHandler.post(
                            () -> {
                              if (dataSnapshot.exists()) {
                                UserInfoCacheMap.put(
                                    "uid-" + _data.get(_position).get("uid").toString(),
                                    _data.get(_position).get("uid").toString());
                                UserInfoCacheMap.put(
                                    "avatar-" + _data.get(_position).get("uid").toString(),
                                    dataSnapshot.child("avatar").getValue(String.class));
                                UserInfoCacheMap.put(
                                    "banned-" + _data.get(_position).get("uid").toString(),
                                    dataSnapshot.child("banned").getValue(String.class));
                                UserInfoCacheMap.put(
                                    "username-" + _data.get(_position).get("uid").toString(),
                                    dataSnapshot.child("username").getValue(String.class));
                                UserInfoCacheMap.put(
                                    "nickname-" + _data.get(_position).get("uid").toString(),
                                    dataSnapshot.child("nickname").getValue(String.class));
                                UserInfoCacheMap.put(
                                    "status-" + _data.get(_position).get("uid").toString(),
                                    dataSnapshot.child("status").getValue(String.class));
                                UserInfoCacheMap.put(
                                    "gender-" + _data.get(_position).get("uid").toString(),
                                    dataSnapshot.child("gender").getValue(String.class));
                                UserInfoCacheMap.put(
                                    "account_type-" + _data.get(_position).get("uid").toString(),
                                    dataSnapshot.child("account_type").getValue(String.class));
                                UserInfoCacheMap.put(
                                    "account_premium-"
                                        + _data.get(_position).get("uid").toString(),
                                    dataSnapshot.child("account_premium").getValue(String.class));
                                UserInfoCacheMap.put(
                                    "verify-" + _data.get(_position).get("uid").toString(),
                                    dataSnapshot.child("verify").getValue(String.class));
                                main.setVisibility(View.VISIBLE);

                                if (dataSnapshot.child("banned").getValue(String.class).equals("true")) {
                                  profileCardImage.setImageResource(R.drawable.banned_avatar);
                                } else {
                                  if (dataSnapshot.child("avatar").getValue(String.class).equals("null")) {
                                    profileCardImage.setImageResource(R.drawable.avatar);
                                  } else {
                                    Glide.with(getContext())
                                        .load(
                                            Uri.parse(
                                                dataSnapshot
                                                    .child("avatar")
                                                    .getValue(String.class)))
                                        .into(profileCardImage);
                                  }
                                }

                                if (dataSnapshot.child("nickname").getValue(String.class).equals("null")) {
                                  username.setText(
                                      "@"
                                          + dataSnapshot
                                              .child("username")
                                              .getValue(String.class));
                                } else {
                                  username.setText(
                                      dataSnapshot.child("nickname").getValue(String.class));
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
                                          verifiedBadge.setImageResource(
                                              R.drawable.verified_badge);
                                          verifiedBadge.setVisibility(View.VISIBLE);
                                        } else {
                                          verifiedBadge.setVisibility(View.GONE);
                                        }
                                      }
                                    }
                                  }
                                }
                              }
                            });
                      }

                      @Override
                      public void onCancelled(@NonNull DatabaseError databaseError) {}
                    });
              });
        }

        main.setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View _view) {
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra("uid", _data.get(_position).get("uid").toString());
                intent.putExtra("origin", "MessagesActivity");
                startActivity(intent);
              }
            });
      } catch (Exception e) {
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
