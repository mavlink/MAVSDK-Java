package org.dronecode.sdk;

import io.grpc.ManagedChannel;
import io.grpc.okhttp.OkHttpChannelBuilder;
import io.reactivex.Flowable;
import java.util.Iterator;
import org.dronecode.sdk.CoreServiceGrpc.CoreServiceBlockingStub;
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
   * Subscribe to drone discovery. When an event is emitted in this stream, it means
   * that the drone is connected. Disconnection is provided by "timeoutFlowable()".
   */
  public Flowable<Long> discoverFlowable() {
    final Iterator<CoreProto.DiscoverResponse> responses
        = blockingStub.subscribeDiscover(CoreProto.SubscribeDiscoverRequest.newBuilder().build());

    return Flowable.fromIterable(() -> responses)
        .map(response -> response.getUuid());
  }
}
