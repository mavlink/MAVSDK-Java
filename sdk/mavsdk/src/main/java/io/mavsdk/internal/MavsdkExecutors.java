package io.mavsdk.internal;

import io.reactivex.annotations.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MavsdkExecutors {

  private MavsdkExecutors() {
  }

  private static final class Holder {
    private static final Executor CONNECTION_EXECUTOR = Executors.newSingleThreadExecutor();
  }

  @NonNull
  public static Executor connectionExecutor() {
    return Holder.CONNECTION_EXECUTOR;
  }
}
