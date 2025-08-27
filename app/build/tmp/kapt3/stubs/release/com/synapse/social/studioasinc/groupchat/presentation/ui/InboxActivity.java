package com.synapse.social.studioasinc.groupchat.presentation.ui;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001:\u0001\u001fB\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0014J\b\u0010\u0012\u001a\u00020\u000fH\u0002J\b\u0010\u0013\u001a\u00020\u000fH\u0002J\b\u0010\u0014\u001a\u00020\u000fH\u0002J\b\u0010\u0015\u001a\u00020\u000fH\u0002J\b\u0010\u0016\u001a\u00020\u000fH\u0002J\b\u0010\u0017\u001a\u00020\u000fH\u0002J\b\u0010\u0018\u001a\u00020\u000fH\u0002J\u0010\u0010\u0019\u001a\u00020\u000f2\u0006\u0010\u001a\u001a\u00020\u001bH\u0002J\b\u0010\u001c\u001a\u00020\u001dH\u0016J\b\u0010\u001e\u001a\u00020\u000fH\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0006\u001a\u00020\u00078BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\n\u0010\u000b\u001a\u0004\b\b\u0010\tR\u000e\u0010\f\u001a\u00020\rX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006 "}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/presentation/ui/InboxActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "<init>", "()V", "binding", "Lcom/synapse/social/studioasinc/databinding/ActivityInboxGroupChatBinding;", "viewModel", "Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/InboxViewModel;", "getViewModel", "()Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/InboxViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "groupsAdapter", "Lcom/synapse/social/studioasinc/groupchat/presentation/adapter/GroupsAdapter;", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "setupWindowInsets", "setupUI", "setupToolbar", "setupViewPager", "setupFAB", "setupSearch", "setupObservers", "updateUI", "state", "Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/InboxUiState;", "onSupportNavigateUp", "", "onBackPressed", "InboxPagerAdapter", "app_release"})
public final class InboxActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.synapse.social.studioasinc.databinding.ActivityInboxGroupChatBinding binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    private com.synapse.social.studioasinc.groupchat.presentation.adapter.GroupsAdapter groupsAdapter;
    
    public InboxActivity() {
        super();
    }
    
    private final com.synapse.social.studioasinc.groupchat.presentation.viewmodel.InboxViewModel getViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupWindowInsets() {
    }
    
    private final void setupUI() {
    }
    
    private final void setupToolbar() {
    }
    
    private final void setupViewPager() {
    }
    
    private final void setupFAB() {
    }
    
    private final void setupSearch() {
    }
    
    private final void setupObservers() {
    }
    
    private final void updateUI(com.synapse.social.studioasinc.groupchat.presentation.viewmodel.InboxUiState state) {
    }
    
    @java.lang.Override()
    public boolean onSupportNavigateUp() {
        return false;
    }
    
    @java.lang.Override()
    public void onBackPressed() {
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0082\u0004\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\b\u0010\u0006\u001a\u00020\u0007H\u0016J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u0007H\u0016\u00a8\u0006\u000b"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/presentation/ui/InboxActivity$InboxPagerAdapter;", "Landroidx/viewpager2/adapter/FragmentStateAdapter;", "fragmentActivity", "Landroidx/fragment/app/FragmentActivity;", "<init>", "(Lcom/synapse/social/studioasinc/groupchat/presentation/ui/InboxActivity;Landroidx/fragment/app/FragmentActivity;)V", "getItemCount", "", "createFragment", "Landroidx/fragment/app/Fragment;", "position", "app_release"})
    final class InboxPagerAdapter extends androidx.viewpager2.adapter.FragmentStateAdapter {
        
        public InboxPagerAdapter(@org.jetbrains.annotations.NotNull()
        androidx.fragment.app.FragmentActivity fragmentActivity) {
            super(null);
        }
        
        @java.lang.Override()
        public int getItemCount() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public androidx.fragment.app.Fragment createFragment(int position) {
            return null;
        }
    }
}