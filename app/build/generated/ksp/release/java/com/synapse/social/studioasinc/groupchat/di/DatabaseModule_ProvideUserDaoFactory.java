package com.synapse.social.studioasinc.groupchat.di;

import com.synapse.social.studioasinc.groupchat.data.local.dao.UserDao;
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
public final class DatabaseModule_ProvideUserDaoFactory implements Factory<UserDao> {
  private final Provider<GroupChatDatabase> databaseProvider;

  public DatabaseModule_ProvideUserDaoFactory(Provider<GroupChatDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public UserDao get() {
    return provideUserDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideUserDaoFactory create(
      Provider<GroupChatDatabase> databaseProvider) {
    return new DatabaseModule_ProvideUserDaoFactory(databaseProvider);
  }

  public static UserDao provideUserDao(GroupChatDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideUserDao(database));
  }
}
