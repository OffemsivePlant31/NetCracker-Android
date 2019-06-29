package com.nc.nc_android.screen.gamelist;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nc.nc_android.R;
import com.nc.nc_android.Utils;
import com.nc.nc_android.custom_view.ClearableEditText;
import com.nc.nc_android.custom_view.GameInfoView;
import com.nc.nc_android.dto.GameInstanceDto;
import com.nc.nc_android.dto.GameTemplateDto;
import com.nc.nc_android.localdata.UserData;
import com.nc.nc_android.retrofit.InstanceApi;
import com.nc.nc_android.retrofit.OrganizerApi;
import com.nc.nc_android.retrofit.RetrofitSingleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PublicGamesFragment extends Fragment {

    View root;

    RecyclerView recyclerView;
    BottomSheetBehavior bottomSheetBehavior;
    View bottomSheetPeek;

    FloatingActionButton fab;

    ClearableEditText cetName;
    ClearableEditText cetAuthor;

    GameListAdapter gameListAdapter = null;


    OrganizerApi organizerApi;

    List<GameInstanceDto> data;

    public PublicGamesFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_public_games, container, false);

        organizerApi = RetrofitSingleton.getInstance(getActivity().getApplicationContext()).getApi(OrganizerApi.class);

        bottomSheetBehavior = BottomSheetBehavior.from(root.findViewById(R.id.bottomSheet));
        bottomSheetPeek = root.findViewById(R.id.bottomSheetPeek);
        bottomSheetPeek.setOnClickListener(v -> {
            if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }else{
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        fab = root.findViewById(R.id.fab);

        fab.setOnClickListener(v -> {
            ArrayList<GameInstanceDto> filteredData;
            filteredData = new ArrayList<>(data.size());
            boolean ok;
            for(GameInstanceDto dto : data){
                ok = true;
                if(!(cetName.isEmpty())&& !(dto.getGameTemplateDto().getName().toUpperCase().contains(cetName.getText().toUpperCase()))){
                    ok = false;
                }
                if(!(cetAuthor.isEmpty())&& !(dto.getOverseer().toUpperCase().contains(cetAuthor.getText().toUpperCase()))){
                    ok = false;
                }

                if(ok) filteredData.add(dto);

            }

            filteredData.trimToSize();

            gameListAdapter.setData(filteredData);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        });

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                fab.animate().scaleX(slideOffset).scaleY(slideOffset).setDuration(0).start();
            }
        });



        cetName = root.findViewById(R.id.cetName);
        cetAuthor = root.findViewById(R.id.cetAuthor);

        recyclerView = root.findViewById(R.id.recycleView);


        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                llm.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }

    public void updateData() {
        InstanceApi api = RetrofitSingleton.getInstance(getContext()).getApi(InstanceApi.class);

        api.gameList(UserData.loadUserId()).enqueue(new Callback<List<GameInstanceDto>>() {
            @Override
            public void onResponse(Call<List<GameInstanceDto>> call, Response<List<GameInstanceDto>> response) {
                if((response.code() == 200)&&(response.body() != null)){

                    data = response.body();

                    Collections.sort(data, (o1, o2) -> (int) (o2.getGameTemplateDto().getRating() - o1.getGameTemplateDto().getRating()) * 100);
                    gameListAdapter = new GameListAdapter(data, "Организатор:");

                    gameListAdapter.setOnClickListener((holder, position) -> {

                        GameInstanceDto dto = data.get(position);

                        GameInfoView gameInfoView = new GameInfoView(getActivity(), dto.getGameTemplateDto());
                        gameInfoView.setSubscribed(dto.isSubscribed());
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setView(gameInfoView);

                        if(! dto.isSubscribed()){
                            builder.setPositiveButton("Подписаться", (dialog, which) ->{
                                organizerApi.subscribe(UserData.loadUserId(), dto.getId()).enqueue(Utils.emptyCallback());
                                Toast.makeText(getActivity(), "Вы подписались на игру", Toast.LENGTH_SHORT).show();
                                holder.tvSubscribed.setText("Вы подписаны");
                            });
                        }

                        builder.create().show();
                    });

                    recyclerView.setAdapter(gameListAdapter);

                }
            }

            @Override
            public void onFailure(Call<List<GameInstanceDto>> call, Throwable t) {

            }
        });

    }
}
