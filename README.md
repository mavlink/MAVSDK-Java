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
    implementation 'io.mavsdk:mavsdk:3.6.0'
    ...
}
```

For Android, `mavsdk_server` is distributed as an Android library (`aar`):

```
dependencies {
    ...
    implementation 'io.mavsdk:mavsdk:3.6.0'
    implementation 'io.mavsdk:mavsdk-server:3.6.0'
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

### Releasing

1. Update the proto submodule (./sdk/proto) to match the version of
   `mavsdk_server` you want to use. The proto submodule should point to the same
   commit hash as the proto submodule in
   [MAVSDK-C++](https://github.com/mavlink/mavsdk) (make sure you check the hash
   associated with the right release tag!).
1. Update the version in both modules:
   [./sdk/build.gradle.kts](./sdk/build.gradle.kts) and
   [./mavsdk_server/build.gradle.kts](./mavsdk_server/build.gradle.kts). The new version
   should be the version of `mavsdk_server` followed by an optional build
   number, e.g. `3.6.0` or `3.6.0-2`. This means that the proto definitions
   should match with the `v3.6.0` tag of
   [MAVSDK-C++](https://github.com/mavlink/mavsdk). We support snapshots, e.g.
   `3.6.0-SNAPSHOT` will publish a temporary snapshot to Maven Central (this is
   convenient for testing before the final release).
1. In [./mavsdk_server/build.gradle.kts](./mavsdk_server/build.gradle.kts),
   change `mavsdk_server_release` to point to the version of `mavsdk_server` you
   want to use. This should correspond to the proto submodule and to the version
   numbers set in the previous step (e.g. if you set 3.6.0-2, the proto
   submodule should point to the same hash as MAVSDK-C++ v3.6.0, and
   `mavsdk_server_release` should be set to "v3.6.0").
1. Update the README to point to the new version.
1. Tag a release. The CI will publish the artifacts to Maven Central
   automatically.

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
