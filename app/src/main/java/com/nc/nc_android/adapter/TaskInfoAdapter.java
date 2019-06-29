package com.nc.nc_android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nc.nc_android.MyApplication;
import com.nc.nc_android.R;
import com.nc.nc_android.localdata.UserData;
import com.nc.nc_android.pojo.TaskInfo;
import com.nc.nc_android.retrofit.EditorApi;
import com.nc.nc_android.retrofit.RetrofitSingleton;

import java.util.List;

/**
 * Created by fastn on 28.11.2017.
 */

public class TaskInfoAdapter extends RecyclerView.Adapter<TaskInfoAdapter.ViewHolder> {
    @Override
    public TaskInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View taskView = inflater.inflate(R.layout.task_info_layout, parent, false);

        TaskInfoAdapter.ViewHolder viewHolder = new TaskInfoAdapter.ViewHolder(taskView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        TaskInfo taskInfo = mTasks.get(position);

        TextView nameTextView = viewHolder.nameTextView;
        nameTextView.setText(taskInfo.getName());
        TextView descTextView = viewHolder.descTextView;
        descTextView.setText(taskInfo.getDescription());
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView descTextView;
        public Button deleteBtn;
        EditorApi api;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.task_name);
            descTextView = (TextView) itemView.findViewById(R.id.task_desc);
        }
    }

    private List<TaskInfo> mTasks;
    private Context mContext;

    public TaskInfoAdapter(Context context, List<TaskInfo> tasks) {
        mTasks = tasks;
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }
}
