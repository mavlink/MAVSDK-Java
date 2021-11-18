package io.mavsdk;

/**
 * The superclass of all exceptions thrown by the MAVSDK-Java.
 *
 * <p>It is needed to differentiate between exceptions thrown by the MAVSDK-Java
 * and exceptions that could have been occurred by other components, e.g.
 * `SocketException` thrown by `gRPC` when trying to connect to the server.
 */
public class MavsdkException extends Exception {

  public MavsdkException(String message) {
    super(message);
  }

  public MavsdkException(String message, Throwable cause) {
    super(message, cause);
  }

  public MavsdkException(Throwable cause) {
    super(cause);
  }
}
