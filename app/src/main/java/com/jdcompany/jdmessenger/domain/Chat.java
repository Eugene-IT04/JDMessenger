package com.jdcompany.jdmessenger.domain;

import androidx.recyclerview.widget.RecyclerView;

import com.jdcompany.jdmessenger.data.InfoLoader;
import com.jdcompany.jdmessenger.data.InternetApi;
import com.jdcompany.jdmessenger.data.InternetService;
import com.jdcompany.jdmessenger.data.Message;
import com.jdcompany.jdmessenger.data.User;

import java.util.List;

public class Chat implements ChatInterface {
    RecyclerView.Adapter<?> adapter;
    List<Message> messagesList;
    InternetService internetService;
    User destination;
    CallBackUpdate callBackUpdate;

    public Chat(User destination){
        this.destination = destination;
        internetService = InternetService.getInstance();
    }

    @Override
    public void tryLoadMessages() {
        //TODO
    }

    @Override
    public void setCallBackUpdate(CallBackUpdate callBackUpdate) {
        this.callBackUpdate = callBackUpdate;
    }

    @Override
    public void sendTextMessage(String text) {
        messagesList.add(internetService.sendTextMessage(text, destination));
        callBackUpdate.update();
    }

    @Override
    public List<Message> getMessagesList() {
        return messagesList;
    }
}
