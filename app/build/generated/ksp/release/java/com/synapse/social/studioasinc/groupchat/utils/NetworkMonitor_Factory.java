package com.synapse.social.studioasinc.groupchat.utils;

import android.content.Context;
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
public final class NetworkMonitor_Factory implements Factory<NetworkMonitor> {
  private final Provider<Context> contextProvider;

  public NetworkMonitor_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public NetworkMonitor get() {
    return newInstance(contextProvider.get());
  }

  public static NetworkMonitor_Factory create(Provider<Context> contextProvider) {
    return new NetworkMonitor_Factory(contextProvider);
  }

  public static NetworkMonitor newInstance(Context context) {
    return new NetworkMonitor(context);
  }
}
