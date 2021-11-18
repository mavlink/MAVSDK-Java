package io.mavsdk.internal;

import io.mavsdk.Plugin;
import io.reactivex.annotations.NonNull;

public class DoubleCheckBind<T extends Plugin> implements Provider<T> {

  private final Provider<T> provider;
  private volatile T instance = null;

  private DoubleCheckBind(@NonNull Provider<T> provider) {
    this.provider = provider;
  }

  @Override
  public T get() {
    if (instance == null) {
      synchronized (this) {
        if (instance == null) {
          instance = provider.get();
          MavsdkExecutors.bindExecutor().execute(instance::bind);
        }
      }
    }
    return instance;
  }

  public static <T extends Plugin> Provider<T> provider(@NonNull Provider<T> provider) {
    return new DoubleCheckBind<>(provider);
  }
}
