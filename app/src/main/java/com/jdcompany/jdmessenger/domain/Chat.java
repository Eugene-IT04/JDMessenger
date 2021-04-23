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

    public Chat(User destination) {
        this.destination = destination;
        internetService = InternetService.getInstance();
        messagesList = new ArrayList<>();
    }

    public User getDestination() {
        return destination;
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
        internetService = InternetService.getInstance();
        Message message;
        message = internetService.sendTextMessage(text, destination);
        messagesList.add(0, message);
        if (callBackUpdate != null)
            callBackUpdate.update();
    }

    @Override
    public List<Message> getMessagesList() {
        return messagesList;
    }

    @Override
    public void addMessages(List<Message> messages) {
        messagesList.addAll(messages);
        sortMessages();
        if (callBackUpdate != null)
            callBackUpdate.update();
    }

    private void sortMessages() {
        messagesList.sort((o1, o2) -> (int) (o2.getTime() - o1.getTime()));
    }

    public boolean isLeftSide(Message message) {
        return message.getFromId() == destination.getId();
    }
}
