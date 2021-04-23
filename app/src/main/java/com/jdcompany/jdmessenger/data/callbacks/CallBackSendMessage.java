package com.jdcompany.jdmessenger.data.callbacks;

import com.jdcompany.jdmessenger.data.Message;

public interface CallBackSendMessage {

    void onMessageSent(Message message);

    void onFailure();
}
