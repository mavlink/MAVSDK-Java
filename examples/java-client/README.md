## Prerequisites

The examples include MAVSDK-Java using gradle, but `protoc-gen-dcsdk` must be in your PATH. It requires Python 3.6+ and can be built from `sdk/proto/pb_plugins` by running:

```sh
pip install -r requirements.txt
pip install -e .
```

Make sure to add `protoc-gen-dcsdk` to your PATH, and try to run the examples!

## Running the examples

The examples can be run with the following commands:

```shell
./gradlew takeoffAndLand
./gradlew setRtlAltitude
./gradlew runMission
```

The examples will connect to a running `mavsdk_server` instance. The `mavsdk_server` binary can be downloaded from the [MAVSDK release page](https://github.com/mavlink/mavsdk/releases).

Note that running `./gradlew run` will default to `takeoffAndLand`.
