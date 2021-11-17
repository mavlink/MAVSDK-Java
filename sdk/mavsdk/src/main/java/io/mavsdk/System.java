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

public class System {
  private final Action action;
  private final ActionServer actionServer;
  private final Calibration calibration;
  private final Camera camera;
  private final Core core;
  private final Failure failure;
  private final FollowMe followMe;
  private final Ftp ftp;
  private final Geofence geofence;
  private final Gimbal gimbal;
  private final Info info;
  private final LogFiles logFiles;
  private final ManualControl manualControl;
  private final Mission mission;
  private final MissionRaw missionRaw;
  private final MissionRawServer missionRawServer;
  private final Mocap mocap;
  private final Offboard offboard;
  private final Param param;
  private final ParamServer paramServer;
  private final ServerUtility serverUtility;
  private final Shell shell;
  private final Telemetry telemetry;
  private final TelemetryServer telemetryServer;
  private final TrackingServer trackingServer;
  private final Transponder transponder;
  private final Tune tune;

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
    this.actionServer = new ActionServer(host, port);
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
    this.missionRawServer = new MissionRawServer(host, port);
    this.mocap = new Mocap(host, port);
    this.offboard = new Offboard(host, port);
    this.param = new Param(host, port);
    this.paramServer = new ParamServer(host, port);
    this.serverUtility = new ServerUtility(host, port);
    this.shell = new Shell(host, port);
    this.telemetry = new Telemetry(host, port);
    this.telemetryServer = new TelemetryServer(host, port);
    this.trackingServer = new TrackingServer(host, port);
    this.transponder = new Transponder(host, port);
    this.tune = new Tune(host, port);
  }

  public Action getAction() {
    return action;
  }

  public ActionServer getActionServer() {
    return actionServer;
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

  public MissionRawServer getMissionRawServer() {
    return missionRawServer;
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

  public ParamServer getParamServer() {
    return paramServer;
  }

  public ServerUtility getServerUtility() {
    return serverUtility;
  }

  public Shell getShell() {
    return shell;
  }

  public Telemetry getTelemetry() {
    return telemetry;
  }

  public TelemetryServer getTelemetryServer() {
    return telemetryServer;
  }

  public TrackingServer getTrackingServer() {
    return trackingServer;
  }

  public Transponder getTransponder() {
    return transponder;
  }

  public Tune getTune() {
    return tune;
  }

  /**
   * Bind all plugins.
   */
  public void bind() {
    this.action.bind();
    this.actionServer.bind();
    this.calibration.bind();
    this.camera.bind();
    this.core.bind();
    this.failure.bind();
    this.followMe.bind();
    this.ftp.bind();
    this.geofence.bind();
    this.gimbal.bind();
    this.info.bind();
    this.logFiles.bind();
    this.manualControl.bind();
    this.mission.bind();
    this.missionRaw.bind();
    this.missionRawServer.bind();
    this.mocap.bind();
    this.offboard.bind();
    this.param.bind();
    this.paramServer.bind();
    this.serverUtility.bind();
    this.shell.bind();
    this.telemetry.bind();
    this.telemetryServer.bind();
    this.trackingServer.bind();
    this.transponder.bind();
    this.tune.bind();
  }

  /**
   * Dispose of all the plugins.
   */
  public void dispose() {
    this.action.dispose();
    this.actionServer.dispose();
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
    this.missionRawServer.dispose();
    this.mocap.dispose();
    this.offboard.dispose();
    this.param.dispose();
    this.paramServer.dispose();
    this.serverUtility.dispose();
    this.shell.dispose();
    this.telemetry.dispose();
    this.telemetryServer.dispose();
    this.trackingServer.dispose();
    this.transponder.dispose();
    this.tune.dispose();
  }
}
