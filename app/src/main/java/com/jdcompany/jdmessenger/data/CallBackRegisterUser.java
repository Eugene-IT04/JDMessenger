package com.jdcompany.jdmessenger.data;

public interface CallBackRegisterUser {
    void onUserRegistered(User user);

    void onUserTagIsTaken();

    void onFailure();
}