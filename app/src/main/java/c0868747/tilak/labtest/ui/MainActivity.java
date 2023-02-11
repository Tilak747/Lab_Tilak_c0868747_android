package c0868747.tilak.labtest.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import c0868747.tilak.labtest.BuildConfig;
import c0868747.tilak.labtest.R;
import c0868747.tilak.labtest.databinding.ActivityMainBinding;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE = 1;

    LocationUpdateListener locationUpdateListener = null;

    MainViewModel viewModel;
    ActivityMainBinding binding;
    NavController controller;

    LocationManager locationManager;
    LocationListener locationListener;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;

    @Override
    protected void onStart() {
        super.onStart();
        checkLocationPermission();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        controller = Navigation.findNavController(this, R.id.fragContainerView);
        controller.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            if(navDestination.getId() == R.id.placeListFragment){
                binding.fabAdd.setVisibility(View.VISIBLE);
            }
            else{
                binding.fabAdd.setVisibility(View.INVISIBLE);
            }
        });
        binding.fabAdd.setOnClickListener(v -> {
            controller.navigate(R.id.action_placeListFragment_to_pickPlaceFragment);
        });

        initLocationService();
    }

    public void checkLocationPermission(){
        // check permission
        if (!checkPermission())
            requestPermission();
        else
            getLastLocation();
    }
    private boolean checkPermission() {
        int permissionState = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (shouldProvideRationale) {

            showSnackBar(R.string.permission_warning, android.R.string.ok, (View.OnClickListener) v -> startLocationPermissionRequest());

        } else {
            startLocationPermissionRequest();
        }
    }


    private void showSnackBar(final int mainStringID, final int actionID, View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                        getString(mainStringID),
                        Snackbar.LENGTH_INDEFINITE).setAction(actionID, listener)
                .show();
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            checkLocationPermission();
            return;
        }
        if(lastLocation != null){
            setLocation(lastLocation);
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(this, task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Location location = task.getResult();
                setLocation(location);
            }
        });
    }
    Location lastLocation;
    private void setLocation(Location location){
        lastLocation = location;
        if(locationUpdateListener != null){
            locationUpdateListener.updateCurrentLocation(location);
        }
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10);
    }
    private void buildLocationCallBack() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location: locationResult.getLocations()) {
                    setLocation(location);
                }
            }
        };
    }

    private void initLocationService(){
        // initialize the fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        buildLocationRequest();
        buildLocationCallBack();
        // initialize the location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i(TAG, "onLocationChanged: " + location);
                setLocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length <= 0) {

        } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // permission is granted
            buildLocationRequest();
            buildLocationCallBack();
        } else {
            // permission is denied
            showSnackBar(R.string.permission_warning, R.string.setting, v -> {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
                intent.setData(uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            });

        }
    }


}