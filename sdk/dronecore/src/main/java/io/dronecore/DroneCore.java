package io.dronecore;

import io.dronecore.rpc.DroneCoreRPCGrpc;
import io.dronecore.rpc.DroneCoreRPCGrpc.DroneCoreRPCBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.okhttp.OkHttpChannelBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DroneCore {
  private static final Logger logger = LoggerFactory.getLogger(DroneCore.class);

  private ManagedChannel channel;
  private DroneCoreRPCBlockingStub blockingStub;

  public void connect() {
    connect("localhost", 50051);
  }

  /**
   * Connect to the DroneCore backend.
   *
   * @param host The address on which the backend is listening.
   * @param port The port on which the backend is listening.
   */
  public void connect(String host, int port) {
    logger.debug("Connecting to " + host + ":" + port);

    channel = OkHttpChannelBuilder.forAddress(host, port)
            .usePlaintext(true)
            .build();

    blockingStub = DroneCoreRPCGrpc.newBlockingStub(channel);
  }
}
