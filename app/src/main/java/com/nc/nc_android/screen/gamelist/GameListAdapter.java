package com.nc.nc_android.screen.gamelist;


import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nc.nc_android.MyApplication;
import com.nc.nc_android.R;
import com.nc.nc_android.Utils;
import com.nc.nc_android.custom_view.GameInfoView;
import com.nc.nc_android.dto.GameInstanceDto;
import com.nc.nc_android.dto.TeamResultDto;
import com.nc.nc_android.localdata.UserData;
import com.nc.nc_android.retrofit.OrganizerApi;
import com.nc.nc_android.retrofit.RetrofitSingleton;

import java.text.SimpleDateFormat;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.GameInstanceViewHolder>{

    private String sender;
    List<GameInstanceDto> data;

    GameListClickListener clickListener;

    public GameListAdapter(List<GameInstanceDto> data){
        this.data = data;
    }

    public GameListAdapter(List<GameInstanceDto> data, String sender) {
        this(data);
        this.sender = sender;

    }

    public void setData(List<GameInstanceDto> data){
        this.data = data;
        notifyDataSetChanged();
    }

    public void setOnClickListener(GameListClickListener clickListener){
        this.clickListener = clickListener;
    }

    @Override
    public GameInstanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_game_instance, parent, false);
        GameInstanceViewHolder holder = new GameInstanceViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(GameInstanceViewHolder holder, int position) {
        GameInstanceDto dto = data.get(position);

        holder.tvName.setText(dto.getGameTemplateDto().getName());
        holder.tvGamemode.setText(dto.getGameTemplateDto().getGameMode());
        holder.tvSender.setText(sender);
        holder.tvOverseer.setText(dto.getOverseer());
        holder.tvStartDate.setText(new SimpleDateFormat("dd.MM.yy HH:mm").format(dto.getStartDate()));
        holder.ratingBar.setRating(dto.getGameTemplateDto().getRating() / 2);
        if(dto.isSubscribed()){
            holder.tvSubscribed.setText("Вы подписаны");
        }else{
            holder.tvSubscribed.setText("");
        }

        holder.root.setOnClickListener(v -> {
            if(clickListener != null){
                clickListener.handle(holder, position);
            }
        });


    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public static class GameInstanceViewHolder extends RecyclerView.ViewHolder {

        View root;
        TextView tvName;
        TextView tvGamemode;
        TextView tvSender;
        TextView tvOverseer;
        TextView tvStartDate;
        RatingBar ratingBar;
        TextView tvSubscribed;

        GameInstanceViewHolder(View itemView) {
            super(itemView);

            root = itemView;
            tvName = itemView.findViewById(R.id.tvName);
            tvGamemode = itemView.findViewById(R.id.tvGamemode);
            tvSender = itemView.findViewById(R.id.tvSender);
            tvOverseer = itemView.findViewById(R.id.tvOverseer);
            tvStartDate = itemView.findViewById(R.id.tvStartDate);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            tvSubscribed = itemView.findViewById(R.id.tvSubscribed);

//            itemView.setOnClickListener(v -> {
//                PopupMenu popupMenu = new PopupMenu(activity, itemView);
//                //popupMenu.getMenu().add(Menu.NONE, 1, Menu.NONE, "kekes");
//               // popupMenu.getMenu().add(Menu.NONE, 2, Menu.NONE, "makekes");
//                //popupMenu.setOnMenuItemClickListener(this);
//                popupMenu.inflate(R.menu.popup_gamelist);
//                popupMenu.show();
//            });

        }



    }

    public interface GameListClickListener {
        void handle(GameListAdapter.GameInstanceViewHolder holder, int position);
    }

}
