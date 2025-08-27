package com.synapse.social.studioasinc.groupchat.presentation.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.synapse.social.studioasinc.databinding.FragmentInboxChatsBinding
import com.synapse.social.studioasinc.groupchat.data.model.Group
import com.synapse.social.studioasinc.groupchat.presentation.adapter.GroupsAdapter
import com.synapse.social.studioasinc.groupchat.presentation.ui.GroupChatActivity
import com.synapse.social.studioasinc.groupchat.presentation.viewmodel.InboxViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InboxChatsFragment : Fragment() {

    private var _binding: FragmentInboxChatsBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: InboxViewModel by activityViewModels()
    private lateinit var groupsAdapter: GroupsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInboxChatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupObservers()
        setupSwipeRefresh()
    }

    private fun setupRecyclerView() {
        groupsAdapter = GroupsAdapter(
            onGroupClick = { group ->
                openGroupChat(group)
            },
            onGroupLongClick = { group ->
                showGroupOptions(group)
            }
        )

        binding.rvGroups.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = groupsAdapter
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.groupsPagingData.collect { pagingData ->
                groupsAdapter.submitData(pagingData)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                updateUI(state)
            }
        }

        // Also observe recent groups for quick access
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.recentGroups.collect { groups ->
                updateEmptyState(groups.isEmpty())
            }
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshGroups()
        }
    }

    private fun updateUI(state: com.synapse.social.studioasinc.groupchat.presentation.viewmodel.InboxUiState) {
        binding.swipeRefresh.isRefreshing = state.isLoading

        // Show/hide search results
        if (state.searchResults.isNotEmpty()) {
            // You might want to show search results in a different way
            // For now, we'll just update the adapter if needed
        }
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            binding.emptyStateGroup.visibility = View.VISIBLE
            binding.rvGroups.visibility = View.GONE
        } else {
            binding.emptyStateGroup.visibility = View.GONE
            binding.rvGroups.visibility = View.VISIBLE
        }
    }

    private fun openGroupChat(group: Group) {
        val intent = Intent(requireContext(), GroupChatActivity::class.java).apply {
            putExtra("GROUP_ID", group.id)
            putExtra("GROUP_NAME", group.name)
        }
        startActivity(intent)
    }

    private fun showGroupOptions(group: Group) {
        val options = arrayOf(
            "View Group Info",
            "Mute Notifications",
            "Pin Group",
            "Archive Group",
            "Leave Group"
        )

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(group.getDisplayName())
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showGroupInfo(group)
                    1 -> toggleMute(group)
                    2 -> togglePin(group)
                    3 -> archiveGroup(group)
                    4 -> showLeaveConfirmation(group)
                }
            }
            .show()
    }

    private fun showGroupInfo(group: Group) {
        // Navigate to group info activity
    }

    private fun toggleMute(group: Group) {
        // Implement mute/unmute functionality
    }

    private fun togglePin(group: Group) {
        // Implement pin/unpin functionality
    }

    private fun archiveGroup(group: Group) {
        // Implement archive functionality
    }

    private fun showLeaveConfirmation(group: Group) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Leave Group")
            .setMessage("Are you sure you want to leave ${group.getDisplayName()}?")
            .setPositiveButton("Leave") { _, _ ->
                leaveGroup(group)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun leaveGroup(group: Group) {
        // Implement leave group functionality
        // This would involve calling repository methods
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}