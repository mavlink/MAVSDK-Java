package io.mavsdk;

/**
 * Plugin interface to be implemented by all MAVSDK-Java plugins.
 */
public interface Plugin {

  /**
   * Initializes the `gRPC` channel, stub and other resources for managing the connection
   * with the `gRPC` server. This also initializes the data flow of the streams.
   *
   * Implementations need to make sure that the initialization of the {@link Plugin}
   * is thread-safe.
   *
   * The method throws `RuntimeException` if it is called more than once.
   */
  void initialize();

  /**
   * Closes the `gRPC` channel.
   *
   * Implementations need to make sure that the {@link Plugin} is initialized before
   * closing the channel.
   */
  void dispose();
}
