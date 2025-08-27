package com.synapse.social.studioasinc.groupchat.di;

import com.synapse.social.studioasinc.groupchat.data.local.dao.GroupDao;
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
public final class DatabaseModule_ProvideGroupDaoFactory implements Factory<GroupDao> {
  private final Provider<GroupChatDatabase> databaseProvider;

  public DatabaseModule_ProvideGroupDaoFactory(Provider<GroupChatDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public GroupDao get() {
    return provideGroupDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideGroupDaoFactory create(
      Provider<GroupChatDatabase> databaseProvider) {
    return new DatabaseModule_ProvideGroupDaoFactory(databaseProvider);
  }

  public static GroupDao provideGroupDao(GroupChatDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideGroupDao(database));
  }
}
