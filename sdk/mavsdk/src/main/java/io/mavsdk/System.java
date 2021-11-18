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
import io.mavsdk.internal.DoubleCheck;
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

    actionProvider = DoubleCheck.create(() -> {
      Action plugin = new Action(host, port);
      MavsdkExecutors.connectionExecutor().execute(plugin::bind);
      return plugin;
    });

    actionServerProvider = DoubleCheck.create(() -> {
      ActionServer plugin = new ActionServer(host, port);
      MavsdkExecutors.connectionExecutor().execute(plugin::bind);
      return plugin;
    });

    calibrationProvider = DoubleCheck.create(() -> {
      Calibration plugin = new Calibration(host, port);
      MavsdkExecutors.connectionExecutor().execute(plugin::bind);
      return plugin;
    });

    cameraProvider = DoubleCheck.create(() -> {
      Camera plugin = new Camera(host, port);
      MavsdkExecutors.connectionExecutor().execute(plugin::bind);
      return plugin;
    });

    coreProvider = DoubleCheck.create(() -> {
      Core plugin = new Core(host, port);
      MavsdkExecutors.connectionExecutor().execute(plugin::bind);
      return plugin;
    });

    failureProvider = DoubleCheck.create(() -> {
      Failure plugin = new Failure(host, port);
      MavsdkExecutors.connectionExecutor().execute(plugin::bind);
      return plugin;
    });

    followMeProvider = DoubleCheck.create(() -> {
      FollowMe plugin = new FollowMe(host, port);
      MavsdkExecutors.connectionExecutor().execute(plugin::bind);
      return plugin;
    });

    ftpProvider = DoubleCheck.create(() -> {
      Ftp plugin = new Ftp(host, port);
      MavsdkExecutors.connectionExecutor().execute(plugin::bind);
      return plugin;
    });

    geofenceProvider = DoubleCheck.create(() -> {
      Geofence plugin = new Geofence(host, port);
      MavsdkExecutors.connectionExecutor().execute(plugin::bind);
      return plugin;
    });

    gimbalProvider = DoubleCheck.create(() -> {
      Gimbal plugin = new Gimbal(host, port);
      MavsdkExecutors.connectionExecutor().execute(plugin::bind);
      return plugin;
    });

    infoProvider = DoubleCheck.create(() -> {
      Info plugin = new Info(host, port);
      MavsdkExecutors.connectionExecutor().execute(plugin::bind);
      return plugin;
    });

    logFilesProvider = DoubleCheck.create(() -> {
      LogFiles plugin = new LogFiles(host, port);
      MavsdkExecutors.connectionExecutor().execute(plugin::bind);
      return plugin;
    });

    manualControlProvider = DoubleCheck.create(() -> {
      ManualControl plugin = new ManualControl(host, port);
      MavsdkExecutors.connectionExecutor().execute(plugin::bind);
      return plugin;
    });

    missionProvider = DoubleCheck.create(() -> {
      Mission plugin = new Mission(host, port);
      MavsdkExecutors.connectionExecutor().execute(plugin::bind);
      return plugin;
    });

    missionRawProvider = DoubleCheck.create(() -> {
      MissionRaw plugin = new MissionRaw(host, port);
      MavsdkExecutors.connectionExecutor().execute(plugin::bind);
      return plugin;
    });

    missionRawServerProvider = DoubleCheck.create(() -> {
      MissionRawServer plugin = new MissionRawServer(host, port);
      MavsdkExecutors.connectionExecutor().execute(plugin::bind);
      return plugin;
    });

    mocapProvider = DoubleCheck.create(() -> {
      Mocap plugin = new Mocap(host, port);
      MavsdkExecutors.connectionExecutor().execute(plugin::bind);
      return plugin;
    });

    offboardProvider = DoubleCheck.create(() -> {
      Offboard plugin = new Offboard(host, port);
      MavsdkExecutors.connectionExecutor().execute(plugin::bind);
      return plugin;
    });

    paramProvider = DoubleCheck.create(() -> {
      Param plugin = new Param(host, port);
      MavsdkExecutors.connectionExecutor().execute(plugin::bind);
      return plugin;
    });

    paramServerProvider = DoubleCheck.create(() -> {
      ParamServer plugin = new ParamServer(host, port);
      MavsdkExecutors.connectionExecutor().execute(plugin::bind);
      return plugin;
    });

    serverUtilityProvider = DoubleCheck.create(() -> {
      ServerUtility plugin = new ServerUtility(host, port);
      MavsdkExecutors.connectionExecutor().execute(plugin::bind);
      return plugin;
    });

    shellProvider = DoubleCheck.create(() -> {
      Shell plugin = new Shell(host, port);
      MavsdkExecutors.connectionExecutor().execute(plugin::bind);
      return plugin;
    });

    telemetryProvider = DoubleCheck.create(() -> {
      Telemetry plugin = new Telemetry(host, port);
      MavsdkExecutors.connectionExecutor().execute(plugin::bind);
      return plugin;
    });

    telemetryServerProvider = DoubleCheck.create(() -> {
      TelemetryServer plugin = new TelemetryServer(host, port);
      MavsdkExecutors.connectionExecutor().execute(plugin::bind);
      return plugin;
    });

    trackingServerProvider = DoubleCheck.create(() -> {
      TrackingServer plugin = new TrackingServer(host, port);
      MavsdkExecutors.connectionExecutor().execute(plugin::bind);
      return plugin;
    });

    transponderProvider = DoubleCheck.create(() -> {
      Transponder plugin = new Transponder(host, port);
      MavsdkExecutors.connectionExecutor().execute(plugin::bind);
      return plugin;
    });

    tuneProvider = DoubleCheck.create(() -> {
      Tune plugin = new Tune(host, port);
      MavsdkExecutors.connectionExecutor().execute(plugin::bind);
      return plugin;
    });
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
    this.actionProvider.get().dispose();
    this.actionServerProvider.get().dispose();
    this.calibrationProvider.get().dispose();
    this.cameraProvider.get().dispose();
    this.coreProvider.get().dispose();
    this.failureProvider.get().dispose();
    this.followMeProvider.get().dispose();
    this.ftpProvider.get().dispose();
    this.geofenceProvider.get().dispose();
    this.gimbalProvider.get().dispose();
    this.infoProvider.get().dispose();
    this.logFilesProvider.get().dispose();
    this.manualControlProvider.get().dispose();
    this.missionProvider.get().dispose();
    this.missionRawProvider.get().dispose();
    this.missionRawServerProvider.get().dispose();
    this.mocapProvider.get().dispose();
    this.offboardProvider.get().dispose();
    this.paramProvider.get().dispose();
    this.paramServerProvider.get().dispose();
    this.serverUtilityProvider.get().dispose();
    this.shellProvider.get().dispose();
    this.telemetryProvider.get().dispose();
    this.telemetryServerProvider.get().dispose();
    this.trackingServerProvider.get().dispose();
    this.transponderProvider.get().dispose();
    this.tuneProvider.get().dispose();
  }
}
