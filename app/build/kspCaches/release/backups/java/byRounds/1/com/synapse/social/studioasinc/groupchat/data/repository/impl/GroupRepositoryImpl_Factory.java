package com.synapse.social.studioasinc.groupchat.data.repository.impl;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.synapse.social.studioasinc.groupchat.data.local.dao.GroupDao;
import com.synapse.social.studioasinc.groupchat.data.local.dao.GroupMemberDao;
import com.synapse.social.studioasinc.groupchat.data.local.dao.UserDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class GroupRepositoryImpl_Factory implements Factory<GroupRepositoryImpl> {
  private final Provider<GroupDao> groupDaoProvider;

  private final Provider<GroupMemberDao> groupMemberDaoProvider;

  private final Provider<UserDao> userDaoProvider;

  private final Provider<FirebaseAuth> firebaseAuthProvider;

  private final Provider<FirebaseDatabase> firebaseDatabaseProvider;

  private final Provider<FirebaseStorage> firebaseStorageProvider;

  public GroupRepositoryImpl_Factory(Provider<GroupDao> groupDaoProvider,
      Provider<GroupMemberDao> groupMemberDaoProvider, Provider<UserDao> userDaoProvider,
      Provider<FirebaseAuth> firebaseAuthProvider,
      Provider<FirebaseDatabase> firebaseDatabaseProvider,
      Provider<FirebaseStorage> firebaseStorageProvider) {
    this.groupDaoProvider = groupDaoProvider;
    this.groupMemberDaoProvider = groupMemberDaoProvider;
    this.userDaoProvider = userDaoProvider;
    this.firebaseAuthProvider = firebaseAuthProvider;
    this.firebaseDatabaseProvider = firebaseDatabaseProvider;
    this.firebaseStorageProvider = firebaseStorageProvider;
  }

  @Override
  public GroupRepositoryImpl get() {
    return newInstance(groupDaoProvider.get(), groupMemberDaoProvider.get(), userDaoProvider.get(), firebaseAuthProvider.get(), firebaseDatabaseProvider.get(), firebaseStorageProvider.get());
  }

  public static GroupRepositoryImpl_Factory create(Provider<GroupDao> groupDaoProvider,
      Provider<GroupMemberDao> groupMemberDaoProvider, Provider<UserDao> userDaoProvider,
      Provider<FirebaseAuth> firebaseAuthProvider,
      Provider<FirebaseDatabase> firebaseDatabaseProvider,
      Provider<FirebaseStorage> firebaseStorageProvider) {
    return new GroupRepositoryImpl_Factory(groupDaoProvider, groupMemberDaoProvider, userDaoProvider, firebaseAuthProvider, firebaseDatabaseProvider, firebaseStorageProvider);
  }

  public static GroupRepositoryImpl newInstance(GroupDao groupDao, GroupMemberDao groupMemberDao,
      UserDao userDao, FirebaseAuth firebaseAuth, FirebaseDatabase firebaseDatabase,
      FirebaseStorage firebaseStorage) {
    return new GroupRepositoryImpl(groupDao, groupMemberDao, userDao, firebaseAuth, firebaseDatabase, firebaseStorage);
  }
}
