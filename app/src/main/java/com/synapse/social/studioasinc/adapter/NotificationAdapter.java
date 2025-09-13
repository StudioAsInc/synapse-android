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
            
            // Create notification data map
            java.util.Map<String, String> notificationData = new java.util.HashMap<>();
            if (notification.getFrom() != null) {
                notificationData.put("sender_uid", notification.getFrom());
            }
            if (notification.getPostId() != null) {
                notificationData.put("postId", notification.getPostId());
            }
            if (notification.getCommentId() != null) {
                notificationData.put("commentId", notification.getCommentId());
            }
            
            // Use the centralized notification click handler
            com.synapse.social.studioasinc.NotificationClickHandler.handleNotificationClick(
                context, 
                notificationType, 
                notificationData
            );
            
        } catch (Exception e) {
            Log.e("NotificationAdapter", "Error handling notification click", e);
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
