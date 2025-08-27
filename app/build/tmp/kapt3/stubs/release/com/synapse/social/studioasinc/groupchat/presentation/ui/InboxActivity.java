package com.synapse.social.studioasinc.groupchat.presentation.ui;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001:\u0001\u001eB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\r\u001a\u00020\u000eH\u0016J\u0012\u0010\u000f\u001a\u00020\u000e2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0014J\b\u0010\u0012\u001a\u00020\u0013H\u0016J\b\u0010\u0014\u001a\u00020\u000eH\u0002J\b\u0010\u0015\u001a\u00020\u000eH\u0002J\b\u0010\u0016\u001a\u00020\u000eH\u0002J\b\u0010\u0017\u001a\u00020\u000eH\u0002J\b\u0010\u0018\u001a\u00020\u000eH\u0002J\b\u0010\u0019\u001a\u00020\u000eH\u0002J\b\u0010\u001a\u001a\u00020\u000eH\u0002J\u0010\u0010\u001b\u001a\u00020\u000e2\u0006\u0010\u001c\u001a\u00020\u001dH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0007\u001a\u00020\b8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000b\u0010\f\u001a\u0004\b\t\u0010\n\u00a8\u0006\u001f"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/presentation/ui/InboxActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/synapse/social/studioasinc/databinding/ActivityInboxGroupChatBinding;", "groupsAdapter", "Lcom/synapse/social/studioasinc/groupchat/presentation/adapter/GroupsAdapter;", "viewModel", "Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/InboxViewModel;", "getViewModel", "()Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/InboxViewModel;", "viewModel$delegate", "Lerror/NonExistentClass;", "onBackPressed", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onSupportNavigateUp", "", "setupFAB", "setupObservers", "setupSearch", "setupToolbar", "setupUI", "setupViewPager", "setupWindowInsets", "updateUI", "state", "Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/InboxUiState;", "InboxPagerAdapter", "app_release"})
public final class InboxActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.synapse.social.studioasinc.databinding.ActivityInboxGroupChatBinding binding;
    @org.jetbrains.annotations.NotNull()
    private final error.NonExistentClass viewModel$delegate = null;
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0082\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\b\u0010\t\u001a\u00020\bH\u0016\u00a8\u0006\n"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/presentation/ui/InboxActivity$InboxPagerAdapter;", "Landroidx/viewpager2/adapter/FragmentStateAdapter;", "fragmentActivity", "Landroidx/fragment/app/FragmentActivity;", "(Lcom/synapse/social/studioasinc/groupchat/presentation/ui/InboxActivity;Landroidx/fragment/app/FragmentActivity;)V", "createFragment", "Landroidx/fragment/app/Fragment;", "position", "", "getItemCount", "app_release"})
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