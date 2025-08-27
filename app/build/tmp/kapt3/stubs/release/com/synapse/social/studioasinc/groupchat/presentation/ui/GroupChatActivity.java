package com.synapse.social.studioasinc.groupchat.presentation.ui;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000v\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010\u0016\u001a\u00020\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0014J\b\u0010\u001a\u001a\u00020\u0017H\u0002J\u0010\u0010\u001b\u001a\u00020\u00172\u0006\u0010\u001c\u001a\u00020\u0013H\u0002J\u0010\u0010\u001d\u001a\u00020\u00172\u0006\u0010\u001c\u001a\u00020\u0013H\u0002J\b\u0010\u001e\u001a\u00020\u0017H\u0002J\b\u0010\u001f\u001a\u00020\u0017H\u0002J\b\u0010 \u001a\u00020\u0017H\u0002J\b\u0010!\u001a\u00020\u0017H\u0002J\u0010\u0010\"\u001a\u00020\u00172\u0006\u0010#\u001a\u00020$H\u0002J\u0010\u0010%\u001a\u00020\u00172\u0006\u0010&\u001a\u00020\'H\u0002J\u0010\u0010(\u001a\u00020\u00172\u0006\u0010)\u001a\u00020*H\u0002J\b\u0010+\u001a\u00020\u0017H\u0002J\b\u0010,\u001a\u00020\u0017H\u0002J\b\u0010-\u001a\u00020\u0017H\u0002J\b\u0010.\u001a\u00020\u0017H\u0002J\b\u0010/\u001a\u00020\u0017H\u0002J\b\u00100\u001a\u00020\u0017H\u0002J\b\u00101\u001a\u00020\u0017H\u0002J\b\u00102\u001a\u00020\u0017H\u0002J\b\u00103\u001a\u00020\u0017H\u0002J\u0010\u00104\u001a\u00020\u00172\u0006\u00105\u001a\u000206H\u0002J\u0010\u00107\u001a\u00020\u00172\u0006\u00105\u001a\u000206H\u0002J\u0010\u00108\u001a\u00020\u00172\u0006\u00105\u001a\u000206H\u0002J\u0010\u00109\u001a\u00020\u00172\u0006\u00105\u001a\u000206H\u0002J\u0010\u0010:\u001a\u00020\u00172\u0006\u00105\u001a\u000206H\u0002J\u0010\u0010;\u001a\u00020\u00172\u0006\u00105\u001a\u000206H\u0002J\u0012\u0010<\u001a\u0004\u0018\u00010=2\u0006\u0010>\u001a\u00020\u0010H\u0002J\b\u0010?\u001a\u00020@H\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0006\u001a\u00020\u00078BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\n\u0010\u000b\u001a\u0004\b\b\u0010\tR\u000e\u0010\f\u001a\u00020\rX\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00150\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006A"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/presentation/ui/GroupChatActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "<init>", "()V", "binding", "Lcom/synapse/social/studioasinc/databinding/ActivityGroupChatBinding;", "viewModel", "Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/GroupChatViewModel;", "getViewModel", "()Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/GroupChatViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "messagesAdapter", "Lcom/synapse/social/studioasinc/groupchat/presentation/adapter/GroupMessagesAdapter;", "selectedAttachments", "", "Landroid/net/Uri;", "attachmentPickerLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "", "cameraLauncher", "Landroid/content/Intent;", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "setupWindowInsets", "setupUI", "groupName", "setupToolbar", "setupRecyclerView", "setupInputArea", "setupClickListeners", "setupObservers", "updateUI", "state", "Lcom/synapse/social/studioasinc/groupchat/presentation/viewmodel/GroupChatUiState;", "updateGroupInfo", "group", "Lcom/synapse/social/studioasinc/groupchat/data/model/Group;", "updateMemberInfo", "memberCount", "", "sendMessage", "showAttachmentOptions", "openCamera", "openGallery", "openDocuments", "openAudioPicker", "updateAttachmentPreview", "clearAttachments", "updateSendButtonState", "showMessageOptions", "message", "Lcom/synapse/social/studioasinc/groupchat/data/model/GroupMessage;", "startEdit", "startReply", "showReactionPicker", "copyMessageToClipboard", "showDeleteConfirmation", "uriToFile", "Ljava/io/File;", "uri", "onSupportNavigateUp", "", "app_release"})
public final class GroupChatActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.synapse.social.studioasinc.databinding.ActivityGroupChatBinding binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    private com.synapse.social.studioasinc.groupchat.presentation.adapter.GroupMessagesAdapter messagesAdapter;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<android.net.Uri> selectedAttachments;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<java.lang.String> attachmentPickerLauncher = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<android.content.Intent> cameraLauncher = null;
    
    public GroupChatActivity() {
        super();
    }
    
    private final com.synapse.social.studioasinc.groupchat.presentation.viewmodel.GroupChatViewModel getViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupWindowInsets() {
    }
    
    private final void setupUI(java.lang.String groupName) {
    }
    
    private final void setupToolbar(java.lang.String groupName) {
    }
    
    private final void setupRecyclerView() {
    }
    
    private final void setupInputArea() {
    }
    
    private final void setupClickListeners() {
    }
    
    private final void setupObservers() {
    }
    
    private final void updateUI(com.synapse.social.studioasinc.groupchat.presentation.viewmodel.GroupChatUiState state) {
    }
    
    private final void updateGroupInfo(com.synapse.social.studioasinc.groupchat.data.model.Group group) {
    }
    
    private final void updateMemberInfo(int memberCount) {
    }
    
    private final void sendMessage() {
    }
    
    private final void showAttachmentOptions() {
    }
    
    private final void openCamera() {
    }
    
    private final void openGallery() {
    }
    
    private final void openDocuments() {
    }
    
    private final void openAudioPicker() {
    }
    
    private final void updateAttachmentPreview() {
    }
    
    private final void clearAttachments() {
    }
    
    private final void updateSendButtonState() {
    }
    
    private final void showMessageOptions(com.synapse.social.studioasinc.groupchat.data.model.GroupMessage message) {
    }
    
    private final void startEdit(com.synapse.social.studioasinc.groupchat.data.model.GroupMessage message) {
    }
    
    private final void startReply(com.synapse.social.studioasinc.groupchat.data.model.GroupMessage message) {
    }
    
    private final void showReactionPicker(com.synapse.social.studioasinc.groupchat.data.model.GroupMessage message) {
    }
    
    private final void copyMessageToClipboard(com.synapse.social.studioasinc.groupchat.data.model.GroupMessage message) {
    }
    
    private final void showDeleteConfirmation(com.synapse.social.studioasinc.groupchat.data.model.GroupMessage message) {
    }
    
    private final java.io.File uriToFile(android.net.Uri uri) {
        return null;
    }
    
    @java.lang.Override()
    public boolean onSupportNavigateUp() {
        return false;
    }
}