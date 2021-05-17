package com.jdcompany.jdmessenger.domain;

import com.jdcompany.jdmessenger.data.network.InternetService;
import com.jdcompany.jdmessenger.data.objects.Message;
import com.jdcompany.jdmessenger.data.objects.User;
import com.jdcompany.jdmessenger.data.callbacks.CallBackFindUser;
import com.jdcompany.jdmessenger.database.daos.MessageDao;
import com.jdcompany.jdmessenger.database.daos.UserDao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class IncomeMessagesHandler implements IIncomeMessagesHandler {

    UserDao userDao;
    MessageDao messageDao;
    Set<User> knownUsers;
    CompositeDisposable compositeDisposable;

    @Override
    public void handle(List<Message> messages) {
        messageDao.insert(messages).subscribe();
        for(Message message : messages){
            if(!doesUserWithIdExist(message.getFromId())){
                InternetService.getInstance()
                        .findUser(message.getFromId(), new CallBackFindUser() {
                            @Override
                            public void onUserFound(User user) {
                                compositeDisposable.add(userDao.insert(user)
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(() -> {}, e -> {}));
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
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(userDao.getAll()
                .subscribeOn(Schedulers.io())
                .subscribe(incomeList -> knownUsers.addAll(incomeList)));
    }

    public void dispose(){
        if(compositeDisposable != null && !compositeDisposable.isDisposed()) compositeDisposable.dispose();
    }
}
