package com.synapse.social.studioasinc;

import android.Manifest;
import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.os.Vibrator;
import android.text.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.browser.*;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.gridlayout.*;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.synapse.social.studioasinc.FadeEditText;
import com.theartofdev.edmodo.cropper.*;
import com.yalantis.ucrop.*;
import java.io.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;
import androidx.appcompat.widget.SwitchCompat;
import com.synapse.social.studioasinc.UploadFiles;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.content.ClipData;


public class CreatePostActivity extends AppCompatActivity {
	
	public final int REQ_CD_FILE_PICKER = 101;
	
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	private FirebaseStorage _firebase_storage = FirebaseStorage.getInstance();
	
	private ProgressDialog SynapseLoadingDialog;
	private String UniquePostKey = "";
	private HashMap<String, Object> PostSendMap = new HashMap<>();
	private String selectedImagePath = "";
	private String selectedVideoPath = "";
	private boolean hasImage = false;
	private boolean hasVideo = false;
	private String mediaType = "none"; // "none", "image", "video"
	
	private LinearLayout main;
	private LinearLayout top;
	private LinearLayout topSpace;
	private ScrollView scroll;
	private ImageView back;
	private LinearLayout topSpc;
	private Button postButton;
	private TextView title;
	private LinearLayout scrollBody;
	private LinearLayout PostInfoTop1;
	private LinearLayout topSpace2;
	private LinearLayout spc2;
	private CardView imageCard;
	private FadeEditText postDescription;
	private ImageView postImageView;
	private VideoView postVideoView;
	private ProgressBar downloadProgressBar;
	private LinearLayout imagePlaceholder;
	private LinearLayout settingsButton;
	private LinearLayout addUrlButton;
	private DatabaseReference maindb = _firebase.getReference("skyline");
	private Calendar cc = Calendar.getInstance();
	private SharedPreferences appSavedData;
	private DatabaseReference fdb = _firebase.getReference("notify");
	
