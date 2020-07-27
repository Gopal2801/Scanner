package com.example.retrofit;

import com.example.model.ScannerReq;
import com.example.model.ScannerResp;
import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    String BASE_URL = "http://52.37.78.247:8081/";


    @Headers("Content-Type: application/json")
    @POST("users/scan")
    Call<ScannerResp> sendData(
            @Body ScannerReq aScan
    );
}
