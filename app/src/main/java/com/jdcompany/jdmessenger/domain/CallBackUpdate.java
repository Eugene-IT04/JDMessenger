package com.jdcompany.jdmessenger.domain;

import com.jdcompany.jdmessenger.data.Message;

import java.util.List;

public interface CallBackUpdate {
    void update(List<Message> messages);
}
