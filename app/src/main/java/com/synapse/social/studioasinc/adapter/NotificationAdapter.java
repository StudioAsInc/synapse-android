package com.synapse.social.studioasinc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.text.format.DateUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.synapse.social.studioasinc.ProfileActivity;
import com.synapse.social.studioasinc.R;
import com.synapse.social.studioasinc.model.Notification;
import com.synapse.social.studioasinc.model.User;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

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
        holder.timestamp.setText(getRelativeTime(notification.getTimestamp()));
        holder.unreadIndicator.setVisibility(notification.isRead() ? View.GONE : View.VISIBLE);

        setNotificationIcon(holder.notificationTypeIcon, notification.getType());
        loadUserAvatar(holder.userAvatar, notification.getFrom());

        holder.itemView.setOnClickListener(v -> {
            // Handle click, e.g., open post or profile
            // This is a placeholder, as the target activities might not exist.
            if ("follow".equals(notification.getType())) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("uid", notification.getFrom());
                context.startActivity(intent);
            } else {
                // Assuming other notifications lead to a post
                // Intent intent = new Intent(context, PostActivity.class);
                // intent.putExtra("postId", notification.getPostId());
                // context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    private void setNotificationIcon(ImageView imageView, String type) {
        if (type == null) return;
        switch (type) {
            case "like":
                imageView.setImageResource(R.drawable.ic_favorite_48px);
                break;
            case "comment":
                imageView.setImageResource(R.drawable.ic_comment_48px);
                break;
            case "follow":
                imageView.setImageResource(R.drawable.ic_person_add_48px);
                break;
            default:
                // A default icon
                imageView.setImageResource(R.drawable.ic_notifications_48px);
                break;
        }
    }

    private void loadUserAvatar(CircleImageView imageView, String userId) {
        FirebaseDatabase.getInstance().getReference("users").child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        if (user != null && user.getProfileImageUrl() != null && !user.getProfileImageUrl().isEmpty()) {
                            Glide.with(context).load(user.getProfileImageUrl()).into(imageView);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private String getRelativeTime(long time) {
        return DateUtils.getRelativeTimeSpanString(time, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS).toString();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView userAvatar;
        public ImageView notificationTypeIcon;
        public TextView notificationMessage;
        public TextView timestamp;
        public View unreadIndicator;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userAvatar = itemView.findViewById(R.id.user_avatar);
            notificationTypeIcon = itemView.findViewById(R.id.notification_type_icon);
            notificationMessage = itemView.findViewById(R.id.notification_message);
            timestamp = itemView.findViewById(R.id.timestamp);
            unreadIndicator = itemView.findViewById(R.id.unread_indicator);
        }
    }
}
