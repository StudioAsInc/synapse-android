package com.synapse.social.studioasinc.groupchat.presentation.fragments;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\b\u0007\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J$\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0016J\u001a\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u00122\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0016J\b\u0010\u001c\u001a\u00020\u001aH\u0002J\b\u0010\u001d\u001a\u00020\u001aH\u0002J\b\u0010\u001e\u001a\u00020\u001aH\u0002J\u0010\u0010\u001f\u001a\u00020\u001a2\u0006\u0010 \u001a\u00020!H\u0002J\u0010\u0010\"\u001a\u00020\u001a2\u0006\u0010#\u001a\u00020$H\u0002J\u0010\u0010%\u001a\u00020\u001a2\u0006\u0010&\u001a\u00020\'H\u0002J\u0010\u0010(\u001a\u00020\u001a2\u0006\u0010&\u001a\u00020\'H\u0002J\u0010\u0010)\u001a\u00020\u001a2\u0006\u0010&\u001a\u00020\'H\u0002J\u0010\u0010*\u001a\u00020\u001a2\u0006\u0010&\u001a\u00020\'H\u0002J\u0010\u0010+\u001a\u00020\u001a2\u0006\u0010&\u001a\u00020\'H\u0002J\u0010\u0010,\u001a\u00020\u001a2\u0006\u0010&\u001a\u00020\'H\u0002J\u0010\u0010-\u001a\u00020\u001a2\u0006\u0010&\u001a\u00020\'H\u0002J\u0010\u0010.\u001a\u00020\u001a2\u0006\u0010&\u001a\u00020\'H\u0002J\b\u0010/\u001a\u00020\u001aH\u0016R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\u00020\u00058BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u001b\u0010\t\u001a\u00020\n8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\u000f\u001a\u00020\u0010X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u00060"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/presentation/fragments/InboxChatsFragment;", "Landroidx/fragment/app/Fragment;", "<init>", "()V", "_binding", "Lcom/synapse/social/studioasinc/databinding/FragmentInboxChatsBinding;", "binding", "getBinding", "()Lcom/synapse/social/studioasinc/databinding/FragmentInboxChatsBinding;", "viewModel", "Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/InboxViewModel;", "getViewModel", "()Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/InboxViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "groupsAdapter", "Lcom/synapse/social/studioasinc/groupchat/presentation/adapter/GroupsAdapter;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onViewCreated", "", "view", "setupRecyclerView", "setupObservers", "setupSwipeRefresh", "updateUI", "state", "Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/InboxUiState;", "updateEmptyState", "isEmpty", "", "openGroupChat", "group", "Lcom/synapse/social/studioasinc/groupchat/data/model/Group;", "showGroupOptions", "showGroupInfo", "toggleMute", "togglePin", "archiveGroup", "showLeaveConfirmation", "leaveGroup", "onDestroyView", "app_release"})
public final class InboxChatsFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.Nullable()
    private com.synapse.social.studioasinc.databinding.FragmentInboxChatsBinding _binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    private com.synapse.social.studioasinc.groupchat.presentation.adapter.GroupsAdapter groupsAdapter;
    
    public InboxChatsFragment() {
        super();
    }
    
    private final com.synapse.social.studioasinc.databinding.FragmentInboxChatsBinding getBinding() {
        return null;
    }
    
    private final com.synapse.social.studioasinc.groupchat.presentation.viewmodel.InboxViewModel getViewModel() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupRecyclerView() {
    }
    
    private final void setupObservers() {
    }
    
    private final void setupSwipeRefresh() {
    }
    
    private final void updateUI(com.synapse.social.studioasinc.groupchat.presentation.viewmodel.InboxUiState state) {
    }
    
    private final void updateEmptyState(boolean isEmpty) {
    }
    
    private final void openGroupChat(com.synapse.social.studioasinc.groupchat.data.model.Group group) {
    }
    
    private final void showGroupOptions(com.synapse.social.studioasinc.groupchat.data.model.Group group) {
    }
    
    private final void showGroupInfo(com.synapse.social.studioasinc.groupchat.data.model.Group group) {
    }
    
    private final void toggleMute(com.synapse.social.studioasinc.groupchat.data.model.Group group) {
    }
    
    private final void togglePin(com.synapse.social.studioasinc.groupchat.data.model.Group group) {
    }
    
    private final void archiveGroup(com.synapse.social.studioasinc.groupchat.data.model.Group group) {
    }
    
    private final void showLeaveConfirmation(com.synapse.social.studioasinc.groupchat.data.model.Group group) {
    }
    
    private final void leaveGroup(com.synapse.social.studioasinc.groupchat.data.model.Group group) {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
}