package com.synapse.social.studioasinc.groupchat.presentation.viewmodel;

import com.synapse.social.studioasinc.groupchat.data.repository.GroupRepository;
import com.synapse.social.studioasinc.groupchat.data.repository.MessageRepository;
import com.synapse.social.studioasinc.groupchat.data.repository.UserRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class GroupChatViewModel_Factory implements Factory<GroupChatViewModel> {
  private final Provider<MessageRepository> messageRepositoryProvider;

  private final Provider<GroupRepository> groupRepositoryProvider;

  private final Provider<UserRepository> userRepositoryProvider;

  public GroupChatViewModel_Factory(Provider<MessageRepository> messageRepositoryProvider,
      Provider<GroupRepository> groupRepositoryProvider,
      Provider<UserRepository> userRepositoryProvider) {
    this.messageRepositoryProvider = messageRepositoryProvider;
    this.groupRepositoryProvider = groupRepositoryProvider;
    this.userRepositoryProvider = userRepositoryProvider;
  }

  @Override
  public GroupChatViewModel get() {
    return newInstance(messageRepositoryProvider.get(), groupRepositoryProvider.get(), userRepositoryProvider.get());
  }

  public static GroupChatViewModel_Factory create(
      Provider<MessageRepository> messageRepositoryProvider,
      Provider<GroupRepository> groupRepositoryProvider,
      Provider<UserRepository> userRepositoryProvider) {
    return new GroupChatViewModel_Factory(messageRepositoryProvider, groupRepositoryProvider, userRepositoryProvider);
  }

  public static GroupChatViewModel newInstance(MessageRepository messageRepository,
      GroupRepository groupRepository, UserRepository userRepository) {
    return new GroupChatViewModel(messageRepository, groupRepository, userRepository);
  }
}
