package com.synapse.social.studioasinc.model;

public class Comment {
    private String uid;
    private String comment;
    private long timestamp;

    public Comment() {
    }

    public Comment(String uid, String comment, long timestamp) {
        this.uid = uid;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
