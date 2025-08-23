package com.synapse.social.studioasinc

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

/**
 * An example ChatActivity demonstrating how to integrate the full presence and notification system.
 */
class ChatActivity : AppCompatActivity() {

    // --- User and Recipient Information ---
    // In a real app, you would pass this data to the activity via intents.
    private var currentUserUid: String? = null
    private lateinit var recipientUid: String
    private lateinit var recipientOneSignalPlayerId: String

    // --- UI Elements ---
    // These would be connected to your layout file (e.g., via findViewById or ViewBinding)
    private lateinit var sendButton: Button
    private lateinit var messageInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make sure you have a layout file, e.g., R.layout.activity_chat
        // setContentView(R.layout.activity_chat)

        // --- Get Current User ---
        // It's crucial to ensure a user is logged in.
        currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserUid == null) {
            Log.e("ChatActivity", "FATAL: User is not logged in.")
            // In a real app, you'd probably finish() the activity or redirect to a login screen.
            finish()
            return
        }

        // --- Get Recipient Info (from Intent) ---
        // In a real app, you would pass the recipient's info when starting this activity.
        // For this example, we'll use placeholder data.
        recipientUid = "placeholder_recipient_uid" // TODO: Replace with actual data from Intent
        recipientOneSignalPlayerId = "placeholder_recipient_onesignal_id" // TODO: Replace with actual data from Intent

        // --- Initialize UI ---
        // For this example, we'll create dummy views to avoid crashing.
        // In a real app, you would use:
        // sendButton = findViewById(R.id.sendButton)
        // messageInput = findViewById(R.id.messageEditText)
        sendButton = Button(this)
        messageInput = EditText(this)

        // --- Setup Send Button Listener ---
        sendButton.setOnClickListener {
            val message = messageInput.text.toString().trim()
            if (message.isNotEmpty()) {
                sendMessage(message)
                messageInput.text.clear()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // When the user enters this chat screen, update their status to "chatting_with_..."
        currentUserUid?.let {
            PresenceManager.setChattingWith(it, recipientUid)
        }
    }

    override fun onPause() {
        super.onPause()
        // When the user leaves this chat screen (but not necessarily the app),
        // revert their status back to "online". The onDisconnect handler will
        // manage the "offline" status if the app is closed.
        currentUserUid?.let {
            PresenceManager.stopChatting(it)
        }
    }

    /**
     * Handles the logic of sending a message and triggering the smart notification check.
     */
    private fun sendMessage(message: String) {
        val senderUid = currentUserUid ?: return // Safety check

        // Launch a coroutine to call our suspend function
        lifecycleScope.launch {
            sendMessageAndNotifyIfNeeded(
                senderUid = senderUid,
                recipientUid = recipientUid,
                recipientOneSignalPlayerId = recipientOneSignalPlayerId,
                message = message
            )
            // The actual saving of the message to Firestore/RTDB would happen
            // inside or after this call, as marked by the TODO in the function itself.
        }
    }
}
