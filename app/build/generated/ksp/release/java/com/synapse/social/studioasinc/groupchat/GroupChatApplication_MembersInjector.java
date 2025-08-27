package com.synapse.social.studioasinc.groupchat;

import com.synapse.social.studioasinc.groupchat.data.repository.impl.UserRepositoryImpl;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class GroupChatApplication_MembersInjector implements MembersInjector<GroupChatApplication> {
  private final Provider<UserRepositoryImpl> userRepositoryProvider;

  public GroupChatApplication_MembersInjector(Provider<UserRepositoryImpl> userRepositoryProvider) {
    this.userRepositoryProvider = userRepositoryProvider;
  }

  public static MembersInjector<GroupChatApplication> create(
      Provider<UserRepositoryImpl> userRepositoryProvider) {
    return new GroupChatApplication_MembersInjector(userRepositoryProvider);
  }

  @Override
  public void injectMembers(GroupChatApplication instance) {
    injectUserRepository(instance, userRepositoryProvider.get());
  }

  @InjectedFieldSignature("com.synapse.social.studioasinc.groupchat.GroupChatApplication.userRepository")
  public static void injectUserRepository(GroupChatApplication instance,
      UserRepositoryImpl userRepository) {
    instance.userRepository = userRepository;
  }
}
