package com.nc.nc_android.screen.validation;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nc.nc_android.MyApplication;
import com.nc.nc_android.R;
import com.nc.nc_android.Utils;
import com.nc.nc_android.dto.AsyncValidationDto;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ValidationViewHolder>{


    List<AsyncValidationDto> data;
    ButtonClickListener clickListener;

    public void remove(int position){
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public ValidationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_validation_card, parent, false);
        ValidationViewHolder holder = new ValidationViewHolder(v, clickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ValidationViewHolder holder, int position) {
        AsyncValidationDto dto = data.get(position);

        Picasso.with(MyApplication.getContext()).load(Utils.getImageUrl(dto.getPhoto())).into(holder.ivPhoto);
        holder.tvDescription.setText(dto.getDescription());

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    RecycleAdapter(List<AsyncValidationDto> data, ButtonClickListener clickListener){
        this.clickListener = clickListener;
        this.data = data;
    }



    public static class ValidationViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        TextView tvDescription;
        Button btnYes;
        Button btnNo;
        ImageView ivPhoto;
        ValidationViewHolder(View itemView, ButtonClickListener listener) {
            super(itemView);

            card = itemView.findViewById(R.id.card);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            btnYes = itemView.findViewById(R.id.btnYes);
            btnNo = itemView.findViewById(R.id.btnNo);
            ivPhoto = itemView.findViewById(R.id.ivPhoto);

            btnYes.setOnClickListener(e->{
                listener.handle(2, getAdapterPosition());
            });

            btnNo.setOnClickListener(e->{
                listener.handle(1, getAdapterPosition());
            });

        }
    }


}
