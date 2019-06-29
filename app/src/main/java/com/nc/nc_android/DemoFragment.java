package com.nc.nc_android;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nc.nc_android.pojo.GamePoint;
import com.nc.nc_android.service.GamePointService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LOCATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class DemoFragment extends Fragment implements OnMapReadyCallback {


    View root;

    private String CURRENT_PROVIDER = LocationManager.GPS_PROVIDER;

    Button btnMe;
    Button btnPoint;
    Button btnLoad;
    Button btnClear;

    TextView tvDistance;
    TextView tvOk;

    EditText edName;

    Context activity;


    SupportMapFragment mapFragment;
    GoogleMap map;


    Marker pointMarker;
    Marker myMarker;

    List<GamePoint> points;

    private LocationManager locationManager;

    GamePointService gamePointService;

    public DemoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        gamePointService = new GamePointService();

        activity = getContext();

        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);


        root = inflater.inflate(R.layout.fragment_demo, container, false);
        btnMe = (Button) root.findViewById(R.id.btnMe);
        btnPoint = (Button) root.findViewById(R.id.btnPoint);
        btnLoad = (Button) root.findViewById(R.id.btnLoad);
        btnClear = (Button) root.findViewById(R.id.btnClear);

        tvDistance = (TextView) root.findViewById(R.id.tvDistance);
        tvOk = (TextView) root.findViewById(R.id.tvOk);

        edName = (EditText) root.findViewById(R.id.edName);


        //----------------------------------------------------------
        btnPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Location loc = null;
                try {
                    loc = getLastKnownLocation(locationManager);

                } catch (SecurityException se) {
                    return;
                }

                GamePoint gp = new GamePoint();
                gp.setName(edName.getText().toString());
                gp.setLatitude(loc.getLatitude());
                gp.setLongitude(loc.getLongitude());

                gamePointService.saveAsync(gp).enqueue(new Callback<List<GamePoint>>() {
                    @Override
                    public void onResponse(Call<List<GamePoint>> call, Response<List<GamePoint>> response) {
                        updatePointMarkers(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<GamePoint>> call, Throwable t) {
                    }
                });

            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gamePointService.clear().enqueue(new Callback<List<GamePoint>>() {
                    @Override
                    public void onResponse(Call<List<GamePoint>> call, Response<List<GamePoint>> response) {
                        updatePointMarkers(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<GamePoint>> call, Throwable t) {
                    }
                });
            }
        });

        btnMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Location location = getLastKnownLocation(locationManager);
                    updateMyMarker(location);

                } catch (SecurityException e) {

                }
            }
        });

        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gamePointService.findAllAsync().enqueue(new Callback<List<GamePoint>>() {
                    @Override
                    public void onResponse(Call<List<GamePoint>> call, Response<List<GamePoint>> response) {
                        updatePointMarkers(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<GamePoint>> call, Throwable t) {
                    }
                });
            }
        });

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        btnLoad.callOnClick();
        //-------------------------------------------------------------

        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    void updatePointMarkers(List<GamePoint> responce) {
        if (points != null) {
            removePointMarkers();
            points.clear();
        }
        points = responce;
        for (GamePoint gp : points) {
            if (gp.getMarker() == null) {
                Marker newMarker = map.addMarker(new MarkerOptions()
                        .position(gp.getLatLng())
                        .title(gp.getName())
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_target)));
                gp.setMarker(newMarker);
            } else {
                gp.getMarker().setPosition(gp.getLatLng());
            }
        }
    }

    void removePointMarkers() {
        for (GamePoint gp : points) {
            gp.getMarker().remove();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

//        try {
//            locationManager.requestLocationUpdates(CURRENT_PROVIDER, 1000, 1, locationListener);
//        } catch (SecurityException ex) {
//
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        locationManager.removeUpdates(locationListener);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        try {
            //map.setMyLocationEnabled(true);
        } catch (SecurityException ex) {

        }

    }

    void updateMyMarker(Location location) {
        LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
        if (myMarker == null) {
            myMarker = map.addMarker(new MarkerOptions()
                    .position(point)
                    .title("я")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_minion)));
        } else {
            myMarker.setPosition(point);
        }
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(point)
                .zoom(18)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);

        boolean pointNear = false;
        if (points != null) {
            for (GamePoint gp : points) {

                float dist = location.distanceTo(gp.getLocation());

                if (dist < 5) {
                    tvDistance.setText(gp.getName());
                    pointNear = true;
                }
            }
        }

        if (!pointNear) tvDistance.setText("-");
    }


    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            updateMyMarker(location);
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

    Location toLocation(LatLng latLng) {
        Location temp = new Location(CURRENT_PROVIDER);
        temp.setLatitude(latLng.latitude);
        temp.setLongitude(latLng.longitude);
        return temp;
    }

    private Location getLastKnownLocation(LocationManager locationManager) {
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

}
