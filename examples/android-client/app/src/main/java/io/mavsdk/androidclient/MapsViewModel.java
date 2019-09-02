package io.mavsdk.androidclient;

import android.annotation.SuppressLint;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.mapbox.mapboxsdk.geometry.LatLng;
import io.mavsdk.action.Action;
import io.mavsdk.mission.Mission;
import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel to hold objects that should be persisted.
 */
public class MapsViewModel extends ViewModel {

  private static final float MISSION_HEIGHT = 5.0f;
  private static final float MISSION_SPEED = 1.0f;

  final MutableLiveData<LatLng> currentPositionLiveData = new MutableLiveData<>();
  final MutableLiveData<List<LatLng>> currentMissionPlanLiveData = new MutableLiveData<>();

  public MapsViewModel() {
    currentMissionPlanLiveData.postValue(new ArrayList<>());
  }

  @Override
  protected void onCleared() {
    super.onCleared();
  }

  /**
   * Executes the current mission.
   */
  @SuppressLint("CheckResult")
  void startMission(Action action, Mission mission) {
    List<LatLng> latLngs = currentMissionPlanLiveData.getValue();
    if (latLngs != null) {
      List<Mission.MissionItem> missionItems = new ArrayList<>();
      for (LatLng latLng : latLngs) {
        Mission.MissionItem missionItem = new Mission.MissionItem(
            latLng.getLatitude(),
            latLng.getLongitude(),
            MISSION_HEIGHT,
            MISSION_SPEED,
            true,
            Float.NaN,
            Float.NaN,
            Mission.MissionItem.CameraAction.NONE,
            Float.NaN,
            1.0);
        missionItems.add(missionItem);
      }
      mission
          .setReturnToLaunchAfterMission(true)
          .andThen(mission.uploadMission(missionItems)
              .doOnComplete(() -> Log.d("MapsViewModel", "Upload succeeded"))
              .doOnError(throwable -> Log.e("MapsViewModel", "Failed to upload the mission")))
          .andThen(action.arm()
                        .onErrorComplete())
          .andThen(mission.startMission()
              .doOnComplete(() -> Log.d("MapsViewModel", "Mission started"))
              .doOnError(throwable -> Log.e("MapsViewModel", "Failed to start the mission")))
          .subscribe(() -> {}, throwable -> {});
    }
  }

  /**
   * Adds a waypoint to the current mission.
   *
   * @param latLng waypoint to add
   */
  void addWaypoint(LatLng latLng) {
    List<LatLng> currentMissionItems = currentMissionPlanLiveData.getValue();
    currentMissionItems.add(latLng);
    currentMissionPlanLiveData.postValue(currentMissionItems);
  }
}
