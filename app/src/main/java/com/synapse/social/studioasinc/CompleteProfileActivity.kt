package com.synapse.social.studioasinc

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import java.util.*

class CompleteProfileActivity : AppCompatActivity() {

    private var userNameErr = false
    private var avatarUri: String? = "null"
    private var thedpurl: String? = "null"
    private val createUserMap = HashMap<String, Any>()

    private lateinit var scroll: ScrollView
    private lateinit var body: LinearLayout
    private lateinit var top: LinearLayout
    private lateinit var title: TextView
    private lateinit var subtitle: TextView
    private lateinit var profileImageCard: androidx.cardview.widget.CardView
    private lateinit var usernameInput: EditText
    private lateinit var nicknameInput: EditText
    private lateinit var biographyInput: EditText
    private lateinit var emailVerification: LinearLayout
    private lateinit var buttons: LinearLayout
    private lateinit var back: ImageView
    private lateinit var topMiddle: LinearLayout
    private lateinit var cancelCreateAccount: ImageView
    private lateinit var cancelCreateAccountProgress: ProgressBar
    private lateinit var profileImage: ImageView
    private lateinit var emailVerificationTitle: TextView
    private lateinit var emailVerificationSubtitle: TextView
    private lateinit var emailVerificationMiddle: LinearLayout
    private lateinit var emailVerificationSend: TextView
    private lateinit var emailVerificationErrorIc: ImageView
    private lateinit var emailVerificationVerifiedIc: ImageView
    private lateinit var emailVerificationStatus: TextView
    private lateinit var emailVerificationStatusRefresh: ImageView
    private lateinit var skipButton: TextView
    private lateinit var completeButton: LinearLayout
    private lateinit var completeButtonTitle: TextView
    private lateinit var completeButtonLoaderBar: ProgressBar

