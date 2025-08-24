package com.synapse.social.studioasinc;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.service.studioasinc.AI.Gemini;
import com.synapse.social.studioasinc.FadeEditText;
import com.synapse.social.studioasinc.FileUtil;
import com.synapse.social.studioasinc.SketchwareUtil;
import com.synapse.social.studioasinc.StorageUtil;
import com.synapse.social.studioasinc.UploadFiles;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public class ChatActivity extends AppCompatActivity {

	// Constants
	private static final String SKYLINE_REF = "skyline";
	private static final String USERS_REF = "users";
	private static final String CHATS_REF = "chats";
	private static final String INBOX_REF = "inbox";
	private static final String BLOCKLIST_REF = "blocklist";
	private static final String TYPING_MESSAGE_REF = "typing-message";
	private static final String USERNAME_REF = "username";

	private static final String UID_KEY = "uid";
	private static final String ORIGIN_KEY = "origin";
	private static final String KEY_KEY = "key";
	private static final String MESSAGE_TEXT_KEY = "message_text";
	private static final String TYPE_KEY = "TYPE";
	private static final String MESSAGE_STATE_KEY = "message_state";
	private static final String PUSH_DATE_KEY = "push_date";
	private static final String REPLIED_MESSAGE_ID_KEY = "replied_message_id";
	private static final String ATTACHMENTS_KEY = "attachments";
	private static final String LAST_MESSAGE_UID_KEY = "last_message_uid";
	private static final String LAST_MESSAGE_TEXT_KEY = "last_message_text";
	private static final String LAST_MESSAGE_STATE_KEY = "last_message_state";

	private static final String MESSAGE_TYPE = "MESSAGE";
	private static final String ATTACHMENT_MESSAGE_TYPE = "ATTACHMENT_MESSAGE";

	private static final String GEMINI_MODEL = "gemini-2.5-flash-lite";
	private static final String GEMINI_TONE = "normal";

	private Timer _timer = new Timer();
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	private FirebaseStorage _firebase_storage = FirebaseStorage.getInstance();

	private ProgressDialog SynapseLoadingDialog;
	private MediaRecorder AudioMessageRecorder;
	private HashMap<String, Object> ChatSendMap = new HashMap<>();
	private HashMap<String, Object> ChatInboxSend = new HashMap<>();
	private double recordMs = 0;
	private HashMap<String, Object> ChatInboxSend2 = new HashMap<>();
	private String SecondUserAvatar = "";
	private HashMap<String, Object> typingSnd = new HashMap<>();
	private String ReplyMessageID = "";
	private String SecondUserName = "";
	private String FirstUserName = "";
	private String oldestMessageKey = null;
	private static final int CHAT_PAGE_SIZE = 80;
	private String object_clicked = "";
	private String handle = "";
	private HashMap<String, Object> block = new HashMap<>();
	private double block_switch = 0;
	private String path = "";
	private String AndroidDevelopersBlogURL = "";
	public final int REQ_CD_IMAGE_PICKER = 101;
	private ChatAdapter chatAdapter;
	private boolean isLoading = false;
	private ChildEventListener _chat_child_listener;
	private DatabaseReference chatMessagesRef;
	private ValueEventListener _userStatusListener;
	private DatabaseReference userRef;

	private ArrayList<HashMap<String, Object>> ChatMessagesList = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> attactmentmap = new ArrayList<>();

	private LinearLayout parent;
	private RelativeLayout relativelayout1;
	private ImageView ivBGimage;
	private LinearLayout body;
	private LinearLayout appBar;
	private LinearLayout middle;
	private RelativeLayout attachmentLayoutListHolder;
	private LinearLayout mMessageReplyLayout;
	private LinearLayout message_input_overall_container;
	private TextView blocked_txt;
	private ImageView back;
	private LinearLayout topProfileLayout;
	private LinearLayout topProfileLayoutSpace;
	private ImageView ic_video_call;
	private ImageView ic_audio_call;
	private ImageView ic_more;
	private CardView topProfileCard;
	private LinearLayout topProfileLayoutRight;
	private ImageView topProfileLayoutProfileImage;
	private LinearLayout topProfileLayoutRightTop;
	private TextView topProfileLayoutStatus;
	private TextView topProfileLayoutUsername;
	private ImageView topProfileLayoutGenderBadge;
	private ImageView topProfileLayoutVerifiedBadge;
	private TextView noChatText;
	private RecyclerView ChatMessagesListRecycler;
	private CardView card_attactmentListRVHolder;
	private RecyclerView rv_attacmentList;
	private LinearLayout mMessageReplyLayoutBody;
	private LinearLayout mMessageReplyLayoutSpace;
	private ImageView mMessageReplyLayoutBodyIc;
	private LinearLayout mMessageReplyLayoutBodyRight;
	private ImageView mMessageReplyLayoutBodyCancel;
	private TextView mMessageReplyLayoutBodyRightUsername;
	private TextView mMessageReplyLayoutBodyRightMessage;
	private LinearLayout message_input_outlined_round;
	private MaterialButton btn_sendMessage;
	private FadeEditText message_et;
	private LinearLayout toolContainer;
	private ImageView expand_send_type_btn;
	private ImageView close_attachments_btn;
	private LinearLayout devider_mic_camera;
	private ImageView galleryBtn;

	private Intent intent = new Intent();
	private DatabaseReference main = _firebase.getReference(SKYLINE_REF);
	private FirebaseAuth auth;
	private TimerTask loadTimer;
	private Calendar cc = Calendar.getInstance();
	private Vibrator vbr;
	private TimerTask timer;
	private DatabaseReference blocklist = _firebase.getReference(SKYLINE_REF).child(BLOCKLIST_REF);
	private ChildEventListener _blocklist_child_listener;
	private SharedPreferences blocked;
	private SharedPreferences theme;
	private Intent i = new Intent();
	private SharedPreferences appSettings;
	private Gemini gemini;

	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.chat);
		initialize(_savedInstanceState);
		FirebaseApp.initializeApp(this);

		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
		|| ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);} else {
			initializeLogic();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1000) {
			initializeLogic();
		}
	}

	private void initialize(Bundle _savedInstanceState) {
		parent = findViewById(R.id.parent);
		relativelayout1 = findViewById(R.id.relativelayout1);
		ivBGimage = findViewById(R.id.ivBGimage);
		body = findViewById(R.id.body);
		appBar = findViewById(R.id.appBar);
		middle = findViewById(R.id.middle);
		attachmentLayoutListHolder = findViewById(R.id.attachmentLayoutListHolder);
		mMessageReplyLayout = findViewById(R.id.mMessageReplyLayout);
		message_input_overall_container = findViewById(R.id.message_input_overall_container);
		blocked_txt = findViewById(R.id.blocked_txt);
		back = findViewById(R.id.back);
		topProfileLayout = findViewById(R.id.topProfileLayout);
		topProfileLayoutSpace = findViewById(R.id.topProfileLayoutSpace);
		ic_video_call = findViewById(R.id.ic_video_call);
		ic_audio_call = findViewById(R.id.ic_audio_call);
		ic_more = findViewById(R.id.ic_more);
		topProfileCard = findViewById(R.id.topProfileCard);
		topProfileLayoutRight = findViewById(R.id.topProfileLayoutRight);
		topProfileLayoutProfileImage = findViewById(R.id.topProfileLayoutProfileImage);
		topProfileLayoutRightTop = findViewById(R.id.topProfileLayoutRightTop);
		topProfileLayoutStatus = findViewById(R.id.topProfileLayoutStatus);
		topProfileLayoutUsername = findViewById(R.id.topProfileLayoutUsername);
		topProfileLayoutGenderBadge = findViewById(R.id.topProfileLayoutGenderBadge);
		topProfileLayoutVerifiedBadge = findViewById(R.id.topProfileLayoutVerifiedBadge);
		noChatText = findViewById(R.id.noChatText);
		ChatMessagesListRecycler = findViewById(R.id.ChatMessagesListRecycler);
		card_attactmentListRVHolder = findViewById(R.id.card_attactmentListRVHolder);
		rv_attacmentList = findViewById(R.id.rv_attacmentList);
		mMessageReplyLayoutBody = findViewById(R.id.mMessageReplyLayoutBody);
		mMessageReplyLayoutSpace = findViewById(R.id.mMessageReplyLayoutSpace);
		mMessageReplyLayoutBodyIc = findViewById(R.id.mMessageReplyLayoutBodyIc);
		mMessageReplyLayoutBodyRight = findViewById(R.id.mMessageReplyLayoutBodyRight);
		mMessageReplyLayoutBodyCancel = findViewById(R.id.mMessageReplyLayoutBodyCancel);
		mMessageReplyLayoutBodyRightUsername = findViewById(R.id.mMessageReplyLayoutBodyRightUsername);
		mMessageReplyLayoutBodyRightMessage = findViewById(R.id.mMessageReplyLayoutBodyRightMessage);
		message_input_outlined_round = findViewById(R.id.message_input_outlined_round);
		btn_sendMessage = findViewById(R.id.btn_sendMessage);
		message_et = findViewById(R.id.message_et);
		toolContainer = findViewById(R.id.toolContainer);
		expand_send_type_btn = findViewById(R.id.expand_send_type_btn);
		devider_mic_camera = findViewById(R.id.devider_mic_camera);
		galleryBtn = findViewById(R.id.galleryBtn);
		close_attachments_btn = findViewById(R.id.close_attachments_btn);
		auth = FirebaseAuth.getInstance();
		vbr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		blocked = getSharedPreferences("block", Activity.MODE_PRIVATE);
		theme = getSharedPreferences("theme", Activity.MODE_PRIVATE);
		appSettings = getSharedPreferences("appSettings", Activity.MODE_PRIVATE);

		close_attachments_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				attachmentLayoutListHolder.setVisibility(View.GONE);
				attactmentmap.clear();
				rv_attacmentList.getAdapter().notifyDataSetChanged();

				// Clear the attachment draft from SharedPreferences
				SharedPreferences drafts = getSharedPreferences("chat_drafts", Context.MODE_PRIVATE);
				String chatId = getChatId(FirebaseAuth.getInstance().getCurrentUser().getUid(), getIntent().getStringExtra("uid"));
				drafts.edit().remove(chatId + "_attachments").apply();
			}
		});

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				onBackPressed();
			}
		});

		View.OnClickListener profileClickListener = v -> startActivityWithUid(Chat2ndUserMoreSettingsActivity.class);
		topProfileLayout.setOnClickListener(profileClickListener);
		ic_more.setOnClickListener(profileClickListener);

		View.OnClickListener callClickListener = v -> startActivityWithUid(CallActivity.class);
		ic_video_call.setOnClickListener(callClickListener);
		ic_audio_call.setOnClickListener(callClickListener);

		mMessageReplyLayoutBodyCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				ReplyMessageID = "null";
				mMessageReplyLayout.setVisibility(View.GONE);
				vbr.vibrate((long)(48));
			}
		});

		message_input_outlined_round.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				message_et.requestFocus();
			}
		});

		btn_sendMessage.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View _view) {
				if (!message_et.getText().toString().isEmpty()) {
					String prompt = "Fix grammar, punctuation, and clarity without changing meaning. " +
					"Preserve original formatting (line breaks, lists, markdown). " +
					"Censor profanity by replacing letters with asterisks. " +
					"Return ONLY the corrected RAW text.\n```"
					.concat(message_et.getText().toString())
					.concat("```");
					callGemini(prompt, true);
				} else {
					if (ReplyMessageID != null && !ReplyMessageID.equals("null")) {
						int repliedMessageIndex = -1;
						for (int i = 0; i < ChatMessagesList.size(); i++) {
							if (ChatMessagesList.get(i).get(KEY_KEY).toString().equals(ReplyMessageID)) {
								repliedMessageIndex = i;
								break;
							}
						}

						if (repliedMessageIndex != -1) {
							StringBuilder contextBuilder = new StringBuilder();
							contextBuilder.append("You are helping 'Me' to write a reply in a conversation with '").append(SecondUserName).append("'.\n");
							contextBuilder.append("Here is the recent chat history:\n---\n");

							int startIndex = Math.max(0, repliedMessageIndex - 10);
							int endIndex = Math.min(ChatMessagesList.size() - 1, repliedMessageIndex + 10);

							for (int i = startIndex; i <= endIndex; i++) {
								HashMap<String, Object> message = ChatMessagesList.get(i);
								String sender = message.get(UID_KEY).toString().equals(auth.getCurrentUser().getUid()) ? "Me" : SecondUserName;
								contextBuilder.append(sender).append(": ").append(message.get(MESSAGE_TEXT_KEY).toString()).append("\n");
							}

							contextBuilder.append("---\n");

							String repliedMessageSender = mMessageReplyLayoutBodyRightUsername.getText().toString();
							String repliedMessageText = mMessageReplyLayoutBodyRightMessage.getText().toString();

							contextBuilder.append("I need to reply to this message from '").append(repliedMessageSender).append("': \"").append(repliedMessageText).append("\"\n");
							contextBuilder.append("Based on the conversation history, please suggest a short, relevant reply from 'Me'.");

							String prompt = contextBuilder.toString();
							callGemini(prompt, false);
						}
					} else {
						// Fallback for non-reply long-press
						String prompt = "Suggest a generic, friendly greeting.";
						callGemini(prompt, false);
					}
				}
				return true;
			}
		});

		btn_sendMessage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_send_btn();
			}
		});

		message_et.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				DatabaseReference typingRef = _firebase.getReference(SKYLINE_REF).child(CHATS_REF).child(getIntent().getStringExtra(UID_KEY)).child(auth.getCurrentUser().getUid()).child(TYPING_MESSAGE_REF);
				if (_charSeq.length() == 0) {
					typingRef.removeValue();
					_setMargin(message_et, 0, 7, 0, 0);
					message_input_outlined_round.setOrientation(LinearLayout.HORIZONTAL);

					_TransitionManager(message_input_overall_container, 125);
					message_input_outlined_round.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)100, (int)2, 0xFFC7C7C7, 0xFFFFFFFF));
				} else {
					typingSnd = new HashMap<>();
					typingSnd.put(UID_KEY, auth.getCurrentUser().getUid());
					typingSnd.put("typingMessageStatus", "true");
					typingRef.updateChildren(typingSnd);
					_TransitionManager(message_input_overall_container, 125);
					message_input_outlined_round.setOrientation(LinearLayout.VERTICAL);

					message_input_outlined_round.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)60, (int)2, 0xFFC7C7C7, 0xFFFFFFFF));
				}
			}

			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {

			}

			@Override
			public void afterTextChanged(Editable _param1) {

			}
		});

		galleryBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				StorageUtil.pickMultipleFiles(ChatActivity.this, "*/*", REQ_CD_IMAGE_PICKER);
			}
		});

		_blocklist_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				handleBlocklistUpdate(_param1);
			}

			@Override
			public void onChildChanged(DataSnapshot _param1, String _param2) {
				handleBlocklistUpdate(_param1);
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
	}

	private void initializeLogic() {
		// Load and apply chat background
		SharedPreferences themePrefs = getSharedPreferences("theme", MODE_PRIVATE);
		String backgroundUrl = themePrefs.getString("chat_background_url", null);
		if (backgroundUrl != null && !backgroundUrl.isEmpty()) {
			Glide.with(this).load(backgroundUrl).into(ivBGimage);
		}

		SecondUserAvatar = "null";
		ReplyMessageID = "null";
		path = "";
		block_switch = 0;
		// Set the Layout Manager
		LinearLayoutManager ChatRecyclerLayoutManager = new LinearLayoutManager(this);
		ChatRecyclerLayoutManager.setReverseLayout(false);
		ChatRecyclerLayoutManager.setStackFromEnd(true);
		ChatMessagesListRecycler.setLayoutManager(ChatRecyclerLayoutManager);

		// Create, configure, and set the new ChatAdapter
		chatAdapter = new ChatAdapter(ChatMessagesList);
		chatAdapter.setChatActivity(this);
		ChatMessagesListRecycler.setAdapter(chatAdapter);
		chatMessagesRef = _firebase.getReference(SKYLINE_REF).child(CHATS_REF).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getIntent().getStringExtra(UID_KEY));
		userRef = _firebase.getReference(SKYLINE_REF).child(USERS_REF).child(getIntent().getStringExtra(UID_KEY));
		// Initialize with custom settings
		gemini = new Gemini.Builder(this)
		.model("gemini-1.5-flash")
		.responseType("text")
		.tone("friendly")
		.size("medium")
		.maxTokens(2000)
		.temperature(0.8)
		.showThinking(true)
		.thinkingText("Analyzing your request...")
		.systemInstruction("Your name is ChatBot, help users with their questions")
		.responseTextView(message_et)
		.build();
		_setupSwipeToReply();
		// --- START: Critical Initialization for Attachment RecyclerView ---

		// 1. Create the adapter for the attachment list, passing it our empty list.
		Rv_attacmentListAdapter attachmentAdapter = new Rv_attacmentListAdapter(attactmentmap);
		rv_attacmentList.setAdapter(attachmentAdapter);

		// 2. A RecyclerView must have a LayoutManager to function.
		//    We set it to a horizontal layout.
		rv_attacmentList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

		// --- END: Critical Initialization ---
		_getUserReference();
		toolContainer.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)360, (int)0, Color.TRANSPARENT, 0xFFF0F3F8));
		message_input_outlined_round.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)95, (int)3, 0xFFC7C7C7, 0xFFFFFFFF));
		message_input_outlined_round.setOrientation(LinearLayout.HORIZONTAL);
		_ImgRound(topProfileLayoutProfileImage, 100);
		if (message_et.getText().toString().trim().equals("")) {
			_TransitionManager(message_input_overall_container, 250);
			message_input_outlined_round.setOrientation(LinearLayout.HORIZONTAL);

		} else {
			_TransitionManager(message_input_overall_container, 250);
			message_input_outlined_round.setOrientation(LinearLayout.VERTICAL);

		}
		ChatMessagesListRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
				if (dy < 0) { //check for scroll up
					if (layoutManager != null && layoutManager.findFirstVisibleItemPosition() == 0) {
						if (!isLoading) {
							_getOldChatMessagesRef();
						}
					}
				}
			}
		});

		// Attach listeners after all references are safely initialized.
		_attachChatListener();
		_attachUserStatusListener();
	}

	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		if (_requestCode == REQ_CD_IMAGE_PICKER && _resultCode == Activity.RESULT_OK) {
			if (_data != null) {
				ArrayList<String> resolvedFilePaths = new ArrayList<>();
				
				try {
					if (_data.getClipData() != null) {
						for (int i = 0; i < _data.getClipData().getItemCount(); i++) {
							Uri fileUri = _data.getClipData().getItemAt(i).getUri();
							String path = StorageUtil.getPathFromUri(getApplicationContext(), fileUri);
							if (path != null && !path.isEmpty()) {
								resolvedFilePaths.add(path);
							} else {
								Log.w("ChatActivity", "Failed to resolve file path for clip data item " + i);
							}
						}
					} else if (_data.getData() != null) {
						Uri fileUri = _data.getData();
						String path = StorageUtil.getPathFromUri(getApplicationContext(), fileUri);
						if (path != null && !path.isEmpty()) {
							resolvedFilePaths.add(path);
						} else {
							Log.w("ChatActivity", "Failed to resolve file path for single data");
						}
					}
				} catch (Exception e) {
					Log.e("ChatActivity", "Error processing file picker result: " + e.getMessage());
					Toast.makeText(this, "Error processing selected files", Toast.LENGTH_SHORT).show();
					return;
				}

				if (!resolvedFilePaths.isEmpty()) {
					attachmentLayoutListHolder.setVisibility(View.VISIBLE);

					int startingPosition = attactmentmap.size();

					for (String filePath : resolvedFilePaths) {
						try {
							HashMap<String, Object> itemMap = new HashMap<>();
							itemMap.put("localPath", filePath);
							itemMap.put("uploadState", "pending");

							// Get image dimensions safely
							BitmapFactory.Options options = new BitmapFactory.Options();
							options.inJustDecodeBounds = true;
							try {
								BitmapFactory.decodeFile(filePath, options);
								itemMap.put("width", options.outWidth > 0 ? options.outWidth : 100);
								itemMap.put("height", options.outHeight > 0 ? options.outHeight : 100);
							} catch (Exception e) {
								Log.w("ChatActivity", "Could not decode image dimensions for: " + filePath);
								itemMap.put("width", 100);
								itemMap.put("height", 100);
							}

							attactmentmap.add(itemMap);
						} catch (Exception e) {
							Log.e("ChatActivity", "Error processing file: " + filePath + ", Error: " + e.getMessage());
						}
					}

					// Notify adapter of changes
					if (rv_attacmentList.getAdapter() != null) {
						rv_attacmentList.getAdapter().notifyItemRangeInserted(startingPosition, resolvedFilePaths.size());
					}

					// Start upload for each item
					for (int i = 0; i < resolvedFilePaths.size(); i++) {
						try {
							_startUploadForItem(startingPosition + i);
						} catch (Exception e) {
							Log.e("ChatActivity", "Error starting upload for item " + i + ": " + e.getMessage());
						}
					}
				} else {
					Log.w("ChatActivity", "No valid file paths resolved from file picker");
					Toast.makeText(this, "No valid files selected", Toast.LENGTH_SHORT).show();
				}
			}
		}
		switch (_requestCode) {
			default:
				break;
		}
	}


	@Override
	public void onPause() {
		super.onPause();
		_firebase.getReference(SKYLINE_REF).child(CHATS_REF).child(getIntent().getStringExtra(UID_KEY)).child(auth.getCurrentUser().getUid()).child(TYPING_MESSAGE_REF).removeValue();

		// Set user status back to online when leaving the chat screen
		if (auth.getCurrentUser() != null) {
			PresenceManager.stopChatting(auth.getCurrentUser().getUid());
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		blocklist.addChildEventListener(_blocklist_child_listener);

		// Set user status to indicate they are in this chat
		if (auth.getCurrentUser() != null) {
			String recipientUid = getIntent().getStringExtra("uid");
			PresenceManager.setChattingWith(auth.getCurrentUser().getUid(), recipientUid);
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		_detachChatListener();
		_detachUserStatusListener();
		blocklist.removeEventListener(_blocklist_child_listener);
		_firebase.getReference(SKYLINE_REF).child(CHATS_REF).child(getIntent().getStringExtra(UID_KEY)).child(auth.getCurrentUser().getUid()).child(TYPING_MESSAGE_REF).removeValue();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		
		// Clean up typing indicator
		if (auth.getCurrentUser() != null) {
			try {
				_firebase.getReference(SKYLINE_REF).child(CHATS_REF).child(getIntent().getStringExtra(UID_KEY)).child(auth.getCurrentUser().getUid()).child(TYPING_MESSAGE_REF).removeValue();
			} catch (Exception e) {
				Log.e("ChatActivity", "Error cleaning up typing indicator: " + e.getMessage());
			}
		}
		
		// Clean up Firebase listeners
		_detachChatListener();
		_detachUserStatusListener();
		
		// Clean up blocklist listener
		if (_blocklist_child_listener != null) {
			try {
				blocklist.removeEventListener(_blocklist_child_listener);
			} catch (Exception e) {
				Log.e("ChatActivity", "Error removing blocklist listener: " + e.getMessage());
			}
		}
		
		// Clean up timers
		if (_timer != null) {
			_timer.cancel();
			_timer = null;
		}
		
		// Clean up media recorder
		if (AudioMessageRecorder != null) {
			try {
				AudioMessageRecorder.release();
				AudioMessageRecorder = null;
			} catch (Exception e) {
				Log.e("ChatActivity", "Error releasing media recorder: " + e.getMessage());
			}
		}
		
		// Clear lists to prevent memory leaks
		if (ChatMessagesList != null) {
			ChatMessagesList.clear();
		}
		if (attactmentmap != null) {
			attactmentmap.clear();
		}
		
		// Clean up progress dialog
		if (SynapseLoadingDialog != null && SynapseLoadingDialog.isShowing()) {
			try {
				SynapseLoadingDialog.dismiss();
			} catch (Exception e) {
				Log.e("ChatActivity", "Error dismissing progress dialog: " + e.getMessage());
			}
			SynapseLoadingDialog = null;
		}
		
		// Clean up adapters
		if (chatAdapter != null) {
			chatAdapter = null;
		}
		
		// Clean up RecyclerViews
		if (ChatMessagesListRecycler != null) {
			ChatMessagesListRecycler.setAdapter(null);
		}
		if (rv_attacmentList != null) {
			rv_attacmentList.setAdapter(null);
		}
	}

	@Override
	public void onBackPressed() {
		if (getIntent().hasExtra(ORIGIN_KEY)) {
			String originSimpleName = getIntent().getStringExtra(ORIGIN_KEY);
			if (originSimpleName != null && !originSimpleName.trim().isEmpty()) {
				try {
					String packageName = "com.synapse.social.studioasinc";
					String fullClassName = packageName + "." + originSimpleName.trim();
					Class<?> clazz = Class.forName(fullClassName);

					Intent intent = new Intent(this, clazz);
					if ("ProfileActivity".equals(originSimpleName.trim())) {
						if (getIntent().hasExtra(UID_KEY)) {
							intent.putExtra(UID_KEY, getIntent().getStringExtra(UID_KEY));
						} else {
							Toast.makeText(this, "Error: UID is required for ProfileActivity", Toast.LENGTH_SHORT).show();
							finish();
							return;
						}
					}
					startActivity(intent);
					finish();
					return;
				} catch (ClassNotFoundException e) {
					Log.e("ChatActivity", "Activity class not found: " + originSimpleName, e);
					Toast.makeText(this, "Error: Activity not found", Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Log.e("ChatActivity", "Failed to start activity: " + originSimpleName, e);
					Toast.makeText(this, "Error: Failed to start activity", Toast.LENGTH_SHORT).show();
				}
			}
		}
		finish();
	}
	public void _stateColor(final int _statusColor, final int _navigationColor) {
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
		getWindow().setStatusBarColor(_statusColor);
		getWindow().setNavigationBarColor(_navigationColor);
	}


	public void _viewGraphics(final View _view, final int _onFocus, final int _onRipple, final double _radius, final double _stroke, final int _strokeColor) {
		android.graphics.drawable.GradientDrawable GG = new android.graphics.drawable.GradientDrawable();
		GG.setColor(_onFocus);
		GG.setCornerRadius((float)_radius);
		GG.setStroke((int) _stroke, _strokeColor);
		android.graphics.drawable.RippleDrawable RE = new android.graphics.drawable.RippleDrawable(new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{ _onRipple}), GG, null);
		_view.setBackground(RE);
	}


	public void _ImageColor(final ImageView _image, final int _color) {
		_image.setColorFilter(_color,PorterDuff.Mode.SRC_ATOP);
	}


	public void _messageOverviewPopup(final View _view, final double _position, final ArrayList<HashMap<String, Object>> _data) {
		if (_data == null || (int)_position >= _data.size() || (int)_position < 0) {
			Log.e("ChatActivity", "Invalid position or data for message overview popup. Position: " + _position + ", Size: " + (_data != null ? _data.size() : "null"));
			return;
		}
		View pop1V = getLayoutInflater().inflate(R.layout.chat_msg_options_popup_cv_synapse, null);

		final PopupWindow pop1 = new PopupWindow(pop1V, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
		pop1.setFocusable(true);
		pop1.setInputMethodMode(ListPopupWindow.INPUT_METHOD_NOT_NEEDED);
		final LinearLayout main = pop1V.findViewById(R.id.main);
		final LinearLayout edit = pop1V.findViewById(R.id.edit);
		final LinearLayout reply = pop1V.findViewById(R.id.reply);
		final LinearLayout summary = pop1V.findViewById(R.id.summary);
		final LinearLayout copy = pop1V.findViewById(R.id.copy);
		final LinearLayout delete = pop1V.findViewById(R.id.delete);
		pop1.setAnimationStyle(android.R.style.Animation_Dialog);
		
		// Measure the popup to get its dimensions
		int popupWidth = ViewGroup.LayoutParams.MATCH_PARENT;
		int popupHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
		
		// Use a post callback to ensure the popup is measured before positioning
		pop1V.post(() -> {
			try {
				int[] location = new int[2];
				View anchorView = _view;
				anchorView.getLocationOnScreen(location);

				int screenHeight = getApplicationContext().getResources().getDisplayMetrics().heightPixels;
				int halfScreenHeight = screenHeight / 2;
				int anchorViewHeight = anchorView.getHeight();
				int popupHeight = pop1.getContentView().getHeight();

				// Ensure we have valid dimensions
				if (popupHeight <= 0) {
					popupHeight = 200; // Fallback height in dp
					popupHeight = (int) (popupHeight * getResources().getDisplayMetrics().density);
				}

				int x = location[0];
				int y;

				if (location[1] < halfScreenHeight) {
					// Show below the anchor view
					y = location[1] + anchorViewHeight;
				} else {
					// Show above the anchor view
					y = location[1] - popupHeight;
				}

				// Ensure popup doesn't go off-screen
				if (y < 0) {
					y = 0;
				} else if (y + popupHeight > screenHeight) {
					y = screenHeight - popupHeight;
				}

				pop1.showAtLocation(anchorView, Gravity.NO_GRAVITY, x, y);
			} catch (Exception e) {
				Log.e("ChatActivity", "Error positioning popup: " + e.getMessage());
				// Fallback positioning
				pop1.showAtLocation(_view, Gravity.CENTER, 0, 0);
			}
		});
		if (_data.get((int)_position).get(UID_KEY).toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
			main.setGravity(Gravity.CENTER | Gravity.RIGHT);
			edit.setVisibility(View.VISIBLE);
			delete.setVisibility(View.VISIBLE);
		} else {
			main.setGravity(Gravity.CENTER | Gravity.LEFT);
			edit.setVisibility(View.GONE);
			delete.setVisibility(View.GONE);
		}
		_viewGraphics(edit, 0xFFFFFFFF, 0xFFEEEEEE, 0, 0, 0xFFFFFFFF);
		_viewGraphics(reply, 0xFFFFFFFF, 0xFFEEEEEE, 0, 0, 0xFFFFFFFF);
		_viewGraphics(summary, 0xFFFFFFFF, 0xFFEEEEEE, 0, 0, 0xFFFFFFFF);

		String messageTextForSummary = _data.get((int)_position).get(MESSAGE_TEXT_KEY).toString();
		if (messageTextForSummary.length() > 200) {
			summary.setVisibility(View.VISIBLE);
		} else {
			summary.setVisibility(View.GONE);
		}
		_viewGraphics(copy, 0xFFFFFFFF, 0xFFEEEEEE, 0, 0, 0xFFFFFFFF);
		_viewGraphics(delete, 0xFFFFFFFF, 0xFFEEEEEE, 0, 0, 0xFFFFFFFF);
		main.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				pop1.dismiss();
			}
		});
		reply.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				ReplyMessageID = _data.get((int)_position).get(KEY_KEY).toString();
				if (_data.get((int)_position).get(UID_KEY).toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
					mMessageReplyLayoutBodyRightUsername.setText(FirstUserName);
				} else {
					mMessageReplyLayoutBodyRightUsername.setText(SecondUserName);
				}
				mMessageReplyLayoutBodyRightMessage.setText(_data.get((int)_position).get(MESSAGE_TEXT_KEY).toString());
				mMessageReplyLayout.setVisibility(View.VISIBLE);
				vbr.vibrate((long)(48));
				pop1.dismiss();
			}
		});
		summary.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				String messageText = _data.get((int)_position).get(MESSAGE_TEXT_KEY).toString();
				String prompt = "Summarize the following text in a few sentences:\n\n" + messageText;

				RecyclerView.ViewHolder viewHolder = ChatMessagesListRecycler.findViewHolderForAdapterPosition((int)_position);
				if (viewHolder instanceof BaseMessageViewHolder) {
					callGeminiForSummary(prompt, (BaseMessageViewHolder) viewHolder);
				}

				pop1.dismiss();
			}
		});
		copy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				((ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("clipboard", _data.get((int)_position).get(MESSAGE_TEXT_KEY).toString()));
				vbr.vibrate((long)(48));
				pop1.dismiss();
			}
		});
		delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_DeleteMessageDialog(_data, _position);
				pop1.dismiss();
			}
		});
		edit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				pop1.dismiss();
				MaterialAlertDialogBuilder Dialogs = new MaterialAlertDialogBuilder(ChatActivity.this);
				Dialogs.setTitle("Edit message");
				View EdittextDesign = LayoutInflater.from(ChatActivity.this).inflate(R.layout.single_et, null);
				Dialogs.setView(EdittextDesign);

				final EditText edittext1 = EdittextDesign.findViewById(R.id.edittext1);
				final TextInputLayout textinputlayout1 = EdittextDesign.findViewById(R.id.textinputlayout1);
				edittext1.setFocusableInTouchMode(true);
				edittext1.setText(_data.get((int)_position).get(MESSAGE_TEXT_KEY).toString());
				edittext1.setMaxLines(10);
				edittext1.setVerticalScrollBarEnabled(true);
				edittext1.setMovementMethod(new ScrollingMovementMethod());
				edittext1.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);

				Dialogs.setPositiveButton("Change", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						if (!(edittext1.getText().toString().length() == 0)) {
							ChatSendMap = new HashMap<>();
							ChatSendMap.put(UID_KEY, FirebaseAuth.getInstance().getCurrentUser().getUid());
							ChatSendMap.put(TYPE_KEY, MESSAGE_TYPE);
							ChatSendMap.put("message_uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
							ChatSendMap.put(MESSAGE_TEXT_KEY, edittext1.getText().toString().trim());
							ChatSendMap.put(MESSAGE_STATE_KEY, "sended");
							ChatSendMap.put(PUSH_DATE_KEY, String.valueOf((long)(cc.getTimeInMillis())));
							
							// Update both chat nodes using setValue for proper real-time updates
							_firebase.getReference(SKYLINE_REF).child(CHATS_REF)
							.child(FirebaseAuth.getInstance().getCurrentUser().getUid())  // Current user UID
							.child(getIntent().getStringExtra(UID_KEY))  // Other user UID
							.child(_data.get((int)_position).get(KEY_KEY).toString())  // Message key
							.setValue(ChatSendMap);
							
							_firebase.getReference(SKYLINE_REF).child(CHATS_REF)
							.child(getIntent().getStringExtra(UID_KEY))  // Other user UID
							.child(FirebaseAuth.getInstance().getCurrentUser().getUid())  // Current user UID
							.child(_data.get((int)_position).get(KEY_KEY).toString())  // Message key
							.setValue(ChatSendMap);
							
							// Clear the edit dialog
							edittext1.setText("");
						} else {
							SketchwareUtil.showMessage(getApplicationContext(), "Can't be empty");
						}
					}
				});
				Dialogs.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {

					}
				});
				androidx.appcompat.app.AlertDialog edittextDialog = Dialogs.create();

				edittextDialog.setCancelable(true);
				edittextDialog.show();
			}
		});
	}


	public void _setMargin(final View _view, final double _r, final double _l, final double _t, final double _b) {
		float dpRatio = new c(this).getContext().getResources().getDisplayMetrics().density;
		int right = (int)(_r * dpRatio);
		int left = (int)(_l * dpRatio);
		int top = (int)(_t * dpRatio);
		int bottom = (int)(_b * dpRatio);

		boolean _default = false;

		ViewGroup.LayoutParams p = _view.getLayoutParams();
		if (p instanceof LinearLayout.LayoutParams) {
			LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)p;
			lp.setMargins(left, top, right, bottom);
			_view.setLayoutParams(lp);
		}
		else if (p instanceof RelativeLayout.LayoutParams) {
			RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)p;
			lp.setMargins(left, top, right, bottom);
			_view.setLayoutParams(lp);
		}
		else if (p instanceof TableRow.LayoutParams) {
			TableRow.LayoutParams lp = (TableRow.LayoutParams)p;
			lp.setMargins(left, top, right, bottom);
			_view.setLayoutParams(lp);
		}


	}

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


	{

	}


	public void _getUserReference() {
		// The user profile data is now fetched via a persistent listener attached in onStart,
		// so the addListenerForSingleValueEvent call is no longer needed here.

		DatabaseReference getFirstUserName = _firebase.getReference(SKYLINE_REF).child(USERS_REF).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
		getFirstUserName.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				try {
					if (dataSnapshot.exists()) {
						String nickname = dataSnapshot.child("nickname").getValue(String.class);
						String username = dataSnapshot.child("username").getValue(String.class);
						
						if (nickname != null && !"null".equals(nickname)) {
							FirstUserName = nickname;
						} else if (username != null && !"null".equals(username)) {
							FirstUserName = "@" + username;
						} else {
							FirstUserName = "Unknown User";
							Log.w("ChatActivity", "Both nickname and username are null or 'null'");
						}
					} else {
						Log.w("ChatActivity", "User data snapshot doesn't exist");
						FirstUserName = "Unknown User";
					}
				} catch (Exception e) {
					Log.e("ChatActivity", "Error processing user data: " + e.getMessage());
					FirstUserName = "Unknown User";
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				Log.e("ChatActivity", "Failed to get first user name: " + databaseError.getMessage());
				FirstUserName = "Unknown User";
			}
		});

		_getChatMessagesRef();
	}


	public void _getChatMessagesRef() {
		// Initial load
		Query getChatsMessages = chatMessagesRef.limitToLast(CHAT_PAGE_SIZE);
		getChatsMessages.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				try {
					if(dataSnapshot.exists()) {
						ChatMessagesListRecycler.setVisibility(View.VISIBLE);
						noChatText.setVisibility(View.GONE);
						// We clear the list here before the initial load
						ChatMessagesList.clear();
						ArrayList<HashMap<String, Object>> initialMessages = new ArrayList<>();
						
						for (DataSnapshot _data : dataSnapshot.getChildren()) {
							try {
								HashMap<String, Object> messageData = _data.getValue(new GenericTypeIndicator<HashMap<String, Object>>() {});
								if (messageData != null && messageData.containsKey(KEY_KEY) && messageData.get(KEY_KEY) != null) {
									initialMessages.add(messageData);
								} else {
									Log.w("ChatActivity", "Skipping initial message without valid key: " + _data.getKey());
								}
							} catch (Exception e) {
								Log.e("ChatActivity", "Error processing initial message data: " + e.getMessage());
							}
						}

						if (!initialMessages.isEmpty()) {
							// Safely get the oldest message key
							HashMap<String, Object> oldestMessage = initialMessages.get(0);
							if (oldestMessage != null && oldestMessage.containsKey(KEY_KEY) && oldestMessage.get(KEY_KEY) != null) {
								oldestMessageKey = oldestMessage.get(KEY_KEY).toString();
							}

							ChatMessagesList.addAll(initialMessages);
							if (chatAdapter != null) {
								chatAdapter.notifyDataSetChanged();
							}
							ChatMessagesListRecycler.scrollToPosition(ChatMessagesList.size() - 1);
						}
					} else {
						ChatMessagesListRecycler.setVisibility(View.GONE);
						noChatText.setVisibility(View.VISIBLE);
					}
				} catch (Exception e) {
					Log.e("ChatActivity", "Error processing initial chat messages: " + e.getMessage());
					ChatMessagesListRecycler.setVisibility(View.GONE);
					noChatText.setVisibility(View.VISIBLE);
				}
			}
			
			@Override 
			public void onCancelled(@NonNull DatabaseError databaseError) {
				Log.e("ChatActivity", "Initial message load failed: " + databaseError.getMessage());
				ChatMessagesListRecycler.setVisibility(View.GONE);
				noChatText.setVisibility(View.VISIBLE);
			}
		});
	}

	private void _attachChatListener() {
		if (_chat_child_listener == null) {
			_chat_child_listener = new ChildEventListener() {
				@Override
				public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
					if (dataSnapshot.exists()) {
						HashMap<String, Object> newMessage = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, Object>>() {});
						if (newMessage != null && newMessage.get(KEY_KEY) != null) {
							// Check if message already exists to avoid duplicates
							boolean exists = false;
							for (HashMap<String, Object> msg : ChatMessagesList) {
								if (msg.get(KEY_KEY) != null && msg.get(KEY_KEY).equals(newMessage.get(KEY_KEY))) {
									exists = true;
									break;
								}
							}

							if (!exists) {
								if (ChatMessagesListRecycler.getVisibility() == View.GONE) {
									ChatMessagesListRecycler.setVisibility(View.VISIBLE);
									noChatText.setVisibility(View.GONE);
								}
								
								// Add new message to the end
								ChatMessagesList.add(newMessage);
								int newPosition = ChatMessagesList.size() - 1;
								
								// Notify adapter of the new item
								chatAdapter.notifyItemInserted(newPosition);
								
								// Update previous item's timestamp if needed
								if (newPosition > 0) {
									chatAdapter.notifyItemChanged(newPosition - 1);
								}
								
								// Scroll to the new message with a slight delay to ensure layout is complete
								ChatMessagesListRecycler.post(() -> {
									scrollToBottom();
								});
							}
						}
					}
				}

				@Override
				public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
					if (snapshot.exists()) {
						HashMap<String, Object> updatedMessage = snapshot.getValue(new GenericTypeIndicator<HashMap<String, Object>>() {});
						if (updatedMessage != null && updatedMessage.get(KEY_KEY) != null) {
							String key = updatedMessage.get(KEY_KEY).toString();
							
							// Find the exact position of the message to update
							for (int i = 0; i < ChatMessagesList.size(); i++) {
								if (ChatMessagesList.get(i).get(KEY_KEY) != null && 
									ChatMessagesList.get(i).get(KEY_KEY).toString().equals(key)) {
									
									// Update the message in the list
									ChatMessagesList.set(i, updatedMessage);
									
									// Notify adapter of the specific item change
									chatAdapter.notifyItemChanged(i);
									break;
								}
							}
						}
					}
				}

				@Override
				public void onChildRemoved(@NonNull DataSnapshot snapshot) {
					if (snapshot.exists()) {
						String removedKey = snapshot.getKey();
						if (removedKey != null) {
							// Find and remove the message by key (not by position)
							for (int i = 0; i < ChatMessagesList.size(); i++) {
								if (ChatMessagesList.get(i).get(KEY_KEY) != null && 
									ChatMessagesList.get(i).get(KEY_KEY).toString().equals(removedKey)) {
									
									// Remove the message from the list
									ChatMessagesList.remove(i);
									
									// Notify adapter of the removal
									chatAdapter.notifyItemRemoved(i);
									
									// Update the last item's timestamp if needed
									if (!ChatMessagesList.isEmpty() && i < ChatMessagesList.size()) {
										chatAdapter.notifyItemChanged(Math.min(i, ChatMessagesList.size() - 1));
									}
									
									// Check if list is empty
									if (ChatMessagesList.isEmpty()) {
										ChatMessagesListRecycler.setVisibility(View.GONE);
										noChatText.setVisibility(View.VISIBLE);
									}
									break;
								}
							}
						}
					}
				}

				@Override public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
				@Override public void onCancelled(@NonNull DatabaseError error) {
					Log.e("ChatActivity", "Chat listener cancelled: " + error.getMessage());
				}
			};
			chatMessagesRef.addChildEventListener(_chat_child_listener);
		}
	}

	/**
	 * Scrolls the RecyclerView to the bottom smoothly
	 */
	private void scrollToBottom() {
		if (ChatMessagesListRecycler != null && !ChatMessagesList.isEmpty()) {
			ChatMessagesListRecycler.smoothScrollToPosition(ChatMessagesList.size() - 1);
		}
	}

	/**
	 * Scrolls the RecyclerView to the bottom immediately
	 */
	private void scrollToBottomImmediate() {
		if (ChatMessagesListRecycler != null && !ChatMessagesList.isEmpty()) {
			ChatMessagesListRecycler.scrollToPosition(ChatMessagesList.size() - 1);
		}
	}

	private void _detachChatListener() {
		if (_chat_child_listener != null) {
			chatMessagesRef.removeEventListener(_chat_child_listener);
			_chat_child_listener = null;
		}
	}

	private void _attachUserStatusListener() {
		if (_userStatusListener == null) {
			_userStatusListener = new ValueEventListener() {
				@Override
				public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
					if (dataSnapshot.exists()) {
						updateUserProfile(dataSnapshot);
						updateUserBadges(dataSnapshot);
					}
				}

				@Override
				public void onCancelled(@NonNull DatabaseError databaseError) {
					Log.e("ChatActivity", "Failed to get user reference: " + databaseError.getMessage());
				}
			};
			userRef.addValueEventListener(_userStatusListener);
		}
	}

	private void _detachUserStatusListener() {
		if (_userStatusListener != null) {
			userRef.removeEventListener(_userStatusListener);
			_userStatusListener = null;
		}
	}


	public void _AudioRecorderStart() {
		cc = Calendar.getInstance();
		recordMs = 0;
		AudioMessageRecorder = new MediaRecorder();

		File getCacheDir = getExternalCacheDir();
		String getCacheDirName = "audio_records";
		File getCacheFolder = new File(getCacheDir, getCacheDirName);
		getCacheFolder.mkdirs();
		File getRecordFile = new File(getCacheFolder, cc.getTimeInMillis() + ".mp3");
		String recordFilePath = getRecordFile.getAbsolutePath();

		AudioMessageRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		AudioMessageRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		AudioMessageRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
		AudioMessageRecorder.setAudioEncodingBitRate(320000);
		AudioMessageRecorder.setOutputFile(recordFilePath);

		try {
			AudioMessageRecorder.prepare();
			AudioMessageRecorder.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

		vbr.vibrate((long)(48));
		timer = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						recordMs = recordMs + 500;

					}
				});
			}
		};
		_timer.scheduleAtFixedRate(timer, (int)(0), (int)(500));

	}


	public void _AudioRecorderStop() {
		if (AudioMessageRecorder != null) {
			AudioMessageRecorder.stop();
			AudioMessageRecorder.release();
			AudioMessageRecorder = null;
		}
		vbr.vibrate((long)(48));
		timer.cancel();
	}


	public String _getDurationString(final long _durationInMillis) {
		long seconds = _durationInMillis / 1000;
		long minutes = seconds / 60;
		long hours = minutes / 60;
		seconds %= 60;
		minutes %= 60;

		if (hours > 0) {
			return String.format("%02d:%02d:%02d", hours, minutes, seconds);
		} else {
			return String.format("%02d:%02d", minutes, seconds);
		}
	}


	public void _getOldChatMessagesRef() {
		if (isLoading || oldestMessageKey == null) {
			return;
		}
		isLoading = true;
		_showLoadMoreIndicator();

		Query getChatsMessages = _firebase.getReference(SKYLINE_REF).child(CHATS_REF)
		.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
		.child(getIntent().getStringExtra(UID_KEY))
		.orderByKey()
		.endBefore(oldestMessageKey)
		.limitToLast(CHAT_PAGE_SIZE);

		getChatsMessages.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				_hideLoadMoreIndicator();
				try {
					if(dataSnapshot.exists()) {
						ArrayList<HashMap<String, Object>> newMessages = new ArrayList<>();
						for (DataSnapshot _data : dataSnapshot.getChildren()) {
							try {
								HashMap<String, Object> messageData = _data.getValue(new GenericTypeIndicator<HashMap<String, Object>>() {});
								if (messageData != null && messageData.containsKey(KEY_KEY) && messageData.get(KEY_KEY) != null) {
									newMessages.add(messageData);
								} else {
									Log.w("ChatActivity", "Skipping message without valid key: " + _data.getKey());
								}
							} catch (Exception e) {
								Log.e("ChatActivity", "Error processing message data: " + e.getMessage());
							}
						}

						if (!newMessages.isEmpty()) {
							// Safely get the oldest message key
							HashMap<String, Object> oldestMessage = newMessages.get(0);
							if (oldestMessage != null && oldestMessage.containsKey(KEY_KEY) && oldestMessage.get(KEY_KEY) != null) {
								oldestMessageKey = oldestMessage.get(KEY_KEY).toString();
							}

							final LinearLayoutManager layoutManager = (LinearLayoutManager) ChatMessagesListRecycler.getLayoutManager();
							if (layoutManager != null) {
								int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
								View firstVisibleView = layoutManager.findViewByPosition(firstVisiblePosition);
								int topOffset = (firstVisibleView != null) ? firstVisibleView.getTop() : 0;

								ChatMessagesList.addAll(0, newMessages);
								if (chatAdapter != null) {
									chatAdapter.notifyItemRangeInserted(0, newMessages.size());
								}

								// Restore scroll position to the item that was previously at the top
								if (firstVisibleView != null) {
									layoutManager.scrollToPositionWithOffset(firstVisiblePosition + newMessages.size(), topOffset);
								}
							}
						}
					}
				} catch (Exception e) {
					Log.e("ChatActivity", "Error processing old messages: " + e.getMessage());
				} finally {
					isLoading = false;
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				_hideLoadMoreIndicator();
				isLoading = false;
				Log.e("ChatActivity", "Failed to load old messages: " + databaseError.getMessage());
			}
		});
	}


	public void _DeleteMessageDialog(final ArrayList<HashMap<String, Object>> _data, final double _position) {
		// Material Delete Dialog
		MaterialAlertDialogBuilder zorry = new MaterialAlertDialogBuilder(ChatActivity.this);

		zorry.setTitle("Delete");
		zorry.setMessage("Are you sure you want to delete this message. Please confirm your decision.");
		zorry.setIcon(R.drawable.popup_ic_3);
		zorry.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				_firebase.getReference(SKYLINE_REF).child(CHATS_REF).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getIntent().getStringExtra(UID_KEY)).child(_data.get((int)_position).get(KEY_KEY).toString()).removeValue();
				_firebase.getReference(SKYLINE_REF).child(CHATS_REF).child(getIntent().getStringExtra(UID_KEY)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(_data.get((int)_position).get(KEY_KEY).toString()).removeValue();
			}
		});
		zorry.setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {

			}
		});
		zorry.create().show();
	}


	public void _ScrollingText(final TextView _view) {
		_view.setSingleLine(true);
		_view.setEllipsize(TextUtils.TruncateAt.MARQUEE);
		_view.setSelected(true);
	}


	public void _setUserLastSeen(final double _currentTime, final TextView _txt) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c2.setTimeInMillis((long)_currentTime);

		long time_diff = c1.getTimeInMillis() - c2.getTimeInMillis();

		long seconds = time_diff / 1000;
		long minutes = seconds / 60;
		long hours = minutes / 60;
		long days = hours / 24;
		long weeks = days / 7;
		long months = days / 30;
		long years = days / 365;

		if (seconds < 60) {
			if (seconds < 2) {
				_txt.setText("1 " + getResources().getString(R.string.status_text_seconds));
			} else {
				_txt.setText(String.valueOf(seconds) + " " + getResources().getString(R.string.status_text_seconds));
			}
		} else if (minutes < 60) {
			if (minutes < 2) {
				_txt.setText("1 " + getResources().getString(R.string.status_text_minutes));
			} else {
				_txt.setText(String.valueOf(minutes) + " " + getResources().getString(R.string.status_text_minutes));
			}
		} else if (hours < 24) {
			if (hours < 2) {
				_txt.setText("1 " + getResources().getString(R.string.status_text_hours));
			} else {
				_txt.setText(String.valueOf(hours) + " " + getResources().getString(R.string.status_text_hours));
			}
		} else if (days < 7) {
			if (days < 2) {
				_txt.setText("1 " + getResources().getString(R.string.status_text_days));
			} else {
				_txt.setText(String.valueOf(days) + " " + getResources().getString(R.string.status_text_days));
			}
		} else if (weeks < 4) {
			if (weeks < 2) {
				_txt.setText("1 " + getResources().getString(R.string.status_text_week));
			} else {
				_txt.setText(String.valueOf(weeks) + " " + getResources().getString(R.string.status_text_week));
			}
		} else if (months < 12) {
			if (months < 2) {
				_txt.setText("1 " + getResources().getString(R.string.status_text_month));
			} else {
				_txt.setText(String.valueOf(months) + " " + getResources().getString(R.string.status_text_month));
			}
		} else {
			if (years < 2) {
				_txt.setText("1 " + getResources().getString(R.string.status_text_years));
			} else {
				_txt.setText(String.valueOf(years) + " " + getResources().getString(R.string.status_text_years));
			}
		}
	}


	public void _textview_mh(final TextView _txt, final String _value) {
		_txt.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
		//_txt.setTextIsSelectable(true);
		updateSpan(_value, _txt);
	}
	private void updateSpan(String str, TextView _txt){
		SpannableStringBuilder ssb = new SpannableStringBuilder(str);
		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(?<![^\\s])(([@]{1}|[#]{1})([A-Za-z0-9_-]\\.?)+)(?![^\\s,])|\\*\\*(.*?)\\*\\*|__(.*?)__|~~(.*?)~~|_(.*?)_|\\*(.*?)\\*|///(.*?)///");
		java.util.regex.Matcher matcher = pattern.matcher(str);
		int offset = 0;

		while (matcher.find()) {
			int start = matcher.start() + offset;
			int end = matcher.end() + offset;

			if (matcher.group(3) != null) {
				// For mentions or hashtags
				ProfileSpan span = new ProfileSpan();
				ssb.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			} else if (matcher.group(4) != null) {
				// For bold text (**bold**)
				String boldText = matcher.group(4); // Extract text inside **
				ssb.replace(start, end, boldText);
				ssb.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), start, start + boldText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				offset -= 4; // Update offset for bold text replacement
			} else if (matcher.group(5) != null) {
				// For italic text (__italic__)
				String italicText = matcher.group(5);
				ssb.replace(start, end, italicText);
				ssb.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.ITALIC), start, start + italicText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				offset -= 4; // Update offset for italic text replacement
			} else if (matcher.group(6) != null) {
				// For strikethrough text (~~strikethrough~~)
				String strikethroughText = matcher.group(6);
				ssb.replace(start, end, strikethroughText);
				ssb.setSpan(new android.text.style.StrikethroughSpan(), start, start + strikethroughText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				offset -= 4; // Update offset for strikethrough text replacement
			} else if (matcher.group(7) != null) {
				// For underline text (_underline_)
				String underlineText = matcher.group(7);
				ssb.replace(start, end, underlineText);
				ssb.setSpan(new android.text.style.UnderlineSpan(), start, start + underlineText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				offset -= 2; // Update offset for underline text replacement
			} else if (matcher.group(8) != null) {
				// For italic text (*italic*)
				String italicText = matcher.group(8);
				ssb.replace(start, end, italicText);
				ssb.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.ITALIC), start, start + italicText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				offset -= 2; // Update offset for italic text replacement
			} else if (matcher.group(9) != null) {
				// For bold-italic text (///bold-italic///)
				String boldItalicText = matcher.group(9);
				ssb.replace(start, end, boldItalicText);
				ssb.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD_ITALIC), start, start + boldItalicText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				offset -= 6; // Update offset for bold-italic text replacement
			}
		}
		_txt.setText(ssb);
	}
	private class ProfileSpan extends android.text.style.ClickableSpan{


		@Override
		public void onClick(View view){

			if(view instanceof TextView){
				TextView tv = (TextView)view;

				if(tv.getText() instanceof Spannable){
					Spannable sp = (Spannable)tv.getText();

					int start = sp.getSpanStart(this);
					int end = sp.getSpanEnd(this);
					object_clicked = sp.subSequence(start,end).toString();
					handle = object_clicked.replace("@", "");
					DatabaseReference getReference = _firebase.getReference(USERNAME_REF).child(handle);
					getReference.addListenerForSingleValueEvent(new ValueEventListener() {
						@Override
						public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
							if(dataSnapshot.exists()) {
								if (!dataSnapshot.child(UID_KEY).getValue(String.class).equals("null")) {
									intent.setClass(getApplicationContext(), ProfileActivity.class);
									intent.putExtra(UID_KEY, dataSnapshot.child(UID_KEY).getValue(String.class));
									startActivity(intent);
								} else {

								}
							} else {
							}
						}
						@Override
						public void onCancelled(@NonNull DatabaseError databaseError) {
							//	swipeLayout.setVisibility(View.GONE);
							//noInternetBody.setVisibility(View.VISIBLE);
							//	loadingBody.setVisibility(View.GONE);
						}
					});
				}
			}

		}
		@Override
		public void updateDrawState(TextPaint ds) {
			ds.setUnderlineText(false);
			ds.setColor(Color.parseColor("#FFFF00"));
			ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
		}
	}


	public void _send_btn() {
		final String messageText = message_et.getText().toString().trim();
		final String senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
		final String recipientUid = getIntent().getStringExtra("uid");

		// The message sending logic is now wrapped inside a Realtime Database query.
		// First, we fetch the recipient's OneSignal Player ID from the 'users' node.
		_firebase.getReference(SKYLINE_REF).child(USERS_REF).child(recipientUid)
			.addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
					String recipientOneSignalPlayerId = "missing_id"; // Default value

					if (dataSnapshot.exists() && dataSnapshot.hasChild("oneSignalPlayerId")) {
						String fetchedId = dataSnapshot.child("oneSignalPlayerId").getValue(String.class);
						if (fetchedId != null && !fetchedId.isEmpty()) {
							recipientOneSignalPlayerId = fetchedId;
						} else {
							Log.e("ChatActivity", "Recipient's OneSignal Player ID is null or empty in the database.");
						}
					} else {
						Log.e("ChatActivity", "Recipient's user node or oneSignalPlayerId not found in Realtime Database.");
					}

					// After fetching the ID (or failing), proceed with sending the message.
					proceedWithMessageSending(messageText, senderUid, recipientUid, recipientOneSignalPlayerId);
				}

				@Override
				public void onCancelled(@NonNull DatabaseError databaseError) {
					Log.e("ChatActivity", "Failed to fetch recipient's data from RTDB. Sending message without notification.", databaseError.toException());
					// Still send the message, just without the notification.
					proceedWithMessageSending(messageText, senderUid, recipientUid, "missing_id");
				}
			});
	}

	/**
	 * This helper method contains the original logic for sending a message.
	 * It's now called after the recipient's OneSignal ID has been fetched.
	 */
	private void proceedWithMessageSending(String messageText, String senderUid, String recipientUid, String recipientOneSignalPlayerId) {
		if (!attactmentmap.isEmpty()) {
			// Logic for sending messages with attachments
			ArrayList<HashMap<String, Object>> successfulAttachments = new ArrayList<>();
			boolean allUploadsSuccessful = true;
			for (HashMap<String, Object> item : attactmentmap) {
				if ("success".equals(item.get("uploadState"))) {
					HashMap<String, Object> attachmentData = new HashMap<>();
					attachmentData.put("url", item.get("cloudinaryUrl"));
					attachmentData.put("publicId", item.get("publicId"));
					attachmentData.put("width", item.get("width"));
					attachmentData.put("height", item.get("height"));
					successfulAttachments.add(attachmentData);
				} else {
					allUploadsSuccessful = false;
				}
			}

			if (allUploadsSuccessful && (!messageText.isEmpty() || !successfulAttachments.isEmpty())) {
				String uniqueMessageKey = main.push().getKey();
				ChatSendMap = new HashMap<>();
				ChatSendMap.put(UID_KEY, senderUid);
				ChatSendMap.put(TYPE_KEY, ATTACHMENT_MESSAGE_TYPE);
				ChatSendMap.put(MESSAGE_TEXT_KEY, messageText);
				ChatSendMap.put(ATTACHMENTS_KEY, successfulAttachments);
				ChatSendMap.put(MESSAGE_STATE_KEY, "sended");
				if (!ReplyMessageID.equals("null")) ChatSendMap.put(REPLIED_MESSAGE_ID_KEY, ReplyMessageID);
				ChatSendMap.put(KEY_KEY, uniqueMessageKey);
				ChatSendMap.put(PUSH_DATE_KEY, String.valueOf(Calendar.getInstance().getTimeInMillis()));

				// Send to both chat nodes using setValue for proper real-time updates
				_firebase.getReference(SKYLINE_REF).child(CHATS_REF).child(senderUid).child(recipientUid).child(uniqueMessageKey).setValue(ChatSendMap);
				_firebase.getReference(SKYLINE_REF).child(CHATS_REF).child(recipientUid).child(senderUid).child(uniqueMessageKey).setValue(ChatSendMap);

				String lastMessage = messageText.isEmpty() ? successfulAttachments.size() + " attachment(s)" : messageText;

				// Smart Notification Check
				NotificationHelper.sendMessageAndNotifyIfNeeded(senderUid, recipientUid, recipientOneSignalPlayerId, lastMessage);

				_updateInbox(lastMessage);

				// Clear UI
				attactmentmap.clear();
				rv_attacmentList.getAdapter().notifyDataSetChanged();
				attachmentLayoutListHolder.setVisibility(View.GONE);
				message_et.setText("");
				ReplyMessageID = "null";
				mMessageReplyLayout.setVisibility(View.GONE);

			} else {
				Toast.makeText(getApplicationContext(), "Waiting for uploads to complete...", Toast.LENGTH_SHORT).show();
			}

		} else if (!messageText.isEmpty()) {
			// Logic for sending text-only messages
			String uniqueMessageKey = main.push().getKey();
			ChatSendMap = new HashMap<>();
			ChatSendMap.put(UID_KEY, senderUid);
			ChatSendMap.put(TYPE_KEY, MESSAGE_TYPE);
			ChatSendMap.put(MESSAGE_TEXT_KEY, messageText);
			ChatSendMap.put(MESSAGE_STATE_KEY, "sended");
			if (!ReplyMessageID.equals("null")) ChatSendMap.put(REPLIED_MESSAGE_ID_KEY, ReplyMessageID);
			ChatSendMap.put(KEY_KEY, uniqueMessageKey);
			ChatSendMap.put(PUSH_DATE_KEY, String.valueOf(Calendar.getInstance().getTimeInMillis()));

			// Send to both chat nodes using setValue for proper real-time updates
			_firebase.getReference(SKYLINE_REF).child(CHATS_REF).child(senderUid).child(recipientUid).child(uniqueMessageKey).setValue(ChatSendMap);
			_firebase.getReference(SKYLINE_REF).child(CHATS_REF).child(recipientUid).child(senderUid).child(uniqueMessageKey).setValue(ChatSendMap);

			// Smart Notification Check
			NotificationHelper.sendMessageAndNotifyIfNeeded(senderUid, recipientUid, recipientOneSignalPlayerId, messageText);

			_updateInbox(messageText);

			// Clear UI
			message_et.setText("");
			ReplyMessageID = "null";
			mMessageReplyLayout.setVisibility(View.GONE);
		}
	}


	public void _Block(final String _uid) {
		block = new HashMap<>();
		block.put(_uid, "true");
		blocklist.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(block);
		block.clear();
	}


	public void _TransitionManager(final View _view, final double _duration) {
		LinearLayout viewgroup =(LinearLayout) _view;

		android.transition.AutoTransition autoTransition = new android.transition.AutoTransition(); autoTransition.setDuration((long)_duration); android.transition.TransitionManager.beginDelayedTransition(viewgroup, autoTransition);
	}


	public void _Unblock_this_user() {
		DatabaseReference blocklistRef = FirebaseDatabase.getInstance().getReference("skyline/blocklist");
		String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
		String uidToRemove = getIntent().getStringExtra("uid");

		blocklistRef.child(myUid).child(uidToRemove).removeValue()
		.addOnSuccessListener(aVoid -> {
			// Create a new intent to restart the activity
			Intent intent = getIntent();
			// Optional: Add flags to clear the activity stack if needed
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			finish();
			startActivity(intent);
		})
		.addOnFailureListener(e -> {
			// Handle any errors here
			Log.e("UnblockUser", "Failed to unblock user", e);
		});
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


	public void _ImgRound(final ImageView _imageview, final double _value) {
		android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable ();
		gd.setColor(android.R.color.transparent);
		gd.setCornerRadius((int)_value);
		_imageview.setClipToOutline(true);
		_imageview.setBackground(gd);
	}


	public void _OpenWebView(final String _URL) {
		AndroidDevelopersBlogURL = _URL;
		CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
		builder.setToolbarColor(Color.parseColor("#242D39"));
		CustomTabsIntent customtabsintent = builder.build();
		customtabsintent.launchUrl(this, Uri.parse(AndroidDevelopersBlogURL));
	}


	public void _startUploadForItem(final double _position) {
		// Use the correct parameter name '_position' as defined by your More Block
		final int itemPosition = (int) _position;

		// Safety check for position bounds
		if (itemPosition < 0 || itemPosition >= attactmentmap.size()) {
			Log.e("ChatActivity", "Invalid position for upload: " + itemPosition + ", size: " + attactmentmap.size());
			return;
		}

		// Check for internet connection first.
		if (!SketchwareUtil.isConnected(getApplicationContext())) {
			try {
				HashMap<String, Object> itemMap = attactmentmap.get(itemPosition);
				if (itemMap != null) {
					itemMap.put("uploadState", "failed");
					if (rv_attacmentList.getAdapter() != null) {
						rv_attacmentList.getAdapter().notifyItemChanged(itemPosition);
					}
				}
			} catch (Exception e) {
				Log.e("ChatActivity", "Error updating upload state: " + e.getMessage());
			}
			return;
		}

		HashMap<String, Object> itemMap = attactmentmap.get(itemPosition);
		if (itemMap == null || !"pending".equals(itemMap.get("uploadState"))) {
			return;
		}
		
		itemMap.put("uploadState", "uploading");
		itemMap.put("uploadProgress", 0.0);
		
		if (rv_attacmentList.getAdapter() != null) {
			rv_attacmentList.getAdapter().notifyItemChanged(itemPosition);
		}

		String filePath = itemMap.get("localPath").toString();
		if (filePath == null || filePath.isEmpty()) {
			Log.e("ChatActivity", "Invalid file path for upload");
			itemMap.put("uploadState", "failed");
			if (rv_attacmentList.getAdapter() != null) {
				rv_attacmentList.getAdapter().notifyItemChanged(itemPosition);
			}
			return;
		}

		File file = new File(filePath);
		if (!file.exists()) {
			Log.e("ChatActivity", "File does not exist: " + filePath);
			itemMap.put("uploadState", "failed");
			if (rv_attacmentList.getAdapter() != null) {
				rv_attacmentList.getAdapter().notifyItemChanged(itemPosition);
			}
			return;
		}

		UploadFiles.uploadFile(filePath, file.getName(), new UploadFiles.UploadCallback() {
			@Override
			public void onProgress(int percent) {
				try {
					if (itemPosition >= 0 && itemPosition < attactmentmap.size()) {
						HashMap<String, Object> currentItem = attactmentmap.get(itemPosition);
						if (currentItem != null) {
							currentItem.put("uploadProgress", (double) percent);
							if (rv_attacmentList.getAdapter() != null) {
								rv_attacmentList.getAdapter().notifyItemChanged(itemPosition);
							}
						}
					}
				} catch (Exception e) {
					Log.e("ChatActivity", "Error updating upload progress: " + e.getMessage());
				}
			}
			
			@Override
			public void onSuccess(String url, String publicId) {
				try {
					if (itemPosition >= 0 && itemPosition < attactmentmap.size()) {
						HashMap<String, Object> mapToUpdate = attactmentmap.get(itemPosition);
						if (mapToUpdate != null) {
							mapToUpdate.put("uploadState", "success");
							mapToUpdate.put("cloudinaryUrl", url); // Keep this key for consistency in _send_btn
							mapToUpdate.put("publicId", publicId);
							if (rv_attacmentList.getAdapter() != null) {
								rv_attacmentList.getAdapter().notifyItemChanged(itemPosition);
							}

							// Set the URL to the 'path' variable instead of message_et
							path = url;
						}
					}
				} catch (Exception e) {
					Log.e("ChatActivity", "Error updating upload success: " + e.getMessage());
				}
			}
			
			@Override
			public void onFailure(String error) {
				try {
					if (itemPosition >= 0 && itemPosition < attactmentmap.size()) {
						HashMap<String, Object> currentItem = attactmentmap.get(itemPosition);
						if (currentItem != null) {
							currentItem.put("uploadState", "failed");
							if (rv_attacmentList.getAdapter() != null) {
								rv_attacmentList.getAdapter().notifyItemChanged(itemPosition);
							}
						}
					}
					Log.e("ChatActivity", "Upload failed: " + error);
				} catch (Exception e) {
					Log.e("ChatActivity", "Error updating upload failure: " + e.getMessage());
				}
			}
		});
	}


	public void _updateInbox(final String _lastMessage) {
		// Using the correct parameter name '_lastMessage' with the underscore prefix.
		cc = Calendar.getInstance();

		// Update inbox for the current user
		ChatInboxSend = new HashMap<>();
		ChatInboxSend.put(UID_KEY, getIntent().getStringExtra(UID_KEY));
		ChatInboxSend.put(LAST_MESSAGE_UID_KEY, FirebaseAuth.getInstance().getCurrentUser().getUid());
		ChatInboxSend.put(LAST_MESSAGE_TEXT_KEY, _lastMessage); // <-- CORRECTED
		ChatInboxSend.put(LAST_MESSAGE_STATE_KEY, "sended");
		ChatInboxSend.put(PUSH_DATE_KEY, String.valueOf((long)(cc.getTimeInMillis())));
		_firebase.getReference(SKYLINE_REF).child(INBOX_REF).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getIntent().getStringExtra(UID_KEY)).setValue(ChatInboxSend);

		// Update inbox for the other user
		ChatInboxSend2 = new HashMap<>();
		ChatInboxSend2.put(UID_KEY, FirebaseAuth.getInstance().getCurrentUser().getUid());
		ChatInboxSend2.put(LAST_MESSAGE_UID_KEY, FirebaseAuth.getInstance().getCurrentUser().getUid());
		ChatInboxSend2.put(LAST_MESSAGE_TEXT_KEY, _lastMessage); // <-- CORRECTED
		ChatInboxSend2.put(LAST_MESSAGE_STATE_KEY, "sended");
		ChatInboxSend2.put(PUSH_DATE_KEY, String.valueOf((long)(cc.getTimeInMillis())));
		_firebase.getReference(SKYLINE_REF).child(INBOX_REF).child(getIntent().getStringExtra(UID_KEY)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(ChatInboxSend2);
	}


	public void _showLoadMoreIndicator() {
		if (!ChatMessagesList.isEmpty() && !ChatMessagesList.get(0).containsKey("isLoadingMore")) {
			HashMap<String, Object> loadingMap = new HashMap<>();
			loadingMap.put("isLoadingMore", true);
			ChatMessagesList.add(0, loadingMap);
			((ChatAdapter)chatAdapter).notifyItemInserted(0);
		}
	}


	public void _hideLoadMoreIndicator() {
		if (!ChatMessagesList.isEmpty() && ChatMessagesList.get(0).containsKey("isLoadingMore")) {
			ChatMessagesList.remove(0);
			((ChatAdapter)chatAdapter).notifyItemRemoved(0);
		}
	}


	public void _showReplyUI(final double _position) {
		// This is where you trigger your reply UI.
		HashMap<String, Object> messageData = ChatMessagesList.get((int)_position);
		ReplyMessageID = messageData.get(KEY_KEY).toString();

		if (messageData.get(UID_KEY).toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
			mMessageReplyLayoutBodyRightUsername.setText(FirstUserName);
		} else {
			mMessageReplyLayoutBodyRightUsername.setText(SecondUserName);
		}
		mMessageReplyLayoutBodyRightMessage.setText(messageData.get(MESSAGE_TEXT_KEY).toString());
		mMessageReplyLayout.setVisibility(View.VISIBLE);
		vbr.vibrate((long)(48));
	}


	public void _setupSwipeToReply() {
		// This helper class handles drawing the swipe background and icon.
		ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
			@Override
			public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
				return false; // We don't want to handle drag & drop
			}

			@Override
			public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
				int position = viewHolder.getAdapterPosition();
				if (position < 0 || position >= ChatMessagesList.size()) {
					return; // Invalid position, do nothing.
				}

				// Get the item and check if it's a real message with a key.
				HashMap<String, Object> messageData = ChatMessagesList.get(position);
				if (messageData == null || !messageData.containsKey("key") || messageData.get("key") == null) {
					// This is not a real message (e.g., typing indicator, loading view).
					// We just notify the adapter to redraw the item back to its original state.
					chatAdapter.notifyItemChanged(position);
					return;
				}

				// If it's a real message, proceed with the reply UI.
				_showReplyUI(position);
				chatAdapter.notifyItemChanged(position);
			}

			@Override
			public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
				if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
					View itemView = viewHolder.itemView;
					Paint p = new Paint();
					Drawable icon = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_reply);

					if (dX > 0) { // Swiping to the right
						p.setColor(Color.parseColor("#3498db")); // Blue background
						c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom(), p);

						int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
						int iconTop = itemView.getTop() + iconMargin;
						int iconBottom = iconTop + icon.getIntrinsicHeight();
						int iconLeft = itemView.getLeft() + iconMargin;
						int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
						icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
						icon.draw(c);
					} else { // Swiping to the left
						p.setColor(Color.parseColor("#3498db")); // Blue background
						c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom(), p);

						int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
						int iconTop = itemView.getTop() + iconMargin;
						int iconBottom = iconTop + icon.getIntrinsicHeight();
						int iconRight = itemView.getRight() - iconMargin;
						int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
						icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
						icon.draw(c);
					}
					// Fade out the item as it is swiped away
					final float alpha = 1.0f - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
					viewHolder.itemView.setAlpha(alpha);
					viewHolder.itemView.setTranslationX(dX);
				} else {
					super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
				}
			}
		};
		ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
		itemTouchHelper.attachToRecyclerView(ChatMessagesListRecycler);
	}

	public String getChatId(String uid1, String uid2) {
		if (uid1.compareTo(uid2) > 0) {
			return uid1 + uid2;
		} else {
			return uid2 + uid1;
		}
	}

	public void scrollToMessage(final String _messageKey) {
		int position = -1;
		for (int i = 0; i < ChatMessagesList.size(); i++) {
			if (ChatMessagesList.get(i).get(KEY_KEY).toString().equals(_messageKey)) {
				position = i;
				break;
			}
		}
		if (position != -1) {
			ChatMessagesListRecycler.smoothScrollToPosition(position);
		} else {
			Toast.makeText(getApplicationContext(), "Original message not found", Toast.LENGTH_SHORT).show();
		}
	}

	private void startActivityWithUid(Class<?> activityClass) {
		Intent intent = new Intent(getApplicationContext(), activityClass);
		intent.putExtra(UID_KEY, getIntent().getStringExtra(UID_KEY));
		startActivity(intent);
	}

	private void callGemini(String prompt, boolean showThinking) {
		gemini.setModel(GEMINI_MODEL);
		gemini.setTone(GEMINI_TONE);
		gemini.setShowThinking(showThinking);
		gemini.setSystemInstruction(
		"You are a concise text assistant. Always return ONLY the transformed text (no explanation, no labels). " +
		"Preserve original formatting. Censor profanity by replacing letters with asterisks (e.g., s***t). " +
		"Keep the language and tone of the input unless asked to change it."
		);
		gemini.sendPrompt(prompt, new Gemini.GeminiCallback() {
			@Override
			public void onSuccess(String response) {
				runOnUiThread(() -> message_et.setText(response));
			}

			@Override
			public void onError(String error) {
				runOnUiThread(() -> message_et.setText("Error: " + error));
			}

			@Override
			public void onThinking() {
				if (showThinking) {
					runOnUiThread(() -> message_et.setText(gemini.getThinkingText()));
				}
			}
		});
	}

	private void callGeminiForSummary(String prompt, final BaseMessageViewHolder viewHolder) {
		Gemini summaryGemini = new Gemini.Builder(this)
				.model(GEMINI_MODEL)
				.tone(GEMINI_TONE)
				.showThinking(true)
				.systemInstruction("You are a text summarizer. Provide a concise summary of the given text.")
				.build();

		summaryGemini.sendPrompt(prompt, new Gemini.GeminiCallback() {
			@Override
			public void onSuccess(String response) {
				runOnUiThread(() -> {
					if (viewHolder != null) {
						viewHolder.stopShimmer();
					}
					SummaryBottomSheetDialogFragment bottomSheet = SummaryBottomSheetDialogFragment.newInstance(response);
					bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
				});
			}

			@Override
			public void onError(String error) {
				runOnUiThread(() -> {
					if (viewHolder != null) {
						viewHolder.stopShimmer();
					}
					Log.e("GeminiSummary", "Error: " + error);
					Toast.makeText(getApplicationContext(), "Error getting summary: " + error, Toast.LENGTH_SHORT).show();
				});
			}

			@Override
			public void onThinking() {
				runOnUiThread(() -> {
					if (viewHolder != null) {
						viewHolder.startShimmer();
					}
				});
			}
		});
	}

	private void handleBlocklistUpdate(DataSnapshot dataSnapshot) {
		GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
		final String _childKey = dataSnapshot.getKey();
		final HashMap<String, Object> _childValue = dataSnapshot.getValue(_ind);

		if (_childValue == null) return;

		String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
		String otherUid = getIntent().getStringExtra(UID_KEY);

		if (_childKey.equals(otherUid)) {
			if (_childValue.containsKey(myUid)) {
				message_input_overall_container.setVisibility(View.GONE);
				blocked_txt.setVisibility(View.VISIBLE);
			} else {
				message_input_overall_container.setVisibility(View.VISIBLE);
				blocked_txt.setVisibility(View.GONE);
			}
		}

		if (_childKey.equals(myUid)) {
			if (_childValue.containsKey(otherUid)) {
				message_input_overall_container.setVisibility(View.GONE);
			} else {
				message_input_overall_container.setVisibility(View.VISIBLE);
			}
		}
	}

	private void updateUserProfile(DataSnapshot dataSnapshot) {
		// Add null checks for critical fields
		if (dataSnapshot == null || !dataSnapshot.exists()) {
			Log.w("ChatActivity", "User profile data snapshot is null or doesn't exist");
			return;
		}
		
		// Check if user is banned
		String bannedStatus = dataSnapshot.child("banned").getValue(String.class);
		if ("true".equals(bannedStatus)) {
			topProfileLayoutProfileImage.setImageResource(R.drawable.banned_avatar);
			SecondUserAvatar = "null_banned";
			topProfileLayoutStatus.setTextColor(0xFF9E9E9E);
			topProfileLayoutStatus.setText(getResources().getString(R.string.offline));
		} else {
			// Handle avatar
			String avatarUrl = dataSnapshot.child("avatar").getValue(String.class);
			if (avatarUrl == null || "null".equals(avatarUrl)) {
				topProfileLayoutProfileImage.setImageResource(R.drawable.avatar);
				SecondUserAvatar = "null";
			} else {
				try {
					Glide.with(getApplicationContext()).load(Uri.parse(avatarUrl)).into(topProfileLayoutProfileImage);
					SecondUserAvatar = avatarUrl;
				} catch (Exception e) {
					Log.e("ChatActivity", "Error loading avatar: " + e.getMessage());
					topProfileLayoutProfileImage.setImageResource(R.drawable.avatar);
					SecondUserAvatar = "null";
				}
			}
		}

		// Handle nickname/username
		String nickname = dataSnapshot.child("nickname").getValue(String.class);
		if (nickname == null || "null".equals(nickname)) {
			String username = dataSnapshot.child("username").getValue(String.class);
			SecondUserName = username != null ? "@" + username : "Unknown User";
		} else {
			SecondUserName = nickname;
		}
		
		if (topProfileLayoutUsername != null) {
			topProfileLayoutUsername.setText(SecondUserName);
		}

		// Update adapter with user info
		if (chatAdapter != null) {
			chatAdapter.setSecondUserName(SecondUserName);
			chatAdapter.setFirstUserName(FirstUserName);
			chatAdapter.setSecondUserAvatar(SecondUserAvatar);
		}

		// Handle status
		String status = dataSnapshot.child("status").getValue(String.class);
		if (topProfileLayoutStatus != null) {
			if ("online".equals(status)) {
				topProfileLayoutStatus.setText(getResources().getString(R.string.online));
				topProfileLayoutStatus.setTextColor(0xFF2196F3);
			} else {
				if ("offline".equals(status)) {
					topProfileLayoutStatus.setText(getResources().getString(R.string.offline));
				} else {
					try {
						_setUserLastSeen(Double.parseDouble(status), topProfileLayoutStatus);
					} catch (NumberFormatException e) {
						Log.e("ChatActivity", "Invalid status timestamp: " + status);
						topProfileLayoutStatus.setText(getResources().getString(R.string.offline));
					}
				}
				topProfileLayoutStatus.setTextColor(0xFF757575);
			}
		}
	}

	private void updateUserBadges(DataSnapshot dataSnapshot) {
		// Add null checks for critical fields
		if (dataSnapshot == null || !dataSnapshot.exists()) {
			Log.w("ChatActivity", "User badge data snapshot is null or doesn't exist");
			return;
		}
		
		// Handle gender badge
		String gender = dataSnapshot.child("gender").getValue(String.class);
		if (topProfileLayoutGenderBadge != null) {
			if (gender == null || "hidden".equals(gender)) {
				topProfileLayoutGenderBadge.setVisibility(View.GONE);
			} else if ("male".equals(gender)) {
				topProfileLayoutGenderBadge.setImageResource(R.drawable.male_badge);
				topProfileLayoutGenderBadge.setVisibility(View.VISIBLE);
			} else if ("female".equals(gender)) {
				topProfileLayoutGenderBadge.setImageResource(R.drawable.female_badge);
				topProfileLayoutGenderBadge.setVisibility(View.VISIBLE);
			}
		}

		// Handle account type badge
		if (topProfileLayoutVerifiedBadge != null) {
			String accountType = dataSnapshot.child("account_type").getValue(String.class);
			topProfileLayoutVerifiedBadge.setVisibility(View.VISIBLE);
			
			if (accountType != null) {
				switch (accountType) {
					case "admin":
						topProfileLayoutVerifiedBadge.setImageResource(R.drawable.admin_badge);
						break;
					case "moderator":
						topProfileLayoutVerifiedBadge.setImageResource(R.drawable.moderator_badge);
						break;
					case "support":
						topProfileLayoutVerifiedBadge.setImageResource(R.drawable.support_badge);
						break;
					default:
						// Check for premium or verified status
						String accountPremium = dataSnapshot.child("account_premium").getValue(String.class);
						String verifyStatus = dataSnapshot.child("verify").getValue(String.class);
						
						if ("true".equals(accountPremium)) {
							topProfileLayoutVerifiedBadge.setImageResource(R.drawable.premium_badge);
						} else if ("true".equals(verifyStatus)) {
							topProfileLayoutVerifiedBadge.setImageResource(R.drawable.verified_badge);
						} else {
							topProfileLayoutVerifiedBadge.setVisibility(View.GONE);
						}
						break;
				}
			} else {
				// No account type specified, check for premium or verified status
				String accountPremium = dataSnapshot.child("account_premium").getValue(String.class);
				String verifyStatus = dataSnapshot.child("verify").getValue(String.class);
				
				if ("true".equals(accountPremium)) {
					topProfileLayoutVerifiedBadge.setImageResource(R.drawable.premium_badge);
				} else if ("true".equals(verifyStatus)) {
					topProfileLayoutVerifiedBadge.setImageResource(R.drawable.verified_badge);
				} else {
					topProfileLayoutVerifiedBadge.setVisibility(View.GONE);
				}
			}
		}
	}

	public class ChatMessagesListRecyclerAdapter extends RecyclerView.Adapter<ChatMessagesListRecyclerAdapter.ViewHolder> {

		ArrayList<HashMap<String, Object>> _data;

		public ChatMessagesListRecyclerAdapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater _inflater = getLayoutInflater();
			View _v = _inflater.inflate(R.layout.chat_msg_cv_synapse, null);
			RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			_v.setLayoutParams(_lp);
			return new ViewHolder(_v);
		}

		@Override
		public void onBindViewHolder(ViewHolder _holder, final int _position) {
			View _view = _holder.itemView;

			final LinearLayout body = _view.findViewById(R.id.body);
			final androidx.cardview.widget.CardView mProfileCard = _view.findViewById(R.id.mProfileCard);
			final LinearLayout message_layout = _view.findViewById(R.id.message_layout);
			final ImageView mProfileImage = _view.findViewById(R.id.mProfileImage);
			final LinearLayout menuView_d = _view.findViewById(R.id.menuView_d);
			final LinearLayout messageBG = _view.findViewById(R.id.messageBG);
			final LinearLayout my_message_info = _view.findViewById(R.id.my_message_info);
			final com.google.android.material.card.MaterialCardView mRepliedMessageLayout = _view.findViewById(R.id.mRepliedMessageLayout);
			final androidx.cardview.widget.CardView mMessageImageBody = _view.findViewById(R.id.mMessageImageBody);
			final com.airbnb.lottie.LottieAnimationView lottie1 = _view.findViewById(R.id.lottie1);
			final TextView message_text = _view.findViewById(R.id.message_text);
			final LinearLayout mRepliedMessageLayoutLeftBar = _view.findViewById(R.id.mRepliedMessageLayoutLeftBar);
			final LinearLayout mRepliedMessageLayoutRightBody = _view.findViewById(R.id.mRepliedMessageLayoutRightBody);
			final TextView mRepliedMessageLayoutUsername = _view.findViewById(R.id.mRepliedMessageLayoutUsername);
			final TextView mRepliedMessageLayoutMessage = _view.findViewById(R.id.mRepliedMessageLayoutMessage);
			final ImageView mMessageImageView = _view.findViewById(R.id.mMessageImageView);
			final TextView date = _view.findViewById(R.id.date);
			final ImageView message_state = _view.findViewById(R.id.message_state);
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

	public class Rv_attacmentListAdapter extends RecyclerView.Adapter<Rv_attacmentListAdapter.ViewHolder> {

		ArrayList<HashMap<String, Object>> _data;

		public Rv_attacmentListAdapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater _inflater = getLayoutInflater();
			View _v = _inflater.inflate(R.layout.chat_attactment, null);
			RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			_v.setLayoutParams(_lp);
			return new ViewHolder(_v);
		}

		@Override
		public void onBindViewHolder(ViewHolder _holder, final int _position) {
			View _view = _holder.itemView;

			final androidx.cardview.widget.CardView cardMediaItem = _view.findViewById(R.id.cardMediaItem);
			final RelativeLayout imageWrapperRL = _view.findViewById(R.id.imageWrapperRL);
			final ImageView previewIV = _view.findViewById(R.id.previewIV);
			final LinearLayout overlayLL = _view.findViewById(R.id.overlayLL);
			final com.google.android.material.progressindicator.CircularProgressIndicator uploadProgressCPI = _view.findViewById(R.id.uploadProgressCPI);
			final ImageView closeIV = _view.findViewById(R.id.closeIV);

			// Safety check for position bounds
			if (_position < 0 || _position >= attactmentmap.size()) {
				Log.w("ChatActivity", "Invalid position in attachment adapter: " + _position);
				_view.setVisibility(View.GONE);
				return;
			}

			// Get the data map using the block's parameters
			HashMap<String, Object> itemData = attactmentmap.get(_position);
			if (itemData == null) {
				Log.w("ChatActivity", "Null item data at position: " + _position);
				_view.setVisibility(View.GONE);
				return;
			}

			// --- START: ROBUSTNESS FIX ---
			// Safety Check: Verify that the required "localPath" key exists and is not null.
			if (!itemData.containsKey("localPath") || itemData.get("localPath") == null) {
				// If the data is invalid, hide this item completely to prevent a crash.
				_view.setVisibility(View.GONE);
				_view.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
				return; // Stop processing this broken item.
			}
			// If we pass this check, we can safely proceed.
			_view.setVisibility(View.VISIBLE);
			_view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			// --- END: ROBUSTNESS FIX ---

			// Set the image preview with error handling
			String localPath = itemData.get("localPath").toString();
			try {
				// Clear previous image to prevent recycling issues
				previewIV.setImageDrawable(null);
				
				// Load new image
				previewIV.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(localPath, 1024, 1024));
			} catch (Exception e) {
				Log.e("ChatActivity", "Error loading image preview: " + e.getMessage());
				// Set a placeholder image on error
				previewIV.setImageResource(R.drawable.ph_imgbluredsqure);
			}

			// Get the upload state and progress.
			String uploadState = itemData.getOrDefault("uploadState", "pending").toString();
			int progress = 0;
			if (itemData.containsKey("uploadProgress")) {
				try {
					progress = (int) Double.parseDouble(itemData.get("uploadProgress").toString());
				} catch (NumberFormatException e) {
					Log.w("ChatActivity", "Invalid upload progress value: " + itemData.get("uploadProgress"));
					progress = 0;
				}
			}

			// Update the UI by accessing views directly by their ID.
			switch (uploadState) {
				case "uploading":
					overlayLL.setVisibility(View.VISIBLE);
					overlayLL.setBackgroundColor(0x80000000);
					uploadProgressCPI.setVisibility(View.VISIBLE);
					uploadProgressCPI.setProgress(progress);
					closeIV.setVisibility(View.GONE);
					break;

				case "success":
					overlayLL.setVisibility(View.GONE);
					uploadProgressCPI.setVisibility(View.GONE);
					closeIV.setVisibility(View.VISIBLE);
					break;

				case "failed":
					overlayLL.setVisibility(View.VISIBLE);
					overlayLL.setBackgroundColor(0x80D32F2F);
					uploadProgressCPI.setVisibility(View.GONE);
					closeIV.setVisibility(View.VISIBLE);
					break;

				default: // "pending" state
					overlayLL.setVisibility(View.GONE);
					uploadProgressCPI.setVisibility(View.GONE);
					closeIV.setVisibility(View.VISIBLE);
					break;
			}

			// Set the click listener on the close icon with proper position handling
			closeIV.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// Use final position to avoid closure issues
					final int finalPosition = _position;
					
					if (finalPosition < 0 || finalPosition >= attactmentmap.size()) {
						Log.w("ChatActivity", "Invalid position for removal: " + finalPosition);
						return;
					}

					HashMap<String, Object> currentItemData = attactmentmap.get(finalPosition);
					if (currentItemData == null) {
						Log.w("ChatActivity", "Null item data for removal at position: " + finalPosition);
						return;
					}

					// Remove the item
					attactmentmap.remove(finalPosition);
					
					// Notify adapter of changes
					if (rv_attacmentList.getAdapter() != null) {
						rv_attacmentList.getAdapter().notifyItemRemoved(finalPosition);
						// Update remaining items
						rv_attacmentList.getAdapter().notifyItemRangeChanged(finalPosition, attactmentmap.size() - finalPosition);
					}

					// Delete from cloud storage if available
					if (currentItemData.containsKey("publicId")) {
						String publicId = currentItemData.get("publicId").toString();
						if (publicId != null && !publicId.isEmpty()) {
							UploadFiles.deleteByPublicId(publicId, new UploadFiles.DeleteCallback() {
								@Override 
								public void onSuccess() {
									Log.d("ChatActivity", "Successfully deleted attachment: " + publicId);
								}
								@Override 
								public void onFailure(String error) {
									Log.e("ChatActivity", "Failed to delete attachment: " + error);
								}
							});
						}
					}

					// Hide attachment holder if no more attachments
					if (attactmentmap.isEmpty()) {
						attachmentLayoutListHolder.setVisibility(View.GONE);
					}
				}
			});
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