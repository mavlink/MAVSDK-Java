package io.dronecore.example;

import io.dronecode_sdk.action.Action;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TakeoffAndLand {
  private static final Logger logger = LoggerFactory.getLogger(TakeoffAndLand.class);

  public static void main(String[] args) {
    logger.debug("Starting example: takeoff and land...");

    Action action = new Action();
    CountDownLatch latch = new CountDownLatch(1);

    action.arm()
          .andThen(action.takeoff())
          .delay(5, TimeUnit.SECONDS)
          .andThen(action.land())
          .doOnComplete(() -> latch.countDown())
          .subscribe();

    try {
      latch.await();
    } catch (InterruptedException ignored) {
        // This is expected
    }
  }
}
