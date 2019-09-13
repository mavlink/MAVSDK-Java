package io.mavsdk.example;

import io.mavsdk.System;
import java.util.concurrent.CountDownLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetRtlAltitude {
  private static final Logger logger = LoggerFactory.getLogger(SetRtlAltitude.class);

  public static void main(String[] args) {
    logger.debug("Starting example: set RTL altitude...");

    System drone = new System();
    CountDownLatch latch = new CountDownLatch(1);

    drone.getAction().setReturnToLaunchAltitude(15f)
            .andThen(drone.getAction().getReturnToLaunchAltitude()
              .doOnSuccess(altitude -> logger.debug("RTL altitude: " + altitude))
              .toCompletable())
            .andThen(drone.getAction().setReturnToLaunchAltitude(20f))
            .andThen(drone.getAction().getReturnToLaunchAltitude()
              .doOnSuccess(altitude -> logger.debug("RTL altitude: " + altitude))
              .toCompletable())
            .subscribe(latch::countDown);

    try {
      latch.await();
    } catch (InterruptedException ignored) {
      // This is expected
    }
  }
}
