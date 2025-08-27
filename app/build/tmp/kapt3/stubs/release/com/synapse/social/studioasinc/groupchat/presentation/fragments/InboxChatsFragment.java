package com.synapse.social.studioasinc.groupchat.presentation.fragments;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0002J\u0010\u0010\u0014\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0002J$\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0016J\b\u0010\u001d\u001a\u00020\u0011H\u0016J\u001a\u0010\u001e\u001a\u00020\u00112\u0006\u0010\u001f\u001a\u00020\u00162\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0016J\u0010\u0010 \u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0002J\b\u0010!\u001a\u00020\u0011H\u0002J\b\u0010\"\u001a\u00020\u0011H\u0002J\b\u0010#\u001a\u00020\u0011H\u0002J\u0010\u0010$\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0002J\u0010\u0010%\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0002J\u0010\u0010&\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0002J\u0010\u0010\'\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0002J\u0010\u0010(\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0002J\u0010\u0010)\u001a\u00020\u00112\u0006\u0010*\u001a\u00020+H\u0002J\u0010\u0010,\u001a\u00020\u00112\u0006\u0010-\u001a\u00020.H\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u00020\u00048BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\n\u001a\u00020\u000b8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000e\u0010\u000f\u001a\u0004\b\f\u0010\r\u00a8\u0006/"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/presentation/fragments/InboxChatsFragment;", "Landroidx/fragment/app/Fragment;", "()V", "_binding", "Lcom/synapse/social/studioasinc/databinding/FragmentInboxChatsBinding;", "binding", "getBinding", "()Lcom/synapse/social/studioasinc/databinding/FragmentInboxChatsBinding;", "groupsAdapter", "Lcom/synapse/social/studioasinc/groupchat/presentation/adapter/GroupsAdapter;", "viewModel", "Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/InboxViewModel;", "getViewModel", "()Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/InboxViewModel;", "viewModel$delegate", "Lerror/NonExistentClass;", "archiveGroup", "", "group", "Lcom/synapse/social/studioasinc/groupchat/data/model/Group;", "leaveGroup", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "onViewCreated", "view", "openGroupChat", "setupObservers", "setupRecyclerView", "setupSwipeRefresh", "showGroupInfo", "showGroupOptions", "showLeaveConfirmation", "toggleMute", "togglePin", "updateEmptyState", "isEmpty", "", "updateUI", "state", "Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/InboxUiState;", "app_release"})
public final class InboxChatsFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.Nullable()
    private com.synapse.social.studioasinc.databinding.FragmentInboxChatsBinding _binding;
    @org.jetbrains.annotations.NotNull()
    private final error.NonExistentClass viewModel$delegate = null;
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