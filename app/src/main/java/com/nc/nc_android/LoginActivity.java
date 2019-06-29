package com.nc.nc_android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nc.nc_android.dto.PersonDto;
import com.nc.nc_android.localdata.UserData;
import com.nc.nc_android.retrofit.AuthorizationApi;
import com.nc.nc_android.retrofit.RetrofitSingleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail;
    private EditText inputPassword;
    private Button btnLogin;
    private TextView linkSignup;

    Context appContext;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        appContext = this.getApplicationContext();

        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);
        btnLogin = findViewById(R.id.btn_login);
        linkSignup = findViewById(R.id.link_signup);

        btnLogin.setOnClickListener(v -> signIn());
        String savedName = UserData.loadString("name");
        if (savedName != null) {
            inputEmail.setText(savedName);
        }

        if(UserData.loadUserId() != -1){
            Intent intent = new Intent(appContext, MainActivity.class);
            intent.putExtra("page", "welcome");
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void signIn(){

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        //progressDialog.show();

        if(validateFields()){

            validateUser(inputEmail.getText().toString(), inputPassword.getText().toString());

        }
    }

    private boolean validateFields() {
        return true;
    }



    private void validateUser(String email, String password){




        AuthorizationApi api = RetrofitSingleton.getInstance(getApplicationContext()).getApi(AuthorizationApi.class);

        api.auth(email, password).enqueue(new Callback<PersonDto>() {
            @Override
            public void onResponse(Call<PersonDto> call, Response<PersonDto> response) {
                if(response.code() == 200){

                    UserData.saveString("name", inputEmail.getText().toString());
                    UserData.saveUserId(response.body().getId());

                    Intent intent = new Intent(appContext, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<PersonDto> call, Throwable t) {
                //progressDialog.dismiss();
            }
        });

    }

}
