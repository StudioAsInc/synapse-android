package com.synapse.social.studioasinc

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Vibrator
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class CreateImagePostNextStepActivity : AppCompatActivity() {

    private var synapseLoadingDialog: ProgressDialog? = null
    private var uniquePostKey = ""
    private val postSendMap = HashMap<String, Any>()
    private var path: String? = null

    private lateinit var main: LinearLayout
    private lateinit var top: LinearLayout
    private lateinit var topSpace: LinearLayout
    private lateinit var scroll: ScrollView
    private lateinit var back: ImageView
    private lateinit var topSpc: LinearLayout
    private lateinit var continueButton: Button
    private lateinit var title: TextView
    private lateinit var scrollBody: LinearLayout
    private lateinit var postInfoTop1: LinearLayout
    private lateinit var topSpace2: LinearLayout
    private lateinit var spc2: LinearLayout
    private lateinit var postSettingsStatistics: LinearLayout
    private lateinit var spc3: LinearLayout
    private lateinit var postSettingsPrivacy: LinearLayout
    private lateinit var spc4: LinearLayout
    private lateinit var cropperImageCard: androidx.cardview.widget.CardView
    private lateinit var postDescription: EditText
    private lateinit var croppedImageView: ImageView
    private lateinit var postSettingsStatisticsTop: LinearLayout
    private lateinit var postSettingsStatisticsMiddle: LinearLayout
    private lateinit var postSettingsStatisticsTopTitle: TextView
    private lateinit var postSettingsStatisticsTopArrow: ImageView
    private lateinit var postSettingsStatisticsHideViews: LinearLayout
    private lateinit var postSettingsStatisticsHideLikes: LinearLayout
    private lateinit var postSettingsStatisticsHideComments: LinearLayout
    private lateinit var postSettingsStatisticsHideViewsSwitch: SwitchCompat
    private lateinit var postSettingsStatisticsHideViewsSubtitle: TextView
    private lateinit var postSettingsStatisticsHideLikesSwitch: SwitchCompat
    private lateinit var postSettingsStatisticsHideLikesSubtitle: TextView
    private lateinit var postSettingsStatisticsHideCommentsSwitch: SwitchCompat
    private lateinit var postSettingsStatisticsHideCommentsSubtitle: TextView
    private lateinit var postSettingsPrivacyTop: LinearLayout
    private lateinit var postSettingsPrivacyMiddle: LinearLayout
    private lateinit var postSettingsPrivacyTopTitle: TextView
    private lateinit var postSettingsPrivacyTopArrow: ImageView
    private lateinit var postSettingsPrivacyHidePost: LinearLayout
    private lateinit var postSettingsPrivacyDisableSave: LinearLayout
    private lateinit var postSettingsPrivacyDisableComments: LinearLayout
    private lateinit var postSettingsPrivacyHidePostSwitch: SwitchCompat
    private lateinit var postSettingsPrivacyHidePostSubtitle: TextView
    private lateinit var postSettingsPrivacyDisableSaveSwitch: SwitchCompat
    private lateinit var postSettingsPrivacyDisableSaveSubtitle: TextView
    private lateinit var postSettingsPrivacyDisableCommentsSwitch: SwitchCompat
    private lateinit var postSettingsPrivacyDisableCommentsSubtitle: TextView

    private lateinit var vbr: Vibrator
    private lateinit var auth: FirebaseAuth
    private lateinit var appSavedData: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_image_post_next_step)
        initialize(savedInstanceState)
        initializeLogic()
    }

    private fun initialize(savedInstanceState: Bundle?) {
        main = findViewById(R.id.main)
        top = findViewById(R.id.top)
        topSpace = findViewById(R.id.topSpace)
        scroll = findViewById(R.id.scroll)
        back = findViewById(R.id.back)
        topSpc = findViewById(R.id.topSpc)
        continueButton = findViewById(R.id.continueButton)
        title = findViewById(R.id.title)
        scrollBody = findViewById(R.id.scrollBody)
        postInfoTop1 = findViewById(R.id.PostInfoTop1)
        topSpace2 = findViewById(R.id.topSpace2)
        spc2 = findViewById(R.id.spc2)
        postSettingsStatistics = findViewById(R.id.post_settings_statistics)
        spc3 = findViewById(R.id.spc3)
        postSettingsPrivacy = findViewById(R.id.post_settings_privacy)
        spc4 = findViewById(R.id.spc4)
        cropperImageCard = findViewById(R.id.cropperImageCard)
        postDescription = findViewById(R.id.postDescription)
        croppedImageView = findViewById(R.id.croppedImageView)
        postSettingsStatisticsTop = findViewById(R.id.post_settings_statistics_top)
        postSettingsStatisticsMiddle = findViewById(R.id.post_settings_statistics_middle)
        postSettingsStatisticsTopTitle = findViewById(R.id.post_settings_statistics_top_title)
        postSettingsStatisticsTopArrow = findViewById(R.id.post_settings_statistics_top_arrow)
        postSettingsStatisticsHideViews = findViewById(R.id.post_settings_statistics_hide_views)
        postSettingsStatisticsHideLikes = findViewById(R.id.post_settings_statistics_hide_likes)
        postSettingsStatisticsHideComments = findViewById(R.id.post_settings_statistics_hide_comments)
        postSettingsStatisticsHideViewsSwitch = findViewById(R.id.post_settings_statistics_hide_views_switch)
        postSettingsStatisticsHideViewsSubtitle = findViewById(R.id.post_settings_statistics_hide_views_subtitle)
        postSettingsStatisticsHideLikesSwitch = findViewById(R.id.post_settings_statistics_hide_likes_switch)
        postSettingsStatisticsHideLikesSubtitle = findViewById(R.id.post_settings_statistics_hide_likes_subtitle)
        postSettingsStatisticsHideCommentsSwitch = findViewById(R.id.post_settings_statistics_hide_comments_switch)
        postSettingsStatisticsHideCommentsSubtitle = findViewById(R.id.post_settings_statistics_hide_comments_subtitle)
        postSettingsPrivacyTop = findViewById(R.id.post_settings_privacy_top)
        postSettingsPrivacyMiddle = findViewById(R.id.post_settings_privacy_middle)
        postSettingsPrivacyTopTitle = findViewById(R.id.post_settings_privacy_top_title)
        postSettingsPrivacyTopArrow = findViewById(R.id.post_settings_privacy_top_arrow)
        postSettingsPrivacyHidePost = findViewById(R.id.post_settings_privacy_hide_post)
        postSettingsPrivacyDisableSave = findViewById(R.id.post_settings_privacy_disable_save)
        postSettingsPrivacyDisableComments = findViewById(R.id.post_settings_privacy_disable_comments)
        postSettingsPrivacyHidePostSwitch = findViewById(R.id.post_settings_privacy_hide_post_switch)
        postSettingsPrivacyHidePostSubtitle = findViewById(R.id.post_settings_privacy_hide_post_subtitle)
        postSettingsPrivacyDisableSaveSwitch = findViewById(R.id.post_settings_privacy_disable_save_switch)
        postSettingsPrivacyDisableSaveSubtitle = findViewById(R.id.post_settings_privacy_disable_save_subtitle)
        postSettingsPrivacyDisableCommentsSwitch = findViewById(R.id.post_settings_privacy_disable_comments_switch)
        postSettingsPrivacyDisableCommentsSubtitle = findViewById(R.id.post_settings_privacy_disable_comments_subtitle)

        vbr = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        auth = FirebaseAuth.getInstance()
        appSavedData = getSharedPreferences("data", Activity.MODE_PRIVATE)

        back.setOnClickListener { onBackPressed() }

        continueButton.setOnClickListener {
            if (intent.hasExtra("type")) {
                if (intent.getStringExtra("type") == "local") {
                    if (intent.hasExtra("path")) {
                        val uriPath = intent.getStringExtra("path")
                        val filePath = FileUtil.convertUriToFilePath(this, Uri.parse(uriPath))
                        if (filePath != null) {
                            uniquePostKey = FirebaseDatabase.getInstance().reference.push().key ?: ""
                            loadingDialog(true)
                            FasterCloudinaryUploader.upload(this, filePath, object : FasterCloudinaryUploader.UploaderCallback {
                                override fun onProgress(progress: Int) {
                                    // You can update a progress bar here
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
                            Toast.makeText(applicationContext, "Could not get file path from URI", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    if (intent.hasExtra("path")) {
                        uniquePostKey = FirebaseDatabase.getInstance().reference.push().key ?: ""
                        loadingDialog(true)
                        savePostToDatabase(intent.getStringExtra("path")!!)
                    }
                }
            }
        }

        postSettingsStatisticsTop.setOnClickListener {
            if (postSettingsStatisticsMiddle.visibility == View.GONE) {
                postSettingsStatisticsMiddle.visibility = View.VISIBLE
                postSettingsStatisticsTopArrow.setImageResource(R.drawable.ic_expand_less_black)
            } else {
                postSettingsStatisticsMiddle.visibility = View.GONE
                postSettingsStatisticsTopArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_black)
            }
        }

        postSettingsPrivacyTop.setOnClickListener {
            if (postSettingsPrivacyMiddle.visibility == View.GONE) {
                postSettingsPrivacyMiddle.visibility = View.VISIBLE
                postSettingsPrivacyTopArrow.setImageResource(R.drawable.ic_expand_less_black)
            } else {
                postSettingsPrivacyMiddle.visibility = View.GONE
                postSettingsPrivacyTopArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_black)
            }
        }
    }

    private fun savePostToDatabase(imageUrl: String) {
        val cc = Calendar.getInstance()
        postSendMap["key"] = uniquePostKey
        postSendMap["uid"] = auth.currentUser!!.uid
        postSendMap["post_type"] = "IMAGE"
        if (postDescription.text.toString().trim().isNotEmpty()) {
            postSendMap["post_text"] = postDescription.text.toString().trim()
        }
        postSendMap["post_image"] = imageUrl
        if (!appSavedData.contains("user_region_data") || appSavedData.getString("user_region_data", "") == "none") {
            postSendMap["post_region"] = "none"
        } else {
            postSendMap["post_region"] = appSavedData.getString("user_region_data", "")!!
        }
        postSendMap["post_hide_views_count"] = postSettingsStatisticsHideViewsSwitch.isChecked.toString()
        postSendMap["post_hide_like_count"] = postSettingsStatisticsHideLikesSwitch.isChecked.toString()
        postSendMap["post_hide_comments_count"] = postSettingsStatisticsHideCommentsSwitch.isChecked.toString()
        postSendMap["post_visibility"] = if (postSettingsPrivacyHidePostSwitch.isChecked) "private" else "public"
        postSendMap["post_disable_favorite"] = postSettingsPrivacyDisableSaveSwitch.isChecked.toString()
        postSendMap["post_disable_comments"] = postSettingsPrivacyDisableCommentsSwitch.isChecked.toString()
        postSendMap["publish_date"] = cc.timeInMillis.toString()

        FirebaseDatabase.getInstance().getReference("skyline/posts").child(uniquePostKey).updateChildren(postSendMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, getString(R.string.post_publish_success), Toast.LENGTH_SHORT).show()
                    loadingDialog(false)
                    finish()
                } else {
                    Toast.makeText(applicationContext, task.exception?.message, Toast.LENGTH_SHORT).show()
                    loadingDialog(false)
                }
            }
    }

    private fun initializeLogic() {
        setStatusBarColor(true, Color.WHITE, Color.WHITE)
        viewGraphics(back, Color.WHITE, 0xFFE0E0E0.toInt(), 300f, 0f, Color.TRANSPARENT)
        cropperImageCard.background = GradientDrawable().apply {
            cornerRadius = 22f
            setStroke(2, 0xFFEEEEEE.toInt())
            setColor(Color.WHITE)
        }
        postDescription.background = GradientDrawable().apply {
            cornerRadius = 28f
            setStroke(3, 0xFFEEEEEE.toInt())
            setColor(Color.WHITE)
        }
        postSettingsStatisticsMiddle.visibility = View.GONE
        postSettingsPrivacyMiddle.visibility = View.GONE

        path = intent.getStringExtra("path")
        val type = intent.getStringExtra("type")

        if (type == "local") {
            Glide.with(applicationContext).load(Uri.parse(path)).into(croppedImageView)
        } else {
            Glide.with(applicationContext).load(path).into(croppedImageView)
        }
    }

    override fun onBackPressed() {
        finish()
    }

    private fun setStatusBarColor(isLight: Boolean, stateColor: Int, navigationColor: Int) {
        if (isLight) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        window.statusBarColor = stateColor
        window.navigationBarColor = navigationColor
    }

    private fun viewGraphics(view: View, onFocus: Int, onRipple: Int, radius: Float, stroke: Float, strokeColor: Int) {
        val gg = GradientDrawable()
        gg.setColor(onFocus)
        gg.cornerRadius = radius
        gg.setStroke(stroke.toInt(), strokeColor)
        val re = android.graphics.drawable.RippleDrawable(
            android.content.res.ColorStateList(arrayOf(intArrayOf()), intArrayOf(onRipple)),
            gg,
            null
        )
        view.background = re
    }

    private fun loadingDialog(visibility: Boolean) {
        if (visibility) {
            if (synapseLoadingDialog == null) {
                synapseLoadingDialog = ProgressDialog(this)
                synapseLoadingDialog?.setCancelable(false)
                synapseLoadingDialog?.setCanceledOnTouchOutside(false)
                synapseLoadingDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
                synapseLoadingDialog?.window?.setBackgroundDrawable(android.graphics.drawable.ColorDrawable(Color.TRANSPARENT))
            }
            synapseLoadingDialog?.show()
            synapseLoadingDialog?.setContentView(R.layout.loading_synapse)
        } else {
            synapseLoadingDialog?.dismiss()
        }
    }
}
