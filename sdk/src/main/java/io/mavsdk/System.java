package io.mavsdk;

import io.mavsdk.action.Action;
import io.mavsdk.action_server.ActionServer;
import io.mavsdk.calibration.Calibration;
import io.mavsdk.camera.Camera;
import io.mavsdk.core.Core;
import io.mavsdk.failure.Failure;
import io.mavsdk.follow_me.FollowMe;
import io.mavsdk.ftp.Ftp;
import io.mavsdk.geofence.Geofence;
import io.mavsdk.gimbal.Gimbal;
import io.mavsdk.info.Info;
import io.mavsdk.internal.LazyPlugin;
import io.mavsdk.log_files.LogFiles;
import io.mavsdk.manual_control.ManualControl;
import io.mavsdk.mavlink_direct.MavlinkDirect;
import io.mavsdk.mission.Mission;
import io.mavsdk.mission_raw.MissionRaw;
import io.mavsdk.mission_raw_server.MissionRawServer;
import io.mavsdk.mocap.Mocap;
import io.mavsdk.offboard.Offboard;
import io.mavsdk.param.Param;
import io.mavsdk.param_server.ParamServer;
import io.mavsdk.server_utility.ServerUtility;
import io.mavsdk.shell.Shell;
import io.mavsdk.telemetry.Telemetry;
import io.mavsdk.telemetry_server.TelemetryServer;
import io.mavsdk.transponder.Transponder;
import io.mavsdk.tune.Tune;
import io.reactivex.annotations.NonNull;

public class System {

  private final LazyPlugin<Action> action;
  private final LazyPlugin<ActionServer> actionServer;
  private final LazyPlugin<Calibration> calibration;
  private final LazyPlugin<Camera> camera;
  private final LazyPlugin<Core> core;
  private final LazyPlugin<Failure> failure;
  private final LazyPlugin<FollowMe> followMe;
  private final LazyPlugin<Ftp> ftp;
  private final LazyPlugin<Geofence> geofence;
  private final LazyPlugin<Gimbal> gimbal;
  private final LazyPlugin<Info> info;
  private final LazyPlugin<LogFiles> logFiles;
  private final LazyPlugin<ManualControl> manualControl;
  private final LazyPlugin<Mission> mission;
  private final LazyPlugin<MissionRaw> missionRaw;
  private final LazyPlugin<MissionRawServer> missionRawServer;
  private final LazyPlugin<Mocap> mocap;
  private final LazyPlugin<Offboard> offboard;
  private final LazyPlugin<Param> param;
  private final LazyPlugin<ParamServer> paramServer;
  private final LazyPlugin<ServerUtility> serverUtility;
  private final LazyPlugin<Shell> shell;
  private final LazyPlugin<Telemetry> telemetry;
  private final LazyPlugin<TelemetryServer> telemetryServer;
  private final LazyPlugin<Transponder> transponder;
  private final LazyPlugin<Tune> tune;
  private final LazyPlugin<MavlinkDirect> mavlinkDirect;

  /**
   * Create a System object. The plugins are initialized lazily, when the corresponding
   * get method is called.
   *
   * <p>This defaults to a mavsdk_server running on localhost:50051.
   */
  public System() {
    this("localhost", 50051);
  }

