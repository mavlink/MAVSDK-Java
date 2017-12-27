package io.dronecore.core;

import io.dronecore.core.CoreProto.SubscribeDevicesRequest;
import io.dronecore.core.CoreServiceGrpc.CoreServiceBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.okhttp.OkHttpChannelBuilder;

import io.reactivex.Flowable;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DroneCore {
  private static final Logger logger = LoggerFactory.getLogger(DroneCore.class);

  private ManagedChannel channel;
  private CoreServiceBlockingStub blockingStub;

  public DroneCore() {
    this("localhost", 50051);
  }

  public DroneCore(String host, int port) {
    this(createChannel(host, port));
  }

  private static ManagedChannel createChannel(String host, int port) {
    logger.debug("Building channel to " + host + ":" + port);

    return OkHttpChannelBuilder.forAddress(host, port)
        .usePlaintext(true)
        .build();
  }

  public DroneCore(ManagedChannel channel) {
    this.channel = channel;
    blockingStub = CoreServiceGrpc.newBlockingStub(channel);
  }

  /**
   * Get a flowable streaming the devices connected to the backend.
   */
  public Flowable<Device> getDevicesFlowable() {
    final Iterator<CoreProto.Device> devices
        = blockingStub.subscribeDevices(SubscribeDevicesRequest.newBuilder().build());

    return Flowable.fromIterable(() -> devices)
        .map(device -> new Device(device.getUuid().getValue()));
  }
}
