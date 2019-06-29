package com.nc.nc_android;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.renderscript.RenderScript;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nc.nc_android.adapter.GameInfoAdapter;
import com.nc.nc_android.behavior.RSBlurProcessor;
import com.nc.nc_android.behavior.RecyclerItemSingleTapListener;
import com.nc.nc_android.dto.GameEditorInfoDto;
import com.nc.nc_android.localdata.UserData;
import com.nc.nc_android.pojo.GameInfo;
import com.nc.nc_android.retrofit.EditorApi;
import com.nc.nc_android.retrofit.RetrofitSingleton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class StoreFragment extends Fragment {


    View root;
    RecyclerView rvTemplates;
    GameInfoAdapter adapter;
    List<GameInfo> games;

    public StoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root = inflater.inflate(R.layout.fragment_store, container, false);

        rvTemplates = root.findViewById(R.id.rv_store);

        initAdapter();

        rvTemplates.setLayoutManager(new LinearLayoutManager(MyApplication.getContext()));

        rvTemplates.addOnItemTouchListener(new RecyclerItemSingleTapListener(MyApplication.getContext(), new RecyclerItemSingleTapListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                RetrofitSingleton.getInstance(MyApplication.getContext()).getApi(EditorApi.class).addTemplateFromStore(UserData.loadUserId(), games.get(position).getId()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        getFragmentManager().popBackStack();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }
        }));

        return root;
    }

    private void initAdapter() {
        RetrofitSingleton.getInstance(MyApplication.getContext()).getApi(EditorApi.class).getTemplatesFromStore(UserData.loadUserId()).enqueue(new Callback<List<GameEditorInfoDto>>() {
            @Override
            public void onResponse(Call<List<GameEditorInfoDto>> call, Response<List<GameEditorInfoDto>> response) {
                List<GameInfo> infos = new ArrayList<>();
                for (GameEditorInfoDto game:response.body()) {
                    infos.add(new GameInfo(game.getName(), game.getDescription(), game.getMode().getName(), game.getMaxPlayers(), game.getId()));
                }
                games = infos;
                adapter = new GameInfoAdapter(MyApplication.getContext(), infos);
                rvTemplates.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<GameEditorInfoDto>> call, Throwable t) {

            }
        });
    }

}
