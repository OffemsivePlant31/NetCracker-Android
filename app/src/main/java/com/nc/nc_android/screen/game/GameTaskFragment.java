package com.nc.nc_android.screen.game;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nc.nc_android.R;
import com.nc.nc_android.dto.GameTaskDto;
import com.nc.nc_android.dto.TaskValidationRequest;
import com.nc.nc_android.dto.TaskValidationResponse;
import com.nc.nc_android.localdata.UserData;
import com.nc.nc_android.retrofit.OrganizerApi;
import com.nc.nc_android.retrofit.RetrofitSingleton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LOCATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameTaskFragment extends Fragment {

    private final String LOCAL_PHOTO_PATH = Environment.getExternalStorageDirectory() + "/TakenFromCamera.jpg";

    private View root;

    private GameTaskDto currentTask;

    private ValidationWaitTask validationWaitTask;

    OrganizerApi api;

    TextView tvPointName;
    TextView tvPointDescription;
    TextView tvFindCondition;
    TextView tvCheckCondition;
    EditText etCode;
    Button btnValidate;
    ImageView ivPhoto;

    EditText etLatitude;
    EditText etLongitude;
    CheckBox cbLocation;

    CardView cardValidate;
    CardView cardValidateWait;
    CardView cardGameEnd;
    CardView cardDescription;
    CardView cardQuest;

    boolean photoCreated;

    public GameTaskFragment() {
        api = RetrofitSingleton.getInstance(getContext()).getApi(OrganizerApi.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_game_task, container, false);



        tvPointName = root.findViewById(R.id.tvPointName);
        tvPointDescription = root.findViewById(R.id.tvPointDescription);
        tvFindCondition = root.findViewById(R.id.tvFindCondition);
        tvCheckCondition = root.findViewById(R.id.tvCheckCondition);
        etCode = root.findViewById(R.id.etCode);
        btnValidate = root.findViewById(R.id.btnValidate);
        ivPhoto = root.findViewById(R.id.ivPhoto);

        etLatitude = root.findViewById(R.id.etLatitude);
        etLongitude = root.findViewById(R.id.etLongitude);
        cbLocation= root.findViewById(R.id.cbLocation);

        cardValidate = root.findViewById(R.id.cardValidate);
        cardValidateWait = root.findViewById(R.id.cardValidateWait);
        cardGameEnd = root.findViewById(R.id.cardGameEnd);
        cardDescription = root.findViewById(R.id.cardDescription);
        cardQuest = root.findViewById(R.id.cardQuest);

        etCode.setVisibility(View.GONE);
        ivPhoto.setVisibility(View.GONE);
        cardValidateWait.setVisibility(View.GONE);
        cardGameEnd.setVisibility(View.GONE);


        btnValidate.setOnClickListener(v -> {

            if(("photo".equals(currentTask.getCheckConditionType()))&&(!photoCreated)) return;

            byte[] byteArray = null;
            if(photoCreated){
                Bitmap bitmap = ((BitmapDrawable)ivPhoto.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byteArray = stream.toByteArray();
            }

            ValidationTask task = new ValidationTask();
            task.execute(byteArray,
                    etCode.getText().toString(),
                    etLatitude.getText().toString(),
                    etLongitude.getText().toString(),
                    cbLocation.isChecked());
        });

        ivPhoto.setOnClickListener(v -> {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, new File(LOCAL_PHOTO_PATH).toURI());
            startActivityForResult(intent, 1313);

        });

        updateUI();

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);


        if (requestCode == 1313 && resultCode == Activity.RESULT_OK) {

            photoCreated = true;
            Bitmap bitmap = (Bitmap) intent.getExtras().get("data");
            ivPhoto.setImageBitmap(bitmap);

        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(validationWaitTask != null){
            validationWaitTask.cancel(true);
        }
    }

    public void updateUI(){



        api.getNextTask(UserData.loadUserId(), UserData.loadGameId()).enqueue(new Callback<GameTaskDto>() {
            @Override
            public void onResponse(Call<GameTaskDto> call, Response<GameTaskDto> response) {


                if(response.code() == 404){
                    cardGameEnd.setVisibility(View.VISIBLE);

                    cardValidateWait.setVisibility(View.GONE);
                    cardValidate.setVisibility(View.GONE);
                    cardDescription.setVisibility(View.GONE);
                    cardQuest.setVisibility(View.GONE);
                    ((GameFragment) getParentFragment()).setPage(2);
                    return;
                }

                currentTask = response.body();

                tvPointName.setText(currentTask.getPoint().getName());
                tvPointDescription.setText(currentTask.getPoint().getDescription());
                tvFindCondition.setText(currentTask.getFindCondition());
                tvCheckCondition.setText(currentTask.getCheckCondition());

                if((currentTask.getCheckConditionType() != null)&&(currentTask.getCheckConditionType().equals("code"))){
                    etCode.setVisibility(View.VISIBLE);
                }else{
                    etCode.setVisibility(View.GONE);
                }

                if((currentTask.getCheckConditionType() != null)&&(currentTask.getCheckConditionType().equals("photo"))){
                    ivPhoto.setVisibility(View.VISIBLE);
                }else{
                    ivPhoto.setVisibility(View.GONE);
                }

                if(currentTask.isInValidation()){
                    cardValidate.setVisibility(View.GONE);
                    cardValidateWait.setVisibility(View.VISIBLE);

                    validationWaitTask = new ValidationWaitTask();
                    validationWaitTask.execute();
                }

            }

            @Override
            public void onFailure(Call<GameTaskDto> call, Throwable t) {}
        });
    }


    @Override
    public void onResume() {
        super.onResume();


    }


    private Location getLastKnownLocation() {

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation;
    }


    class ValidationTask extends AsyncTask<Object, Object, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            cardValidate.setVisibility(View.GONE);
            cardValidateWait.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String message) {
            super.onPostExecute(message);

            if("async".equals(message)){
                validationWaitTask = new ValidationWaitTask();
                validationWaitTask.execute();
                return;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(message)
                    .setTitle("Ответ")
                    .setPositiveButton("ok", (dialog, id) -> onResume());
            AlertDialog dialog = builder.create();
            dialog.show();

            cardValidate.setVisibility(View.VISIBLE);
            cardValidateWait.setVisibility(View.GONE);

            updateUI();
        }

        @Override
        protected String doInBackground(Object[] params) {

            byte[] byteArray = (byte[]) params[0];
            String code = (String) params[1];
            String latitude = (String) params[2];
            String longitude = (String) params[3];
            boolean emulateLocation = (boolean) params[4];

            TaskValidationRequest request = new TaskValidationRequest();

            if(emulateLocation){
                request.setLatitude(Float.valueOf(latitude));
                request.setLongitude(Float.valueOf(longitude));
            }else{
                Location location = getLastKnownLocation();
                if(location == null){
                    return null;
                }
                request.setLatitude((float) location.getLatitude());
                request.setLongitude((float) location.getLongitude());
            }

            if("photo".equals(currentTask.getCheckConditionType())){
                long photo = 0;
                try {
                    Response<Long> response = api.sendPhoto(byteArray).execute();
                    if((response.code() == 200)&&(response.body() != null)){
                        photo = response.body();
                    }
                } catch (IOException e) {e.printStackTrace();}

                request.setPhotoId(photo);
            }

            if("code".equals(currentTask.getCheckConditionType())){
                request.setCode(code);
            }

            String message = "Всё плохо.";

            try {
                Response<TaskValidationResponse> response =
                        api.validateTask(request, UserData.loadUserId(), UserData.loadGameId()).execute();
                if((response.code() == 200)&&(response.body() != null)) {
                    TaskValidationResponse taskValidationResponse = response.body();

                    message = taskValidationResponse.getMessage();
                }
            } catch (IOException e) {e.printStackTrace();}



            return message;
        }

    }

    class ValidationWaitTask extends AsyncTask<Object, Object, TaskValidationResponse>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(TaskValidationResponse response) {
            super.onPostExecute(response);

            if(response == null) return;

            String message;
            if(response.getMessage() == null){
                if(response.getCode() == 2){
                    message = "Все ок";
                }else{
                    message = "Неверно";
                }
            }else{
                message = response.getMessage();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(message)
                    .setTitle("Ответ")
                    .setPositiveButton("ok", (dialog, id) -> onResume());
            AlertDialog dialog = builder.create();
            dialog.show();

            cardValidate.setVisibility(View.VISIBLE);
            cardValidateWait.setVisibility(View.GONE);

            updateUI();
        }

        @Override
        protected TaskValidationResponse doInBackground(Object[] objects) {

            m1:while(true){

                try {

                    Thread.sleep(3000);

                    Response<TaskValidationResponse> response = api.getAsyncValidationResult(currentTask.getId()).execute();
                    if(response.body() != null){

                        if(response.body().getCode() == 0){
                            continue m1;
                        }else{
                            return response.body();
                        }


                    }

                } catch (InterruptedException e) {
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }

}