  /**
   * Create a System object.The plugins are initialized lazily, when the corresponding
   * get method is called.
   *
   * @param host the address of mavsdk_server
   * @param port the port of mavsdk_server
   */
  public System(@NonNull String host, int port) {
    action = LazyPlugin.from(() -> new Action(host, port));
    actionServer = LazyPlugin.from(() -> new ActionServer(host, port));
    calibration = LazyPlugin.from(() -> new Calibration(host, port));
    camera = LazyPlugin.from(() -> new Camera(host, port));
    core = LazyPlugin.from(() -> new Core(host, port));
    failure = LazyPlugin.from(() -> new Failure(host, port));
    followMe = LazyPlugin.from(() -> new FollowMe(host, port));
    ftp = LazyPlugin.from(() -> new Ftp(host, port));
    geofence = LazyPlugin.from(() -> new Geofence(host, port));
    gimbal = LazyPlugin.from(() -> new Gimbal(host, port));
    info = LazyPlugin.from(() -> new Info(host, port));
    logFiles = LazyPlugin.from(() -> new LogFiles(host, port));
    manualControl = LazyPlugin.from(() -> new ManualControl(host, port));
    mission = LazyPlugin.from(() -> new Mission(host, port));
    missionRaw = LazyPlugin.from(() -> new MissionRaw(host, port));
    missionRawServer = LazyPlugin.from(() -> new MissionRawServer(host, port));
    mocap = LazyPlugin.from(() -> new Mocap(host, port));
    offboard = LazyPlugin.from(() -> new Offboard(host, port));
    param = LazyPlugin.from(() -> new Param(host, port));
    paramServer = LazyPlugin.from(() -> new ParamServer(host, port));
    serverUtility = LazyPlugin.from(() -> new ServerUtility(host, port));
    shell = LazyPlugin.from(() -> new Shell(host, port));
    telemetry = LazyPlugin.from(() -> new Telemetry(host, port));
    telemetryServer = LazyPlugin.from(() -> new TelemetryServer(host, port));
    transponder = LazyPlugin.from(() -> new Transponder(host, port));
    tune = LazyPlugin.from(() -> new Tune(host, port));
    mavlinkDirect = LazyPlugin.from(() -> new MavlinkDirect(host, port));
  }

  @NonNull
  public Action getAction() {
    return action.get();
  }

  @NonNull
  public ActionServer getActionServer() {
    return actionServer.get();
  }

  @NonNull
  public Calibration getCalibration() {
    return calibration.get();
  }

  @NonNull
  public Camera getCamera() {
    return camera.get();
  }

  @NonNull
  public Core getCore() {
    return core.get();
  }

  @NonNull
  public Failure getFailure() {
    return failure.get();
  }

  @NonNull
  public FollowMe getFollowMe() {
    return followMe.get();
  }

  @NonNull
  public Ftp getFtp() {
    return ftp.get();
  }

  @NonNull
  public Geofence getGeofence() {
    return geofence.get();
  }

  @NonNull
  public Gimbal getGimbal() {
    return gimbal.get();
  }

  @NonNull
  public Info getInfo() {
    return info.get();
  }

  @NonNull
  public LogFiles getLogFiles() {
    return logFiles.get();
  }

  @NonNull
  public ManualControl getManualControl() {
    return manualControl.get();
  }

  @NonNull
  public Mission getMission() {
    return mission.get();
  }

  @NonNull
  public MissionRaw getMissionRaw() {
    return missionRaw.get();
  }

  @NonNull
  public MissionRawServer getMissionRawServer() {
    return missionRawServer.get();
  }

  @NonNull
  public Mocap getMocap() {
    return mocap.get();
  }

  @NonNull
  public Offboard getOffboard() {
    return offboard.get();
  }

  @NonNull
  public Param getParam() {
    return param.get();
  }

  @NonNull
  public ParamServer getParamServer() {
    return paramServer.get();
  }

  @NonNull
  public ServerUtility getServerUtility() {
    return serverUtility.get();
  }

  @NonNull
  public Shell getShell() {
    return shell.get();
  }

  @NonNull
  public Telemetry getTelemetry() {
    return telemetry.get();
  }

  @NonNull
  public TelemetryServer getTelemetryServer() {
    return telemetryServer.get();
  }


  @NonNull
  public Transponder getTransponder() {
    return transponder.get();
  }

  @NonNull
  public Tune getTune() {
    return tune.get();
  }

  @NonNull
  public MavlinkDirect getMavlinkDirect() { return mavlinkDirect.get(); }

  /**
   * Dispose of all the plugins.
   */
  public void dispose() {
    action.dispose();
    actionServer.dispose();
    calibration.dispose();
    camera.dispose();
    core.dispose();
    failure.dispose();
    followMe.dispose();
    ftp.dispose();
    geofence.dispose();
    gimbal.dispose();
    info.dispose();
    logFiles.dispose();
    manualControl.dispose();
    mission.dispose();
    missionRaw.dispose();
    missionRawServer.dispose();
    mocap.dispose();
    offboard.dispose();
    param.dispose();
    paramServer.dispose();
    serverUtility.dispose();
    shell.dispose();
    telemetry.dispose();
    telemetryServer.dispose();
    transponder.dispose();
    tune.dispose();
    mavlinkDirect.dispose();
  }
}
