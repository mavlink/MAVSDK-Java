package io.mavsdk.internal;

import io.mavsdk.MavsdkExecutors;
import io.mavsdk.Plugin;
import io.reactivex.annotations.NonNull;

/**
 * Uses the Double-Check idiom to lazily construct and initialize instances of
 * {@link Plugin} only once by adding the initialize method execution to the
 * `mavsdk-event-queue`. The memoized instances are provided in the subsequent
 * calls of `get()`.
 *
 * <p>The dispose method can be used to dispose of the underlying {@link Plugin}.
 * The {@link Plugin} is disposed only if it has been initialized.
 *
 * <p>This class is thread-safe and makes sure that the `mavsdk-event-queue` has
 * executions in the correct order.
 */
public class PluginWrapper<T extends Plugin> {

  private final Provider<T> provider;
  private volatile T instance = null;

  private PluginWrapper(@NonNull Provider<T> provider) {
    this.provider = provider;
  }

  /**
   * Uses the {@link Provider} to lazily constructs the instance of {@link Plugin}
   * and adds the initialization to the `mavsdk-event-queue`.
   *
   * @return The instance of {@link Plugin}.
   */
  @NonNull
  public T get() {
    if (instance == null) {
      synchronized (this) {
        if (instance == null) {
          instance = provider.get();
          MavsdkExecutors.eventQueue().execute(instance::initialize);
        }
      }
    }
    return instance;
  }

  /**
   * Disposes the underlying {@link Plugin} if it has been initialized by adding
   * the dispose method execution to the `mavsdk-event-queue`.
   */
  public void dispose() {
    if (instance != null) {
      synchronized (this) {
        if (instance != null) {
          MavsdkExecutors.eventQueue().execute(instance::dispose);
        }
      }
    }
  }

  /**
   * Wraps a {@link Provider} into a {@link PluginWrapper}.
   *
   * @param provider The {@link Provider} to wrap.
   * @return A {@link PluginWrapper} wrapping the given {@link Provider}.
   */
  @NonNull
  public static <T extends Plugin> PluginWrapper<T> wrap(@NonNull Provider<T> provider) {
    return new PluginWrapper<T>(provider);
  }
}
