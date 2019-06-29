package com.nc.nc_android.task_editor;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.nc.nc_android.MyApplication;
import com.nc.nc_android.R;
import com.nc.nc_android.dto.GameEditorTaskInfoDto;
import com.nc.nc_android.dto.GameModeDto;
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

public class TaskInfoFragment extends android.support.v4.app.Fragment {

    View root;

    TextView nameTxt;
    TextView descTxt;
    TextView findTxt;
    TextView checkTxt;
    TextView codeTxt;
    Button commitBtn;
    Button getPoint;
    Button deleteBtn;
    TextView latLng;
    Spinner spinner;

    GameEditorTaskInfoDto currentTask;

    EditorApi api;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(
                R.layout.fragment_task_info, container, false);

        nameTxt = (TextView) root.findViewById(R.id.task_name_txt);
        descTxt = (TextView) root.findViewById(R.id.task_desc_txt);
        findTxt = (TextView) root.findViewById(R.id.task_find_txt);
        checkTxt = (TextView) root.findViewById(R.id.task_check_txt);
        codeTxt = (TextView) root.findViewById(R.id.task_code_txt);
        commitBtn = (Button) root.findViewById(R.id.commit_task_changes);
        getPoint = (Button) root.findViewById(R.id.btn_get_point);
        deleteBtn = (Button) root.findViewById(R.id.delete_task);
        spinner = (Spinner) root.findViewById(R.id.spinner_type);

        commitBtn.setActivated(false);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        api = RetrofitSingleton.getInstance(MyApplication.getContext()).getApi(EditorApi.class);

        long taskId = getActivity().getIntent().getLongExtra("taskId", 0);
        api.getGameTasksById(taskId).enqueue(new Callback<GameEditorTaskInfoDto>() {
            @Override
            public void onResponse(Call<GameEditorTaskInfoDto> call, Response<GameEditorTaskInfoDto> response) {
                currentTask = response.body();
                nameTxt.setText(response.body().getPoint().getName());
                descTxt.setText(response.body().getPoint().getDescription());
                findTxt.setText(response.body().getFindCondition());
                checkTxt.setText(response.body().getCheckConditionDescription());
                codeTxt.setText(response.body().getCheckConditionCode());
                double lat = response.body().getPoint().getLatitude();
                double lng = response.body().getPoint().getLongitude();
                getActivity().getIntent().putExtra("task_lat", lat);
                getActivity().getIntent().putExtra("task_lng", lng);
                commitBtn.setActivated(true);
            }

            @Override
            public void onFailure(Call<GameEditorTaskInfoDto> call, Throwable t) {

            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                api.deleteTask(UserData.loadUserId(), taskId).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        getFragmentManager().popBackStack();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }
        });

        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTask.getPoint().setName(nameTxt.getText().toString());
                currentTask.getPoint().setDescription(descTxt.getText().toString());
                currentTask.setFindCondition(findTxt.getText().toString());
                currentTask.setCheckConditionDescription(checkTxt.getText().toString());
                currentTask.setCheckConditionCode(codeTxt.getText().toString());
                double lat = getActivity().getIntent().getDoubleExtra("task_lat", currentTask.getPoint().getLatitude());
                double lng = getActivity().getIntent().getDoubleExtra("task_lng", currentTask.getPoint().getLongitude());
                try {
                    double new_lat = Double.parseDouble(UserData.loadString("task_lat"));
                    double new_lng = Double.parseDouble(UserData.loadString("task_lng"));
                    lat = new_lat;
                    lng = new_lng;
                } catch (Exception e) {

                }

                currentTask.getPoint().setLatitude((float) lat);
                currentTask.getPoint().setLongitude((float)lng);
                api.updateTaskInfo(currentTask).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        getFragmentManager().popBackStack();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }
        });

        getPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, new TaskMapFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return root;
    }
}
