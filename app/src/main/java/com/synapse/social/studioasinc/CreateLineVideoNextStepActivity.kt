package com.synapse.social.studioasinc

import android.app.Activity
import android.app.ProgressDialog
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class CreateLineVideoNextStepActivity : AppCompatActivity() {

    private var synapseLoadingDialog: ProgressDialog? = null
    private val postSendMap = HashMap<String, Any>()
    private var uniquePostKey = ""

    private lateinit var back: ImageView
    private lateinit var continueButton: Button
    private lateinit var postDescription: EditText

    private lateinit var auth: FirebaseAuth
    private lateinit var appSavedData: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_line_video_next_step)
        initialize()
    }

    private fun initialize() {
        back = findViewById(R.id.back)
        continueButton = findViewById(R.id.continueButton)
        postDescription = findViewById(R.id.postDescription)

        auth = FirebaseAuth.getInstance()
        appSavedData = getSharedPreferences("data", Activity.MODE_PRIVATE)

        back.setOnClickListener { onBackPressed() }

        continueButton.setOnClickListener {
            if (intent.hasExtra("path")) {
                uploadLineVideo(intent.getStringExtra("path")!!)
            }
        }
    }

    private fun uploadLineVideo(path: String) {
        val filePath = FileUtil.convertUriToFilePath(this, Uri.parse(path))
        if (filePath != null) {
            loadingDialog(true)
            uniquePostKey = FirebaseDatabase.getInstance().reference.push().key ?: ""
            FasterCloudinaryUploader.upload(this, filePath, object : FasterCloudinaryUploader.UploaderCallback {
                override fun onProgress(progress: Int) {
                    // Update progress if needed
                }

                override fun onSuccess(url: String, publicId: String) {
                    savePostToDatabase(url)
                }

                override fun onError(error: String) {
                    loadingDialog(false)
                    Toast.makeText(applicationContext, "Upload failed: $error", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Could not get file path from URI", Toast.LENGTH_SHORT).show()
        }
    }

    private fun savePostToDatabase(videoUrl: String) {
        val cc = Calendar.getInstance()
        postSendMap["key"] = uniquePostKey
        postSendMap["uid"] = auth.currentUser!!.uid
        postSendMap["post_type"] = "LINE_VIDEO"
        if (postDescription.text.toString().trim().isNotEmpty()) {
            postSendMap["post_text"] = postDescription.text.toString().trim()
        }
        postSendMap["videoUri"] = videoUrl
        if (!appSavedData.contains("user_region_data") || appSavedData.getString("user_region_data", "") == "none") {
            postSendMap["post_region"] = "none"
        } else {
            postSendMap["post_region"] = appSavedData.getString("user_region_data", "")!!
        }
        postSendMap["publish_date"] = cc.timeInMillis.toString()

        FirebaseDatabase.getInstance().getReference("skyline/line-posts").child(uniquePostKey)
            .updateChildren(postSendMap)
            .addOnCompleteListener { task ->
                loadingDialog(false)
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, getString(R.string.post_publish_success), Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(applicationContext, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun loadingDialog(visibility: Boolean) {
        if (visibility) {
            if (synapseLoadingDialog == null) {
                synapseLoadingDialog = ProgressDialog(this).apply {
                    setCancelable(false)
                    setCanceledOnTouchOutside(false)
                    requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
                    window?.setBackgroundDrawable(android.graphics.drawable.ColorDrawable(Color.TRANSPARENT))
                }
            }
            synapseLoadingDialog?.show()
            synapseLoadingDialog?.setContentView(R.layout.loading_synapse)
        } else {
            synapseLoadingDialog?.dismiss()
        }
    }
}
