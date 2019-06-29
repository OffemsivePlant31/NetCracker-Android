package com.nc.nc_android;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nc.nc_android.adapter.ViewPagerAdapter;

public class GameInfoFragment extends Fragment {

    View root;

    ViewPager pager;
    TabLayout frameLayout;
    ViewPagerAdapter adapter;

    public GameInfoFragment() {

    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        root = inflater.inflate(R.layout.fragment_task_editor, container, false);

        pager = (ViewPager) root.findViewById(R.id.pager_task_editor);
        pager.setAdapter(adapter);

        frameLayout = root.findViewById(R.id.layout_pager);
        frameLayout.setupWithViewPager(pager);

        adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new GameFormFragment(), "Основное");
        adapter.addFragment(new GameEditorFragment(), "Задания");

        pager.setAdapter(adapter);

        return root;
    }

}
