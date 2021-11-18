package io.mavsdk;

import io.reactivex.annotations.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Executors for the MAVSDK-Java library.
 *
 * This class uses the Initialize-On-Demand Holder idiom to lazily construct the executors.
 */
public class MavsdkExecutors {

  private MavsdkExecutors() {
  }

  private static final class InitializerHolder {
    private static final Executor INITIALIZER = Executors.newSingleThreadExecutor();
  }

  /**
   * The `Executor` for initializing the {@link Plugin}s in the background.
   *
   * Note: For Android, this executor should be used to run the MavsdkServer before
   * any {@link Plugin} is called/initialized. Ideally, use this to run the MavsdkServer
   * before constructing the {@link System} instance.
   *
   * @return The `Executor` for initializing the {@link Plugin}s in the background.
   */
  @NonNull
  public static Executor initializer() {
    return InitializerHolder.INITIALIZER;
  }
}
