package com.synapse.social.studioasinc.groupchat.data.repository.impl;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
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
public final class UserRepositoryImpl_Factory implements Factory<UserRepositoryImpl> {
  private final Provider<UserDao> userDaoProvider;

  private final Provider<FirebaseAuth> firebaseAuthProvider;

  private final Provider<FirebaseDatabase> firebaseDatabaseProvider;

  private final Provider<FirebaseStorage> firebaseStorageProvider;

  private final Provider<FirebaseMessaging> firebaseMessagingProvider;

  public UserRepositoryImpl_Factory(Provider<UserDao> userDaoProvider,
      Provider<FirebaseAuth> firebaseAuthProvider,
      Provider<FirebaseDatabase> firebaseDatabaseProvider,
      Provider<FirebaseStorage> firebaseStorageProvider,
      Provider<FirebaseMessaging> firebaseMessagingProvider) {
    this.userDaoProvider = userDaoProvider;
    this.firebaseAuthProvider = firebaseAuthProvider;
    this.firebaseDatabaseProvider = firebaseDatabaseProvider;
    this.firebaseStorageProvider = firebaseStorageProvider;
    this.firebaseMessagingProvider = firebaseMessagingProvider;
  }

  @Override
  public UserRepositoryImpl get() {
    return newInstance(userDaoProvider.get(), firebaseAuthProvider.get(), firebaseDatabaseProvider.get(), firebaseStorageProvider.get(), firebaseMessagingProvider.get());
  }

  public static UserRepositoryImpl_Factory create(Provider<UserDao> userDaoProvider,
      Provider<FirebaseAuth> firebaseAuthProvider,
      Provider<FirebaseDatabase> firebaseDatabaseProvider,
      Provider<FirebaseStorage> firebaseStorageProvider,
      Provider<FirebaseMessaging> firebaseMessagingProvider) {
    return new UserRepositoryImpl_Factory(userDaoProvider, firebaseAuthProvider, firebaseDatabaseProvider, firebaseStorageProvider, firebaseMessagingProvider);
  }

  public static UserRepositoryImpl newInstance(UserDao userDao, FirebaseAuth firebaseAuth,
      FirebaseDatabase firebaseDatabase, FirebaseStorage firebaseStorage,
      FirebaseMessaging firebaseMessaging) {
    return new UserRepositoryImpl(userDao, firebaseAuth, firebaseDatabase, firebaseStorage, firebaseMessaging);
  }
}
