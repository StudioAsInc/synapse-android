package com.synapse.social.studioasinc

import android.content.Context
import com.google.firebase.database.FirebaseDatabase
import com.synapse.social.studioasinc.crypto.E2EEHelper
import com.synapse.social.studioasinc.model.AttachmentInfo

class MessageSender(
    private val e2eeHelper: E2EEHelper,
    private val firebaseDatabase: FirebaseDatabase,
    private val currentUserUid: String
) {

    companion object {
        private const val UID_KEY = "uid"
        private const val TYPE_KEY = "TYPE"
        private const val MESSAGE_STATE_KEY = "message_state"
        private const val KEY_KEY = "key"
        private const val PUSH_DATE_KEY = "push_date"
        private const val REPLIED_MESSAGE_ID_KEY = "replied_message_id"
        private const val IS_ENCRYPTED_KEY = "isEncrypted"
        private const val MESSAGE_TEXT_KEY = "message_text"
        private const val ATTACHMENTS_KEY = "attachments"
        private const val WIDTH_KEY = "width"
        private const val HEIGHT_KEY = "height"
        private const val URL_KEY = "url"
        private const val PUBLIC_ID_KEY = "publicId"
        private const val LAST_MESSAGE_UID_KEY = "last_message_uid"
        private const val LAST_MESSAGE_TEXT_KEY = "last_message_text"
        private const val LAST_MESSAGE_STATE_KEY = "last_message_state"
        private const val MESSAGE_TYPE_ATTACHMENT = "ATTACHMENT_MESSAGE"
        private const val MESSAGE_TYPE_TEXT = "MESSAGE"
        private const val MESSAGE_STATE_SENDED = "sended"
    }

    interface SendMessageCallback {
        fun onSuccess()
        fun onFailure(exception: Exception)
    }

    fun sendMessage(
        messageText: String,
        recipientUid: String,
        attachments: List<AttachmentInfo>,
        repliedMessageId: String?,
        senderDisplayName: String,
        callback: SendMessageCallback
    ): HashMap<String, Any> {
        val messageData = HashMap<String, Any>()
        try {
            val messageType = if (attachments.isNotEmpty()) MESSAGE_TYPE_ATTACHMENT else MESSAGE_TYPE_TEXT
            val mainRef = firebaseDatabase.getReference("skyline")
            val uniqueMessageKey = mainRef.push().key ?: throw Exception("Couldn't get push key for message.")

            messageData[UID_KEY] = currentUserUid
            messageData[TYPE_KEY] = messageType
            messageData[MESSAGE_STATE_KEY] = MESSAGE_STATE_SENDED
            messageData[KEY_KEY] = uniqueMessageKey
            messageData[PUSH_DATE_KEY] = com.google.firebase.database.ServerValue.TIMESTAMP

            if (!repliedMessageId.isNullOrEmpty()) {
                messageData[REPLIED_MESSAGE_ID_KEY] = repliedMessageId
            }

            // Encrypt message content
            messageData[IS_ENCRYPTED_KEY] = true
            messageData[MESSAGE_TEXT_KEY] = if (messageText.isNotEmpty()) e2eeHelper.encrypt(recipientUid, messageText) else ""

            if (attachments.isNotEmpty()) {
                val encryptedAttachments = attachments.map { attachment ->
                    val encryptedAttachment = HashMap<String, Any?>()
                    encryptedAttachment[WIDTH_KEY] = attachment.width
                    encryptedAttachment[HEIGHT_KEY] = attachment.height
                    encryptedAttachment[URL_KEY] = attachment.url?.let { e2eeHelper.encrypt(recipientUid, it) }
                    encryptedAttachment[PUBLIC_ID_KEY] = attachment.publicId?.let { e2eeHelper.encrypt(recipientUid, it) }
                    encryptedAttachment
                }
                messageData[ATTACHMENTS_KEY] = encryptedAttachments
            }

            // Use a multi-path update for an atomic write to both users' chat nodes.
            val fanOutData = HashMap<String, Any>()
            fanOutData["chats/$currentUserUid/$recipientUid/$uniqueMessageKey"] = messageData
            fanOutData["chats/$recipientUid/$currentUserUid/$uniqueMessageKey"] = messageData

            mainRef.updateChildren(fanOutData)
                .addOnSuccessListener {
                    // Update inboxes and send notification after successful DB write
                    val lastMessage = if (attachments.isNotEmpty()) {
                        if (messageText.isNotEmpty()) "$messageText + ${attachments.size} attachment(s)" else "${attachments.size} attachment(s)"
                    } else {
                        messageText
                    }
                    updateInbox(recipientUid, lastMessage)
                    sendNotification(recipientUid, messageText, attachments, senderDisplayName)
                    callback.onSuccess()
                }
                .addOnFailureListener { e ->
                    callback.onFailure(e)
                }

        } catch (e: Exception) {
            callback.onFailure(e)
        }
        return messageData
    }

    private fun updateInbox(recipientUid: String, lastMessage: String) {
        val timestamp = com.google.firebase.database.ServerValue.TIMESTAMP
        val inboxRef = firebaseDatabase.getReference("skyline/inbox")

        // Update sender's inbox
        val inboxSenderMap = mapOf(
            UID_KEY to recipientUid,
            LAST_MESSAGE_UID_KEY to currentUserUid,
            LAST_MESSAGE_TEXT_KEY to lastMessage,
            LAST_MESSAGE_STATE_KEY to MESSAGE_STATE_SENDED,
            PUSH_DATE_KEY to timestamp
        )
        inboxRef.child(currentUserUid).child(recipientUid).setValue(inboxSenderMap)

        // Update recipient's inbox
        val inboxRecipientMap = mapOf(
            UID_KEY to currentUserUid,
            LAST_MESSAGE_UID_KEY to currentUserUid,
            LAST_MESSAGE_TEXT_KEY to lastMessage,
            LAST_MESSAGE_STATE_KEY to MESSAGE_STATE_SENDED,
            PUSH_DATE_KEY to timestamp
        )
        inboxRef.child(recipientUid).child(currentUserUid).setValue(inboxRecipientMap)
    }

    private fun sendNotification(recipientUid: String, messageText: String, attachments: List<AttachmentInfo>, senderDisplayName: String) {
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
}
