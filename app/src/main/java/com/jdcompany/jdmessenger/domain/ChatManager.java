package com.jdcompany.jdmessenger.domain;

import com.jdcompany.jdmessenger.data.Message;
import com.jdcompany.jdmessenger.data.User;

import java.util.ArrayList;
import java.util.List;

public class ChatManager {
    private static ChatManager chatManager;
    private List<Chat> chatList;

    private ChatManager(){}

    //FIXME
    public static synchronized ChatManager getInstance(User destination){
        if(chatManager == null){
            chatManager = new ChatManager();
            chatManager.chatList = new ArrayList<>();
            chatManager.chatList.add(new Chat(destination));
        }
        return chatManager;
    }

    public void updateMessages(List<Message> messageList){
        //FIXME
        chatList.get(0).addMessages(messageList);
    }

    public List<Chat> getChatList(){
        return chatList;
    }
}
