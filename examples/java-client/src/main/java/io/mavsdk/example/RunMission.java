package io.mavsdk.example;

import io.mavsdk.System;
import io.mavsdk.mission.Mission;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunMission {
  private static final Logger logger = LoggerFactory.getLogger(RunMission.class);

  public static void main(String[] args) {
    logger.debug("Starting example: mission...");

    List<Mission.MissionItem> missionItems = new ArrayList<>();
    missionItems.add(generateMissionItem(47.398039859999997, 8.5455725400000002));
    missionItems.add(generateMissionItem(47.398036222362471, 8.5450146439425509));
    missionItems.add(generateMissionItem(47.397825620791885, 8.5450092830163271));
    missionItems.add(generateMissionItem(47.397832880000003, 8.5455939999999995));

    Mission.MissionPlan missionPlan = new Mission.MissionPlan(missionItems);

    System drone = new System();

    drone.getMission()
        .setReturnToLaunchAfterMission(true)
        .andThen(drone.getMission().uploadMission(missionPlan)
            .doOnComplete(() -> logger.debug("Upload succeeded")))
        .andThen(drone.getAction().arm())
        .andThen(drone.getMission().startMission()
            .doOnComplete(() -> logger.debug("Mission started")))
        .subscribe();

    CountDownLatch latch = new CountDownLatch(1);
    drone.getMission()
        .getMissionProgress()
        .filter(progress -> progress.getCurrent() == progress.getTotal())
        .take(1)
        .subscribe(ignored -> latch.countDown());

    try {
      latch.await();
    } catch (InterruptedException ignored) {
      // This is expected
    }
  }

  public static Mission.MissionItem generateMissionItem(double latitudeDeg, double longitudeDeg) {
    return new Mission.MissionItem(
        latitudeDeg,
        longitudeDeg,
        10f,
        10f,
        true,
        Float.NaN,
        Float.NaN,
        Mission.MissionItem.CameraAction.NONE,
        Float.NaN,
        1.0);
  }
}

