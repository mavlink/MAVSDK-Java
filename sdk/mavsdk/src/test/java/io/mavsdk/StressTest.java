package io.mavsdk;

import io.mavsdk.action.Action;
import io.mavsdk.camera.Camera;
import io.reactivex.Flowable;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class StressTest {

  @Test
  public void stress() throws InterruptedException {
    System system = new System("localhost", 9000);

    java.lang.System.out.println(Thread.currentThread().getName());

    system.getTelemetry()
      .getPositionVelocityNed()
      .subscribe(c -> java.lang.System.out.println("subscribe: " + Thread.currentThread().getName()), throwable -> {});

//    system.getTelemetry()
//      .getPosition()
//      .subscribe(c -> java.lang.System.out.println("subscribe: " + Thread.currentThread().getName()), throwable -> {});

//    system.getCamera()
//      .getCaptureInfo()
//      .subscribe(c -> java.lang.System.out.println(Thread.currentThread().getName()), throwable -> {});

    system.getCamera()
      .getCaptureInfo()
      .subscribe(c -> java.lang.System.out.println(Thread.currentThread().getName()), throwable -> {});;

    system.getCamera().processInformation();

//    Action action = new Action("localhost", 9000);
//    action.initialize();

    TimeUnit.SECONDS.sleep(20);
  }
}
