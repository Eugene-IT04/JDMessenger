package com.jdcompany.jdmessenger.domain.callbacks;

public interface CallBackFailedSendMessage {
    void handle(Throwable t);
}
