package com.synapse.social.studioasinc.model

data class AttachmentItem @JvmOverloads constructor(
    val localPath: String,
    var uploadState: String = UPLOAD_STATE_PENDING,
    var uploadProgress: Int = 0,
    var cloudinaryUrl: String? = null,
    var publicId: String? = null
) {
    companion object {
        const val UPLOAD_STATE_PENDING = "pending"
        const val UPLOAD_STATE_UPLOADING = "uploading"
        const val UPLOAD_STATE_SUCCESS = "success"
        const val UPLOAD_STATE_FAILED = "failed"
    }
}
