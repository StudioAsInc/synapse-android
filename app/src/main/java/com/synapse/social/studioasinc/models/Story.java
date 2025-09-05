package com.synapse.social.studioasinc.models;

import java.io.Serializable;

public class Story implements Serializable {
    private String uid;
    private String url;
    private long timestamp;
    private String type;

    public Story() {
        // Default constructor required for calls to DataSnapshot.getValue(Story.class)
    }

    public Story(String uid, String url, long timestamp, String type) {
        this.uid = uid;
        this.url = url;
        this.timestamp = timestamp;
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
