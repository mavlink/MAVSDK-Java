package io.dronecore;

import io.dronecore.rpc.DroneCoreRPCGrpc;
import io.dronecore.rpc.DroneCoreRPCGrpc.DroneCoreRPCBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.okhttp.OkHttpChannelBuilder;

public class DroneCore {
    private ManagedChannel channel;
    private DroneCoreRPCBlockingStub blockingStub;

    public void connect() {
        connect("localhost", 50051);
    }

    public void connect(String host, int port) {
        channel = OkHttpChannelBuilder.forAddress(host, port)
                .usePlaintext(true)
                .build();

        blockingStub = DroneCoreRPCGrpc.newBlockingStub(channel);
   }
}
