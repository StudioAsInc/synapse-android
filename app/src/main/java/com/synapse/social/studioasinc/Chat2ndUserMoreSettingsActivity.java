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
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.*;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.gridlayout.*;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.materialswitch.MaterialSwitch;
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
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;

public class Chat2ndUserMoreSettingsActivity extends AppCompatActivity {

	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();

	private String UserAvatarUri = "";
	private HashMap<String, Object> block = new HashMap<>();
	private String user2nickname = "";

	private LinearLayout linear1;
	private LinearLayout linear2;
	private ScrollView vscroll1;
	private ImageView back_btn;
	private TextView invisible_txt_appbar;
	private LinearLayout linear3;
	private ImageView menu_btn;
	private LinearLayout container;
	private LinearLayout user_info_and_actions_container;
	private LinearLayout user_info;
	private LinearLayout actionButtonGroup;
	private LinearLayout settings_container;
	private CardView cardProfilePictureWrapper;
	private TextView username;
	private Button button1;
	private ImageView profilePictureIV;
	private TextView textview8;
	private CardView cardview1;
	private TextView textview17;
	private CardView cardview2;
	private TextView textview34;
	private CardView cardview4;
	private LinearLayout linear14;
	private LinearLayout themeMainOption;
	private LinearLayout nicknameMainOption;
	private LinearLayout wordEffectMainOption;
	private LinearLayout quickReactionOption;
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
	private CardView cardReadReceipt;
	private LinearLayout disappearingMainSwitch;
	private LinearLayout savePhotoVideoMainSwitch;
	private LinearLayout pinnedMessagesMainOption;
	private LinearLayout searchMessagesMainOption;
	private LinearLayout readReceiptsMainSwitch;
	private ImageView imageview14;
	private LinearLayout linear26;
	private MaterialSwitch switchReadReceipt;
	private TextView textview18;
	private TextView textview19;
	private ImageView imageview15;
	private LinearLayout linear27;
	private MaterialSwitch switchDisappearingMessages;
	private TextView textview20;
	private TextView textview21;
	private ImageView imageview26;
	private LinearLayout linear49;
	private MaterialSwitch switchAutoSaveMedia;
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
	private LinearLayout e2eVerificationMainOption;
	private LinearLayout sharedContactsMainOption;
	private LinearLayout blockMainOption;
	private LinearLayout reportMainOption;
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

