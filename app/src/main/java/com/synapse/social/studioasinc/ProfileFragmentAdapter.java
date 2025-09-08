package com.synapse.social.studioasinc;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ProfileFragmentAdapter extends FragmentStateAdapter {

    private final String uid;

    public ProfileFragmentAdapter(@NonNull FragmentActivity fragmentActivity, String uid) {
        super(fragmentActivity);
        this.uid = uid;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return UserInfoFragment.newInstance(uid);
        }
        return UserPostsFragment.newInstance(uid);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
