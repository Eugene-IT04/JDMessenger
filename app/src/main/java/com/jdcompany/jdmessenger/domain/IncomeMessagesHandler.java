package com.jdcompany.jdmessenger.domain;

import com.jdcompany.jdmessenger.data.network.InternetService;
import com.jdcompany.jdmessenger.data.objects.Message;
import com.jdcompany.jdmessenger.data.objects.User;
import com.jdcompany.jdmessenger.data.callbacks.CallBackFindUser;
import com.jdcompany.jdmessenger.database.daos.MessageDao;
import com.jdcompany.jdmessenger.database.daos.UserDao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class IncomeMessagesHandler implements IIncomeMessagesHandler {

    UserDao userDao;
    MessageDao messageDao;
    Set<User> knownUsers;
    Set<Long> idsUsersToFind;
    CompositeDisposable compositeDisposable;

    @Override
    public void handle(List<Message> messages) {
        List<Message> messagesToSave = new ArrayList<>();

        //handle income messages
        for(Message message : messages){
            if(!doesUserWithIdExist(message.getFromId())){
                idsUsersToFind.add(message.getFromId());
            }

            String messageAction = message.getAction();

            if(messageAction.equals(MessageAction.TEXT.toString())) {
                messagesToSave.add(message);
            }
            else if(messageAction.equals(MessageAction.DELETE_MESSAGE.toString())){
                messageDao.deleteMessageById(message.getId()).subscribe();
            }
            else if(messageAction.equals(MessageAction.EDIT_MESSAGE.toString())){
                int separatorIndex = message.getBody().indexOf(" ");
                if(separatorIndex != -1){
                    try {
                        long id = Long.parseLong(message.getBody().substring(0, separatorIndex));
                        String newBody = message.getBody().substring(separatorIndex + 1);
                        messageDao.updateMessageWithId(id, newBody).subscribe();
                    } catch (Exception e){}
                }

            }

        }

        //get unknown users' profiles
        for(Long id : idsUsersToFind){
            findUserWithId(id);
        }

        //save messages in database
        messageDao.insert(messagesToSave).subscribe();
    }

    private boolean doesUserWithIdExist(long id){
        for(User user : knownUsers){
            if(user.getId() == id) return true;
        }
        return false;
    }

    private void findUserWithId(long id){
        InternetService.getInstance().findUser(id, new CallBackFindUser() {
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

    public IncomeMessagesHandler(UserDao userDao, MessageDao messageDao){
        this.userDao = userDao;
        this.messageDao = messageDao;
        knownUsers = new HashSet<>();
        idsUsersToFind = new HashSet<>();
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(userDao.getAll()
                .subscribeOn(Schedulers.io())
                .subscribe(incomeList -> knownUsers.addAll(incomeList)));
    }

    public void dispose(){
        if(compositeDisposable != null && !compositeDisposable.isDisposed()) compositeDisposable.dispose();
    }
}
