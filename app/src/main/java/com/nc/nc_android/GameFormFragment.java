package com.nc.nc_android;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.nc.nc_android.dto.GameEditorInfoDto;
import com.nc.nc_android.dto.GameModeDto;
import com.nc.nc_android.localdata.UserData;
import com.nc.nc_android.retrofit.EditorApi;
import com.nc.nc_android.retrofit.RetrofitSingleton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameFormFragment extends Fragment {

    View root;

    GameEditorInfoDto currentGame;
    long gameId;

    Button commit;
    EditText name;
    EditText desc;
    Spinner mode;
    EditorApi api;

    List<GameModeDto> gameModeDtoList;

    public GameFormFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root = inflater.inflate(R.layout.fragment_game_form, container, false);

        commit = root.findViewById(R.id.btn_commit_editor);
        name = root.findViewById(R.id.input_game_name);
        desc = root.findViewById(R.id.input_game_desc);
        mode = root.findViewById(R.id.spinner_game_mode);

        gameId = getActivity().getIntent().getLongExtra("gameId", 0);

        api = RetrofitSingleton.getInstance(MyApplication.getContext()).getApi(EditorApi.class);

        initializeGameInfo();

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString() != null && desc.getText().toString() != null) {
                    currentGame.setName(name.getText().toString());
                    currentGame.setDescription(desc.getText().toString());
                    currentGame.setMode(gameModeDtoList.get(mode.getSelectedItemPosition()));
                    api.publishGame(gameId, UserData.loadUserId(), currentGame).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            String strOrder = getActivity().getIntent().getStringExtra("task_order");
                            if (strOrder != null){
                                String[] parts = strOrder.split(" ");
                                List<Long> order = new ArrayList<>();
                                for (String str:parts) {
                                    try {
                                        order.add(Long.parseLong(str));
                                    } catch (Exception e) {

                                    }
                                }
                                api.reorderTasks(gameId, order).enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {

                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {

                                    }
                                });

                            }
                            getFragmentManager().popBackStack();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {

                        }
                    });
                }
            }
        });

        return root;
    }

    private void initializeGameInfo(){
        api.getInfo(gameId, UserData.loadUserId()).enqueue(new Callback<GameEditorInfoDto>() {
            @Override
            public void onResponse(Call<GameEditorInfoDto> call, Response<GameEditorInfoDto> response) {
                currentGame = response.body();
                desc.setText(response.body().getDescription());
                name.setText(response.body().getName());
                initializeSpinner();
            }

            @Override
            public void onFailure(Call<GameEditorInfoDto> call, Throwable t) {

            }
        });
    }

    private void initializeSpinner(){
        api.getModes().enqueue(new Callback<List<GameModeDto>>() {
            @Override
            public void onResponse(Call<List<GameModeDto>> call, Response<List<GameModeDto>> response) {
                gameModeDtoList = response.body();
                List<String> modes = new ArrayList<>();
                String currentMode = currentGame.getMode().getName();
                int currModePos = 0;
                for (GameModeDto gameMode:response.body()) {
                    modes.add(gameMode.getName());
                    if (gameMode.getName().equals(currentMode)){
                        currModePos = modes.size() - 1;
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MyApplication.getContext(), android.R.layout.simple_spinner_item, modes);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mode.setAdapter(adapter);
                mode.setSelection(currModePos);
            }

            @Override
            public void onFailure(Call<List<GameModeDto>> call, Throwable t) {

            }
        });
    }
}
