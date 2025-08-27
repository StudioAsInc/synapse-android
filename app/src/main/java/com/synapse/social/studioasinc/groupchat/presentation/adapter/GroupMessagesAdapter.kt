package com.synapse.social.studioasinc.groupchat.presentation.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.synapse.social.studioasinc.R
import com.synapse.social.studioasinc.databinding.ItemMessageIncomingBinding
import com.synapse.social.studioasinc.databinding.ItemMessageOutgoingBinding
import com.synapse.social.studioasinc.databinding.ItemMessageSystemBinding
import com.synapse.social.studioasinc.groupchat.data.model.*

class GroupMessagesAdapter(
    private val onMessageClick: (GroupMessage) -> Unit,
    private val onMessageLongClick: (GroupMessage) -> Unit,
    private val onReactionClick: (GroupMessage, String) -> Unit,
    private val onReplyClick: (GroupMessage) -> Unit,
    private val onEditClick: (GroupMessage) -> Unit,
    private val onDeleteClick: (GroupMessage) -> Unit,
    private val canUserEditMessage: (GroupMessage) -> Boolean,
    private val canUserDeleteMessage: (GroupMessage) -> Boolean,
    private val currentUserId: String = "" // You'd inject this
) : PagingDataAdapter<GroupMessage, RecyclerView.ViewHolder>(MessageDiffCallback()) {

    companion object {
        private const val VIEW_TYPE_OUTGOING = 1
        private const val VIEW_TYPE_INCOMING = 2
        private const val VIEW_TYPE_SYSTEM = 3
    }

    override fun getItemViewType(position: Int): Int {
        val message = getItem(position) ?: return VIEW_TYPE_INCOMING
        
        return when {
            message.isSystemMessage -> VIEW_TYPE_SYSTEM
            message.senderId == currentUserId -> VIEW_TYPE_OUTGOING
            else -> VIEW_TYPE_INCOMING
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        
        return when (viewType) {
            VIEW_TYPE_OUTGOING -> {
                val binding = ItemMessageOutgoingBinding.inflate(inflater, parent, false)
                OutgoingMessageViewHolder(binding)
            }
            VIEW_TYPE_INCOMING -> {
                val binding = ItemMessageIncomingBinding.inflate(inflater, parent, false)
                IncomingMessageViewHolder(binding)
            }
            VIEW_TYPE_SYSTEM -> {
                val binding = ItemMessageSystemBinding.inflate(inflater, parent, false)
                SystemMessageViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = getItem(position) ?: return
        
        when (holder) {
            is OutgoingMessageViewHolder -> holder.bind(message)
            is IncomingMessageViewHolder -> holder.bind(message)
            is SystemMessageViewHolder -> holder.bind(message)
        }
    }

    inner class OutgoingMessageViewHolder(
        private val binding: ItemMessageOutgoingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(message: GroupMessage) {
            binding.apply {
                tvMessageText.text = message.text
                tvTimestamp.text = formatTimestamp(message.timestamp)
                
                // Show edited indicator
                tvEdited.visibility = if (message.isEdited) View.VISIBLE else View.GONE
                
                // Show delivery status
                updateDeliveryStatus(message.getDeliveryStatus())
                
                // Show attachments if any
                setupAttachments(message.attachments)
                
                // Show reply preview if replying to another message
                setupReplyPreview(message.replyToMessageId)
                
                // Show reactions
                setupReactions(message.reactions)
                
                // Click listeners
                root.setOnClickListener { onMessageClick(message) }
                root.setOnLongClickListener { 
                    onMessageLongClick(message)
                    true
                }
                
                btnReact.setOnClickListener { 
                    onReactionClick(message, "👍") // Default reaction
                }
                
                if (canUserEditMessage(message)) {
                    btnEdit.visibility = View.VISIBLE
                    btnEdit.setOnClickListener { onEditClick(message) }
                } else {
                    btnEdit.visibility = View.GONE
                }
                
                if (canUserDeleteMessage(message)) {
                    btnDelete.visibility = View.VISIBLE
                    btnDelete.setOnClickListener { onDeleteClick(message) }
                } else {
                    btnDelete.visibility = View.GONE
                }
            }
        }

        private fun updateDeliveryStatus(status: DeliveryStatus) {
            binding.ivDeliveryStatus.setImageResource(
                when (status) {
                    DeliveryStatus.SENDING -> R.drawable.ic_clock
                    DeliveryStatus.SENT -> R.drawable.ic_check
                    DeliveryStatus.DELIVERED -> R.drawable.ic_check_double
                    DeliveryStatus.SEEN -> R.drawable.ic_check_double_blue
                    DeliveryStatus.FAILED -> R.drawable.ic_error
                }
            )
        }

        private fun setupAttachments(attachments: List<MessageAttachment>) {
            if (attachments.isEmpty()) {
                binding.attachmentContainer.visibility = View.GONE
                return
            }
            
            binding.attachmentContainer.visibility = View.VISIBLE
            
            // Handle different attachment types
            attachments.forEach { attachment ->
                when (attachment.getAttachmentType()) {
                    AttachmentType.IMAGE -> setupImageAttachment(attachment)
                    AttachmentType.VIDEO -> setupVideoAttachment(attachment)
                    AttachmentType.AUDIO -> setupAudioAttachment(attachment)
                    AttachmentType.FILE -> setupFileAttachment(attachment)
                }
            }
        }

        private fun setupImageAttachment(attachment: MessageAttachment) {
            binding.ivAttachment.visibility = View.VISIBLE
            Glide.with(binding.root.context)
                .load(attachment.url)
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.ic_broken_image)
                .into(binding.ivAttachment)
        }

        private fun setupVideoAttachment(attachment: MessageAttachment) {
            // Setup video thumbnail and play button
        }

        private fun setupAudioAttachment(attachment: MessageAttachment) {
            // Setup audio player UI
        }

        private fun setupFileAttachment(attachment: MessageAttachment) {
            // Setup file preview
        }

        private fun setupReplyPreview(replyToMessageId: String) {
            if (replyToMessageId.isEmpty()) {
                binding.replyPreview.visibility = View.GONE
                return
            }
            
            binding.replyPreview.visibility = View.VISIBLE
            // Load and display the original message preview
        }

        private fun setupReactions(reactions: Map<String, List<String>>) {
            binding.chipGroupReactions.removeAllViews()
            
            if (reactions.isEmpty()) {
                binding.chipGroupReactions.visibility = View.GONE
                return
            }
            
            binding.chipGroupReactions.visibility = View.VISIBLE
            
            reactions.forEach { (emoji, userIds) ->
                val chip = Chip(binding.root.context).apply {
                    text = "$emoji ${userIds.size}"
                    isClickable = true
                    setOnClickListener {
                        // Toggle reaction
                    }
                }
                binding.chipGroupReactions.addView(chip)
            }
        }
    }

    inner class IncomingMessageViewHolder(
        private val binding: ItemMessageIncomingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(message: GroupMessage) {
            binding.apply {
                tvSenderName.text = message.senderName
                tvMessageText.text = message.text
                tvTimestamp.text = formatTimestamp(message.timestamp)
                
                // Load sender avatar
                Glide.with(root.context)
                    .load(message.senderPhotoUrl)
                    .circleCrop()
                    .placeholder(R.drawable.ic_person_placeholder)
                    .error(R.drawable.ic_person_placeholder)
                    .into(ivSenderAvatar)
                
                // Show edited indicator
                tvEdited.visibility = if (message.isEdited) View.VISIBLE else View.GONE
                
                // Show attachments if any
                setupAttachments(message.attachments)
                
                // Show reply preview if replying to another message
                setupReplyPreview(message.replyToMessageId)
                
                // Show reactions
                setupReactions(message.reactions)
                
                // Click listeners
                root.setOnClickListener { onMessageClick(message) }
                root.setOnLongClickListener { 
                    onMessageLongClick(message)
                    true
                }
                
                btnReact.setOnClickListener { 
                    onReactionClick(message, "👍")
                }
                
                btnReply.setOnClickListener { 
                    onReplyClick(message)
                }
            }
        }

        private fun setupAttachments(attachments: List<MessageAttachment>) {
            // Similar to outgoing message attachment setup
        }

        private fun setupReplyPreview(replyToMessageId: String) {
            // Similar to outgoing message reply setup
        }

        private fun setupReactions(reactions: Map<String, List<String>>) {
            // Similar to outgoing message reactions setup
        }
    }

    inner class SystemMessageViewHolder(
        private val binding: ItemMessageSystemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(message: GroupMessage) {
            binding.apply {
                tvSystemMessage.text = message.text
                tvTimestamp.text = formatTimestamp(message.timestamp)
            }
        }
    }

    private fun formatTimestamp(timestamp: Long): String {
        return DateUtils.getRelativeTimeSpanString(
            timestamp,
            System.currentTimeMillis(),
            DateUtils.MINUTE_IN_MILLIS,
            DateUtils.FORMAT_ABBREV_RELATIVE
        ).toString()
    }

    private class MessageDiffCallback : DiffUtil.ItemCallback<GroupMessage>() {
        override fun areItemsTheSame(oldItem: GroupMessage, newItem: GroupMessage): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GroupMessage, newItem: GroupMessage): Boolean {
            return oldItem == newItem
        }
    }
}