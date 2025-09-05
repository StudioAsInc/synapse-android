package com.synapse.social.studioasinc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.synapse.social.studioasinc.crypto.E2EEHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class SendMessageModal extends LinearLayout {

    private static final String TAG = "SendMessageModal";

    // Constants from ChatActivity
    private static final String SKYLINE_REF = "skyline";
    private static final String CHATS_REF = "chats";
    private static final String INBOX_REF = "inbox";
    private static final String UID_KEY = "uid";
    private static final String MESSAGE_TEXT_KEY = "message_text";
    private static final String TYPE_KEY = "TYPE";
    private static final String MESSAGE_STATE_KEY = "message_state";
    private static final String PUSH_DATE_KEY = "push_date";
    private static final String REPLIED_MESSAGE_ID_KEY = "replied_message_id";
    public static final String ATTACHMENTS_KEY = "attachments";
    private static final String LAST_MESSAGE_UID_KEY = "last_message_uid";
    private static final String LAST_MESSAGE_TEXT_KEY = "last_message_text";
    private static final String LAST_MESSAGE_STATE_KEY = "last_message_state";
    private static final String MESSAGE_TYPE = "MESSAGE";
    private static final String ATTACHMENT_MESSAGE_TYPE = "ATTACHMENT_MESSAGE";
    private static final String KEY_KEY = "key";

    // Views
    private FadeEditText message_et;
    private MaterialButton btn_sendMessage;
    private ImageView galleryBtn;
    private LinearLayout message_input_outlined_round;
    private LinearLayout message_input_overall_container;


    // Firebase
    private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
    private DatabaseReference main = _firebase.getReference(SKYLINE_REF);
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    // Collaborators
    private E2EEHelper e2eeHelper;
    private String recipientUid;
    private String firstUserName;
    private Listener listener;
    private String replyMessageID = "null";


    public interface Listener {
        void onMessageSent(HashMap<String, Object> messageData);
        ArrayList<HashMap<String, Object>> getAttachmentMap();
        void resetAttachmentState();
        void onPickFiles();
    }

    public SendMessageModal(Context context) {
        super(context);
        init(context);
    }

    public SendMessageModal(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SendMessageModal(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.send_message_modal, this, true);
        setOrientation(VERTICAL);

        message_et = findViewById(R.id.message_et);
        btn_sendMessage = findViewById(R.id.btn_sendMessage);
        galleryBtn = findViewById(R.id.galleryBtn);
        message_input_outlined_round = findViewById(R.id.message_input_outlined_round);
        message_input_overall_container = findViewById(R.id.message_input_overall_container);

        e2eeHelper = new E2EEHelper(context);

        btn_sendMessage.setOnClickListener(v -> _send_btn());
        galleryBtn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPickFiles();
            }
        });

        message_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    message_input_outlined_round.setOrientation(LinearLayout.HORIZONTAL);
                    message_input_outlined_round.setBackgroundResource(R.drawable.bg_message_input);
                } else {
                    message_input_outlined_round.setOrientation(LinearLayout.VERTICAL);
                    message_input_outlined_round.setBackgroundResource(R.drawable.bg_message_input_expanded);
                }
                 message_input_outlined_round.invalidate();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setChatPartner(String recipientUid, String firstUserName) {
        this.recipientUid = recipientUid;
        this.firstUserName = firstUserName;
    }
     public void setReplyMessageID(String replyMessageID) {
        this.replyMessageID = replyMessageID;
    }

    public void _send_btn() {
        if (auth.getCurrentUser() == null) {
            Log.e(TAG, "Cannot send message, user is not authenticated.");
            Toast.makeText(getContext(), "Error: User not signed in.", Toast.LENGTH_SHORT).show();
            return;
        }
        final String messageText = message_et.getText().toString().trim();
        final String senderUid = auth.getCurrentUser().getUid();

        proceedWithMessageSending(messageText, senderUid, recipientUid);
    }

    private void proceedWithMessageSending(String messageText, String senderUid, String recipientUid) {
        if (auth.getCurrentUser() != null) {
            PresenceManager.setActivity(auth.getCurrentUser().getUid(), "Idle");
        }

        ArrayList<HashMap<String, Object>> attactmentmap = listener != null ? listener.getAttachmentMap() : new ArrayList<>();

        if (!attactmentmap.isEmpty()) {
            // Logic for sending messages with attachments
            ArrayList<HashMap<String, Object>> successfulAttachments = new ArrayList<>();
            boolean allUploadsSuccessful = true;
            for (HashMap<String, Object> item : attactmentmap) {
                if ("success".equals(item.get("uploadState"))) {
                    HashMap<String, Object> attachmentData = new HashMap<>();
                    attachmentData.put("url", item.get("cloudinaryUrl"));
                    attachmentData.put("publicId", item.get("publicId"));
                    attachmentData.put("width", item.get("width"));
                    attachmentData.put("height", item.get("height"));
                    successfulAttachments.add(attachmentData);
                } else {
                    allUploadsSuccessful = false;
                }
            }

            if (allUploadsSuccessful && (!messageText.isEmpty() || !successfulAttachments.isEmpty())) {
                String uniqueMessageKey = main.push().getKey();
                HashMap<String, Object> chatSendMap = new HashMap<>();
                chatSendMap.put(UID_KEY, senderUid);
                chatSendMap.put(TYPE_KEY, ATTACHMENT_MESSAGE_TYPE);

                try {
                    String encryptedMessageText = messageText.isEmpty() ? "" : e2eeHelper.encrypt(recipientUid, messageText);
                    chatSendMap.put(MESSAGE_TEXT_KEY, encryptedMessageText);

                    ArrayList<HashMap<String, Object>> encryptedAttachments = new ArrayList<>();
                    for (HashMap<String, Object> attachment : successfulAttachments) {
                        HashMap<String, Object> encryptedAttachment = new HashMap<>(attachment);
                        String url = (String) encryptedAttachment.get("url");
                        String publicId = (String) encryptedAttachment.get("publicId");

                        if (url != null) {
                            encryptedAttachment.put("url", e2eeHelper.encrypt(recipientUid, url));
                        }
                        if (publicId != null) {
                            encryptedAttachment.put("publicId", e2eeHelper.encrypt(recipientUid, publicId));
                        }
                        encryptedAttachments.add(encryptedAttachment);
                    }
                    chatSendMap.put(ATTACHMENTS_KEY, encryptedAttachments);
                    chatSendMap.put("isEncrypted", true);

                } catch (Exception e) {
                    Log.e(TAG, "Failed to encrypt attachment message", e);
                    Toast.makeText(getContext(), "Error: Could not send secure attachment message.", Toast.LENGTH_SHORT).show();
                    return;
                }

                chatSendMap.put(MESSAGE_STATE_KEY, "sended");
                if (!replyMessageID.equals("null")) chatSendMap.put(REPLIED_MESSAGE_ID_KEY, replyMessageID);
                chatSendMap.put(KEY_KEY, uniqueMessageKey);
                chatSendMap.put(PUSH_DATE_KEY, ServerValue.TIMESTAMP);

                DatabaseReference ref1 = _firebase.getReference(SKYLINE_REF).child(CHATS_REF).child(senderUid).child(recipientUid).child(uniqueMessageKey);
                DatabaseReference ref2 = _firebase.getReference(SKYLINE_REF).child(CHATS_REF).child(recipientUid).child(senderUid).child(uniqueMessageKey);
                ref1.setValue(chatSendMap);
                ref2.setValue(chatSendMap);

                if (listener != null) {
                    listener.onMessageSent(chatSendMap);
                }

                String lastMessage = messageText.isEmpty() ? successfulAttachments.size() + " attachment(s)" : messageText;
                _updateInbox(lastMessage, ServerValue.TIMESTAMP);

                message_et.setText("");
                replyMessageID = "null";
                if (listener != null) {
                    listener.resetAttachmentState();
                }
            } else {
                Toast.makeText(getContext(), "Waiting for uploads to complete...", Toast.LENGTH_SHORT).show();
            }

        } else if (!messageText.isEmpty()) {
            // Logic for sending text-only messages
            String uniqueMessageKey;
            HashMap<String, Object> chatSendMap;
            try {
                String encryptedMessage = e2eeHelper.encrypt(recipientUid, messageText);
                uniqueMessageKey = main.push().getKey();

                chatSendMap = new HashMap<>();
                chatSendMap.put(UID_KEY, senderUid);
                chatSendMap.put(TYPE_KEY, MESSAGE_TYPE);
                chatSendMap.put(MESSAGE_TEXT_KEY, encryptedMessage);
                chatSendMap.put("isEncrypted", true);
                chatSendMap.put(MESSAGE_STATE_KEY, "sended");
                if (!replyMessageID.equals("null")) chatSendMap.put(REPLIED_MESSAGE_ID_KEY, replyMessageID);
                chatSendMap.put(KEY_KEY, uniqueMessageKey);
                chatSendMap.put(PUSH_DATE_KEY, ServerValue.TIMESTAMP);
            } catch (Exception e) {
                Log.e(TAG, "Failed to encrypt and send message", e);
                Toast.makeText(getContext(), "Error: Could not send secure message.", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseReference ref1 = _firebase.getReference(SKYLINE_REF).child(CHATS_REF).child(senderUid).child(recipientUid).child(uniqueMessageKey);
            DatabaseReference ref2 = _firebase.getReference(SKYLINE_REF).child(CHATS_REF).child(recipientUid).child(senderUid).child(uniqueMessageKey);
            ref1.setValue(chatSendMap);
            ref2.setValue(chatSendMap);

            if (listener != null) {
                listener.onMessageSent(chatSendMap);
            }

            _updateInbox(messageText, ServerValue.TIMESTAMP);

            message_et.setText("");
            replyMessageID = "null";
        }
    }

    public void _updateInbox(final String _lastMessage, final Object _timestamp) {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Log.e(TAG, "Cannot update inbox, user is not authenticated.");
            return;
        }
        String myUid = currentUser.getUid();

        // Update inbox for the current user
        HashMap<String, Object> chatInboxSend = new HashMap<>();
        chatInboxSend.put(UID_KEY, recipientUid);
        chatInboxSend.put(LAST_MESSAGE_UID_KEY, myUid);
        chatInboxSend.put(LAST_MESSAGE_TEXT_KEY, _lastMessage);
        chatInboxSend.put(LAST_MESSAGE_STATE_KEY, "sended");
        chatInboxSend.put(PUSH_DATE_KEY, _timestamp);
        _firebase.getReference(SKYLINE_REF).child(INBOX_REF).child(myUid).child(recipientUid).setValue(chatInboxSend);

        // Update inbox for the other user
        HashMap<String, Object> chatInboxSend2 = new HashMap<>();
        chatInboxSend2.put(UID_KEY, myUid);
        chatInboxSend2.put(LAST_MESSAGE_UID_KEY, myUid);
        chatInboxSend2.put(LAST_MESSAGE_TEXT_KEY, _lastMessage);
        chatInboxSend2.put(LAST_MESSAGE_STATE_KEY, "sended");
        chatInboxSend2.put(PUSH_DATE_KEY, _timestamp);
        _firebase.getReference(SKYLINE_REF).child(INBOX_REF).child(recipientUid).child(myUid).setValue(chatInboxSend2);
    }
     public EditText getMessageEditText() {
        return message_et;
    }
}
