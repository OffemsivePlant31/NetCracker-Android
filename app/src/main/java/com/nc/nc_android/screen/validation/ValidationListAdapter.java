package com.nc.nc_android.screen.validation;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nc.nc_android.R;
import com.nc.nc_android.dto.AsyncValidationDto;
import com.nc.nc_android.screen.game.TeamListAdapter;

import java.util.List;


public class ValidationListAdapter extends RecyclerView.Adapter<ValidationListAdapter.ValidationViewHolder>{

    ValidationClickListener clickListener;

    List<AsyncValidationDto> data;

    ValidationListAdapter(List<AsyncValidationDto> data, ValidationClickListener clickListener){
        this.data = data;
        this.clickListener = clickListener;
    }


    @Override
    public ValidationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_validation_list_item, parent, false);
        ValidationListAdapter.ValidationViewHolder holder = new ValidationListAdapter.ValidationViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ValidationViewHolder holder, int position) {
        AsyncValidationDto dto = data.get(position);

        holder.tvFrom.setText(dto.getFrom());
        holder.tvPointName.setText(dto.getPointName());
        holder.tvDescription.setText(dto.getDescription());

        holder.root.setOnClickListener(v -> {
            if(clickListener != null){
                clickListener.handle(holder, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ValidationViewHolder extends RecyclerView.ViewHolder {


        View root;
        TextView tvFrom;
        TextView tvPointName;
        TextView tvDescription;



        ValidationViewHolder(View itemView) {
            super(itemView);

            root = itemView;
            tvFrom = itemView.findViewById(R.id.tvFrom);
            tvPointName = itemView.findViewById(R.id.tvPointName);
            tvDescription = itemView.findViewById(R.id.tvDescription);

        }
    }

    public interface ValidationClickListener {
        void handle(ValidationListAdapter.ValidationViewHolder holder, int position);
    }

}
