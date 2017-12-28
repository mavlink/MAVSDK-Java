## Contributing

The SDK implements the DroneCore protobuf interfaces by using RxJava and TDD-style. This means that every single public method of a component must be unit-tested.

## Project organization

This is the root of the main "sdk" project, composed of multiple modules. Each module corresponds to a component defined by a protobuf interface (see [DroneCore-Proto](https://github.com/dronecore/DroneCore-Proto)).

Each module is only building a specific part of the overall protobuf interface by using a symbolic link to the corresponding folder in DroneCore-Proto (see for instance [the core module](./dronecore-core/src/main/proto)).

Most of the gradle setup is shared between the components and can be found in [build.gradle](./build.gradle).
