package com.synapse.social.studioasinc.groupchat.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.synapse.social.studioasinc.databinding.FragmentInboxCallsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InboxCallsFragment : Fragment() {

    private var _binding: FragmentInboxCallsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInboxCallsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Setup calls functionality
        setupUI()
    }

    private fun setupUI() {
        // For now, show a placeholder
        binding.tvPlaceholder.text = "Group Calls feature coming soon!"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}