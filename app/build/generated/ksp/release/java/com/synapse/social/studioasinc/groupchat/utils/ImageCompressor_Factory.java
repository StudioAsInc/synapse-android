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
public final class ImageCompressor_Factory implements Factory<ImageCompressor> {
  private final Provider<Context> contextProvider;

  public ImageCompressor_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public ImageCompressor get() {
    return newInstance(contextProvider.get());
  }

  public static ImageCompressor_Factory create(Provider<Context> contextProvider) {
    return new ImageCompressor_Factory(contextProvider);
  }

  public static ImageCompressor newInstance(Context context) {
    return new ImageCompressor(context);
  }
}
