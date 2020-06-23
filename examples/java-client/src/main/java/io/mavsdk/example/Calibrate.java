package io.mavsdk.example;

import io.mavsdk.System;
import io.mavsdk.calibration.Calibration;
import io.reactivex.Flowable;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Calibrate {
  private static final Logger logger = LoggerFactory.getLogger(Calibrate.class);

  public static void main(String[] args) {
    logger.debug("Starting example: calibration...");

    System drone = new System();
    CountDownLatch latch = new CountDownLatch(1);

    Flowable<Calibration.ProgressData> gyroFlowable
            = drone.getCalibration().getCalibrateGyro()
                .delay(2, TimeUnit.SECONDS)
                .doOnSubscribe(subscription -> logger.debug("Starting Gyro calibration"));

    Flowable<Calibration.ProgressData> accelFlowable
            = drone.getCalibration().getCalibrateAccelerometer()
                .delay(2, TimeUnit.SECONDS)
                .doOnSubscribe(subscription -> logger.debug("Starting Accelerometer calibration"));

    Flowable.concat(gyroFlowable, accelFlowable)
            .subscribe(
              progressData -> {
                if (progressData.getHasProgress()) {
                  logger.debug("Progress: " + progressData.getProgress());
                }

                if (progressData.getHasStatusText()) {
                  logger.debug("Status text: " + progressData.getStatusText());
                }
              },
              throwable -> {
                logger.error("error: ", throwable);
                latch.countDown();
              },
              latch::countDown);

    try {
      latch.await();
    } catch (InterruptedException ignored) {
      // This is expected
    }
  }
}
