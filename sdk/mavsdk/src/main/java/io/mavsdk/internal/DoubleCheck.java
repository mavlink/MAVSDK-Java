package io.mavsdk.internal;

import io.mavsdk.Plugin;
import io.reactivex.annotations.NonNull;

public class DoubleCheck<T extends Plugin> implements Provider<T> {

  private final Provider<T> provider;
  private volatile T instance = null;

  private DoubleCheck(@NonNull Provider<T> provider) {
    this.provider = provider;
  }

  @Override
  public T get() {
    if (instance == null) {
      synchronized (this) {
        if (instance == null) {
          instance = provider.get();
        }
      }
    }
    return instance;
  }

  public static <T extends Plugin> Provider<T> create(@NonNull Provider<T> provider) {
    return new DoubleCheck<>(provider);
  }
}
