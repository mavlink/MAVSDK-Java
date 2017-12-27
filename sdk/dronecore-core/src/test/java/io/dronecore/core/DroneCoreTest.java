package io.dronecore.core;

import io.dronecore.core.CoreProto.SubscribeDevicesRequest;
import io.dronecore.core.CoreServiceGrpc.CoreServiceImplBase;
import io.grpc.Server;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.util.MutableHandlerRegistry;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DroneCoreTest {
  private static final String FAKE_SERVER_ID = "FakeServer" + DroneCoreTest.class;

  private final MutableHandlerRegistry serviceRegistry = new MutableHandlerRegistry();

  private Server fakeServer;
  private DroneCore dc;

  @Before
  public void setUp() throws Exception {
    fakeServer = InProcessServerBuilder
        .forName(FAKE_SERVER_ID)
        .fallbackHandlerRegistry(serviceRegistry)
        .directExecutor()
        .build()
        .start();

    dc = new DroneCore(InProcessChannelBuilder.forName(FAKE_SERVER_ID).directExecutor().build());
  }

  @After
  public void tearDown() throws Exception {
    fakeServer.shutdownNow();
  }

  @Test
  public void testDevicesFlowable_doesNotEmitWhenNoDevice() {
    dc.getDevicesFlowable()
        .test()
        .assertNoValues();
  }

  @Test
  public void testDevicesFlowable_emitsOneDevice() {
    final FakeDeviceEmittingServiceBuilder fakeServiceBuild
        = new FakeDeviceEmittingServiceBuilder(42);
    serviceRegistry.addService(fakeServiceBuild.getFakeServer());

    dc.getDevicesFlowable()
        .test()
        .assertValue(fakeServiceBuild.getDevices().get(0));
  }

  @Test
  public void testDevicesFlowable_emitsMultipleDevices() {
    final FakeDeviceEmittingServiceBuilder fakeServiceBuild
        = new FakeDeviceEmittingServiceBuilder(42, 33, 21, 15432523);
    serviceRegistry.addService(fakeServiceBuild.getFakeServer());

    dc.getDevicesFlowable()
        .test()
        .assertValueSequence(fakeServiceBuild.getDevices());
  }

  class FakeDeviceEmittingServiceBuilder {
    private List<Device> devices = new ArrayList<>();

    private FakeDeviceEmittingServiceBuilder(Integer... uuids) {
      for (Integer uuid : uuids) {
        devices.add(new Device(uuid));
      }
    }

    private List<Device> getDevices() {
      return devices;
    }

    private CoreServiceImplBase getFakeServer() {
      return new CoreServiceImplBase() {
        @Override
        public void subscribeDevices(SubscribeDevicesRequest request,
                                     StreamObserver<CoreProto.Device> responseObserver) {
          for (Device device : devices) {
            CoreProto.UUID uuid = CoreProto.UUID.newBuilder().setValue(device.getUuid()).build();
            responseObserver.onNext(CoreProto.Device.newBuilder().setUuid(uuid).build());
          }

          responseObserver.onCompleted();
        }
      };
    }
  }
}