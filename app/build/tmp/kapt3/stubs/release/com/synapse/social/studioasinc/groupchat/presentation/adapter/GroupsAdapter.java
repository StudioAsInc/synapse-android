package com.synapse.social.studioasinc.groupchat.presentation.adapter;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001:\u0002\u0012\u0013B1\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u0014\b\u0002\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\u0004\b\b\u0010\tJ\u0018\u0010\n\u001a\u00020\u00032\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0016J\u0018\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u00032\u0006\u0010\u0011\u001a\u00020\u000eH\u0016R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/presentation/adapter/GroupsAdapter;", "Landroidx/paging/PagingDataAdapter;", "Lcom/synapse/social/studioasinc/groupchat/data/model/Group;", "Lcom/synapse/social/studioasinc/groupchat/presentation/adapter/GroupsAdapter$GroupViewHolder;", "onGroupClick", "Lkotlin/Function1;", "", "onGroupLongClick", "<init>", "(Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "", "onBindViewHolder", "holder", "position", "GroupViewHolder", "GroupDiffCallback", "app_release"})
public final class GroupsAdapter extends androidx.paging.PagingDataAdapter<com.synapse.social.studioasinc.groupchat.data.model.Group, com.synapse.social.studioasinc.groupchat.presentation.adapter.GroupsAdapter.GroupViewHolder> {
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function1<com.synapse.social.studioasinc.groupchat.data.model.Group, kotlin.Unit> onGroupClick = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function1<com.synapse.social.studioasinc.groupchat.data.model.Group, kotlin.Unit> onGroupLongClick = null;
    
    public GroupsAdapter(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.synapse.social.studioasinc.groupchat.data.model.Group, kotlin.Unit> onGroupClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.synapse.social.studioasinc.groupchat.data.model.Group, kotlin.Unit> onGroupLongClick) {
        super(null, null);
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public com.synapse.social.studioasinc.groupchat.presentation.adapter.GroupsAdapter.GroupViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.presentation.adapter.GroupsAdapter.GroupViewHolder holder, int position) {
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0003\u0010\u0004J\u0018\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\u0002H\u0016J\u0018\u0010\t\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\u0002H\u0016\u00a8\u0006\n"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/presentation/adapter/GroupsAdapter$GroupDiffCallback;", "Landroidx/recyclerview/widget/DiffUtil$ItemCallback;", "Lcom/synapse/social/studioasinc/groupchat/data/model/Group;", "<init>", "()V", "areItemsTheSame", "", "oldItem", "newItem", "areContentsTheSame", "app_release"})
    static final class GroupDiffCallback extends androidx.recyclerview.widget.DiffUtil.ItemCallback<com.synapse.social.studioasinc.groupchat.data.model.Group> {
        
        public GroupDiffCallback() {
            super();
        }
        
        @java.lang.Override()
        public boolean areItemsTheSame(@org.jetbrains.annotations.NotNull()
        com.synapse.social.studioasinc.groupchat.data.model.Group oldItem, @org.jetbrains.annotations.NotNull()
        com.synapse.social.studioasinc.groupchat.data.model.Group newItem) {
            return false;
        }
        
        @java.lang.Override()
        public boolean areContentsTheSame(@org.jetbrains.annotations.NotNull()
        com.synapse.social.studioasinc.groupchat.data.model.Group oldItem, @org.jetbrains.annotations.NotNull()
        com.synapse.social.studioasinc.groupchat.data.model.Group newItem) {
            return false;
        }
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B7\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005\u0012\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005\u00a2\u0006\u0004\b\t\u0010\nJ\u000e\u0010\u000b\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u0006R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/presentation/adapter/GroupsAdapter$GroupViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lcom/synapse/social/studioasinc/databinding/ItemGroupBinding;", "onGroupClick", "Lkotlin/Function1;", "Lcom/synapse/social/studioasinc/groupchat/data/model/Group;", "", "onGroupLongClick", "<init>", "(Lcom/synapse/social/studioasinc/databinding/ItemGroupBinding;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "bind", "group", "app_release"})
    public static final class GroupViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final com.synapse.social.studioasinc.databinding.ItemGroupBinding binding = null;
        @org.jetbrains.annotations.NotNull()
        private final kotlin.jvm.functions.Function1<com.synapse.social.studioasinc.groupchat.data.model.Group, kotlin.Unit> onGroupClick = null;
        @org.jetbrains.annotations.NotNull()
        private final kotlin.jvm.functions.Function1<com.synapse.social.studioasinc.groupchat.data.model.Group, kotlin.Unit> onGroupLongClick = null;
        
        public GroupViewHolder(@org.jetbrains.annotations.NotNull()
        com.synapse.social.studioasinc.databinding.ItemGroupBinding binding, @org.jetbrains.annotations.NotNull()
        kotlin.jvm.functions.Function1<? super com.synapse.social.studioasinc.groupchat.data.model.Group, kotlin.Unit> onGroupClick, @org.jetbrains.annotations.NotNull()
        kotlin.jvm.functions.Function1<? super com.synapse.social.studioasinc.groupchat.data.model.Group, kotlin.Unit> onGroupLongClick) {
            super(null);
        }
        
        public final void bind(@org.jetbrains.annotations.NotNull()
        com.synapse.social.studioasinc.groupchat.data.model.Group group) {
        }
    }
}