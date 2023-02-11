package c0868747.tilak.labtest.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import c0868747.tilak.labtest.R;
import c0868747.tilak.labtest.databinding.FragmentPickPlaceBinding;
import c0868747.tilak.labtest.model.FavLocation;
import c0868747.tilak.labtest.model.PlaceType;

public class PickPlaceFragment extends Fragment implements LocationUpdateListener {

    FragmentPickPlaceBinding binding;
    MainViewModel viewModel;
    @Nullable
    FavLocation myFavLocation = null;
    Location currentLocation = null;

    private MyMapFragment mMap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null) {
            binding = FragmentPickPlaceBinding.inflate(inflater, container, false);
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        try {
            myFavLocation = (FavLocation) getArguments().getSerializable("data");
        } catch (NullPointerException e) {

        }

        mMap = (MyMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        ((MainActivity) requireActivity()).locationUpdateListener = this;

        mMap.setMyMapReadyListener(googleMap -> {
            if (myFavLocation != null) {
                initMyFavPlaceData(myFavLocation);
            }
            ((MainActivity) requireActivity()).checkLocationPermission();
            setUpMapThings();
        });
        mMap.callMapAsyncNow();

        binding.tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        mMap.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                    case 1:
                        mMap.googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    default:
                        mMap.googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.btnSave.setOnClickListener(v -> {
            if (myFavLocation == null) {
                myFavLocation = new FavLocation();
            }
            String title = "";
            switch (binding.typeTab.getSelectedTabPosition()) {
                case 0:
                    title = PlaceType.HOME.toString();
                    break;
                case 1:
                    title = PlaceType.WORK.toString();
                    break;
                default:
                    title = PlaceType.OTHER.toString();
                    break;
            }
            myFavLocation.setTitle(
                    title
            );
            String desciption = "";
            double lat = 0.0;
            double lng = 0.0;
            if (marker != null) {
                desciption = marker.getTitle();
                lat = marker.getPosition().latitude;
                lng = marker.getPosition().longitude;
            } else if (currentMarker != null) {
                desciption = getCurrentDateTime();
                lat = currentMarker.getPosition().latitude;
                lng = currentMarker.getPosition().longitude;
            } else {
                desciption = getCurrentDateTime();
            }
            myFavLocation.setDescription(desciption);
            myFavLocation.setLat(lat);
            myFavLocation.setLng(lng);
            viewModel.addLocation(myFavLocation);
            Navigation.findNavController(requireActivity(), R.id.fragContainerView).popBackStack();
        });

    }

    private void initMyFavPlaceData(FavLocation location) {
        marker = drawMarker(
                new LatLng(location.getLat(), location.getLng()),
                location.getDescription(),
                true,
                true
        );
        String title = location.getTitle();

        if (title.equals(PlaceType.HOME.toString())) {
            TabLayout.Tab tab = binding.typeTab.getTabAt(0);
            tab.select();
        } else if (title.equals(PlaceType.WORK.toString())) {
            TabLayout.Tab tab = binding.typeTab.getTabAt(1);
            tab.select();
        } else {
            TabLayout.Tab tab = binding.typeTab.getTabAt(2);
            tab.select();
        }

        binding.desc.setText(location.getDescription());
        binding.btnSave.setText("Update");
    }

    Marker currentMarker = null;
    Marker marker = null;
    Marker distanceMarker = null;
    Polyline polyline;

    void calculateDistance(Location from,Location to){
        float distance = from.distanceTo(to) / 1000;
        if(distanceMarker != null){
            distanceMarker.remove();
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        LatLng fromLatLng = new LatLng(from.getLatitude(),from.getLongitude());
        LatLng toLatLng = new LatLng(to.getLatitude(),to.getLongitude());
        builder.include(fromLatLng);
        builder.include(toLatLng);
        LatLngBounds bounds = builder.build();
        LatLng center = bounds.getCenter();
//        mMap.googleMap.setLatLngBoundsForCameraTarget(bounds);

        String title = String.format("%.2f",distance) + "km";
        distanceMarker = drawMarker(center,title,false,false);

        if(polyline != null){
            polyline.remove();
        }
        PolylineOptions defaultLineOptions = new PolylineOptions();
        defaultLineOptions.width(4f);
        defaultLineOptions.color(ContextCompat.getColor(requireContext(), R.color.colorAccent));
        defaultLineOptions.jointType(JointType.ROUND);
        defaultLineOptions.startCap(new RoundCap());
        defaultLineOptions.endCap(new RoundCap());
        defaultLineOptions.add(fromLatLng);
        defaultLineOptions.add(toLatLng);
        polyline = mMap.googleMap.addPolyline(defaultLineOptions);

        distanceMarker.showInfoWindow();

    }

    void setUpMapThings() {

        mMap.googleMap.setOnMapClickListener(latLng -> {
            String updatedName = fetchCurrentLocationDetails(latLng);
            if (marker != null) {
                marker.remove();
            }
            marker = drawMarker(latLng, updatedName, true, true);
            binding.desc.setText(updatedName);
            Location from = new Location("");
            from.setLatitude(latLng.latitude);
            from.setLongitude(latLng.longitude);
            calculateDistance(
                    from,
                    currentLocation
            );
        });

        mMap.googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(@NonNull Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(@NonNull Marker marker) {
//                mMap.googleMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                String updatedName = fetchCurrentLocationDetails(marker.getPosition());
                marker.setTitle(updatedName);
                binding.desc.setText(updatedName);

                Location from = new Location("");
                from.setLatitude(marker.getPosition().latitude);
                from.setLongitude(marker.getPosition().longitude);
                calculateDistance(
                        from,
                        currentLocation
                );
            }

            @Override
            public void onMarkerDragStart(@NonNull Marker marker) {

            }
        });
    }

    String fetchCurrentLocationDetails(LatLng latLng) {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        String locationDetails = "";
        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            Address address = addressList.get(0);

            String featureName = address.getFeatureName();
            if (featureName != null && !featureName.isEmpty()) {
                locationDetails += featureName;
            }
            String locality = address.getLocality();
            if (locality != null && !locality.isEmpty()) {
                locationDetails += "," + locality;
            }
            String subLocality = address.getSubLocality();
            if (subLocality != null && !subLocality.isEmpty()) {
                locationDetails += "," + subLocality;
            }
            String countryName = address.getCountryName();
            if (countryName != null && !countryName.isEmpty()) {
                locationDetails += "," + countryName;
            }
            String postalCode = address.getPostalCode();
            if (postalCode != null && !postalCode.isEmpty()) {
                locationDetails += "," + postalCode;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (locationDetails.isEmpty()) {

            locationDetails += getCurrentDateTime();
        }
        return locationDetails;
    }

    String getCurrentDateTime() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa", Locale.getDefault());
        return df.format(date);
    }

    @Override
    public void updateCurrentLocation(Location location) {
        currentLocation = location;
        if (mMap.googleMap != null) {

            if (mMap.currentLocation == null) {
                mMap.currentLocation = location;

                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                if (currentMarker != null) {
                    currentMarker.remove();
                }
                String locationName = fetchCurrentLocationDetails(userLocation);
                currentMarker = drawMarker(userLocation, "my location", true, false);
//                binding.desc.setText(locationName);
            }
        }
    }

    Marker drawMarker(LatLng latLng, String title, boolean moveCamera, boolean draggable) {

        if (moveCamera) {
            mMap.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        }
        return mMap.googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(title)
                        .draggable(draggable)
        );
    }



}
