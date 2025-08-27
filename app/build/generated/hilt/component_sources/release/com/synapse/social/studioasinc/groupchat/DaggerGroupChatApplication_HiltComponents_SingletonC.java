package com.synapse.social.studioasinc.groupchat;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.hilt.work.HiltWrapper_WorkerFactoryModule;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.synapse.social.studioasinc.groupchat.data.local.dao.GroupDao;
import com.synapse.social.studioasinc.groupchat.data.local.dao.GroupMemberDao;
import com.synapse.social.studioasinc.groupchat.data.local.dao.GroupMessageDao;
import com.synapse.social.studioasinc.groupchat.data.local.dao.UserDao;
import com.synapse.social.studioasinc.groupchat.data.local.database.GroupChatDatabase;
import com.synapse.social.studioasinc.groupchat.data.repository.impl.GroupRepositoryImpl;
import com.synapse.social.studioasinc.groupchat.data.repository.impl.MessageRepositoryImpl;
import com.synapse.social.studioasinc.groupchat.data.repository.impl.UserRepositoryImpl;
import com.synapse.social.studioasinc.groupchat.di.DatabaseModule;
import com.synapse.social.studioasinc.groupchat.di.DatabaseModule_ProvideGroupChatDatabaseFactory;
import com.synapse.social.studioasinc.groupchat.di.DatabaseModule_ProvideGroupDaoFactory;
import com.synapse.social.studioasinc.groupchat.di.DatabaseModule_ProvideGroupMemberDaoFactory;
import com.synapse.social.studioasinc.groupchat.di.DatabaseModule_ProvideGroupMessageDaoFactory;
import com.synapse.social.studioasinc.groupchat.di.DatabaseModule_ProvideUserDaoFactory;
import com.synapse.social.studioasinc.groupchat.di.FirebaseModule;
import com.synapse.social.studioasinc.groupchat.di.FirebaseModule_ProvideFirebaseAuthFactory;
import com.synapse.social.studioasinc.groupchat.di.FirebaseModule_ProvideFirebaseDatabaseFactory;
import com.synapse.social.studioasinc.groupchat.di.FirebaseModule_ProvideFirebaseMessagingFactory;
import com.synapse.social.studioasinc.groupchat.di.FirebaseModule_ProvideFirebaseStorageFactory;
import com.synapse.social.studioasinc.groupchat.presentation.fragments.InboxCallsFragment;
import com.synapse.social.studioasinc.groupchat.presentation.fragments.InboxChatsFragment;
import com.synapse.social.studioasinc.groupchat.presentation.fragments.InboxStoriesFragment;
import com.synapse.social.studioasinc.groupchat.presentation.ui.CreateGroupActivity;
import com.synapse.social.studioasinc.groupchat.presentation.ui.GroupChatActivity;
import com.synapse.social.studioasinc.groupchat.presentation.ui.InboxActivity;
import com.synapse.social.studioasinc.groupchat.presentation.viewmodel.CreateGroupViewModel;
import com.synapse.social.studioasinc.groupchat.presentation.viewmodel.CreateGroupViewModel_HiltModules_KeyModule_ProvideFactory;
import com.synapse.social.studioasinc.groupchat.presentation.viewmodel.GroupChatViewModel;
import com.synapse.social.studioasinc.groupchat.presentation.viewmodel.GroupChatViewModel_HiltModules_KeyModule_ProvideFactory;
import com.synapse.social.studioasinc.groupchat.presentation.viewmodel.InboxViewModel;
import com.synapse.social.studioasinc.groupchat.presentation.viewmodel.InboxViewModel_HiltModules_KeyModule_ProvideFactory;
import com.synapse.social.studioasinc.groupchat.service.GroupChatFirebaseMessagingService;
import com.synapse.social.studioasinc.groupchat.service.GroupChatFirebaseMessagingService_MembersInjector;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.flags.HiltWrapper_FragmentGetContextFix_FragmentGetContextFixModule;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.Preconditions;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class DaggerGroupChatApplication_HiltComponents_SingletonC {
  private DaggerGroupChatApplication_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    /**
     * @deprecated This module is declared, but an instance is not used in the component. This method is a no-op. For more, see https://dagger.dev/unused-modules.
     */
    @Deprecated
    public Builder databaseModule(DatabaseModule databaseModule) {
      Preconditions.checkNotNull(databaseModule);
      return this;
    }

    /**
     * @deprecated This module is declared, but an instance is not used in the component. This method is a no-op. For more, see https://dagger.dev/unused-modules.
     */
    @Deprecated
    public Builder firebaseModule(FirebaseModule firebaseModule) {
      Preconditions.checkNotNull(firebaseModule);
      return this;
    }

    /**
     * @deprecated This module is declared, but an instance is not used in the component. This method is a no-op. For more, see https://dagger.dev/unused-modules.
     */
    @Deprecated
    public Builder hiltWrapper_FragmentGetContextFix_FragmentGetContextFixModule(
        HiltWrapper_FragmentGetContextFix_FragmentGetContextFixModule hiltWrapper_FragmentGetContextFix_FragmentGetContextFixModule) {
      Preconditions.checkNotNull(hiltWrapper_FragmentGetContextFix_FragmentGetContextFixModule);
      return this;
    }

    /**
     * @deprecated This module is declared, but an instance is not used in the component. This method is a no-op. For more, see https://dagger.dev/unused-modules.
     */
    @Deprecated
    public Builder hiltWrapper_WorkerFactoryModule(
        HiltWrapper_WorkerFactoryModule hiltWrapper_WorkerFactoryModule) {
      Preconditions.checkNotNull(hiltWrapper_WorkerFactoryModule);
      return this;
    }

    public GroupChatApplication_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements GroupChatApplication_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public GroupChatApplication_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements GroupChatApplication_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public GroupChatApplication_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements GroupChatApplication_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public GroupChatApplication_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements GroupChatApplication_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public GroupChatApplication_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements GroupChatApplication_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public GroupChatApplication_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements GroupChatApplication_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public GroupChatApplication_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements GroupChatApplication_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public GroupChatApplication_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends GroupChatApplication_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends GroupChatApplication_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public void injectInboxCallsFragment(InboxCallsFragment inboxCallsFragment) {
    }

    @Override
    public void injectInboxChatsFragment(InboxChatsFragment inboxChatsFragment) {
    }

    @Override
    public void injectInboxStoriesFragment(InboxStoriesFragment inboxStoriesFragment) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends GroupChatApplication_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends GroupChatApplication_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectCreateGroupActivity(CreateGroupActivity createGroupActivity) {
    }

    @Override
    public void injectGroupChatActivity(GroupChatActivity groupChatActivity) {
    }

    @Override
    public void injectInboxActivity(InboxActivity inboxActivity) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Set<String> getViewModelKeys() {
      return ImmutableSet.<String>of(CreateGroupViewModel_HiltModules_KeyModule_ProvideFactory.provide(), GroupChatViewModel_HiltModules_KeyModule_ProvideFactory.provide(), InboxViewModel_HiltModules_KeyModule_ProvideFactory.provide());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }
  }

  private static final class ViewModelCImpl extends GroupChatApplication_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<CreateGroupViewModel> createGroupViewModelProvider;

    private Provider<GroupChatViewModel> groupChatViewModelProvider;

    private Provider<InboxViewModel> inboxViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.createGroupViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.groupChatViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.inboxViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
    }

    @Override
    public Map<String, Provider<ViewModel>> getHiltViewModelMap() {
      return ImmutableMap.<String, Provider<ViewModel>>of("com.synapse.social.studioasinc.groupchat.presentation.viewmodel.CreateGroupViewModel", ((Provider) createGroupViewModelProvider), "com.synapse.social.studioasinc.groupchat.presentation.viewmodel.GroupChatViewModel", ((Provider) groupChatViewModelProvider), "com.synapse.social.studioasinc.groupchat.presentation.viewmodel.InboxViewModel", ((Provider) inboxViewModelProvider));
    }

    @Override
    public Map<String, Object> getHiltViewModelAssistedMap() {
      return ImmutableMap.<String, Object>of();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.synapse.social.studioasinc.groupchat.presentation.viewmodel.CreateGroupViewModel 
          return (T) new CreateGroupViewModel(singletonCImpl.groupRepositoryImplProvider.get(), singletonCImpl.userRepositoryImplProvider.get());

          case 1: // com.synapse.social.studioasinc.groupchat.presentation.viewmodel.GroupChatViewModel 
          return (T) new GroupChatViewModel(singletonCImpl.messageRepositoryImplProvider.get(), singletonCImpl.groupRepositoryImplProvider.get(), singletonCImpl.userRepositoryImplProvider.get());

          case 2: // com.synapse.social.studioasinc.groupchat.presentation.viewmodel.InboxViewModel 
          return (T) new InboxViewModel(singletonCImpl.groupRepositoryImplProvider.get(), singletonCImpl.userRepositoryImplProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends GroupChatApplication_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends GroupChatApplication_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }

    @Override
    public void injectGroupChatFirebaseMessagingService(
        GroupChatFirebaseMessagingService groupChatFirebaseMessagingService) {
      injectGroupChatFirebaseMessagingService2(groupChatFirebaseMessagingService);
    }

    @CanIgnoreReturnValue
    private GroupChatFirebaseMessagingService injectGroupChatFirebaseMessagingService2(
        GroupChatFirebaseMessagingService instance) {
      GroupChatFirebaseMessagingService_MembersInjector.injectUserRepository(instance, singletonCImpl.userRepositoryImplProvider.get());
      return instance;
    }
  }

  private static final class SingletonCImpl extends GroupChatApplication_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<GroupChatDatabase> provideGroupChatDatabaseProvider;

    private Provider<FirebaseAuth> provideFirebaseAuthProvider;

    private Provider<FirebaseDatabase> provideFirebaseDatabaseProvider;

    private Provider<FirebaseStorage> provideFirebaseStorageProvider;

    private Provider<FirebaseMessaging> provideFirebaseMessagingProvider;

    private Provider<UserRepositoryImpl> userRepositoryImplProvider;

    private Provider<GroupRepositoryImpl> groupRepositoryImplProvider;

    private Provider<MessageRepositoryImpl> messageRepositoryImplProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private UserDao userDao() {
      return DatabaseModule_ProvideUserDaoFactory.provideUserDao(provideGroupChatDatabaseProvider.get());
    }

    private GroupDao groupDao() {
      return DatabaseModule_ProvideGroupDaoFactory.provideGroupDao(provideGroupChatDatabaseProvider.get());
    }

    private GroupMemberDao groupMemberDao() {
      return DatabaseModule_ProvideGroupMemberDaoFactory.provideGroupMemberDao(provideGroupChatDatabaseProvider.get());
    }

    private GroupMessageDao groupMessageDao() {
      return DatabaseModule_ProvideGroupMessageDaoFactory.provideGroupMessageDao(provideGroupChatDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideGroupChatDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<GroupChatDatabase>(singletonCImpl, 1));
      this.provideFirebaseAuthProvider = DoubleCheck.provider(new SwitchingProvider<FirebaseAuth>(singletonCImpl, 2));
      this.provideFirebaseDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<FirebaseDatabase>(singletonCImpl, 3));
      this.provideFirebaseStorageProvider = DoubleCheck.provider(new SwitchingProvider<FirebaseStorage>(singletonCImpl, 4));
      this.provideFirebaseMessagingProvider = DoubleCheck.provider(new SwitchingProvider<FirebaseMessaging>(singletonCImpl, 5));
      this.userRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<UserRepositoryImpl>(singletonCImpl, 0));
      this.groupRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<GroupRepositoryImpl>(singletonCImpl, 6));
      this.messageRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<MessageRepositoryImpl>(singletonCImpl, 7));
    }

    @Override
    public void injectGroupChatApplication(GroupChatApplication groupChatApplication) {
      injectGroupChatApplication2(groupChatApplication);
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return ImmutableSet.<Boolean>of();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    @CanIgnoreReturnValue
    private GroupChatApplication injectGroupChatApplication2(GroupChatApplication instance) {
      GroupChatApplication_MembersInjector.injectUserRepository(instance, userRepositoryImplProvider.get());
      return instance;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.synapse.social.studioasinc.groupchat.data.repository.impl.UserRepositoryImpl 
          return (T) new UserRepositoryImpl(singletonCImpl.userDao(), singletonCImpl.provideFirebaseAuthProvider.get(), singletonCImpl.provideFirebaseDatabaseProvider.get(), singletonCImpl.provideFirebaseStorageProvider.get(), singletonCImpl.provideFirebaseMessagingProvider.get());

          case 1: // com.synapse.social.studioasinc.groupchat.data.local.database.GroupChatDatabase 
          return (T) DatabaseModule_ProvideGroupChatDatabaseFactory.provideGroupChatDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 2: // com.google.firebase.auth.FirebaseAuth 
          return (T) FirebaseModule_ProvideFirebaseAuthFactory.provideFirebaseAuth();

          case 3: // com.google.firebase.database.FirebaseDatabase 
          return (T) FirebaseModule_ProvideFirebaseDatabaseFactory.provideFirebaseDatabase();

          case 4: // com.google.firebase.storage.FirebaseStorage 
          return (T) FirebaseModule_ProvideFirebaseStorageFactory.provideFirebaseStorage();

          case 5: // com.google.firebase.messaging.FirebaseMessaging 
          return (T) FirebaseModule_ProvideFirebaseMessagingFactory.provideFirebaseMessaging();

          case 6: // com.synapse.social.studioasinc.groupchat.data.repository.impl.GroupRepositoryImpl 
          return (T) new GroupRepositoryImpl(singletonCImpl.groupDao(), singletonCImpl.groupMemberDao(), singletonCImpl.userDao(), singletonCImpl.provideFirebaseAuthProvider.get(), singletonCImpl.provideFirebaseDatabaseProvider.get(), singletonCImpl.provideFirebaseStorageProvider.get());

          case 7: // com.synapse.social.studioasinc.groupchat.data.repository.impl.MessageRepositoryImpl 
          return (T) new MessageRepositoryImpl(singletonCImpl.groupMessageDao(), singletonCImpl.groupMemberDao(), singletonCImpl.provideFirebaseAuthProvider.get(), singletonCImpl.provideFirebaseDatabaseProvider.get(), singletonCImpl.provideFirebaseStorageProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
