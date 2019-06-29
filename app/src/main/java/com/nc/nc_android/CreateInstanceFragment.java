package com.nc.nc_android;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TimePicker;

import com.nc.nc_android.dto.CreateGameDto;
import com.nc.nc_android.dto.PersonDto;
import com.nc.nc_android.localdata.UserData;
import com.nc.nc_android.retrofit.EditorApi;
import com.nc.nc_android.retrofit.RetrofitSingleton;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateInstanceFragment extends Fragment {

    View root;

    CheckBox isPublic;
    DatePicker datePicker;
    TimePicker timePicker;
    ListView users;
    EditorApi api;
    List<PersonDto> userList;
    ArrayAdapter<String> adapter;
    List<Long> selected;
    Button create;

    public CreateInstanceFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root = inflater.inflate(R.layout.fragment_create_instance, container, false);

        users = root.findViewById(R.id.lv_users);
        isPublic = root.findViewById(R.id.cb_public);
        datePicker = root.findViewById(R.id.date_picker);
        timePicker = root.findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
        create = root.findViewById(R.id.btn_create_instance);

        api = RetrofitSingleton.getInstance(MyApplication.getContext()).getApi(EditorApi.class);

        getAllUsers();

        selected = new ArrayList<>();

        users.setVisibility(View.INVISIBLE);

        isPublic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked) {
                    users.setVisibility(View.VISIBLE);
                } else {
                    users.setVisibility(View.INVISIBLE);
                }
            }
        });

        users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(Long pos:selected){
                    if (pos == userList.get(position).getId()){
                        selected.remove(pos);
                        view.setBackgroundColor(Color.WHITE);
                        return;
                    }
                }
                selected.add(userList.get(position).getId());
                view.setBackgroundColor(Color.GRAY);
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateGameDto createGameDto = new CreateGameDto();
                createGameDto.setPublic(isPublic.isChecked());
                createGameDto.setDay(datePicker.getDayOfMonth());
                createGameDto.setHour(timePicker.getCurrentHour());
                createGameDto.setMinute(timePicker.getCurrentMinute());
                createGameDto.setMonth(datePicker.getMonth());
                createGameDto.setYear(datePicker.getYear());
                List<PersonDto> allowed = new ArrayList<>();
                for (Long id:selected) {
                    for(PersonDto personDto:userList){
                        if (personDto.getId() == id){
                            allowed.add(personDto);
                        }
                    }
                }
                createGameDto.setUsers(allowed);
                api.createGameInstance(getActivity().getIntent().getLongExtra("gameId", 0), UserData.loadUserId(), createGameDto).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        getFragmentManager().popBackStack();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }
        });

        return root;
    }

    private void getAllUsers() {
        api.getUsers().enqueue(new Callback<List<PersonDto>>() {
            @Override
            public void onResponse(Call<List<PersonDto>> call, Response<List<PersonDto>> response) {
                userList = response.body();
                List<String> names = new ArrayList<>();
                for (PersonDto person:userList) {
                    names.add(person.getName());
                }
                adapter = new ArrayAdapter<String>(MyApplication.getContext(), android.R.layout.simple_list_item_1, names);
                users.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<PersonDto>> call, Throwable t) {

            }
        });
    }

}
