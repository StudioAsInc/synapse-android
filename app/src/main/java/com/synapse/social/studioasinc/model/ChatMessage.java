package com.synapse.social.studioasinc.model;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatMessage {
    public String uid;
    public String message_text;
    public Object attachments;
    public boolean isEncrypted;
    public String replied_message_id;
    public String key;
    public long push_date;
    public String message_state;
    public String TYPE;
    public String attachmentViewType;
    public ArrayList<HashMap<String, Object>> decryptedAttachments;

    public ChatMessage() {
    }
}
