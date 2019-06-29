package com.nc.nc_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nc.nc_android.dto.GameEditorTaskInfoDto;
import com.nc.nc_android.localdata.UserData;
import com.nc.nc_android.retrofit.EditorApi;
import com.nc.nc_android.retrofit.RetrofitSingleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskEditorActivity extends AppCompatActivity {

    Button btnPoint;
    EditText txtName;
    EditText txtDescription;
    EditText txtFindCondition;
    EditText txtCheckCondition;
    EditText txtCode;
    TextView tvDebug;
    Button btnSave;
    Intent intent;
    long taskId;
    GameEditorTaskInfoDto task;
    EditorApi api;
    long pointId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_editor);

        btnPoint = findViewById(R.id.btnSelectPoint);
        txtName = findViewById(R.id.txtTaskName);
        txtDescription = findViewById(R.id.txtTaskDescription);
        txtFindCondition = findViewById(R.id.txtFindCondition);
        txtCheckCondition = findViewById(R.id.txtCheckCondition);
        txtCode = findViewById(R.id.txtCheckCode);
        tvDebug = findViewById(R.id.tvDebugTask);
        btnSave = findViewById(R.id.btnSaveTask);
        intent = getIntent();
        taskId = intent.getLongExtra("taskId", 0);
        tvDebug.setText(String.valueOf(taskId));
        api = RetrofitSingleton.getInstance(MyApplication.getContext()).getApi(EditorApi.class);
        if (taskId != 0){
            api.getGameTasksById(taskId).enqueue(new Callback<GameEditorTaskInfoDto>() {
                @Override
                public void onResponse(Call<GameEditorTaskInfoDto> call, Response<GameEditorTaskInfoDto> response) {
                    try {
                        txtName.setText(response.body().getName());
                        txtDescription.setText(response.body().getDescription());
                        txtFindCondition.setText(response.body().getFindCondition());
                        txtCheckCondition.setText(response.body().getCheckConditionDescription());
                        txtCode.setText(response.body().getCheckConditionCode());
                        pointId = response.body().getPoint().getId();
                    } catch (Exception e){

                    }
                }

                @Override
                public void onFailure(Call<GameEditorTaskInfoDto> call, Throwable t) {

                }
            });

            txtName.setText(String.valueOf(taskId));
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                api.getGameTasksById(taskId).enqueue(new Callback<GameEditorTaskInfoDto>() {
                    @Override
                    public void onResponse(Call<GameEditorTaskInfoDto> call, Response<GameEditorTaskInfoDto> response) {
                        task = response.body();
                        task.setCheckConditionCode(txtCode.getText().toString());
                        task.setCheckConditionDescription(txtCheckCondition.getText().toString());
                        task.setDescription(txtDescription.getText().toString());
                        task.setFindCondition(txtFindCondition.getText().toString());
                        task.setName(txtName.getText().toString());
                        api.updateTaskInfo(task).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {

                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<GameEditorTaskInfoDto> call, Throwable t) {

                    }
                });
            }
        });

        btnPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pointId != 0){
                    Intent intent = new Intent(MyApplication.getContext(), PointEditorActivity.class);
                    intent.putExtra("pointId", pointId);
                    startActivity(intent);
                } else {
                    api.createNewPointInTask(taskId, UserData.loadUserId()).enqueue(new Callback<Long>() {
                        @Override
                        public void onResponse(Call<Long> call, Response<Long> response) {
                            Intent intent = new Intent(MyApplication.getContext(), PointEditorActivity.class);
                            intent.putExtra("pointId", response.body());
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<Long> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }
}
