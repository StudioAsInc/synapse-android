package com.synapse.social.studioasinc.groupchat.presentation.ui;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u000bH\u0002J\u0012\u0010\u0017\u001a\u00020\u00152\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0014J\b\u0010\u001a\u001a\u00020\u001bH\u0016J\b\u0010\u001c\u001a\u00020\u0015H\u0002J\b\u0010\u001d\u001a\u00020\u0015H\u0002J\u0010\u0010\u001e\u001a\u00020\u00152\u0006\u0010\u001f\u001a\u00020\tH\u0002J\b\u0010 \u001a\u00020\u0015H\u0002J\b\u0010!\u001a\u00020\u0015H\u0002J\b\u0010\"\u001a\u00020\u0015H\u0002J\b\u0010#\u001a\u00020\u0015H\u0002J\b\u0010$\u001a\u00020\u0015H\u0002J\b\u0010%\u001a\u00020\u0015H\u0002J\b\u0010&\u001a\u00020\u0015H\u0002J\b\u0010\'\u001a\u00020\u0015H\u0002J\u0018\u0010(\u001a\u00020\u00152\u0006\u0010)\u001a\u00020\t2\u0006\u0010*\u001a\u00020+H\u0002J\u0016\u0010,\u001a\u00020\u00152\f\u0010-\u001a\b\u0012\u0004\u0012\u00020/0.H\u0002J\u0010\u00100\u001a\u00020\u00152\u0006\u00101\u001a\u000202H\u0002J\u0012\u00103\u001a\u0004\u0018\u0001042\u0006\u0010\u0016\u001a\u00020\u000bH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u000e\u001a\u00020\u000f8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u0010\u0010\u0011\u00a8\u00065"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/presentation/ui/CreateGroupActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/synapse/social/studioasinc/databinding/ActivityCreateGroupBinding;", "cameraLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "Landroid/content/Intent;", "imagePickerLauncher", "", "selectedImageUri", "Landroid/net/Uri;", "userSearchAdapter", "Lcom/synapse/social/studioasinc/groupchat/presentation/adapter/UserSearchAdapter;", "viewModel", "Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/CreateGroupViewModel;", "getViewModel", "()Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/CreateGroupViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "loadGroupIcon", "", "uri", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onSupportNavigateUp", "", "openCamera", "openGallery", "openGroupChat", "groupId", "setupClickListeners", "setupForm", "setupObservers", "setupRecyclerView", "setupToolbar", "setupUI", "setupWindowInsets", "showImagePickerDialog", "showRoleSelectionDialog", "userId", "currentRole", "Lcom/synapse/social/studioasinc/groupchat/data/model/UserRole;", "updateSelectedMembersUI", "members", "", "Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/SelectedMember;", "updateUI", "state", "Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/CreateGroupUiState;", "uriToFile", "Ljava/io/File;", "app_release"})
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