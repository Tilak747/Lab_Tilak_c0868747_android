package c0868747.tilak.labtest.ui;

import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class MyMapFragment extends SupportMapFragment implements OnMapReadyCallback {

    GoogleMap googleMap = null;
    MyMapReadyListener mapReadyListener;
    Location currentLocation = null;

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        initMap(googleMap);
    }

    public void initMap(GoogleMap googleMap){
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setAllGesturesEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(true);
        googleMap.getUiSettings().setTiltGesturesEnabled(false);
        this.googleMap = googleMap;
        mapReadyListener.onMapReady(googleMap);

    }

    void callMapAsyncNow(){
        getMapAsync(this);
    }

    void setMyMapReadyListener(MyMapReadyListener mapReadyListener){
        this.mapReadyListener = mapReadyListener;
    }
    interface MyMapReadyListener{
        void onMapReady(GoogleMap googleMap);
    }



}
