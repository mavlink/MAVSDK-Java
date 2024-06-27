package io.mavsdk.androidclient;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Circle;
import com.mapbox.mapboxsdk.plugins.annotation.CircleManager;
import com.mapbox.mapboxsdk.plugins.annotation.CircleOptions;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.utils.ColorUtils;

import io.mavsdk.MavsdkEventQueue;
import io.mavsdk.System;
import io.mavsdk.mavsdkserver.MavsdkServer;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main Activity to display map and create missions.
 * 1. Take off
 * 2. Long click on map to add a waypoint
 * 3. Hit play to start mission.
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
  private static final Logger logger = LoggerFactory.getLogger(MapsActivity.class);

  public static final String BACKEND_IP_ADDRESS = "127.0.0.1";

  private CircleManager circleManager;
  private SymbolManager symbolManager;

  private MapView mapView;
  private MapboxMap map;

  private Button buttonRunDestroyMavsdkServer;

  private MapsViewModel viewModel;
  private Symbol currentPositionMarker;

  private MavsdkServer mavsdkServer = new MavsdkServer();
  private System drone;
  private boolean isMavsdkServerRunning = false;
  private final List<Circle> waypoints = new ArrayList<>();
  private final List<Disposable> disposables = new ArrayList<>();

  private Observer<LatLng> currentPositionObserver = this::updateVehiclePosition;
  private Observer<List<LatLng>> currentMissionPlanObserver = this::updateMarkers;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Mapbox.getInstance(this, getString(R.string.access_token));
    setContentView(R.layout.activity_maps);
    mapView = findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);

    buttonRunDestroyMavsdkServer = findViewById(R.id.buttonRunDestroyMavsdkServer);
    buttonRunDestroyMavsdkServer.setOnClickListener(v -> runDestroyMavsdkServer());

    viewModel = ViewModelProviders.of(this).get(MapsViewModel.class);

    FloatingActionButton floatingActionButton = findViewById(R.id.fab);
    floatingActionButton.setOnClickListener(v -> viewModel.startMission(drone));
  }

  @Override
  public void onStart() {
    super.onStart();
    mapView.onStart();
  }

  @Override
  public void onResume() {
    super.onResume();
    mapView.onResume();
    viewModel.currentPositionLiveData.observe(this, currentPositionObserver);
    viewModel.currentMissionPlanLiveData.observe(this, currentMissionPlanObserver);
  }

  @Override
  public void onPause() {
    super.onPause();
    mapView.onPause();
    viewModel.currentPositionLiveData.removeObserver(currentPositionObserver);
    viewModel.currentMissionPlanLiveData.removeObserver(currentMissionPlanObserver);
  }

  @Override
  public void onStop() {
    super.onStop();
    mapView.onStop();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mapView.onDestroy();
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_maps, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle item selection
    int itemId = item.getItemId();
    if(itemId == R.id.disarm) {
      drone.getAction().kill().subscribe();
    } else if (itemId == R.id.land) {
      drone.getAction().land().subscribe();
    } else if (itemId == R.id.return_home) {
      drone.getAction().returnToLaunch().subscribe();
    } else if (itemId == R.id.takeoff) {
      drone.getAction().arm().andThen(drone.getAction().takeoff()).subscribe();
    } else {
      return super.onOptionsItemSelected(item);
    }
    return true;
  }

  /**
   * Update [currentPositionMarker] position with a new [position].
   *
   * @param newLatLng new position of the vehicle
   */
  private void updateVehiclePosition(@Nullable LatLng newLatLng) {
    if (newLatLng == null || map == null || symbolManager == null) {
      // Not ready
      return;
    }

    // Add a vehicle marker and move the camera
    if (currentPositionMarker == null) {
      SymbolOptions symbolOptions = new SymbolOptions();
      symbolOptions.withLatLng(newLatLng);
      symbolOptions.withIconImage("marker-icon-id");
      currentPositionMarker = symbolManager.create(symbolOptions);

      map.moveCamera(CameraUpdateFactory.tiltTo(0));
      map.moveCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 14.0f));
    } else {
      currentPositionMarker.setLatLng(newLatLng);
      symbolManager.update(currentPositionMarker);
    }
  }

  /**
   * Update the [map] with the current mission plan waypoints.
   *
   * @param latLngs current mission waypoints
   */
  private void updateMarkers(@NonNull List<LatLng> latLngs) {
    if (circleManager != null) {
      circleManager.delete(waypoints);
      waypoints.clear();
    }

    for (LatLng latLng : latLngs) {
      CircleOptions circleOptions = new CircleOptions()
              .withLatLng(latLng)
              .withCircleColor(ColorUtils.colorToRgbaString(Color.BLUE))
              .withCircleStrokeColor(ColorUtils.colorToRgbaString(Color.BLACK))
              .withCircleStrokeWidth(1.0f)
              .withCircleRadius(12f)
              .withDraggable(false);

      circleManager.create(circleOptions);
    }
  }

  /**
   * Manipulates the map once available.
   * This callback is triggered when the map is ready to be used.
   * This is where we can add markers or lines, add listeners or move the camera.
   */
  @Override
  public void onMapReady(@NonNull MapboxMap mapboxMap) {
    mapboxMap.getUiSettings().setRotateGesturesEnabled(false);
    mapboxMap.getUiSettings().setTiltGesturesEnabled(false);
    mapboxMap.addOnMapLongClickListener(point -> {
      viewModel.addWaypoint(point);
      return true;
    });

    mapboxMap.setStyle(Style.LIGHT, style -> {
      // Add the marker image to map
      style.addImage("marker-icon-id",
              BitmapFactory.decodeResource(
                      MapsActivity.this.getResources(), com.mapbox.mapboxsdk.plugins.annotation.R.drawable.mapbox_marker_icon_default));

      symbolManager = new SymbolManager(this.mapView, this.map, style);
      symbolManager.setIconAllowOverlap(true);
      circleManager = new CircleManager(this.mapView, this.map, style);
    });

    map = mapboxMap;
  }

  private void runDestroyMavsdkServer() {
    if (!isMavsdkServerRunning) {
      runMavsdkServer();
    } else {
      destroyMavsdkServer();
    }
  }

  private void runMavsdkServer() {
    MavsdkEventQueue.executor().execute(() -> {
      int mavsdkServerPort = mavsdkServer.run();
      drone = new System(BACKEND_IP_ADDRESS, mavsdkServerPort);

      disposables.add(drone.getTelemetry().getFlightMode().distinctUntilChanged()
              .subscribe(flightMode -> logger.debug("flight mode: " + flightMode)));
      disposables.add(drone.getTelemetry().getArmed().distinctUntilChanged()
              .subscribe(armed -> logger.debug("armed: " + armed)));
      disposables.add(drone.getTelemetry().getPosition().subscribe(position -> {
        LatLng latLng = new LatLng(position.getLatitudeDeg(), position.getLongitudeDeg());
        viewModel.currentPositionLiveData.postValue(latLng);
      }));

      isMavsdkServerRunning = true;
      runOnUiThread(() -> buttonRunDestroyMavsdkServer.setText(R.string.destroy_mavsdk_server));
    });
  }

  private void destroyMavsdkServer() {
    MavsdkEventQueue.executor().execute(() -> {
      for (Disposable disposable : disposables) {
        disposable.dispose();
      }
      disposables.clear();
      drone.dispose();
      drone = null;
      mavsdkServer.stop();
      mavsdkServer.destroy();

      isMavsdkServerRunning = false;
      runOnUiThread(() -> {
        symbolManager.delete(currentPositionMarker);
        currentPositionMarker = null;
        buttonRunDestroyMavsdkServer.setText(R.string.run_mavsdk_server);
      });
    });
  }
}