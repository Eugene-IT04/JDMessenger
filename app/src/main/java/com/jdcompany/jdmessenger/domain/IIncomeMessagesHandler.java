package com.jdcompany.jdmessenger.domain;

import com.jdcompany.jdmessenger.data.objects.Message;

import java.util.List;

public interface IIncomeMessagesHandler {
    void handle(List<Message> messages);
}
