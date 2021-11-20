package io.mavsdk;

import io.reactivex.annotations.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Executors for the MAVSDK-Java library.
 *
 * <p>This class uses the Initialize-On-Demand Holder idiom to lazily construct
 * the executors.
 */
public class MavsdkEventQueue {

  private MavsdkEventQueue() {
  }

  private static final class ExecutorHolder {
    private static final Executor EXECUTOR = Executors.newSingleThreadExecutor(runnable ->
        new Thread(runnable, "mavsdk-event-queue")
    );
  }

  /**
   * The `Executor` for running the mavsdk events in the background in a FIFO manner.
   *
   * <p>The events include:
   * <ul>
   *   <li>Execution of `MavsdkServer#run()` (for Android)</li>
   *   <li>Initialization of {@link Plugin}s</li>
   *   <li>Disposal of {@link Plugin}s</li>
   *   <li>Initialization of streams</li>
   * </ul>
   *
   * <p>The order of execution of the initialization methods of the plugins and
   * the streams is important, and library handles it for you.
   *
   * <p>For Android, this executor should be used to run the MavsdkServer
   * before any {@link Plugin} is called/initialized. Ideally, use this to run the
   * MavsdkServer before constructing the {@link System} instance. In that case,
   * this mechanism ensures that the initialization of the plugins and the streams
   * is blocked in the `mavsdk-event-queue` until the MavsdkServer is ready after
   * detecting a system to avoid socket connection crashes.
   *
   * <p>This executor is thread-safe.
   *
   * @return The `mavsdk-event-queue` `Executor`
   */
  @NonNull
  public static Executor executor() {
    return ExecutorHolder.EXECUTOR;
  }
}
