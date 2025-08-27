package com.synapse.social.studioasinc.groupchat.di;

import com.google.firebase.database.FirebaseDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class FirebaseModule_ProvideFirebaseDatabaseFactory implements Factory<FirebaseDatabase> {
  @Override
  public FirebaseDatabase get() {
    return provideFirebaseDatabase();
  }

  public static FirebaseModule_ProvideFirebaseDatabaseFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static FirebaseDatabase provideFirebaseDatabase() {
    return Preconditions.checkNotNullFromProvides(FirebaseModule.INSTANCE.provideFirebaseDatabase());
  }

  private static final class InstanceHolder {
    private static final FirebaseModule_ProvideFirebaseDatabaseFactory INSTANCE = new FirebaseModule_ProvideFirebaseDatabaseFactory();
  }
}
