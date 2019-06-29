package com.nc.nc_android.screen.game;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nc.nc_android.R;
import com.nc.nc_android.dto.TeamResultDto;
import com.nc.nc_android.screen.validation.ButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.List;

public class TeamListAdapter extends RecyclerView.Adapter<TeamListAdapter.TeamResultViewHolder>{

    List<TeamResultDto> data;

    public TeamListAdapter(List<TeamResultDto> data){
        this.data = data;
    }

    @Override
    public TeamResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_team_result, parent, false);
        TeamResultViewHolder holder = new TeamResultViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(TeamResultViewHolder holder, int position) {
        TeamResultDto dto = data.get(position);

        holder.tvName.setText(dto.getName());
        holder.tvPoints.setText(String.valueOf(dto.getCountDone()));
        if(dto.getTimeDone() != null){
            holder.tvLastTime.setText(new SimpleDateFormat("HH:mm").format(dto.getTimeDone()));
        }else{
            holder.tvLastTime.setText("-");
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public static class TeamResultViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvPoints;
        TextView tvLastTime;

        TeamResultViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvPoints = itemView.findViewById(R.id.tvPoints);
            tvLastTime = itemView.findViewById(R.id.tvLastTime);

        }
    }

}
