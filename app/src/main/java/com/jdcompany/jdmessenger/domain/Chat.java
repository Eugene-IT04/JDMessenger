package com.jdcompany.jdmessenger.domain;

import android.icu.text.IDNA;

import com.jdcompany.jdmessenger.data.InfoLoader;
import com.jdcompany.jdmessenger.data.InternetService;
import com.jdcompany.jdmessenger.data.Message;
import com.jdcompany.jdmessenger.data.User;
import com.jdcompany.jdmessenger.data.callbacks.CallBackSendMessage;
import com.jdcompany.jdmessenger.database.AppDatabase;
import com.jdcompany.jdmessenger.database.MessageDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.schedulers.Schedulers;

public class Chat {

    List<Message> messagesList;
    InternetService internetService;
    User destination;
    MessageDao messageDao;

    public Chat(User destination, MessageDao messageDao) {
        this.destination = destination;
        internetService = InternetService.getInstance();
        this.messageDao = messageDao;
    }

    public User getDestination() {
        return destination;
    }

    public void sendTextMessage(String text) {
        internetService = InternetService.getInstance();
        Message message = new Message();
        message.setFromId(InfoLoader.getInstance().getCurrentUser().getId());
        message.setToId(destination.getId());
        message.setAction("text");
        message.setBody(text);
        internetService.sendMessage(message, new CallBackSendMessage() {
            @Override
            public void onMessageSent(Message message) {
                messageDao.insert(message)
                        .subscribeOn(Schedulers.io())
                        .subscribe();
            }

            @Override
            public void onFailure() {

            }
        });
    }

    public boolean isLeftSide(Message message) {
        return message.getFromId() == destination.getId();
    }


}
