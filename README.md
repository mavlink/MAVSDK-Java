# MAVSDK-Java

This is the Java frontend implementation to [MAVSDK](https://github.com/mavlink/MAVSDK).

It is organized as follows:

* The [examples](./examples) directory contains Java and Android examples using the sdk.
* The [sdk](./sdk) directory contains the actual SDK.
* The [mavsdk_server](./mavsdk_server) directory contains the Android library exposing `mavsdk_server`.

## QuickStart

The fastest way to start is to follow the instructions in the README of the [java-client](./examples/java-client) example. For Android, the [android-client](./example/android-client) is the next step.

Please note `MavsdkServer` currently is not compatible for running on x86 and x86_64 Android images. If you would like to develop using `MavsdkServer`, you will need to deploy and develop using an ARM-based product or emulator.

MAVSDK-Java is distributed through MavenCentral, meaning that it can be imported using gradle with:

```
dependencies {
    ...
    implementation 'io.mavsdk:mavsdk:0.4.0'
    ...
}
```

For Android, `mavsdk_server` is distributed as an Android library (`aar`):

```
dependencies {
    ...
    implementation 'io.mavsdk:mavsdk:0.4.0'
    implementation 'io.mavsdk:mavsdk-server:0.4.0'
    ...
}
```

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

Both [sdk](./sdk) and [mavsdk_server](./mavdsk_server) are released with Maven. Publishing can be done through a gradle task:

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

## Further Information

- [Getting started with MAVSDK – Java](https://auterion.com/getting-started-with-mavsdk-java/) (Jonas Vautherin, Auterion blog)
