# MAVSDK-Java

This is the Java frontend implementation to [MAVSDK](https://github.com/mavlink/MAVSDK).

It is organized as follows:

* The [examples](./examples) directory contains Java and Android examples using the sdk.
* The [sdk](./sdk) directory contains the actual SDK.
* The [mavsdk_server](./mavsdk_server) directory contains the Android library exposing `mavsdk_server`.

## QuickStart

The fastest way to start is to follow the instructions in the README of the [java-client](./examples/java-client) example. For Android, the [android-client](./examples/android-client) is the next step.

Please note `MavsdkServer` currently is not compatible for running on x86 and x86_64 Android images. If you would like to develop using `MavsdkServer`, you will need to deploy and develop using an ARM-based product or emulator.

MAVSDK-Java is distributed through MavenCentral, meaning that it can be imported using gradle with:

```
dependencies {
    ...
    implementation 'io.mavsdk:mavsdk:3.0.0'
    ...
}
```

For Android, `mavsdk_server` is distributed as an Android library (`aar`):

```
dependencies {
    ...
    implementation 'io.mavsdk:mavsdk:3.0.0'
    implementation 'io.mavsdk:mavsdk-server:3.1.0'
    ...
}
```

### ProGuard

ProGuard users may need to add the following rule:

```
-keep class io.mavsdk.** { *; }
```

### Notes

1. `MAVSDK-Java`'s plugins initialize on a background thread (`mavsdk-event-queue`).  The initializations happen in a thread-safe manner and the library handles the correct order itself. This is done to provide a simple API to the users.

2. For Android, run the `mavsdk_server` as follows:

```java
MavsdkServer server = new MavsdkServer();
MavsdkEventQueue.executor().execute(() -> server.run(SYSTEM_ADDRESS, MAVSDK_SERVER_PORT));
```

This makes sure that the calling thread (which may be the UI thread) is not blocked as the `mavsdk_server` discovers a system. This should ideally be done before the user creates the `io.mavsdk.System` so that `MavsdkServer.run()` is the first command to run in the `mavsdk-event-queue`.


To stop the server:

```java
MavsdkEventQueue.executor().execute(() -> server.stop());
```

3. Users should avoid using the plugins directly by accessing them only through `io.mavsdk.System` objects.

The plugins are constructed and initialized lazily upon their first call through `System`, therefore the users do not bear any runtime overhead for the plugins that they won't be using.

4. Data streams start flowing in the background once the system is discovered by the `mavsdk_server`, so they are safe to subscribe to immediately after the creation of a `System` object. Streams that are not accessed won't start flowing.

5. One-shot calls like `takeoff` and `land` are not added to the `mavsdk-event-queue` when the user subscribes to them. This is done to avoid their piling up while the `mavsdk_server` discovers a system. Instead, the `onError` callback will be triggered after a 100ms delay indicating that no system was available for the command.

## Contributing

### Coding style

Java/Android coding style is ensured using CheckStyle with the Google style.

#### Command line

A `checkstyle` task is defined in the root `build.gradle` of each project and can be run as follows:

    $ ./gradlew checkstyle

The `build` task depends on `checkstyle`, meaning that `$ ./gradlew build` runs the checks as well.

#### IntelliJ / Android-Studio

There exist a plugin for CheckStyle in JetBrains' IDEs.

##### Setup

1. Install the plugin called "CheckStyle-IDEA" in IntelliJ / Android-Studio.
2. Import the checkstyle configuration as a code style scheme in _Settings > Editor > Code Style > Java > Manage... >
   Import..._ by selecting "CheckStyle configuration" and then browsing to `config/checkstyle/checkstyle.xml`.
3. In _Settings > Other Settings > Checkstyle_, change the "Scan Scope" to "Only Java sources (including tests)".
4. Still in _Settings > Other Settings > Checkstyle_, add a new configuration file and browse to
   `config/checkstyle/checkstyle.xml`.

##### Usage

In IntelliJ / Android-Studio's bottom task bar, you should see a "CheckStyle" tab. It will allow you to select your configuration
with the "Rules" dropdown-list, and to run the analysis on your code.

Note that by default, the IDE will not run checkstyle when building the project (whereas `$ ./gradlew build` always does it).

#### Troubleshooting

In IntelliJ / Android-Studio, the IDE might force the order of the imports in a way that is not following the checkstyle rules. For some reason, this is not set when importing `checkstyle.xml` as a code style scheme. However, it can be manually updated in _Settings > Code Style > Java > Import Layout_.

### Releasing

Both [sdk](./sdk) and [mavsdk_server](./mavsdk_server) are released with Maven. Publishing can be done through a gradle task:

```gradle
./gradlew uploadArchives
```

This task requires a few secrets in `gradle.properties`:

```
signing.keyId=<keyId>
signing.password=<password>
signing.secretKeyRingFile=<ring_file>

ossrhUsername=<username>
ossrhPassword=<password>
```

### Debugging without pushing to maven

Sometimes you just need to rebuild `mavsdk_server` or even `libmavsdk_server.so` a couple of times to directly debug a problem.

This can be achieved with the Gradle [composite builds](https://docs.gradle.org/current/userguide/composite_builds.html). This is already setup in the Android example project [here](https://github.com/mavlink/MAVSDK-Java/blob/main/examples/android-client/settings.gradle#L3-L11) (just uncomment the lines to build `sdk` and/or `mavsdk_server` from sources).


Then you can just build the example and it will in turn build `mavsdk_server`.

To replace the `libmavsdk_server.so`, you have to build it using dockcross and replace the file for the architecture that you're testing on. This is assuming you have MAVSDK-Java and MAVSDK side to side in the same directory:

```
cd ../MAVSDK
docker run --rm dockcross/android-arm64 > ./dockcross-android-arm64 && chmod +x ./dockcross-android-arm64
tools/generate_mavlink_headers.sh
./dockcross-android-arm64 cmake -DCMAKE_BUILD_TYPE=Debug -DBUILD_MAVSDK_SERVER=ON -DBUILD_SHARED_LIBS=OFF -Bbuild/android-arm64 -DMAVLINK_HEADERS="mavlink-headers/include" -H. -DCMAKE_INSTALL_PREFIX=build/android-arm64/install
./dockcross-android-arm64 cmake --build build/android-arm64 --target install && cp build/android-arm64/install/lib/libmavsdk_server.so ../mavsdk-android-test/mavsdk_server/src/main/prebuiltLibs/arm64-v8a/libmavsdk_server.so
```

To avoid keep getting the file overwritten, comment out the [function downloading and extracting the .so library artifacts](https://github.com/mavlink/MAVSDK-Java/blob/85a2f3d5f96d67c1919f52c67f5f6bdbc7607486/mavsdk_server/build.gradle#L24-L53).

Now build, install, run the Android app again.
