package com.nc.nc_android.screen.game;



import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nc.nc_android.MainActivity;
import com.nc.nc_android.NamedFragment;
import com.nc.nc_android.R;
import com.nc.nc_android.Utils;
import com.nc.nc_android.adapter.ViewPagerAdapter;
import com.nc.nc_android.custom_view.GameInfoView;
import com.nc.nc_android.dto.GameInstanceDto;
import com.nc.nc_android.localdata.UserData;
import com.nc.nc_android.retrofit.InstanceApi;
import com.nc.nc_android.retrofit.OrganizerApi;
import com.nc.nc_android.retrofit.RetrofitSingleton;
import com.nc.nc_android.screen.validation.ValidationFragment;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class GameFragment extends NamedFragment {

    View root;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    Fragment taskFragment;
    Fragment mapFragment;
    Fragment ratingFragment;
    Fragment validationFragment;

    public GameFragment() {
        //this.setRetainInstance(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(root == null) {

            root = inflater.inflate(R.layout.fragment_game, container, false);
            viewPager = root.findViewById(R.id.viewPager);
            tabLayout = root.findViewById(R.id.tabLayout);
            tabLayout.setupWithViewPager(viewPager);

            taskFragment = new GameTaskFragment();
            mapFragment = new GameMapFragment();
            ratingFragment = new TeamRatingFragment();
            validationFragment = new ValidationFragment();


        }

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        if(UserData.loadBoolean("isOverseer")){
            adapter.addFragment(validationFragment, "Проверка");
        }else{
            adapter.addFragment(taskFragment, "Задача");
        }

        adapter.addFragment(mapFragment, "Карта");
        adapter.addFragment(ratingFragment, "Команды");
        viewPager.setAdapter(adapter);

        return root;
    }


    public void exitButtonHandle() {

        OrganizerApi api = RetrofitSingleton.getInstance(getContext()).getApi(OrganizerApi.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Вы хотите покинуть игру?")
                .setTitle("")
                .setPositiveButton("да", (dialog, id) -> {
                    if (! UserData.loadBoolean("isOverseer")){
                        api.leaveGame(UserData.loadUserId(), UserData.loadGameId()).enqueue(Utils.emptyCallback());
                    }
                    UserData.saveGameId(-1);
                    UserData.saveBoolean("isOverseer", false);
                    ((MainActivity)getActivity()).showHome();
                })
                .setNegativeButton("нет", null);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void aboutButtonHandle() {

        InstanceApi api = RetrofitSingleton.getInstance(getContext()).getApi(InstanceApi.class);

        api.gameInfo(UserData.loadGameId()).enqueue(new Callback<GameInstanceDto>() {
            @Override
            public void onResponse(Call<GameInstanceDto> call, Response<GameInstanceDto> response) {
                if((response.code() == 200)&&(response.body() != null)){

                    GameInstanceDto dto = response.body();
                    GameInfoView gameInfoView = new GameInfoView(getContext(), dto.getGameTemplateDto());
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setView(gameInfoView);
                    builder.create().show();

                }
            }

            @Override
            public void onFailure(Call<GameInstanceDto> call, Throwable t) {

            }
        });


    }

    public void setPage(int pageNumber){
        viewPager.postDelayed(() -> viewPager.setCurrentItem(pageNumber, true), 100);
    }

    @Override
    public String getName() {
        return "Текущая игра";
    }
}
