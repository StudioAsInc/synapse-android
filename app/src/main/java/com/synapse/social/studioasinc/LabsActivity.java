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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.*;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.gridlayout.*;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.FirebaseApp;
import com.theartofdev.edmodo.cropper.*;
import com.yalantis.ucrop.*;
import java.io.*;
import java.io.InputStream;
import java.text.*;
import java.util.*;
import java.util.regex.*;
import org.json.*;

public class LabsActivity extends AppCompatActivity {
	
	private ScrollView vscroll2;
	private LinearLayout linear2;
	private MaterialToolbar toolbar;
	private MaterialCardView cardGroupChat;
	private MaterialCardView cardOldInbox;
	private MaterialCardView cardSettings;
	private MaterialCardView cardPlaceholder;
	private TextView textview4;
	private TextView textview5;
	private TextView textview6;
	
	private Intent intent = new Intent();
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.labs);
		initialize(_savedInstanceState);
		FirebaseApp.initializeApp(this);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		// Setup toolbar
		toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		// Initialize views
		vscroll2 = findViewById(R.id.vscroll2);
		linear2 = findViewById(R.id.linear2);
		cardGroupChat = findViewById(R.id.cardGroupChat);
		cardOldInbox = findViewById(R.id.cardOldInbox);
		cardSettings = findViewById(R.id.cardSettings);
		cardPlaceholder = findViewById(R.id.cardPlaceholder);
		textview4 = findViewById(R.id.textview4);
		textview5 = findViewById(R.id.textview5);
		textview6 = findViewById(R.id.textview6);
		
		// Setup click listeners
		cardGroupChat.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				// Navigate to new Group Chat InboxActivity
				intent.setClass(getApplicationContext(), com.synapse.social.studioasinc.groupchat.presentation.ui.InboxActivity.class);
				startActivity(intent);
			}
		});
		
		cardOldInbox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				intent.setClass(getApplicationContext(), MessagesActivity.class);
				startActivity(intent);
			}
		});
		
		cardSettings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				intent.setClass(getApplicationContext(), SettingsActivity.class);
				startActivity(intent);
			}
		});
		
		cardPlaceholder.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				// Show toast for coming soon feature
				Toast.makeText(LabsActivity.this, "This feature is coming soon! 🚀", Toast.LENGTH_SHORT).show();
			}
		});
		
		// Handle toolbar navigation
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
	
	private void initializeLogic() {
	}
	
}