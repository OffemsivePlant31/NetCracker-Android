package com.nc.nc_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.nc.nc_android.dto.GameEditorInfoDto;
import com.nc.nc_android.dto.GameEditorTaskInfoDto;
import com.nc.nc_android.retrofit.EditorApi;
import com.nc.nc_android.retrofit.RetrofitSingleton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TasksManagerEditorActivity extends AppCompatActivity {

    ListView lvTasks;
    Button btnNewTask;
    Button btnEditTask;
    TextView tvTaskSelection;
    TextView tvDebug;
    Button btnReorder;
    Intent intent;
    long gameId;
    int selected;

    ArrayAdapter<Long> myAdapter;
    List<Long> userTasks;
    List<GameEditorTaskInfoDto> userTasksData;
    EditorApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_manager_editor);

        lvTasks = findViewById(R.id.lvGameTasks);
        btnEditTask = findViewById(R.id.btnEditSelectedTask);
        btnNewTask = findViewById(R.id.btnNewTask);
        tvTaskSelection = findViewById(R.id.tvTaskSelection);
        tvDebug = findViewById(R.id.tvDebugManager);
        btnReorder = findViewById(R.id.btnReorder);
        intent = getIntent();

        selected = -1;
        gameId = intent.getLongExtra("gameId", 0);
        api = RetrofitSingleton.getInstance(MyApplication.getContext()).getApi(EditorApi.class);

        api.getGameTasks(gameId).enqueue(new Callback<List<GameEditorTaskInfoDto>>() {
            @Override
            public void onResponse(Call<List<GameEditorTaskInfoDto>> call, Response<List<GameEditorTaskInfoDto>> response) {
                userTasks = new ArrayList<>();
                userTasksData = new ArrayList<>();
                for (GameEditorTaskInfoDto task:response.body()) {
                    userTasks.add(task.getTaskId());
                    userTasksData.add(task);
                }
                myAdapter = new ArrayAdapter<Long>(MyApplication.getContext(), android.R.layout.simple_list_item_1, userTasks);
                lvTasks.setAdapter(myAdapter);
            }

            @Override
            public void onFailure(Call<List<GameEditorTaskInfoDto>> call, Throwable t) {

            }
        });

        lvTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(selected == -1){
                    selected = position;
                    view.setSelected(true);
                    tvTaskSelection.setText(String.valueOf(userTasks.get(selected)));
                }
                else if(selected == position){
                    tvTaskSelection.setText("Nothing selected");
                    selected = -1;
                    view.setSelected(false);
                }
                else{
                    Long temp = userTasks.get(selected);
                    GameEditorTaskInfoDto tempGame = userTasksData.get(selected);
                    userTasks.set(selected, userTasks.get(position));
                    userTasksData.set(selected, userTasksData.get(position));
                    userTasks.set(position, temp);
                    userTasksData.set(position, tempGame);
                    myAdapter.notifyDataSetChanged();
                    selected = -1;
                    tvTaskSelection.setText("Nothing selected");
                }
            }
        });

        btnReorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                api.reorderTasks(gameId, userTasks).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }
        });

        btnNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                api.createNewTaskInGame(gameId).enqueue(new Callback<Long>() {
                    @Override
                    public void onResponse(Call<Long> call, Response<Long> response) {
                        Intent intent = new Intent(MyApplication.getContext(), TaskEditorActivity.class);
                        intent.putExtra("taskId", response.body());
                        startActivity(intent);
                        tvDebug.setText(response.toString());
                    }

                    @Override
                    public void onFailure(Call<Long> call, Throwable t) {

                    }
                });
            }
        });

        btnEditTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected != -1) {
                    Intent intent = new Intent(MyApplication.getContext(), TaskEditorActivity.class);
                    intent.putExtra("taskId", userTasksData.get(selected).getTaskId());
                    startActivity(intent);
                }
            }
        });
    }
}
