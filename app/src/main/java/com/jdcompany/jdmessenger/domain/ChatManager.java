package com.jdcompany.jdmessenger.domain;

import com.jdcompany.jdmessenger.data.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatManager {
    private static ChatManager chatManager;
    private List<Chat> chatList;

    private ChatManager(){}

    public static synchronized ChatManager getInstance(){
        if(chatManager == null){
            chatManager = new ChatManager();
            chatManager.chatList = new ArrayList<>();
        }
        return chatManager;
    }

    public void updateMessages(List<Message> messageList){
        //TODO
        if(chatList.get(0) != null) chatList.get(0).addMessages(messageList);
    }

    public List<Chat> getChatList(){
        return chatList;
    }
}
