package com.synapse.social.studioasinc

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.synapse.social.studioasinc.databinding.ActivityConversationSettingsBinding
import java.util.HashMap

class ConversationSettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConversationSettingsBinding
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val mainRef = firebaseDatabase.getReference("skyline")
    private val blocklistRef = firebaseDatabase.getReference("skyline/blocklist")
    private lateinit var auth: FirebaseAuth

    private var userAvatarUri: String = ""
    private var user2nickname: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            blockUser(intent.getStringExtra("uid"))
        }
    }

    private fun getUserReference() {
        val userId = intent.getStringExtra("uid") ?: return
        val getUserReference = firebaseDatabase.getReference("skyline/users").child(userId)

        getUserReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val isBanned = dataSnapshot.child("banned").getValue(String::class.java) == "true"
                    if (isBanned) {
                        userAvatarUri = "null"
                        binding.profilePictureIV.setImageResource(R.drawable.banned_avatar)
                    } else {
                        userAvatarUri = dataSnapshot.child("avatar").getValue(String::class.java) ?: "null"
                        if (userAvatarUri == "null") {
                            binding.profilePictureIV.setImageResource(R.drawable.avatar)
                        } else {
                            Glide.with(applicationContext).load(Uri.parse(userAvatarUri)).into(binding.profilePictureIV)
                        }
                    }

                    user2nickname = if (dataSnapshot.child("nickname").getValue(String::class.java) == "null") {
                        "@${dataSnapshot.child("username").getValue(String::class.java)}"
                    } else {
                        dataSnapshot.child("nickname").getValue(String::class.java) ?: ""
                    }
                    binding.username.text = user2nickname
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
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