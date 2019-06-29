package com.nc.nc_android.screen.game;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nc.nc_android.R;
import com.nc.nc_android.dto.AsyncValidationDto;
import com.nc.nc_android.dto.TeamResultDto;
import com.nc.nc_android.localdata.UserData;
import com.nc.nc_android.retrofit.InstanceApi;
import com.nc.nc_android.retrofit.OrganizerApi;
import com.nc.nc_android.retrofit.RetrofitSingleton;
import com.nc.nc_android.screen.validation.RecycleAdapter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TeamRatingFragment extends Fragment {


    View root;

    RecyclerView recyclerView;
    TeamListAdapter teamListAdapter = null;

    CardView cardGameEnd;

    public TeamRatingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_team_rating, container, false);

        recyclerView = root.findViewById(R.id.recycleView);
        cardGameEnd = root.findViewById(R.id.cardGameEnd);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);

        InstanceApi api = RetrofitSingleton.getInstance(getContext()).getApi(InstanceApi.class);


        api.teamResults(UserData.loadGameId()).enqueue(new Callback<List<TeamResultDto>>() {
            @Override
            public void onResponse(Call<List<TeamResultDto>> call, Response<List<TeamResultDto>> response) {
                if((response.code() == 200)&&(response.body() != null)){

                    List<TeamResultDto> data = response.body();

                    Collections.sort(data, (o1, o2) -> o2.getCountDone() - o1.getCountDone());

                    teamListAdapter = new TeamListAdapter(data);
                    recyclerView.setAdapter(teamListAdapter);

                }
            }

            @Override
            public void onFailure(Call<List<TeamResultDto>> call, Throwable t) {

            }
        });








        return root;
    }

}
