package io.dronecore.dronecoreclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import io.dronecore.core.DroneCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainActivity extends AppCompatActivity {
  private static final Logger logger = LoggerFactory.getLogger(MainActivity.class);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    logger.trace("Verbose message");
    logger.debug("Debug message");
    logger.info("Info message");
    logger.warn("Warning message");
    logger.error("Error message");

    DroneCore dc = new DroneCore();
    dc.getDevicesFlowable()
        .doOnNext(device -> logger.debug("Registered device: " + device.getUuid()))
        .subscribe();
  }
}
