package io.mavsdk;

import io.mavsdk.action.Action;
import io.mavsdk.calibration.Calibration;
import io.mavsdk.camera.Camera;
import io.mavsdk.core.Core;
import io.mavsdk.failure.Failure;
import io.mavsdk.follow_me.FollowMe;
import io.mavsdk.ftp.Ftp;
import io.mavsdk.geofence.Geofence;
import io.mavsdk.gimbal.Gimbal;
import io.mavsdk.info.Info;
import io.mavsdk.log_files.LogFiles;
import io.mavsdk.manual_control.ManualControl;
import io.mavsdk.mission.Mission;
import io.mavsdk.mission_raw.MissionRaw;
import io.mavsdk.mocap.Mocap;
import io.mavsdk.offboard.Offboard;
import io.mavsdk.param.Param;
import io.mavsdk.shell.Shell;
import io.mavsdk.telemetry.Telemetry;
import io.mavsdk.tune.Tune;

public class System {
  private Action action;
  private Calibration calibration;
  private Camera camera;
  private Core core;
  private Failure failure;
  private FollowMe followMe;
  private Ftp ftp;
  private Geofence geofence;
  private Gimbal gimbal;
  private Info info;
  private LogFiles logFiles;
  private ManualControl manualControl;
  private Mission mission;
  private MissionRaw missionRaw;
  private Mocap mocap;
  private Offboard offboard;
  private Param param;
  private Shell shell;
  private Telemetry telemetry;
  private Tune tune;

  /**
   * Create a System object, initializing the plugins and connecting them to mavsdk_server.
   *
   * <p>This defaults to a mavsdk_server running on localhost:50051.</p>
   */
  public System() {
    this("localhost", 50051);
  }

  /**
   * Create a System object, initializing the plugins and connecting them to mavsdk_server.
   * @param host the address of mavsdk_server
   * @param port the port of mavsdk_server
   */
  public System(String host, int port) {
    this.action = new Action(host, port);
    this.calibration = new Calibration(host, port);
    this.camera = new Camera(host, port);
    this.core = new Core(host, port);
    this.failure = new Failure(host, port);
    this.followMe = new FollowMe(host, port);
    this.ftp = new Ftp(host, port);
    this.geofence = new Geofence(host, port);
    this.gimbal = new Gimbal(host, port);
    this.info = new Info(host, port);
    this.logFiles = new LogFiles(host, port);
    this.manualControl = new ManualControl(host, port);
    this.mission = new Mission(host, port);
    this.missionRaw = new MissionRaw(host, port);
    this.mocap = new Mocap(host, port);
    this.offboard = new Offboard(host, port);
    this.param = new Param(host, port);
    this.shell = new Shell(host, port);
    this.telemetry = new Telemetry(host, port);
    this.tune = new Tune(host, port);
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

  public Failure getFailure() {
    return failure;
  }

  public FollowMe getFollowMe() {
    return followMe;
  }

  public Ftp getFtp() {
    return ftp;
  }

  public Geofence getGeofence() {
    return geofence;
  }

  public Gimbal getGimbal() {
    return gimbal;
  }

  public Info getInfo() {
    return info;
  }

  public LogFiles getLogFiles() {
    return logFiles;
  }

  public ManualControl getManualControl() {
    return manualControl;
  }

  public Mission getMission() {
    return mission;
  }

  public MissionRaw getMissionRaw() {
    return missionRaw;
  }

  public Mocap getMocap() {
    return mocap;
  }

  public Offboard getOffboard() {
    return offboard;
  }

  public Param getParam() {
    return param;
  }

  public Shell getShell() {
    return shell;
  }

  public Telemetry getTelemetry() {
    return telemetry;
  }

  public Tune getTune() {
    return tune;
  }

  /**
   * Dispose of all the plugins.
   */
  public void dispose() {
    this.action.dispose();
    this.calibration.dispose();
    this.camera.dispose();
    this.core.dispose();
    this.failure.dispose();
    this.followMe.dispose();
    this.ftp.dispose();
    this.geofence.dispose();
    this.gimbal.dispose();
    this.info.dispose();
    this.logFiles.dispose();
    this.manualControl.dispose();
    this.mission.dispose();
    this.missionRaw.dispose();
    this.mocap.dispose();
    this.offboard.dispose();
    this.param.dispose();
    this.shell.dispose();
    this.telemetry.dispose();
    this.tune.dispose();
  }
}
