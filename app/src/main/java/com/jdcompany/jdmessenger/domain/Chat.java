package com.jdcompany.jdmessenger.domain;

import com.jdcompany.jdmessenger.data.InfoLoader;
import com.jdcompany.jdmessenger.data.InternetService;
import com.jdcompany.jdmessenger.data.Message;
import com.jdcompany.jdmessenger.data.User;
import com.jdcompany.jdmessenger.data.callbacks.CallBackSendMessage;
import com.jdcompany.jdmessenger.database.MessageDao;
import com.jdcompany.jdmessenger.domain.callbacks.CallBackFailedSendMessage;

import io.reactivex.schedulers.Schedulers;

public class Chat {
    User destination;
    MessageDao messageDao;

    public Chat(User destination, MessageDao messageDao) {
        this.destination = destination;
        this.messageDao = messageDao;
    }

    public void sendTextMessage(String text, CallBackFailedSendMessage callBackFailedSendMessage) {
        InternetService internetService = InternetService.getInstance();
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
            public void onFailure(Throwable t) {
                if(callBackFailedSendMessage != null)
                callBackFailedSendMessage.handle(t);
            }
        });
    }

    public boolean isLeftSide(Message message) {
        return message.getFromId() == destination.getId();
    }
}
