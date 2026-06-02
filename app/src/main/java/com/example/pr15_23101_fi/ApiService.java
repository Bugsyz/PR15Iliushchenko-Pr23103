package com.example.pr15_23101_fi;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("feelings")
    Call<ApiResponse<FeelingItem>> getFeelings();

    @GET("quotes")
    Call<ApiResponse<QuoteItem>> getQuotes();

    @POST("user/login")
    Call<LoginResponse> login(@Body LoginRequest request);
}