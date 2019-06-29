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
import android.widget.FrameLayout;
import android.widget.TextView;

import com.nc.nc_android.adapter.ViewPagerAdapter;
import com.nc.nc_android.task_editor.TaskInfoFragment;
import com.nc.nc_android.task_editor.TaskMapFragment;

public class TaskEditorFragment extends Fragment {

    View root;
    ViewPager pager;
    ViewPagerAdapter adapter;
    TabLayout frameLayout;

    public TaskEditorFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        root = inflater.inflate(R.layout.fragment_task_editor, container, false);

        pager = (ViewPager) root.findViewById(R.id.pager_task_editor);
        pager.setAdapter(adapter);

        frameLayout = root.findViewById(R.id.layout_pager);
        frameLayout.setupWithViewPager(pager);

        adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new TaskInfoFragment(), "Задача");
        adapter.addFragment(new TaskMapFragment(), "Карта");

        pager.setAdapter(adapter);



        return root;
    }
}