	private DatabaseReference main = _firebase.getReference("skyline");
	private ChildEventListener _main_child_listener;
	private DatabaseReference blocklist = _firebase.getReference("skyline/blocklist");
	private ChildEventListener _blocklist_child_listener;
	private Intent i = new Intent();
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

	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.chat_2nd_user_more_settings);
		initialize(_savedInstanceState);
		FirebaseApp.initializeApp(this);
		initializeLogic();
	}

	private void initialize(Bundle _savedInstanceState) {
		linear1 = findViewById(R.id.linear1);
		linear2 = findViewById(R.id.linear2);
		vscroll1 = findViewById(R.id.vscroll1);
		back_btn = findViewById(R.id.back_btn);
		invisible_txt_appbar = findViewById(R.id.invisible_txt_appbar);
		linear3 = findViewById(R.id.linear3);
		menu_btn = findViewById(R.id.menu_btn);
		container = findViewById(R.id.container);
		user_info_and_actions_container = findViewById(R.id.user_info_and_actions_container);
		user_info = findViewById(R.id.user_info);
		actionButtonGroup = findViewById(R.id.actionButtonGroup);
		settings_container = findViewById(R.id.settings_container);
		cardProfilePictureWrapper = findViewById(R.id.cardProfilePictureWrapper);
		username = findViewById(R.id.username);
		button1 = findViewById(R.id.button1);
		profilePictureIV = findViewById(R.id.profilePictureIV);
		textview8 = findViewById(R.id.textview8);
		cardview1 = findViewById(R.id.cardview1);
		textview17 = findViewById(R.id.textview17);
		cardview2 = findViewById(R.id.cardview2);
		textview34 = findViewById(R.id.textview34);
		cardview4 = findViewById(R.id.cardview4);
		linear14 = findViewById(R.id.linear14);
		themeMainOption = findViewById(R.id.themeMainOption);
		nicknameMainOption = findViewById(R.id.nicknameMainOption);
		wordEffectMainOption = findViewById(R.id.wordEffectMainOption);
		quickReactionOption = findViewById(R.id.quickReactionOption);
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
		cardReadReceipt = findViewById(R.id.cardReadReceipt);
		disappearingMainSwitch = findViewById(R.id.disappearingMainSwitch);
		savePhotoVideoMainSwitch = findViewById(R.id.savePhotoVideoMainSwitch);
		pinnedMessagesMainOption = findViewById(R.id.pinnedMessagesMainOption);
		searchMessagesMainOption = findViewById(R.id.searchMessagesMainOption);
		readReceiptsMainSwitch = findViewById(R.id.readReceiptsMainSwitch);
		imageview14 = findViewById(R.id.imageview14);
		linear26 = findViewById(R.id.linear26);
		switchReadReceipt = findViewById(R.id.switchReadReceipt);
		textview18 = findViewById(R.id.textview18);
		textview19 = findViewById(R.id.textview19);
		imageview15 = findViewById(R.id.imageview15);
		linear27 = findViewById(R.id.linear27);
		switchDisappearingMessages = findViewById(R.id.switchDisappearingMessages);
		textview20 = findViewById(R.id.textview20);
		textview21 = findViewById(R.id.textview21);
		imageview26 = findViewById(R.id.imageview26);
		linear49 = findViewById(R.id.linear49);
		switchAutoSaveMedia = findViewById(R.id.switchAutoSaveMedia);
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
		e2eVerificationMainOption = findViewById(R.id.e2eVerificationMainOption);
		sharedContactsMainOption = findViewById(R.id.sharedContactsMainOption);
		blockMainOption = findViewById(R.id.blockMainOption);
		reportMainOption = findViewById(R.id.reportMainOption);
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

		back_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				finish();
			}
		});

		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {

			}
		});

		cardReadReceipt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				switchReadReceipt.performClick();
			}
		});

		disappearingMainSwitch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				switchDisappearingMessages.performClick();
			}
		});

		savePhotoVideoMainSwitch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				switchAutoSaveMedia.performClick();
			}
		});

		blockMainOption.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_Block(getIntent().getStringExtra("uid"));
			}
		});

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

		_blocklist_child_listener = new ChildEventListener() {
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
		blocklist.addChildEventListener(_blocklist_child_listener);

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
		_getUserReference();
	}

	@Override
	public void onBackPressed() {
		finish();
	}
	public void _ImgRound(final ImageView _imageview, final double _value) {
		android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable ();
		gd.setColor(android.R.color.transparent);
		gd.setCornerRadius((int)_value);
		_imageview.setClipToOutline(true);
		_imageview.setBackground(gd);
	}


	public void _rippleRoundStroke(final View _view, final String _focus, final String _pressed, final double _round, final double _stroke, final String _strokeclr) {
		android.graphics.drawable.GradientDrawable GG = new android.graphics.drawable.GradientDrawable();
		GG.setColor(Color.parseColor(_focus));
		GG.setCornerRadius((float)_round);
		GG.setStroke((int) _stroke,
		Color.parseColor("#" + _strokeclr.replace("#", "")));
		android.graphics.drawable.RippleDrawable RE = new android.graphics.drawable.RippleDrawable(new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{ Color.parseColor("#FF757575")}), GG, null);
		_view.setBackground(RE);
	}


	public void _getUserReference() {
		DatabaseReference getUserReference = FirebaseDatabase.getInstance().getReference("skyline/users").child(getIntent().getStringExtra("uid"));
		getUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				if(dataSnapshot.exists()) {
					if (dataSnapshot.child("banned").getValue(String.class).equals("true")) {
						UserAvatarUri = "null";
						profilePictureIV.setImageResource(R.drawable.banned_avatar);
						profilePictureIV.setImageResource(R.drawable.banned_cover_photo);
					} else {
						UserAvatarUri = dataSnapshot.child("avatar").getValue(String.class);
						if (dataSnapshot.child("avatar").getValue(String.class).equals("null")) {
							profilePictureIV.setImageResource(R.drawable.avatar);
						} else {
							Glide.with(getApplicationContext()).load(Uri.parse(dataSnapshot.child("avatar").getValue(String.class))).into(profilePictureIV);
						}
					}
					if (dataSnapshot.child("nickname").getValue(String.class).equals("null")) {
						username.setText("@" + dataSnapshot.child("username").getValue(String.class));
						user2nickname = "@" + dataSnapshot.child("username").getValue(String.class);
					} else {
						username.setText(dataSnapshot.child("nickname").getValue(String.class));
						user2nickname = dataSnapshot.child("nickname").getValue(String.class);
					}
				} else {
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {

			}
		});
	}


	public void _Block(final String _uid) {
		block = new HashMap<>();
		block.put(_uid, _uid);
		blocklist.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(block);
		block.clear();
	}

}