package com.nc.nc_android;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.nc.nc_android.dto.GameEditorInfoDto;
import com.nc.nc_android.localdata.UserData;
import com.nc.nc_android.retrofit.EditorApi;
import com.nc.nc_android.retrofit.RetrofitSingleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditorActivity extends AppCompatActivity {

    Button btnTasks;
    Button btnPublish;
    EditText txtName;
    EditText txtDescription;
    EditText txtMaxPlayers;
    Spinner spinnerGameMode;
    Context appContext;
    Intent intent;
    long gameId;
    EditorApi api;
    GameEditorInfoDto game;
    TextView tvDebug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        appContext = this.getApplicationContext();

        btnPublish = findViewById(R.id.btnPublish);
        btnTasks = findViewById(R.id.btnTasks);
        txtName = findViewById(R.id.txtName);
        txtDescription = findViewById(R.id.txtDescription);
        txtMaxPlayers = findViewById(R.id.txtMaxPlayers);
        spinnerGameMode = findViewById(R.id.spinnerGameMode);
        tvDebug = findViewById(R.id.tvDebugEditor);
        intent = getIntent();
        gameId = intent.getLongExtra("gameId", 0);
        tvDebug.setText(String.valueOf(gameId));
        api = RetrofitSingleton.getInstance(appContext).getApi(EditorApi.class);
        if (gameId != 0 && gameId != -1){
            api.getInfo(gameId, UserData.loadUserId()).enqueue(new Callback<GameEditorInfoDto>() {
                @Override
                public void onResponse(Call<GameEditorInfoDto> call, Response<GameEditorInfoDto> response) {
                    game = response.body();
                    txtName.setText(game.getName());
                    txtDescription.setText(game.getDescription());
                    txtMaxPlayers.setText(String.valueOf(game.getMaxPlayers()));
                }

                @Override
                public void onFailure(Call<GameEditorInfoDto> call, Throwable t) {
                    txtName.setText("Failure");
                }
            });
        }


        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.setName(txtName.getText().toString());
                game.setDescription(txtDescription.getText().toString());
                Integer maxPlayers = 0;
                String max = txtMaxPlayers.getText().toString();
                if (max != null){
                    maxPlayers = Integer.parseInt(max);
                }
                game.setMaxPlayers(maxPlayers);
                api.publishGame(UserData.loadUserId(), gameId, game).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }
        });

        btnTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(appContext, TasksManagerEditorActivity.class);
                intent.putExtra("gameId", gameId);
                startActivity(intent);
            }
        });
    }
}
