package com.nc.nc_android;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.nc.nc_android.dto.CreateGameInstanceRequest;
import com.nc.nc_android.localdata.UserData;
import com.nc.nc_android.service.OrganizerService;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class DemoButtonsFragment extends Fragment {

    View root;

    EditText etGameId;
    EditText etPersonId;
    Button btnStart;
    TextView tvResponse;

    EditText etDate;
    EditText etTime;

    EditText etGameId1;
    EditText etPersonId1;
    Button btnSubscribe;
    TextView tvResponse1;

    OrganizerService organizerService;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public DemoButtonsFragment() {

        organizerService = new OrganizerService();



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_demo_buttons, container, false);


        etGameId = root.findViewById(R.id.etGameId);
        etPersonId = root.findViewById(R.id.etPersonId);
        btnStart = root.findViewById(R.id.btnStart);
        tvResponse = root.findViewById(R.id.tvResponse);

        etDate = root.findViewById(R.id.etDate);
        etTime = root.findViewById(R.id.etTime);

        etGameId1 = root.findViewById(R.id.etGameId1);
        etPersonId1 = root.findViewById(R.id.etPersonId1);
        btnSubscribe = root.findViewById(R.id.btnSubscribe);
        tvResponse1 = root.findViewById(R.id.tvResponse1);

        etPersonId.setText(String.valueOf(UserData.loadUserId()));


        etDate.setOnClickListener(v->{

            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(),
                    (view, year, monthOfYear, dayOfMonth) ->
                            etDate.setText((monthOfYear + 1) + "." + dayOfMonth + "." + year)
                    , mYear, mMonth, mDay);
            datePickerDialog.show();
        });

        etTime.setOnClickListener(v->{
            final Calendar c = Calendar.getInstance();
            int mHour = c.get(Calendar.HOUR_OF_DAY);
            int mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    getContext(),
                    (view, hourOfDay, minute) ->
                            etTime.setText(hourOfDay + ":" + minute)
                    , mHour, mMinute
                    , true);
            timePickerDialog.show();
        });

        btnStart.setOnClickListener(v -> {

            long gameId = Integer.valueOf(etGameId.getText().toString());
            long personId = Integer.valueOf(etPersonId.getText().toString());
            Date startDate = null;
            try {
                startDate = dateFormat.parse(etDate.getText().toString() + " " + etTime.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Call<Void> res = organizerService.start(new CreateGameInstanceRequest(gameId, personId, startDate));

            res.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    tvResponse.setText(String.valueOf(response.code()));

                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    tvResponse.setText(t.getMessage());
                }
            });

        });

        btnSubscribe.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder
                    .setPositiveButton("Get Pro", (dialog, which) -> {})
                    .setNegativeButton("No thanks", (dialog, which) -> {});
            final AlertDialog dialog = builder.create();
            ImageView iv = new ImageView(getContext());
            iv.setAdjustViewBounds(true);
            iv.setMaxHeight(500);

            Picasso.with(getContext()).load(Utils.getImageUrl(1)).into(iv);
            dialog.setView(iv);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.show();


        });



        return root;
    }

}
