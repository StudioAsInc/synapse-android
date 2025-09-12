package com.synapse.social.studioasinc;

import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.content.*;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.*;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.gridlayout.*;
import com.google.firebase.FirebaseApp;
import com.theartofdev.edmodo.cropper.*;
import com.yalantis.ucrop.*;
import java.io.*;
import java.io.InputStream;
import java.text.*;
import java.util.*;
import java.util.regex.*;
import org.json.*;

public class SettingsActivity extends AppCompatActivity {
	
	private SharedPreferences appSettings;
	private Intent intent = new Intent();
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.settings);
		initialize(_savedInstanceState);
		FirebaseApp.initializeApp(this);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		appSettings = getSharedPreferences("appSettings", Activity.MODE_PRIVATE);
	}
	
	private void initializeLogic() {
		LinearLayout mainLayout = findViewById(R.id._main);

		// Create a title TextView
		TextView titleTextView = new TextView(this);
		titleTextView.setText("Gemini API Key");
		titleTextView.setTextSize(20);
		titleTextView.setTypeface(null, android.graphics.Typeface.BOLD);
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		titleParams.setMargins(16, 16, 16, 8);
		titleTextView.setLayoutParams(titleParams);
		mainLayout.addView(titleTextView);

		// Create an EditText for the API key
		EditText apiKeyEditText = new EditText(this);
		apiKeyEditText.setHint("Enter your Gemini API Key");
		LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		editTextParams.setMargins(16, 8, 16, 8);
		apiKeyEditText.setLayoutParams(editTextParams);
		mainLayout.addView(apiKeyEditText);

		// Load the saved API key and set it to the EditText
		String savedApiKey = appSettings.getString("gemini_api_key", "");
		apiKeyEditText.setText(savedApiKey);

		// Create a Save button
		Button saveButton = new Button(this);
		saveButton.setText("Save");
		LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		buttonParams.setMargins(16, 8, 16, 16);
		saveButton.setLayoutParams(buttonParams);
		mainLayout.addView(saveButton);

		// Set an OnClickListener for the Save button
		saveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String apiKey = apiKeyEditText.getText().toString();
				SharedPreferences.Editor editor = appSettings.edit();
				editor.putString("gemini_api_key", apiKey);
				editor.apply();
				Toast.makeText(SettingsActivity.this, "API Key saved!", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser() != null) {
			PresenceManager.setActivity(com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser().getUid(), "In Settings");
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	
}