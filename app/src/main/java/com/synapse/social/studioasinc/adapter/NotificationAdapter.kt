package com.synapse.social.studioasinc.adapter

import android.content.Context
import android.content.Intent
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.synapse.social.studioasinc.ProfileActivity
import com.synapse.social.studioasinc.R
import com.synapse.social.studioasinc.model.Notification
import com.synapse.social.studioasinc.model.User
import de.hdodenhof.circleimageview.CircleImageView

class NotificationAdapter(
    private val context: Context,
    private val notificationList: List<Notification>
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notification = notificationList[position]

        holder.notificationMessage.text = notification.message
        holder.timestamp.text = getRelativeTime(notification.timestamp)
        holder.unreadIndicator.visibility = if (notification.isRead) View.GONE else View.VISIBLE

        setNotificationIcon(holder.notificationTypeIcon, notification.type)
        loadUserAvatar(holder.userAvatar, notification.from)

        holder.itemView.setOnClickListener {
            if ("follow" == notification.type) {
                val intent = Intent(context, ProfileActivity::class.java).apply {
                    putExtra("uid", notification.from)
                }
                context.startActivity(intent)
            } else {
                // Assuming other notifications lead to a post
                // val intent = Intent(context, PostActivity::class.java).apply {
                //     putExtra("postId", notification.postId)
                // }
                // context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = notificationList.size

    private fun setNotificationIcon(imageView: ImageView, type: String?) {
        val iconRes = when (type) {
            "like" -> R.drawable.ic_favorite_48px
            "comment" -> R.drawable.ic_comment_48px
            "follow" -> R.drawable.ic_person_add_48px
            else -> R.drawable.ic_notifications_48px // A default icon
        }
        imageView.setImageResource(iconRes)
    }

    private fun loadUserAvatar(imageView: CircleImageView, userId: String) {
        FirebaseDatabase.getInstance().getReference("users").child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    if (user?.profileImageUrl?.isNotEmpty() == true) {
                        Glide.with(context).load(user.profileImageUrl).into(imageView)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
    }

    private fun getRelativeTime(time: Long): String {
        return DateUtils.getRelativeTimeSpanString(
            time,
            System.currentTimeMillis(),
            DateUtils.MINUTE_IN_MILLIS
        ).toString()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userAvatar: CircleImageView = itemView.findViewById(R.id.user_avatar)
        val notificationTypeIcon: ImageView = itemView.findViewById(R.id.notification_type_icon)
        val notificationMessage: TextView = itemView.findViewById(R.id.notification_message)
        val timestamp: TextView = itemView.findViewById(R.id.timestamp)
        val unreadIndicator: View = itemView.findViewById(R.id.unread_indicator)
    }
}
