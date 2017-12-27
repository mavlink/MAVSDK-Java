package io.dronecore.example;

import io.dronecore.core.DroneCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DroneCoreExample {
  public static final Logger logger = LoggerFactory.getLogger(DroneCoreExample.class);

  public static void main(String[] args) {
    logger.debug("starting example...");

    DroneCore dc = new DroneCore();

    dc.getDevicesFlowable()
        .doOnNext(device -> logger.debug("Registered device: " + device.getUuid()))
        .subscribe();
  }
}
