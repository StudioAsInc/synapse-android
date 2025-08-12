package com.synapse.social.studioasinc;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import com.bumptech.glide.Glide;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;

public class Chat2ndUserMoreSettingsActivity extends AppCompatActivity {

  private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();

  private String UserAvatarUri = "";
  private HashMap<String, Object> block = new HashMap<>();
  private String user2nickname = "";

  private Toolbar toolbar;
  private NestedScrollView vscroll1;
  private LinearLayout container;
  private LinearLayout user_info_and_actions_container;
  private LinearLayout user_info;
  private LinearLayout action_btn_container;
  private LinearLayout settings_container;
  private ImageView user_profile_picture;
  private TextView username;
  private Button button1;
  private LinearLayout audio_call_btn;
  private LinearLayout video_call_btn;
  private LinearLayout mute_btn;
  private LinearLayout delete_chat_btn;
  private LinearLayout round_this1;
  private TextView textview4;
  private ImageView imageview6;
  private LinearLayout round_this2;
  private TextView textview5;
  private ImageView imageview7;
  private LinearLayout round_this3;
  private TextView textview6;
  private ImageView imageview8;
  private LinearLayout round_this4;
  private TextView textview7;
  private ImageView imageview9;
  private TextView textview8;
  private CardView cardview1;
  private TextView textview17;
  private CardView cardview2;
  private TextView textview34;
  private CardView cardview4;
  private LinearLayout linear14;
  private LinearLayout linear12;
  private LinearLayout linear15;
  private LinearLayout linear17;
  private LinearLayout linear19;
  private ImageView imageview10;
  private LinearLayout linear13;
  private TextView textview10;
  private TextView textview9;
  private ImageView imageview11;
  private LinearLayout linear16;
  private TextView textview11;
  private TextView textview12;
  private ImageView imageview12;
  private LinearLayout linear18;
  private TextView textview13;
  private TextView textview14;
  private ImageView imageview13;
  private LinearLayout linear20;
  private TextView textview15;
  private TextView textview16;
  private LinearLayout linear21;
  private LinearLayout linear22;
  private LinearLayout linear23;
  private LinearLayout linear48;
  private LinearLayout linear24;
  private LinearLayout linear25;
  private ImageView imageview14;
  private LinearLayout linear26;
  private MaterialSwitch Read_recipt_switch;
  private TextView textview18;
  private TextView textview19;
  private ImageView imageview15;
  private LinearLayout linear27;
  private MaterialSwitch disapear_switch;
  private TextView textview20;
  private TextView textview21;
  private ImageView imageview26;
  private LinearLayout linear49;
  private MaterialSwitch autosavephoto_switch;
  private TextView textview44;
  private TextView textview45;
  private ImageView imageview16;
  private LinearLayout linear28;
  private TextView textview22;
  private TextView textview23;
  private ImageView imageview17;
  private LinearLayout linear29;
  private TextView textview24;
  private TextView textview25;
  private LinearLayout linear39;
  private LinearLayout linear40;
  private LinearLayout linear41;
  private LinearLayout block_btn;
  private LinearLayout linear43;
  private ImageView imageview22;
  private LinearLayout linear44;
  private TextView textview36;
  private TextView textview37;
  private ImageView imageview23;
  private LinearLayout linear45;
  private TextView textview38;
  private TextView textview39;
  private ImageView imageview24;
  private LinearLayout block_lay1;
  private TextView textview40;
  private TextView textview41;
  private ImageView imageview25;
  private LinearLayout linear47;
  private TextView textview42;
  private TextView textview43;

  private DatabaseReference blocklist = _firebase.getReference("skyline/blocklist");
  private Intent i = new Intent();
  private FirebaseAuth auth;

  @Override
  protected void onCreate(Bundle _savedInstanceState) {
    super.onCreate(_savedInstanceState);
    setContentView(R.layout.chat_2nd_user_more_settings);
    initialize(_savedInstanceState);
    FirebaseApp.initializeApp(this);
    initializeLogic();
  }

