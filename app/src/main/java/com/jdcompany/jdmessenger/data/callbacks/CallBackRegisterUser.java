package com.jdcompany.jdmessenger.data.callbacks;

import com.jdcompany.jdmessenger.data.objects.User;

public interface CallBackRegisterUser {

    void onUserRegistered(User user);

    void onUserTagIsTaken();

    void onFailure();
}