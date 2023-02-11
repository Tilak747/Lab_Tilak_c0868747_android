package c0868747.tilak.labtest.ui;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import c0868747.tilak.labtest.databinding.FragmentPickPlaceBinding;
import c0868747.tilak.labtest.model.FavLocation;

public class PickPlaceFragment extends Fragment implements LocationUpdateListener, OnMapReadyCallback {

    FragmentPickPlaceBinding binding;
    MainViewModel viewModel;
    FavLocation myFavLocation;

    private GoogleMap mMap;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(binding == null){
            binding = FragmentPickPlaceBinding.inflate(inflater,container,false);
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        myFavLocation = (FavLocation) getArguments().getSerializable("data");
    }

    @Override
    public void updateCurrentLocation(Location location) {
        if(myFavLocation == null){
            mMap.clear();
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(userLocation).title("your location!"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 10));
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        ((MainActivity) requireActivity()).checkLocationPermission();
    }
}
