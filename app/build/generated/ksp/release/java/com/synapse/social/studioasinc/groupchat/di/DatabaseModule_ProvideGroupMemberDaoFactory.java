package com.synapse.social.studioasinc.groupchat.di;

import com.synapse.social.studioasinc.groupchat.data.local.dao.GroupMemberDao;
import com.synapse.social.studioasinc.groupchat.data.local.database.GroupChatDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class DatabaseModule_ProvideGroupMemberDaoFactory implements Factory<GroupMemberDao> {
  private final Provider<GroupChatDatabase> databaseProvider;

  public DatabaseModule_ProvideGroupMemberDaoFactory(Provider<GroupChatDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public GroupMemberDao get() {
    return provideGroupMemberDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideGroupMemberDaoFactory create(
      Provider<GroupChatDatabase> databaseProvider) {
    return new DatabaseModule_ProvideGroupMemberDaoFactory(databaseProvider);
  }

  public static GroupMemberDao provideGroupMemberDao(GroupChatDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideGroupMemberDao(database));
  }
}
