package com.ramadhany.vodjo.latihan1.helper;

import com.ramadhany.vodjo.latihan1.Model.GirdBody;
import com.ramadhany.vodjo.latihan1.Model.LoginBody;
import com.ramadhany.vodjo.latihan1.Model.RegisterBody;
import com.ramadhany.vodjo.latihan1.Model.Response.ResponseLogin;
import com.ramadhany.vodjo.latihan1.Model.UserMovBody;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by RaRa on 12/10/2017.
 */

public interface ApiService {

    @POST("login")
    Call<ResponseLogin> loginRequest(@Header("Content-Type") String header,
                                     @Body LoginBody body);


    @POST("register")
    Call<ResponseBody> registerRequest(@Header("Content-Type") String header,
                                       @Body RegisterBody body);

    @POST("usermov")
    Call<ResponseBody> locationRequest(@Header("Content-Type") String header,
                                       @Body UserMovBody body);

    @GET("gridmap")
    Call<List<GirdBody>> getGird();

}
