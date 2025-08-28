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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
// import com.synapse.social.studioasinc.styling.TextStylingUtil;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.stream.Collectors;


public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "ChatAdapter";
    private static final int VIEW_TYPE_TEXT = 1;
    private static final int VIEW_TYPE_MEDIA_GRID = 2;
    private static final int VIEW_TYPE_TYPING = 3;
    private static final int VIEW_TYPE_VIDEO = 4;
    private static final int VIEW_TYPE_LINK_PREVIEW = 5;
    private static final int VIEW_TYPE_LOADING_MORE = 99;

    private ArrayList<HashMap<String, Object>> _data;
    private HashMap<String, HashMap<String, Object>> repliedMessagesCache;
    private Context _context;
    private String secondUserAvatarUrl = "";
    private String firstUserName = "";
    private String secondUserName = "";
    private SharedPreferences appSettings;
    // private TextStylingUtil textStylingUtil;
    private ChatActivity chatActivity;

    public ChatAdapter(ArrayList<HashMap<String, Object>> _arr, HashMap<String, HashMap<String, Object>> repliedCache) {
        _data = _arr;
        this.repliedMessagesCache = repliedCache;
    }
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
    public long getItemId(int position) {
        try {
            // CRITICAL FIX: Use the correct key constant that matches ChatActivity
            Object keyObj = _data.get(position).getOrDefault("key", _data.get(position).getOrDefault("KEY_KEY", position));
            return String.valueOf(keyObj).hashCode();
        } catch (Exception e) {
            return position;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        _context = parent.getContext();
        appSettings = _context.getSharedPreferences("appSettings", Context.MODE_PRIVATE);
        // textStylingUtil = new TextStylingUtil(_context);
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
                // CRITICAL FIX: Smart timestamp visibility logic
                boolean shouldShowTime = _shouldShowTimestamp(position, data);
                holder.date.setVisibility(shouldShowTime ? View.VISIBLE : View.GONE);
                if (shouldShowTime) {
                    holder.date.setText(new SimpleDateFormat("hh:mm a").format(push.getTime()));
                }
                
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
                    
                    if (repliedMessagesCache != null && repliedMessagesCache.containsKey(repliedId)) {
                        HashMap<String, Object> snapshot = repliedMessagesCache.get(repliedId);
                        if (snapshot != null && !snapshot.isEmpty()) {
                            holder.mRepliedMessageLayout.setVisibility(View.VISIBLE);

                            if (isMyMessage) {
                                holder.mRepliedMessageLayout.setCardBackgroundColor(Color.parseColor("#80FFFFFF"));
                            } else {
                                holder.mRepliedMessageLayout.setCardBackgroundColor(Color.parseColor("#F5F5F5"));
                            }

                            String repliedUid = snapshot.get("uid") instanceof String ? (String) snapshot.get("uid") : null;
                            String repliedText = (String) snapshot.get("message_text");

                            if (holder.mRepliedMessageLayoutImage != null) {
                                if (snapshot.containsKey("attachments")) {
                                    ArrayList<HashMap<String, Object>> attachments = (ArrayList<HashMap<String, Object>>) snapshot.get("attachments");
                                    if (attachments != null && !attachments.isEmpty()) {
                                        holder.mRepliedMessageLayoutImage.setVisibility(View.VISIBLE);
                                        String publicId = (String) attachments.get(0).get("publicId");
                                        if (publicId != null && !publicId.isEmpty() && _context != null) {
                                            String imageUrl = "https://res.cloudinary.com/demo/image/upload/w_120,h_120,c_fill/" + publicId;
                                            Glide.with(_context).load(imageUrl).placeholder(R.drawable.ph_imgbluredsqure).error(R.drawable.ph_imgbluredsqure).transform(new RoundedCorners(dpToPx(20))).into(holder.mRepliedMessageLayoutImage);
                                        } else {
                                            holder.mRepliedMessageLayoutImage.setImageResource(R.drawable.ph_imgbluredsqure);
                                        }
                                    } else {
                                        holder.mRepliedMessageLayoutImage.setVisibility(View.GONE);
                                    }
                                } else {
                                    holder.mRepliedMessageLayoutImage.setVisibility(View.GONE);
                                }
                            }

                            if (holder.mRepliedMessageLayoutUsername != null) {
                                String username = repliedUid != null && repliedUid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) ? firstUserName : secondUserName;
                                holder.mRepliedMessageLayoutUsername.setText(username);
                                holder.mRepliedMessageLayoutUsername.setTextColor(isMyMessage ? Color.WHITE : Color.parseColor("#1A1A1A"));
                            }

                            if (holder.mRepliedMessageLayoutMessage != null) {
                                String messageText = repliedText != null ? repliedText : "";
                                holder.mRepliedMessageLayoutMessage.setText(messageText);
                                holder.mRepliedMessageLayoutMessage.setTextColor(isMyMessage ? Color.parseColor("#E0E0E0") : Color.parseColor("#424242"));
                                holder.mRepliedMessageLayoutMessage.setVisibility(messageText.isEmpty() ? View.GONE : View.VISIBLE);
                            }

                            if (holder.mRepliedMessageLayoutLeftBar != null) {
                                android.graphics.drawable.GradientDrawable leftBarDrawable = new android.graphics.drawable.GradientDrawable();
                                leftBarDrawable.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE);
                                leftBarDrawable.setColor(_context.getResources().getColor(R.color.colorPrimary));
                                leftBarDrawable.setCornerRadius(dpToPx(100));
                                holder.mRepliedMessageLayoutLeftBar.setBackground(leftBarDrawable);
                            }

                            View.OnClickListener clickListener = v -> {
                                if (chatActivity != null) {
                                    chatActivity.scrollToMessage(repliedId);
                                }
                            };
                            holder.mRepliedMessageLayout.setOnClickListener(clickListener);
                            if (holder.mRepliedMessageLayoutMessage != null) holder.mRepliedMessageLayoutMessage.setOnClickListener(clickListener);
                            if (holder.mRepliedMessageLayoutImage != null) holder.mRepliedMessageLayoutImage.setOnClickListener(clickListener);
                        } else {
                            holder.mRepliedMessageLayout.setVisibility(View.GONE);
                        }
                    } else {
                        holder.mRepliedMessageLayout.setVisibility(View.GONE);
                    }
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

            // Rounded ripple foreground to match bubble corners
            int rippleColor = isMyMessage ? 0x33FFFFFF : 0x22000000;
            android.graphics.drawable.GradientDrawable rippleMask = new android.graphics.drawable.GradientDrawable();
            rippleMask.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE);
            rippleMask.setCornerRadius(density * cornerRadius);
            rippleMask.setColor(Color.WHITE);
            android.content.res.ColorStateList rippleColors = new android.content.res.ColorStateList(new int[][]{ new int[]{} }, new int[]{ rippleColor });
            android.graphics.drawable.RippleDrawable ripple = new android.graphics.drawable.RippleDrawable(rippleColors, null, rippleMask);
            holder.messageBG.setForeground(ripple);
            holder.messageBG.setClickable(true);
            holder.messageBG.setLongClickable(true);
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
        
        // Consolidated long click listener for the message context menu.
        View.OnLongClickListener longClickListener = v -> {
            Log.d(TAG, "Long click detected on view: " + v.getClass().getSimpleName() + " at position: " + position);
            if (chatActivity != null) {
                chatActivity.performHapticFeedbackLight();
            }
            // Use the message bubble (messageBG) as the anchor for the popup if it exists.
            View anchor = holder.messageBG != null ? holder.messageBG : holder.itemView;
            chatActivity._messageOverviewPopup(anchor, position, _data);
            return true;
        };

        // Set the listener on the message bubble itself.
        if (holder.messageBG != null) {
            holder.messageBG.setOnLongClickListener(longClickListener);
        }

        // Also set it on the TextView as a fallback, as it may consume touch events
        // due to the Markdown/link handling.
        if (holder.message_text != null) {
            holder.message_text.setOnLongClickListener(longClickListener);
        }
        
        // Keep only one definitive long click handler to avoid conflicts
    }

    private void bindTextViewHolder(TextViewHolder holder, int position) {
        bindCommonMessageProperties(holder, position);
        String text = String.valueOf(_data.get(position).getOrDefault("message_text", ""));
        holder.message_text.setVisibility(View.VISIBLE);
        com.synapse.social.studioasinc.styling.MarkdownRenderer.get(holder.message_text.getContext()).render(holder.message_text, text);
    }

    private void bindMediaViewHolder(MediaViewHolder holder, int position) {
        Log.d(TAG, "bindMediaViewHolder called for position " + position);
        bindCommonMessageProperties(holder, position);
        HashMap<String, Object> data = _data.get(position);
        String msgText = data.getOrDefault("message_text", "").toString();
        holder.message_text.setVisibility(msgText.isEmpty() ? View.GONE : View.VISIBLE);
        if (!msgText.isEmpty()) {
            com.synapse.social.studioasinc.styling.MarkdownRenderer.get(holder.message_text.getContext()).render(holder.message_text, msgText);
        }

        ArrayList<HashMap<String, Object>> attachments = (ArrayList<HashMap<String, Object>>) data.get("attachments");
        if (attachments == null || attachments.isEmpty()) {
            holder.mediaCarouselRecyclerView.setVisibility(View.GONE);
            holder.viewAllButton.setVisibility(View.GONE);
            return;
        }

        holder.mediaCarouselRecyclerView.setVisibility(View.VISIBLE);

        // Set up the carousel RecyclerView
        MediaCarouselAdapter carouselAdapter = new MediaCarouselAdapter(_context, attachments);
        holder.mediaCarouselRecyclerView.setLayoutManager(new LinearLayoutManager(_context, LinearLayoutManager.HORIZONTAL, false));
        holder.mediaCarouselRecyclerView.setAdapter(carouselAdapter);

        // Set up the "View All" button
        if (attachments.size() > 4) {
            holder.viewAllButton.setVisibility(View.VISIBLE);
            holder.viewAllButton.setText("View all " + attachments.size() + " images");
            holder.viewAllButton.setOnClickListener(v -> {
                Intent intent = new Intent(_context, GalleryActivity.class);
                ArrayList<String> imageUrls = attachments.stream()
                                                     .map(p -> (String) p.get("url"))
                                                     .collect(Collectors.toCollection(ArrayList::new));
                intent.putStringArrayListExtra(GalleryActivity.EXTRA_IMAGE_URLS, imageUrls);
                intent.putExtra(GalleryActivity.EXTRA_INITIAL_POSITION, 0);
                _context.startActivity(intent);
            });
        } else {
            holder.viewAllButton.setVisibility(View.GONE);
        }
    }

    private void bindVideoViewHolder(VideoViewHolder holder, int position) {
        bindCommonMessageProperties(holder, position);
        HashMap<String, Object> data = _data.get(position);
        String msgText = data.getOrDefault("message_text", "").toString();
        holder.message_text.setVisibility(msgText.isEmpty() ? View.GONE : View.VISIBLE);
        if (!msgText.isEmpty()) com.synapse.social.studioasinc.styling.MarkdownRenderer.get(holder.message_text.getContext()).render(holder.message_text, msgText);
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
        com.synapse.social.studioasinc.styling.MarkdownRenderer.get(holder.message_text.getContext()).render(holder.message_text, messageText);

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
        // CRITICAL FIX: Safe dp to px conversion with null checks
        try {
            if (_context != null && _context.getResources() != null && _context.getResources().getDisplayMetrics() != null) {
                return (int) (dp * _context.getResources().getDisplayMetrics().density);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error converting dp to px: " + e.getMessage());
        }
        return dp; // Fallback to dp value
    }
    
    // CRITICAL FIX: Smart timestamp visibility logic
    private boolean _shouldShowTimestamp(int position, HashMap<String, Object> currentMessage) {
        // Always show timestamp for the last message
        if (position == _data.size() - 1) {
            return true;
        }
        
        // Show timestamp if there's a significant time gap (more than 5 minutes)
        if (position < _data.size() - 1) {
            try {
                HashMap<String, Object> nextMessage = _data.get(position + 1);
                long currentTime = _getMessageTimestamp(currentMessage);
                long nextTime = _getMessageTimestamp(nextMessage);
                
                // Show timestamp if gap is more than 5 minutes (300000 ms)
                return Math.abs(currentTime - nextTime) > 300000;
            } catch (Exception e) {
                Log.e(TAG, "Error calculating timestamp visibility: " + e.getMessage());
                return false;
            }
        }
        
        return false;
    }
    
    // Helper method to get message timestamp
    private long _getMessageTimestamp(HashMap<String, Object> message) {
        try {
            Object pushDateObj = message.get("push_date");
			if (pushDateObj instanceof Long) {
				return (Long) pushDateObj;
			} else if (pushDateObj instanceof Double) {
				return ((Double) pushDateObj).longValue();
			} else if (pushDateObj instanceof String) {
				return Long.parseLong((String) pushDateObj);
			}
        } catch (Exception e) {
            Log.w(TAG, "Error parsing message timestamp: " + e.getMessage());
        }
        return System.currentTimeMillis();
    }
}