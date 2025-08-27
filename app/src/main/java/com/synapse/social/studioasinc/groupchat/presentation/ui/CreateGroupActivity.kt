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
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.synapse.social.studioasinc.R
import com.synapse.social.studioasinc.databinding.ActivityCreateGroupBinding
import com.synapse.social.studioasinc.groupchat.data.model.User
import com.synapse.social.studioasinc.groupchat.data.model.UserRole
import com.synapse.social.studioasinc.groupchat.presentation.adapter.UserSearchAdapter
import com.synapse.social.studioasinc.groupchat.presentation.viewmodel.CreateGroupViewModel
import com.synapse.social.studioasinc.groupchat.presentation.viewmodel.CreateGroupUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class CreateGroupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateGroupBinding
    private val viewModel: CreateGroupViewModel by viewModels()
    private lateinit var userSearchAdapter: UserSearchAdapter
    private var selectedImageUri: Uri? = null

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            selectedImageUri = it
            loadGroupIcon(it)
            // Convert URI to File for ViewModel
            val file = uriToFile(it)
            viewModel.updateGroupIcon(file)
        }
    }

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedImageUri?.let {
                loadGroupIcon(it)
                val file = uriToFile(it)
                viewModel.updateGroupIcon(file)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityCreateGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupWindowInsets()
        setupUI()
        setupObservers()
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupUI() {
        setupToolbar()
        setupForm()
        setupRecyclerView()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Create Group"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupForm() {
        // Group name input
        binding.etGroupName.addTextChangedListener { text ->
            viewModel.updateGroupName(text.toString())
        }

        // Group description input
        binding.etGroupDescription.addTextChangedListener { text ->
            viewModel.updateGroupDescription(text.toString())
        }

        // Member search input
        binding.etSearchMembers.addTextChangedListener { text ->
            viewModel.searchUsers(text.toString())
        }

        // Max members slider
        binding.sliderMaxMembers.addOnChangeListener { _, value, _ ->
            viewModel.updateMaxMembers(value.toInt())
            binding.tvMaxMembersValue.text = "${value.toInt()} members"
        }

        // Group settings switches
        binding.switchPrivateGroup.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateIsPrivate(isChecked)
        }

        binding.switchAllowMembersToAdd.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleGroupSetting("allowMembersToAddOthers")
        }

        binding.switchAllowMembersToEdit.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleGroupSetting("allowMembersToEditInfo")
        }

        binding.switchOnlyAdminsMessage.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleGroupSetting("onlyAdminsCanMessage")
        }
    }

    private fun setupRecyclerView() {
        userSearchAdapter = UserSearchAdapter(
            onUserClick = { user ->
                viewModel.addMember(user)
                binding.etSearchMembers.text?.clear()
            }
        )
        
        binding.rvSearchResults.apply {
            layoutManager = LinearLayoutManager(this@CreateGroupActivity)
            adapter = userSearchAdapter
        }
    }

    private fun setupClickListeners() {
        // Group icon selection
        binding.ivGroupIcon.setOnClickListener {
            showImagePickerDialog()
        }

        binding.btnSelectIcon.setOnClickListener {
            showImagePickerDialog()
        }

        // Create group button
        binding.btnCreateGroup.setOnClickListener {
            viewModel.createGroup()
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                updateUI(state)
            }
        }

        lifecycleScope.launch {
            viewModel.searchResults.collect { users ->
                userSearchAdapter.submitList(users)
                binding.rvSearchResults.visibility = if (users.isNotEmpty()) {
                    android.view.View.VISIBLE
                } else {
                    android.view.View.GONE
                }
            }
        }
    }

    private fun updateUI(state: CreateGroupUiState) {
        // Update form validation
        binding.btnCreateGroup.isEnabled = state.isFormValid && !state.isCreating

        // Show loading state
        binding.progressBar.visibility = if (state.isCreating) {
            android.view.View.VISIBLE
        } else {
            android.view.View.GONE
        }

        // Update selected members
        updateSelectedMembersUI(state.selectedMembers)

        // Handle success
        if (state.isGroupCreated && state.createdGroupId != null) {
            Snackbar.make(binding.root, "Group created successfully!", Snackbar.LENGTH_LONG)
                .setAction("Open") {
                    openGroupChat(state.createdGroupId)
                }
                .show()
            
            // Optional: Auto-navigate to group chat
            // openGroupChat(state.createdGroupId)
        }

        // Handle errors
        state.error?.let { error ->
            Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG)
                .setAction("Retry") { 
                    if (state.isGroupCreated) {
                        viewModel.resetCreationState()
                    } else {
                        viewModel.createGroup()
                    }
                }
                .show()
            viewModel.clearError()
        }

        // Update search loading
        binding.progressBarSearch.visibility = if (state.isSearchingUsers) {
            android.view.View.VISIBLE
        } else {
            android.view.View.GONE
        }
    }

    private fun updateSelectedMembersUI(members: List<com.synapse.social.studioasinc.groupchat.presentation.viewmodel.SelectedMember>) {
        binding.chipGroupMembers.removeAllViews()
        
        members.forEach { member ->
            val chip = Chip(this).apply {
                text = "${member.userName} (${member.role.displayName})"
                isCloseIconVisible = true
                setOnCloseIconClickListener {
                    viewModel.removeMember(member.userId)
                }
                setOnLongClickListener {
                    showRoleSelectionDialog(member.userId, member.role)
                    true
                }
            }
            binding.chipGroupMembers.addView(chip)
        }

        binding.tvMemberCount.text = "${members.size} members selected"
    }

    private fun showRoleSelectionDialog(userId: String, currentRole: UserRole) {
        val roles = arrayOf("Member", "Manager", "Admin")
        val currentIndex = when (currentRole) {
            UserRole.MEMBER -> 0
            UserRole.MANAGER -> 1
            UserRole.ADMIN -> 2
            UserRole.OWNER -> 2 // Can't change owner role
        }

        MaterialAlertDialogBuilder(this)
            .setTitle("Select Role")
            .setSingleChoiceItems(roles, currentIndex) { dialog, which ->
                val newRole = when (which) {
                    0 -> UserRole.MEMBER
                    1 -> UserRole.MANAGER
                    2 -> UserRole.ADMIN
                    else -> UserRole.MEMBER
                }
                viewModel.updateMemberRole(userId, newRole)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showImagePickerDialog() {
        val options = arrayOf("Camera", "Gallery")
        MaterialAlertDialogBuilder(this)
            .setTitle("Select Group Icon")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openCamera()
                    1 -> openGallery()
                }
            }
            .show()
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // You would need to create a temporary file for the camera image
        cameraLauncher.launch(intent)
    }

    private fun openGallery() {
        imagePickerLauncher.launch("image/*")
    }

    private fun loadGroupIcon(uri: Uri) {
        Glide.with(this)
            .load(uri)
            .circleCrop()
            .placeholder(R.drawable.ic_group_placeholder)
            .error(R.drawable.ic_group_placeholder)
            .into(binding.ivGroupIcon)
    }

    private fun uriToFile(uri: Uri): File? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val tempFile = File.createTempFile("group_icon", ".jpg", cacheDir)
            tempFile.outputStream().use { outputStream ->
                inputStream?.copyTo(outputStream)
            }
            tempFile
        } catch (e: Exception) {
            null
        }
    }

    private fun openGroupChat(groupId: String) {
        val intent = Intent(this, GroupChatActivity::class.java).apply {
            putExtra("GROUP_ID", groupId)
        }
        startActivity(intent)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}