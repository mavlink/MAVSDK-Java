package io.mavsdk.example;

import io.mavsdk.System;
import java.util.concurrent.CountDownLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DownloadLastLog {
  private static final Logger logger = LoggerFactory.getLogger(DownloadLastLog.class);

  public static void main(String[] args) {
    logger.debug("Starting example: download last log...");

    System drone = new System();
    CountDownLatch latch = new CountDownLatch(1);

    drone.getLogFiles().getEntries()
            .toFlowable()
            .map(entries -> entries.get(entries.size() - 1))
            .flatMap(lastEntry ->
                    drone.getLogFiles().getDownloadLogFile(lastEntry, "./example_log.ulg"))
            .map(progressData -> Math.round(progressData.getProgress() * 100))
            .filter(progressPercent -> progressPercent % 5 == 0)
            .distinctUntilChanged()
            .subscribe(
              progressPercent -> {
                logger.debug("Progress: " + progressPercent + "%");
              },
              throwable -> {
                logger.error("Error: " + throwable.getMessage());
                latch.countDown();
              },
              () -> {
                logger.debug("Successfully downloaded last log!");
                latch.countDown();
              });

    try {
      latch.await();
    } catch (InterruptedException ignored) {
      // This is expected
    }
  }
}
