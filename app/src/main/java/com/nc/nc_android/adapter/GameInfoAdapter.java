package com.nc.nc_android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nc.nc_android.R;
import com.nc.nc_android.pojo.GameInfo;

import java.util.List;

/**
 * Created by fastn on 26.11.2017.
 */

public class GameInfoAdapter extends RecyclerView.Adapter<GameInfoAdapter.ViewHolder> {
    @Override
    public GameInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.game_info_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(GameInfoAdapter.ViewHolder viewHolder, int position) {
        GameInfo gameInfo = mGames.get(position);

        TextView nameTextView = viewHolder.nameTextView;
        nameTextView.setText(gameInfo.getName());
        TextView modeTextView = viewHolder.modeTextView;
        modeTextView.setText(gameInfo.getMode());
        TextView descTextView = viewHolder.descTextView;
        descTextView.setText(gameInfo.getDescription());
    }

    @Override
    public int getItemCount() {
        return mGames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView modeTextView;
        public TextView descTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.game_name);
            modeTextView = (TextView) itemView.findViewById(R.id.game_mode);
            descTextView = (TextView) itemView.findViewById(R.id.game_desc);
        }
    }

    private List<GameInfo> mGames;
    private Context mContext;

    public GameInfoAdapter(Context context, List<GameInfo> games) {
        mGames = games;
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }
}
