package com.nc.nc_android;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.nc.nc_android.adapter.TaskInfoAdapter;
import com.nc.nc_android.behavior.RecyclerItemSingleTapListener;
import com.nc.nc_android.dto.GameEditorInfoDto;
import com.nc.nc_android.dto.GameEditorTaskInfoDto;
import com.nc.nc_android.dto.GameModeDto;
import com.nc.nc_android.dto.GamePointDto;
import com.nc.nc_android.localdata.UserData;
import com.nc.nc_android.pojo.TaskInfo;
import com.nc.nc_android.retrofit.EditorApi;
import com.nc.nc_android.retrofit.RetrofitSingleton;
import com.nc.nc_android.task_editor.TaskInfoFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameEditorFragment extends Fragment {

    public GameEditorFragment() {
    }

    View root;
    EditorApi api;
    Long gameId;
    EditText txtName;
    EditText txtMax;
    EditText txtDesc;
    Spinner spnMode;
    RecyclerView rvTasks;
    Button commit;
    FloatingActionButton fbaNew;
    List<TaskInfo> taskInfoList;
    GameEditorInfoDto currentGame;
    TaskInfoAdapter adapter;
    int selectedItem;
    List<Long> order;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root = inflater.inflate(R.layout.fragment_game_editor, container, false);

        api = RetrofitSingleton.getInstance(MyApplication.getContext()).getApi(EditorApi.class);

        gameId = getActivity().getIntent().getLongExtra("gameId", 0);

        rvTasks = (RecyclerView) root.findViewById(R.id.rv_game_tasks);
        fbaNew = (FloatingActionButton) root.findViewById(R.id.new_task_fba);

        selectedItem = -1;

        getTasksFromServer();

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(MyApplication.getContext(), DividerItemDecoration.VERTICAL);
        rvTasks.addItemDecoration(itemDecoration);
        rvTasks.setLayoutManager(new LinearLayoutManager(MyApplication.getContext()));

        rvTasks.addOnItemTouchListener(new RecyclerItemSingleTapListener(MyApplication.getContext(), new RecyclerItemSingleTapListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (selectedItem == -1) {
                    selectedItem = position;
                    view.setBackgroundColor(Color.MAGENTA);
                } else if (selectedItem == position) {
                    getActivity().getIntent().putExtra("taskId", taskInfoList.get(position).getId());
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_frame, new TaskInfoFragment())
                            .addToBackStack(null)
                            .commit();
                } else {
                    rvTasks.getLayoutManager().findViewByPosition(selectedItem).setBackgroundColor(Color.WHITE);
                    TaskInfo tempTask = taskInfoList.get(position);
                    taskInfoList.set(position, taskInfoList.get(selectedItem));
                    taskInfoList.set(selectedItem, tempTask);
                    Collections.swap(order, selectedItem, position);
                    String strOrder = "";
                    for (Long pos:order) {
                        strOrder.concat(pos + " ");
                    }
                    getActivity().getIntent().putExtra("task_order", strOrder);
                    adapter.notifyDataSetChanged();
                    selectedItem = -1;
                }
            }
        }));

        fbaNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                api.createNewTaskInGame(gameId).enqueue(new Callback<Long>() {
                    @Override
                    public void onResponse(Call<Long> call, Response<Long> response) {
                        getActivity().getIntent().putExtra("taskId", response.body());
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content_frame, new TaskInfoFragment())
                                .addToBackStack(null)
                                .commit();
                    }

                    @Override
                    public void onFailure(Call<Long> call, Throwable t) {

                    }
                });
            }
        });
/*
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentGame.setMaxPlayers(Integer.parseInt(txtMax.getText().toString()));
                currentGame.setDescription(txtDesc.getText().toString());
                currentGame.setName(txtName.getText().toString());
                api.publishGame(gameId,UserData.loadUserId(), currentGame).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }
        });
        */

        return root;
    }

    private void getTasksFromServer(){
        List<TaskInfo> tasks = new ArrayList<>();
        order = new ArrayList<Long>();
        api.getGameTasks(gameId).enqueue(new Callback<List<GameEditorTaskInfoDto>>() {
            @Override
            public void onResponse(Call<List<GameEditorTaskInfoDto>> call, Response<List<GameEditorTaskInfoDto>> response) {
                for (GameEditorTaskInfoDto task:response.body()) {
                    GamePointDto point = task.getPoint();
                    TaskInfo info = new TaskInfo(task.getTaskId(), point.getName(), point.getDescription(), task.getFindCondition(), task.getCheckConditionDescription(), task.getCheckConditionCode(), 0, 0);
                    tasks.add(info);
                    order.add(info.getId());
                }
                initializeAdapter(tasks);
            }

            @Override
            public void onFailure(Call<List<GameEditorTaskInfoDto>> call, Throwable t) {

            }
        });
    }

    private void initializeAdapter(List<TaskInfo> info){
        taskInfoList = info;
        adapter = new TaskInfoAdapter(MyApplication.getContext(), taskInfoList);
        rvTasks.setAdapter(adapter);
    }
}
