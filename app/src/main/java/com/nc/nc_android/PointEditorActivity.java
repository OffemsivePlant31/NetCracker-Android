package com.nc.nc_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nc.nc_android.dto.GamePointDto;
import com.nc.nc_android.retrofit.EditorApi;
import com.nc.nc_android.retrofit.RetrofitSingleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PointEditorActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener {

    MapFragment map;
    Marker marker;
    Intent intent;
    long pointId;
    GamePointDto point;
    TextView tvTest;

    EditorApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_editor);

        intent = getIntent();
        pointId = intent.getLongExtra("pointId", 0);
        tvTest = findViewById(R.id.tvTestPoint);
        tvTest.setText(String.valueOf(pointId));


        api = RetrofitSingleton.getInstance(MyApplication.getContext()).getApi(EditorApi.class);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapPointEditor);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        if (pointId != 0){
            api.getPoint(pointId).enqueue(new Callback<GamePointDto>() {
                @Override
                public void onResponse(Call<GamePointDto> call, Response<GamePointDto> response) {
                    point = response.body();
                    marker = gMap.addMarker(new MarkerOptions()
                            .position(new LatLng(response.body().getLatitude(), response.body().getLongitude()))
                            .title(response.body().getName())
                            .draggable(true));
                }

                @Override
                public void onFailure(Call<GamePointDto> call, Throwable t) {

                }
            });
        } else {
            marker = gMap.addMarker(new MarkerOptions()
                    .position(new LatLng(59, 30))
                    .title("Spb")
                    .draggable(true));
            marker.setTag(0);
            point = new GamePointDto();
            point.setLatitude((float)marker.getPosition().latitude);
            point.setLongitude((float)marker.getPosition().longitude);
            point.setName(marker.getTitle());
        }
        gMap.setOnMarkerClickListener(this);
        gMap.setOnMarkerDragListener(this);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        double lat = marker.getPosition().latitude;
        double lng = marker.getPosition().longitude;
        Toast.makeText(this, "Latitude is " + lat + "Longitude is " + lng, Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        this.marker.setPosition(marker.getPosition());
        point.setLongitude((float)marker.getPosition().longitude);
        point.setLatitude((float)marker.getPosition().latitude);
    }

    @Override
    public void onBackPressed(){
        if (pointId != 0) {
            api.changePoint(pointId, point).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        } else {
            api.createPoint(point).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        }
        super.onBackPressed();
    }
}
