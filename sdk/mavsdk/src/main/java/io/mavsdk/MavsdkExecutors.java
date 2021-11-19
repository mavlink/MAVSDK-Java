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
public class MavsdkExecutors {

  private MavsdkExecutors() {
  }

  private static final class EventQueueHolder {
    private static final Executor EVENT_QUEUE = Executors.newSingleThreadExecutor(runnable ->
        new Thread(runnable, "mavsdk-event-queue")
    );
  }

  /**
   * The `Executor` for running the mavsdk events in the background in a FIFO manner.
   *
   * <p>The events include:
   * - Execution of `MavsdkServer#run()` (for Android)
   * - Initialization of {@link Plugin}s
   * - Disposal of {@link Plugin}s
   * - Initialization of streams
   *
   * <p>The order of execution of the initialization methods of the plugins and
   * the streams is important, and library handles it for you.
   *
   * <p>Note: For Android, this executor should be used to run the MavsdkServer
   * before any {@link Plugin} is called/initialized. Ideally, use this to run the
   * MavsdkServer before constructing the {@link System} instance. In that case,
   * this mechanism ensures that the initialization of the plugins and the streams
   * is blocked until the MavsdkServer is ready after detecting a system.
   *
   * @return The `mavsdk-event-queue` `Executor`
   */
  @NonNull
  public static Executor eventQueue() {
    return EventQueueHolder.EVENT_QUEUE;
  }
}
