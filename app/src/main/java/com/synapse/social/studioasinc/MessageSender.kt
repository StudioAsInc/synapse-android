package com.synapse.social.studioasinc

import android.content.Context
import com.google.firebase.database.FirebaseDatabase
import com.synapse.social.studioasinc.crypto.E2EEHelper
import com.synapse.social.studioasinc.model.AttachmentInfo

class MessageSender(
    private val e2eeHelper: E2EEHelper,
    private val firebaseDatabase: FirebaseDatabase,
    private val currentUserUid: String,
    private val applicationContext: Context
) {

    interface SendMessageCallback {
        fun onSuccess(messageData: HashMap<String, Any>)
        fun onFailure(exception: Exception)
    }

    fun sendMessage(
        messageText: String,
        recipientUid: String,
        attachments: List<AttachmentInfo>,
        repliedMessageId: String?,
        callback: SendMessageCallback
    ) {
        try {
            val messageType = if (attachments.isNotEmpty()) "ATTACHMENT_MESSAGE" else "MESSAGE"
            val mainRef = firebaseDatabase.getReference("skyline")
            val uniqueMessageKey = mainRef.push().key ?: run {
                callback.onFailure(Exception("Couldn't get push key for message."))
                return
            }

            val messageData = HashMap<String, Any>()
            messageData["uid"] = currentUserUid
            messageData["TYPE"] = messageType
            messageData["message_state"] = "sended"
            messageData["key"] = uniqueMessageKey
            messageData["push_date"] = com.google.firebase.database.ServerValue.TIMESTAMP

            if (repliedMessageId != null && repliedMessageId != "null") {
                messageData["replied_message_id"] = repliedMessageId
            }

            // Encrypt message content
            messageData["isEncrypted"] = true
            messageData["message_text"] = if (messageText.isNotEmpty()) e2eeHelper.encrypt(recipientUid, messageText) else ""

            if (attachments.isNotEmpty()) {
                val encryptedAttachments = attachments.map { attachment ->
                    val encryptedAttachment = HashMap<String, Any?>()
                    encryptedAttachment["width"] = attachment.width
                    encryptedAttachment["height"] = attachment.height
                    encryptedAttachment["url"] = attachment.url?.let { e2eeHelper.encrypt(recipientUid, it) }
                    encryptedAttachment["publicId"] = attachment.publicId?.let { e2eeHelper.encrypt(recipientUid, it) }
                    encryptedAttachment
                }
                messageData["attachments"] = encryptedAttachments
            }

            // Write to both chat nodes
            val chatRefSender = mainRef.child("chats").child(currentUserUid).child(recipientUid).child(uniqueMessageKey)
            val chatRefRecipient = mainRef.child("chats").child(recipientUid).child(currentUserUid).child(uniqueMessageKey)

            chatRefSender.setValue(messageData)
            chatRefRecipient.setValue(messageData)
                .addOnSuccessListener {
                    // Update inboxes and send notification after successful DB write
                    val lastMessage = if (attachments.isNotEmpty()) {
                        if (messageText.isNotEmpty()) "$messageText + ${attachments.size} attachment(s)" else "${attachments.size} attachment(s)"
                    } else {
                        messageText
                    }
                    updateInbox(recipientUid, lastMessage)
                    sendNotification(recipientUid, messageText, attachments)
                    callback.onSuccess(messageData)
                }
                .addOnFailureListener { e ->
                    callback.onFailure(e)
                }

        } catch (e: Exception) {
            callback.onFailure(e)
        }
    }

    private fun updateInbox(recipientUid: String, lastMessage: String) {
        val timestamp = com.google.firebase.database.ServerValue.TIMESTAMP
        val inboxRef = firebaseDatabase.getReference("skyline/inbox")

        // Update sender's inbox
        val inboxSenderMap = mapOf(
            "uid" to recipientUid,
            "last_message_uid" to currentUserUid,
            "last_message_text" to lastMessage,
            "last_message_state" to "sended",
            "push_date" to timestamp
        )
        inboxRef.child(currentUserUid).child(recipientUid).setValue(inboxSenderMap)

        // Update recipient's inbox
        val inboxRecipientMap = mapOf(
            "uid" to currentUserUid,
            "last_message_uid" to currentUserUid,
            "last_message_text" to lastMessage,
            "last_message_state" to "sended",
            "push_date" to timestamp
        )
        inboxRef.child(recipientUid).child(currentUserUid).setValue(inboxRecipientMap)
    }

    private fun sendNotification(recipientUid: String, messageText: String, attachments: List<AttachmentInfo>) {
        // This logic is based on the original implementation in ChatActivity
        val usersRef = firebaseDatabase.getReference("skyline/users")
        usersRef.child(currentUserUid).addListenerForSingleValueEvent(object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                val nickname = snapshot.child("nickname").getValue(String::class.java)
                val username = snapshot.child("username").getValue(String::class.java)
                val senderDisplayName = if (!nickname.isNullOrEmpty() && nickname != "null") nickname else if (!username.isNullOrEmpty() && username != "null") "@$username" else "Someone"

                val notificationPreview = if (attachments.isNotEmpty()) {
                    if (messageText.isEmpty()) "Sent an attachment" else "$messageText + Sent an attachment"
                } else {
                    messageText
                }
                val notificationMessage = "$senderDisplayName: $notificationPreview"
                val chatId = if (currentUserUid.compareTo(recipientUid) > 0) "$currentUserUid$recipientUid" else "$recipientUid$currentUserUid"

                val data = mapOf("chatId" to chatId)
                NotificationHelper.sendNotification(
                    recipientUid,
                    currentUserUid,
                    notificationMessage,
                    "chat_message",
                    data
                )
            }

            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                // Could log this error, but for now we just won't send the notification
            }
        })
    }
}
