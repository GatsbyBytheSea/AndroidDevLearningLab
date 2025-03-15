package fr.imt_atlantique.myfirstapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class GoogleMapsMarkerActivity extends AppCompatActivity {

    private GoogleMap mMap;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private ActivityResultLauncher<Intent> markerInputLauncher;
    private static final String KEY_MARKERS = "markers";
    private static final String KEY_MAP_TYPE = "map_type";
    private static final String KEY_CAMERA_POSITION = "camera_position";

    private LatLng pendingMarkerLatLng = null;
    private ArrayList<MarkerData> markerDataList = new ArrayList<>();
    private int mapType = GoogleMap.MAP_TYPE_NORMAL;
    private CameraPosition cameraPosition = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_google_maps_marker);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.google_maps_marker), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Restore saved instance state
        if(savedInstanceState != null) {
            markerDataList = savedInstanceState.getParcelableArrayList(KEY_MARKERS);
            mapType = savedInstanceState.getInt(KEY_MAP_TYPE);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
            if(markerDataList == null) {
                markerDataList = new ArrayList<>();
            }
        }

        markerInputLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                String markerText = result.getData().getStringExtra("markerText");
                String markerImagePath = result.getData().getStringExtra("markerImagePath");

                if(pendingMarkerLatLng != null){
                    MarkerOptions options = new MarkerOptions().position(pendingMarkerLatLng).title(markerText);
                    if(markerImagePath != null){
                        Bitmap scaledIcon = getResizedBitmap(markerImagePath, 100, 100);
                        options.icon(BitmapDescriptorFactory.fromBitmap(scaledIcon));
                    }
                    Marker marker = mMap.addMarker(options);
                    MarkerData data = new MarkerData(pendingMarkerLatLng, markerText, markerImagePath);
                    marker.setTag(data);
                    markerDataList.add(data);
                    pendingMarkerLatLng = null;
                }
            }
        );

        // Initialize the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if(mapFragment != null) {
            mapFragment.getMapAsync(googleMap -> {
                mMap = googleMap;

                mMap.getUiSettings().setZoomGesturesEnabled(true);
                mMap.getUiSettings().setCompassEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);

                if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                }
                mMap.setMyLocationEnabled(true);

                LatLng imtBrest = new LatLng(48.359285, -4.569933);
                mMap.addMarker(new MarkerOptions().position(imtBrest).title("IMT Atlantique - Brest"));

                // Move the camera to the saved position
                if( cameraPosition == null ) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(imtBrest, 15));
                }else{
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }

                // Add markers from saved instance state
                for(MarkerData data : markerDataList ) {
                    MarkerOptions options = new MarkerOptions().position(data.position).title(data.title);
                    if(data.markerImagePath != null){
                        Bitmap scaledIcon = getResizedBitmap(data.markerImagePath, 100, 100);
                        options.icon(BitmapDescriptorFactory.fromBitmap(scaledIcon));
                    }
                    Marker marker = mMap.addMarker(options);
                    marker.setTag(data);
                }

                // Set the saved map type
                mMap.setMapType(mapType);


                // Add listeners
                mMap.setOnMapClickListener(latLng -> {
                    String message = getString(R.string.word_latitude) + ": " + latLng.latitude + "\n"
                            + getString(R.string.word_longitude) + ": " + latLng.longitude;
                    Toast.makeText(GoogleMapsMarkerActivity.this, message, Toast.LENGTH_SHORT).show();
                });

                mMap.setOnMapLongClickListener(latLng -> {
                    pendingMarkerLatLng = latLng;
                    Intent intent = new Intent(GoogleMapsMarkerActivity.this, MarkerInputActivity.class);
                    markerInputLauncher.launch(intent);
                });

                mMap.setOnMarkerClickListener(marker -> {
                    MarkerData data = (MarkerData) marker.getTag();
                    if(data != null) {
                        Intent markerDetailIntent = new Intent(GoogleMapsMarkerActivity.this, MarkerDetailActivity.class);
                        markerDetailIntent.putExtra("markerTitle", data.title);
                        markerDetailIntent.putExtra("markerLatLng", data.position);
                        markerDetailIntent.putExtra("markerImagePath", data.markerImagePath);
                        startActivity(markerDetailIntent);
                        return true;
                    }else{
                        return false;
                    }
                });
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map_type, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_map_type_normal) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        } else if(item.getItemId() == R.id.menu_map_type_hybrid) {
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        } else if(item.getItemId() == R.id.menu_map_type_satellite) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putInt(KEY_MAP_TYPE, mMap.getMapType());
        }
        outState.putParcelableArrayList(KEY_MARKERS, markerDataList);
    }

    private Bitmap getResizedBitmap(String imagePath, int width, int height) {
        Bitmap original = BitmapFactory.decodeFile(imagePath);
        if(original != null){
            return Bitmap.createScaledBitmap(original, width, height, true);
        }
        return null;
    }
}