package org.dronecode.sdk;

import io.grpc.Server;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.util.MutableHandlerRegistry;
import java.util.ArrayList;
import java.util.List;
import org.dronecode.sdk.CoreServiceGrpc.CoreServiceImplBase;
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
  public void tearDown() {
    fakeServer.shutdownNow();
  }

  @Test
  public void testDiscoverFlowable_doesNotEmitWhenNoDevice() {
    dc.discoverFlowable()
        .test()
        .assertNoValues();
  }

  @Test
  public void testDiscoverFlowable_emitsOneDevice() {
    final FakeUuidEmittingServiceBuilder fakeServiceBuild
        = new FakeUuidEmittingServiceBuilder(42L);
    serviceRegistry.addService(fakeServiceBuild.getFakeServer());

    dc.discoverFlowable()
        .test()
        .assertValue(fakeServiceBuild.getUuids().get(0));
  }

  @Test
  public void testDiscoverFlowable_emitsMultipleDevices() {
    final FakeUuidEmittingServiceBuilder fakeServiceBuild
        = new FakeUuidEmittingServiceBuilder(42L, 33L, 21L, 15432523L);
    serviceRegistry.addService(fakeServiceBuild.getFakeServer());

    dc.discoverFlowable()
        .test()
        .assertValueSequence(fakeServiceBuild.getUuids());
  }

  class FakeUuidEmittingServiceBuilder {
    private List<Long> uuids = new ArrayList<>();

    private FakeUuidEmittingServiceBuilder(Long... uuids) {
      for (Long uuid : uuids) {
        this.uuids.add(uuid);
      }
    }

    private List<Long> getUuids() {
      return uuids;
    }

    private CoreServiceImplBase getFakeServer() {
      return new CoreServiceImplBase() {
        @Override
        public void subscribeDiscover(CoreProto.SubscribeDiscoverRequest request,
                                   StreamObserver<CoreProto.DiscoverResponse> responseObserver) {
          for (Long uuid : uuids) {
            responseObserver.onNext(CoreProto.DiscoverResponse.newBuilder().setUuid(uuid).build());
          }

          responseObserver.onCompleted();
        }
      };
    }
  }
}
