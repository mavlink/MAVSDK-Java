# Java client

This project is meant to show examples using MAVSDK-Java, and is a good place to get started.

## Prerequisites

MAVSDK-Java will connect to a running instance of `mavsdk_server`, that can be downloaded from the [MAVSDK release page](https://github.com/mavlink/MAVSDK/releases).

Running `mavsdk_server` from the command line will show output similar to the following:

```
$ ./mavsdk_server
[02:58:29|Info ] MAVSDK version: 0.18.3-51-g0c2c48f1 (mavsdk_impl.cpp:25)
[02:58:29|Debug] New: System ID: 0 Comp ID: 0 (mavsdk_impl.cpp:361)
[02:58:29|Info ] Server set to listen on 0.0.0.0:50051 (grpc_server.cpp:44)
[02:58:29|Info ] Server started (grpc_server.cpp:28)
[02:58:29|Info ] Waiting to discover system... (connection_initiator.h:58)
```

The other requirement is to have a MAVLink drone (or simulator) running. You can get started with the simulation environment [here](https://dev.px4.io/master/en/simulation/). If using docker, an alternative is to run a headless gazebo instance, as described [here](https://github.com/JonasVautherin/px4-gazebo-headless):

```sh
$ docker run --rm -it jonasvautherin/px4-gazebo-headless:v1.9.2
```

## Running the examples

The examples can be run with the following commands:

```shell
./gradlew takeoffAndLand
./gradlew setRtlAltitude
./gradlew runMission
```

Note that running `./gradlew run` will default to `takeoffAndLand`.

## Playing with jshell, the Java REPL

It can be useful to start with the REPL to get a feeling of how MAVSDK-Java works. Try to start jshell from gradle (note: that may not work on all platforms):

```sh
./gradlew jshell
```

If everything goes well, you should now be in an interactive jshell, showing the prompt:

```
jshell>
```

Let's start by importing the `Action` plugin:

```
jshell> import io.mavsdk.action.Action
```

We can now instantiate an `action` object:

```
jshell> Action action = new Action()
```

And, given that `mavsdk_server` is running and connected to a (possibly simulated) drone, we can directly takeoff:

```
jshell> action.arm().andThen(action.takeoff()).subscribe()
```
