package com.synapse.social.studioasinc.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.synapse.social.studioasinc.ChatActivity;
import com.synapse.social.studioasinc.HomeActivity;
import com.synapse.social.studioasinc.NotificationConfig;
import com.synapse.social.studioasinc.ProfileActivity;
import com.synapse.social.studioasinc.R;
import com.synapse.social.studioasinc.model.Notification;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context context;
    private List<Notification> notificationList;

    public NotificationAdapter(Context context, List<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = notificationList.get(position);
        holder.notificationMessage.setText(notification.getMessage());
        
        // Handle notification item clicks for in-app navigation
        holder.itemView.setOnClickListener(v -> {
            handleNotificationClick(notification);
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
    
    /**
     * Handles in-app notification clicks by navigating to appropriate activities.
     * This provides the same deep linking functionality as OneSignal notifications.
     */
    private void handleNotificationClick(Notification notification) {
        try {
            Log.d("NotificationAdapter", "Handling notification click for type: " + notification.getType());
            
            String notificationType = notification.getType();
            if (notificationType == null) {
                Log.w("NotificationAdapter", "Notification type is null, opening HomeActivity");
                openHomeActivity();
                return;
            }
            
            switch (notificationType) {
                case NotificationConfig.NOTIFICATION_TYPE_CHAT_MESSAGE:
                    handleChatMessage(notification);
                    break;
                case NotificationConfig.NOTIFICATION_TYPE_NEW_FOLLOWER:
                    handleNewFollower(notification);
                    break;
                case NotificationConfig.NOTIFICATION_TYPE_PROFILE_LIKE:
                    handleProfileLike(notification);
                    break;
                default:
                    // Handle post-related notifications (likes, comments, replies, new posts, mentions)
                    if (notificationType.equals(NotificationConfig.NOTIFICATION_TYPE_NEW_LIKE_POST) ||
                        notificationType.equals(NotificationConfig.NOTIFICATION_TYPE_NEW_COMMENT) ||
                        notificationType.equals(NotificationConfig.NOTIFICATION_TYPE_NEW_REPLY) ||
                        notificationType.equals(NotificationConfig.NOTIFICATION_TYPE_NEW_POST) ||
                        notificationType.equals(NotificationConfig.NOTIFICATION_TYPE_MENTION_POST)) {
                        handlePostNotification(notification);
                    } else {
                        Log.w("NotificationAdapter", "Unknown notification type: " + notificationType + ", opening HomeActivity");
                        openHomeActivity();
                    }
                    break;
            }
            
        } catch (Exception e) {
            Log.e("NotificationAdapter", "Error handling notification click", e);
            openHomeActivity();
        }
    }
    
    private void handleChatMessage(Notification notification) {
        try {
            String senderUid = notification.getFrom();
            if (senderUid == null || senderUid.isEmpty()) {
                Log.e("NotificationAdapter", "Chat notification missing sender UID");
                openHomeActivity();
                return;
            }
            
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("uid", senderUid);
            intent.putExtra("origin", "NotificationClick");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
            
        } catch (Exception e) {
            Log.e("NotificationAdapter", "Error handling chat message notification", e);
            openHomeActivity();
        }
    }
    
    private void handleNewFollower(Notification notification) {
        try {
            String senderUid = notification.getFrom();
            if (senderUid == null || senderUid.isEmpty()) {
                Log.e("NotificationAdapter", "New follower notification missing sender UID");
                openHomeActivity();
                return;
            }
            
            Intent intent = new Intent(context, ProfileActivity.class);
            intent.putExtra("uid", senderUid);
            intent.putExtra("origin", "NotificationClick");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
            
        } catch (Exception e) {
            Log.e("NotificationAdapter", "Error handling new follower notification", e);
            openHomeActivity();
        }
    }
    
    private void handleProfileLike(Notification notification) {
        try {
            String senderUid = notification.getFrom();
            if (senderUid == null || senderUid.isEmpty()) {
                Log.e("NotificationAdapter", "Profile like notification missing sender UID");
                openHomeActivity();
                return;
            }
            
            Intent intent = new Intent(context, ProfileActivity.class);
            intent.putExtra("uid", senderUid);
            intent.putExtra("origin", "NotificationClick");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
            
        } catch (Exception e) {
            Log.e("NotificationAdapter", "Error handling profile like notification", e);
            openHomeActivity();
        }
    }
    
    private void handlePostNotification(Notification notification) {
        try {
            String postId = notification.getPostId();
            String senderUid = notification.getFrom();
            
            Intent intent = new Intent(context, HomeActivity.class);
            if (postId != null && !postId.isEmpty()) {
                intent.putExtra("postId", postId);
            }
            if (senderUid != null && !senderUid.isEmpty()) {
                intent.putExtra("senderUid", senderUid);
            }
            intent.putExtra("origin", "NotificationClick");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
            
        } catch (Exception e) {
            Log.e("NotificationAdapter", "Error handling post notification", e);
            openHomeActivity();
        }
    }
    
    private void openHomeActivity() {
        try {
            Intent intent = new Intent(context, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e("NotificationAdapter", "Failed to open HomeActivity as fallback", e);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView notificationMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationMessage = itemView.findViewById(R.id.notification_message);
        }
    }
}