    private lateinit var vbr: Vibrator
    private lateinit var auth: FirebaseAuth
    private lateinit var filePicker: FilePicker
    private var synapseLoadingDialog: ProgressDialog? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your app.
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.complete_profile)
        initialize(savedInstanceState)
        initializeLogic()
    }

    private fun initialize(savedInstanceState: Bundle?) {
        scroll = findViewById(R.id.scroll)
        body = findViewById(R.id.body)
        top = findViewById(R.id.top)
        title = findViewById(R.id.title)
        subtitle = findViewById(R.id.subtitle)
        profileImageCard = findViewById(R.id.profile_image_card)
        usernameInput = findViewById(R.id.username_input)
        nicknameInput = findViewById(R.id.nickname_input)
        biographyInput = findViewById(R.id.biography_input)
        emailVerification = findViewById(R.id.email_verification)
        buttons = findViewById(R.id.buttons)
        back = findViewById(R.id.back)
        topMiddle = findViewById(R.id.topMiddle)
        cancelCreateAccount = findViewById(R.id.cancelCreateAccount)
        cancelCreateAccountProgress = findViewById(R.id.cancel_create_account_progress)
        profileImage = findViewById(R.id.profile_image)
        emailVerificationTitle = findViewById(R.id.email_verification_title)
        emailVerificationSubtitle = findViewById(R.id.email_verification_subtitle)
        emailVerificationMiddle = findViewById(R.id.email_verification_middle)
        emailVerificationSend = findViewById(R.id.email_verification_send)
        emailVerificationErrorIc = findViewById(R.id.email_verification_error_ic)
        emailVerificationVerifiedIc = findViewById(R.id.email_verification_verified_ic)
        emailVerificationStatus = findViewById(R.id.email_verification_status)
        emailVerificationStatusRefresh = findViewById(R.id.email_verification_status_refresh)
        skipButton = findViewById(R.id.skip_button)
        completeButton = findViewById(R.id.complete_button)
        completeButtonTitle = findViewById(R.id.complete_button_title)
        completeButtonLoaderBar = findViewById(R.id.complete_button_loader_bar)

        vbr = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        auth = FirebaseAuth.getInstance()

        filePicker = FilePicker(activityResultRegistry, this) { uri ->
            if (uri != null) {
                avatarUri = uri.toString()
                Glide.with(this).load(uri).into(profileImage)
            }
        }

        profileImageCard.setOnLongClickListener {
            avatarUri = "null"
            profileImage.setImageResource(R.drawable.avatar)
            vbr.vibrate(48)
            true
        }

        profileImageCard.setOnClickListener {
            checkPermissionsAndPickImage()
        }

        usernameInput.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                validateUsername(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })

        completeButton.setOnClickListener {
            if (userNameErr) {
                Toast.makeText(applicationContext, getString(R.string.username_err_invalid), Toast.LENGTH_SHORT).show()
                vbr.vibrate(48)
            } else {
                pushData()
            }
        }
    }

    private fun checkPermissionsAndPickImage() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            filePicker.pickFile("image/*")
        } else {
            requestPermissionLauncher.launch(permission)
        }
    }

    private fun validateUsername(username: String) {
        if (username.trim().isEmpty()) {
            usernameInput.error = getString(R.string.enter_username)
            userNameErr = true
        } else if (!username.matches("[a-z0-9_.]+".toRegex())) {
            usernameInput.error = getString(R.string.username_err_invalid_characters)
            userNameErr = true
        } else if (username.length < 3) {
            usernameInput.error = getString(R.string.username_err_3_characters)
            userNameErr = true
        } else if (username.length > 25) {
            usernameInput.error = getString(R.string.username_err_25_characters)
            userNameErr = true
        } else {
            val checkUsernameRef = FirebaseDatabase.getInstance().getReference("skyline/users")
            val checkUsernameQuery: Query = checkUsernameRef.orderByChild("username").equalTo(username.trim())
            checkUsernameQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        usernameInput.error = getString(R.string.username_err_already_taken)
                        userNameErr = true
                    } else {
                        usernameInput.error = null
                        userNameErr = false
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
    }

    private fun pushData() {
        loadingDialog(true)
        if (avatarUri != "null" && avatarUri != null) {
            val filePath = FileUtil.convertUriToFilePath(this, Uri.parse(avatarUri))
            if (filePath != null) {
                FasterCloudinaryUploader.upload(this, filePath, object : FasterCloudinaryUploader.UploaderCallback {
                    override fun onProgress(progress: Int) {
                        // Update progress if needed
                    }

                    override fun onSuccess(url: String, publicId: String) {
                        thedpurl = url
                        saveUserProfile()
                    }

                    override fun onError(error: String) {
                        loadingDialog(false)
                        Toast.makeText(applicationContext, "Upload failed: $error", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                loadingDialog(false)
                Toast.makeText(this, "Could not get file path from URI", Toast.LENGTH_SHORT).show()
            }
        } else {
            saveUserProfile()
        }
    }

    private fun saveUserProfile() {
        val getJoinTime = Calendar.getInstance()
        createUserMap["uid"] = auth.currentUser!!.uid
        createUserMap["email"] = auth.currentUser!!.email!!
        createUserMap["profile_cover_image"] = "null"
        createUserMap["avatar"] = thedpurl ?: "null"
        createUserMap["avatar_history_type"] = "local"
        createUserMap["username"] = usernameInput.text.toString().trim()
        createUserMap["nickname"] = if (nicknameInput.text.toString().trim().isEmpty()) "null" else nicknameInput.text.toString().trim()
        createUserMap["biography"] = if (biographyInput.text.toString().trim().isEmpty()) "null" else biographyInput.text.toString().trim()
        createUserMap["verify"] = "false"
        createUserMap["account_type"] = "user"
        createUserMap["account_premium"] = "false"
        createUserMap["banned"] = "false"
        createUserMap["gender"] = "hidden"
        createUserMap["status"] = "online"
        createUserMap["join_date"] = getJoinTime.timeInMillis.toString()

        val mainDb = FirebaseDatabase.getInstance().getReference("skyline")
        mainDb.child("users").child(auth.currentUser!!.uid).updateChildren(createUserMap)
            .addOnCompleteListener { task ->
                loadingDialog(false)
                if (task.isSuccessful) {
                    val intent = Intent(applicationContext, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(applicationContext, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun initializeLogic() {
        emailVerificationTitle.typeface = Typeface.DEFAULT_BOLD
        subtitle.typeface = Typeface.DEFAULT
        title.typeface = Typeface.DEFAULT_BOLD
        stateColor(Color.WHITE, Color.WHITE)
        avatarUri = "null"
        thedpurl = "null"
        userNameErr = true
        viewGraphics(back, Color.WHITE, 0xFFEEEEEE.toInt(), 300.0, 0.0, Color.TRANSPARENT)
        viewGraphics(cancelCreateAccount, Color.WHITE, 0xFFFFCDD2.toInt(), 300.0, 0.0, Color.TRANSPARENT)
        // ... rest of the graphics initialization
    }

    private fun loadingDialog(visibility: Boolean) {
        if (visibility) {
            if (synapseLoadingDialog == null) {
                synapseLoadingDialog = ProgressDialog(this).apply {
                    setCancelable(false)
                    setCanceledOnTouchOutside(false)
                    requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
                    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                }
            }
            synapseLoadingDialog?.show()
            synapseLoadingDialog?.setContentView(R.layout.loading_synapse)
        } else {
            synapseLoadingDialog?.dismiss()
        }
    }

    private fun stateColor(statusColor: Int, navigationColor: Int) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = statusColor
        window.navigationBarColor = navigationColor
    }

    private fun viewGraphics(view: View, onFocus: Int, onRipple: Int, radius: Double, stroke: Double, strokeColor: Int) {
        val gg = GradientDrawable()
        gg.setColor(onFocus)
        gg.cornerRadius = radius.toFloat()
        gg.setStroke(stroke.toInt(), strokeColor)
        val re = android.graphics.drawable.RippleDrawable(
            android.content.res.ColorStateList(arrayOf(intArrayOf()), intArrayOf(onRipple)),
            gg,
            null
        )
        view.background = re
    }
}
