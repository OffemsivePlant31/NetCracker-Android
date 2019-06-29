package com.nc.nc_android.screen.gamelist;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.nc.nc_android.NamedFragment;
import com.nc.nc_android.R;
import com.nc.nc_android.adapter.ViewPagerAdapter;
import com.nc.nc_android.dto.GameInstanceDto;
import com.nc.nc_android.dto.TeamResultDto;
import com.nc.nc_android.localdata.UserData;
import com.nc.nc_android.retrofit.InstanceApi;
import com.nc.nc_android.retrofit.RetrofitSingleton;
import com.nc.nc_android.screen.game.GameInfoFragment;
import com.nc.nc_android.screen.game.GameTaskFragment;
import com.nc.nc_android.screen.game.TeamListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GameListFragment extends NamedFragment {

    View root;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private PublicGamesFragment publicGamesFragment;
    private PrivateGamesFragment privateGamesFragment;

    public GameListFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if(root == null) {

            root = inflater.inflate(R.layout.fragment_gamelist, container, false);
            viewPager = root.findViewById(R.id.viewPager);
            tabLayout = root.findViewById(R.id.tabLayout);
            tabLayout.setupWithViewPager(viewPager);
            ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());

            publicGamesFragment = new PublicGamesFragment();
            privateGamesFragment = new PrivateGamesFragment();

            adapter.addFragment(publicGamesFragment, "Открытые");
            adapter.addFragment(privateGamesFragment, "Приглашения");
            viewPager.setAdapter(adapter);

        }else{
            publicGamesFragment.updateData();
            privateGamesFragment.updateData();
        }

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public String getName() {
        return "Квесты";
    }
}
