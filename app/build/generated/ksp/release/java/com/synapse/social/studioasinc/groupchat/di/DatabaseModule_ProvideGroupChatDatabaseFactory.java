package com.synapse.social.studioasinc.groupchat.di;

import android.content.Context;
import com.synapse.social.studioasinc.groupchat.data.local.database.GroupChatDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class DatabaseModule_ProvideGroupChatDatabaseFactory implements Factory<GroupChatDatabase> {
  private final Provider<Context> contextProvider;

  public DatabaseModule_ProvideGroupChatDatabaseFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public GroupChatDatabase get() {
    return provideGroupChatDatabase(contextProvider.get());
  }

  public static DatabaseModule_ProvideGroupChatDatabaseFactory create(
      Provider<Context> contextProvider) {
    return new DatabaseModule_ProvideGroupChatDatabaseFactory(contextProvider);
  }

  public static GroupChatDatabase provideGroupChatDatabase(Context context) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideGroupChatDatabase(context));
  }
}
