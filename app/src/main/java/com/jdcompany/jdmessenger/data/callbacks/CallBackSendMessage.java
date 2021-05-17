package com.jdcompany.jdmessenger.data.callbacks;

import com.jdcompany.jdmessenger.data.objects.Message;

public interface CallBackSendMessage {

    void onMessageSent(Message message);

    void onFailure(Throwable t);
}
