package io.mavsdk.internal;

import io.mavsdk.Plugin;
import io.reactivex.annotations.NonNull;

/**
 * Provides instances of {@link Plugin} `T`, enabling a callback mechanism for
 * constructor invocation.
 */
public interface Provider<T extends Plugin> {

  /**
   * Provides fully constructed instances of `T`.
   *
   * <p>Note: The instances may not have been initialized yet, i.e.,
   * {@link Plugin#initialize()} may not have been invoked.
   *
   * @return Instance of {@link Plugin} `T`
   */
  @NonNull
  T get();
}
