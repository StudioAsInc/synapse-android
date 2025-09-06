package com.synapse.social.studioasinc

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.synapse.social.studioasinc.databinding.ActivityConversationSettingsBinding
import java.util.HashMap

class ConversationSettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConversationSettingsBinding
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val blocklistRef = firebaseDatabase.getReference(REF_SKYLINE).child(REF_BLOCKLIST)
    private lateinit var auth: FirebaseAuth

    companion object {
        private const val REF_SKYLINE = "skyline"
        private const val REF_USERS = "users"
        private const val REF_BLOCKLIST = "blocklist"
        private const val KEY_UID = "uid"
        private const val KEY_BANNED = "banned"
        private const val KEY_AVATAR = "avatar"
        private const val KEY_NICKNAME = "nickname"
        private const val KEY_USERNAME = "username"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityConversationSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        initializeLogic()
    }

    private fun initializeLogic() {
        getUserReference()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.button1.setOnClickListener {
            // TODO: Implement action
        }

        binding.cardReadReceipt.setOnClickListener {
            binding.switchReadReceipt.performClick()
        }

        binding.disappearingMainSwitch.setOnClickListener {
            binding.switchDisappearingMessages.performClick()
        }

        binding.savePhotoVideoMainSwitch.setOnClickListener {
            binding.switchAutoSaveMedia.performClick()
        }

        binding.blockMainOption.setOnClickListener {
            blockUser(intent.getStringExtra(KEY_UID))
        }
    }

    private fun getUserReference() {
        val userId = intent.getStringExtra(KEY_UID) ?: return
        val getUserReference = firebaseDatabase.getReference(REF_SKYLINE).child(REF_USERS).child(userId)

        getUserReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val isBanned = dataSnapshot.child(KEY_BANNED).getValue(String::class.java) == "true"
                    if (isBanned) {
                        binding.profilePictureIV.setImageResource(R.drawable.banned_avatar)
                    } else {
                        val avatarUrl = dataSnapshot.child(KEY_AVATAR).getValue(String::class.java)
                        if (avatarUrl.isNullOrEmpty() || avatarUrl == "null") {
                            binding.profilePictureIV.setImageResource(R.drawable.avatar)
                        } else {
                            Glide.with(applicationContext).load(Uri.parse(avatarUrl)).into(binding.profilePictureIV)
                        }
                    }

                    val nickname = dataSnapshot.child(KEY_NICKNAME).getValue(String::class.java)
                    val username = dataSnapshot.child(KEY_USERNAME).getValue(String::class.java)

                    val user2nickname: String = if (nickname.isNullOrEmpty() || nickname == "null") {
                        if (username.isNullOrEmpty()) "" else "@$username"
                    } else {
                        nickname
                    }
                    binding.username.text = user2nickname
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("ConversationSettings", "Database error: ${databaseError.message}")
            }
        })
    }

    private fun blockUser(uid: String?) {
        uid?.let {
            val blockData = hashMapOf<String, Any>(it to it)
            auth.currentUser?.uid?.let { currentUserUid ->
                blocklistRef.child(currentUserUid).updateChildren(blockData)
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }
}