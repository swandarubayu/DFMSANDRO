package com.ramadhany.vodjo.latihan1.View;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ramadhany.vodjo.latihan1.Model.RegisterBody;
import com.ramadhany.vodjo.latihan1.R;
import com.ramadhany.vodjo.latihan1.helper.ApiService;
import com.ramadhany.vodjo.latihan1.helper.UtilsApi;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DaftarActivity extends AppCompatActivity {

    EditText etUsername;
    EditText etPassword;
    EditText etDomisili;
    EditText edtNama;

    Button btnRegister;
    TextView txtLogin;
    ProgressDialog loading;

    Context mContext;
    ApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);
        mContext = this;
        mApiService = UtilsApi.getAPIService();
        // methode ini berfungsi untuk mendeklarasikan widget yang ada
        // di layout activity_daftar
        initComponents();

    }

    private void initComponents() {
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etDomisili = (EditText) findViewById(R.id.etDomisili);
        edtNama = findViewById(R.id.edtNama);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        txtLogin = (TextView) findViewById(R.id.txtLogin);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                requestRegister();
            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }

    private void requestRegister() {
        RegisterBody body = new RegisterBody(etUsername.getText().toString(),etPassword.getText().toString(),etDomisili.getText().toString(), edtNama.getText().toString());
            mApiService.registerRequest("application/json", body)

                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()){
                                Toast.makeText(mContext, "Berhasil", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "Error : " + response.code(), Toast.LENGTH_SHORT).show();
                            }
                            loading.dismiss();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                            loading.dismiss();
                            Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
