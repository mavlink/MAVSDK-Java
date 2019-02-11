package org.dronecode.sdk;

import io.dronecode_sdk.action.Action;
import io.dronecode_sdk.telemetry.Telemetry;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

public class DroneCoreTest {

  @Test
  public void testStream() throws InterruptedException {
    Telemetry telemetry = new Telemetry();
    telemetry.position()
             .doOnNext(next -> System.out.println(next))
             .test()
             .await(5, TimeUnit.SECONDS);
  }

  @Test
  public void testCall() throws InterruptedException {
    Action action = new Action();
    action.arm()
          .andThen(action.takeoff())
          .delay(5, TimeUnit.SECONDS)
          .andThen(action.land())
          .test()
          .await();
  }

  @Test
  public void testRequest() throws InterruptedException {
    Action action = new Action();
    action.getTakeoffAltitude().doOnSuccess(result -> System.out.println(result)).test().await();
  }
}
