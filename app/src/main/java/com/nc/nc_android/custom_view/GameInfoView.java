package com.nc.nc_android.custom_view;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.nc.nc_android.R;
import com.nc.nc_android.dto.GameTemplateDto;


public class GameInfoView extends CoordinatorLayout {

    GameTemplateDto dto;

    public GameInfoView(Context context, GameTemplateDto dto) {
        super(context);
        this.dto = dto;

        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.alert_gameinfo, this, true);

        ((TextView) findViewById(R.id.tvAlertName)).setText(dto.getName());
        ((TextView) findViewById(R.id.tvAlertDescription)).setText(dto.getDescription());
        ((TextView) findViewById(R.id.tvAlertGamemode)).setText(dto.getGameMode());
        ((TextView) findViewById(R.id.tvAlertAuthor)).setText(dto.getAuthor());

        findViewById(R.id.tvAlertSubscribed).setVisibility(View.GONE);

    }

    public void setSubscribed(boolean subscribed){
        if(subscribed){
            findViewById(R.id.tvAlertSubscribed).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.tvAlertSubscribed).setVisibility(View.GONE);
        }
    }

}
