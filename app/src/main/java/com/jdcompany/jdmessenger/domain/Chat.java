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
    InternetApi internetApi;
    User destination;
    User currentUser;

    public Chat(User destination){
        this.destination = destination;
        currentUser = InfoLoader.getInstance().getCurrentUser();
        internetApi = InternetService.getInternetApi();
    }

    @Override
    public void tryLoadMessages() {
        //TODO
    }

    @Override
    public void setCallBackAdapter(RecyclerView.Adapter<?> adapter) {
        this.adapter = adapter;
    }

    @Override
    public void sendTextMessage(String text) {

    }

    @Override
    public List<Message> getMessagesList() {
        return null;
    }
}
