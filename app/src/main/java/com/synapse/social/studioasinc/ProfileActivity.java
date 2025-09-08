package com.synapse.social.studioasinc;

import android.animation.*;
import android.app.*;
import android.content.*;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.*;
import android.graphics.*;
import android.graphics.Typeface;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.os.Bundle;
import android.os.Vibrator;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.*;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.gridlayout.*;
import androidx.recyclerview.widget.*;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;
import androidx.viewpager2.widget.ViewPager2;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButtonGroup;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
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
import com.synapse.social.studioasinc.R;
import com.theartofdev.edmodo.cropper.*;
import com.yalantis.ucrop.*;
import java.io.*;
import java.io.InputStream;
import java.text.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.*;
import org.json.*;
import androidx.core.widget.NestedScrollView;
import com.google.firebase.database.Query;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.browser.customtabs.CustomTabsIntent;

public class ProfileActivity extends AppCompatActivity {
	
	private Timer _timer = new Timer();
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	
	private HashMap<String, Object> UserInfoCacheMap = new HashMap<>();
	private HashMap<String, Object> postLikeCountCache = new HashMap<>();
	private String UserAvatarUri = "";
	private HashMap<String, Object> mSendHistoryMap = new HashMap<>();
	private String handle = "";
	private String object_clicked = "";
	private String nickname = "";
	private String AndroidDevelopersBlogURL = "";
	
	private ArrayList<HashMap<String, Object>> UserPostsList = new ArrayList<>();
	
	private MaterialToolbar topAppBar;
	private ViewPager2 viewPager;
	private TabLayout tabLayout;
	
	private Intent intent = new Intent();
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
	private Vibrator vbr;
	private Calendar cc = Calendar.getInstance();

class c {
	Context co;
	public <T extends Activity> c(T a) {
		co = a;
	}
	public <T extends Fragment> c(T a) {
		co = a.getActivity();
	}
	public <T extends DialogFragment> c(T a) {
		co = a.getActivity();
	}

	public Context getContext() {
		return co;
	}

}
	private RequestNetwork req;
	private RequestNetwork.RequestListener _req_request_listener;
	private Calendar JoinDateCC = Calendar.getInstance();
	private DatabaseReference maindb = _firebase.getReference("/");
	private ChildEventListener _maindb_child_listener;
	private TimerTask after;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.profile);
		initialize();
		FirebaseApp.initializeApp(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser() != null) {
			PresenceManager.setActivity(com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser().getUid(), "In Profile");
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	
	private void initialize() {
		topAppBar = findViewById(R.id.top_app_bar);
		viewPager = findViewById(R.id.view_pager);
		tabLayout = findViewById(R.id.ProfilePageTabLayout);

		setSupportActionBar(topAppBar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		String uid = getIntent().getStringExtra("uid");
		ProfileFragmentAdapter adapter = new ProfileFragmentAdapter(this, uid);
		viewPager.setAdapter(adapter);

		new TabLayoutMediator(tabLayout, viewPager,
				(tab, position) -> {
					if (position == 0) {
						tab.setText(R.string.profile_tab);
					} else {
						tab.setText(R.string.posts_tab);
					}
				}
		).attach();
		
		topAppBar.setNavigationOnClickListener(v -> onBackPressed());

		auth = FirebaseAuth.getInstance();
		vbr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		req = new RequestNetwork(this);
	}
	
public class ProfileActivity extends AppCompatActivity {
	
	private Timer _timer = new Timer();
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	
	private HashMap<String, Object> UserInfoCacheMap = new HashMap<>();
	private HashMap<String, Object> postLikeCountCache = new HashMap<>();
	private String UserAvatarUri = "";
	private HashMap<String, Object> mSendHistoryMap = new HashMap<>();
	private String handle = "";
	private String object_clicked = "";
	private String nickname = "";
	private String AndroidDevelopersBlogURL = "";
	
	private ArrayList<HashMap<String, Object>> UserPostsList = new ArrayList<>();
	
	private MaterialToolbar topAppBar;
	private ViewPager2 viewPager;
	private TabLayout tabLayout;
	
	private Intent intent = new Intent();
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
	private Vibrator vbr;
	private Calendar cc = Calendar.getInstance();

class c {
	Context co;
	public <T extends Activity> c(T a) {
		co = a;
	}
	public <T extends Fragment> c(T a) {
		co = a.getActivity();
	}
	public <T extends DialogFragment> c(T a) {
		co = a.getActivity();
	}

	public Context getContext() {
		return co;
	}

}
	private RequestNetwork req;
	private RequestNetwork.RequestListener _req_request_listener;
	private Calendar JoinDateCC = Calendar.getInstance();
	private DatabaseReference maindb = _firebase.getReference("/");
	private ChildEventListener _maindb_child_listener;
	private TimerTask after;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.profile);
		initialize();
		FirebaseApp.initializeApp(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser() != null) {
			PresenceManager.setActivity(com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser().getUid(), "In Profile");
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	
	private void initialize() {
		topAppBar = findViewById(R.id.top_app_bar);
		viewPager = findViewById(R.id.view_pager);
		tabLayout = findViewById(R.id.ProfilePageTabLayout);

		setSupportActionBar(topAppBar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		String uid = getIntent().getStringExtra("uid");
		ProfileFragmentAdapter adapter = new ProfileFragmentAdapter(this, uid);
		viewPager.setAdapter(adapter);

		new TabLayoutMediator(tabLayout, viewPager,
				(tab, position) -> {
					if (position == 0) {
						tab.setText(R.string.profile_tab);
					} else {
						tab.setText(R.string.posts_tab);
					}
				}
		).attach();
		
		topAppBar.setNavigationOnClickListener(v -> onBackPressed());

		auth = FirebaseAuth.getInstance();
		vbr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		req = new RequestNetwork(this);
	}

}