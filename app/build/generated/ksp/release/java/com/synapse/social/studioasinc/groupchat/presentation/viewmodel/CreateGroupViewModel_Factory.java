package com.synapse.social.studioasinc.groupchat.presentation.viewmodel;

import com.synapse.social.studioasinc.groupchat.data.repository.GroupRepository;
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
public final class CreateGroupViewModel_Factory implements Factory<CreateGroupViewModel> {
  private final Provider<GroupRepository> groupRepositoryProvider;

  private final Provider<UserRepository> userRepositoryProvider;

  public CreateGroupViewModel_Factory(Provider<GroupRepository> groupRepositoryProvider,
      Provider<UserRepository> userRepositoryProvider) {
    this.groupRepositoryProvider = groupRepositoryProvider;
    this.userRepositoryProvider = userRepositoryProvider;
  }

  @Override
  public CreateGroupViewModel get() {
    return newInstance(groupRepositoryProvider.get(), userRepositoryProvider.get());
  }

  public static CreateGroupViewModel_Factory create(
      Provider<GroupRepository> groupRepositoryProvider,
      Provider<UserRepository> userRepositoryProvider) {
    return new CreateGroupViewModel_Factory(groupRepositoryProvider, userRepositoryProvider);
  }

  public static CreateGroupViewModel newInstance(GroupRepository groupRepository,
      UserRepository userRepository) {
    return new CreateGroupViewModel(groupRepository, userRepository);
  }
}
