package com.synapse.social.studioasinc

import android.content.Context
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback as CloudinaryUploadCallback
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers

object FasterCloudinaryUploader {

    private var isInitialized = false

    fun init(context: Context) {
        if (isInitialized) return
        try {
            val config = mapOf(
                "cloud_name" to BuildConfig.CLOUDINARY_CLOUD_NAME,
                "api_key" to BuildConfig.CLOUDINARY_API_KEY,
                "api_secret" to BuildConfig.CLOUDINARY_API_SECRET
            )
            MediaManager.init(context, config)
            isInitialized = true
        } catch (e: Exception) {
            // This will happen if the app is not built yet and BuildConfig is not generated.
            // We can't do much about it here, but we should not crash the app.
            e.printStackTrace()
        }
    }

    interface UploaderCallback {
        fun onProgress(progress: Int)
        fun onSuccess(url: String, publicId: String)
        fun onError(error: String)
    }

    fun upload(context: Context, filePath: String, callback: UploaderCallback) {
        if (!isInitialized) {
            init(context.applicationContext)
        }

        if (!isInitialized) {
            callback.onError("Cloudinary is not initialized. Please check your credentials.")
            return
        }

        GlobalScope.launch(Dispatchers.IO) {
            MediaManager.get().upload(filePath)
                .callback(object : CloudinaryUploadCallback {
                    override fun onStart(requestId: String) {
                        // Not used
                    }

                    override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                        val progress = (bytes.toDouble() / totalBytes.toDouble() * 100).toInt()
                        launch(Dispatchers.Main) {
                            callback.onProgress(progress)
                        }
                    }

                    override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                        val url = resultData["secure_url"] as? String ?: ""
                        val publicId = resultData["public_id"] as? String ?: ""
                        launch(Dispatchers.Main) {
                            callback.onSuccess(url, publicId)
                        }
                    }

                    override fun onError(requestId: String, error: ErrorInfo) {
                        launch(Dispatchers.Main) {
                            callback.onError("Upload failed: ${error.description}")
                        }
                    }

                    override fun onReschedule(requestId: String, error: ErrorInfo) {
                        // Not used
                    }
                })
                .dispatch()
        }
    }

    interface DeleteCallback {
        fun onSuccess()
        fun onError(error: String)
    }

    fun delete(publicId: String, callback: DeleteCallback) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = MediaManager.get().destroy(publicId)
                if (result["result"] == "ok") {
                    launch(Dispatchers.Main) {
                        callback.onSuccess()
                    }
                } else {
                    launch(Dispatchers.Main) {
                        callback.onError("Delete failed: ${result["result"]}")
                    }
                }
            } catch (e: Exception) {
                launch(Dispatchers.Main) {
                    callback.onError("Delete failed: ${e.message}")
                }
            }
        }
    }
}
