package com.synapse.social.studioasinc.groupchat.data.repository.impl;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.synapse.social.studioasinc.groupchat.data.local.dao.GroupMemberDao;
import com.synapse.social.studioasinc.groupchat.data.local.dao.GroupMessageDao;
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
public final class MessageRepositoryImpl_Factory implements Factory<MessageRepositoryImpl> {
  private final Provider<GroupMessageDao> messageDaoProvider;

  private final Provider<GroupMemberDao> memberDaoProvider;

  private final Provider<FirebaseAuth> firebaseAuthProvider;

  private final Provider<FirebaseDatabase> firebaseDatabaseProvider;

  private final Provider<FirebaseStorage> firebaseStorageProvider;

  public MessageRepositoryImpl_Factory(Provider<GroupMessageDao> messageDaoProvider,
      Provider<GroupMemberDao> memberDaoProvider, Provider<FirebaseAuth> firebaseAuthProvider,
      Provider<FirebaseDatabase> firebaseDatabaseProvider,
      Provider<FirebaseStorage> firebaseStorageProvider) {
    this.messageDaoProvider = messageDaoProvider;
    this.memberDaoProvider = memberDaoProvider;
    this.firebaseAuthProvider = firebaseAuthProvider;
    this.firebaseDatabaseProvider = firebaseDatabaseProvider;
    this.firebaseStorageProvider = firebaseStorageProvider;
  }

  @Override
  public MessageRepositoryImpl get() {
    return newInstance(messageDaoProvider.get(), memberDaoProvider.get(), firebaseAuthProvider.get(), firebaseDatabaseProvider.get(), firebaseStorageProvider.get());
  }

  public static MessageRepositoryImpl_Factory create(Provider<GroupMessageDao> messageDaoProvider,
      Provider<GroupMemberDao> memberDaoProvider, Provider<FirebaseAuth> firebaseAuthProvider,
      Provider<FirebaseDatabase> firebaseDatabaseProvider,
      Provider<FirebaseStorage> firebaseStorageProvider) {
    return new MessageRepositoryImpl_Factory(messageDaoProvider, memberDaoProvider, firebaseAuthProvider, firebaseDatabaseProvider, firebaseStorageProvider);
  }

  public static MessageRepositoryImpl newInstance(GroupMessageDao messageDao,
      GroupMemberDao memberDao, FirebaseAuth firebaseAuth, FirebaseDatabase firebaseDatabase,
      FirebaseStorage firebaseStorage) {
    return new MessageRepositoryImpl(messageDao, memberDao, firebaseAuth, firebaseDatabase, firebaseStorage);
  }
}
