package com.synapse.social.studioasinc

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.synapse.social.studioasinc.databinding.ActivityChatSettingsBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ChatSettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatSettingsBinding
    private val viewModel: ChatSettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.getStringExtra("uid")
        if (userId == null) {
            finish() // or show an error
            return
        }

        setupToolbar()
        setupClickListeners(userId)
        observeUiState()
        setupAnimations()

        viewModel.loadUserSettings(userId)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun setupClickListeners(userId: String) {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.blockMainOption.setOnClickListener {
            viewModel.blockUser(userId)
        }

        binding.readReceiptsMainSwitch.setOnClickListener {
            binding.switchReadReceipt.performClick()
        }

        binding.disappearingMainSwitch.setOnClickListener {
            binding.switchDisappearingMessages.performClick()
        }

        binding.savePhotoVideoMainSwitch.setOnClickListener {
            binding.switchAutoSaveMedia.performClick()
        }

        binding.switchReadReceipt.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onReadReceiptsChanged(isChecked)
        }

        binding.switchDisappearingMessages.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onDisappearingMessagesChanged(isChecked)
        }

        binding.switchAutoSaveMedia.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onAutoSaveMediaChanged(isChecked)
        }
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                // Update UI with the new state
                binding.username.text = state.nickname
                binding.collapsingToolbar.title = state.nickname // Show nickname in collapsed toolbar

                if (state.userAvatarUrl != null) {
                    Glide.with(this@ChatSettingsActivity)
                        .load(state.userAvatarUrl)
                        .into(binding.profilePictureIV)
                } else {
                    binding.profilePictureIV.setImageResource(R.drawable.avatar)
                }

                // Update other UI elements based on the state
                binding.switchReadReceipt.isChecked = state.readReceiptsEnabled
                binding.switchDisappearingMessages.isChecked = state.disappearingMessagesEnabled
                binding.switchAutoSaveMedia.isChecked = state.autoSaveMediaEnabled

                if (state.isBlocked) {
                    // Update UI to show user is blocked
                }
            }
        }
    }

    private fun setupAnimations() {
        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            // The CollapsingToolbarLayout handles the main collapse animation.
            // We can add more subtle animations here if needed.
            // For example, we can fade the username text as it collapses.
            val percentage = Math.abs(verticalOffset).toFloat() / appBarLayout.totalScrollRange
            binding.username.alpha = 1.0f - percentage * 2 // Fade out faster
        })
    }
}
