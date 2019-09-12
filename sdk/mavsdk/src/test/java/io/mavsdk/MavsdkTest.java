package io.mavsdk;

import java.util.concurrent.TimeUnit;
import org.junit.Test;

public class MavsdkTest {

  @Test
  public void testStream() throws InterruptedException {
    System system = new System();
    system.getTelemetry().getPosition()
             .doOnNext(next -> java.lang.System.out.println(next))
             .test()
             .await(5, TimeUnit.SECONDS);
  }

  @Test
  public void testCall() throws InterruptedException {
    System system = new System();
    system.getAction().arm()
          .andThen(system.getAction().takeoff())
          .delay(5, TimeUnit.SECONDS)
          .andThen(system.getAction().land())
          .test()
          .await();
  }

  @Test
  public void testRequest() throws InterruptedException {
    System system = new System();
    system.getAction().getTakeoffAltitude().doOnSuccess(result -> java.lang.System.out.println(result)).test().await();
  }
}
