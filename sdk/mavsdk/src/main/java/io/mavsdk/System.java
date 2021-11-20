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
import io.mavsdk.internal.PluginWrapper;
import io.mavsdk.log_files.LogFiles;
import io.mavsdk.manual_control.ManualControl;
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
import io.mavsdk.tracking_server.TrackingServer;
import io.mavsdk.transponder.Transponder;
import io.mavsdk.tune.Tune;
import io.reactivex.annotations.NonNull;

public class System {

  private final PluginWrapper<Action> action;
  private final PluginWrapper<ActionServer> actionServer;
  private final PluginWrapper<Calibration> calibration;
  private final PluginWrapper<Camera> camera;
  private final PluginWrapper<Core> core;
  private final PluginWrapper<Failure> failure;
  private final PluginWrapper<FollowMe> followMe;
  private final PluginWrapper<Ftp> ftp;
  private final PluginWrapper<Geofence> geofence;
  private final PluginWrapper<Gimbal> gimbal;
  private final PluginWrapper<Info> info;
  private final PluginWrapper<LogFiles> logFiles;
  private final PluginWrapper<ManualControl> manualControl;
  private final PluginWrapper<Mission> mission;
  private final PluginWrapper<MissionRaw> missionRaw;
  private final PluginWrapper<MissionRawServer> missionRawServer;
  private final PluginWrapper<Mocap> mocap;
  private final PluginWrapper<Offboard> offboard;
  private final PluginWrapper<Param> param;
  private final PluginWrapper<ParamServer> paramServer;
  private final PluginWrapper<ServerUtility> serverUtility;
  private final PluginWrapper<Shell> shell;
  private final PluginWrapper<Telemetry> telemetry;
  private final PluginWrapper<TelemetryServer> telemetryServer;
  private final PluginWrapper<TrackingServer> trackingServer;
  private final PluginWrapper<Transponder> transponder;
  private final PluginWrapper<Tune> tune;

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
    action = PluginWrapper.wrap(() -> new Action(host, port));
    actionServer = PluginWrapper.wrap(() -> new ActionServer(host, port));
    calibration = PluginWrapper.wrap(() -> new Calibration(host, port));
    camera = PluginWrapper.wrap(() -> new Camera(host, port));
    core = PluginWrapper.wrap(() -> new Core(host, port));
    failure = PluginWrapper.wrap(() -> new Failure(host, port));
    followMe = PluginWrapper.wrap(() -> new FollowMe(host, port));
    ftp = PluginWrapper.wrap(() -> new Ftp(host, port));
    geofence = PluginWrapper.wrap(() -> new Geofence(host, port));
    gimbal = PluginWrapper.wrap(() -> new Gimbal(host, port));
    info = PluginWrapper.wrap(() -> new Info(host, port));
    logFiles = PluginWrapper.wrap(() -> new LogFiles(host, port));
    manualControl = PluginWrapper.wrap(() -> new ManualControl(host, port));
    mission = PluginWrapper.wrap(() -> new Mission(host, port));
    missionRaw = PluginWrapper.wrap(() -> new MissionRaw(host, port));
    missionRawServer = PluginWrapper.wrap(() -> new MissionRawServer(host, port));
    mocap = PluginWrapper.wrap(() -> new Mocap(host, port));
    offboard = PluginWrapper.wrap(() -> new Offboard(host, port));
    param = PluginWrapper.wrap(() -> new Param(host, port));
    paramServer = PluginWrapper.wrap(() -> new ParamServer(host, port));
    serverUtility = PluginWrapper.wrap(() -> new ServerUtility(host, port));
    shell = PluginWrapper.wrap(() -> new Shell(host, port));
    telemetry = PluginWrapper.wrap(() -> new Telemetry(host, port));
    telemetryServer = PluginWrapper.wrap(() -> new TelemetryServer(host, port));
    trackingServer = PluginWrapper.wrap(() -> new TrackingServer(host, port));
    transponder = PluginWrapper.wrap(() -> new Transponder(host, port));
    tune = PluginWrapper.wrap(() -> new Tune(host, port));
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
  public TrackingServer getTrackingServer() {
    return trackingServer.get();
  }

  @NonNull
  public Transponder getTransponder() {
    return transponder.get();
  }

  @NonNull
  public Tune getTune() {
    return tune.get();
  }

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
    trackingServer.dispose();
    transponder.dispose();
    tune.dispose();
  }
}
