package com.synapse.social.studioasinc.groupchat.presentation.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.synapse.social.studioasinc.R
import com.synapse.social.studioasinc.databinding.ActivityInboxGroupChatBinding
import com.synapse.social.studioasinc.groupchat.presentation.adapter.GroupsAdapter
import com.synapse.social.studioasinc.groupchat.presentation.fragments.InboxChatsFragment
import com.synapse.social.studioasinc.groupchat.presentation.fragments.InboxCallsFragment
import com.synapse.social.studioasinc.groupchat.presentation.fragments.InboxStoriesFragment
import com.synapse.social.studioasinc.groupchat.presentation.viewmodel.InboxViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InboxActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInboxGroupChatBinding
    private val viewModel: InboxViewModel by viewModels()
    private lateinit var groupsAdapter: GroupsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityInboxGroupChatBinding.inflate(layoutInflater)
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
        setupViewPager()
        setupFAB()
        setupSearch()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Chats"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupViewPager() {
        val adapter = InboxPagerAdapter(this)
        binding.viewPager.adapter = adapter
        
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Chats"
                1 -> "Calls"
                2 -> "Stories"
                else -> ""
            }
            tab.setIcon(when (position) {
                0 -> R.drawable.icon_message_round
                1 -> R.drawable.ic_call_48px
                2 -> R.drawable.ic_bookmark
                else -> R.drawable.icon_message_round
            })
        }.attach()

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.fabCreateGroup.show()
            }
        })
    }

    private fun setupFAB() {
        binding.fabCreateGroup.setOnClickListener {
            val intent = Intent(this, CreateGroupActivity::class.java)
            startActivity(intent)
        }

        // Add expressive animation
        binding.fabCreateGroup.setOnLongClickListener {
            // Show tooltip or additional options
            true
        }
    }

    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.searchGroups(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { 
                    if (it.isEmpty()) {
                        viewModel.clearSearch()
                    } else {
                        viewModel.searchGroups(it)
                    }
                }
                return true
            }
        })

        binding.searchView.setOnCloseListener {
            viewModel.clearSearch()
            false
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                updateUI(state)
            }
        }

        lifecycleScope.launch {
            viewModel.searchResults.collect { searchResults ->
                // Handle search results if needed
            }
        }
    }

    private fun updateUI(state: com.synapse.social.studioasinc.groupchat.presentation.viewmodel.InboxUiState) {
        binding.progressBar.visibility = if (state.isLoading) 
            android.view.View.VISIBLE else android.view.View.GONE

        state.error?.let { error ->
            com.google.android.material.snackbar.Snackbar
                .make(binding.root, error, com.google.android.material.snackbar.Snackbar.LENGTH_LONG)
                .setAction("Retry") { viewModel.loadInitialData() }
                .show()
            viewModel.clearError()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        val intent = Intent(this, com.synapse.social.studioasinc.HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private inner class InboxPagerAdapter(fragmentActivity: FragmentActivity) : 
        FragmentStateAdapter(fragmentActivity) {

        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> InboxChatsFragment()
                1 -> InboxCallsFragment()
                2 -> InboxStoriesFragment()
                else -> InboxChatsFragment()
            }
        }
    }
}