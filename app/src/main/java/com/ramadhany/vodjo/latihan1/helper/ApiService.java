package com.ramadhany.vodjo.latihan1.helper;

import com.ramadhany.vodjo.latihan1.Model.LoginBody;
import com.ramadhany.vodjo.latihan1.Model.RegisterBody;
import com.ramadhany.vodjo.latihan1.Model.Response.ResponseLogin;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by RaRa on 12/10/2017.
 */

public interface ApiService {
    // Fungsi ini untuk memanggil API http://192.168.88.20/latihan1/login.php
    @POST("login")
    Call<ResponseLogin> loginRequest(@Header("Content-Type") String header,
                                     @Body LoginBody body);

    // Fungsi ini untuk memanggil API http://192.168.88.20/latihan1/register.php
    @POST("register")
    Call<ResponseBody> registerRequest(@Header("Content-Type") String header,
                                       @Body RegisterBody body);


}
