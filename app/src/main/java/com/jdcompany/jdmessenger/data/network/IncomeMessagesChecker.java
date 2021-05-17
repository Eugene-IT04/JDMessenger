package com.jdcompany.jdmessenger.data.network;

import com.jdcompany.jdmessenger.data.InfoLoader;
import com.jdcompany.jdmessenger.data.objects.Message;
import com.jdcompany.jdmessenger.data.callbacks.CallBackMessagesReceived;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import retrofit2.Response;

public class IncomeMessagesChecker {

    private final CallBackMessagesReceived callBackMessagesReceived;
    private final ScheduledExecutorService scheduledExecutorService;
    private volatile boolean isPaused = false;
    public static final int delay = 1;

    public IncomeMessagesChecker(CallBackMessagesReceived callBackMessagesReceived){
        this.callBackMessagesReceived = callBackMessagesReceived;
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
    }

    public void start(){
        scheduledExecutorService.scheduleWithFixedDelay(this::updateMessagesLoop, 0, delay, TimeUnit.SECONDS);
    }

    public void resume(){
        isPaused = false;
    }

    public void pause(){
        isPaused = true;
    }

    public void terminate(){
        scheduledExecutorService.shutdownNow();
    }

    private void updateMessagesLoop(){
        if(!isPaused) {
            try {
                Response<List<Message>> response = InternetApi.Manager.getInstance()
                        .getMessages(InfoLoader.getInstance().getCurrentUser().getId()).execute();
                List<Message> messages = response.body();
                if (messages != null && messages.size() > 0)
                    callBackMessagesReceived.updateMessages(messages);
            } catch (Exception e) {
            }
        }
    }
}
