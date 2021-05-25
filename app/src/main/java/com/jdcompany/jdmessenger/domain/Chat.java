package com.jdcompany.jdmessenger.domain;

import android.graphics.Bitmap;
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;

import com.jdcompany.jdmessenger.data.InfoLoader;
import com.jdcompany.jdmessenger.data.network.InternetService;
import com.jdcompany.jdmessenger.data.objects.Message;
import com.jdcompany.jdmessenger.data.objects.User;
import com.jdcompany.jdmessenger.data.callbacks.CallBackSendMessage;
import com.jdcompany.jdmessenger.database.daos.MessageDao;
import com.jdcompany.jdmessenger.domain.callbacks.CallBackFailedSendMessage;
import com.jdcompany.jdmessenger.domain.callbacks.StoreImage;

import io.reactivex.schedulers.Schedulers;

public class Chat {
    User destination;
    MessageDao messageDao;
    IImageConverter imageConverter;
    StoreImage storeImage;

    public Chat(User destination, MessageDao messageDao, IImageConverter imageConverter, StoreImage storeImage) {
        this.destination = destination;
        this.messageDao = messageDao;
        this.imageConverter = imageConverter;
        this.storeImage = storeImage;
    }

    public void sendTextMessage(String text, CallBackFailedSendMessage callBackFailedSendMessage) {
        Message message = createStandardMessage();
        message.setAction(MessageAction.TEXT.toString());
        message.setBody(text);
        InternetService.getInstance().sendMessage(message, new CallBackSendMessage() {
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

    public void sendPicture(Bitmap image, CallBackFailedSendMessage callBackFailedSendMessage){
        Message message = createStandardMessage();
        message.setAction(MessageAction.IMAGE.toString());
        message.setBody(imageConverter.bitmapToString(image));
        InternetService.getInstance().sendMessage(message, new CallBackSendMessage() {
            @Override
            public void onMessageSent(Message message) {
                String imagePath = storeImage.storeImage(image);
                if (imagePath != null) {
                    message.setBody(imagePath);
                    messageDao.insert(message)
                            .subscribeOn(Schedulers.io())
                            .subscribe();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if(callBackFailedSendMessage != null)
                    callBackFailedSendMessage.handle(t);
            }
        });
    }

    public void sendEditTextMessage(String newText, long id, CallBackFailedSendMessage callBackFailedSendMessage){
        Message message = createStandardMessage();
        message.setAction(MessageAction.EDIT_MESSAGE.toString());
        StringBuilder bodyString = new StringBuilder();
        bodyString.append(id);
        bodyString.append(" ");
        bodyString.append(newText);
        message.setBody(bodyString.toString());
        InternetService.getInstance().sendMessage(message, new CallBackSendMessage() {
            @Override
            public void onMessageSent(Message message) {
                messageDao.updateMessageWithId(id, newText)
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

    public void sendDeleteMessageMessage(long id, CallBackFailedSendMessage callBackFailedSendMessage){
        Message message = createStandardMessage();
        message.setAction(MessageAction.DELETE_MESSAGE.toString());
        message.setBody(String.valueOf(id));
        InternetService.getInstance().sendMessage(message, new CallBackSendMessage() {
            @Override
            public void onMessageSent(Message message) {
                messageDao.deleteMessageById(id)
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

    private Message createStandardMessage(){
        Message message = new Message();
        message.setFromId(InfoLoader.getInstance().getCurrentUser().getId());
        message.setToId(destination.getId());
        return message;
    }
}
