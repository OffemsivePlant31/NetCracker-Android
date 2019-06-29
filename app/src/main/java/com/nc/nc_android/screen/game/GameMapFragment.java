package com.nc.nc_android.screen.game;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.nc.nc_android.R;
import com.nc.nc_android.dto.GamePointDto;
import com.nc.nc_android.localdata.UserData;
import com.nc.nc_android.retrofit.OrganizerApi;
import com.nc.nc_android.retrofit.RetrofitSingleton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameMapFragment extends Fragment {

    MapView mapView;
    GoogleMap googleMap;


    public GameMapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_game_map, container, false);

        mapView = root.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(mMap -> {

            googleMap = mMap;

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }else{
                //googleMap.setMyLocationEnabled(true);
            }


            OrganizerApi api = RetrofitSingleton.getInstance(getContext()).getApi(OrganizerApi.class);

            if(UserData.loadBoolean("isOverseer")){
                api.getGamePoints(UserData.loadGameId()).enqueue(new Callback<List<GamePointDto>>() {
                    @Override
                    public void onResponse(Call<List<GamePointDto>> call, Response<List<GamePointDto>> response) {
                        if(response.body() == null) return;

                        PolylineOptions rectOptions = new PolylineOptions()
                                .color(Color.BLUE)
                                .width(10)
                                .geodesic(true);

                        Marker lastMarker = null;
                        for(GamePointDto gamePointDto : response.body()){
                            rectOptions.add(new LatLng(gamePointDto.getLatitude(), gamePointDto.getLongitude()));

                            lastMarker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(gamePointDto.getLatitude(), gamePointDto.getLongitude()))
                                    .title(gamePointDto.getName())
                                    .snippet("snipped"));
                            //.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_target)));

                        }

                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(lastMarker.getPosition())
                                .zoom(14)
                                .build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000, null);

                        Polyline polyline = mMap.addPolyline(rectOptions);
                    }

                    @Override
                    public void onFailure(Call<List<GamePointDto>> call, Throwable t) {

                    }
                });
            }


        });

        return root;

    }

}
