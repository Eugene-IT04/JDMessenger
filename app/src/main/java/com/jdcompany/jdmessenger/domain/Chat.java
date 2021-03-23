package com.jdcompany.jdmessenger.domain;

import com.jdcompany.jdmessenger.data.InternetService;
import com.jdcompany.jdmessenger.data.Message;
import com.jdcompany.jdmessenger.data.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Chat implements ChatInterface {
    List<Message> messagesList;
    InternetService internetService;
    User destination;
    CallBackUpdate callBackUpdate;

    public Chat(User destination){
        this.destination = destination;
        internetService = InternetService.getInstance();
        messagesList = new ArrayList<>();
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
        messagesList.add(0, internetService.sendTextMessage(text, destination));
        if(callBackUpdate != null)
        callBackUpdate.update();
    }

    @Override
    public List<Message> getMessagesList() {
        return messagesList;
    }

    @Override
    public void addMessages(List<Message> messages){
        messages.addAll(messages);
        sortMessages();
        if(callBackUpdate != null)
            callBackUpdate.update();
    }

    private void sortMessages(){
        Collections.sort(messagesList, new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                return (int)(o1.getTime() - o2.getTime());
            }
        });
    }
}
