package com.synapse.social.studioasinc.model

data class AttachmentItem(
    val localPath: String,
    var uploadState: String = "pending", // pending, uploading, success, failed
    var uploadProgress: Int = 0,
    var cloudinaryUrl: String? = null,
    var publicId: String? = null
)
