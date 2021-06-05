package com.jdcompany.jdmessenger.data.network;

import com.jdcompany.jdmessenger.data.objects.Message;
import com.jdcompany.jdmessenger.data.objects.User;
import com.jdcompany.jdmessenger.data.objects.CallBackInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
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

    @POST("api/users")
    Call<CallBackInfo> updateUser(@Body User user, @Query("update") long id);

    @GET("/api/users")
    Call<User> getUserById(@Query("id") long id);

    @GET("/api/users")
    Call<User> getUserByTag(@Query("tag") String tag);

    class Manager{
        private static final String BASE_URL = "http://3.142.181.116:8000";
        private static final InternetApi internetApi;

        static {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            internetApi = retrofit.create(InternetApi.class);
        }

        static InternetApi getInstance(){
            return internetApi;
        }
    }
}
