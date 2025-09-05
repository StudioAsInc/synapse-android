package com.synapse.social.studioasinc

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.synapse.social.studioasinc.crypto.E2EEHelper
import java.util.HashMap

class SendMessageModal @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val TAG = "SendMessageModal"

    // Constants
    private val SKYLINE_REF = "skyline"
    private val CHATS_REF = "chats"
    private val INBOX_REF = "inbox"
    private val UID_KEY = "uid"
    private val MESSAGE_TEXT_KEY = "message_text"
    private val TYPE_KEY = "TYPE"
    private val MESSAGE_STATE_KEY = "message_state"
    private val PUSH_DATE_KEY = "push_date"
    private val REPLIED_MESSAGE_ID_KEY = "replied_message_id"
    val ATTACHMENTS_KEY = "attachments"
    private val LAST_MESSAGE_UID_KEY = "last_message_uid"
    private val LAST_MESSAGE_TEXT_KEY = "last_message_text"
    private val LAST_MESSAGE_STATE_KEY = "last_message_state"
    private val MESSAGE_TYPE = "MESSAGE"
    private val ATTACHMENT_MESSAGE_TYPE = "ATTACHMENT_MESSAGE"
    private val KEY_KEY = "key"

    // Views
    private val message_et: FadeEditText
    private val btn_sendMessage: MaterialButton
    private val galleryBtn: ImageView
    private val message_input_outlined_round: LinearLayout

    // Firebase
    private val _firebase = FirebaseDatabase.getInstance()
    private val main: DatabaseReference = _firebase.getReference(SKYLINE_REF)
    private val auth = FirebaseAuth.getInstance()

    // Collaborators
    private val e2eeHelper: E2EEHelper
    private var recipientUid: String? = null
    private var firstUserName: String? = null
    private var listener: Listener? = null
    var replyMessageID: String? = "null"

    interface Listener {
        fun onMessageSent(messageData: HashMap<String, Any>)
        fun getAttachmentMap(): ArrayList<HashMap<String, Any>>
        fun resetAttachmentState()
        fun onPickFiles()
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.send_message_modal, this, true)
        orientation = VERTICAL

        message_et = findViewById(R.id.message_et)
        btn_sendMessage = findViewById(R.id.btn_sendMessage)
        galleryBtn = findViewById(R.id.galleryBtn)
        message_input_outlined_round = findViewById(R.id.message_input_outlined_round)

        e2eeHelper = E2EEHelper(context)

        btn_sendMessage.setOnClickListener { _send_btn() }
        galleryBtn.setOnClickListener {
            listener?.onPickFiles()
        }

        message_et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    message_input_outlined_round.orientation = HORIZONTAL
                    message_input_outlined_round.setBackgroundResource(R.drawable.bg_message_input)
                } else {
                    message_input_outlined_round.orientation = VERTICAL
                    message_input_outlined_round.setBackgroundResource(R.drawable.bg_message_input_expanded)
                }
                message_input_outlined_round.invalidate()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun setChatPartner(recipientUid: String, firstUserName: String) {
        this.recipientUid = recipientUid
        this.firstUserName = firstUserName
    }

    fun _send_btn() {
        if (auth.currentUser == null) {
            Log.e(TAG, "Cannot send message, user is not authenticated.")
            Toast.makeText(context, "Error: User not signed in.", Toast.LENGTH_SHORT).show()
            return
        }
        val messageText = message_et.text.toString().trim()
        val senderUid = auth.currentUser!!.uid
        val recipientUid = this.recipientUid

        if (recipientUid != null) {
            proceedWithMessageSending(messageText, senderUid, recipientUid)
        }
    }

    private fun proceedWithMessageSending(messageText: String, senderUid: String, recipientUid: String) {
        if (auth.currentUser != null) {
            PresenceManager.setActivity(auth.currentUser!!.uid, "Idle")
        }

        val attactmentmap = listener?.getAttachmentMap() ?: ArrayList()

        if (attactmentmap.isNotEmpty()) {
            // Logic for sending messages with attachments
            val successfulAttachments = ArrayList<HashMap<String, Any>>()
            var allUploadsSuccessful = true
            for (item in attactmentmap) {
                if ("success" == item["uploadState"]) {
                    val attachmentData = HashMap<String, Any>()
                    attachmentData["url"] = item["cloudinaryUrl"] as Any
                    attachmentData["publicId"] = item["publicId"] as Any
                    attachmentData["width"] = item["width"] as Any
                    attachmentData["height"] = item["height"] as Any
                    successfulAttachments.add(attachmentData)
                } else {
                    allUploadsSuccessful = false
                }
            }

            if (allUploadsSuccessful && (messageText.isNotEmpty() || successfulAttachments.isNotEmpty())) {
                val uniqueMessageKey = main.push().key
                val chatSendMap = HashMap<String, Any>()
                chatSendMap[UID_KEY] = senderUid
                chatSendMap[TYPE_KEY] = ATTACHMENT_MESSAGE_TYPE

                try {
                    val encryptedMessageText = if (messageText.isEmpty()) "" else e2eeHelper.encrypt(recipientUid, messageText)
                    chatSendMap[MESSAGE_TEXT_KEY] = encryptedMessageText

                    val encryptedAttachments = ArrayList<HashMap<String, Any>>()
                    for (attachment in successfulAttachments) {
                        val encryptedAttachment = HashMap(attachment)
                        val url = encryptedAttachment["url"] as? String
                        val publicId = encryptedAttachment["publicId"] as? String

                        if (url != null) {
                            encryptedAttachment["url"] = e2eeHelper.encrypt(recipientUid, url)
                        }
                        if (publicId != null) {
                            encryptedAttachment["publicId"] = e2eeHelper.encrypt(recipientUid, publicId)
                        }
                        encryptedAttachments.add(encryptedAttachment)
                    }
                    chatSendMap[ATTACHMENTS_KEY] = encryptedAttachments
                    chatSendMap["isEncrypted"] = true

                } catch (e: Exception) {
                    Log.e(TAG, "Failed to encrypt attachment message", e)
                    Toast.makeText(context, "Error: Could not send secure attachment message.", Toast.LENGTH_SHORT).show()
                    return
                }

                chatSendMap[MESSAGE_STATE_KEY] = "sended"
                if (replyMessageID != "null") chatSendMap[REPLIED_MESSAGE_ID_KEY] = replyMessageID as Any
                chatSendMap[KEY_KEY] = uniqueMessageKey as Any
                chatSendMap[PUSH_DATE_KEY] = ServerValue.TIMESTAMP

                val ref1 = _firebase.getReference(SKYLINE_REF).child(CHATS_REF).child(senderUid).child(recipientUid).child(uniqueMessageKey!!)
                val ref2 = _firebase.getReference(SKYLINE_REF).child(CHATS_REF).child(recipientUid).child(senderUid).child(uniqueMessageKey)
                ref1.setValue(chatSendMap)
                ref2.setValue(chatSendMap)

                listener?.onMessageSent(chatSendMap)

                val lastMessage = if (messageText.isEmpty()) "${successfulAttachments.size} attachment(s)" else messageText
                _updateInbox(lastMessage, ServerValue.TIMESTAMP)

                message_et.setText("")
                replyMessageID = "null"
                listener?.resetAttachmentState()
            } else {
                Toast.makeText(context, "Waiting for uploads to complete...", Toast.LENGTH_SHORT).show()
            }

        } else if (messageText.isNotEmpty()) {
            // Logic for sending text-only messages
            val uniqueMessageKey: String?
            val chatSendMap: HashMap<String, Any>
            try {
                val encryptedMessage = e2eeHelper.encrypt(recipientUid, messageText)
                uniqueMessageKey = main.push().key

                chatSendMap = HashMap()
                chatSendMap[UID_KEY] = senderUid
                chatSendMap[TYPE_KEY] = MESSAGE_TYPE
                chatSendMap[MESSAGE_TEXT_KEY] = encryptedMessage
                chatSendMap["isEncrypted"] = true
                chatSendMap[MESSAGE_STATE_KEY] = "sended"
                if (replyMessageID != "null") chatSendMap[REPLIED_MESSAGE_ID_KEY] = replyMessageID as Any
                chatSendMap[KEY_KEY] = uniqueMessageKey as Any
                chatSendMap[PUSH_DATE_KEY] = ServerValue.TIMESTAMP
            } catch (e: Exception) {
                Log.e(TAG, "Failed to encrypt and send message", e)
                Toast.makeText(context, "Error: Could not send secure message.", Toast.LENGTH_SHORT).show()
                return
            }

            val ref1 = _firebase.getReference(SKYLINE_REF).child(CHATS_REF).child(senderUid).child(recipientUid).child(uniqueMessageKey!!)
            val ref2 = _firebase.getReference(SKYLINE_REF).child(CHATS_REF).child(recipientUid).child(senderUid).child(uniqueMessageKey)
            ref1.setValue(chatSendMap)
            ref2.setValue(chatSendMap)

            listener?.onMessageSent(chatSendMap)

            _updateInbox(messageText, ServerValue.TIMESTAMP)

            message_et.setText("")
            replyMessageID = "null"
        }
    }

    fun _updateInbox(lastMessage: String, timestamp: Any) {
        val currentUser = auth.currentUser ?: return
        val myUid = currentUser.uid

        // Update inbox for the current user
        val chatInboxSend = HashMap<String, Any>()
        chatInboxSend[UID_KEY] = recipientUid as Any
        chatInboxSend[LAST_MESSAGE_UID_KEY] = myUid
        chatInboxSend[LAST_MESSAGE_TEXT_KEY] = lastMessage
        chatInboxSend[LAST_MESSAGE_STATE_KEY] = "sended"
        chatInboxSend[PUSH_DATE_KEY] = timestamp
        _firebase.getReference(SKYLINE_REF).child(INBOX_REF).child(myUid).child(recipientUid!!).setValue(chatInboxSend)

        // Update inbox for the other user
        val chatInboxSend2 = HashMap<String, Any>()
        chatInboxSend2[UID_KEY] = myUid
        chatInboxSend2[LAST_MESSAGE_UID_KEY] = myUid
        chatInboxSend2[LAST_MESSAGE_TEXT_KEY] = lastMessage
        chatInboxSend2[LAST_MESSAGE_STATE_KEY] = "sended"
        chatInboxSend2[PUSH_DATE_KEY] = timestamp
        _firebase.getReference(SKYLINE_REF).child(INBOX_REF).child(recipientUid!!).child(myUid).setValue(chatInboxSend2)
    }

    fun getMessageEditText(): EditText {
        return message_et
    }
}
