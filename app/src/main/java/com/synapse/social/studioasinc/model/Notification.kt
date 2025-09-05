package com.synapse.social.studioasinc.model

import com.google.firebase.database.PropertyName

data class Notification(
    @PropertyName("from")
    val from: String = "", // This will be used as userId
    @PropertyName("message")
    val message: String = "",
    @PropertyName("type")
    val type: String = "",
    @PropertyName("postId")
    val postId: String = "",
    @PropertyName("commentId")
    val commentId: String = "",
    @PropertyName("timestamp")
    val timestamp: Long = 0,
    @get:PropertyName("isRead")
    @set:PropertyName("isRead")
    var isRead: Boolean = false
)