  private void initialize(Bundle _savedInstanceState) {
    vscroll1 = findViewById(R.id.vscroll1);
    container = findViewById(R.id.container);
    user_info_and_actions_container = findViewById(R.id.user_info_and_actions_container);
    user_info = findViewById(R.id.user_info);
    action_btn_container = findViewById(R.id.action_btn_container);
    settings_container = findViewById(R.id.settings_container);
    user_profile_picture = findViewById(R.id.user_profile_picture);
    username = findViewById(R.id.username);
    button1 = findViewById(R.id.button1);
    audio_call_btn = findViewById(R.id.audio_call_btn);
    video_call_btn = findViewById(R.id.video_call_btn);
    mute_btn = findViewById(R.id.mute_btn);
    delete_chat_btn = findViewById(R.id.delete_chat_btn);
    round_this1 = findViewById(R.id.round_this1);
    textview4 = findViewById(R.id.textview4);
    imageview6 = findViewById(R.id.imageview6);
    round_this2 = findViewById(R.id.round_this2);
    textview5 = findViewById(R.id.textview5);
    imageview7 = findViewById(R.id.imageview7);
    round_this3 = findViewById(R.id.round_this3);
    textview6 = findViewById(R.id.textview6);
    imageview8 = findViewById(R.id.imageview8);
    round_this4 = findViewById(R.id.round_this4);
    textview7 = findViewById(R.id.textview7);
    imageview9 = findViewById(R.id.imageview9);
    textview8 = findViewById(R.id.textview8);
    cardview1 = findViewById(R.id.cardview1);
    textview17 = findViewById(R.id.textview17);
    cardview2 = findViewById(R.id.cardview2);
    textview34 = findViewById(R.id.textview34);
    cardview4 = findViewById(R.id.cardview4);
    linear14 = findViewById(R.id.linear14);
    linear12 = findViewById(R.id.linear12);
    linear15 = findViewById(R.id.linear15);
    linear17 = findViewById(R.id.linear17);
    linear19 = findViewById(R.id.linear19);
    imageview10 = findViewById(R.id.imageview10);
    linear13 = findViewById(R.id.linear13);
    textview10 = findViewById(R.id.textview10);
    textview9 = findViewById(R.id.textview9);
    imageview11 = findViewById(R.id.imageview11);
    linear16 = findViewById(R.id.linear16);
    textview11 = findViewById(R.id.textview11);
    textview12 = findViewById(R.id.textview12);
    imageview12 = findViewById(R.id.imageview12);
    linear18 = findViewById(R.id.linear18);
    textview13 = findViewById(R.id.textview13);
    textview14 = findViewById(R.id.textview14);
    imageview13 = findViewById(R.id.imageview13);
    linear20 = findViewById(R.id.linear20);
    textview15 = findViewById(R.id.textview15);
    textview16 = findViewById(R.id.textview16);
    linear21 = findViewById(R.id.linear21);
    linear22 = findViewById(R.id.linear22);
    linear23 = findViewById(R.id.linear23);
    linear48 = findViewById(R.id.linear48);
    linear24 = findViewById(R.id.linear24);
    linear25 = findViewById(R.id.linear25);
    imageview14 = findViewById(R.id.imageview14);
    linear26 = findViewById(R.id.linear26);
    Read_recipt_switch = findViewById(R.id.Read_recipt_switch);
    textview18 = findViewById(R.id.textview18);
    textview19 = findViewById(R.id.textview19);
    imageview15 = findViewById(R.id.imageview15);
    linear27 = findViewById(R.id.linear27);
    disapear_switch = findViewById(R.id.disapear_switch);
    textview20 = findViewById(R.id.textview20);
    textview21 = findViewById(R.id.textview21);
    imageview26 = findViewById(R.id.imageview26);
    linear49 = findViewById(R.id.linear49);
    autosavephoto_switch = findViewById(R.id.autosavephoto_switch);
    textview44 = findViewById(R.id.textview44);
    textview45 = findViewById(R.id.textview45);
    imageview16 = findViewById(R.id.imageview16);
    linear28 = findViewById(R.id.linear28);
    textview22 = findViewById(R.id.textview22);
    textview23 = findViewById(R.id.textview23);
    imageview17 = findViewById(R.id.imageview17);
    linear29 = findViewById(R.id.linear29);
    textview24 = findViewById(R.id.textview24);
    textview25 = findViewById(R.id.textview25);
    linear39 = findViewById(R.id.linear39);
    linear40 = findViewById(R.id.linear40);
    linear41 = findViewById(R.id.linear41);
    block_btn = findViewById(R.id.block_btn);
    linear43 = findViewById(R.id.linear43);
    imageview22 = findViewById(R.id.imageview22);
    linear44 = findViewById(R.id.linear44);
    textview36 = findViewById(R.id.textview36);
    textview37 = findViewById(R.id.textview37);
    imageview23 = findViewById(R.id.imageview23);
    linear45 = findViewById(R.id.linear45);
    textview38 = findViewById(R.id.textview38);
    textview39 = findViewById(R.id.textview39);
    imageview24 = findViewById(R.id.imageview24);
    block_lay1 = findViewById(R.id.block_lay1);
    textview40 = findViewById(R.id.textview40);
    textview41 = findViewById(R.id.textview41);
    imageview25 = findViewById(R.id.imageview25);
    linear47 = findViewById(R.id.linear47);
    textview42 = findViewById(R.id.textview42);
    textview43 = findViewById(R.id.textview43);
    auth = FirebaseAuth.getInstance();
    toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    button1.setOnClickListener(
        _view -> {
          // TODO
        });

    delete_chat_btn.setOnClickListener(
        _view -> {
          i.setClass(getApplicationContext(), ProfileActivity.class);
          i.putExtra("uid", getIntent().getStringExtra("uid"));
          startActivity(i);
        });

    linear23.setOnClickListener(
        _view -> {
          i.setClass(getApplicationContext(), DisappearingMessageSettingsActivity.class);
          startActivity(i);
        });

    block_btn.setOnClickListener(
        _view -> {
          _Block(getIntent().getStringExtra("uid"));
        });
  }

