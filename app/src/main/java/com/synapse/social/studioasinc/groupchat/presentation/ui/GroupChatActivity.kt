package com.synapse.social.studioasinc.groupchat.presentation.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.synapse.social.studioasinc.R
import com.synapse.social.studioasinc.databinding.ActivityGroupChatBinding
import com.synapse.social.studioasinc.groupchat.data.model.Group
import com.synapse.social.studioasinc.groupchat.data.model.GroupMessage
import com.synapse.social.studioasinc.groupchat.presentation.adapter.GroupMessagesAdapter
import com.synapse.social.studioasinc.groupchat.presentation.viewmodel.GroupChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class GroupChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGroupChatBinding
    private val viewModel: GroupChatViewModel by viewModels()
    private lateinit var messagesAdapter: GroupMessagesAdapter
    private var selectedAttachments = mutableListOf<Uri>()

    private val attachmentPickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        selectedAttachments.addAll(uris)
        updateAttachmentPreview()
    }

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Handle camera result
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityGroupChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val groupId = intent.getStringExtra("GROUP_ID") ?: return finish()
        val groupName = intent.getStringExtra("GROUP_NAME") ?: "Group Chat"
        
        setupWindowInsets()
        setupUI(groupName)
        setupObservers()
        
        viewModel.initialize(groupId)
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            
            // Adjust input area for keyboard
            binding.inputContainer.setPadding(0, 0, 0, maxOf(systemBars.bottom, imeInsets.bottom))
            
            insets
        }
    }

    private fun setupUI(groupName: String) {
        setupToolbar(groupName)
        setupRecyclerView()
        setupInputArea()
        setupClickListeners()
    }

    private fun setupToolbar(groupName: String) {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = groupName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        // Setup group info click
        binding.toolbar.setOnClickListener {
            // Open group info activity
        }
    }

    private fun setupRecyclerView() {
        messagesAdapter = GroupMessagesAdapter(
            onMessageClick = { message ->
                // Handle message click
            },
            onMessageLongClick = { message ->
                showMessageOptions(message)
            },
            onReactionClick = { message, emoji ->
                viewModel.addReaction(message.id, emoji)
            },
            onReplyClick = { message ->
                startReply(message)
            },
            onEditClick = { message ->
                startEdit(message)
            },
            onDeleteClick = { message ->
                showDeleteConfirmation(message)
            },
            canUserEditMessage = { message ->
                viewModel.canUserEditMessage(message)
            },
            canUserDeleteMessage = { message ->
                viewModel.canUserDeleteMessage(message)
            }
        )

        binding.rvMessages.apply {
            layoutManager = LinearLayoutManager(this@GroupChatActivity).apply {
                reverseLayout = true
                stackFromEnd = true
            }
            adapter = messagesAdapter
        }
    }

    private fun setupInputArea() {
        binding.etMessage.addTextChangedListener(
            onTextChanged = { _, _, _, _ ->
                updateSendButtonState()
                viewModel.startTyping()
            },
            afterTextChanged = {
                if (it.isNullOrEmpty()) {
                    viewModel.stopTyping()
                }
            }
        )
    }

    private fun setupClickListeners() {
        binding.btnSend.setOnClickListener {
            sendMessage()
        }

        binding.btnAttachment.setOnClickListener {
            showAttachmentOptions()
        }

        binding.btnEmoji.setOnClickListener {
            // Show emoji picker
        }

        binding.btnVoiceNote.setOnClickListener {
            // Start voice recording
        }

        // Clear attachments
        binding.btnClearAttachments.setOnClickListener {
            clearAttachments()
        }
    }

    private fun setupObservers() {
        // Observe messages
        lifecycleScope.launch {
            viewModel.messagesPagingData.collect { pagingData ->
                messagesAdapter.submitData(pagingData)
            }
        }

        // Observe UI state
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                updateUI(state)
            }
        }

        // Observe group info
        lifecycleScope.launch {
            viewModel.groupFlow.collect { group ->
                group?.let { updateGroupInfo(it) }
            }
        }

        // Observe group members
        lifecycleScope.launch {
            viewModel.groupMembersFlow.collect { members ->
                updateMemberInfo(members.size)
            }
        }
    }

    private fun updateUI(state: com.synapse.social.studioasinc.groupchat.presentation.viewmodel.GroupChatUiState) {
        // Update loading state
        binding.progressBar.visibility = if (state.isLoading) {
            android.view.View.VISIBLE
        } else {
            android.view.View.GONE
        }

        // Update input area based on permissions
        binding.inputContainer.visibility = if (state.canSendMessages) {
            android.view.View.VISIBLE
        } else {
            android.view.View.GONE
        }

        // Show upload progress
        binding.progressBarUpload.visibility = if (state.isUploadingAttachment) {
            android.view.View.VISIBLE
        } else {
            android.view.View.GONE
        }

        // Handle errors
        state.error?.let { error ->
            Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG)
                .setAction("Retry") { 
                    viewModel.retryFailedMessages()
                }
                .show()
            viewModel.clearError()
        }

        // Update search results
        if (state.searchResults.isNotEmpty()) {
            // Show search results
        }
    }

    private fun updateGroupInfo(group: Group) {
        supportActionBar?.title = group.getDisplayName()
        supportActionBar?.subtitle = "${group.memberCount} members"
    }

    private fun updateMemberInfo(memberCount: Int) {
        supportActionBar?.subtitle = "$memberCount members"
    }

    private fun sendMessage() {
        val messageText = binding.etMessage.text.toString()
        
        if (selectedAttachments.isNotEmpty()) {
            // Send with attachments
            val attachmentFiles = selectedAttachments.mapNotNull { uri ->
                uriToFile(uri)
            }
            viewModel.sendMessageWithAttachments(messageText, attachmentFiles)
            clearAttachments()
        } else if (messageText.isNotEmpty()) {
            // Send text message
            viewModel.sendMessage(messageText)
        }
        
        binding.etMessage.text?.clear()
    }

    private fun showAttachmentOptions() {
        val options = arrayOf("Camera", "Gallery", "Documents", "Audio")
        
        MaterialAlertDialogBuilder(this)
            .setTitle("Add Attachment")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openCamera()
                    1 -> openGallery()
                    2 -> openDocuments()
                    3 -> openAudioPicker()
                }
            }
            .show()
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }

    private fun openGallery() {
        attachmentPickerLauncher.launch("image/*")
    }

    private fun openDocuments() {
        attachmentPickerLauncher.launch("*/*")
    }

    private fun openAudioPicker() {
        attachmentPickerLauncher.launch("audio/*")
    }

    private fun updateAttachmentPreview() {
        if (selectedAttachments.isNotEmpty()) {
            binding.attachmentPreview.visibility = android.view.View.VISIBLE
            binding.tvAttachmentCount.text = "${selectedAttachments.size} file(s) selected"
        } else {
            binding.attachmentPreview.visibility = android.view.View.GONE
        }
        updateSendButtonState()
    }

    private fun clearAttachments() {
        selectedAttachments.clear()
        updateAttachmentPreview()
    }

    private fun updateSendButtonState() {
        val hasText = binding.etMessage.text.toString().isNotEmpty()
        val hasAttachments = selectedAttachments.isNotEmpty()
        
        binding.btnSend.isEnabled = hasText || hasAttachments
        binding.btnSend.alpha = if (binding.btnSend.isEnabled) 1.0f else 0.5f
    }

    private fun showMessageOptions(message: GroupMessage) {
        val options = mutableListOf<String>()
        
        if (viewModel.canUserEditMessage(message)) {
            options.add("Edit")
        }
        options.addAll(listOf("Reply", "React", "Copy"))
        if (viewModel.canUserDeleteMessage(message)) {
            options.add("Delete")
        }

        MaterialAlertDialogBuilder(this)
            .setTitle("Message Options")
            .setItems(options.toTypedArray()) { _, which ->
                when (options[which]) {
                    "Edit" -> startEdit(message)
                    "Reply" -> startReply(message)
                    "React" -> showReactionPicker(message)
                    "Copy" -> copyMessageToClipboard(message)
                    "Delete" -> showDeleteConfirmation(message)
                }
            }
            .show()
    }

    private fun startEdit(message: GroupMessage) {
        binding.etMessage.setText(message.text)
        binding.etMessage.requestFocus()
        // Show edit mode UI
    }

    private fun startReply(message: GroupMessage) {
        // Show reply preview and focus input
        binding.etMessage.requestFocus()
    }

    private fun showReactionPicker(message: GroupMessage) {
        val emojis = arrayOf("👍", "❤️", "😂", "😮", "😢", "😡")
        
        MaterialAlertDialogBuilder(this)
            .setTitle("Add Reaction")
            .setItems(emojis) { _, which ->
                viewModel.addReaction(message.id, emojis[which])
            }
            .show()
    }

    private fun copyMessageToClipboard(message: GroupMessage) {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = android.content.ClipData.newPlainText("Message", message.text)
        clipboard.setPrimaryClip(clip)
        
        Snackbar.make(binding.root, "Message copied", Snackbar.LENGTH_SHORT).show()
    }

    private fun showDeleteConfirmation(message: GroupMessage) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Delete Message")
            .setMessage("Are you sure you want to delete this message?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteMessage(message.id)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun uriToFile(uri: Uri): File? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val tempFile = File.createTempFile("attachment", ".tmp", cacheDir)
            tempFile.outputStream().use { outputStream ->
                inputStream?.copyTo(outputStream)
            }
            tempFile
        } catch (e: Exception) {
            null
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}