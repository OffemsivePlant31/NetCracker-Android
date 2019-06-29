package com.nc.nc_android.screen.validation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.nc.nc_android.R;
import com.nc.nc_android.Utils;
import com.nc.nc_android.dto.AsyncValidationDto;
import com.nc.nc_android.localdata.UserData;
import com.nc.nc_android.retrofit.OrganizerApi;
import com.nc.nc_android.retrofit.RetrofitSingleton;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ValidationFragment extends Fragment {


    View root;

    RecyclerView recyclerView;
    RecycleAdapter recycleAdapter = null;

    ValidationListAdapter validationListAdapter = null;

    public ValidationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_validation, container, false);

        recyclerView = root.findViewById(R.id.recycleView);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                llm.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        OrganizerApi api = RetrofitSingleton.getInstance(getContext()).getApi(OrganizerApi.class);

        api.getAsyncValidationRequests(UserData.loadUserId()).enqueue(new Callback<List<AsyncValidationDto>>() {
            @Override
            public void onResponse(Call<List<AsyncValidationDto>> call, Response<List<AsyncValidationDto>> response) {
                if((response.code() == 200)&&(response.body() != null)){

                    List<AsyncValidationDto> data = response.body();

//                    recycleAdapter = new RecycleAdapter(data, (state, pos)->{
//
//                        AsyncValidationDto dto = data.get(pos);
//                        dto.setState(state);
//
//                        api.sendAsyncValidationResult(dto).enqueue(Utils.emptyCallback());
//
//                        recycleAdapter.remove(pos);
//                    });
//                    recyclerView.setAdapter(recycleAdapter);

                    validationListAdapter = new ValidationListAdapter(data, (holder, position) -> {
                        AsyncValidationDto dto = data.get(position);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder
                                .setPositiveButton("Ok", (dialog, which) -> {
                                    dto.setState(2);
                                    api.sendAsyncValidationResult(dto).enqueue(Utils.emptyCallback());
                                    data.remove(position);
                                    validationListAdapter.notifyItemRemoved(position);
                                })
                                .setNegativeButton("No", (dialog, which) -> {
                                    dto.setState(1);
                                    api.sendAsyncValidationResult(dto).enqueue(Utils.emptyCallback());
                                    data.remove(position);
                                    validationListAdapter.notifyItemRemoved(position);
                                });
                        final AlertDialog dialog = builder.create();
                        ImageView iv = new ImageView(getContext());
                        iv.setAdjustViewBounds(true);
                        iv.setMaxHeight(500);

                        Picasso.with(getContext()).load(Utils.getImageUrl(dto.getPhoto())).into(iv);
                        dialog.setView(iv);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                        dialog.show();
                    });
                    recyclerView.setAdapter(validationListAdapter);


                }
            }

            @Override
            public void onFailure(Call<List<AsyncValidationDto>> call, Throwable t) {}
        });


        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final int position = viewHolder.getAdapterPosition();
                recycleAdapter.remove(position);
            }

        };


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);

        itemTouchHelper.attachToRecyclerView(recyclerView);




        return root;
    }

}
