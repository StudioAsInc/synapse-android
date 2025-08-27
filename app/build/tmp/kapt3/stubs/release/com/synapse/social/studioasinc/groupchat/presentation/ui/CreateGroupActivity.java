package com.synapse.social.studioasinc.groupchat.presentation.ui;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010\u0015\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0014J\b\u0010\u0019\u001a\u00020\u0016H\u0002J\b\u0010\u001a\u001a\u00020\u0016H\u0002J\b\u0010\u001b\u001a\u00020\u0016H\u0002J\b\u0010\u001c\u001a\u00020\u0016H\u0002J\b\u0010\u001d\u001a\u00020\u0016H\u0002J\b\u0010\u001e\u001a\u00020\u0016H\u0002J\b\u0010\u001f\u001a\u00020\u0016H\u0002J\u0010\u0010 \u001a\u00020\u00162\u0006\u0010!\u001a\u00020\"H\u0002J\u0016\u0010#\u001a\u00020\u00162\f\u0010$\u001a\b\u0012\u0004\u0012\u00020&0%H\u0002J\u0018\u0010\'\u001a\u00020\u00162\u0006\u0010(\u001a\u00020\u00122\u0006\u0010)\u001a\u00020*H\u0002J\b\u0010+\u001a\u00020\u0016H\u0002J\b\u0010,\u001a\u00020\u0016H\u0002J\b\u0010-\u001a\u00020\u0016H\u0002J\u0010\u0010.\u001a\u00020\u00162\u0006\u0010/\u001a\u00020\u000fH\u0002J\u0012\u00100\u001a\u0004\u0018\u0001012\u0006\u0010/\u001a\u00020\u000fH\u0002J\u0010\u00102\u001a\u00020\u00162\u0006\u00103\u001a\u00020\u0012H\u0002J\b\u00104\u001a\u000205H\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0006\u001a\u00020\u00078BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\n\u0010\u000b\u001a\u0004\b\b\u0010\tR\u000e\u0010\f\u001a\u00020\rX\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00066"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/presentation/ui/CreateGroupActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "<init>", "()V", "binding", "Lcom/synapse/social/studioasinc/databinding/ActivityCreateGroupBinding;", "viewModel", "Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/CreateGroupViewModel;", "getViewModel", "()Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/CreateGroupViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "userSearchAdapter", "Lcom/synapse/social/studioasinc/groupchat/presentation/adapter/UserSearchAdapter;", "selectedImageUri", "Landroid/net/Uri;", "imagePickerLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "", "cameraLauncher", "Landroid/content/Intent;", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "setupWindowInsets", "setupUI", "setupToolbar", "setupForm", "setupRecyclerView", "setupClickListeners", "setupObservers", "updateUI", "state", "Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/CreateGroupUiState;", "updateSelectedMembersUI", "members", "", "Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/SelectedMember;", "showRoleSelectionDialog", "userId", "currentRole", "Lcom/synapse/social/studioasinc/groupchat/data/model/UserRole;", "showImagePickerDialog", "openCamera", "openGallery", "loadGroupIcon", "uri", "uriToFile", "Ljava/io/File;", "openGroupChat", "groupId", "onSupportNavigateUp", "", "app_release"})
public final class CreateGroupActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.synapse.social.studioasinc.databinding.ActivityCreateGroupBinding binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    private com.synapse.social.studioasinc.groupchat.presentation.adapter.UserSearchAdapter userSearchAdapter;
    @org.jetbrains.annotations.Nullable()
    private android.net.Uri selectedImageUri;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<java.lang.String> imagePickerLauncher = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<android.content.Intent> cameraLauncher = null;
    
    public CreateGroupActivity() {
        super();
    }
    
    private final com.synapse.social.studioasinc.groupchat.presentation.viewmodel.CreateGroupViewModel getViewModel() {
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
    
    private final void setupForm() {
    }
    
    private final void setupRecyclerView() {
    }
    
    private final void setupClickListeners() {
    }
    
    private final void setupObservers() {
    }
    
    private final void updateUI(com.synapse.social.studioasinc.groupchat.presentation.viewmodel.CreateGroupUiState state) {
    }
    
    private final void updateSelectedMembersUI(java.util.List<com.synapse.social.studioasinc.groupchat.presentation.viewmodel.SelectedMember> members) {
    }
    
    private final void showRoleSelectionDialog(java.lang.String userId, com.synapse.social.studioasinc.groupchat.data.model.UserRole currentRole) {
    }
    
    private final void showImagePickerDialog() {
    }
    
    private final void openCamera() {
    }
    
    private final void openGallery() {
    }
    
    private final void loadGroupIcon(android.net.Uri uri) {
    }
    
    private final java.io.File uriToFile(android.net.Uri uri) {
        return null;
    }
    
    private final void openGroupChat(java.lang.String groupId) {
    }
    
    @java.lang.Override()
    public boolean onSupportNavigateUp() {
        return false;
    }
}