package com.synapse.social.studioasinc.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.synapse.social.studioasinc.fragments.ChannelsFragment
import com.synapse.social.studioasinc.fragments.ChatsFragment
import com.synapse.social.studioasinc.fragments.GroupsFragment

class MessagesPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ChatsFragment()
            1 -> ChannelsFragment()
            2 -> GroupsFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}
