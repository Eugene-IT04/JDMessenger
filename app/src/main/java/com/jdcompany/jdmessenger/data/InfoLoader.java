package com.jdcompany.jdmessenger.data;

public class InfoLoader {
    private static InfoLoader infoLoader;
    private InfoLoader(){}
    private User currentUser;

    public synchronized static InfoLoader getInstance(){
        if(infoLoader == null){
            infoLoader = new InfoLoader();
        }
        return infoLoader;
    }

    //FIXME
    public void setCurrentUser(User user){
        currentUser = user;
    }

    public User getCurrentUser(){
        return currentUser;
    }
}
