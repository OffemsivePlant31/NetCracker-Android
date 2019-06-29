package com.nc.nc_android.screen.game;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nc.nc_android.MainActivity;
import com.nc.nc_android.R;
import com.nc.nc_android.dto.GameInstanceDto;
import com.nc.nc_android.localdata.UserData;
import com.nc.nc_android.retrofit.InstanceApi;
import com.nc.nc_android.retrofit.OrganizerApi;
import com.nc.nc_android.retrofit.RetrofitSingleton;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameInfoFragment extends Fragment {

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    private View root;

    TextView tvName;
    TextView tvDescription;
    TextView tvAuthor;
    TextView tvGamemode;
    TextView tvStartDate;
    Button btnLeave;
    RatingBar rbGameRating;
    Button btnClose;
    TextView tvGameState;

    InstanceApi instanceApi;
    OrganizerApi organizerApi;


    public GameInfoFragment() {
        instanceApi = RetrofitSingleton.getInstance(getContext()).getApi(InstanceApi.class);
        organizerApi = RetrofitSingleton.getInstance(getContext()).getApi(OrganizerApi.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_game_info, container, false);

        tvName = root.findViewById(R.id.tvName);
        tvDescription = root.findViewById(R.id.tvDescription);
        tvAuthor = root.findViewById(R.id.tvAuthor);
        tvGamemode = root.findViewById(R.id.tvGamemode);
        tvStartDate = root.findViewById(R.id.tvStartDate);
        btnLeave = root.findViewById(R.id.btnLeave);
        rbGameRating = root.findViewById(R.id.rbGameRating);
        btnClose = root.findViewById(R.id.btnClose);
        tvGameState = root.findViewById(R.id.tvGameState);

        btnLeave.setOnClickListener(v -> organizerApi.leaveGame(UserData.loadUserId(), UserData.loadGameId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                onResume();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        }));

        btnClose.setOnClickListener(v -> {
            UserData.saveGameId(-1);
            ((MainActivity)getActivity()).showHome();
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();


        instanceApi.gameInfo(UserData.loadGameId()).enqueue(new Callback<GameInstanceDto>() {
            @Override
            public void onResponse(Call<GameInstanceDto> call, Response<GameInstanceDto> response) {
                if(response.body() != null){

                    GameInstanceDto gameInstanceDto = response.body();

                    tvName.setText(gameInstanceDto.getGameTemplateDto().getName());
                    tvDescription.setText(gameInstanceDto.getGameTemplateDto().getDescription());
                    tvAuthor.setText(gameInstanceDto.getGameTemplateDto().getAuthor());
                    tvGamemode.setText(gameInstanceDto.getGameTemplateDto().getGameMode());
                    tvStartDate.setText(dateFormat.format(gameInstanceDto.getStartDate()));
                    rbGameRating.setRating(gameInstanceDto.getGameTemplateDto().getRating());

                    btnLeave.setEnabled(true);
                    btnLeave.setText("Завершить игру");
                    tvGameState.setVisibility(View.INVISIBLE);

                    if(gameInstanceDto.getStartDate().compareTo(new Date()) > 0){ // Еще не началась
                        btnLeave.setText("Отказаться от игры");
                    }

                    if(gameInstanceDto.getEndDate() != null){
                        btnLeave.setEnabled(false);
                        tvGameState.setVisibility(View.VISIBLE);
                    }

                }

            }
            @Override
            public void onFailure(Call<GameInstanceDto> call, Throwable t) {}
        });
    }
}
