package io.dronecore.example;

import io.dronecode_sdk.action.Action;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DroneCoreExample {
  public static final Logger logger = LoggerFactory.getLogger(DroneCoreExample.class);

  public static void main(String[] args) {
    logger.debug("Starting example...");

    CountDownLatch latch = new CountDownLatch(1);

    Action action = new Action();

    action.setReturnToLaunchAltitude(15f).blockingAwait();
    logger.debug("RTL altitude: " + action.getReturnToLaunchAltitude().blockingGet());
    action.setReturnToLaunchAltitude(20f).blockingAwait();
    logger.debug("RTL altitude: " + action.getReturnToLaunchAltitude().blockingGet());

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
