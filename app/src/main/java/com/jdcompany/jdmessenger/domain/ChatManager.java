package com.jdcompany.jdmessenger.domain;

import com.jdcompany.jdmessenger.data.Message;

import java.util.List;

public class ChatManager {
    private static ChatManager chatManager;

    private ChatManager(){}

    public static synchronized ChatManager getInstance(){
        if(chatManager == null){
            chatManager = new ChatManager();
        }
        return chatManager;
    }

    public void updateMessages(List<Message> messageList){

    }
}
