package com.synapse.social.studioasinc.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.synapse.social.studioasinc.fragments.ChannelsFragment;
import com.synapse.social.studioasinc.fragments.ChatsFragment;
import com.synapse.social.studioasinc.fragments.GroupsFragment;

public class MessagesPagerAdapter extends FragmentStateAdapter {

  public MessagesPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
    super(fragmentActivity);
  }

  @NonNull
  @Override
  public Fragment createFragment(int position) {
    switch (position) {
      case 0:
        return new ChatsFragment();
      case 1:
        return new ChannelsFragment();
      case 2:
        return new GroupsFragment();
      default:
        throw new IllegalStateException("Invalid position: " + position);
    }
  }

  @Override
  public int getItemCount() {
    return 3;
  }
}
