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
import android.widget.Toast;
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
        if ("ATTACHMENT_MESSAGE".equals(type)) {
            ArrayList<HashMap<String, Object>> attachments = (ArrayList<HashMap<String, Object>>) _data.get(position).get("attachments");
            if (attachments != null && attachments.size() == 1 && attachments.get(0).getOrDefault("publicId", "").toString().contains("|video")) {
                return VIEW_TYPE_VIDEO;
            }
            return VIEW_TYPE_MEDIA_GRID;
        }

        String messageText = _data.get(position).getOrDefault("message_text", "").toString();
        if (LinkPreviewUtil.extractUrl(messageText) != null) {
            return VIEW_TYPE_LINK_PREVIEW;
        }
        
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
        boolean isMyMessage = data.get("uid").toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid());
        
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

                boolean nextIsDifferentUser = !nextMsg.get("uid").toString().equals(currentMsg.get("uid").toString());
                boolean nextIsTyping = nextMsg.containsKey("typingMessageStatus");

                boolean timeIsSignificant = false;
                if (currentMsg.containsKey("push_date") && nextMsg.containsKey("push_date")) {
                    try {
                        long currentTime = (long) Double.parseDouble(currentMsg.get("push_date").toString());
                        long nextTime = (long) Double.parseDouble(nextMsg.get("push_date").toString());
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
                push.setTimeInMillis((long) Double.parseDouble(data.get("push_date").toString()));
                holder.date.setText(new SimpleDateFormat("hh:mm a").format(push.getTime()));
                
                holder.message_state.setVisibility(isMyMessage ? View.VISIBLE : View.GONE);
                if (isMyMessage) {
                    String state = data.get("message_state").toString();
                    holder.message_state.setImageResource("seen".equals(state) ? R.drawable.icon_done_all_round : R.drawable.icon_done_round);
                    holder.message_state.setColorFilter("seen".equals(state) ? _context.getResources().getColor(R.color.colorPrimary) : 0xFF424242, PorterDuff.Mode.SRC_ATOP);
                }
            }
        }

        if (holder.mProfileCard != null && holder.mProfileImage != null) {
            if (!isMyMessage) {
                boolean isFirstOfGroup = (position == 0) || (position - 1 >= 0 && !_data.get(position - 1).get("uid").toString().equals(data.get("uid").toString()));
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
            final String repliedMessageId = (String) data.get("replied_message_id");
            final String repliedMessageText = (String) data.get("replied_message_text");
            final String repliedMessageSenderUid = (String) data.get("replied_message_sender_uid");

            if (repliedMessageId != null && !repliedMessageId.isEmpty() && !repliedMessageId.equals("null") &&
                repliedMessageText != null && repliedMessageSenderUid != null) {

                holder.mRepliedMessageLayout.setVisibility(View.VISIBLE);

                if (holder.mRepliedMessageLayoutUsername != null) {
                    holder.mRepliedMessageLayoutUsername.setText(
                        repliedMessageSenderUid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) ? firstUserName : secondUserName
                    );
                    if (isMyMessage) {
                        holder.mRepliedMessageLayoutUsername.setTextColor(Color.WHITE);
                    } else {
                        holder.mRepliedMessageLayoutUsername.setTextColor(_context.getResources().getColor(R.color.colorPrimary));
                    }
                }

                if (holder.mRepliedMessageLayoutMessage != null) {
                    holder.mRepliedMessageLayoutMessage.setText(repliedMessageText);
                }

                if (holder.mRepliedMessageLayoutLeftBar != null) {
                    android.graphics.drawable.GradientDrawable leftBarDrawable = new android.graphics.drawable.GradientDrawable();
                    leftBarDrawable.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE);
                    leftBarDrawable.setColor(_context.getResources().getColor(R.color.colorPrimary));
                    leftBarDrawable.setCornerRadius(dpToPx(100));
                    holder.mRepliedMessageLayoutLeftBar.setBackground(leftBarDrawable);
                }

                holder.mRepliedMessageLayout.setOnClickListener(v -> {
                    if (chatActivity != null) {
                        chatActivity.scrollToMessage(repliedMessageId);
                    }
                });

            } else {
                holder.mRepliedMessageLayout.setVisibility(View.GONE);
            }
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

        if (!isMyMessage && data.containsKey("message_state") && "sended".equals(data.get("message_state").toString())) {
            String otherUserUid = chatActivity.getIntent().getStringExtra("uid");
            String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String messageKey = data.get("key") != null ? data.get("key").toString() : null;
            
            if (otherUserUid != null && myUid != null && messageKey != null) {
                FirebaseDatabase.getInstance().getReference("skyline/chats").child(otherUserUid).child(myUid).child(messageKey).child("message_state").setValue("seen");
                FirebaseDatabase.getInstance().getReference("skyline/chats").child(myUid).child(otherUserUid).child(messageKey).child("message_state").setValue("seen");
                FirebaseDatabase.getInstance().getReference("skyline/inbox").child(otherUserUid).child(myUid).child("last_message_state").setValue("seen");
            }
        }
        
        if (holder.messageBG != null) {
            View.OnLongClickListener longClickListener = v -> {
                chatActivity._messageOverviewPopup(v, position, _data);
                return true;
            };
            holder.messageBG.setOnLongClickListener(longClickListener);
            if(holder.message_text != null) {
                holder.message_text.setOnLongClickListener(longClickListener);
            }
        }
    }

    private void bindTextViewHolder(TextViewHolder holder, int position) {
        bindCommonMessageProperties(holder, position);
        String text = _data.get(position).get("message_text").toString();
        holder.message_text.setVisibility(View.VISIBLE);
        textStylingUtil.applyStyling(text, holder.message_text);
    }

    private void bindMediaViewHolder(MediaViewHolder holder, int position) {
        bindCommonMessageProperties(holder, position);
        HashMap<String, Object> data = _data.get(position);
        String msgText = data.getOrDefault("message_text", "").toString();
        holder.message_text.setVisibility(msgText.isEmpty() ? View.GONE : View.VISIBLE);
        if (!msgText.isEmpty()) {
            textStylingUtil.applyStyling(msgText, holder.message_text);
        }

        ArrayList<HashMap<String, Object>> attachments = (ArrayList<HashMap<String, Object>>) data.get("attachments");
        GridLayout gridLayout = holder.mediaGridLayout;
        if (gridLayout == null) return;

        gridLayout.removeAllViews();
        gridLayout.setVisibility(View.VISIBLE);

        if (attachments == null || attachments.isEmpty()) {
            gridLayout.setVisibility(View.GONE);
            return;
        }

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
        String url = attachment.get("url").toString();
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
            String videoUrl = attachments.get(0).get("url").toString();
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
        // Create a subtle typing bubble background
        android.graphics.drawable.GradientDrawable bubbleDrawable = new android.graphics.drawable.GradientDrawable();
        bubbleDrawable.setColor(0xFFF5F5F5); // Light gray background
        bubbleDrawable.setCornerRadius(dpToPx(20)); // Rounded corners
        if(holder.messageBG != null) holder.messageBG.setBackground(bubbleDrawable);

        // Set profile image for the typing user
        if (holder.mProfileImage != null) {
            if (secondUserAvatarUrl != null && !secondUserAvatarUrl.isEmpty() && !secondUserAvatarUrl.equals("null_banned")) {
                Glide.with(_context).load(Uri.parse(secondUserAvatarUrl)).into(holder.mProfileImage);
            } else if ("null_banned".equals(secondUserAvatarUrl)) {
                holder.mProfileImage.setImageResource(R.drawable.banned_avatar);
            } else {
                holder.mProfileImage.setImageResource(R.drawable.avatar);
            }
        }
        
        // Show profile card for typing indicator
        if(holder.mProfileCard != null) holder.mProfileCard.setVisibility(View.VISIBLE);
        
        // Start the typing animation
        if (holder.lottie_typing != null) {
            holder.lottie_typing.setVisibility(View.VISIBLE);
            holder.lottie_typing.playAnimation();
        }
        
        // Set the typing indicator to show it's from the other user
        // The typing indicator will appear on the left side like other user messages
        if(holder.body != null) {
            holder.body.setGravity(Gravity.START);
        }
    }
    
    private void bindLinkPreviewViewHolder(LinkPreviewViewHolder holder, int position) {
        bindCommonMessageProperties(holder, position);
        HashMap<String, Object> data = _data.get(position);
        String messageText = data.get("message_text").toString();
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