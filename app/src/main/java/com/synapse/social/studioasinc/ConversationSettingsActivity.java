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

public class ConversationSettingsActivity extends AppCompatActivity {

	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();

	private String UserAvatarUri = "";
	private HashMap<String, Object> block = new HashMap<>();
	private String user2nickname = "";

	private LinearLayout linear1;
	private LinearLayout linear2;
	private ScrollView vscroll;
	private ImageView backBtn;
	private TextView invisibleTxtAppbar;
	private LinearLayout linear3;
	private ImageView menuBtn;
	private LinearLayout container;
	private LinearLayout userInfoAndActionsContainer;
	private LinearLayout userInfo;
	private LinearLayout actionButtonGroup;
	private LinearLayout settingsContainer;
	private CardView cardProfilePictureWrapper;
	private TextView username;
	private Button encryptedButton;
	private ImageView profilePictureIV;
	private TextView conversationTitle;
	private CardView conversationCard;
	private TextView moreActionsTitle;
	private CardView moreActionsCard;
	private TextView privacyTitle;
	private CardView privacyCard;
	private LinearLayout linear14;
	private LinearLayout themeMainOption;
	private LinearLayout nicknameMainOption;
	private LinearLayout wordEffectMainOption;
	private LinearLayout quickReactionOption;
	private ImageView themeIcon;
	private LinearLayout linear13;
	private TextView themeTitle;
	private TextView themeSubtitle;
	private ImageView nicknameIcon;
	private LinearLayout linear16;
	private TextView nicknameTitle;
	private TextView nicknameSubtitle;
	private ImageView wordEffectIcon;
	private LinearLayout linear18;
	private TextView wordEffectTitle;
	private TextView wordEffectSubtitle;
	private ImageView quickReactionIcon;
	private LinearLayout linear20;
	private TextView quickReactionTitle;
	private TextView quickReactionSubtitle;
	private LinearLayout linear21;
	private CardView cardReadReceipt;
	private LinearLayout disappearingMainSwitch;
	private LinearLayout savePhotoVideoMainSwitch;
	private LinearLayout pinnedMessagesMainOption;
	private LinearLayout searchMessagesMainOption;
	private LinearLayout readReceiptsMainSwitch;
	private ImageView readReceiptsIcon;
	private LinearLayout linear26;
	private MaterialSwitch switchReadReceipt;
	private TextView readReceiptsTitle;
	private TextView readReceiptsSubtitle;
	private ImageView disappearingIcon;
	private LinearLayout linear27;
	private MaterialSwitch switchDisappearingMessages;
	private TextView disappearingTitle;
	private TextView disappearingSubtitle;
	private ImageView saveMediaIcon;
	private LinearLayout linear49;
	private MaterialSwitch switchAutoSaveMedia;
	private TextView saveMediaTitle;
	private TextView saveMediaSubtitle;
	private ImageView pinnedMessagesIcon;
	private LinearLayout linear28;
	private TextView pinnedMessagesTitle;
	private TextView pinnedMessagesSubtitle;
	private ImageView searchIcon;
	private LinearLayout linear29;
	private TextView searchTitle;
	private TextView searchSubtitle;
	private LinearLayout linear39;
	private LinearLayout e2eVerificationMainOption;
	private LinearLayout sharedContactsMainOption;
	private LinearLayout blockMainOption;
	private LinearLayout reportMainOption;
	private ImageView verifyEncryptionIcon;
	private LinearLayout linear44;
	private TextView verifyEncryptionTitle;
	private TextView verifyEncryptionSubtitle;
	private ImageView shareContactIcon;
	private LinearLayout linear45;
	private TextView shareContactTitle;
	private TextView shareContactSubtitle;
	private ImageView blockIcon;
	private LinearLayout blockLayout;
	private TextView blockTitle;
	private TextView blockSubtitle;
	private ImageView reportIcon;
	private LinearLayout linear47;
	private TextView reportTitle;
	private TextView reportSubtitle;

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
		setContentView(R.layout.activity_conversation_settings);
		initialize(_savedInstanceState);
		FirebaseApp.initializeApp(this);
		initializeLogic();
	}

	private void initialize(Bundle _savedInstanceState) {
		linear1 = findViewById(R.id.linear1);
		linear2 = findViewById(R.id.linear2);
		vscroll = findViewById(R.id.vscroll);
		backBtn = findViewById(R.id.backBtn);
		invisibleTxtAppbar = findViewById(R.id.invisibleTxtAppbar);
		linear3 = findViewById(R.id.linear3);
		menuBtn = findViewById(R.id.menuBtn);
		container = findViewById(R.id.container);
		userInfoAndActionsContainer = findViewById(R.id.userInfoAndActionsContainer);
		userInfo = findViewById(R.id.userInfo);
		actionButtonGroup = findViewById(R.id.actionButtonGroup);
		settingsContainer = findViewById(R.id.settingsContainer);
		cardProfilePictureWrapper = findViewById(R.id.cardProfilePictureWrapper);
		username = findViewById(R.id.username);
		encryptedButton = findViewById(R.id.encryptedButton);
		profilePictureIV = findViewById(R.id.profilePictureIV);
		conversationTitle = findViewById(R.id.conversationTitle);
		conversationCard = findViewById(R.id.conversationCard);
		moreActionsTitle = findViewById(R.id.moreActionsTitle);
		moreActionsCard = findViewById(R.id.moreActionsCard);
		privacyTitle = findViewById(R.id.privacyTitle);
		privacyCard = findViewById(R.id.privacyCard);
		linear14 = findViewById(R.id.linear14);
		themeMainOption = findViewById(R.id.themeMainOption);
		nicknameMainOption = findViewById(R.id.nicknameMainOption);
		wordEffectMainOption = findViewById(R.id.wordEffectMainOption);
		quickReactionOption = findViewById(R.id.quickReactionOption);
		themeIcon = findViewById(R.id.themeIcon);
		linear13 = findViewById(R.id.linear13);
		themeTitle = findViewById(R.id.themeTitle);
		themeSubtitle = findViewById(R.id.themeSubtitle);
		nicknameIcon = findViewById(R.id.nicknameIcon);
		linear16 = findViewById(R.id.linear16);
		nicknameTitle = findViewById(R.id.nicknameTitle);
		nicknameSubtitle = findViewById(R.id.nicknameSubtitle);
		wordEffectIcon = findViewById(R.id.wordEffectIcon);
		linear18 = findViewById(R.id.linear18);
		wordEffectTitle = findViewById(R.id.wordEffectTitle);
		wordEffectSubtitle = findViewById(R.id.wordEffectSubtitle);
		quickReactionIcon = findViewById(R.id.quickReactionIcon);
		linear20 = findViewById(R.id.linear20);
		quickReactionTitle = findViewById(R.id.quickReactionTitle);
		quickReactionSubtitle = findViewById(R.id.quickReactionSubtitle);
		linear21 = findViewById(R.id.linear21);
		cardReadReceipt = findViewById(R.id.cardReadReceipt);
		disappearingMainSwitch = findViewById(R.id.disappearingMainSwitch);
		savePhotoVideoMainSwitch = findViewById(R.id.savePhotoVideoMainSwitch);
		pinnedMessagesMainOption = findViewById(R.id.pinnedMessagesMainOption);
		searchMessagesMainOption = findViewById(R.id.searchMessagesMainOption);
		readReceiptsMainSwitch = findViewById(R.id.readReceiptsMainSwitch);
		readReceiptsIcon = findViewById(R.id.readReceiptsIcon);
		linear26 = findViewById(R.id.linear26);
		switchReadReceipt = findViewById(R.id.switchReadReceipt);
		readReceiptsTitle = findViewById(R.id.readReceiptsTitle);
		readReceiptsSubtitle = findViewById(R.id.readReceiptsSubtitle);
		disappearingIcon = findViewById(R.id.disappearingIcon);
		linear27 = findViewById(R.id.linear27);
		switchDisappearingMessages = findViewById(R.id.switchDisappearingMessages);
		disappearingTitle = findViewById(R.id.disappearingTitle);
		disappearingSubtitle = findViewById(R.id.disappearingSubtitle);
		saveMediaIcon = findViewById(R.id.saveMediaIcon);
		linear49 = findViewById(R.id.linear49);
		switchAutoSaveMedia = findViewById(R.id.switchAutoSaveMedia);
		saveMediaTitle = findViewById(R.id.saveMediaTitle);
		saveMediaSubtitle = findViewById(R.id.saveMediaSubtitle);
		pinnedMessagesIcon = findViewById(R.id.pinnedMessagesIcon);
		linear28 = findViewById(R.id.linear28);
		pinnedMessagesTitle = findViewById(R.id.pinnedMessagesTitle);
		pinnedMessagesSubtitle = findViewById(R.id.pinnedMessagesSubtitle);
		searchIcon = findViewById(R.id.searchIcon);
		linear29 = findViewById(R.id.linear29);
		searchTitle = findViewById(R.id.searchTitle);
		searchSubtitle = findViewById(R.id.searchSubtitle);
		linear39 = findViewById(R.id.linear39);
		e2eVerificationMainOption = findViewById(R.id.e2eVerificationMainOption);
		sharedContactsMainOption = findViewById(R.id.sharedContactsMainOption);
		blockMainOption = findViewById(R.id.blockMainOption);
		reportMainOption = findViewById(R.id.reportMainOption);
		verifyEncryptionIcon = findViewById(R.id.verifyEncryptionIcon);
		linear44 = findViewById(R.id.linear44);
		verifyEncryptionTitle = findViewById(R.id.verifyEncryptionTitle);
		verifyEncryptionSubtitle = findViewById(R.id.verifyEncryptionSubtitle);
		shareContactIcon = findViewById(R.id.shareContactIcon);
		linear45 = findViewById(R.id.linear45);
		shareContactTitle = findViewById(R.id.shareContactTitle);
		shareContactSubtitle = findViewById(R.id.shareContactSubtitle);
		blockIcon = findViewById(R.id.blockIcon);
		blockLayout = findViewById(R.id.blockLayout);
		blockTitle = findViewById(R.id.blockTitle);
		blockSubtitle = findViewById(R.id.blockSubtitle);
		reportIcon = findViewById(R.id.reportIcon);
		linear47 = findViewById(R.id.linear47);
		reportTitle = findViewById(R.id.reportTitle);
		reportSubtitle = findViewById(R.id.reportSubtitle);
		auth = FirebaseAuth.getInstance();

		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				finish();
			}
		});

		encryptedButton.setOnClickListener(new View.OnClickListener() {
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
