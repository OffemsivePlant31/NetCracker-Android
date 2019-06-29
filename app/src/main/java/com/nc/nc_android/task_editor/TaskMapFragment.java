package com.nc.nc_android.task_editor;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mikepenz.iconics.utils.Utils;
import com.nc.nc_android.MyApplication;
import com.nc.nc_android.R;
import com.nc.nc_android.dto.GameEditorTaskInfoDto;
import com.nc.nc_android.dto.GamePointDto;
import com.nc.nc_android.localdata.UserData;
import com.nc.nc_android.retrofit.EditorApi;
import com.nc.nc_android.retrofit.RetrofitSingleton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by fastn on 28.11.2017.
 */

public class TaskMapFragment extends android.support.v4.app.Fragment{

    View root;

    MapView map;
    GoogleMap googleMap;
    TextView tvLL;
    EditorApi api;
    List<GamePointDto> gamePoints;
    Button setPoint;
    Boolean commited;

    LatLng latLng;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(
                R.layout.fragment_task_map, container, false);

        map = root.findViewById(R.id.map_task);
        map.onCreate(savedInstanceState);
        latLng = new LatLng(getActivity().getIntent().getDoubleExtra("task_lat", 0), getActivity().getIntent().getDoubleExtra("task_lng", 0));
        tvLL = root.findViewById(R.id.tvLL);
        setPoint = root.findViewById(R.id.btn_set_point);

        commited = true;

        api = RetrofitSingleton.getInstance(MyApplication.getContext()).getApi(EditorApi.class);
        gamePoints = new ArrayList<>();

        map.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        map.getMapAsync(mMap -> {

            googleMap = mMap;

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }else{
                googleMap.setMyLocationEnabled(true);
            }

            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setCompassEnabled(false);
            googleMap.getUiSettings().setRotateGesturesEnabled(false);

            googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    latLng = cameraPosition.target;
                    tvLL.setText("Lat " + latLng.latitude + " Lng " + latLng.longitude);
                }
            });

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(latLng.latitude, latLng.longitude)).zoom(14).build();

            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

            getUserTasks();

        });

        setPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar mSnackbar = Snackbar.make(v, "Координаты изменены", Snackbar.LENGTH_SHORT)
                        .setAction("Отмена", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                commited = false;
                            }
                        });

                mSnackbar.show();

                mSnackbar.setCallback(new Snackbar.Callback() {

                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT && commited) {
                            UserData.saveString("task_lat", String.valueOf(latLng.latitude));
                            UserData.saveString("task_lng", String.valueOf(latLng.longitude));
                        }
                    }

                    @Override
                    public void onShown(Snackbar snackbar) {

                    }
                });
                getFragmentManager().popBackStack();
            }
        });

        return root;
    }

    private void getUserTasks(){
        long gameId = getActivity().getIntent().getLongExtra("gameId", 0);
        long taskId = getActivity().getIntent().getLongExtra("taskId", 0);
        api.getGameTasks(gameId).enqueue(new Callback<List<GameEditorTaskInfoDto>>() {
            @Override
            public void onResponse(Call<List<GameEditorTaskInfoDto>> call, Response<List<GameEditorTaskInfoDto>> response) {
                for (GameEditorTaskInfoDto task:response.body()) {
                    if (taskId != task.getTaskId()){
                        GamePointDto point = task.getPoint();
                        gamePoints.add(point);
                    }
                }
                createMarkers();
            }

            @Override
            public void onFailure(Call<List<GameEditorTaskInfoDto>> call, Throwable t) {

            }
        });
    }

    private void createMarkers(){
        for (GamePointDto point:gamePoints) {
            LatLng coords = new LatLng(point.getLatitude(), point.getLongitude());
            Marker location = googleMap.addMarker(new MarkerOptions().position(coords).title(point.getName()).snippet(point.getDescription()));
            location.showInfoWindow();
        }
    }
}
