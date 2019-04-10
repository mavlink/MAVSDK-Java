package io.dronecore.dronecoreclient;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import io.dronecode_sdk.action.Action;
import io.dronecode_sdk.mission.Mission;
import io.dronecode_sdk.telemetry.Telemetry;
import io.reactivex.disposables.Disposable;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;

/**
 * Main Activity to display map and create missions.
 * 1. Take off
 * 2. Long click on map to add a waypoint
 * 3. Touch an existing waypoint to delete it
 * 4. Hit play to start mission.
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    public static final String TAG = "MapsActivity";
    public static final String BACKEND_IP_ADDRESS = "10.42.0.65"; // todo update this to ip address of device running the backend. remove this once the backend is included in the app

    private GoogleMap map = null;
    private MapsViewModel viewModel;
    private Marker currentPositionMarker;
    private final List<Marker> markers = new ArrayList<>();
    private Action action;
    private Mission mission;
    private Telemetry telemetry;
    private final List<Disposable> disposables = new ArrayList<>();

    private Observer<LatLng> currentPositionObserver = this::updateVehiclePosition;
    private Observer<List<LatLng>> currentMissionPlanObserver = this::updateMarkers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        viewModel = ViewModelProviders.of(this).get(MapsViewModel.class);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(v -> viewModel.startMission(mission));
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.currentPositionLiveData.observe(this, currentPositionObserver);
        viewModel.currentMissionPlanLiveData.observe(this, currentMissionPlanObserver);
        action = new Action(BACKEND_IP_ADDRESS, 50051); // TODO: 4/4/19 50051 should be a constant in the sdk somewhere
        mission = new Mission(BACKEND_IP_ADDRESS, 50051);
        telemetry = new Telemetry(BACKEND_IP_ADDRESS, 50051);
        disposables.add(telemetry.getFlightMode().distinct()
                .subscribe(flightMode -> Log.d(TAG, "flight mode: " + flightMode)));
        disposables.add(telemetry.getArmed().distinct()
                .subscribe(armed -> Log.d(TAG, "armed: " + armed)));
        disposables.add(telemetry.getPosition().subscribe(position -> {
            LatLng latLng = new LatLng(position.getLatitudeDeg(), position.getLongitudeDeg());
            viewModel.currentPositionLiveData.postValue(latLng);
        }));
    }

    @Override
    public void onPause() {
        super.onPause();
        viewModel.currentPositionLiveData.removeObserver(currentPositionObserver);
        viewModel.currentMissionPlanLiveData.removeObserver(currentMissionPlanObserver);
        for (Disposable disposable : disposables) {
            disposable.dispose();
        }

        // TODO: 4/10/19 close these channels properly
        action = null;
        mission = null;
        telemetry = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_maps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.disarm:
                action.kill().subscribe();
                break;
            case R.id.land:
                action.land().subscribe();
                break;
            case R.id.return_home:
                action.returnToLaunch().subscribe();
                break;
            case R.id.takeoff:
                action.arm().andThen(action.takeoff()).subscribe();
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /**
     * Update [currentPositionMarker] position with a new [position]
     *
     * @param newLatLng new position of the vehicle
     */
    private void updateVehiclePosition(@Nullable LatLng newLatLng) {
        if (newLatLng != null && map != null) {
            // Add a vehicle marker and move the camera
            if (currentPositionMarker == null) {
                currentPositionMarker = map.addMarker(new MarkerOptions().position(newLatLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 20.0f));
            } else {
                currentPositionMarker.setPosition(newLatLng);
            }
        }
    }

    /**
     * Update the [map] with the current mission plan waypoints
     *
     * @param latLngs current mission waypoints
     */
    private void updateMarkers(@Nullable List<LatLng> latLngs) {
        if (map == null) {
            return;
        }
        for (Marker marker : markers) {
            marker.remove();
        }
        markers.clear();
        if (latLngs != null) {
            Log.d("Waypoints", "received mission plan with size " + latLngs.size());
            for (LatLng latLng : latLngs) {
                markers.add(map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .draggable(true)));
            }
        }
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        if (marker == null) {
            return;
        }

        int index = markers.indexOf(marker);
        viewModel.replaceWaypoint(index, marker.getPosition());
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
    }

    @Override
    public void onMarkerDrag(Marker marker) {
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapLongClickListener(latLng -> viewModel.addWaypoint(markers.size(), latLng));

        map.setOnMarkerClickListener(marker -> {
            int index = markers.indexOf(marker);
            if (index != -1) {
                viewModel.removeWaypoint(index);
            }
            return true;
        });

        map.setMapType(MAP_TYPE_HYBRID);
        map.setOnMarkerDragListener(this);
        updateMarkers(viewModel.currentMissionPlanLiveData.getValue());
    }
}
