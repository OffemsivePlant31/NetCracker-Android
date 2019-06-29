package com.nc.nc_android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nc.nc_android.dto.GameEditorInfoDto;
import com.nc.nc_android.localdata.UserData;
import com.nc.nc_android.retrofit.EditorApi;
import com.nc.nc_android.retrofit.RetrofitSingleton;
import com.nc.nc_android.service.EditorService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditorFragment extends Fragment {
    View root;

    Button editButton;
    Button newButton;
    TextView selectedItem;
    ListView itemsList;

    EditorApi api;

    int selected = -1;

    ArrayAdapter<String> myAdapter;
    List<String> userGames;
    List<GameEditorInfoDto> userGamesData;

    public EditorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_editor, container, false);

        editButton = (Button) root.findViewById(R.id.btnEditSelected);
        newButton = (Button) root.findViewById(R.id.btnNewGame);
        selectedItem = (TextView) root.findViewById(R.id.tvSelection);
        itemsList = (ListView) root.findViewById(R.id.lvUserGames);

        api = RetrofitSingleton.getInstance(root.getContext()).getApi(EditorApi.class);

        api.getUserTemplates(UserData.loadUserId()).enqueue(new Callback<List<GameEditorInfoDto>>() {
            @Override
            public void onResponse(Call<List<GameEditorInfoDto>> call, Response<List<GameEditorInfoDto>> response) {
                userGames = new ArrayList<>();
                try {
                    for (GameEditorInfoDto game : response.body()) {
                        if (game.getName() != null){
                            userGames.add(game.getName());
                        } else {
                            userGames.add("empty" + game.getId());
                        }
                    }
                    userGamesData = response.body();
                    myAdapter = new ArrayAdapter<String>(root.getContext(), android.R.layout.simple_list_item_1, userGames);
                    itemsList.setAdapter(myAdapter);
                }
                catch (Exception e){
                    selectedItem.setText(response.toString());
                }
            }

            @Override
            public void onFailure(Call<List<GameEditorInfoDto>> call, Throwable t) {
                selectedItem.setText(call.request().toString());
                String[] temp = new String[]{"Something", "Went", "Wrong", "Please", "Come", "Later", "Test", "Text", "Text", "Text", "Text", "Text", "Text", "Text", "Text", "Text", "Text", "Text", "Text", "Text"};
                myAdapter = new ArrayAdapter<String>(root.getContext(), android.R.layout.simple_list_item_1, temp);
                itemsList.setAdapter(myAdapter);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected != -1){
                    Intent intent = new Intent(MyApplication.getContext(), EditorActivity.class);
                    intent.putExtra("gameId", userGamesData.get(selected).getId());
                    startActivity(intent);
                }
            }
        });

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    api.createNewEmptyGame(UserData.loadUserId()).enqueue(new Callback<Long>() {
                        @Override
                        public void onResponse(Call<Long> call, Response<Long> response) {
                            Intent intent = new Intent(MyApplication.getContext(), EditorActivity.class);
                            intent.putExtra("gameId", response.body());
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<Long> call, Throwable t) {

                        }
                    });
            }
        });

        itemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(selected == -1){
                    selected = i;
                    view.setSelected(true);
                    selectedItem.setText(userGames.get(selected));
                }
                else if(selected == i){
                    selectedItem.setText("Nothing selected");
                    selected = -1;
                    view.setSelected(false);
                }
                else{
                    String temp = userGames.get(selected);
                    GameEditorInfoDto tempGame = userGamesData.get(selected);
                    userGames.set(selected, userGames.get(i));
                    userGamesData.set(selected, userGamesData.get(i));
                    userGames.set(i, temp);
                    userGamesData.set(i, tempGame);
                    myAdapter.notifyDataSetChanged();
                    selected = -1;
                    selectedItem.setText("Nothing selected");
                }
            }
        });

        return root;
    }
}
