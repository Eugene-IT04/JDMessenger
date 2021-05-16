package com.jdcompany.jdmessenger.data;

import androidx.annotation.NonNull;

import com.jdcompany.jdmessenger.data.callbacks.CallBackFindUser;
import com.jdcompany.jdmessenger.data.callbacks.CallBackInfo;
import com.jdcompany.jdmessenger.data.callbacks.CallBackMessagesReceived;
import com.jdcompany.jdmessenger.data.callbacks.CallBackRegisterUser;
import com.jdcompany.jdmessenger.data.callbacks.CallBackSendMessage;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class InternetService {
    private static InternetService internetService;
    private static String BASE_URL = "http://34.78.104.83:8000";

    private InternetApi internetApi;
    private ScheduledExecutorService scheduledExecutorService;
    private CallBackMessagesReceived callBackMessagesReceived;

    private InternetService() {
    }

    public static InternetService getInstance() {
        if (internetService == null) {
            throw new IllegalStateException("InternetService is not initialized");
        }
        return internetService;
    }

    public static synchronized void startService(@NonNull CallBackMessagesReceived callBackMessagesReceived) {
        if (internetService == null) {
            internetService = new InternetService();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            internetService.internetApi = retrofit.create(InternetApi.class);
            internetService.scheduledExecutorService = Executors.newScheduledThreadPool(1);
            internetService.scheduledExecutorService.scheduleWithFixedDelay(internetService::updateMessages, 0, 1, TimeUnit.SECONDS);
            internetService.callBackMessagesReceived = callBackMessagesReceived;
        }
    }


    public void sendMessage(Message message, @NonNull CallBackSendMessage callBackSendMessage){
        internetApi.sendMessage(message).enqueue(new Callback<CallBackInfo>() {
            @Override
            public void onResponse(Call<CallBackInfo> call, Response<CallBackInfo> response) {
                if(callBackSendMessage != null && message != null) {
                    CallBackInfo callBackInfo = response.body();
                    if(callBackInfo.getStatus().equals("fail")){
                        callBackSendMessage.onFailure(new Throwable("fail from server"));
                        return;
                    }
                    message.setId(callBackInfo.getId());
                    message.setTime(callBackInfo.getTime());
                    callBackSendMessage.onMessageSent(message);
                }
            }

            @Override
            public void onFailure(Call<CallBackInfo> call, Throwable t) {
                if(callBackSendMessage!= null)
                callBackSendMessage.onFailure(t);
            }
        });
    }

    public void tryRegisterUser(User user, @NonNull CallBackRegisterUser callBackRegisterUser) {
        user.setPublicRsa("hui");
        internetApi.registerUser(user).enqueue(new Callback<CallBackInfo>() {
            @Override
            public void onResponse(Call<CallBackInfo> call, Response<CallBackInfo> response) {
                if(callBackRegisterUser != null && user != null) {
                    CallBackInfo callBackInfo = response.body();
                    if (callBackInfo.getStatus().equals("success")) {
                        user.setId(callBackInfo.getId());
                        callBackRegisterUser.onUserRegistered(user);
                    } else callBackRegisterUser.onUserTagIsTaken();
                }
            }

            @Override
            public void onFailure(Call<CallBackInfo> call, Throwable t) {
                if(callBackRegisterUser != null)
                callBackRegisterUser.onFailure();
            }
        });
    }

    public void findUser(String tag, @NonNull CallBackFindUser callBackFindUser){
        internetApi.getUserByTag(tag).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(callBackFindUser != null) {
                    Object callBackObject = response.body();
                    if (callBackObject instanceof User)
                        callBackFindUser.onUserFound((User) callBackObject);
                    else callBackFindUser.onUserDoesNotExist();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if(callBackFindUser != null)
                callBackFindUser.onFailure();
            }
        });
    }

    public void findUser(long id, @NonNull CallBackFindUser callBackFindUser){
        internetApi.getUserById(id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(callBackFindUser != null) {
                    Object callBackObject = response.body();
                    if (callBackObject instanceof User)
                        callBackFindUser.onUserFound((User) callBackObject);
                    else callBackFindUser.onUserDoesNotExist();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if(callBackFindUser != null)
                callBackFindUser.onFailure();
            }
        });
    }

    void updateMessages() {
        try {
            Response<List<Message>> response = internetApi.getMessages(InfoLoader.getInstance().getCurrentUser().getId()).execute();
            List<Message> messages = response.body();
            if (messages != null && messages.size() > 0)
                callBackMessagesReceived.updateMessages(messages);
        } catch (Exception e) {
        }
    }

    private interface InternetApi {

        @POST("/api/messages")
        Call<CallBackInfo> sendMessage(@Body Message message);

        @GET("/api/messages?del=true")
        Call<List<Message>> getMessages(@Query("id") long user_id);

        @POST("api/users")
        Call<CallBackInfo> registerUser(@Body User user);

        @GET("/api/users")
        Call<User> getUserById(@Query("id") long id);

        @GET("/api/users")
        Call<User> getUserByTag(@Query("tag") String tag);
    }
}
