package io.mavsdk.example;

import io.mavsdk.System;
import io.mavsdk.action.Action;
import io.mavsdk.mission.Mission;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TakeoffAndLand {
  private static final Logger logger = LoggerFactory.getLogger(TakeoffAndLand.class);

  public static void main(String[] args) {
    logger.debug("Starting example: takeoff and land...");

    System drone = new System();
    CountDownLatch latch = new CountDownLatch(1);

    drone.getAction().arm()
          .doOnComplete(() -> logger.debug("Arming..."))
          .doOnError(throwable -> logger.error("Failed to arm: "
                  + ((Action.ActionException) throwable).getCode()))
          .andThen(drone.getAction().takeoff()
            .doOnComplete(() -> logger.debug("Taking off..."))
            .doOnError(throwable -> logger.error("Failed to take off: "
                    + ((Mission.MissionException) throwable).getCode())))
          .delay(5, TimeUnit.SECONDS)
          .andThen(drone.getAction().land()
            .doOnComplete(() -> logger.debug("Landing..."))
            .doOnError(throwable -> logger.error("Failed to land: "
                    + ((Mission.MissionException) throwable).getCode())))
          .subscribe(latch::countDown, throwable -> latch.countDown());

    try {
      latch.await();
    } catch (InterruptedException ignored) {
        // This is expected
    }
  }
}
