package io.mavsdk.mavsdkserver;

public class MavsdkServer {

    static {
        java.lang.System.loadLibrary("native_lib");
    }

    public native void run(String connectionUrl);
}
