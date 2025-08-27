package com.synapse.social.studioasinc.groupchat.service;

import com.synapse.social.studioasinc.groupchat.data.repository.UserRepository;
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
public final class GroupChatFirebaseMessagingService_MembersInjector implements MembersInjector<GroupChatFirebaseMessagingService> {
  private final Provider<UserRepository> userRepositoryProvider;

  public GroupChatFirebaseMessagingService_MembersInjector(
      Provider<UserRepository> userRepositoryProvider) {
    this.userRepositoryProvider = userRepositoryProvider;
  }

  public static MembersInjector<GroupChatFirebaseMessagingService> create(
      Provider<UserRepository> userRepositoryProvider) {
    return new GroupChatFirebaseMessagingService_MembersInjector(userRepositoryProvider);
  }

  @Override
  public void injectMembers(GroupChatFirebaseMessagingService instance) {
    injectUserRepository(instance, userRepositoryProvider.get());
  }

  @InjectedFieldSignature("com.synapse.social.studioasinc.groupchat.service.GroupChatFirebaseMessagingService.userRepository")
  public static void injectUserRepository(GroupChatFirebaseMessagingService instance,
      UserRepository userRepository) {
    instance.userRepository = userRepository;
  }
}
