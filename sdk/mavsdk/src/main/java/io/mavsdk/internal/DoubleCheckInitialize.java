package io.mavsdk.internal;

import io.mavsdk.MavsdkExecutors;
import io.mavsdk.Plugin;
import io.reactivex.annotations.NonNull;

/**
 * An implementation of {@link Provider} uses the Double-Check idiom to construct
 * and initialize instances of {@link Plugin} only once. The memoized instances
 * are provided in the subsequent calls of `get()`.
 *
 * <p>This enables lazy construction and initialization of {@link Plugin}s, reducing
 * the startup time of the application.
 */
public class DoubleCheckInitialize<T extends Plugin> implements Provider<T> {

  private final Provider<T> provider;
  private volatile T instance = null;

  private DoubleCheckInitialize(@NonNull Provider<T> provider) {
    this.provider = provider;
  }

  @Override
  @NonNull
  public T get() {
    if (instance == null) {
      synchronized (this) {
        if (instance == null) {
          instance = provider.get();
          MavsdkExecutors.initializeQueue().execute(instance::initialize);
        }
      }
    }
    return instance;
  }

  public void dispose() {
    if (instance != null) {
      synchronized (this) {
        if (instance != null) {
          MavsdkExecutors.initializeQueue().execute(instance::dispose);
        }
      }
    }
  }

  /**
   * Wraps a {@link Provider} into a {@link DoubleCheckInitialize}.
   *
   * @param provider The {@link Provider} to wrap.
   * @return A {@link DoubleCheckInitialize} wrapping the given {@link Provider}.
   */
  @NonNull
  public static <T extends Plugin> Provider<T> provider(@NonNull Provider<T> provider) {
    return new DoubleCheckInitialize<>(provider);
  }
}
