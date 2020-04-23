package io.mavsdk.mavsdkserver;

public class MavsdkServer {

  static {
    java.lang.System.loadLibrary("native_lib");
  }

  private long mavsdkServerHandle;

  /**
   * Run MavsdkServer with MAVLink defaulting to udp://:14540.
   *
   * <p>MavsdkServer will start a gRPC server listening on an
   * arbitrary port, to which a `System` should connect.</p>
   *
   * @return The port on which MavsdkServer listens for a `System` to connect.
   *     A return value of 0 means that the server failed to start.
   */
  public int run() {
    return run("udp://:14540");
  }

  /**
   * Run MavsdkServer with MAVLink on `systemAddress`.
   *
   * <p>MavsdkServer will start a gRPC server listening on an
   * arbitrary port, to which a `System` should connect.</p>
   *
   * @param systemAddress The address on which the remote MAVLink system is expected.
   *     Valid formats are:
   *     For TCP : tcp://[server_host][:server_port].
   *     For UDP : udp://[bind_host][:bind_port].
   *     For Serial : serial:///path/to/serial/dev[:baudrate].
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
   *     For TCP : tcp://[server_host][:server_port]
   *     For UDP : udp://[bind_host][:bind_port]
   *     For Serial : serial:///path/to/serial/dev[:baudrate]
   * @param mavsdkServerPort The port on which the server should listen for a `System`.
   * @return A handle to the started MavsdkServer, to be used with other native functions.
   */
  public int run(String systemAddress, int mavsdkServerPort) {
    mavsdkServerHandle = runNative(systemAddress, mavsdkServerPort);
    return getPort(mavsdkServerHandle);
  }

  private native long runNative(String systemAddress, int mavsdkServerPort);

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
}
