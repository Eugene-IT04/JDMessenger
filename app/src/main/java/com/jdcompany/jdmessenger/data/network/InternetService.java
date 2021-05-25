package com.jdcompany.jdmessenger.data.network;

import com.jdcompany.jdmessenger.data.objects.Message;
import com.jdcompany.jdmessenger.data.objects.User;
import com.jdcompany.jdmessenger.data.callbacks.CallBackFindUser;
import com.jdcompany.jdmessenger.data.objects.CallBackInfo;
import com.jdcompany.jdmessenger.data.callbacks.CallBackRegisterUser;
import com.jdcompany.jdmessenger.data.callbacks.CallBackSendMessage;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InternetService {

    private final static InternetService internetService;

    static {
        internetService = new InternetService();
    }

    private InternetService() {
    }

    public static InternetService getInstance() {
        return internetService;
    }


    public void sendMessage(Message message, CallBackSendMessage callBackSendMessage){
        InternetApi.Manager.getInstance().sendMessage(message).enqueue(new Callback<CallBackInfo>() {
            @Override
            public void onResponse(@NotNull Call<CallBackInfo> call, @NotNull Response<CallBackInfo> response) {
                if(callBackSendMessage != null && message != null) {
                    CallBackInfo callBackInfo = response.body();
                    if(callBackInfo == null || callBackInfo.getStatus().equals(ResponseStatus.FAIL.toString())){
                        callBackSendMessage.onFailure(new Throwable("fail from server"));
                        return;
                    }
                    message.setId(callBackInfo.getId());
                    message.setTime(callBackInfo.getTime());
                    callBackSendMessage.onMessageSent(message);
                }
            }

            @Override
            public void onFailure(@NotNull Call<CallBackInfo> call, @NotNull Throwable t) {
                if(callBackSendMessage!= null)
                callBackSendMessage.onFailure(t);
            }
        });
    }

    public void registerUser(@NotNull User user, CallBackRegisterUser callBackRegisterUser) {
        user.setPublicRsa("RSA");
        InternetApi.Manager.getInstance().registerUser(user).enqueue(new Callback<CallBackInfo>() {
            @Override
            public void onResponse(@NotNull Call<CallBackInfo> call, @NotNull Response<CallBackInfo> response) {
                if(callBackRegisterUser != null) {
                    CallBackInfo callBackInfo = response.body();
                    if(callBackInfo == null || callBackInfo.getStatus().equals(ResponseStatus.FAIL.toString())){
                        callBackRegisterUser.onFailure();
                        return;
                    }
                    if (callBackInfo.getStatus().equals(ResponseStatus.SUCCESS.toString())) {
                        user.setId(callBackInfo.getId());
                        callBackRegisterUser.onUserRegistered(user);
                        return;
                    }
                    if(callBackInfo.getStatus().equals(ResponseStatus.TAG_IS_TAKEN.toString())){
                        callBackRegisterUser.onUserTagIsTaken();
                        return;
                    }
                    callBackRegisterUser.onFailure();
                }
            }

            @Override
            public void onFailure(@NotNull Call<CallBackInfo> call, @NotNull Throwable t) {
                if(callBackRegisterUser != null)
                callBackRegisterUser.onFailure();
            }
        });
    }

    public void updateUser(@NotNull User user, CallBackRegisterUser callBackRegisterUser) {
        user.setPublicRsa("RSA");
        InternetApi.Manager.getInstance().updateUser(user, user.getId()).enqueue(new Callback<CallBackInfo>() {
            @Override
            public void onResponse(@NotNull Call<CallBackInfo> call, @NotNull Response<CallBackInfo> response) {
                if(callBackRegisterUser != null) {
                    CallBackInfo callBackInfo = response.body();
                    if(callBackInfo == null || callBackInfo.getStatus().equals(ResponseStatus.FAIL.toString())){
                        callBackRegisterUser.onFailure();
                        return;
                    }
                    if (callBackInfo.getStatus().equals(ResponseStatus.SUCCESS.toString())) {
                        user.setId(callBackInfo.getId());
                        callBackRegisterUser.onUserRegistered(user);
                        return;
                    }
                    if(callBackInfo.getStatus().equals(ResponseStatus.TAG_IS_TAKEN.toString())){
                        callBackRegisterUser.onUserTagIsTaken();
                        return;
                    }
                    callBackRegisterUser.onFailure();
                }
            }

            @Override
            public void onFailure(@NotNull Call<CallBackInfo> call, @NotNull Throwable t) {
                if(callBackRegisterUser != null)
                    callBackRegisterUser.onFailure();
            }
        });
    }

    public void findUser(String tag, CallBackFindUser callBackFindUser){
        InternetApi.Manager.getInstance().getUserByTag(tag).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {
                if(callBackFindUser != null) {
                    User callBackObject = response.body();
                    if (callBackObject != null)
                        callBackFindUser.onUserFound(callBackObject);
                    else callBackFindUser.onUserDoesNotExist();
                }
            }

            @Override
            public void onFailure(@NotNull Call<User> call, @NotNull Throwable t) {
                if(callBackFindUser != null)
                callBackFindUser.onFailure();
            }
        });
    }

    public void findUser(long id, CallBackFindUser callBackFindUser){
        InternetApi.Manager.getInstance().getUserById(id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {
                if(callBackFindUser != null) {
                    User callBackObject = response.body();
                    if (callBackObject != null)
                        callBackFindUser.onUserFound(callBackObject);
                    else callBackFindUser.onUserDoesNotExist();
                }
            }

            @Override
            public void onFailure(@NotNull Call<User> call, @NotNull Throwable t) {
                if(callBackFindUser != null)
                callBackFindUser.onFailure();
            }
        });
    }
}
