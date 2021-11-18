package io.mavsdk.internal;

import io.reactivex.annotations.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MavsdkExecutors {

  private MavsdkExecutors() {
  }

  private static final class Holder {
    private static final Executor BIND_EXECUTOR = Executors.newSingleThreadExecutor();
  }

  @NonNull
  public static Executor bindExecutor() {
    return Holder.BIND_EXECUTOR;
  }
}
