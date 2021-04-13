package com.jdcompany.jdmessenger.data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

interface InternetApi {

    @POST("/api/messages")
    Call<CallBackInfo> sendMessage(@Body Message message);

    @GET("/api/messages?del=true")
    Call<List<Message>> getMessages(@Query("id") long user_id);

    @POST("api/users")
    Call<CallBackInfo> registerUser(@Body User user);
}
