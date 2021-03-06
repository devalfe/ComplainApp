package com.example.peter.workcomplain;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.peter.workcomplain.Utils.ApiUtils;
import com.example.peter.workcomplain.retrofit.ApiService;
import com.example.peter.workcomplain.retrofit.model.LoginDataResponse;
import com.example.peter.workcomplain.retrofit.model.LoginResponse;
import com.pixplicity.easyprefs.library.Prefs;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.username) EditText edUsername;
    @BindView(R.id.password) EditText edPassword;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;
    ApiService mApiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mApiService = ApiUtils.getApiService();
    }

    @OnClick(R.id.loginbt) void loginUser(){
        String user = edUsername.getText().toString().trim();
        String pass= edPassword.getText().toString().trim();
        startLogin("","");
        if (!user.isEmpty() && !pass.isEmpty()){
            startLogin(user, pass);
        }else {
            Toast.makeText(this, "Check that All Credentials are entered", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick({R.id.signup})void signUp(){
        startActivity(new Intent(MainActivity.this,RegisterActivity.class));
    }
    private void startLogin(final String user, String pass) {
        mProgressBar.setVisibility(View.VISIBLE);
        mApiService.getLoginResponse(user,pass).enqueue(new Callback<LoginDataResponse>() {
            @Override
            public void onResponse(Call<LoginDataResponse> call, Response<LoginDataResponse> response) {
                if (response.isSuccessful()){
                    mProgressBar.setVisibility(View.GONE);
                    if (response.body().getResultMsg().equals("success")){
                        Toast.makeText(MainActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                        Prefs.putString("user",response.body().getName());
                        Prefs.putString("department",response.body().getDepartment());
                        Prefs.putString("email",response.body().getAuthorization());
                        startActivity(new Intent(MainActivity.this,DashboardActivity.class));
                    }else {
                        mProgressBar.setVisibility(View.GONE);
                        //Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginDataResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
       //startActivity(new Intent(this,DashboardActivity.class));
    }
}
