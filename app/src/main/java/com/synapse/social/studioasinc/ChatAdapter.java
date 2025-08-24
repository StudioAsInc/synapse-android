package com.synapse.social.studioasinc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.synapse.social.studioasinc.styling.TextStylingUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import androidx.gridlayout.widget.GridLayout;
import android.widget.RelativeLayout;
import com.google.firebase.database.GenericTypeIndicator;
import android.view.MotionEvent;
import android.widget.Toast;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "ChatAdapter";
    private static final int VIEW_TYPE_TEXT = 1;
    private static final int VIEW_TYPE_MEDIA_GRID = 2;
    private static final int VIEW_TYPE_TYPING = 3;
    private static final int VIEW_TYPE_VIDEO = 4;
    private static final int VIEW_TYPE_LINK_PREVIEW = 5;
    private static final int VIEW_TYPE_LOADING_MORE = 99;

    private ArrayList<HashMap<String, Object>> _data;
    private Context _context;
    private String secondUserAvatarUrl = "";
    private String firstUserName = "";
    private String secondUserName = "";
    private SharedPreferences appSettings;
    private TextStylingUtil textStylingUtil;
    private ChatActivity chatActivity;

    public ChatAdapter(ArrayList<HashMap<String, Object>> _arr) { _data = _arr; }
    public void setChatActivity(ChatActivity activity) { this.chatActivity = activity; }
    public void setSecondUserAvatar(String url) { this.secondUserAvatarUrl = url; }
    public void setFirstUserName(String name) { this.firstUserName = name; }
    public void setSecondUserName(String name) { this.secondUserName = name; }

    @Override
    public int getItemViewType(int position) {
        if (_data.get(position).containsKey("isLoadingMore")) return VIEW_TYPE_LOADING_MORE;
        if (_data.get(position).containsKey("typingMessageStatus")) return VIEW_TYPE_TYPING;
        
        String type = _data.get(position).getOrDefault("TYPE", "MESSAGE").toString();
        Log.d(TAG, "Message at position " + position + " has type: " + type);
        
        if ("ATTACHMENT_MESSAGE".equals(type)) {
            ArrayList<HashMap<String, Object>> attachments = (ArrayList<HashMap<String, Object>>) _data.get(position).get("attachments");
            Log.d(TAG, "ATTACHMENT_MESSAGE detected with " + (attachments != null ? attachments.size() : 0) + " attachments");
            
            if (attachments != null && attachments.size() == 1 && String.valueOf(attachments.get(0).getOrDefault("publicId", "")).contains("|video")) {
                Log.d(TAG, "Video message detected, returning VIEW_TYPE_VIDEO");
                return VIEW_TYPE_VIDEO;
            }
            Log.d(TAG, "Media message detected, returning VIEW_TYPE_MEDIA_GRID");
            return VIEW_TYPE_MEDIA_GRID;
        }

        String messageText = String.valueOf(_data.get(position).getOrDefault("message_text", ""));
        if (LinkPreviewUtil.extractUrl(messageText) != null) {
            Log.d(TAG, "Link preview message detected, returning VIEW_TYPE_LINK_PREVIEW");
            return VIEW_TYPE_LINK_PREVIEW;
        }
        
        Log.d(TAG, "Text message detected, returning VIEW_TYPE_TEXT");
        return VIEW_TYPE_TEXT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        _context = parent.getContext();
        appSettings = _context.getSharedPreferences("appSettings", Context.MODE_PRIVATE);
        textStylingUtil = new TextStylingUtil(_context);
        LayoutInflater inflater = LayoutInflater.from(_context);
        switch (viewType) {
            case VIEW_TYPE_MEDIA_GRID: return new MediaViewHolder(inflater.inflate(R.layout.chat_bubble_media, parent, false));
            case VIEW_TYPE_VIDEO: return new VideoViewHolder(inflater.inflate(R.layout.chat_bubble_video, parent, false));
            case VIEW_TYPE_TYPING: return new TypingViewHolder(inflater.inflate(R.layout.chat_bubble_typing, parent, false));
            case VIEW_TYPE_LINK_PREVIEW: return new LinkPreviewViewHolder(inflater.inflate(R.layout.chat_bubble_link_preview, parent, false));
            case VIEW_TYPE_LOADING_MORE: return new LoadingViewHolder(inflater.inflate(R.layout.chat_bubble_loading_more, parent, false));
            default: return new TextViewHolder(inflater.inflate(R.layout.chat_bubble_text, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_TEXT: bindTextViewHolder((TextViewHolder) holder, position); break;
            case VIEW_TYPE_MEDIA_GRID: bindMediaViewHolder((MediaViewHolder) holder, position); break;
            case VIEW_TYPE_VIDEO: bindVideoViewHolder((VideoViewHolder) holder, position); break;
            case VIEW_TYPE_TYPING: bindTypingViewHolder((TypingViewHolder) holder, position); break;
            case VIEW_TYPE_LINK_PREVIEW: bindLinkPreviewViewHolder((LinkPreviewViewHolder) holder, position); break;
            case VIEW_TYPE_LOADING_MORE: bindLoadingViewHolder((LoadingViewHolder) holder, position); break;
        }
    }

    @Override
    public int getItemCount() { return _data.size(); }

    private void bindCommonMessageProperties(BaseMessageViewHolder holder, int position) {
        HashMap<String, Object> data = _data.get(position);
        String myUid = FirebaseAuth.getInstance().getCurrentUser() != null ? FirebaseAuth.getInstance().getCurrentUser().getUid() : "";
        String msgUid = data != null && data.get("uid") != null ? String.valueOf(data.get("uid")) : "";
        boolean isMyMessage = msgUid.equals(myUid);
        
        if (holder.message_layout != null) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.message_layout.getLayoutParams();
            int sideMarginPx = dpToPx(40);
            int topBottomPaddingPx = dpToPx(2);
            int innerPaddingPx = dpToPx(8);

            if (isMyMessage) {
                params.setMargins(sideMarginPx, topBottomPaddingPx, innerPaddingPx, topBottomPaddingPx);
                holder.body.setGravity(Gravity.TOP | Gravity.RIGHT);
                holder.message_layout.setGravity(Gravity.RIGHT);
            } else {
                params.setMargins(innerPaddingPx, topBottomPaddingPx, sideMarginPx, topBottomPaddingPx);
                holder.body.setGravity(Gravity.TOP | Gravity.LEFT);
                holder.message_layout.setGravity(Gravity.LEFT);
            }
            holder.message_layout.setLayoutParams(params);
        }
        holder.body.setGravity(isMyMessage ? (Gravity.TOP | Gravity.RIGHT) : (Gravity.TOP | Gravity.LEFT));
        
        if (holder.my_message_info != null && holder.date != null && holder.message_state != null) {
            boolean showMessageInfo = false;
            if (position == _data.size() - 1) {
                showMessageInfo = true;
            } else if (position + 1 < _data.size()) {
                HashMap<String, Object> currentMsg = _data.get(position);
                HashMap<String, Object> nextMsg = _data.get(position + 1);

                String nextUid = nextMsg != null && nextMsg.get("uid") != null ? String.valueOf(nextMsg.get("uid")) : "";
                String currUid = currentMsg != null && currentMsg.get("uid") != null ? String.valueOf(currentMsg.get("uid")) : "";
                boolean nextIsDifferentUser = !nextUid.equals(currUid);
                boolean nextIsTyping = nextMsg.containsKey("typingMessageStatus");

                boolean timeIsSignificant = false;
                if (currentMsg.containsKey("push_date") && nextMsg.containsKey("push_date") && currentMsg.get("push_date") != null && nextMsg.get("push_date") != null) {
                    try {
                        long currentTime = (long) Double.parseDouble(String.valueOf(currentMsg.get("push_date")));
                        long nextTime = (long) Double.parseDouble(String.valueOf(nextMsg.get("push_date")));
                        if ((nextTime - currentTime) > (5 * 60 * 1000)) { // 5 minutes
                            timeIsSignificant = true;
                        }
                    } catch (NumberFormatException e) {
                        // Handle case where push_date is not a valid double
                        timeIsSignificant = false;
                    }
                }

                if (nextIsDifferentUser || nextIsTyping || timeIsSignificant) {
                    showMessageInfo = true;
                }
            }
            holder.my_message_info.setVisibility(showMessageInfo ? View.VISIBLE : View.GONE);
            
            if (showMessageInfo) {
                Calendar push = Calendar.getInstance();
                try {
                    double pushVal = Double.parseDouble(String.valueOf(data.get("push_date")));
                    push.setTimeInMillis((long) pushVal);
                } catch (Exception e) {
                    push.setTimeInMillis(System.currentTimeMillis());
                }
                holder.date.setText(new SimpleDateFormat("hh:mm a").format(push.getTime()));
                
                holder.message_state.setVisibility(isMyMessage ? View.VISIBLE : View.GONE);
                if (isMyMessage) {
                    String state = data.get("message_state") != null ? String.valueOf(data.get("message_state")) : "";
                    holder.message_state.setImageResource("seen".equals(state) ? R.drawable.icon_done_all_round : R.drawable.icon_done_round);
                    holder.message_state.setColorFilter("seen".equals(state) ? _context.getResources().getColor(R.color.colorPrimary) : 0xFF424242, PorterDuff.Mode.SRC_ATOP);
                }
            }
        }

        if (holder.mProfileCard != null && holder.mProfileImage != null) {
            if (!isMyMessage) {
                String currUid = data != null && data.get("uid") != null ? String.valueOf(data.get("uid")) : "";
                String prevUid = (position - 1 >= 0 && _data.get(position - 1) != null && _data.get(position - 1).get("uid") != null) ? String.valueOf(_data.get(position - 1).get("uid")) : "";
                boolean isFirstOfGroup = (position == 0) || (position - 1 >= 0 && !prevUid.equals(currUid));
                holder.mProfileCard.setVisibility(isFirstOfGroup ? View.VISIBLE : View.GONE);
                if (isFirstOfGroup) {
                    if (secondUserAvatarUrl != null && !secondUserAvatarUrl.isEmpty() && !secondUserAvatarUrl.equals("null_banned")) {
                         Glide.with(_context).load(Uri.parse(secondUserAvatarUrl)).into(holder.mProfileImage);
                    } else if ("null_banned".equals(secondUserAvatarUrl)) {
                         holder.mProfileImage.setImageResource(R.drawable.banned_avatar);
                    } else {
                         holder.mProfileImage.setImageResource(R.drawable.avatar);
                    }
                }
            } else {
                holder.mProfileCard.setVisibility(View.GONE);
            }
        }
        
        if (holder.mRepliedMessageLayout != null) {
            holder.mRepliedMessageLayout.setVisibility(View.GONE);
            Log.d(TAG, "Checking for reply data at position " + position + " - Reply layout holder exists");
            
            if (data.containsKey("replied_message_id")) {
                String repliedId = data.get("replied_message_id").toString();
                Log.d(TAG, "Found replied_message_id: " + repliedId + " for position: " + position);
                
                if (repliedId != null && !repliedId.isEmpty() && !repliedId.equals("null")) {
                    Log.d(TAG, "Processing reply for message ID: " + repliedId);
                    
                    String theirUid = chatActivity.getIntent().getStringExtra("uid");
                    
                    Log.d(TAG, "Looking for replied message in chat: " + myUid + "/" + theirUid + " with ID: " + repliedId);
                    
                    // Use the same Firebase path structure as the main chat
                    FirebaseDatabase.getInstance().getReference("skyline/chats")
                        .child(myUid)
                        .child(theirUid)
                        .child(repliedId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                Log.d(TAG, "Firebase reply lookup result - Snapshot exists: " + (snapshot != null && snapshot.exists()) + ", Holder null: " + (holder.mRepliedMessageLayout == null));
                                
                                if (snapshot.exists() && holder.mRepliedMessageLayout != null) {
                                    Log.d(TAG, "Found replied message data, showing reply layout");
                                    holder.mRepliedMessageLayout.setVisibility(View.VISIBLE);
                                    
                                    // CRITICAL FIX: Set proper background for reply layout to ensure visibility
                                    if (isMyMessage) {
                                        // For my messages, use a semi-transparent white background
                                        holder.mRepliedMessageLayout.setCardBackgroundColor(Color.parseColor("#80FFFFFF"));
                                    } else {
                                        // For other's messages, use a semi-transparent light gray background
                                        holder.mRepliedMessageLayout.setCardBackgroundColor(Color.parseColor("#F5F5F5"));
                                    }
                                    
                                    String repliedUid = snapshot.child("uid").getValue(String.class);
                                    String repliedText = snapshot.child("message_text").getValue(String.class);
                                    
                                    Log.d(TAG, "Replied message - UID: " + repliedUid + ", Text: " + repliedText);
                                    
                                    // CRITICAL FIX: Handle image replies by checking for attachments
                                    if (holder.mRepliedMessageLayoutImage != null) {
                                        if (snapshot.hasChild("attachments")) {
                                            // This is an image/video message
                                            ArrayList<HashMap<String, Object>> attachments = (ArrayList<HashMap<String, Object>>) snapshot.child("attachments").getValue(new GenericTypeIndicator<ArrayList<HashMap<String, Object>>>() {});
                                            if (attachments != null && !attachments.isEmpty()) {
                                                // Show image preview
                                                holder.mRepliedMessageLayoutImage.setVisibility(View.VISIBLE);
                                                
                                                // Get the first attachment for preview
                                                HashMap<String, Object> firstAttachment = attachments.get(0);
                                                String publicId = firstAttachment.getOrDefault("publicId", "").toString();
                                                
                                                if (!publicId.isEmpty()) {
                                                    // Load image from Cloudinary
                                                    String imageUrl = "https://res.cloudinary.com/demo/image/upload/w_120,h_120,c_fill/" + publicId;
                                                    Glide.with(_context)
                                                        .load(imageUrl)
                                                        .placeholder(R.drawable.ph_imgbluredsqure)
                                                        .error(R.drawable.ph_imgbluredsqure)
                                                        .into(holder.mRepliedMessageLayoutImage);
                                                    
                                                    Log.d(TAG, "Loaded reply image preview: " + imageUrl);
                                                } else {
                                                    // Fallback to placeholder
                                                    holder.mRepliedMessageLayoutImage.setImageResource(R.drawable.ph_imgbluredsqure);
                                                }
                                            } else {
                                                // No attachments, hide image
                                                holder.mRepliedMessageLayoutImage.setVisibility(View.GONE);
                                            }
                                        } else {
                                            // No attachments, hide image
                                            holder.mRepliedMessageLayoutImage.setVisibility(View.GONE);
                                        }
                                    }
                                    
                                    if(holder.mRepliedMessageLayoutUsername != null) {
                                        String username = repliedUid != null && repliedUid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) ? firstUserName : secondUserName;
                                        holder.mRepliedMessageLayoutUsername.setText(username);
                                        Log.d(TAG, "Set reply username: " + username);
                                        
                                        // CRITICAL FIX: Use high-contrast colors for better visibility
                                        if (isMyMessage) {
                                            // For my messages (purple background), use white text
                                            holder.mRepliedMessageLayoutUsername.setTextColor(Color.WHITE);
                                        } else {
                                            // For other's messages (white background), use dark text
                                            holder.mRepliedMessageLayoutUsername.setTextColor(Color.parseColor("#1A1A1A"));
                                        }
                                    } else {
                                        Log.w(TAG, "Reply username TextView is null");
                                    }
                                    
                                    if(holder.mRepliedMessageLayoutMessage != null) {
                                        String messageText = repliedText != null ? repliedText : "";
                                        holder.mRepliedMessageLayoutMessage.setText(messageText);
                                        Log.d(TAG, "Set reply message: " + messageText);
                                        
                                        // CRITICAL FIX: Set message text color for better visibility
                                        if (isMyMessage) {
                                            holder.mRepliedMessageLayoutMessage.setTextColor(Color.parseColor("#E0E0E0"));
                                        } else {
                                            holder.mRepliedMessageLayoutMessage.setTextColor(Color.parseColor("#424242"));
                                        }
                                        
                                        // CRITICAL FIX: Show/hide message text based on content
                                        if (messageText.isEmpty()) {
                                            holder.mRepliedMessageLayoutMessage.setVisibility(View.GONE);
                                        } else {
                                            holder.mRepliedMessageLayoutMessage.setVisibility(View.VISIBLE);
                                        }
                                    } else {
                                        Log.w(TAG, "Reply message TextView is null");
                                    }
                                    
                                    if(holder.mRepliedMessageLayoutLeftBar != null) {
                                        android.graphics.drawable.GradientDrawable leftBarDrawable = new android.graphics.drawable.GradientDrawable();
                                        leftBarDrawable.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE);
                                        leftBarDrawable.setColor(_context.getResources().getColor(R.color.colorPrimary));
                                        leftBarDrawable.setCornerRadius(dpToPx(100));
                                        holder.mRepliedMessageLayoutLeftBar.setBackground(leftBarDrawable);
                                    } else {
                                        Log.w(TAG, "Reply left bar is null");
                                    }

                                    holder.mRepliedMessageLayout.setOnClickListener(v -> {
                                        if (chatActivity != null) {
                                            chatActivity.scrollToMessage(repliedId);
                                        }
                                    });
                                    
                                    Log.d(TAG, "Reply layout should now be visible with proper styling");
                                } else {
                                    Log.d(TAG, "Replied message not found or holder is null. Snapshot exists: " + (snapshot != null && snapshot.exists()) + ", Holder null: " + (holder.mRepliedMessageLayout == null));
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError error) {
                                Log.e(TAG, "Failed to load replied message: " + error.getMessage());
                            }
                        });
                } else {
                    Log.d(TAG, "Reply message ID is null or empty for position: " + position);
                }
            } else {
                Log.d(TAG, "No replied_message_id found for position: " + position + ". Available keys: " + data.keySet());
            }
        } else {
            Log.w(TAG, "Reply layout holder is null for position: " + position);
        }
        
        if (holder.messageBG != null) {
            int cornerRadius = 27;
            try { cornerRadius = (int) Double.parseDouble(appSettings.getString("ChatCornerRadius", "27")); } catch (Exception e) {}
            
            android.graphics.drawable.GradientDrawable bubbleDrawable = new android.graphics.drawable.GradientDrawable();
            float density = _context.getResources().getDisplayMetrics().density;
            bubbleDrawable.setCornerRadius(density * cornerRadius);

            if (isMyMessage) {
                bubbleDrawable.setColor(0xFF6B4CFF);
                if(holder.message_text != null) holder.message_text.setTextColor(Color.WHITE);
            } else {
                bubbleDrawable.setColor(Color.WHITE);
                bubbleDrawable.setStroke((int)(2 * density), 0xFFDFDFDF);
                if(holder.message_text != null) holder.message_text.setTextColor(Color.BLACK);
            }
            holder.messageBG.setBackground(bubbleDrawable);
        }

        int textSize = 16;
        try { textSize = (int) Double.parseDouble(appSettings.getString("ChatTextSize", "16")); } catch (Exception e) {}
        
        if(holder.message_text != null) holder.message_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        if (holder.mRepliedMessageLayoutUsername != null) holder.mRepliedMessageLayoutUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize - 2);
        if (holder.mRepliedMessageLayoutMessage != null) holder.mRepliedMessageLayoutMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

        if (!isMyMessage && data.containsKey("message_state") && "sended".equals(String.valueOf(data.get("message_state")))) {
            String otherUserUid = chatActivity.getIntent().getStringExtra("uid");
            
            String messageKey = String.valueOf(data.get("key"));
            FirebaseDatabase.getInstance().getReference("skyline/chats").child(otherUserUid).child(myUid).child(messageKey).child("message_state").setValue("seen");
            FirebaseDatabase.getInstance().getReference("skyline/chats").child(myUid).child(otherUserUid).child(messageKey).child("message_state").setValue("seen");
            FirebaseDatabase.getInstance().getReference("skyline/inbox").child(otherUserUid).child(myUid).child("last_message_state").setValue("seen");
        }
        
        // CRITICAL FIX: Set up long click listener on the entire message layout for better touch detection
        View.OnLongClickListener longClickListener = v -> {
            Log.d(TAG, "=== LONG CLICK DETECTED ===");
            Log.d(TAG, "Long click on view: " + v.getClass().getSimpleName() + " at position: " + position);
            Log.d(TAG, "Message data: " + data.toString());
            if (chatActivity != null) chatActivity.performHapticFeedbackLight();
            chatActivity._messageOverviewPopup(v, position, _data);
            return true;
        };
        
        // CRITICAL FIX: Enhanced touch event handling with multiple fallbacks
        View.OnTouchListener touchListener = (v, event) -> {
            // Log all touch events for debugging
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.d(TAG, "=== TOUCH DOWN DETECTED ===");
                Log.d(TAG, "Touch on view: " + v.getClass().getSimpleName() + " at position: " + position);
            }
            
            // Handle long press manually if needed
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.d(TAG, "=== TOUCH UP DETECTED ===");
            }
            
            return false; // Don't consume the event
        };
        
        // Set long click listener on multiple views to ensure it works
        if (holder.message_layout != null) {
            holder.message_layout.setOnLongClickListener(longClickListener);
            holder.message_layout.setOnTouchListener(touchListener);
            Log.d(TAG, "Set long click listener on message_layout for position: " + position);
        }
        if (holder.messageBG != null) {
            holder.messageBG.setOnLongClickListener(longClickListener);
            holder.messageBG.setOnTouchListener(touchListener);
            Log.d(TAG, "Set long click listener on messageBG for position: " + position);
        }
        if (holder.message_text != null) {
            holder.message_text.setOnLongClickListener(longClickListener);
            holder.message_text.setOnTouchListener(touchListener);
            Log.d(TAG, "Set long click listener on message_text for position: " + position);
        }
        if (holder.body != null) {
            holder.body.setOnLongClickListener(longClickListener);
            holder.body.setOnTouchListener(touchListener);
            Log.d(TAG, "Set long click listener on body for position: " + position);
        }
        
        // CRITICAL FIX: Also set on the entire item view as a fallback
        holder.itemView.setOnLongClickListener(longClickListener);
        holder.itemView.setOnTouchListener(touchListener);
        Log.d(TAG, "Set long click listener on itemView for position: " + position);
        
        // Keep only one definitive long click handler to avoid conflicts
    }

    private void bindTextViewHolder(TextViewHolder holder, int position) {
        bindCommonMessageProperties(holder, position);
        String text = String.valueOf(_data.get(position).getOrDefault("message_text", ""));
        holder.message_text.setVisibility(View.VISIBLE);
        textStylingUtil.applyStyling(text, holder.message_text);
    }

    private void bindMediaViewHolder(MediaViewHolder holder, int position) {
        Log.d(TAG, "bindMediaViewHolder called for position " + position);
        bindCommonMessageProperties(holder, position);
        HashMap<String, Object> data = _data.get(position);
        String msgText = data.getOrDefault("message_text", "").toString();
        holder.message_text.setVisibility(msgText.isEmpty() ? View.GONE : View.VISIBLE);
        if (!msgText.isEmpty()) {
            textStylingUtil.applyStyling(msgText, holder.message_text);
        }

        ArrayList<HashMap<String, Object>> attachments = (ArrayList<HashMap<String, Object>>) data.get("attachments");
        Log.d(TAG, "Attachments found: " + (attachments != null ? attachments.size() : 0));
        
        GridLayout gridLayout = holder.mediaGridLayout;
        if (gridLayout == null) {
            Log.e(TAG, "mediaGridLayout is null in MediaViewHolder");
            return;
        }

        gridLayout.removeAllViews();
        gridLayout.setVisibility(View.VISIBLE);

        if (attachments == null || attachments.isEmpty()) {
            Log.w(TAG, "No attachments found, hiding grid layout");
            gridLayout.setVisibility(View.GONE);
            return;
        }

        Log.d(TAG, "Processing " + attachments.size() + " attachments");
        int count = attachments.size();
        int colCount = 2;
        int maxImages = 4;
        int totalGridWidth = dpToPx(250);
        int imageSize = totalGridWidth / 2;

        ViewGroup.LayoutParams cardParams = holder.mediaContainerCard.getLayoutParams();
        cardParams.width = totalGridWidth;
        holder.mediaContainerCard.setLayoutParams(cardParams);

        gridLayout.setColumnCount(colCount);

        if (count == 1) {
            gridLayout.setColumnCount(1);
            HashMap<String, Object> attachment = attachments.get(0);
            ImageView iv = createImageView(attachment, totalGridWidth, true);
            gridLayout.addView(iv);

        } else if (count == 3) {
            int portraitIndex = -1;
            for(int i=0; i < attachments.size(); i++){
                HashMap<String, Object> attachment = attachments.get(i);
                Object widthObj = attachment.get("width");
                Object heightObj = attachment.get("height");

                if (widthObj != null && heightObj != null) {
                    double width = ((Number) widthObj).doubleValue();
                    double height = ((Number) heightObj).doubleValue();
                    if(height > width){
                        portraitIndex = i;
                        break;
                    }
                }
            }

            if(portraitIndex != -1){
                // Tall layout
                ImageView iv1 = createImageView(attachments.get(portraitIndex), imageSize, false);
                GridLayout.LayoutParams params1 = new GridLayout.LayoutParams(GridLayout.spec(0, 2, 1f), GridLayout.spec(0, 1, 1f));
                iv1.setLayoutParams(params1);
                gridLayout.addView(iv1);

                int attachmentIndex = 0;
                for(int i=0; i<2; i++){
                    if(attachmentIndex == portraitIndex) attachmentIndex++;
                    ImageView iv = createImageView(attachments.get(attachmentIndex), imageSize, false);
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(i, 1, 1f), GridLayout.spec(1, 1, 1f));
                    iv.setLayoutParams(params);
                    gridLayout.addView(iv);
                    attachmentIndex++;
                }

            } else {
                // Wide layout
                ImageView iv1 = createImageView(attachments.get(0), totalGridWidth, false);
                GridLayout.LayoutParams params1 = new GridLayout.LayoutParams(GridLayout.spec(0, 1, 1f), GridLayout.spec(0, 2, 1f));
                iv1.setLayoutParams(params1);
                gridLayout.addView(iv1);

                for (int i = 1; i < 3; i++) {
                    ImageView iv = createImageView(attachments.get(i), imageSize, false);
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(1, 1, 1f), GridLayout.spec(i - 1, 1, 1f));
                    iv.setLayoutParams(params);
                    gridLayout.addView(iv);
                }
            }
        } else { // 2, 4, or >4 images
            int limit = Math.min(count, maxImages);
            for (int i = 0; i < limit; i++) {
                View viewToAdd;
                ImageView iv = createImageView(attachments.get(i), imageSize, false);

                if (i == maxImages - 1 && count > maxImages) {
                    RelativeLayout overlayContainer = new RelativeLayout(_context);
                    overlayContainer.setLayoutParams(new ViewGroup.LayoutParams(imageSize, imageSize));
                    overlayContainer.addView(iv);

                    View overlay = new View(_context);
                    overlay.setBackgroundColor(0x40000000);
                    overlayContainer.addView(overlay, new ViewGroup.LayoutParams(imageSize, imageSize));

                    TextView moreText = new TextView(_context);
                    moreText.setText("+" + (count - maxImages));
                    moreText.setTextColor(Color.WHITE);
                    moreText.setTextSize(24);
                    moreText.setGravity(Gravity.CENTER);
                    overlayContainer.addView(moreText, new ViewGroup.LayoutParams(imageSize, imageSize));
                    viewToAdd = overlayContainer;
                    viewToAdd.setOnClickListener(v -> {
                        if (chatActivity != null) {
                             chatActivity._OpenWebView(attachments.get(3).get("url").toString());
                        }
                    });
                } else {
                    viewToAdd = iv;
                }
                gridLayout.addView(viewToAdd);
            }
        }
    }

    private ImageView createImageView(HashMap<String, Object> attachment, int width, boolean adjustBounds) {
        String url = String.valueOf(attachment.get("url"));
        ImageView imageView = new ImageView(_context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        if (adjustBounds) {
            imageView.setAdjustViewBounds(true);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            Glide.with(_context).load(url).into(imageView);
        } else {
            int height;
            Object widthObj = attachment.get("width");
            Object heightObj = attachment.get("height");

            if (widthObj != null && heightObj != null) {
                double imageWidth = ((Number) widthObj).doubleValue();
                double imageHeight = ((Number) heightObj).doubleValue();
                if (imageWidth > 0) {
                    double ratio = imageHeight / imageWidth;
                    height = (int) (width * ratio);
                } else {
                    height = width; // Fallback to square
                }
            } else {
                // Fallback for old messages without dimensions
                height = width;
            }

            imageView.setLayoutParams(new ViewGroup.LayoutParams(width, height));
            Glide.with(_context).load(url).override(width, height).into(imageView);
        }

        imageView.setOnClickListener(v -> {
            if (chatActivity != null) {
                chatActivity._OpenWebView(url);
            }
        });
        return imageView;
    }

    private void bindVideoViewHolder(VideoViewHolder holder, int position) {
        bindCommonMessageProperties(holder, position);
        HashMap<String, Object> data = _data.get(position);
        String msgText = data.getOrDefault("message_text", "").toString();
        holder.message_text.setVisibility(msgText.isEmpty() ? View.GONE : View.VISIBLE);
        if (!msgText.isEmpty()) textStylingUtil.applyStyling(msgText, holder.message_text);
        ArrayList<HashMap<String, Object>> attachments = (ArrayList<HashMap<String, Object>>) data.get("attachments");
        if (attachments != null && !attachments.isEmpty()) {
            String videoUrl = String.valueOf(attachments.get(0).get("url"));
            if(holder.videoThumbnail != null) Glide.with(_context).load(videoUrl).into(holder.videoThumbnail);
            
            // --- CRITICAL FIX: Attach click listener to videoContainerCard ---
            if(holder.videoContainerCard != null) {
                holder.videoContainerCard.setOnClickListener(v -> chatActivity._OpenWebView(videoUrl));
            } else {
                // Fallback if card isn't found (though it should be)
                holder.itemView.setOnClickListener(v -> chatActivity._OpenWebView(videoUrl));
            }
        }
    }

    private void bindTypingViewHolder(TypingViewHolder holder, int position) {
        android.graphics.drawable.GradientDrawable bubbleDrawable = new android.graphics.drawable.GradientDrawable();
        bubbleDrawable.setColor(Color.TRANSPARENT);
        if(holder.messageBG != null) holder.messageBG.setBackground(bubbleDrawable);

        if (holder.mProfileImage != null) {
            if (secondUserAvatarUrl != null && !secondUserAvatarUrl.isEmpty() && !secondUserAvatarUrl.equals("null_banned")) {
                Glide.with(_context).load(Uri.parse(secondUserAvatarUrl)).into(holder.mProfileImage);
            } else if ("null_banned".equals(secondUserAvatarUrl)) {
                holder.mProfileImage.setImageResource(R.drawable.banned_avatar);
            } else {
                holder.mProfileImage.setImageResource(R.drawable.avatar);
            }
        }
        if(holder.mProfileCard != null) holder.mProfileCard.setVisibility(View.VISIBLE);
    }
    
    private void bindLinkPreviewViewHolder(LinkPreviewViewHolder holder, int position) {
        bindCommonMessageProperties(holder, position);
        HashMap<String, Object> data = _data.get(position);
        String messageText = String.valueOf(data.getOrDefault("message_text", ""));
        holder.message_text.setVisibility(View.VISIBLE);
        textStylingUtil.applyStyling(messageText, holder.message_text);

        String urlToPreview = LinkPreviewUtil.extractUrl(messageText);
        if (urlToPreview != null) {
            // Check if link preview views exist before accessing them
            if (holder.linkPreviewImage != null) holder.linkPreviewImage.setVisibility(View.GONE);
            if (holder.linkPreviewTitle != null) holder.linkPreviewTitle.setText("Loading Preview...");
            if (holder.linkPreviewDescription != null) holder.linkPreviewDescription.setText("");
            if (holder.linkPreviewDomain != null) holder.linkPreviewDomain.setText(urlToPreview);

            LinkPreviewUtil.fetchPreview(urlToPreview, new LinkPreviewUtil.LinkPreviewCallback() {
                @Override
                public void onPreviewDataFetched(LinkPreviewUtil.LinkData linkData) {
                    if (linkData != null) { 
                        if (holder.linkPreviewTitle != null) holder.linkPreviewTitle.setText(linkData.title);
                        if (holder.linkPreviewDescription != null) holder.linkPreviewDescription.setText(linkData.description);
                        if (holder.linkPreviewDomain != null) holder.linkPreviewDomain.setText(linkData.domain);
                        if (linkData.imageUrl != null && !linkData.imageUrl.isEmpty() && holder.linkPreviewImage != null) {
                            if (chatActivity != null && !chatActivity.isDestroyed()) {
                                Glide.with(chatActivity).load(linkData.imageUrl).into(holder.linkPreviewImage);
                                holder.linkPreviewImage.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
                @Override
                public void onError(Exception e) {
                    Log.e(TAG, "Link preview error: " + e.getMessage());
                    if (holder.linkPreviewTitle != null) holder.linkPreviewTitle.setText("Cannot load preview");
                    if (holder.linkPreviewDescription != null) holder.linkPreviewDescription.setText("");
                    if (holder.linkPreviewDomain != null) holder.linkPreviewDomain.setText(urlToPreview);
                    if (holder.linkPreviewImage != null) holder.linkPreviewImage.setVisibility(View.GONE);
                }
            });
        }
    }
    
    private void bindLoadingViewHolder(LoadingViewHolder holder, int position) {
        // The progress bar is displayed, and the loading is handled by the
        // scroll listener in ChatActivity. No action needed here.
    }

    private int dpToPx(int dp) {
        return (int) (dp * _context.getResources().getDisplayMetrics().density);
    }
}