package com.jdcompany.jdmessenger.domain;

import com.jdcompany.jdmessenger.data.InternetService;
import com.jdcompany.jdmessenger.data.Message;
import com.jdcompany.jdmessenger.data.User;
import com.jdcompany.jdmessenger.data.callbacks.CallBackFindUser;
import com.jdcompany.jdmessenger.database.AppDatabase;
import com.jdcompany.jdmessenger.database.MessageDao;
import com.jdcompany.jdmessenger.database.UserDao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class IncomeMessagesHandler implements IIncomeMessagesHandler {

    UserDao userDao;
    MessageDao messageDao;
    Set<User> knownUsers;
    Disposable disposable;

    @Override
    public void handle(List<Message> messages) {
        messageDao.insert(messages).subscribe();
        for(Message message : messages){
            if(!doesUserWithIdExist(message.getFromId())){
                InternetService.getInstance()
                        .findUser(message.getFromId(), new CallBackFindUser() {
                            @Override
                            public void onUserFound(User user) {
                                userDao.insert(user)
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(() -> {}, e -> {});
                            }

                            @Override
                            public void onUserDoesNotExist() {
                            }

                            @Override
                            public void onFailure() {
                            }
                        });
            }
        }
    }

    boolean doesUserWithIdExist(long id){
        for(User user : knownUsers){
            if(user.getId() == id) return true;
        }
        return false;
    }

    public IncomeMessagesHandler(UserDao userDao, MessageDao messageDao){
        this.userDao = userDao;
        this.messageDao = messageDao;
        knownUsers = new HashSet<>();
        disposable = userDao.getAll()
                .subscribeOn(Schedulers.io())
                .subscribe(incomeList -> knownUsers.addAll(incomeList));
    }

    public void dispose(){
        if(disposable != null && !disposable.isDisposed()) disposable.dispose();
    }
}
