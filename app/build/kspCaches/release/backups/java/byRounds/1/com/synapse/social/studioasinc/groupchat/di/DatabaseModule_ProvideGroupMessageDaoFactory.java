package com.synapse.social.studioasinc.groupchat.di;

import com.synapse.social.studioasinc.groupchat.data.local.dao.GroupMessageDao;
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
public final class DatabaseModule_ProvideGroupMessageDaoFactory implements Factory<GroupMessageDao> {
  private final Provider<GroupChatDatabase> databaseProvider;

  public DatabaseModule_ProvideGroupMessageDaoFactory(
      Provider<GroupChatDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public GroupMessageDao get() {
    return provideGroupMessageDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideGroupMessageDaoFactory create(
      Provider<GroupChatDatabase> databaseProvider) {
    return new DatabaseModule_ProvideGroupMessageDaoFactory(databaseProvider);
  }

  public static GroupMessageDao provideGroupMessageDao(GroupChatDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideGroupMessageDao(database));
  }
}
