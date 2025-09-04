package com.synapse.social.studioasinc

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.synapse.social.studioasinc.databinding.ActivityConversationSettingsBinding
import java.util.*

class ConversationSettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConversationSettingsBinding
    private val firebase = FirebaseDatabase.getInstance()
    private lateinit var auth: FirebaseAuth

    private var userAvatarUri = ""
    private var user2nickname = ""

    private lateinit var mainRef: DatabaseReference
    private lateinit var blocklistRef: DatabaseReference

    private var mainChildEventListener: ChildEventListener? = null
    private var blocklistChildEventListener: ChildEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConversationSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        initializeLogic()
    }

    private fun initializeLogic() {
        mainRef = firebase.getReference("skyline")
        blocklistRef = firebase.getReference("skyline/blocklist")

        setupClickListeners()
        setupFirebaseListeners()
        getUserReference()
    }

    private fun setupClickListeners() {
        binding.backBtn.setOnClickListener { finish() }
        binding.encryptedButton.setOnClickListener { /* Handle click */ }
        binding.cardReadReceipt.setOnClickListener { binding.switchReadReceipt.performClick() }
        binding.disappearingMainSwitch.setOnClickListener { binding.switchDisappearingMessages.performClick() }
        binding.savePhotoVideoMainSwitch.setOnClickListener { binding.switchAutoSaveMedia.performClick() }
        binding.blockMainOption.setOnClickListener {
            intent.getStringExtra("uid")?.let { blockUser(it) }
        }
    }

    private fun setupFirebaseListeners() {
        mainChildEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onCancelled(error: DatabaseError) {}
        }
        mainRef.addChildEventListener(mainChildEventListener as ChildEventListener)

        blocklistChildEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onCancelled(error: DatabaseError) {}
        }
        blocklistRef.addChildEventListener(blocklistChildEventListener as ChildEventListener)
    }

    private fun getUserReference() {
        val uid = intent.getStringExtra("uid") ?: return
        val getUserReference = firebase.getReference("skyline/users").child(uid)
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

                    val nickname = dataSnapshot.child("nickname").getValue(String::class.java)
                    val username = dataSnapshot.child("username").getValue(String::class.java)
                    user2nickname = if (nickname != null && nickname != "null") {
                        nickname
                    } else {
                        "@$username"
                    }
                    binding.username.text = user2nickname
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun blockUser(uid: String) {
        val blockData = mapOf(uid to uid)
        auth.currentUser?.uid?.let {
            blocklistRef.child(it).updateChildren(blockData)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainChildEventListener?.let { mainRef.removeEventListener(it) }
        blocklistChildEventListener?.let { blocklistRef.removeEventListener(it) }
    }
}