	// Post settings variables
	private boolean hideViewsCount = false;
	private boolean hideLikesCount = false;
	private boolean hideCommentsCount = false;
	private boolean hidePostFromEveryone = false;
	private boolean disableSaveToFavorites = false;
	private boolean disableComments = false;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.create_post);
		initialize(_savedInstanceState);
		FirebaseApp.initializeApp(this);
		
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
		|| ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);} else {
			initializeLogic();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (FirebaseAuth.getInstance().getCurrentUser() != null) {
			PresenceManager.setActivity(FirebaseAuth.getInstance().getCurrentUser().getUid(), "Creating a post");
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1000) {
			initializeLogic();
		}
	}
	
	private void initialize(Bundle _savedInstanceState) {
		main = findViewById(R.id.main);
		top = findViewById(R.id.top);
		topSpace = findViewById(R.id.topSpace);
		scroll = findViewById(R.id.scroll);
		back = findViewById(R.id.back);
		topSpc = findViewById(R.id.topSpc);
		postButton = findViewById(R.id.postButton);
		title = findViewById(R.id.title);
		scrollBody = findViewById(R.id.scrollBody);
		PostInfoTop1 = findViewById(R.id.PostInfoTop1);
		topSpace2 = findViewById(R.id.topSpace2);
		spc2 = findViewById(R.id.spc2);
		imageCard = findViewById(R.id.imageCard);
		postDescription = findViewById(R.id.postDescription);
		postImageView = findViewById(R.id.postImageView);
		postVideoView = findViewById(R.id.postVideoView);
		downloadProgressBar = findViewById(R.id.downloadProgressBar);
		imagePlaceholder = findViewById(R.id.imagePlaceholder);
		settingsButton = findViewById(R.id.settingsButton);
		addUrlButton = findViewById(R.id.addUrlButton);
		appSavedData = getSharedPreferences("data", Activity.MODE_PRIVATE);
		
		// Setup MediaController for VideoView
		MediaController mediaController = new MediaController(this);
		mediaController.setAnchorView(postVideoView);
		postVideoView.setMediaController(mediaController);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				onBackPressed();
			}
		});
		
		postButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_createPost();
			}
		});
		
		imagePlaceholder.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_openFilePicker();
			}
		});
		
		View.OnClickListener mediaClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final CharSequence[] items = { "Change Media", "Remove Media", "Cancel" };
				AlertDialog.Builder builder = new AlertDialog.Builder(CreatePostActivity.this);
				builder.setTitle("Media Options");
				builder.setItems(items, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int item) {
						if (items[item].equals("Change Media")) {
							_openFilePicker();
						} else if (items[item].equals("Remove Media")) {
							_resetMediaSelection();
						} else if (items[item].equals("Cancel")) {
							dialog.dismiss();
						}
					}
				});
				builder.show();
			}
		};

		postImageView.setOnClickListener(mediaClickListener);
		postVideoView.setOnClickListener(mediaClickListener);

		settingsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_showPostSettingsBottomSheet();
			}
		});
		
		addUrlButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_showAddFromUrlDialog();
			}
		});

		postDescription.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				_TransitionManager(PostInfoTop1, 130);
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
	}
	
	private void initializeLogic() {
		// Removed hardcoded color settings to allow theme to control the UI
		
		// Check if we have an image from intent
		if (getIntent().hasExtra("type") && getIntent().hasExtra("path")) {
			selectedImagePath = getIntent().getStringExtra("path");
			hasImage = true;
			_loadSelectedImage();
		}
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		if (_requestCode == REQ_CD_FILE_PICKER && _resultCode == Activity.RESULT_OK && _data != null) {
			Uri selectedFileUri = _data.getData();
			if (selectedFileUri != null) {
				String mimeType = getContentResolver().getType(selectedFileUri);
				String filePath = StorageUtil.getPathFromUri(getApplicationContext(), selectedFileUri);
				
				if (filePath == null) {
					Toast.makeText(getApplicationContext(), "Failed to get file path.", Toast.LENGTH_SHORT).show();
					return;
				}
				
				_resetMediaSelection();

				if (mimeType != null && mimeType.startsWith("image/")) {
					mediaType = "image";
					hasImage = true;
					selectedImagePath = filePath;
					_loadSelectedImage();
				} else if (mimeType != null && mimeType.startsWith("video/")) {
					mediaType = "video";
					hasVideo = true;
					selectedVideoPath = filePath;
					_loadSelectedVideo();
				} else {
					Toast.makeText(getApplicationContext(), "Unsupported file type selected.", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}
	
	private void _openFilePicker() {
		if (Build.VERSION.SDK_INT >= 23) {
			if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == android.content.pm.PackageManager.PERMISSION_DENIED || checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == android.content.pm.PackageManager.PERMISSION_DENIED) {
				requestPermissions(new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
			} else {
				StorageUtil.pickFileWithMimeTypes(this, new String[]{"image/*", "video/*"}, REQ_CD_FILE_PICKER);
			}
		} else {
			StorageUtil.pickFileWithMimeTypes(this, new String[]{"image/*", "video/*"}, REQ_CD_FILE_PICKER);
		}
	}
	
	private void _loadSelectedImage() {
		if (hasImage && selectedImagePath != null) {
			imagePlaceholder.setVisibility(View.GONE);
			postImageView.setVisibility(View.VISIBLE);
			postVideoView.setVisibility(View.GONE);
			Glide.with(getApplicationContext()).load(selectedImagePath).into(postImageView);
		}
	}
	
	private void _loadSelectedVideo() {
		if (hasVideo && selectedVideoPath != null) {
			imagePlaceholder.setVisibility(View.GONE);
			postImageView.setVisibility(View.GONE);
			postVideoView.setVisibility(View.VISIBLE);
			postVideoView.setVideoURI(Uri.parse(selectedVideoPath));
			postVideoView.start();
		}
	}

	private void _resetMediaSelection() {
		hasImage = false;
		hasVideo = false;
		mediaType = "none";
		selectedImagePath = "";
		selectedVideoPath = "";

		postImageView.setVisibility(View.GONE);
		postVideoView.setVisibility(View.GONE);
		imagePlaceholder.setVisibility(View.VISIBLE);

		postImageView.setImageDrawable(null);
		postVideoView.stopPlayback();
		postVideoView.setVideoURI(null);
	}

	private void _createPost() {
		if (postDescription.getText().toString().trim().isEmpty() && !hasImage && !hasVideo) {
			Toast.makeText(getApplicationContext(), "Please add some text, an image, or a video to your post", Toast.LENGTH_SHORT).show();
			return;
		}
		
		_LoadingDialog(true);
		UniquePostKey = maindb.push().getKey();

		UploadFiles.UploadCallback uploadCallback = new UploadFiles.UploadCallback() {
			@Override
			public void onProgress(int percent) {
				// You can update a progress bar here if you want
			}

			@Override
			public void onSuccess(String url, String publicId) {
				_savePostToDatabase(url);
			}

			@Override
			public void onFailure(String error) {
				_LoadingDialog(false);
				Toast.makeText(getApplicationContext(), "Upload failed: " + error, Toast.LENGTH_SHORT).show();
			}
		};
		
		if (hasImage) {
			UploadFiles.uploadFile(selectedImagePath, new File(selectedImagePath).getName(), uploadCallback);
		} else if (hasVideo) {
			UploadFiles.uploadFile(selectedVideoPath, new File(selectedVideoPath).getName(), uploadCallback);
		} else {
			_savePostToDatabase(null); // Text-only post
		}
	}
	
	private void _savePostToDatabase(String mediaUrl) {
		cc = Calendar.getInstance();
		PostSendMap = new HashMap<>();
		PostSendMap.put("key", UniquePostKey);
		
		// Check if user is logged in
		FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
		if (currentUser == null) {
			Toast.makeText(getApplicationContext(), "User not logged in", Toast.LENGTH_SHORT).show();
			_LoadingDialog(false);
			return;
		}
		PostSendMap.put("uid", currentUser.getUid());
		
		DatabaseReference targetRef;

		if (hasVideo) {
			PostSendMap.put("post_type", "LINE_VIDEO");
			PostSendMap.put("post_video", mediaUrl);
			// From ReelsFragment.java, the path is "skyline/line-posts"
			targetRef = FirebaseDatabase.getInstance().getReference("skyline/line-posts").child(UniquePostKey);
		} else {
			if (hasImage) {
				PostSendMap.put("post_type", "IMAGE");
				PostSendMap.put("post_image", mediaUrl);
			} else {
				PostSendMap.put("post_type", "TEXT");
			}
			targetRef = FirebaseDatabase.getInstance().getReference("skyline/posts").child(UniquePostKey);
		}
		
		if (!postDescription.getText().toString().trim().equals("")) {
			PostSendMap.put("post_text", postDescription.getText().toString().trim());
		}
		
		if (appSavedData.contains("user_region_data") && !appSavedData.getString("user_region_data", "").equals("none")) {
			PostSendMap.put("post_region", appSavedData.getString("user_region_data", ""));
		} else {
			PostSendMap.put("post_region", "none");
		}
		
		// Apply post settings
		PostSendMap.put("post_hide_views_count", hideViewsCount);
		PostSendMap.put("post_hide_like_count", hideLikesCount);
		PostSendMap.put("post_hide_comments_count", hideCommentsCount);
		
		if (hidePostFromEveryone) {
			PostSendMap.put("post_visibility", "private");
		} else {
			PostSendMap.put("post_visibility", "public");
		}
		
		PostSendMap.put("post_disable_favorite", disableSaveToFavorites);
		PostSendMap.put("post_disable_comments", disableComments);
		PostSendMap.put("publish_date", String.valueOf((long)(cc.getTimeInMillis())));
		
		targetRef.updateChildren(PostSendMap, new DatabaseReference.CompletionListener() {
			@Override
			public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
				if (databaseError == null) {
					if (!hasVideo) {
						_sendNotificationsToFollowers(UniquePostKey, postDescription.getText().toString().trim());
					}
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.post_publish_success), Toast.LENGTH_SHORT).show();
					_LoadingDialog(false);
					finish();
				} else {
					Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
					_LoadingDialog(false);
				}
			}
		});
	}
	
	private void _showAddFromUrlDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Add Video from URL");

		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
		input.setHint("Enter video URL");
		builder.setView(input);

		builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String url = input.getText().toString().trim();
				if (!url.isEmpty() && Patterns.WEB_URL.matcher(url).matches()) {
					new DownloadFileTask().execute(url);
				} else {
					Toast.makeText(getApplicationContext(), "Invalid URL", Toast.LENGTH_SHORT).show();
				}
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		builder.show();
	}

	private class DownloadFileTask extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			imagePlaceholder.setVisibility(View.GONE);
			downloadProgressBar.setVisibility(View.VISIBLE);
			downloadProgressBar.setIndeterminate(true); // Start as indeterminate
		}

		@Override
		protected String doInBackground(String... sUrl) {
			InputStream input = null;
			OutputStream output = null;
			HttpURLConnection connection = null;
			try {
				URL url = new URL(sUrl[0]);
				connection = (HttpURLConnection) url.openConnection();
				connection.connect();

				if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
					return "Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage();
				}

				int fileLength = connection.getContentLength();

				File tempFile = File.createTempFile("video", ".mp4", getCacheDir());

				input = connection.getInputStream();
				output = new FileOutputStream(tempFile);

				byte data[] = new byte[4096];
				long total = 0;
				int count;
				while ((count = input.read(data)) != -1) {
					if (isCancelled()) {
						input.close();
						return null;
					}
					total += count;
					if (fileLength > 0) {
						publishProgress((int) (total * 100 / fileLength));
					}
					output.write(data, 0, count);
				}
				return tempFile.getAbsolutePath();
			} catch (Exception e) {
				return e.toString();
			} finally {
				try {
					if (output != null) output.close();
					if (input != null) input.close();
				} catch (IOException ignored) {
				}

				if (connection != null) connection.disconnect();
			}
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
			downloadProgressBar.setIndeterminate(false);
			downloadProgressBar.setProgress(progress[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			downloadProgressBar.setVisibility(View.GONE);
			if (result != null && new File(result).exists()) {
				_resetMediaSelection();
				mediaType = "video";
				hasVideo = true;
				selectedVideoPath = result;
				_loadSelectedVideo();
			} else {
				imagePlaceholder.setVisibility(View.VISIBLE);
				Toast.makeText(getApplicationContext(), "Download failed: " + result, Toast.LENGTH_LONG).show();
			}
		}
	}

	private void _sendNotificationsToFollowers(String postKey, final String postText) {
		FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
		if (currentUser == null) {
			return;
		}
		String currentUid = currentUser.getUid();
		com.synapse.social.studioasinc.util.UserUtils.getUserDisplayName(currentUid, new com.synapse.social.studioasinc.util.UserUtils.Callback<String>() {
			public void onResult(String senderName) {
				// Send notification to followers
				DatabaseReference followersRef = FirebaseDatabase.getInstance().getReference("skyline/users").child(currentUid).child("followers");
				followersRef.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
						for (DataSnapshot followerSnapshot : dataSnapshot.getChildren()) {
							String followerUid = followerSnapshot.getKey();
							if (followerUid != null) {
								String message = senderName + " has a new post";
								HashMap<String, String> data = new HashMap<>();
								data.put("postId", postKey);
								NotificationHelper.sendNotification(
									followerUid,
									currentUid,
									message,
									NotificationConfig.NOTIFICATION_TYPE_NEW_POST,
									data
								);
							}
						}
					}

					@Override
					public void onCancelled(@NonNull DatabaseError databaseError) {
						// Handle error
					}
				});

				// Send notification to mentioned users
				Pattern pattern = Pattern.compile("@(\\w+)");
				Matcher matcher = pattern.matcher(postText);
				while (matcher.find()) {
					String username = matcher.group(1);
					DatabaseReference usernameRef = FirebaseDatabase.getInstance().getReference("skyline/usernames").child(username);
					usernameRef.addListenerForSingleValueEvent(new ValueEventListener() {
						@Override
						public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
							if (dataSnapshot.exists()) {
								String recipientUid = dataSnapshot.child("uid").getValue(String.class);
								if (recipientUid != null && !recipientUid.equals(currentUid)) {
									String message = senderName + " mentioned you in a post";
									HashMap<String, String> data = new HashMap<>();
									data.put("postId", postKey);
									NotificationHelper.sendNotification(
										recipientUid,
										currentUid,
										message,
										"mention_post",
										data
									);
								}
							}
						}

						@Override
						public void onCancelled(@NonNull DatabaseError databaseError) {
						}
					});
				}
			}

			public void onCancelled(@NonNull DatabaseError databaseError) {
				// Handle error
			}
		});
	}

	private void _showPostSettingsBottomSheet() {
		BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
		View bottomSheetView = getLayoutInflater().inflate(R.layout.create_post_settings_bottom_sheet, null);
		bottomSheetDialog.setContentView(bottomSheetView);
		
		// Initialize switches
		SwitchCompat hideViewsSwitch = bottomSheetView.findViewById(R.id.hideViewsSwitch);
		SwitchCompat hideLikesSwitch = bottomSheetView.findViewById(R.id.hideLikesSwitch);
		SwitchCompat hideCommentsSwitch = bottomSheetView.findViewById(R.id.hideCommentsSwitch);
		SwitchCompat hidePostSwitch = bottomSheetView.findViewById(R.id.hidePostSwitch);
		SwitchCompat disableSaveSwitch = bottomSheetView.findViewById(R.id.disableSaveSwitch);
		SwitchCompat disableCommentsSwitch = bottomSheetView.findViewById(R.id.disableCommentsSwitch);
		
		// Set current values
		hideViewsSwitch.setChecked(hideViewsCount);
		hideLikesSwitch.setChecked(hideLikesCount);
		hideCommentsSwitch.setChecked(hideCommentsCount);
		hidePostSwitch.setChecked(hidePostFromEveryone);
		disableSaveSwitch.setChecked(disableSaveToFavorites);
		disableCommentsSwitch.setChecked(disableComments);
		
		// Set listeners
		hideViewsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				hideViewsCount = isChecked;
			}
		});
		
		hideLikesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				hideLikesCount = isChecked;
			}
		});
		
		hideCommentsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				hideCommentsCount = isChecked;
			}
		});
		
		hidePostSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				hidePostFromEveryone = isChecked;
				if (isChecked) {
					disableCommentsSwitch.setChecked(true);
					disableCommentsSwitch.setEnabled(false);
				} else {
					disableCommentsSwitch.setChecked(false);
					disableCommentsSwitch.setEnabled(true);
				}
			}
		});
		
		disableSaveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				disableSaveToFavorites = isChecked;
			}
		});
		
		disableCommentsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				disableComments = isChecked;
			}
		});
		
		bottomSheetDialog.show();
	}
	
	public void _LoadingDialog(final boolean _visibility) {
		if (_visibility) {
			if (SynapseLoadingDialog== null){
				SynapseLoadingDialog = new ProgressDialog(this);
				SynapseLoadingDialog.setCancelable(false);
				SynapseLoadingDialog.setCanceledOnTouchOutside(false);
				
				SynapseLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
				SynapseLoadingDialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(Color.TRANSPARENT));
				
			}
			SynapseLoadingDialog.show();
			SynapseLoadingDialog.setContentView(R.layout.loading_synapse);
			
			LinearLayout loading_bar_layout = (LinearLayout)SynapseLoadingDialog.findViewById(R.id.loading_bar_layout);
			
			
			//loading_bar_layout.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)100, 0xFFFFFFFF));
		} else {
			if (SynapseLoadingDialog != null){
				SynapseLoadingDialog.dismiss();
			}
		}
		
	}
	
	
	public void _TransitionManager(final View _view, final double _duration) {
		LinearLayout viewgroup =(LinearLayout) _view;
		
		android.transition.AutoTransition autoTransition = new android.transition.AutoTransition(); autoTransition.setDuration((long)_duration); android.transition.TransitionManager.beginDelayedTransition(viewgroup, autoTransition);
	}
	
}