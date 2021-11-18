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
import io.mavsdk.internal.DoubleCheckBind;
import io.mavsdk.internal.MavsdkExecutors;
import io.mavsdk.internal.Provider;
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

  private final Provider<Action> actionProvider;
  private final Provider<ActionServer> actionServerProvider;
  private final Provider<Calibration> calibrationProvider;
  private final Provider<Camera> cameraProvider;
  private final Provider<Core> coreProvider;
  private final Provider<Failure> failureProvider;
  private final Provider<FollowMe> followMeProvider;
  private final Provider<Ftp> ftpProvider;
  private final Provider<Geofence> geofenceProvider;
  private final Provider<Gimbal> gimbalProvider;
  private final Provider<Info> infoProvider;
  private final Provider<LogFiles> logFilesProvider;
  private final Provider<ManualControl> manualControlProvider;
  private final Provider<Mission> missionProvider;
  private final Provider<MissionRaw> missionRawProvider;
  private final Provider<MissionRawServer> missionRawServerProvider;
  private final Provider<Mocap> mocapProvider;
  private final Provider<Offboard> offboardProvider;
  private final Provider<Param> paramProvider;
  private final Provider<ParamServer> paramServerProvider;
  private final Provider<ServerUtility> serverUtilityProvider;
  private final Provider<Shell> shellProvider;
  private final Provider<Telemetry> telemetryProvider;
  private final Provider<TelemetryServer> telemetryServerProvider;
  private final Provider<TrackingServer> trackingServerProvider;
  private final Provider<Transponder> transponderProvider;
  private final Provider<Tune> tuneProvider;

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
   *
   * @param host the address of mavsdk_server
   * @param port the port of mavsdk_server
   */
  public System(@NonNull String host, int port) {
    actionProvider = DoubleCheckBind.provider(() -> new Action(host, port));
    actionServerProvider = DoubleCheckBind.provider(() -> new ActionServer(host, port));
    calibrationProvider = DoubleCheckBind.provider(() -> new Calibration(host, port));
    cameraProvider = DoubleCheckBind.provider(() -> new Camera(host, port));
    coreProvider = DoubleCheckBind.provider(() -> new Core(host, port));
    failureProvider = DoubleCheckBind.provider(() -> new Failure(host, port));
    followMeProvider = DoubleCheckBind.provider(() -> new FollowMe(host, port));
    ftpProvider = DoubleCheckBind.provider(() -> new Ftp(host, port));
    geofenceProvider = DoubleCheckBind.provider(() -> new Geofence(host, port));
    gimbalProvider = DoubleCheckBind.provider(() -> new Gimbal(host, port));
    infoProvider = DoubleCheckBind.provider(() -> new Info(host, port));
    logFilesProvider = DoubleCheckBind.provider(() -> new LogFiles(host, port));
    manualControlProvider = DoubleCheckBind.provider(() -> new ManualControl(host, port));
    missionProvider = DoubleCheckBind.provider(() -> new Mission(host, port));
    missionRawProvider = DoubleCheckBind.provider(() -> new MissionRaw(host, port));
    missionRawServerProvider = DoubleCheckBind.provider(() -> new MissionRawServer(host, port));
    mocapProvider = DoubleCheckBind.provider(() -> new Mocap(host, port));
    offboardProvider = DoubleCheckBind.provider(() -> new Offboard(host, port));
    paramProvider = DoubleCheckBind.provider(() -> new Param(host, port));
    paramServerProvider = DoubleCheckBind.provider(() -> new ParamServer(host, port));
    serverUtilityProvider = DoubleCheckBind.provider(() -> new ServerUtility(host, port));
    shellProvider = DoubleCheckBind.provider(() -> new Shell(host, port));
    telemetryProvider = DoubleCheckBind.provider(() -> new Telemetry(host, port));
    telemetryServerProvider = DoubleCheckBind.provider(() -> new TelemetryServer(host, port));
    trackingServerProvider = DoubleCheckBind.provider(() -> new TrackingServer(host, port));
    transponderProvider = DoubleCheckBind.provider(() -> new Transponder(host, port));
    tuneProvider = DoubleCheckBind.provider(() -> new Tune(host, port));
  }

  @NonNull
  public Action getAction() {
    return actionProvider.get();
  }

  @NonNull
  public ActionServer getActionServer() {
    return actionServerProvider.get();
  }

  @NonNull
  public Calibration getCalibration() {
    return calibrationProvider.get();
  }

  @NonNull
  public Camera getCamera() {
    return cameraProvider.get();
  }

  @NonNull
  public Core getCore() {
    return coreProvider.get();
  }

  @NonNull
  public Failure getFailure() {
    return failureProvider.get();
  }

  @NonNull
  public FollowMe getFollowMe() {
    return followMeProvider.get();
  }

  @NonNull
  public Ftp getFtp() {
    return ftpProvider.get();
  }

  @NonNull
  public Geofence getGeofence() {
    return geofenceProvider.get();
  }

  @NonNull
  public Gimbal getGimbal() {
    return gimbalProvider.get();
  }

  @NonNull
  public Info getInfo() {
    return infoProvider.get();
  }

  @NonNull
  public LogFiles getLogFiles() {
    return logFilesProvider.get();
  }

  @NonNull
  public ManualControl getManualControl() {
    return manualControlProvider.get();
  }

  @NonNull
  public Mission getMission() {
    return missionProvider.get();
  }

  @NonNull
  public MissionRaw getMissionRaw() {
    return missionRawProvider.get();
  }

  @NonNull
  public MissionRawServer getMissionRawServer() {
    return missionRawServerProvider.get();
  }

  @NonNull
  public Mocap getMocap() {
    return mocapProvider.get();
  }

  @NonNull
  public Offboard getOffboard() {
    return offboardProvider.get();
  }

  @NonNull
  public Param getParam() {
    return paramProvider.get();
  }

  @NonNull
  public ParamServer getParamServer() {
    return paramServerProvider.get();
  }

  @NonNull
  public ServerUtility getServerUtility() {
    return serverUtilityProvider.get();
  }

  @NonNull
  public Shell getShell() {
    return shellProvider.get();
  }

  @NonNull
  public Telemetry getTelemetry() {
    return telemetryProvider.get();
  }

  @NonNull
  public TelemetryServer getTelemetryServer() {
    return telemetryServerProvider.get();
  }

  @NonNull
  public TrackingServer getTrackingServer() {
    return trackingServerProvider.get();
  }

  @NonNull
  public Transponder getTransponder() {
    return transponderProvider.get();
  }

  @NonNull
  public Tune getTune() {
    return tuneProvider.get();
  }


  /**
   * Dispose of all the plugins.
   */
  public void dispose() {
    actionProvider.get().dispose();
    actionServerProvider.get().dispose();
    calibrationProvider.get().dispose();
    cameraProvider.get().dispose();
    coreProvider.get().dispose();
    failureProvider.get().dispose();
    followMeProvider.get().dispose();
    ftpProvider.get().dispose();
    geofenceProvider.get().dispose();
    gimbalProvider.get().dispose();
    infoProvider.get().dispose();
    logFilesProvider.get().dispose();
    manualControlProvider.get().dispose();
    missionProvider.get().dispose();
    missionRawProvider.get().dispose();
    missionRawServerProvider.get().dispose();
    mocapProvider.get().dispose();
    offboardProvider.get().dispose();
    paramProvider.get().dispose();
    paramServerProvider.get().dispose();
    serverUtilityProvider.get().dispose();
    shellProvider.get().dispose();
    telemetryProvider.get().dispose();
    telemetryServerProvider.get().dispose();
    trackingServerProvider.get().dispose();
    transponderProvider.get().dispose();
    tuneProvider.get().dispose();
  }
}
