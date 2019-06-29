package com.nc.nc_android.screen.gamelist;


import android.content.DialogInterface;
import android.os.Bundle;
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
import com.nc.nc_android.custom_view.GameInfoView;
import com.nc.nc_android.dto.GameInstanceDto;
import com.nc.nc_android.localdata.UserData;
import com.nc.nc_android.retrofit.InstanceApi;
import com.nc.nc_android.retrofit.OrganizerApi;
import com.nc.nc_android.retrofit.RetrofitSingleton;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PrivateGamesFragment extends Fragment {

    View root;

    RecyclerView recyclerView;
    GameListAdapter gameListAdapter = null;
    OrganizerApi organizerApi;

    public PrivateGamesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_private_games, container, false);

        organizerApi = RetrofitSingleton.getInstance(getActivity().getApplicationContext()).getApi(OrganizerApi.class);

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
        api.privateGameList(UserData.loadUserId()).enqueue(new Callback<List<GameInstanceDto>>() {
            @Override
            public void onResponse(Call<List<GameInstanceDto>> call, Response<List<GameInstanceDto>> response) {
                if((response.code() == 200)&&(response.body() != null)){

                    List<GameInstanceDto> data = response.body();

                    Collections.sort(data, (o1, o2) -> (int) (o2.getGameTemplateDto().getRating() - o1.getGameTemplateDto().getRating()) * 100);

                    gameListAdapter = new GameListAdapter(data, "Пригласил:");

                    gameListAdapter.setOnClickListener((holder, position) -> {

                        GameInstanceDto dto = data.get(position);

                        GameInfoView gameInfoView = new GameInfoView(getActivity(), dto.getGameTemplateDto());
                        gameInfoView.setSubscribed(dto.isSubscribed());
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setView(gameInfoView);

                        builder.setPositiveButton("Принять", (dialog, which) ->{
                            organizerApi.sendInviteResponse(dto.getId(), true).enqueue(Utils.emptyCallback());
                            data.remove(position);
                            gameListAdapter.notifyItemRemoved(position);
                            Toast.makeText(getActivity(), "Вы подписались на игру", Toast.LENGTH_SHORT).show();
                        });

                        builder.setNegativeButton("Отказаться", (dialog, which) -> {
                            organizerApi.sendInviteResponse(dto.getId(), false).enqueue(Utils.emptyCallback());
                            data.remove(position);
                            gameListAdapter.notifyItemRemoved(position);
                            Toast.makeText(getActivity(), "Вы отклонили приглашение", Toast.LENGTH_SHORT).show();
                        });


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
