package io.mavsdk.mavsdkserver;

public class MavsdkServer {

  static {
    java.lang.System.loadLibrary("native_lib");
  }

  private long mavsdkServerHandle;

  /**
   * Run MavsdkServer with MAVLink defaulting to udpin://:14540.
   *
   * <p>MavsdkServer will start a gRPC server listening on an
   * arbitrary port, to which a `System` should connect.</p>
   *
   * @return The port on which MavsdkServer listens for a `System` to connect.
   *     A return value of 0 means that the server failed to start.
   */
  public int run() {
    return run("udpin://:14540");
  }

  /**
   * Run MavsdkServer with MAVLink on `systemAddress`.
   *
   * <p>MavsdkServer will start a gRPC server listening on an
   * arbitrary port, to which a `System` should connect.</p>
   *
   * @param systemAddress The address on which the remote MAVLink system is expected.
   *     Valid formats are:
   *     UDP in (server): udpin://our_ip:port
   *     UDP out (client): udpout://remote_ip:port
   *     TCP in (server): tcpin://our_ip:port
   *     TCP out (client): tcpout://remote_ip:port
   *     Serial: serial://dev_node:baudrate
   *     Serial with flow control: serial_flowcontrol://dev_node:baudrate
   * @return The port on which MavsdkServer listens for a `System` to connect.
   *     A return value of 0 means that the server failed to start.
   */
  public int run(String systemAddress) {
    return run(systemAddress, 0);
  }

  /**
   * Run MavsdkServer with MAVLink on `systemAddress`.
   *
   * <p>MavsdkServer will listen for a `System` to connect on `mavsdkServerPort`.</p>
   *
   * @param systemAddress The address on which the remote MAVLink system is expected.
   *     Valid formats are:
   *     UDP in (server): udpin://our_ip:port
   *     UDP out (client): udpout://remote_ip:port
   *     TCP in (server): tcpin://our_ip:port
   *     TCP out (client): tcpout://remote_ip:port
   *     Serial: serial://dev_node:baudrate
   *     Serial with flow control: serial_flowcontrol://dev_node:baudrate
   * @param mavsdkServerPort The port on which the server should listen for a `System`.
   * @return The port on which MavsdkServer listens for a `System` to connect.
   *     A return value of 0 means that the server failed to start.
   */
  public int run(String systemAddress, int mavsdkServerPort) {
    mavsdkServerHandle = initNative();

    if (!runNative(mavsdkServerHandle, systemAddress, mavsdkServerPort)) {
      return 0;
    }

    return getPort(mavsdkServerHandle);
  }

  /**
   * Run MavsdkServer with MAVLink on `systemAddress` with custom MAVLink IDs.
   *
   * <p>MavsdkServer will listen for a `System` to connect on `mavsdkServerPort`.</p>
   *
   * @param systemAddress The address on which the remote MAVLink system is expected.
   *     UDP in (server): udpin://our_ip:port
   *     UDP out (client): udpout://remote_ip:port
   *     TCP in (server): tcpin://our_ip:port
   *     TCP out (client): tcpout://remote_ip:port
   *     Serial: serial://dev_node:baudrate
   *     Serial with flow control: serial_flowcontrol://dev_node:baudrate
   * @param mavsdkServerPort The port on which the server should listen for a `System`.
   * @param systemId The MAVLink sysid that MAVSDK should use.
   * @param componentId The MAVLink compid that MAVSDK should use.
   * @return The port on which MavsdkServer listens for a `System` to connect.
   *     A return value of 0 means that the server failed to start.
   */
  public int runWithMavIds(String systemAddress, int mavsdkServerPort, int systemId, int componentId) {
    mavsdkServerHandle = initNative();

    if (!runNativeWithMavIds(mavsdkServerHandle, systemAddress, mavsdkServerPort, systemId, componentId)) {
      return 0;
    }

    return getPort(mavsdkServerHandle);
  }

  private native long initNative();

  private native boolean runNative(long mavsdkServerHandle, String systemAddress, int mavsdkServerPort);

  private native boolean runNativeWithMavIds(long mavsdkServerHandle, String systemAddress, int mavsdkServerPort, int systemId, int componentId);

  private native int getPort(long mavsdkServerHandle);

  /**
   * Attach to the running MavsdkServer, effectively blocking until it stops.
   */
  public void attach() {
    attach(mavsdkServerHandle);
  }

  private native void attach(long mavsdkServerHandle);

  public void stop() {
    stop(mavsdkServerHandle);
  }

  private native void stop(long mavsdkServerHandle);

  public void destroy() {
    destroy(mavsdkServerHandle);
  }

  private native void destroy(long mavsdkServerHandle);
}
