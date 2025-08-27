package com.synapse.social.studioasinc.groupchat.presentation.adapter;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001:\u0002\u0010\u0011B\u0019\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\u0002\u0010\u0007J\u0018\u0010\b\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH\u0016J\u0018\u0010\f\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000bH\u0016R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/presentation/adapter/UserSearchAdapter;", "Landroidx/recyclerview/widget/ListAdapter;", "Lcom/synapse/social/studioasinc/groupchat/data/model/User;", "Lcom/synapse/social/studioasinc/groupchat/presentation/adapter/UserSearchAdapter$UserViewHolder;", "onUserClick", "Lkotlin/Function1;", "", "(Lkotlin/jvm/functions/Function1;)V", "onBindViewHolder", "holder", "position", "", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "UserDiffCallback", "UserViewHolder", "app_release"})
public final class UserSearchAdapter extends androidx.recyclerview.widget.ListAdapter<com.synapse.social.studioasinc.groupchat.data.model.User, com.synapse.social.studioasinc.groupchat.presentation.adapter.UserSearchAdapter.UserViewHolder> {
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function1<com.synapse.social.studioasinc.groupchat.data.model.User, kotlin.Unit> onUserClick = null;
    
    public UserSearchAdapter(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.synapse.social.studioasinc.groupchat.data.model.User, kotlin.Unit> onUserClick) {
        super(null);
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public com.synapse.social.studioasinc.groupchat.presentation.adapter.UserSearchAdapter.UserViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.presentation.adapter.UserSearchAdapter.UserViewHolder holder, int position) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0003J\u0018\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016J\u0018\u0010\b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016\u00a8\u0006\t"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/presentation/adapter/UserSearchAdapter$UserDiffCallback;", "Landroidx/recyclerview/widget/DiffUtil$ItemCallback;", "Lcom/synapse/social/studioasinc/groupchat/data/model/User;", "()V", "areContentsTheSame", "", "oldItem", "newItem", "areItemsTheSame", "app_release"})
    static final class UserDiffCallback extends androidx.recyclerview.widget.DiffUtil.ItemCallback<com.synapse.social.studioasinc.groupchat.data.model.User> {
        
        public UserDiffCallback() {
            super();
        }
        
        @java.lang.Override()
        public boolean areItemsTheSame(@org.jetbrains.annotations.NotNull()
        com.synapse.social.studioasinc.groupchat.data.model.User oldItem, @org.jetbrains.annotations.NotNull()
        com.synapse.social.studioasinc.groupchat.data.model.User newItem) {
            return false;
        }
        
        @java.lang.Override()
        public boolean areContentsTheSame(@org.jetbrains.annotations.NotNull()
        com.synapse.social.studioasinc.groupchat.data.model.User oldItem, @org.jetbrains.annotations.NotNull()
        com.synapse.social.studioasinc.groupchat.data.model.User newItem) {
            return false;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B!\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005\u00a2\u0006\u0002\u0010\bJ\u000e\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u0006R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/presentation/adapter/UserSearchAdapter$UserViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lcom/synapse/social/studioasinc/databinding/ItemUserSearchBinding;", "onUserClick", "Lkotlin/Function1;", "Lcom/synapse/social/studioasinc/groupchat/data/model/User;", "", "(Lcom/synapse/social/studioasinc/databinding/ItemUserSearchBinding;Lkotlin/jvm/functions/Function1;)V", "bind", "user", "app_release"})
    public static final class UserViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final com.synapse.social.studioasinc.databinding.ItemUserSearchBinding binding = null;
        @org.jetbrains.annotations.NotNull()
        private final kotlin.jvm.functions.Function1<com.synapse.social.studioasinc.groupchat.data.model.User, kotlin.Unit> onUserClick = null;
        
        public UserViewHolder(@org.jetbrains.annotations.NotNull()
        com.synapse.social.studioasinc.databinding.ItemUserSearchBinding binding, @org.jetbrains.annotations.NotNull()
        kotlin.jvm.functions.Function1<? super com.synapse.social.studioasinc.groupchat.data.model.User, kotlin.Unit> onUserClick) {
            super(null);
        }
        
        public final void bind(@org.jetbrains.annotations.NotNull()
        com.synapse.social.studioasinc.groupchat.data.model.User user) {
        }
    }
}