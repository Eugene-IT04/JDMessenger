package com.jdcompany.jdmessenger.domain;

import com.jdcompany.jdmessenger.data.Message;

import java.util.List;

public interface ChatInterface {
    void tryLoadMessages();
    void setCallBackUpdate(CallBackUpdate callBackAdapter);
    void sendTextMessage(String text);
    void addMessages(List<Message> messages);
    List<Message> getMessagesList();
}
