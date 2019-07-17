package io.mavsdk.androidclient;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import io.mavsdk.mission.Mission;

/**
 * ViewModel to hold objects that should be persisted
 */
public class MapsViewModel extends ViewModel {

    private static final float MISSION_HEIGHT = 5.0f; // height of the vehicle from the reference location in meters
    private static final float MISSION_SPEED = 1.0f; // speed the vehicle flies the mission in meters per second
    // creating a Vehicle is expensive, avoid recreating it if you can
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
     * Executes the current mission
     */
    public void startMission(Mission mission) {
        List<LatLng> latLngs = currentMissionPlanLiveData.getValue();
        if (latLngs != null) {
            List<Mission.MissionItem> missionItems = new ArrayList<>();
            for (LatLng latLng : latLngs) {
                Mission.MissionItem missionItem = new Mission.MissionItem(
                        latLng.latitude,
                        latLng.longitude,
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
                    .setReturnToLaunchAfterMission(false)
                    .andThen(mission.uploadMission(missionItems)
                            .doOnComplete(() -> Log.d("MapsViewModel", "Upload succeeded")))
                    .andThen(mission.startMission()
                            .doOnComplete(() -> Log.d("MapsViewModel", "Mission started")))
                    .subscribe();
        }
    }

    /**
     * Adds a waypoint to the current mission
     *
     * @param index    index to add the waypoint at
     * @param latLng waypoint to add
     */
    public void addWaypoint(int index, LatLng latLng) {
        List<LatLng> currentMissionItems = currentMissionPlanLiveData.getValue();
        currentMissionItems.add(index, latLng);
        currentMissionPlanLiveData.postValue(currentMissionItems);
    }

    /**
     * Removes waypoint at [index] from the current mission
     *
     * @param index index of waypoint to remove
     */
    public void removeWaypoint(int index) {
        List<LatLng> latLngs = currentMissionPlanLiveData.getValue();
        latLngs.remove(index);
        currentMissionPlanLiveData.postValue(latLngs);
    }

    /**
     * Replaces waypoint at [index] with [waypoint]
     *
     * @param index index of the waypoint to replace
     */
    public void replaceWaypoint(int index, LatLng waypoint) {
        List<LatLng> latLngs = currentMissionPlanLiveData.getValue();
        latLngs.set(index, waypoint);
        currentMissionPlanLiveData.postValue(latLngs);
    }
}
