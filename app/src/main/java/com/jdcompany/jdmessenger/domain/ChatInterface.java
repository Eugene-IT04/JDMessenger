package com.jdcompany.jdmessenger.domain;

import androidx.recyclerview.widget.RecyclerView;

import com.jdcompany.jdmessenger.data.Message;

import java.util.List;

public interface ChatInterface {
    void tryLoadMessages();
    void setCallBackAdapter(RecyclerView.Adapter<?> adapter);
    void sendTextMessage(String text);
    List<Message> getMessagesList();
}
