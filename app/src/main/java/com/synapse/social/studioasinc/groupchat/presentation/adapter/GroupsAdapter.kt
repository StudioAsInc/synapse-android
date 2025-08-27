package com.synapse.social.studioasinc.groupchat.presentation.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.synapse.social.studioasinc.R
import com.synapse.social.studioasinc.databinding.ItemGroupBinding
import com.synapse.social.studioasinc.groupchat.data.model.Group

class GroupsAdapter(
    private val onGroupClick: (Group) -> Unit,
    private val onGroupLongClick: (Group) -> Unit = {}
) : PagingDataAdapter<Group, GroupsAdapter.GroupViewHolder>(GroupDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val binding = ItemGroupBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GroupViewHolder(binding, onGroupClick, onGroupLongClick)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        getItem(position)?.let { group ->
            holder.bind(group)
        }
    }

    class GroupViewHolder(
        private val binding: ItemGroupBinding,
        private val onGroupClick: (Group) -> Unit,
        private val onGroupLongClick: (Group) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(group: Group) {
            binding.apply {
                tvGroupName.text = group.getDisplayName()
                tvLastMessage.text = group.lastMessageText.ifEmpty { "No messages yet" }
                tvMemberCount.text = "${group.memberCount} members"
                
                // Format last message time
                tvLastMessageTime.text = if (group.lastMessageTime > 0) {
                    DateUtils.getRelativeTimeSpanString(
                        group.lastMessageTime,
                        System.currentTimeMillis(),
                        DateUtils.MINUTE_IN_MILLIS
                    )
                } else {
                    ""
                }

                // Load group icon
                Glide.with(itemView.context)
                    .load(group.iconUrl)
                    .circleCrop()
                    .placeholder(R.drawable.ic_group_placeholder)
                    .error(R.drawable.ic_group_placeholder)
                    .into(ivGroupIcon)

                // Show private group indicator
                ivPrivateIndicator.visibility = if (group.isPrivate) {
                    android.view.View.VISIBLE
                } else {
                    android.view.View.GONE
                }

                // Show unread message indicator (you might want to pass unread count)
                // For now, we'll just show a dot if there are recent messages
                unreadIndicator.visibility = if (group.lastMessageTime > 0) {
                    android.view.View.VISIBLE
                } else {
                    android.view.View.GONE
                }

                root.setOnClickListener { onGroupClick(group) }
                root.setOnLongClickListener { 
                    onGroupLongClick(group)
                    true
                }
            }
        }
    }

    private class GroupDiffCallback : DiffUtil.ItemCallback<Group>() {
        override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem == newItem
        }
    }
}