package com.ramadhany.vodjo.latihan1.View;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ramadhany.vodjo.latihan1.MainActivity;
import com.ramadhany.vodjo.latihan1.Model.LoginBody;
import com.ramadhany.vodjo.latihan1.Model.RegisterBody;
import com.ramadhany.vodjo.latihan1.Model.Response.ResponseLogin;
import com.ramadhany.vodjo.latihan1.R;
import com.ramadhany.vodjo.latihan1.helper.ApiService;
import com.ramadhany.vodjo.latihan1.helper.UtilsApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername;
    EditText etPassword;
    Button btnLogin;
    Button btnRegister;
    ProgressDialog loading;

    Context mContext;
    ApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        mApiService = UtilsApi.getAPIService(); // meng-init yang ada di package apihelper
        initComponents();
    }

    private void initComponents() {
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                requestLogin();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, DaftarActivity.class));
            }
        });
    }

    private void requestLogin() {
        Log.d("Doing", "requesst");
        LoginBody body = new LoginBody(etUsername.getText().toString(),etPassword.getText().toString());
        mApiService.loginRequest("application/json", body)
                    .enqueue(new Callback<ResponseLogin>() {
                        @Override
                        public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                            Log.d("Doing", "res code :" + response.code() );
                            if (response.isSuccessful()){
                                if (!response.body().error){
                                    Toast.makeText(mContext, "Login berhasil", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    Toast.makeText(mContext, "Username tidak ditemukan", Toast.LENGTH_SHORT).show();
                                }
                                loading.dismiss();
                            } else {
                                loading.dismiss();
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseLogin> call, Throwable t) {
                            Log.e("debug", "onFailure: ERROR > " + t.toString());
                            loading.dismiss();
                        }
                    });
        }
    }