  private void initializeLogic() {
    _UI();
    _getUserReference();
  }

  @Override
  public void onBackPressed() {
    finish();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  public void _ImgRound(final ImageView _imageview, final double _value) {
    android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable();
    gd.setColor(android.R.color.transparent);
    gd.setCornerRadius((int) _value);
    _imageview.setClipToOutline(true);
    _imageview.setBackground(gd);
  }

  public void _rippleRoundStroke(
      final View _view,
      final String _focus,
      final String _pressed,
      final double _round,
      final double _stroke,
      final String _strokeclr) {
    android.graphics.drawable.GradientDrawable GG = new android.graphics.drawable.GradientDrawable();
    GG.setColor(android.graphics.Color.parseColor(_focus));
    GG.setCornerRadius((float) _round);
    GG.setStroke(
        (int) _stroke, android.graphics.Color.parseColor("#" + _strokeclr.replace("#", "")));
    android.graphics.drawable.RippleDrawable RE =
        new android.graphics.drawable.RippleDrawable(
            new android.content.res.ColorStateList(
                new int[][] {new int[] {}}, new int[] {0xFF757575}),
            GG,
            null);
    _view.setBackground(RE);
  }

  public void _UI() {
    _rippleRoundStroke(round_this1, "#F5F5F5", "#F4F4F4", 300, 0, "#000000");
    _rippleRoundStroke(round_this2, "#F5F5F5", "#F4F4F4", 300, 0, "#000000");
    _rippleRoundStroke(round_this3, "#F5F5F5", "#F4F4F4", 300, 0, "#000000");
    _rippleRoundStroke(round_this4, "#F5F5F5", "#F4F4F4", 300, 0, "#000000");
    _ImgRound(user_profile_picture, 360);

    vscroll1.setHorizontalScrollBarEnabled(false);
    vscroll1.setVerticalScrollBarEnabled(false);
  }

  public void _getUserReference() {
    DatabaseReference getUserReference =
        FirebaseDatabase.getInstance().getReference("skyline/users").child(getIntent().getStringExtra("uid"));
    getUserReference.addListenerForSingleValueEvent(
        new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
              if (dataSnapshot.child("banned").getValue(String.class).equals("true")) {
                UserAvatarUri = "null";
                user_profile_picture.setImageResource(R.drawable.banned_avatar);
              } else {
                UserAvatarUri = dataSnapshot.child("avatar").getValue(String.class);
                if (dataSnapshot.child("avatar").getValue(String.class).equals("null")) {
                  user_profile_picture.setImageResource(R.drawable.avatar);
                } else {
                  Glide.with(getApplicationContext())
                      .load(Uri.parse(dataSnapshot.child("avatar").getValue(String.class)))
                      .into(user_profile_picture);
                }
              }
              if (dataSnapshot.child("nickname").getValue(String.class).equals("null")) {
                username.setText("@" + dataSnapshot.child("username").getValue(String.class));
                user2nickname = "@" + dataSnapshot.child("username").getValue(String.class);
                getSupportActionBar().setTitle(user2nickname);
              } else {
                username.setText(dataSnapshot.child("nickname").getValue(String.class));
                user2nickname = dataSnapshot.child("nickname").getValue(String.class);
                getSupportActionBar().setTitle(user2nickname);
              }
            } else {
            }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
  }

  public void _Block(final String _uid) {
    block = new HashMap<>();
    block.put(_uid, _uid);
    blocklist
        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
        .updateChildren(block);
    block.clear();
  }
}
