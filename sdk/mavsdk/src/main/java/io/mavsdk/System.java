package io.mavsdk;

import io.mavsdk.action.Action;
import io.mavsdk.calibration.Calibration;
import io.mavsdk.camera.Camera;
import io.mavsdk.core.Core;
import io.mavsdk.gimbal.Gimbal;
import io.mavsdk.info.Info;
import io.mavsdk.mission.Mission;
import io.mavsdk.offboard.Offboard;
import io.mavsdk.param.Param;
import io.mavsdk.telemetry.Telemetry;

public class System {
    private Action action;
    private Calibration calibration;
    private Camera camera;
    private Core core;
    private Gimbal gimbal;
    private Info info;
    private Mission mission;
    private Offboard offboard;
    private Param param;
    private Telemetry telemetry;

    public System() {
        this("localhost", 50051);
    }

    public System(String host, int port) {
        this.action = new Action(host, port);
        this.calibration = new Calibration(host, port);
        this.camera = new Camera(host, port);
        this.core = new Core(host, port);
        this.gimbal = new Gimbal(host, port);
        this.info = new Info(host, port);
        this.mission = new Mission(host, port);
        this.offboard = new Offboard(host, port);
        this.param = new Param(host, port);
        this.telemetry = new Telemetry(host, port);
    }

    public Action getAction() {
        return action;
    }

    public Calibration getCalibration() {
        return calibration;
    }

    public Camera getCamera() {
        return camera;
    }

    public Core getCore() {
        return core;
    }

    public Gimbal getGimbal() {
        return gimbal;
    }

    public Info getInfo() {
        return info;
    }

    public Mission getMission() {
        return mission;
    }

    public Offboard getOffboard() {
        return offboard;
    }

    public Param getParam() {
        return param;
    }

    public Telemetry getTelemetry() {
        return telemetry;
    }
}