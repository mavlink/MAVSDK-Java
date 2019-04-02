package io.dronecore.example;

import io.dronecode_sdk.action.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetRtlAltitude {
  private static final Logger logger = LoggerFactory.getLogger(TakeoffAndLand.class);

  public static void main(String[] args) {
    logger.debug("Starting example: set RTL altitude...");

    Action action = new Action();

    action.setReturnToLaunchAltitude(15f).blockingAwait();
    logger.debug("RTL altitude: " + action.getReturnToLaunchAltitude().blockingGet());
    action.setReturnToLaunchAltitude(20f).blockingAwait();
    logger.debug("RTL altitude: " + action.getReturnToLaunchAltitude().blockingGet());
  }
}
